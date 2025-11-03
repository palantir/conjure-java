/*
 * (c) Copyright 2025 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java.endpointerrors;

import static com.palantir.conjure.java.EteTestServer.clientConfiguration;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.google.common.collect.Iterables;
import com.palantir.conjure.java.TestBase;
import com.palantir.conjure.java.api.errors.RemoteException;
import com.palantir.conjure.java.undertow.runtime.ConjureHandler;
import com.palantir.dialogue.clients.DialogueClients;
import com.palantir.tokens.auth.AuthHeader;
import endpointerrors.com.palantir.product.ErrorServiceBlocking;
import endpointerrors.com.palantir.product.ErrorServiceBlocking.TestBasicErrorErrors;
import endpointerrors.com.palantir.product.ErrorServiceEndpoints;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import io.undertow.server.HttpHandler;
import java.net.InetSocketAddress;
import java.util.Optional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@Execution(ExecutionMode.CONCURRENT)
public final class UndertowServiceEteTest extends TestBase {
    private static final AuthHeader AUTH_HEADER = AuthHeader.valueOf("authHeader");
    private static Undertow server;
    private static int port;
    private final ErrorServiceBlocking errorServiceClient;

    public UndertowServiceEteTest() {
        this.errorServiceClient = DialogueClients.create(ErrorServiceBlocking.class, clientConfiguration(port));
    }

    @BeforeAll
    public static void before() {

        HttpHandler handler = ConjureHandler.builder()
                .services(ErrorServiceEndpoints.of(new ErrorResource.Impl()))
                .build();

        server = Undertow.builder()
                .setServerOption(UndertowOptions.DECODE_URL, false)
                .addHttpListener(0, "0.0.0.0")
                .setHandler(Handlers.path().addPrefixPath("/test-example/api", handler))
                .build();
        server.start();
        port = ((InetSocketAddress)
                        Iterables.getOnlyElement(server.getListenerInfo()).getAddress())
                .getPort();
    }

    @AfterAll
    public static void after() {
        if (server != null) {
            server.stop();
        }
    }

    @Test
    public void test_endpoint_error_utility() {
        try {
            errorServiceClient.testMultipleErrorsAndPackages(AUTH_HEADER, Optional.of("invalidArgument"));
        } catch (RemoteException e) {
            TestBasicErrorErrors error = ErrorServiceBlocking.TestBasicErrorErrors.from(e);
            assertThat(error).isInstanceOfSatisfying(TestBasicErrorErrors.InvalidArgument.class, invalidArgument -> {
                assertThat(invalidArgument.exception().error().parameters().field())
                        .isEqualTo("field");
                assertThat(invalidArgument.exception().error().parameters().value())
                        .isEqualTo("value");
            });
        }
    }
}
