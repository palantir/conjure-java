/*
 * (c) Copyright 2019 Palantir Technologies Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.palantir.conjure.java.services.dialogue;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.ByteStreams;
import com.google.common.util.concurrent.MoreExecutors;
import com.palantir.conjure.defs.Conjure;
import com.palantir.conjure.java.EteTestServer;
import com.palantir.conjure.java.FeatureFlags;
import com.palantir.conjure.java.TestBase;
import com.palantir.conjure.java.config.ssl.SslSocketFactories;
import com.palantir.conjure.java.services.UndertowServiceGenerator;
import com.palantir.conjure.java.types.ObjectGenerator;
import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.conjure.java.undertow.lib.HandlerContext;
import com.palantir.conjure.java.undertow.lib.SerializerRegistry;
import com.palantir.conjure.java.undertow.runtime.ConjureHandler;
import com.palantir.conjure.java.undertow.runtime.Serializers;
import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.dialogue.OkHttpChannel;
import com.palantir.product.BlockingDialogueSampleService;
import com.palantir.product.Complex;
import com.palantir.product.DialogueDialogueSampleService;
import com.palantir.product.DialogueSampleServiceEndpoint;
import com.palantir.tokens.auth.AuthHeader;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public final class DialogueClientGeneratorEteTest extends TestBase {

    @ClassRule
    public static final TemporaryFolder folder = new TemporaryFolder();

    private static Undertow server;
    private static BlockingDialogueSampleService client;

    @BeforeClass
    public static void beforeClass() throws IOException {
        ConjureDefinition def = Conjure.parse(ImmutableList.of(
                new File("src/test/resources/dialogue-sample-service.yml")));
        // generate and create test server
        List<Path> files = new UndertowServiceGenerator(ImmutableSet.of(FeatureFlags.UndertowServicePrefix))
                .emit(def, folder.getRoot());
        validateGeneratorOutput(files, Paths.get("src/integrationInput/java/com/palantir/product"));
        SerializerRegistry serializers = new SerializerRegistry(Serializers.json(), Serializers.cbor());
        HandlerContext context = HandlerContext.builder()
                .serializerRegistry(serializers)
                .build();
        ConjureHandler handler = new ConjureHandler();
        List<Endpoint> endpoints = ImmutableList.of(DialogueSampleServiceEndpoint.of(new DialogueTestResource()));
        endpoints.forEach(endpoint -> endpoint.create(context).register(handler));
        server = Undertow.builder()
                .setServerOption(UndertowOptions.DECODE_URL, false)
                .addHttpListener(8080, "0.0.0.0")
                .setHandler(Handlers.path().addPrefixPath("/test-example/api", handler))
                .build();
        server.start();

        // generate and create client
        files = new ObjectGenerator().emit(def, folder.getRoot());
        validateGeneratorOutput(files, Paths.get("src/integrationInput/java/com/palantir/product"));
        files = new DialogueClientGenerator().emit(def, folder.getRoot());
        validateGeneratorOutput(files, Paths.get("src/integrationInput/java/com/palantir/product"));
        client = DialogueDialogueSampleService.blocking(
                createChannel(url("localhost", 8080, "/test-example/api"), Duration.ofSeconds(10)));

    }


    @AfterClass
    public static void after() {
        if (server != null) {
            server.stop();
        }
    }

    @Test
    public void returnsData() throws IOException {
        assertThat(client.integer(DialogueTestResource.EXPECTED_AUTH)).isEqualTo(DialogueTestResource.INT);
        assertThat(client.integerEcho(DialogueTestResource.EXPECTED_AUTH, 84)).isEqualTo(84);
        assertThat(client.string(DialogueTestResource.EXPECTED_AUTH)).isEqualTo(DialogueTestResource.STRING);
        assertThat(client.stringEcho(DialogueTestResource.EXPECTED_AUTH, "84")).isEqualTo("84");
        assertThat(client.queryEcho(DialogueTestResource.EXPECTED_AUTH, 84)).isEqualTo("84");
        assertThat(client.complex(DialogueTestResource.EXPECTED_AUTH))
                .isEqualTo(Complex.of(DialogueTestResource.STRING, DialogueTestResource.INT));
        assertThat(client.complexEcho(DialogueTestResource.EXPECTED_AUTH,
                Complex.of(DialogueTestResource.STRING, DialogueTestResource.INT)))
                .isEqualTo(Complex.of(DialogueTestResource.STRING, DialogueTestResource.INT));
        assertThat(ByteStreams.toByteArray(client.binaryEcho(DialogueTestResource.EXPECTED_AUTH, "84")))
                .isEqualTo("84".getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void rethrowsExceptions() {
        assertThatThrownBy(() -> client.integer(AuthHeader.valueOf("bogus")))
                .isInstanceOf(RuntimeException.class);
    }

    private static OkHttpChannel createChannel(URL url, Duration timeout) {
        return OkHttpChannel.of(
                new OkHttpClient.Builder()
                        .protocols(ImmutableList.of(Protocol.HTTP_1_1))
                        // Execute calls on same thread so that async tests are deterministic.
                        .dispatcher(new Dispatcher(MoreExecutors.newDirectExecutorService()))
                        .connectTimeout(timeout.toMillis(), TimeUnit.MILLISECONDS)
                        .readTimeout(timeout.toMillis(), TimeUnit.MILLISECONDS)
                        .writeTimeout(timeout.toMillis(), TimeUnit.MILLISECONDS)
                        .sslSocketFactory(
                                SslSocketFactories.createSslSocketFactory(EteTestServer.TRUST_STORE_CONFIGURATION),
                                SslSocketFactories.createX509TrustManager(EteTestServer.TRUST_STORE_CONFIGURATION))
                        .build(),
                url);
    }

    private static URL url(String host, int port, String path) {
        try {
            return new URL("http", host, port, path);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Failed to create URL", e);
        }
    }
}
