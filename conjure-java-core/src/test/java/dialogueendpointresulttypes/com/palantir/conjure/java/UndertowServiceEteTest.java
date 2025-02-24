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

package dialogueendpointresulttypes.com.palantir.conjure.java;

import static com.palantir.conjure.java.EteTestServer.clientConfiguration;
import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.Iterables;
import com.palantir.conjure.java.TestBase;
import com.palantir.conjure.java.undertow.runtime.ConjureHandler;
import com.palantir.dialogue.clients.DialogueClients;
import com.palantir.product.ErrorServiceEndpoints;
import com.palantir.tokens.auth.AuthHeader;
import dialogueendpointresulttypes.com.palantir.product.EndpointSpecificErrors;
import dialogueendpointresulttypes.com.palantir.product.EndpointSpecificTwoErrors;
import dialogueendpointresulttypes.com.palantir.product.ErrorServiceBlocking;
import dialogueendpointresulttypes.com.palantir.product.ErrorServiceBlocking.TestBasicErrorResponse;
import dialogueendpointresulttypes.com.palantir.product.ErrorServiceBlocking.TestBinaryResponse;
import dialogueendpointresulttypes.com.palantir.product.ErrorServiceBlocking.TestEmptyBodyResponse;
import dialogueendpointresulttypes.com.palantir.product.ErrorServiceBlocking.TestImportedErrorResponse;
import dialogueendpointresulttypes.com.palantir.product.ErrorServiceBlocking.TestMultipleErrorsAndPackagesResponse;
import dialogueendpointresulttypes.com.palantir.product.ErrorServiceBlocking.TestOptionalBinaryResponse;
import dialogueendpointresulttypes.com.palantir.product.OptionalBinaryResponseMode;
import dialogueendpointresulttypes.com.palantir.product.TestErrors;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import io.undertow.server.HttpHandler;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
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
    public void error_client_returns_basic_result() {
        TestBasicErrorResponse result = errorServiceClient.testBasicError(AUTH_HEADER, false);
        assertThat(result)
                .isInstanceOfSatisfying(TestBasicErrorResponse.Success.class, success -> assertThat(success.value())
                        .isEqualTo(ErrorResource.Impl.SUCCESS));
    }

    @Test
    public void error_client_returns_basic_error() {
        TestBasicErrorResponse result = errorServiceClient.testBasicError(AUTH_HEADER, true);
        assertThat(result)
                .isInstanceOfSatisfying(
                        TestBasicErrorResponse.InvalidArgument.class,
                        error -> assertInvalidArgumentError(
                                error.getErrorCode(),
                                error.getErrorName(),
                                error.getParams().field(),
                                error.getParams().value()));
    }

    @Test
    public void error_client_returns_imported_error() {
        TestImportedErrorResponse result = errorServiceClient.testImportedError(AUTH_HEADER, true);
        assertThat(result).isInstanceOfSatisfying(TestImportedErrorResponse.EndpointError.class, error -> {
            assertThat(error.getErrorCode())
                    .isEqualTo(EndpointSpecificErrors.ENDPOINT_ERROR.code().name());
            assertThat(error.getErrorName()).isEqualTo(EndpointSpecificErrors.ENDPOINT_ERROR.name());
            assertThat(error.getParams()).satisfies(params -> {
                assertThat(params.typeDef()).isEqualTo("typeDef");
                assertThat(params.typeName()).isEqualTo("typeName");
            });
        });
    }

    @Test
    public void error_client_returns_one_of_many_errors() {
        assertThat(errorServiceClient.testMultipleErrorsAndPackages(AUTH_HEADER, Optional.of("invalidArgument")))
                .isInstanceOfSatisfying(TestMultipleErrorsAndPackagesResponse.InvalidArgument.class, error ->
                    assertInvalidArgumentError(
                            error.getErrorCode(),
                            error.getErrorName(),
                            error.getParams().field(),
                            error.getParams().value())
                );
        assertThat(errorServiceClient.testMultipleErrorsAndPackages(AUTH_HEADER, Optional.of("notFound")))
                .isInstanceOfSatisfying(TestMultipleErrorsAndPackagesResponse.NotFound.class, error -> {
                    assertThat(error.getErrorCode())
                            .isEqualTo(TestErrors.NOT_FOUND.code().name());
                    assertThat(error.getErrorName()).isEqualTo(TestErrors.NOT_FOUND.name());
                    assertThat(error.getParams()).satisfies(params ->
                        assertThat(params.resource()).isEqualTo("resource")
                    );
                });
        assertThat(errorServiceClient.testMultipleErrorsAndPackages(AUTH_HEADER, Optional.of("differentNamespace")))
                .isInstanceOfSatisfying(TestMultipleErrorsAndPackagesResponse.DifferentNamespace.class, error -> {
                    assertThat(error.getErrorCode())
                            .isEqualTo(EndpointSpecificTwoErrors.DIFFERENT_NAMESPACE
                                    .code()
                                    .name());
                    assertThat(error.getErrorName()).isEqualTo(EndpointSpecificTwoErrors.DIFFERENT_NAMESPACE.name());
                });
        assertThat(errorServiceClient.testMultipleErrorsAndPackages(AUTH_HEADER, Optional.of("differentPackage")))
                .isInstanceOfSatisfying(TestMultipleErrorsAndPackagesResponse.DifferentPackage.class, error -> {
                    assertThat(error.getErrorCode())
                            .isEqualTo(com.palantir.another.EndpointSpecificErrors.DIFFERENT_PACKAGE
                                    .code()
                                    .name());
                    assertThat(error.getErrorName())
                            .isEqualTo(com.palantir.another.EndpointSpecificErrors.DIFFERENT_PACKAGE.name());
                });
    }

    @Test
    public void error_client_test_empty() {
        TestEmptyBodyResponse result = errorServiceClient.testEmptyBody(AUTH_HEADER, false);
        assertThat(result).isInstanceOf(TestEmptyBodyResponse.Success.class);
    }

    @Test
    public void error_client_test_empty_error() {
        TestEmptyBodyResponse result = errorServiceClient.testEmptyBody(AUTH_HEADER, true);
        assertThat(result).isInstanceOfSatisfying(TestEmptyBodyResponse.InvalidArgument.class, invalidArgument ->
            assertInvalidArgumentError(
                    invalidArgument.getErrorCode(),
                    invalidArgument.getErrorName(),
                    invalidArgument.getParams().field(),
                    invalidArgument.getParams().value())
        );
    }

    @Test
    public void error_client_binary_response() {
        try (TestBinaryResponse result = errorServiceClient.testBinary(AUTH_HEADER, false)) {
            assertThat(result).isInstanceOfSatisfying(TestBinaryResponse.Success.class, success -> {
                try (InputStream is = success.value()) {
                    assertThat(is.readAllBytes())
                            .isEqualTo(ErrorResource.Impl.SUCCESS.getBytes(StandardCharsets.UTF_8));

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void error_client_binary_response_error() {
        TestBinaryResponse result = errorServiceClient.testBinary(AUTH_HEADER, true);
        assertThat(result).isInstanceOfSatisfying(TestBinaryResponse.InvalidArgument.class, invalidArgument ->
            assertInvalidArgumentError(
                    invalidArgument.getErrorCode(),
                    invalidArgument.getErrorName(),
                    invalidArgument.getParams().field(),
                    invalidArgument.getParams().value())
        );
    }

    @Test
    public void error_client_optional_binary_response() {
        try (TestOptionalBinaryResponse result =
                errorServiceClient.testOptionalBinary(AUTH_HEADER, OptionalBinaryResponseMode.PRESENT)) {
            assertThat(result).isInstanceOfSatisfying(TestOptionalBinaryResponse.Success.class, success -> {
                assertThat(success.value()).isPresent();
                try (InputStream is = success.value().get()) {
                    assertThat(is.readAllBytes())
                            .isEqualTo(ErrorResource.Impl.SUCCESS.getBytes(StandardCharsets.UTF_8));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void error_client_optional_binary_response_absent() {
        TestOptionalBinaryResponse result =
                errorServiceClient.testOptionalBinary(AUTH_HEADER, OptionalBinaryResponseMode.ABSENT);
        assertThat(result)
                .isInstanceOfSatisfying(TestOptionalBinaryResponse.Success.class, success -> assertThat(success.value())
                        .isEmpty());
    }

    @Test
    public void error_client_optional_binary_response_error() {
        TestOptionalBinaryResponse result =
                errorServiceClient.testOptionalBinary(AUTH_HEADER, OptionalBinaryResponseMode.ERROR);
        assertThat(result).isInstanceOfSatisfying(TestOptionalBinaryResponse.InvalidArgument.class, invalidArgument ->
            assertInvalidArgumentError(
                    invalidArgument.getErrorCode(),
                    invalidArgument.getErrorName(),
                    invalidArgument.getParams().field(),
                    invalidArgument.getParams().value())
        );
    }

    private static void assertInvalidArgumentError(String errorCode, String errorName, String field, String value) {
        assertThat(errorCode).isEqualTo(TestErrors.INVALID_ARGUMENT.code().name());
        assertThat(errorName).isEqualTo(TestErrors.INVALID_ARGUMENT.name());
        assertThat(field).isEqualTo("field");
        assertThat(value).isEqualTo("value");
    }
}
