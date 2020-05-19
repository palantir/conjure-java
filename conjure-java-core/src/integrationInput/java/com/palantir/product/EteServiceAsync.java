package com.palantir.product;

import com.google.common.util.concurrent.ListenableFuture;
import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.dialogue.Channel;
import com.palantir.dialogue.ConjureRuntime;
import com.palantir.dialogue.Deserializer;
import com.palantir.dialogue.EndpointChannel;
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
     * <h2>Very Important Documentation</h2>
     * <p>This documentation provides a <em>list</em>:</p>
     * <ul>
     * <li>Docs rule</li>
     * <li>Lists are wonderful</li>
     * </ul>
     */
    ListenableFuture<String> string(AuthHeader authHeader);

    /**
     * one <em>two</em> three.
     */
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
     * @param param Documentation for <code>param</code>
     */
    ListenableFuture<String> path(AuthHeader authHeader, String param);

    ListenableFuture<Long> externalLongPath(AuthHeader authHeader, long param);

    ListenableFuture<Optional<Long>> optionalExternalLongQuery(AuthHeader authHeader, Optional<Long> param);

    ListenableFuture<StringAliasExample> notNullBody(AuthHeader authHeader, StringAliasExample notNullBody);

    ListenableFuture<StringAliasExample> aliasOne(AuthHeader authHeader, StringAliasExample queryParamName);

    ListenableFuture<StringAliasExample> optionalAliasOne(
            AuthHeader authHeader, Optional<StringAliasExample> queryParamName);

    ListenableFuture<NestedStringAliasExample> aliasTwo(AuthHeader authHeader, NestedStringAliasExample queryParamName);

    ListenableFuture<StringAliasExample> notNullBodyExternalImport(
            AuthHeader authHeader, StringAliasExample notNullBody);

    ListenableFuture<Optional<StringAliasExample>> optionalBodyExternalImport(
            AuthHeader authHeader, Optional<StringAliasExample> body);

    ListenableFuture<Optional<StringAliasExample>> optionalQueryExternalImport(
            AuthHeader authHeader, Optional<StringAliasExample> query);

    ListenableFuture<Void> noReturn(AuthHeader authHeader);

    ListenableFuture<SimpleEnum> enumQuery(AuthHeader authHeader, SimpleEnum queryParamName);

    ListenableFuture<List<SimpleEnum>> enumListQuery(AuthHeader authHeader, List<SimpleEnum> queryParamName);

    ListenableFuture<Optional<SimpleEnum>> optionalEnumQuery(
            AuthHeader authHeader, Optional<SimpleEnum> queryParamName);

    ListenableFuture<SimpleEnum> enumHeader(AuthHeader authHeader, SimpleEnum headerParameter);

    ListenableFuture<Optional<LongAlias>> aliasLongEndpoint(AuthHeader authHeader, Optional<LongAlias> input);

    ListenableFuture<Void> complexQueryParameters(
            AuthHeader authHeader,
            ResourceIdentifier datasetRid,
            Set<StringAliasExample> strings,
            Set<Long> longs,
            Set<Integer> ints);

    /**
     * Creates an asynchronous/non-blocking client for a EteService service.
     */
    static EteServiceAsync of(Channel _channel, ConjureRuntime _runtime) {
        return new EteServiceAsync() {
            private final PlainSerDe _plainSerDe = _runtime.plainSerDe();

            private final EndpointChannel stringChannel =
                    _runtime.clients().bind(_channel, DialogueEteEndpoints.string);

            private final Deserializer<String> stringDeserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<String>() {});

            private final EndpointChannel integerChannel =
                    _runtime.clients().bind(_channel, DialogueEteEndpoints.integer);

            private final Deserializer<Integer> integerDeserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<Integer>() {});

            private final EndpointChannel double_Channel =
                    _runtime.clients().bind(_channel, DialogueEteEndpoints.double_);

            private final Deserializer<Double> double_Deserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<Double>() {});

            private final EndpointChannel boolean_Channel =
                    _runtime.clients().bind(_channel, DialogueEteEndpoints.boolean_);

            private final Deserializer<Boolean> boolean_Deserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<Boolean>() {});

            private final EndpointChannel safelongChannel =
                    _runtime.clients().bind(_channel, DialogueEteEndpoints.safelong);

            private final Deserializer<SafeLong> safelongDeserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<SafeLong>() {});

            private final EndpointChannel ridChannel = _runtime.clients().bind(_channel, DialogueEteEndpoints.rid);

            private final Deserializer<ResourceIdentifier> ridDeserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<ResourceIdentifier>() {});

            private final EndpointChannel bearertokenChannel =
                    _runtime.clients().bind(_channel, DialogueEteEndpoints.bearertoken);

            private final Deserializer<BearerToken> bearertokenDeserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<BearerToken>() {});

            private final EndpointChannel optionalStringChannel =
                    _runtime.clients().bind(_channel, DialogueEteEndpoints.optionalString);

            private final Deserializer<Optional<String>> optionalStringDeserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<Optional<String>>() {});

            private final EndpointChannel optionalEmptyChannel =
                    _runtime.clients().bind(_channel, DialogueEteEndpoints.optionalEmpty);

            private final Deserializer<Optional<String>> optionalEmptyDeserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<Optional<String>>() {});

            private final EndpointChannel datetimeChannel =
                    _runtime.clients().bind(_channel, DialogueEteEndpoints.datetime);

            private final Deserializer<OffsetDateTime> datetimeDeserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<OffsetDateTime>() {});

            private final EndpointChannel binaryChannel =
                    _runtime.clients().bind(_channel, DialogueEteEndpoints.binary);

            private final EndpointChannel pathChannel = _runtime.clients().bind(_channel, DialogueEteEndpoints.path);

            private final Deserializer<String> pathDeserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<String>() {});

            private final EndpointChannel externalLongPathChannel =
                    _runtime.clients().bind(_channel, DialogueEteEndpoints.externalLongPath);

            private final Deserializer<Long> externalLongPathDeserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<Long>() {});

            private final EndpointChannel optionalExternalLongQueryChannel =
                    _runtime.clients().bind(_channel, DialogueEteEndpoints.optionalExternalLongQuery);

            private final Deserializer<Optional<Long>> optionalExternalLongQueryDeserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<Optional<Long>>() {});

            private final Serializer<StringAliasExample> notNullBodySerializer =
                    _runtime.bodySerDe().serializer(new TypeMarker<StringAliasExample>() {});

            private final EndpointChannel notNullBodyChannel =
                    _runtime.clients().bind(_channel, DialogueEteEndpoints.notNullBody);

            private final Deserializer<StringAliasExample> notNullBodyDeserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<StringAliasExample>() {});

            private final EndpointChannel aliasOneChannel =
                    _runtime.clients().bind(_channel, DialogueEteEndpoints.aliasOne);

            private final Deserializer<StringAliasExample> aliasOneDeserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<StringAliasExample>() {});

            private final EndpointChannel optionalAliasOneChannel =
                    _runtime.clients().bind(_channel, DialogueEteEndpoints.optionalAliasOne);

            private final Deserializer<StringAliasExample> optionalAliasOneDeserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<StringAliasExample>() {});

            private final EndpointChannel aliasTwoChannel =
                    _runtime.clients().bind(_channel, DialogueEteEndpoints.aliasTwo);

            private final Deserializer<NestedStringAliasExample> aliasTwoDeserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<NestedStringAliasExample>() {});

            private final Serializer<StringAliasExample> notNullBodyExternalImportSerializer =
                    _runtime.bodySerDe().serializer(new TypeMarker<StringAliasExample>() {});

            private final EndpointChannel notNullBodyExternalImportChannel =
                    _runtime.clients().bind(_channel, DialogueEteEndpoints.notNullBodyExternalImport);

            private final Deserializer<StringAliasExample> notNullBodyExternalImportDeserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<StringAliasExample>() {});

            private final Serializer<Optional<StringAliasExample>> optionalBodyExternalImportSerializer =
                    _runtime.bodySerDe().serializer(new TypeMarker<Optional<StringAliasExample>>() {});

            private final EndpointChannel optionalBodyExternalImportChannel =
                    _runtime.clients().bind(_channel, DialogueEteEndpoints.optionalBodyExternalImport);

            private final Deserializer<Optional<StringAliasExample>> optionalBodyExternalImportDeserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<Optional<StringAliasExample>>() {});

            private final EndpointChannel optionalQueryExternalImportChannel =
                    _runtime.clients().bind(_channel, DialogueEteEndpoints.optionalQueryExternalImport);

            private final Deserializer<Optional<StringAliasExample>> optionalQueryExternalImportDeserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<Optional<StringAliasExample>>() {});

            private final EndpointChannel noReturnChannel =
                    _runtime.clients().bind(_channel, DialogueEteEndpoints.noReturn);

            private final Deserializer<Void> noReturnDeserializer =
                    _runtime.bodySerDe().emptyBodyDeserializer();

            private final EndpointChannel enumQueryChannel =
                    _runtime.clients().bind(_channel, DialogueEteEndpoints.enumQuery);

            private final Deserializer<SimpleEnum> enumQueryDeserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<SimpleEnum>() {});

            private final EndpointChannel enumListQueryChannel =
                    _runtime.clients().bind(_channel, DialogueEteEndpoints.enumListQuery);

            private final Deserializer<List<SimpleEnum>> enumListQueryDeserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<List<SimpleEnum>>() {});

            private final EndpointChannel optionalEnumQueryChannel =
                    _runtime.clients().bind(_channel, DialogueEteEndpoints.optionalEnumQuery);

            private final Deserializer<Optional<SimpleEnum>> optionalEnumQueryDeserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<Optional<SimpleEnum>>() {});

            private final EndpointChannel enumHeaderChannel =
                    _runtime.clients().bind(_channel, DialogueEteEndpoints.enumHeader);

            private final Deserializer<SimpleEnum> enumHeaderDeserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<SimpleEnum>() {});

            private final EndpointChannel aliasLongEndpointChannel =
                    _runtime.clients().bind(_channel, DialogueEteEndpoints.aliasLongEndpoint);

            private final Deserializer<Optional<LongAlias>> aliasLongEndpointDeserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<Optional<LongAlias>>() {});

            private final EndpointChannel complexQueryParametersChannel =
                    _runtime.clients().bind(_channel, DialogueEteEndpoints.complexQueryParameters);

            private final Deserializer<Void> complexQueryParametersDeserializer =
                    _runtime.bodySerDe().emptyBodyDeserializer();

            @Override
            public ListenableFuture<String> string(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                return _runtime.clients().call(stringChannel, _request.build(), stringDeserializer);
            }

            @Override
            public ListenableFuture<Integer> integer(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                return _runtime.clients().call(integerChannel, _request.build(), integerDeserializer);
            }

            @Override
            public ListenableFuture<Double> double_(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                return _runtime.clients().call(double_Channel, _request.build(), double_Deserializer);
            }

            @Override
            public ListenableFuture<Boolean> boolean_(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                return _runtime.clients().call(boolean_Channel, _request.build(), boolean_Deserializer);
            }

            @Override
            public ListenableFuture<SafeLong> safelong(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                return _runtime.clients().call(safelongChannel, _request.build(), safelongDeserializer);
            }

            @Override
            public ListenableFuture<ResourceIdentifier> rid(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                return _runtime.clients().call(ridChannel, _request.build(), ridDeserializer);
            }

            @Override
            public ListenableFuture<BearerToken> bearertoken(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                return _runtime.clients().call(bearertokenChannel, _request.build(), bearertokenDeserializer);
            }

            @Override
            public ListenableFuture<Optional<String>> optionalString(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                return _runtime.clients().call(optionalStringChannel, _request.build(), optionalStringDeserializer);
            }

            @Override
            public ListenableFuture<Optional<String>> optionalEmpty(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                return _runtime.clients().call(optionalEmptyChannel, _request.build(), optionalEmptyDeserializer);
            }

            @Override
            public ListenableFuture<OffsetDateTime> datetime(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                return _runtime.clients().call(datetimeChannel, _request.build(), datetimeDeserializer);
            }

            @Override
            public ListenableFuture<InputStream> binary(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                return _runtime.clients()
                        .call(
                                binaryChannel,
                                _request.build(),
                                _runtime.bodySerDe().inputStreamDeserializer());
            }

            @Override
            public ListenableFuture<String> path(AuthHeader authHeader, String param) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.putPathParams("param", _plainSerDe.serializeString(param));
                return _runtime.clients().call(pathChannel, _request.build(), pathDeserializer);
            }

            @Override
            public ListenableFuture<Long> externalLongPath(AuthHeader authHeader, long param) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.putPathParams("param", Objects.toString(param));
                return _runtime.clients().call(externalLongPathChannel, _request.build(), externalLongPathDeserializer);
            }

            @Override
            public ListenableFuture<Optional<Long>> optionalExternalLongQuery(
                    AuthHeader authHeader, Optional<Long> param) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                if (param.isPresent()) {
                    _request.putQueryParams("param", Objects.toString(param.get()));
                }
                return _runtime.clients()
                        .call(
                                optionalExternalLongQueryChannel,
                                _request.build(),
                                optionalExternalLongQueryDeserializer);
            }

            @Override
            public ListenableFuture<StringAliasExample> notNullBody(
                    AuthHeader authHeader, StringAliasExample notNullBody) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.body(notNullBodySerializer.serialize(notNullBody));
                return _runtime.clients().call(notNullBodyChannel, _request.build(), notNullBodyDeserializer);
            }

            @Override
            public ListenableFuture<StringAliasExample> aliasOne(
                    AuthHeader authHeader, StringAliasExample queryParamName) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.putQueryParams("queryParamName", _plainSerDe.serializeString(queryParamName.get()));
                return _runtime.clients().call(aliasOneChannel, _request.build(), aliasOneDeserializer);
            }

            @Override
            public ListenableFuture<StringAliasExample> optionalAliasOne(
                    AuthHeader authHeader, Optional<StringAliasExample> queryParamName) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                if (queryParamName.isPresent()) {
                    _request.putQueryParams(
                            "queryParamName",
                            _plainSerDe.serializeString(queryParamName.get().get()));
                }
                return _runtime.clients().call(optionalAliasOneChannel, _request.build(), optionalAliasOneDeserializer);
            }

            @Override
            public ListenableFuture<NestedStringAliasExample> aliasTwo(
                    AuthHeader authHeader, NestedStringAliasExample queryParamName) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.putQueryParams(
                        "queryParamName",
                        _plainSerDe.serializeString(queryParamName.get().get()));
                return _runtime.clients().call(aliasTwoChannel, _request.build(), aliasTwoDeserializer);
            }

            @Override
            public ListenableFuture<StringAliasExample> notNullBodyExternalImport(
                    AuthHeader authHeader, StringAliasExample notNullBody) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.body(notNullBodyExternalImportSerializer.serialize(notNullBody));
                return _runtime.clients()
                        .call(
                                notNullBodyExternalImportChannel,
                                _request.build(),
                                notNullBodyExternalImportDeserializer);
            }

            @Override
            public ListenableFuture<Optional<StringAliasExample>> optionalBodyExternalImport(
                    AuthHeader authHeader, Optional<StringAliasExample> body) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.body(optionalBodyExternalImportSerializer.serialize(body));
                return _runtime.clients()
                        .call(
                                optionalBodyExternalImportChannel,
                                _request.build(),
                                optionalBodyExternalImportDeserializer);
            }

            @Override
            public ListenableFuture<Optional<StringAliasExample>> optionalQueryExternalImport(
                    AuthHeader authHeader, Optional<StringAliasExample> query) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                if (query.isPresent()) {
                    _request.putQueryParams("query", Objects.toString(query.get()));
                }
                return _runtime.clients()
                        .call(
                                optionalQueryExternalImportChannel,
                                _request.build(),
                                optionalQueryExternalImportDeserializer);
            }

            @Override
            public ListenableFuture<Void> noReturn(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                return _runtime.clients().call(noReturnChannel, _request.build(), noReturnDeserializer);
            }

            @Override
            public ListenableFuture<SimpleEnum> enumQuery(AuthHeader authHeader, SimpleEnum queryParamName) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.putQueryParams("queryParamName", Objects.toString(queryParamName));
                return _runtime.clients().call(enumQueryChannel, _request.build(), enumQueryDeserializer);
            }

            @Override
            public ListenableFuture<List<SimpleEnum>> enumListQuery(
                    AuthHeader authHeader, List<SimpleEnum> queryParamName) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                for (SimpleEnum queryParamNameElement : queryParamName) {
                    _request.putQueryParams("queryParamName", Objects.toString(queryParamNameElement));
                }
                return _runtime.clients().call(enumListQueryChannel, _request.build(), enumListQueryDeserializer);
            }

            @Override
            public ListenableFuture<Optional<SimpleEnum>> optionalEnumQuery(
                    AuthHeader authHeader, Optional<SimpleEnum> queryParamName) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                if (queryParamName.isPresent()) {
                    _request.putQueryParams("queryParamName", Objects.toString(queryParamName.get()));
                }
                return _runtime.clients()
                        .call(optionalEnumQueryChannel, _request.build(), optionalEnumQueryDeserializer);
            }

            @Override
            public ListenableFuture<SimpleEnum> enumHeader(AuthHeader authHeader, SimpleEnum headerParameter) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.putHeaderParams("Custom-Header", Objects.toString(headerParameter));
                return _runtime.clients().call(enumHeaderChannel, _request.build(), enumHeaderDeserializer);
            }

            @Override
            public ListenableFuture<Optional<LongAlias>> aliasLongEndpoint(
                    AuthHeader authHeader, Optional<LongAlias> input) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                if (input.isPresent()) {
                    _request.putQueryParams(
                            "input", Objects.toString(input.get().get()));
                }
                return _runtime.clients()
                        .call(aliasLongEndpointChannel, _request.build(), aliasLongEndpointDeserializer);
            }

            @Override
            public ListenableFuture<Void> complexQueryParameters(
                    AuthHeader authHeader,
                    ResourceIdentifier datasetRid,
                    Set<StringAliasExample> strings,
                    Set<Long> longs,
                    Set<Integer> ints) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.putPathParams("datasetRid", _plainSerDe.serializeRid(datasetRid));
                for (StringAliasExample stringsElement : strings) {
                    _request.putQueryParams("strings", _plainSerDe.serializeString(stringsElement.get()));
                }
                for (long longsElement : longs) {
                    _request.putQueryParams("longs", Objects.toString(longsElement));
                }
                for (int intsElement : ints) {
                    _request.putQueryParams("ints", _plainSerDe.serializeInteger(intsElement));
                }
                return _runtime.clients()
                        .call(complexQueryParametersChannel, _request.build(), complexQueryParametersDeserializer);
            }

            @Override
            public String toString() {
                return "EteServiceBlocking{channel=" + _channel + ", runtime=" + _runtime + '}';
            }
        };
    }
}
