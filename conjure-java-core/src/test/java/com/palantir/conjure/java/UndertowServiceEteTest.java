/*
 * (c) Copyright 2018 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java;

import static com.palantir.conjure.java.EteTestServer.clientConfiguration;
import static com.palantir.conjure.java.EteTestServer.clientUserAgent;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.palantir.conjure.defs.Conjure;
import com.palantir.conjure.java.client.jaxrs.JaxRsClient;
import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.conjure.java.okhttp.HostMetricsRegistry;
import com.palantir.conjure.java.serialization.ObjectMappers;
import com.palantir.conjure.java.services.UndertowServiceGenerator;
import com.palantir.conjure.java.undertow.lib.HandlerContext;
import com.palantir.conjure.java.undertow.lib.SerializerRegistry;
import com.palantir.conjure.java.undertow.runtime.ConjureHandler;
import com.palantir.conjure.java.undertow.runtime.Serializers;
import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.product.EmptyPathService;
import com.palantir.product.EmptyPathServiceEndpoint;
import com.palantir.product.EteService;
import com.palantir.product.EteServiceEndpoint;
import com.palantir.product.StringAliasExample;
import com.palantir.ri.ResourceIdentifier;
import com.palantir.tokens.auth.AuthHeader;
import io.undertow.Handlers;
import io.undertow.Undertow;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public final class UndertowServiceEteTest extends TestBase {
    private static final ObjectMapper CLIENT_OBJECT_MAPPER = ObjectMappers.newClientObjectMapper();

    @ClassRule
    public static final TemporaryFolder folder = new TemporaryFolder();

    private static Undertow server;

    private final EteService client;

    public UndertowServiceEteTest() {
        client = JaxRsClient.create(
                EteService.class,
                clientUserAgent(),
                new HostMetricsRegistry(),
                clientConfiguration());
    }

    @BeforeClass
    public static void before() {
        SerializerRegistry serializers = new SerializerRegistry(Serializers.json(), Serializers.cbor());
        HandlerContext context = HandlerContext.builder()
                .serializerRegistry(serializers)
                .build();

        ConjureHandler handler = new ConjureHandler();
        EteServiceEndpoint.of(new UndertowEteResource()).create(context).register(handler);
        EmptyPathServiceEndpoint.of(() -> true).create(context).register(handler);

        server = Undertow.builder()
                .addHttpListener(8080, "0.0.0.0")
                .setHandler(Handlers.path().addPrefixPath("/test-example/api", handler))
                .build();
        server.start();
    }

    @AfterClass
    public static void after() {
        if (server != null) {
            server.stop();
        }
    }

    @Test
    public void jaxrs_client_can_make_a_call_to_an_empty_path() throws Exception {
        EmptyPathService emptyPathClient = JaxRsClient.create(
                EmptyPathService.class,
                clientUserAgent(),
                new HostMetricsRegistry(),
                clientConfiguration());
        assertThat(emptyPathClient.emptyPath()).isEqualTo(true);
    }

    @Test
    public void client_can_retrieve_a_string_from_a_server() throws Exception {
        assertThat(client.string(AuthHeader.valueOf("authHeader")))
                .isEqualTo("Hello, world!");
    }

    @Test
    public void client_can_retrieve_a_double_from_a_server() throws Exception {
        assertThat(client.double_(AuthHeader.valueOf("authHeader")))
                .isEqualTo(1 / 3d);
    }

    @Test
    public void client_can_retrieve_a_boolean_from_a_server() throws Exception {
        assertThat(client.boolean_(AuthHeader.valueOf("authHeader")))
                .isEqualTo(true);
    }

    @Test
    public void client_can_retrieve_a_safelong_from_a_server() throws Exception {
        assertThat(client.safelong(AuthHeader.valueOf("authHeader")))
                .isEqualTo(SafeLong.of(12345));
    }

    @Test
    public void client_can_retrieve_an_rid_from_a_server() throws Exception {
        assertThat(client.rid(AuthHeader.valueOf("authHeader")))
                .isEqualTo(ResourceIdentifier.of("ri.foundry.main.dataset.1234"));
    }

    @Test
    public void client_can_retrieve_an_optional_string_from_a_server() throws Exception {
        assertThat(client.optionalString(AuthHeader.valueOf("authHeader")))
                .isEqualTo(Optional.of("foo"));
    }

    @Test
    public void jaxrs_client_can_retrieve_an_optional_empty_from_a_server() throws Exception {
        assertThat(client.optionalEmpty(AuthHeader.valueOf("authHeader")))
                .isEqualTo(Optional.empty());
    }

    @Test
    public void client_can_retrieve_a_date_time_from_a_server() throws Exception {
        assertThat(client.datetime(AuthHeader.valueOf("authHeader")))
                .isEqualTo(OffsetDateTime.ofInstant(Instant.ofEpochMilli(1234), ZoneId.from(ZoneOffset.UTC)));
    }

    @Test
    public void java_url_client_receives_ok_with_complete_request() throws IOException {
        HttpURLConnection httpUrlConnection = preparePostRequest();
        httpUrlConnection.setRequestProperty("Authorization", "Bearer authheader");
        sendPostRequestData(
                httpUrlConnection,
                CLIENT_OBJECT_MAPPER.writeValueAsString(StringAliasExample.of("foo")));
        assertThat(httpUrlConnection.getResponseCode()).isEqualTo(200);
    }

    @Test
    public void java_url_client_receives_bad_request_without_authheader() throws IOException {
        HttpURLConnection httpUrlConnection = preparePostRequest();
        sendPostRequestData(
                httpUrlConnection,
                CLIENT_OBJECT_MAPPER.writeValueAsString(StringAliasExample.of("foo")));
        assertThat(httpUrlConnection.getResponseCode()).isEqualTo(400);
    }

    @Test
    @Ignore // Results in 500 response code
    public void java_url_client_receives_unprocessable_entity_with_null_body() throws IOException {
        HttpURLConnection httpUrlConnection = preparePostRequest();
        httpUrlConnection.setRequestProperty("Authorization", "Bearer authheader");
        sendPostRequestData(httpUrlConnection, "");
        assertThat(httpUrlConnection.getResponseCode()).isEqualTo(422);
    }

    @BeforeClass
    public static void beforeClass() throws IOException {
        ConjureDefinition def = Conjure.parse(
                ImmutableList.of(new File("src/test/resources/ete-service.yml")));
        List<Path> files = new UndertowServiceGenerator(ImmutableSet.of(FeatureFlags.UndertowServicePrefix))
                .emit(def, folder.getRoot());

        for (Path file : files) {
            Path output = Paths.get("src/integrationInput/java/com/palantir/product/" + file.getFileName());
            if (Boolean.valueOf(System.getProperty("recreate", "false"))) {
                Files.deleteIfExists(output);
                Files.copy(file, output);
            }

            assertThat(readFromFile(file)).isEqualTo(readFromFile(output));
        }
    }

    private static HttpURLConnection preparePostRequest() throws IOException {
        URL url = new URL("http://0.0.0.0:8080/test-example/api/base/notNullBody");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        con.setRequestProperty("Content-Type", "application/json");
        return con;
    }

    private static void sendPostRequestData(HttpURLConnection connection, String data) throws IOException {
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.write(data.getBytes(StandardCharsets.UTF_8));
        out.flush();
        out.close();
    }
}
