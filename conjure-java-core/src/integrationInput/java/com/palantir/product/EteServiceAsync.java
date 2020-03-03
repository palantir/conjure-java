package com.palantir.product;

import com.google.common.util.concurrent.ListenableFuture;
import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.dialogue.Channel;
import com.palantir.dialogue.ConjureRuntime;
import com.palantir.dialogue.Deserializer;
import com.palantir.dialogue.PlainSerDe;
import com.palantir.dialogue.Request;
import com.palantir.dialogue.Serializer;
import com.palantir.dialogue.TypeMarker;
import com.palantir.ri.ResourceIdentifier;
import com.palantir.tokens.auth.AuthHeader;
import com.palantir.tokens.auth.BearerToken;
import java.io.InputStream;
import java.lang.Boolean;
import java.lang.Double;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.Void;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.dialogue.DialogueInterfaceGenerator")
public interface EteServiceAsync {
    /**
     * foo bar baz.
     *
     * <h2>Very Important Documentation</h2>
     *
     * <p>This documentation provides a <em>list</em>:
     *
     * <ul>
     *   <li>Docs rule
     *   <li>Lists are wonderful
     * </ul>
     */
    ListenableFuture<String> string(AuthHeader authHeader);

    /** one <em>two</em> three. */
    ListenableFuture<Integer> integer(AuthHeader authHeader);

    ListenableFuture<Double> double_(AuthHeader authHeader);

    ListenableFuture<Boolean> boolean_(AuthHeader authHeader);

    ListenableFuture<SafeLong> safelong(AuthHeader authHeader);

    ListenableFuture<ResourceIdentifier> rid(AuthHeader authHeader);

    ListenableFuture<BearerToken> bearertoken(AuthHeader authHeader);

    ListenableFuture<Optional<String>> optionalString(AuthHeader authHeader);

    ListenableFuture<Optional<String>> optionalEmpty(AuthHeader authHeader);

    ListenableFuture<OffsetDateTime> datetime(AuthHeader authHeader);

    ListenableFuture<InputStream> binary(AuthHeader authHeader);

    /**
     * Path endpoint.
     *
     * @param param Documentation for <code>param</code>
     */
    ListenableFuture<String> path(AuthHeader authHeader, String param);

    ListenableFuture<Long> externalLongPath(AuthHeader authHeader, long param);

    ListenableFuture<Optional<Long>> optionalExternalLongQuery(
            AuthHeader authHeader, Optional<Long> param);

    ListenableFuture<StringAliasExample> notNullBody(
            AuthHeader authHeader, StringAliasExample notNullBody);

    ListenableFuture<StringAliasExample> aliasOne(
            AuthHeader authHeader, StringAliasExample queryParamName);

    ListenableFuture<StringAliasExample> optionalAliasOne(
            AuthHeader authHeader, Optional<StringAliasExample> queryParamName);

    ListenableFuture<NestedStringAliasExample> aliasTwo(
            AuthHeader authHeader, NestedStringAliasExample queryParamName);

    ListenableFuture<StringAliasExample> notNullBodyExternalImport(
            AuthHeader authHeader, StringAliasExample notNullBody);

    ListenableFuture<Optional<StringAliasExample>> optionalBodyExternalImport(
            AuthHeader authHeader, Optional<StringAliasExample> body);

    ListenableFuture<Optional<StringAliasExample>> optionalQueryExternalImport(
            AuthHeader authHeader, Optional<StringAliasExample> query);

    ListenableFuture<Void> noReturn(AuthHeader authHeader);

    ListenableFuture<SimpleEnum> enumQuery(AuthHeader authHeader, SimpleEnum queryParamName);

    ListenableFuture<List<SimpleEnum>> enumListQuery(
            AuthHeader authHeader, List<SimpleEnum> queryParamName);

    ListenableFuture<Optional<SimpleEnum>> optionalEnumQuery(
            AuthHeader authHeader, Optional<SimpleEnum> queryParamName);

    ListenableFuture<SimpleEnum> enumHeader(AuthHeader authHeader, SimpleEnum headerParameter);

    ListenableFuture<Optional<LongAlias>> aliasLongEndpoint(
            AuthHeader authHeader, Optional<LongAlias> input);

    ListenableFuture<Void> complexQueryParameters(
            AuthHeader authHeader,
            ResourceIdentifier datasetRid,
            Set<StringAliasExample> strings,
            Set<Long> longs,
            Set<Integer> ints);

    /** Creates an asynchronous/non-blocking client for a EteService service. */
    static EteServiceAsync of(Channel channel, ConjureRuntime runtime) {
        return new EteServiceAsync() {
            private final PlainSerDe plainSerDe = runtime.plainSerDe();

            private final Deserializer<String> stringDeserializer =
                    runtime.bodySerDe().deserializer(new TypeMarker<String>() {});

            private final Deserializer<Integer> integerDeserializer =
                    runtime.bodySerDe().deserializer(new TypeMarker<Integer>() {});

            private final Deserializer<Double> double_Deserializer =
                    runtime.bodySerDe().deserializer(new TypeMarker<Double>() {});

            private final Deserializer<Boolean> boolean_Deserializer =
                    runtime.bodySerDe().deserializer(new TypeMarker<Boolean>() {});

            private final Deserializer<SafeLong> safelongDeserializer =
                    runtime.bodySerDe().deserializer(new TypeMarker<SafeLong>() {});

            private final Deserializer<ResourceIdentifier> ridDeserializer =
                    runtime.bodySerDe().deserializer(new TypeMarker<ResourceIdentifier>() {});

            private final Deserializer<BearerToken> bearertokenDeserializer =
                    runtime.bodySerDe().deserializer(new TypeMarker<BearerToken>() {});

            private final Deserializer<Optional<String>> optionalStringDeserializer =
                    runtime.bodySerDe().deserializer(new TypeMarker<Optional<String>>() {});

            private final Deserializer<Optional<String>> optionalEmptyDeserializer =
                    runtime.bodySerDe().deserializer(new TypeMarker<Optional<String>>() {});

            private final Deserializer<OffsetDateTime> datetimeDeserializer =
                    runtime.bodySerDe().deserializer(new TypeMarker<OffsetDateTime>() {});

            private final Deserializer<String> pathDeserializer =
                    runtime.bodySerDe().deserializer(new TypeMarker<String>() {});

            private final Deserializer<Long> externalLongPathDeserializer =
                    runtime.bodySerDe().deserializer(new TypeMarker<Long>() {});

            private final Deserializer<Optional<Long>> optionalExternalLongQueryDeserializer =
                    runtime.bodySerDe().deserializer(new TypeMarker<Optional<Long>>() {});

            private final Serializer<StringAliasExample> notNullBodySerializer =
                    runtime.bodySerDe().serializer(new TypeMarker<StringAliasExample>() {});

            private final Deserializer<StringAliasExample> notNullBodyDeserializer =
                    runtime.bodySerDe().deserializer(new TypeMarker<StringAliasExample>() {});

            private final Deserializer<StringAliasExample> aliasOneDeserializer =
                    runtime.bodySerDe().deserializer(new TypeMarker<StringAliasExample>() {});

            private final Deserializer<StringAliasExample> optionalAliasOneDeserializer =
                    runtime.bodySerDe().deserializer(new TypeMarker<StringAliasExample>() {});

            private final Deserializer<NestedStringAliasExample> aliasTwoDeserializer =
                    runtime.bodySerDe().deserializer(new TypeMarker<NestedStringAliasExample>() {});

            private final Serializer<StringAliasExample> notNullBodyExternalImportSerializer =
                    runtime.bodySerDe().serializer(new TypeMarker<StringAliasExample>() {});

            private final Deserializer<StringAliasExample> notNullBodyExternalImportDeserializer =
                    runtime.bodySerDe().deserializer(new TypeMarker<StringAliasExample>() {});

            private final Serializer<Optional<StringAliasExample>>
                    optionalBodyExternalImportSerializer =
                            runtime.bodySerDe()
                                    .serializer(new TypeMarker<Optional<StringAliasExample>>() {});

            private final Deserializer<Optional<StringAliasExample>>
                    optionalBodyExternalImportDeserializer =
                            runtime.bodySerDe()
                                    .deserializer(
                                            new TypeMarker<Optional<StringAliasExample>>() {});

            private final Deserializer<Optional<StringAliasExample>>
                    optionalQueryExternalImportDeserializer =
                            runtime.bodySerDe()
                                    .deserializer(
                                            new TypeMarker<Optional<StringAliasExample>>() {});

            private final Deserializer<Void> noReturnDeserializer =
                    runtime.bodySerDe().emptyBodyDeserializer();

            private final Deserializer<SimpleEnum> enumQueryDeserializer =
                    runtime.bodySerDe().deserializer(new TypeMarker<SimpleEnum>() {});

            private final Deserializer<List<SimpleEnum>> enumListQueryDeserializer =
                    runtime.bodySerDe().deserializer(new TypeMarker<List<SimpleEnum>>() {});

            private final Deserializer<Optional<SimpleEnum>> optionalEnumQueryDeserializer =
                    runtime.bodySerDe().deserializer(new TypeMarker<Optional<SimpleEnum>>() {});

            private final Deserializer<SimpleEnum> enumHeaderDeserializer =
                    runtime.bodySerDe().deserializer(new TypeMarker<SimpleEnum>() {});

            private final Deserializer<Optional<LongAlias>> aliasLongEndpointDeserializer =
                    runtime.bodySerDe().deserializer(new TypeMarker<Optional<LongAlias>>() {});

            private final Deserializer<Void> complexQueryParametersDeserializer =
                    runtime.bodySerDe().emptyBodyDeserializer();

            @Override
            public ListenableFuture<String> string(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                return runtime.clients()
                        .call(
                                channel,
                                DialogueEteEndpoints.string,
                                _request.build(),
                                stringDeserializer::deserialize);
            }

            @Override
            public ListenableFuture<Integer> integer(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                return runtime.clients()
                        .call(
                                channel,
                                DialogueEteEndpoints.integer,
                                _request.build(),
                                integerDeserializer::deserialize);
            }

            @Override
            public ListenableFuture<Double> double_(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                return runtime.clients()
                        .call(
                                channel,
                                DialogueEteEndpoints.double_,
                                _request.build(),
                                double_Deserializer::deserialize);
            }

            @Override
            public ListenableFuture<Boolean> boolean_(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                return runtime.clients()
                        .call(
                                channel,
                                DialogueEteEndpoints.boolean_,
                                _request.build(),
                                boolean_Deserializer::deserialize);
            }

            @Override
            public ListenableFuture<SafeLong> safelong(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                return runtime.clients()
                        .call(
                                channel,
                                DialogueEteEndpoints.safelong,
                                _request.build(),
                                safelongDeserializer::deserialize);
            }

            @Override
            public ListenableFuture<ResourceIdentifier> rid(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                return runtime.clients()
                        .call(
                                channel,
                                DialogueEteEndpoints.rid,
                                _request.build(),
                                ridDeserializer::deserialize);
            }

            @Override
            public ListenableFuture<BearerToken> bearertoken(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                return runtime.clients()
                        .call(
                                channel,
                                DialogueEteEndpoints.bearertoken,
                                _request.build(),
                                bearertokenDeserializer::deserialize);
            }

            @Override
            public ListenableFuture<Optional<String>> optionalString(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                return runtime.clients()
                        .call(
                                channel,
                                DialogueEteEndpoints.optionalString,
                                _request.build(),
                                optionalStringDeserializer::deserialize);
            }

            @Override
            public ListenableFuture<Optional<String>> optionalEmpty(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                return runtime.clients()
                        .call(
                                channel,
                                DialogueEteEndpoints.optionalEmpty,
                                _request.build(),
                                optionalEmptyDeserializer::deserialize);
            }

            @Override
            public ListenableFuture<OffsetDateTime> datetime(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                return runtime.clients()
                        .call(
                                channel,
                                DialogueEteEndpoints.datetime,
                                _request.build(),
                                datetimeDeserializer::deserialize);
            }

            @Override
            public ListenableFuture<InputStream> binary(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                return runtime.clients()
                        .call(
                                channel,
                                DialogueEteEndpoints.binary,
                                _request.build(),
                                runtime.bodySerDe()::deserializeInputStream);
            }

            @Override
            public ListenableFuture<String> path(AuthHeader authHeader, String param) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                _request.putPathParams("param", plainSerDe.serializeString(param));
                return runtime.clients()
                        .call(
                                channel,
                                DialogueEteEndpoints.path,
                                _request.build(),
                                pathDeserializer::deserialize);
            }

            @Override
            public ListenableFuture<Long> externalLongPath(AuthHeader authHeader, long param) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                _request.putPathParams("param", Objects.toString(param));
                return runtime.clients()
                        .call(
                                channel,
                                DialogueEteEndpoints.externalLongPath,
                                _request.build(),
                                externalLongPathDeserializer::deserialize);
            }

            @Override
            public ListenableFuture<Optional<Long>> optionalExternalLongQuery(
                    AuthHeader authHeader, Optional<Long> param) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                if (param.isPresent()) {
                    _request.putQueryParams("param", Objects.toString(param.get()));
                }
                return runtime.clients()
                        .call(
                                channel,
                                DialogueEteEndpoints.optionalExternalLongQuery,
                                _request.build(),
                                optionalExternalLongQueryDeserializer::deserialize);
            }

            @Override
            public ListenableFuture<StringAliasExample> notNullBody(
                    AuthHeader authHeader, StringAliasExample notNullBody) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                _request.body(notNullBodySerializer.serialize(notNullBody));
                return runtime.clients()
                        .call(
                                channel,
                                DialogueEteEndpoints.notNullBody,
                                _request.build(),
                                notNullBodyDeserializer::deserialize);
            }

            @Override
            public ListenableFuture<StringAliasExample> aliasOne(
                    AuthHeader authHeader, StringAliasExample queryParamName) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                _request.putQueryParams(
                        "queryParamName", plainSerDe.serializeString(queryParamName.get()));
                return runtime.clients()
                        .call(
                                channel,
                                DialogueEteEndpoints.aliasOne,
                                _request.build(),
                                aliasOneDeserializer::deserialize);
            }

            @Override
            public ListenableFuture<StringAliasExample> optionalAliasOne(
                    AuthHeader authHeader, Optional<StringAliasExample> queryParamName) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                if (queryParamName.isPresent()) {
                    _request.putQueryParams(
                            "queryParamName",
                            plainSerDe.serializeString(queryParamName.get().get()));
                }
                return runtime.clients()
                        .call(
                                channel,
                                DialogueEteEndpoints.optionalAliasOne,
                                _request.build(),
                                optionalAliasOneDeserializer::deserialize);
            }

            @Override
            public ListenableFuture<NestedStringAliasExample> aliasTwo(
                    AuthHeader authHeader, NestedStringAliasExample queryParamName) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                _request.putQueryParams(
                        "queryParamName", plainSerDe.serializeString(queryParamName.get().get()));
                return runtime.clients()
                        .call(
                                channel,
                                DialogueEteEndpoints.aliasTwo,
                                _request.build(),
                                aliasTwoDeserializer::deserialize);
            }

            @Override
            public ListenableFuture<StringAliasExample> notNullBodyExternalImport(
                    AuthHeader authHeader, StringAliasExample notNullBody) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                _request.body(notNullBodyExternalImportSerializer.serialize(notNullBody));
                return runtime.clients()
                        .call(
                                channel,
                                DialogueEteEndpoints.notNullBodyExternalImport,
                                _request.build(),
                                notNullBodyExternalImportDeserializer::deserialize);
            }

            @Override
            public ListenableFuture<Optional<StringAliasExample>> optionalBodyExternalImport(
                    AuthHeader authHeader, Optional<StringAliasExample> body) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                _request.body(optionalBodyExternalImportSerializer.serialize(body));
                return runtime.clients()
                        .call(
                                channel,
                                DialogueEteEndpoints.optionalBodyExternalImport,
                                _request.build(),
                                optionalBodyExternalImportDeserializer::deserialize);
            }

            @Override
            public ListenableFuture<Optional<StringAliasExample>> optionalQueryExternalImport(
                    AuthHeader authHeader, Optional<StringAliasExample> query) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                if (query.isPresent()) {
                    _request.putQueryParams("query", Objects.toString(query.get()));
                }
                return runtime.clients()
                        .call(
                                channel,
                                DialogueEteEndpoints.optionalQueryExternalImport,
                                _request.build(),
                                optionalQueryExternalImportDeserializer::deserialize);
            }

            @Override
            public ListenableFuture<Void> noReturn(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                return runtime.clients()
                        .call(
                                channel,
                                DialogueEteEndpoints.noReturn,
                                _request.build(),
                                noReturnDeserializer::deserialize);
            }

            @Override
            public ListenableFuture<SimpleEnum> enumQuery(
                    AuthHeader authHeader, SimpleEnum queryParamName) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                _request.putQueryParams("queryParamName", Objects.toString(queryParamName));
                return runtime.clients()
                        .call(
                                channel,
                                DialogueEteEndpoints.enumQuery,
                                _request.build(),
                                enumQueryDeserializer::deserialize);
            }

            @Override
            public ListenableFuture<List<SimpleEnum>> enumListQuery(
                    AuthHeader authHeader, List<SimpleEnum> queryParamName) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                for (SimpleEnum queryParamNameElement : queryParamName) {
                    _request.putQueryParams(
                            "queryParamName", Objects.toString(queryParamNameElement));
                }
                return runtime.clients()
                        .call(
                                channel,
                                DialogueEteEndpoints.enumListQuery,
                                _request.build(),
                                enumListQueryDeserializer::deserialize);
            }

            @Override
            public ListenableFuture<Optional<SimpleEnum>> optionalEnumQuery(
                    AuthHeader authHeader, Optional<SimpleEnum> queryParamName) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                if (queryParamName.isPresent()) {
                    _request.putQueryParams(
                            "queryParamName", Objects.toString(queryParamName.get()));
                }
                return runtime.clients()
                        .call(
                                channel,
                                DialogueEteEndpoints.optionalEnumQuery,
                                _request.build(),
                                optionalEnumQueryDeserializer::deserialize);
            }

            @Override
            public ListenableFuture<SimpleEnum> enumHeader(
                    AuthHeader authHeader, SimpleEnum headerParameter) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                _request.putHeaderParams("Custom-Header", Objects.toString(headerParameter));
                return runtime.clients()
                        .call(
                                channel,
                                DialogueEteEndpoints.enumHeader,
                                _request.build(),
                                enumHeaderDeserializer::deserialize);
            }

            @Override
            public ListenableFuture<Optional<LongAlias>> aliasLongEndpoint(
                    AuthHeader authHeader, Optional<LongAlias> input) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                if (input.isPresent()) {
                    _request.putQueryParams("input", Objects.toString(input.get().get()));
                }
                return runtime.clients()
                        .call(
                                channel,
                                DialogueEteEndpoints.aliasLongEndpoint,
                                _request.build(),
                                aliasLongEndpointDeserializer::deserialize);
            }

            @Override
            public ListenableFuture<Void> complexQueryParameters(
                    AuthHeader authHeader,
                    ResourceIdentifier datasetRid,
                    Set<StringAliasExample> strings,
                    Set<Long> longs,
                    Set<Integer> ints) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                _request.putPathParams("datasetRid", plainSerDe.serializeRid(datasetRid));
                for (StringAliasExample stringsElement : strings) {
                    _request.putQueryParams(
                            "strings", plainSerDe.serializeString(stringsElement.get()));
                }
                for (long longsElement : longs) {
                    _request.putQueryParams("longs", Objects.toString(longsElement));
                }
                for (int intsElement : ints) {
                    _request.putQueryParams("ints", plainSerDe.serializeInteger(intsElement));
                }
                return runtime.clients()
                        .call(
                                channel,
                                DialogueEteEndpoints.complexQueryParameters,
                                _request.build(),
                                complexQueryParametersDeserializer::deserialize);
            }
        };
    }
}
