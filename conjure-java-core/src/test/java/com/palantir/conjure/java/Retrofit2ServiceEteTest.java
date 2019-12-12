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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.util.concurrent.Futures;
import com.palantir.conjure.defs.Conjure;
import com.palantir.conjure.java.client.retrofit2.Retrofit2Client;
import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.conjure.java.okhttp.HostMetricsRegistry;
import com.palantir.conjure.java.services.Retrofit2ServiceGenerator;
import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.product.EteServiceRetrofit;
import com.palantir.ri.ResourceIdentifier;
import com.palantir.tokens.auth.AuthHeader;
import io.dropwizard.Configuration;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.api.parallel.ResourceLock;

@Execution(ExecutionMode.CONCURRENT)
@ExtendWith(DropwizardExtensionsSupport.class)
@ResourceLock("port:8080")
public final class Retrofit2ServiceEteTest extends TestBase {

    @TempDir
    public static File folder;

    public static final DropwizardAppExtension<Configuration> RULE = new DropwizardAppExtension<>(EteTestServer.class);

    private final EteServiceRetrofit client;

    public Retrofit2ServiceEteTest() {
        client = Retrofit2Client.create(
                EteServiceRetrofit.class, clientUserAgent(), new HostMetricsRegistry(), clientConfiguration());
    }

    @Disabled // string returns in Jersey should use a mandated wrapper alias type
    @Test
    public void retrofit2_can_retrieve_a_string_from_a_server() {
        assertThat(Futures.getUnchecked(client.string(AuthHeader.valueOf("authHeader"))))
                .isEqualTo("Hello, world!");
    }

    @Test
    public void retrofit2_client_can_retrieve_a_double_from_a_server() {
        assertThat(Futures.getUnchecked(client.double_(AuthHeader.valueOf("authHeader"))))
                .isEqualTo(1 / 3d);
    }

    @Test
    public void retrofit2_client_can_retrieve_a_boolean_from_a_server() {
        assertThat(Futures.getUnchecked(client.boolean_(AuthHeader.valueOf("authHeader"))))
                .isEqualTo(true);
    }

    @Test
    public void retrofit2_client_can_retrieve_a_safelong_from_a_server() {
        assertThat(Futures.getUnchecked(client.safelong(AuthHeader.valueOf("authHeader"))))
                .isEqualTo(SafeLong.of(12345));
    }

    @Test
    public void retrofit2_client_can_retrieve_an_rid_from_a_server() {
        assertThat(Futures.getUnchecked(client.rid(AuthHeader.valueOf("authHeader"))))
                .isEqualTo(ResourceIdentifier.of("ri.foundry.main.dataset.1234"));
    }

    @Disabled("string returns in Jersey should use a mandated wrapper alias type")
    @Test
    public void retrofit2_client_can_retrieve_an_optional_string_from_a_server() {
        assertThat(Futures.getUnchecked(client.optionalString(AuthHeader.valueOf("authHeader"))))
                .isEqualTo(Optional.of("foo"));
    }

    @Disabled("https://github.com/palantir/conjure-java-runtime/issues/668")
    @Test
    public void retrofit2_client_can_retrieve_an_optional_empty_from_a_server() {
        assertThat(Futures.getUnchecked(client.optionalEmpty(AuthHeader.valueOf("authHeader"))))
                .isNotPresent();
    }

    @Test
    public void retrofit2_client_can_retrieve_a_date_time_from_a_server() {
        assertThat(Futures.getUnchecked(client.datetime(AuthHeader.valueOf("authHeader"))))
                .isEqualTo(OffsetDateTime.ofInstant(Instant.ofEpochMilli(1234), ZoneId.from(ZoneOffset.UTC)));
    }

    @Test
    public void retrofit2_client_can_retrieve_binary_data_from_a_server() throws IOException {
        assertThat(Futures.getUnchecked(client.binary(AuthHeader.valueOf("authHeader"))).string())
                .isEqualTo("Hello, world!");
    }

    @BeforeAll
    public static void beforeAll() throws IOException {
        ConjureDefinition def = Conjure.parse(ImmutableList.of(
                new File("src/test/resources/ete-service.yml"), new File("src/test/resources/ete-binary.yml")));
        List<Path> files = new Retrofit2ServiceGenerator(ImmutableSet.of()).emit(def, folder);
        validateGeneratorOutput(files, Paths.get("src/integrationInput/java/com/palantir/product"));
    }
}
