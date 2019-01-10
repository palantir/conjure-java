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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.ByteStreams;
import com.google.common.net.HttpHeaders;
import com.palantir.conjure.defs.Conjure;
import com.palantir.conjure.java.api.errors.RemoteException;
import com.palantir.conjure.java.client.jaxrs.JaxRsClient;
import com.palantir.conjure.java.client.retrofit2.Retrofit2Client;
import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.conjure.java.okhttp.HostMetricsRegistry;
import com.palantir.conjure.java.serialization.ObjectMappers;
import com.palantir.conjure.java.services.UndertowServiceGenerator;
import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.conjure.java.undertow.lib.HandlerContext;
import com.palantir.conjure.java.undertow.lib.SerializerRegistry;
import com.palantir.conjure.java.undertow.runtime.ConjureHandler;
import com.palantir.conjure.java.undertow.runtime.Serializers;
import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.product.EmptyPathService;
import com.palantir.product.EmptyPathServiceEndpoint;
import com.palantir.product.EteBinaryServiceEndpoint;
import com.palantir.product.EteBinaryServiceRetrofit;
import com.palantir.product.EteService;
import com.palantir.product.EteServiceEndpoint;
import com.palantir.product.EteServiceRetrofit;
import com.palantir.product.NestedStringAliasExample;
import com.palantir.product.SimpleEnum;
import com.palantir.product.StringAliasExample;
import com.palantir.ri.ResourceIdentifier;
import com.palantir.tokens.auth.AuthHeader;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
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
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import retrofit2.Response;

public final class UndertowServiceEteTest extends TestBase {
    private static final ObjectMapper CLIENT_OBJECT_MAPPER = ObjectMappers.newClientObjectMapper();

    @ClassRule
    public static final TemporaryFolder folder = new TemporaryFolder();

    private static Undertow server;

    private final EteService client;

    private final EteServiceRetrofit retrofitClient;

    private final EteBinaryServiceRetrofit binaryClient;

    public UndertowServiceEteTest() {
        client = JaxRsClient.create(
                EteService.class,
                clientUserAgent(),
                new HostMetricsRegistry(),
                clientConfiguration());
        retrofitClient = Retrofit2Client.create(
                EteServiceRetrofit.class,
                clientUserAgent(),
                new HostMetricsRegistry(),
                clientConfiguration());
        binaryClient = Retrofit2Client.create(
                EteBinaryServiceRetrofit.class,
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
        List<Endpoint> endpoints = ImmutableList.of(
                EteServiceEndpoint.of(new UndertowEteResource()),
                EmptyPathServiceEndpoint.of(() -> true),
                EteBinaryServiceEndpoint.of(new UndertowBinaryResource()));
        endpoints.forEach(endpoint -> endpoint.create(context).register(handler));
        server = Undertow.builder()
                .setServerOption(UndertowOptions.DECODE_URL, false)
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
    public void optional_empty_from_a_server_has_empty_status() throws Exception {
        assertThat(retrofitClient.optionalEmpty(AuthHeader.valueOf("authHeader")).execute().code()).isEqualTo(204);
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

    @Test
    public void testCborContent() throws Exception {
        ObjectMapper cborMapper = ObjectMappers.newCborClientObjectMapper();
        // postString method
        HttpURLConnection connection = (HttpURLConnection)
                new URL("http://localhost:8080/test-example/api/base/notNullBody").openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty(HttpHeaders.AUTHORIZATION, AuthHeader.valueOf("authHeader").toString());
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
        HttpURLConnection connection = (HttpURLConnection)
                new URL("http://localhost:8080/test-example/api/base/notNullBody").openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty(HttpHeaders.AUTHORIZATION, AuthHeader.valueOf("authHeader").toString());
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
        Response<ResponseBody> response = binaryClient.postBinary(AuthHeader.valueOf("authHeader"),
                RequestBody.create(MediaType.parse("application/octet-stream"), expected)).execute();
        try (ResponseBody body = response.body()) {
            assertThat(response.isSuccessful()).isTrue();
            assertThat(body.contentType()).isEqualTo(MediaType.parse("application/octet-stream"));
            assertThat(body.bytes()).isEqualTo(expected);
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
        assertThat(client.notNullBodyExternalImport(AuthHeader.valueOf("authHeader"), expected)).isEqualTo(expected);
    }

    @Test
    public void testExternalImportOptionalQueryParameter() {
        Optional<StringAliasExample> expected = Optional.of(StringAliasExample.of("value"));
        assertThat(client.optionalQueryExternalImport(AuthHeader.valueOf("authHeader"), expected)).isEqualTo(expected);
    }

    @Test
    public void testExternalImportOptionalEmptyQueryParameter() {
        assertThat(client.optionalQueryExternalImport(AuthHeader.valueOf("authHeader"), Optional.empty()))
                .isEqualTo(Optional.empty());
    }

    @Test
    public void testExternalImportOptionalBody() {
        Optional<StringAliasExample> expected = Optional.of(StringAliasExample.of("value"));
        assertThat(client.optionalBodyExternalImport(AuthHeader.valueOf("authHeader"), expected)).isEqualTo(expected);
    }

    @Test
    public void testExternalImportOptionalEmptyBody() {
        assertThat(client.optionalBodyExternalImport(AuthHeader.valueOf("authHeader"), Optional.empty()))
                .isEqualTo(Optional.empty());
    }

    @Test
    public void testUnknownContentType() {
        assertThatThrownBy(() -> binaryClient.postBinary(AuthHeader.valueOf("authHeader"),
                RequestBody.create(MediaType.parse("application/unsupported"), new byte[] {1, 2, 3})).execute())
                .isInstanceOf(RemoteException.class)
                .hasMessageContaining("INVALID_ARGUMENT");
    }

    @Test
    public void testSlashesInPathParam() {
        String expected = "foo/bar/baz/%2F";
        assertThat(client.path(AuthHeader.valueOf("bearer"), expected)).isEqualTo(expected);
    }

    @Test
    public void testBinaryOptionalEmptyResponse() throws Exception {
        URL url = new URL("http://0.0.0.0:8080/test-example/api/binary/optional/empty");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty(HttpHeaders.AUTHORIZATION, AuthHeader.valueOf("authHeader").toString());
        con.setRequestMethod("GET");
        assertThat(con.getResponseCode()).isEqualTo(204);
        assertThat(con.getHeaderField(HttpHeaders.CONTENT_TYPE)).isNull();
    }

    @Test
    public void testBinaryOptionalPresentResponse() throws Exception {
        URL url = new URL("http://0.0.0.0:8080/test-example/api/binary/optional/present");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty(HttpHeaders.AUTHORIZATION, AuthHeader.valueOf("authHeader").toString());
        con.setRequestMethod("GET");
        assertThat(con.getResponseCode()).isEqualTo(200);
        assertThat(con.getHeaderField(HttpHeaders.CONTENT_TYPE)).startsWith("application/octet-stream");
        assertThat(new String(ByteStreams.toByteArray(con.getInputStream()), StandardCharsets.UTF_8))
                .isEqualTo("Hello World!");
    }

    @Test
    public void testBinaryServerSideFailureAfterManyBytesSent() throws Exception {
        Response<ResponseBody> response = binaryClient.getBinaryFailure(AuthHeader.valueOf("authHeader"),
                // Write more bytes than one buffer
                20000).execute();
        try (ResponseBody body = response.body()) {
            assertThatThrownBy(() -> ByteStreams.copy(body.byteStream(), ByteStreams.nullOutputStream()))
                    .isInstanceOf(IOException.class);
        }
    }

    @Test
    public void testBinaryServerSideFailureAfterFewBytesSent() {
        assertThatThrownBy(() -> binaryClient.getBinaryFailure(AuthHeader.valueOf("authHeader"), 1).execute())
                .isInstanceOf(IOException.class);
    }

    @Test
    public void testVoidMethod() {
        client.noReturn(AuthHeader.valueOf("authHeader"));
    }

    @Test
    public void testVoidMethodRespondsNoContent() throws Exception {
        URL url = new URL("http://0.0.0.0:8080/test-example/api/base/no-return");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty(HttpHeaders.AUTHORIZATION, AuthHeader.valueOf("authHeader").toString());
        assertThat(con.getResponseCode()).isEqualTo(204);
        assertThat(con.getHeaderField(HttpHeaders.CONTENT_TYPE)).isNull();
    }

    @Test
    public void testEnumQueryParameter() {
        assertThat(client.enumQuery(AuthHeader.valueOf("authHeader"), SimpleEnum.VALUE)).isEqualTo(SimpleEnum.VALUE);
    }

    @Test
    public void testOptionalEnumQueryParameterPresent() {
        assertThat(client.optionalEnumQuery(AuthHeader.valueOf("authHeader"), Optional.of(SimpleEnum.VALUE)))
                .isEqualTo(Optional.of(SimpleEnum.VALUE));
    }

    @Test
    public void testOptionalEnumQueryParameterEmpty() {
        assertThat(client.optionalEnumQuery(AuthHeader.valueOf("authHeader"), Optional.empty()))
                .isEqualTo(Optional.empty());
    }

    @Test
    public void testEnumQueryParameterList() {
        assertThat(client.enumListQuery(AuthHeader.valueOf("authHeader"), Collections.singletonList(SimpleEnum.VALUE)))
                .isEqualTo(Collections.singletonList(SimpleEnum.VALUE));
    }

    @Test
    public void testEnumHeaderParameter() {
        assertThat(client.enumHeader(AuthHeader.valueOf("authHeader"), SimpleEnum.VALUE)).isEqualTo(SimpleEnum.VALUE);
    }

    @BeforeClass
    public static void beforeClass() throws IOException {
        ConjureDefinition def = Conjure.parse(ImmutableList.of(
                new File("src/test/resources/ete-service.yml"),
                new File("src/test/resources/ete-binary.yml")));
        List<Path> files = new UndertowServiceGenerator(ImmutableSet.of(FeatureFlags.UndertowServicePrefix))
                .emit(def, folder.getRoot());
        validateGeneratorOutput(files, Paths.get("src/integrationInput/java/com/palantir/product"));
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
