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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.net.HttpHeaders;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.palantir.conjure.defs.Conjure;
import com.palantir.conjure.java.api.errors.RemoteException;
import com.palantir.conjure.java.api.errors.SerializableError;
import com.palantir.conjure.java.client.jaxrs.JaxRsClient;
import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.conjure.java.okhttp.HostMetricsRegistry;
import com.palantir.conjure.java.serialization.ObjectMappers;
import com.palantir.conjure.java.services.UndertowServiceGenerator;
import com.palantir.conjure.java.types.ObjectGenerator;
import com.palantir.conjure.java.undertow.runtime.ConjureHandler;
import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.dialogue.BinaryRequestBody;
import com.palantir.dialogue.clients.DialogueClients;
import com.palantir.product.ConjureJavaErrors;
import com.palantir.product.EmptyPathService;
import com.palantir.product.EmptyPathServiceEndpoints;
import com.palantir.product.EteBinaryServiceBlocking;
import com.palantir.product.EteBinaryServiceEndpoints;
import com.palantir.product.EteServiceAsync;
import com.palantir.product.EteServiceBlocking;
import com.palantir.product.EteServiceEndpoints;
import com.palantir.product.NestedStringAliasExample;
import com.palantir.product.SimpleEnum;
import com.palantir.product.StringAliasExample;
import com.palantir.ri.ResourceIdentifier;
import com.palantir.tokens.auth.AuthHeader;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import io.undertow.server.HttpHandler;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@Execution(ExecutionMode.CONCURRENT)
public final class UndertowServiceEteTest extends TestBase {
    private static final ObjectMapper CLIENT_OBJECT_MAPPER = ObjectMappers.newClientObjectMapper();

    @TempDir
    public static File folder;

    private static Undertow server;

    private final EteServiceBlocking client;
    private final EteServiceAsync asyncClient;

    private final EteBinaryServiceBlocking binaryClient;

    private static int port;

    public UndertowServiceEteTest() {
        this.client = DialogueClients.create(EteServiceBlocking.class, clientConfiguration(port));
        this.asyncClient = DialogueClients.create(EteServiceAsync.class, clientConfiguration(port));
        this.binaryClient = DialogueClients.create(EteBinaryServiceBlocking.class, clientConfiguration(port));
    }

    @BeforeAll
    public static void before() {

        HttpHandler handler = ConjureHandler.builder()
                .services(EteServiceEndpoints.of(new UndertowEteResource()))
                .services(EmptyPathServiceEndpoints.of(() -> true))
                .services(EteBinaryServiceEndpoints.of(new UndertowBinaryResource()))
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
    public void jaxrs_client_can_make_a_call_to_an_empty_path() {
        EmptyPathService emptyPathClient = JaxRsClient.create(
                EmptyPathService.class, clientUserAgent(), new HostMetricsRegistry(), clientConfiguration(port));
        assertThat(emptyPathClient.emptyPath()).isTrue();
    }

    @Test
    public void client_can_retrieve_a_string_from_a_server() {
        assertThat(client.string(AuthHeader.valueOf("authHeader"))).isEqualTo("Hello, world!");
    }

    @Test
    public void client_can_retrieve_an_integer_from_a_server() {
        assertThat(client.integer(AuthHeader.valueOf("authHeader"))).isEqualTo(1234);
    }

    @Test
    public void client_throws_when_retrieving_an_integer_from_a_server() {
        assertThatExceptionOfType(ConjureJavaErrors.JavaCompilationFailed.class)
                .isThrownBy(() -> client.integer(AuthHeader.valueOf("throw")));
    }

    @Test
    public void client_can_retrieve_a_double_from_a_server() {
        assertThat(client.double_(AuthHeader.valueOf("authHeader"))).isEqualTo(1 / 3d);
    }

    @Test
    public void client_can_retrieve_a_boolean_from_a_server() throws Exception {
        assertThat(client.boolean_(AuthHeader.valueOf("authHeader"))).isTrue();
    }

    @Test
    public void client_can_retrieve_a_safelong_from_a_server() throws Exception {
        assertThat(client.safelong(AuthHeader.valueOf("authHeader"))).isEqualTo(SafeLong.of(12345));
    }

    @Test
    public void client_can_retrieve_an_rid_from_a_server() {
        assertThat(client.rid(AuthHeader.valueOf("authHeader")))
                .isEqualTo(ResourceIdentifier.of("ri.foundry.main.dataset.1234"));
    }

    @Test
    public void client_can_retrieve_an_optional_string_from_a_server() {
        assertThat(client.optionalString(AuthHeader.valueOf("authHeader"))).contains("foo");
    }

    @Test
    public void jaxrs_client_can_retrieve_an_optional_empty_from_a_server() {
        assertThat(client.optionalEmpty(AuthHeader.valueOf("authHeader"))).isNotPresent();
    }

    @Test
    public void optional_empty_from_a_server_has_empty_status() {
        assertThat(client.optionalEmpty(AuthHeader.valueOf("authHeader"))).isEmpty();
    }

    @Test
    public void client_can_retrieve_a_date_time_from_a_server() {
        assertThat(client.datetime(AuthHeader.valueOf("authHeader")))
                .isEqualTo(OffsetDateTime.ofInstant(Instant.ofEpochMilli(1234), ZoneId.from(ZoneOffset.UTC)));
    }

    @Test
    public void java_url_client_receives_ok_with_complete_request() throws IOException {
        HttpURLConnection httpUrlConnection = preparePostRequest();
        httpUrlConnection.setRequestProperty("Authorization", "Bearer authheader");
        sendPostRequestData(httpUrlConnection, CLIENT_OBJECT_MAPPER.writeValueAsString(StringAliasExample.of("foo")));
        assertThat(httpUrlConnection.getResponseCode()).isEqualTo(200);
    }

    @Test
    public void java_url_client_receives_unauthorized_without_authheader() throws IOException {
        HttpURLConnection httpUrlConnection = preparePostRequest();
        sendPostRequestData(httpUrlConnection, CLIENT_OBJECT_MAPPER.writeValueAsString(StringAliasExample.of("foo")));
        assertThat(httpUrlConnection.getResponseCode()).isEqualTo(401);
    }

    @Test
    public void java_url_client_receives_bad_request_with_json_null() throws IOException {
        HttpURLConnection connection = preparePostRequest();
        connection.setRequestProperty("Authorization", "Bearer authheader");
        connection.setRequestProperty(HttpHeaders.CONTENT_TYPE, "application/json");
        connection.setRequestProperty(HttpHeaders.ACCEPT, "application/json");
        sendPostRequestData(connection, "null");
        assertThat(connection.getResponseCode()).isEqualTo(400);
        try (InputStream responseBody = connection.getErrorStream()) {
            SerializableError error = CLIENT_OBJECT_MAPPER.readValue(responseBody, SerializableError.class);
            assertThat(error.errorCode()).isEqualTo("INVALID_ARGUMENT");
            assertThat(error.errorName()).isEqualTo("Default:InvalidArgument");
        }
    }

    @Test
    public void java_url_client_receives_unprocessable_entity_with_null_body() throws IOException {
        HttpURLConnection httpUrlConnection = preparePostRequest();
        httpUrlConnection.setRequestProperty("Authorization", "Bearer authheader");
        sendPostRequestData(httpUrlConnection, "");
        assertThat(httpUrlConnection.getResponseCode()).isEqualTo(422);
        try (InputStream responseBody = httpUrlConnection.getErrorStream()) {
            SerializableError error = CLIENT_OBJECT_MAPPER.readValue(responseBody, SerializableError.class);
            assertThat(error.errorCode()).isEqualTo("INVALID_ARGUMENT");
            assertThat(error.errorName()).isEqualTo("Conjure:UnprocessableEntity");
            assertThat(error.parameters())
                    .as("Diagnostic data is logged, not transmitted")
                    .isEmpty();
        }
    }

    @Test
    public void testCborContent() throws Exception {
        ObjectMapper cborMapper = ObjectMappers.newCborClientObjectMapper();
        // postString method
        HttpURLConnection connection = openConnectionToTestApi("/base/notNullBody");
        connection.setRequestMethod("POST");
        connection.setRequestProperty(
                HttpHeaders.AUTHORIZATION, AuthHeader.valueOf("authHeader").toString());
        connection.setRequestProperty(HttpHeaders.CONTENT_TYPE, "application/cbor");
        connection.setRequestProperty(HttpHeaders.ACCEPT, "application/cbor");
        connection.setDoOutput(true);
        byte[] contents = cborMapper.writeValueAsBytes("Hello, World!");
        try (OutputStream requestBody = connection.getOutputStream()) {
            requestBody.write(contents);
        }
        assertThat(connection.getResponseCode()).isEqualTo(200);
        assertThat(connection.getHeaderField(HttpHeaders.CONTENT_TYPE)).startsWith("application/cbor");
        assertThat(connection.getHeaderField(HttpHeaders.CONTENT_LENGTH)).isEqualTo(Integer.toString(contents.length));
        try (InputStream responseBody = connection.getInputStream()) {
            assertThat(cborMapper.readValue(responseBody, String.class)).isEqualTo("Hello, World!");
        }
    }

    @Test
    public void testContentLengthSet() throws Exception {
        // postString method
        HttpURLConnection connection = openConnectionToTestApi("/base/notNullBody");
        connection.setRequestMethod("POST");
        connection.setRequestProperty(
                HttpHeaders.AUTHORIZATION, AuthHeader.valueOf("authHeader").toString());
        connection.setRequestProperty(HttpHeaders.CONTENT_TYPE, "application/json");
        connection.setRequestProperty(HttpHeaders.ACCEPT, "application/json");
        connection.setDoOutput(true);
        byte[] contents = "\"Hello, World!\"".getBytes(StandardCharsets.UTF_8);
        try (OutputStream requestBody = connection.getOutputStream()) {
            requestBody.write(contents);
        }
        assertThat(connection.getResponseCode()).isEqualTo(200);
        assertThat(connection.getHeaderField(HttpHeaders.CONTENT_LENGTH)).isEqualTo(Integer.toString(contents.length));
        connection.getInputStream().close();
    }

    @Test
    public void testBinaryPost() throws Exception {
        byte[] expected = "Hello, World".getBytes(StandardCharsets.UTF_8);
        try (InputStream response = binaryClient.postBinary(
                AuthHeader.valueOf("authHeader"), BinaryRequestBody.of(new ByteArrayInputStream(expected)))) {
            assertThat(response.readAllBytes()).isEqualTo(expected);
        }
    }

    @Test
    public void testAliasQueryParameter() {
        NestedStringAliasExample expected = NestedStringAliasExample.of(StringAliasExample.of("value"));
        assertThat(client.aliasTwo(AuthHeader.valueOf("authHeader"), expected)).isEqualTo(expected);
    }

    @Test
    public void testExternalImportBody() {
        StringAliasExample expected = StringAliasExample.of("value");
        assertThat(client.notNullBodyExternalImport(AuthHeader.valueOf("authHeader"), expected))
                .isEqualTo(expected);
    }

    @Test
    public void testExternalImportOptionalQueryParameter() {
        Optional<StringAliasExample> expected = Optional.of(StringAliasExample.of("value"));
        assertThat(client.optionalQueryExternalImport(AuthHeader.valueOf("authHeader"), expected))
                .isEqualTo(expected);
    }

    @Test
    public void testExternalImportOptionalEmptyQueryParameter() {
        assertThat(client.optionalQueryExternalImport(AuthHeader.valueOf("authHeader"), Optional.empty()))
                .isNotPresent();
    }

    @Test
    public void testExternalImportOptionalBody() {
        Optional<StringAliasExample> expected = Optional.of(StringAliasExample.of("value"));
        assertThat(client.optionalBodyExternalImport(AuthHeader.valueOf("authHeader"), expected))
                .isEqualTo(expected);
    }

    @Test
    public void testExternalImportOptionalEmptyBody() {
        assertThat(client.optionalBodyExternalImport(AuthHeader.valueOf("authHeader"), Optional.empty()))
                .isNotPresent();
    }

    @Test
    public void testExternalImportOptionalEmptyBodyZeroLength_noContentType() throws IOException {
        // Empty optional request body parameters may be encoded as JSON 'null' or an empty HTTP request body.
        // Feign clients send JSON 'null', here we test that a non-present body works as expected.
        HttpURLConnection con = openConnectionToTestApi("/base/external/optional-body");
        con.setRequestMethod("POST");
        con.setRequestProperty(
                HttpHeaders.AUTHORIZATION, AuthHeader.valueOf("authHeader").toString());
        assertThat(con.getResponseCode()).isEqualTo(204);
    }

    @Test
    public void testExternalImportOptionalEmptyBodyZeroLength_withContentType() throws IOException {
        // Empty optional request body parameters may be encoded as JSON 'null' or an empty HTTP request body.
        // Feign send JSON 'null', here we test that a non-present body works as expected.
        // In this test case, we include "Content-Type: application/json" for backwards compatibility with
        // clients that always set request content-type regardless of the presence of a body.
        HttpURLConnection con = openConnectionToTestApi("/base/external/optional-body");
        con.setRequestMethod("POST");
        con.setRequestProperty(HttpHeaders.CONTENT_TYPE, "application/json");
        con.setRequestProperty(
                HttpHeaders.AUTHORIZATION, AuthHeader.valueOf("authHeader").toString());
        assertThat(con.getResponseCode()).isEqualTo(204);
    }

    @Test
    public void testGetMethodsAllowHeadRequests() throws IOException {
        HttpURLConnection con = openConnectionToTestApi("/base/string");
        con.setRequestMethod("HEAD");
        con.setRequestProperty(HttpHeaders.ACCEPT, "application/json");
        con.setRequestProperty(
                HttpHeaders.AUTHORIZATION, AuthHeader.valueOf("authHeader").toString());
        assertThat(con.getResponseCode()).isEqualTo(200);
        try (InputStream responseBody = con.getInputStream()) {
            assertThat(responseBody).hasContent("");
        }
    }

    @Test
    public void testOptionsOnGetIncludesHead() throws IOException {
        HttpURLConnection con = openConnectionToTestApi("/base/string");
        con.setRequestMethod("OPTIONS");
        con.setRequestProperty(HttpHeaders.ACCEPT, "application/json");
        con.setRequestProperty(
                HttpHeaders.AUTHORIZATION, AuthHeader.valueOf("authHeader").toString());
        assertThat(con.getResponseCode()).isEqualTo(204);
        assertThat(con.getHeaderField(HttpHeaders.ALLOW)).isEqualTo("OPTIONS, GET, HEAD");
    }

    @Test
    public void testUnknownContentType() throws Exception {
        HttpURLConnection con = openConnectionToTestApi("/binary");
        con.setRequestMethod("POST");
        con.setRequestProperty(HttpHeaders.CONTENT_TYPE, "application/unsupported");
        con.setRequestProperty(
                HttpHeaders.AUTHORIZATION, AuthHeader.valueOf("authHeader").toString());
        con.setDoOutput(true);
        con.getOutputStream().write(new byte[] {1, 2, 3});
        assertThat(con.getResponseCode()).isEqualTo(415);
        SerializableError serializableError =
                CLIENT_OBJECT_MAPPER.readValue(con.getErrorStream(), SerializableError.class);
        assertThat(serializableError.errorCode()).isEqualTo("INVALID_ARGUMENT");
    }

    @Test
    public void testSlashesInPathParam() {
        String expected = "foo/bar/baz/%2F";
        assertThat(client.path(AuthHeader.valueOf("bearer"), expected)).isEqualTo(expected);
        assertThat(Futures.getUnchecked(asyncClient.path(AuthHeader.valueOf("bearer"), expected)))
                .isEqualTo(expected);
    }

    @Test
    public void testPlusInPathParam() {
        String expected = "foo+bar";
        assertThat(client.path(AuthHeader.valueOf("bearer"), expected)).isEqualTo(expected);
        assertThat(Futures.getUnchecked(asyncClient.path(AuthHeader.valueOf("bearer"), expected)))
                .isEqualTo(expected);
    }

    @Test
    public void testSpaceInPathParam() {
        String expected = "foo bar";
        assertThat(client.path(AuthHeader.valueOf("bearer"), expected)).isEqualTo(expected);
        assertThat(Futures.getUnchecked(asyncClient.path(AuthHeader.valueOf("bearer"), expected)))
                .isEqualTo(expected);
    }

    @Test
    public void testBinaryOptionalEmptyResponse() {
        Optional<InputStream> response = binaryClient.getOptionalBinaryEmpty(AuthHeader.valueOf("authHeader"));
        assertThat(response).isNotPresent();
    }

    @Test
    public void testBinaryOptionalPresentResponse() throws Exception {
        Optional<InputStream> response = binaryClient.getOptionalBinaryPresent(AuthHeader.valueOf("authHeader"));
        assertThat(response).isPresent();
        try (InputStream is = response.get()) {
            assertThat(new String(is.readAllBytes(), StandardCharsets.UTF_8)).isEqualTo("Hello World!");
        }
    }

    @Test
    public void testBinaryServerSideFailureAfterManyBytesSent() throws IOException {
        try (InputStream response = binaryClient.getBinaryFailure(
                AuthHeader.valueOf("authHeader"),
                // Write more bytes than one buffer
                20000,
                false)) {
            assertThatThrownBy(response::readAllBytes).isInstanceOf(IOException.class);
        }
    }

    @Test
    public void testBinaryServerSideFailureAfterManyBytesSentTryWithResources() throws IOException {
        try (InputStream response = binaryClient.getBinaryFailure(
                AuthHeader.valueOf("authHeader"),
                // Write more bytes than one buffer
                20000,
                true)) {
            assertThatThrownBy(response::readAllBytes).isInstanceOf(IOException.class);
        }
    }

    @Test
    public void testBinaryServerSideFailureAfterFewBytesSent() {
        assertThatThrownBy(() -> {
                    try {
                        binaryClient
                                .getBinaryFailure(AuthHeader.valueOf("authHeader"), 1, false)
                                .close();
                    } catch (UncheckedExecutionException e) {
                        throw e.getCause();
                    }
                })
                .isInstanceOf(RemoteException.class);
    }

    @Test
    public void testBinaryServerSideFailureAfterFewBytesSentTryWithResources() {
        assertThatThrownBy(() -> {
                    try {
                        binaryClient
                                .getBinaryFailure(AuthHeader.valueOf("authHeader"), 1, true)
                                .close();
                    } catch (UncheckedExecutionException e) {
                        throw e.getCause();
                    }
                })
                .isInstanceOf(RemoteException.class);
    }

    @Test
    @Timeout(20)
    public void testBinaryServerSideFailureAfterFewBytesReceived() {
        int chunkSize = 1024 * 1024; // 1 MB
        int expectedChunks = 1; // 1 MB
        int chunksToSend = 1024; // 1 GB
        int bytesExpected = expectedChunks * chunkSize;

        byte[] data = new byte[chunkSize];
        ThreadLocalRandom.current().nextBytes(data);
        assertThatThrownBy(() -> binaryClient.postBinaryThrows(
                        AuthHeader.valueOf("authHeader"), bytesExpected, (OutputStream sink) -> {
                            for (int i = 0; i < chunksToSend; i++) {
                                sink.write(data);
                            }
                        }))
                .isInstanceOfSatisfying(
                        RemoteException.class, re -> assertThat(re.getStatus()).isEqualTo(400));
    }

    @Test
    public void testVoidMethod() {
        client.noReturn(AuthHeader.valueOf("authHeader"));
    }

    @Test
    public void testVoidMethodRespondsNoContent() throws Exception {
        HttpURLConnection con = openConnectionToTestApi("/base/no-return");
        con.setRequestMethod("POST");
        con.setRequestProperty(
                HttpHeaders.AUTHORIZATION, AuthHeader.valueOf("authHeader").toString());
        assertThat(con.getResponseCode()).isEqualTo(204);
        assertThat(con.getHeaderField(HttpHeaders.CONTENT_TYPE)).isNull();
    }

    @Test
    public void testEnumQueryParameter() {
        assertThat(client.enumQuery(AuthHeader.valueOf("authHeader"), SimpleEnum.VALUE))
                .isEqualTo(SimpleEnum.VALUE);
    }

    @Test
    public void testOptionalEnumQueryParameterPresent() {
        assertThat(client.optionalEnumQuery(AuthHeader.valueOf("authHeader"), Optional.of(SimpleEnum.VALUE)))
                .contains(SimpleEnum.VALUE);
    }

    @Test
    public void testOptionalEnumQueryParameterEmpty() {
        assertThat(client.optionalEnumQuery(AuthHeader.valueOf("authHeader"), Optional.empty()))
                .isNotPresent();
    }

    @Test
    public void testEnumQueryParameterList() {
        assertThat(client.enumListQuery(AuthHeader.valueOf("authHeader"), Collections.singletonList(SimpleEnum.VALUE)))
                .containsExactlyElementsOf(Collections.singletonList(SimpleEnum.VALUE));
    }

    @Test
    public void testEnumHeaderParameter() {
        assertThat(client.enumHeader(AuthHeader.valueOf("authHeader"), SimpleEnum.VALUE))
                .isEqualTo(SimpleEnum.VALUE);
    }

    @Test
    public void testListOfNull() {
        assertThatThrownBy(() ->
                        client.receiveListOfStrings(AuthHeader.valueOf("authHeader"), Collections.singletonList(null)))
                .isInstanceOfSatisfying(RemoteException.class, re -> {
                    assertThat(re.getStatus()).isEqualTo(422);
                });
    }

    @BeforeAll
    public static void beforeClass() throws IOException {
        ConjureDefinition def = Conjure.parse(ImmutableList.of(
                new File("src/test/resources/ete-service.yml"),
                new File("src/test/resources/ete-binary.yml"),
                new File("src/test/resources/alias-test-service.yml"),
                new File("src/test/resources/external-long-test-service.yml")));
        Options options = Options.builder()
                .undertowServicePrefix(true)
                .nonNullCollections(true)
                .excludeEmptyOptionals(true)
                .jetbrainsContractAnnotations(true)
                .build();
        List<Path> files = new GenerationCoordinator(
                        MoreExecutors.directExecutor(),
                        ImmutableSet.of(new UndertowServiceGenerator(options), new ObjectGenerator(options)))
                .emit(def, folder);
        validateGeneratorOutput(files, Paths.get("src/integrationInput/java/com/palantir/product"));
    }

    private static HttpURLConnection openConnectionToTestApi(String path) throws IOException {
        URL url = new URL("http://localhost:" + port + "/test-example/api" + path);
        return (HttpURLConnection) url.openConnection();
    }

    private static HttpURLConnection preparePostRequest() throws IOException {
        HttpURLConnection con = openConnectionToTestApi("/base/notNullBody");
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
