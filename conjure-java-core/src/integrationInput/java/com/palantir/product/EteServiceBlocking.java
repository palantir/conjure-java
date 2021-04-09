package com.palantir.product;

import com.google.errorprone.annotations.MustBeClosed;
import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.conjure.java.lib.internal.ConjureEndpoint;
import com.palantir.conjure.java.lib.internal.ConjureService;
import com.palantir.dialogue.Channel;
import com.palantir.dialogue.ConjureRuntime;
import com.palantir.dialogue.Deserializer;
import com.palantir.dialogue.DialogueService;
import com.palantir.dialogue.DialogueServiceFactory;
import com.palantir.dialogue.Endpoint;
import com.palantir.dialogue.EndpointChannel;
import com.palantir.dialogue.EndpointChannelFactory;
import com.palantir.dialogue.PlainSerDe;
import com.palantir.dialogue.Request;
import com.palantir.dialogue.Serializer;
import com.palantir.dialogue.TypeMarker;
import com.palantir.ri.ResourceIdentifier;
import com.palantir.tokens.auth.AuthHeader;
import com.palantir.tokens.auth.BearerToken;
import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Generated;

@DialogueService(EteServiceBlocking.Factory.class)
@Generated("com.palantir.conjure.java.services.dialogue.DialogueInterfaceGenerator")
@ConjureService(name = "EteService", package_ = "com.palantir.product")
public interface EteServiceBlocking {
    /**
     * foo bar baz.
     * <h2>Very Important Documentation</h2>
     * <p>This documentation provides a <em>list</em>:</p>
     * <ul>
     * <li>Docs rule</li>
     * <li>Lists are wonderful</li>
     * </ul>
     * @apiNote {@code GET /base/string}
     */
    @ConjureEndpoint(path = "/base/string", method = "GET")
    String string(AuthHeader authHeader);

    /**
     * one <em>two</em> three.
     * @apiNote {@code GET /base/integer}
     */
    @ConjureEndpoint(path = "/base/integer", method = "GET")
    int integer(AuthHeader authHeader);

    /**
     * @apiNote {@code GET /base/double}
     */
    @ConjureEndpoint(path = "/base/double", method = "GET")
    double double_(AuthHeader authHeader);

    /**
     * @apiNote {@code GET /base/boolean}
     */
    @ConjureEndpoint(path = "/base/boolean", method = "GET")
    boolean boolean_(AuthHeader authHeader);

    /**
     * @apiNote {@code GET /base/safelong}
     */
    @ConjureEndpoint(path = "/base/safelong", method = "GET")
    SafeLong safelong(AuthHeader authHeader);

    /**
     * @apiNote {@code GET /base/rid}
     */
    @ConjureEndpoint(path = "/base/rid", method = "GET")
    ResourceIdentifier rid(AuthHeader authHeader);

    /**
     * @apiNote {@code GET /base/bearertoken}
     */
    @ConjureEndpoint(path = "/base/bearertoken", method = "GET")
    BearerToken bearertoken(AuthHeader authHeader);

    /**
     * @apiNote {@code GET /base/optionalString}
     */
    @ConjureEndpoint(path = "/base/optionalString", method = "GET")
    Optional<String> optionalString(AuthHeader authHeader);

    /**
     * @apiNote {@code GET /base/optionalEmpty}
     */
    @ConjureEndpoint(path = "/base/optionalEmpty", method = "GET")
    Optional<String> optionalEmpty(AuthHeader authHeader);

    /**
     * @apiNote {@code GET /base/datetime}
     */
    @ConjureEndpoint(path = "/base/datetime", method = "GET")
    OffsetDateTime datetime(AuthHeader authHeader);

    /**
     * @apiNote {@code GET /base/binary}
     */
    @ConjureEndpoint(path = "/base/binary", method = "GET")
    @MustBeClosed
    InputStream binary(AuthHeader authHeader);

    /**
     * Path endpoint.
     * @apiNote {@code GET /base/path/{param}}
     * @param param Documentation for <code>param</code>
     */
    @ConjureEndpoint(path = "/base/path/{param}", method = "GET")
    String path(AuthHeader authHeader, String param);

    /**
     * @apiNote {@code GET /base/externalLong/{param}}
     */
    @ConjureEndpoint(path = "/base/externalLong/{param}", method = "GET")
    long externalLongPath(AuthHeader authHeader, long param);

    /**
     * @apiNote {@code GET /base/optionalExternalLong}
     */
    @ConjureEndpoint(path = "/base/optionalExternalLong", method = "GET")
    Optional<Long> optionalExternalLongQuery(AuthHeader authHeader, Optional<Long> param);

    /**
     * @apiNote {@code POST /base/notNullBody}
     */
    @ConjureEndpoint(path = "/base/notNullBody", method = "POST")
    StringAliasExample notNullBody(AuthHeader authHeader, StringAliasExample notNullBody);

    /**
     * @apiNote {@code GET /base/aliasOne}
     */
    @ConjureEndpoint(path = "/base/aliasOne", method = "GET")
    StringAliasExample aliasOne(AuthHeader authHeader, StringAliasExample queryParamName);

    /**
     * @apiNote {@code GET /base/optionalAliasOne}
     */
    @ConjureEndpoint(path = "/base/optionalAliasOne", method = "GET")
    StringAliasExample optionalAliasOne(AuthHeader authHeader, Optional<StringAliasExample> queryParamName);

    /**
     * @apiNote {@code GET /base/aliasTwo}
     */
    @ConjureEndpoint(path = "/base/aliasTwo", method = "GET")
    NestedStringAliasExample aliasTwo(AuthHeader authHeader, NestedStringAliasExample queryParamName);

    /**
     * @apiNote {@code POST /base/external/notNullBody}
     */
    @ConjureEndpoint(path = "/base/external/notNullBody", method = "POST")
    StringAliasExample notNullBodyExternalImport(AuthHeader authHeader, StringAliasExample notNullBody);

    /**
     * @apiNote {@code POST /base/external/optional-body}
     */
    @ConjureEndpoint(path = "/base/external/optional-body", method = "POST")
    Optional<StringAliasExample> optionalBodyExternalImport(AuthHeader authHeader, Optional<StringAliasExample> body);

    /**
     * @apiNote {@code POST /base/external/optional-query}
     */
    @ConjureEndpoint(path = "/base/external/optional-query", method = "POST")
    Optional<StringAliasExample> optionalQueryExternalImport(AuthHeader authHeader, Optional<StringAliasExample> query);

    /**
     * @apiNote {@code POST /base/no-return}
     */
    @ConjureEndpoint(path = "/base/no-return", method = "POST")
    void noReturn(AuthHeader authHeader);

    /**
     * @apiNote {@code GET /base/enum/query}
     */
    @ConjureEndpoint(path = "/base/enum/query", method = "GET")
    SimpleEnum enumQuery(AuthHeader authHeader, SimpleEnum queryParamName);

    /**
     * @apiNote {@code GET /base/enum/list/query}
     */
    @ConjureEndpoint(path = "/base/enum/list/query", method = "GET")
    List<SimpleEnum> enumListQuery(AuthHeader authHeader, List<SimpleEnum> queryParamName);

    /**
     * @apiNote {@code GET /base/enum/optional/query}
     */
    @ConjureEndpoint(path = "/base/enum/optional/query", method = "GET")
    Optional<SimpleEnum> optionalEnumQuery(AuthHeader authHeader, Optional<SimpleEnum> queryParamName);

    /**
     * @apiNote {@code GET /base/enum/header}
     */
    @ConjureEndpoint(path = "/base/enum/header", method = "GET")
    SimpleEnum enumHeader(AuthHeader authHeader, SimpleEnum headerParameter);

    /**
     * @apiNote {@code GET /base/alias-long}
     */
    @ConjureEndpoint(path = "/base/alias-long", method = "GET")
    Optional<LongAlias> aliasLongEndpoint(AuthHeader authHeader, Optional<LongAlias> input);

    /**
     * @apiNote {@code GET /base/datasets/{datasetRid}/strings}
     */
    @ConjureEndpoint(path = "/base/datasets/{datasetRid}/strings", method = "GET")
    void complexQueryParameters(
            AuthHeader authHeader,
            ResourceIdentifier datasetRid,
            Set<StringAliasExample> strings,
            Set<Long> longs,
            Set<Integer> ints);

    /**
     * @apiNote {@code PUT /base/list/optionals}
     */
    @ConjureEndpoint(path = "/base/list/optionals", method = "PUT")
    void receiveListOfOptionals(AuthHeader authHeader, List<Optional<String>> value);

    /**
     * @apiNote {@code PUT /base/set/optionals}
     */
    @ConjureEndpoint(path = "/base/set/optionals", method = "PUT")
    void receiveSetOfOptionals(AuthHeader authHeader, Set<Optional<String>> value);

    /**
     * Creates a synchronous/blocking client for a EteService service.
     */
    static EteServiceBlocking of(EndpointChannelFactory _endpointChannelFactory, ConjureRuntime _runtime) {
        return new EteServiceBlocking() {
            private final PlainSerDe _plainSerDe = _runtime.plainSerDe();

            private final EndpointChannel stringChannel = _endpointChannelFactory.endpoint(DialogueEteEndpoints.string);

            private final Deserializer<String> stringDeserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<String>() {});

            private final EndpointChannel integerChannel =
                    _endpointChannelFactory.endpoint(DialogueEteEndpoints.integer);

            private final Deserializer<Integer> integerDeserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<Integer>() {});

            private final EndpointChannel double_Channel =
                    _endpointChannelFactory.endpoint(DialogueEteEndpoints.double_);

            private final Deserializer<Double> double_Deserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<Double>() {});

            private final EndpointChannel boolean_Channel =
                    _endpointChannelFactory.endpoint(DialogueEteEndpoints.boolean_);

            private final Deserializer<Boolean> boolean_Deserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<Boolean>() {});

            private final EndpointChannel safelongChannel =
                    _endpointChannelFactory.endpoint(DialogueEteEndpoints.safelong);

            private final Deserializer<SafeLong> safelongDeserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<SafeLong>() {});

            private final EndpointChannel ridChannel = _endpointChannelFactory.endpoint(DialogueEteEndpoints.rid);

            private final Deserializer<ResourceIdentifier> ridDeserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<ResourceIdentifier>() {});

            private final EndpointChannel bearertokenChannel =
                    _endpointChannelFactory.endpoint(DialogueEteEndpoints.bearertoken);

            private final Deserializer<BearerToken> bearertokenDeserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<BearerToken>() {});

            private final EndpointChannel optionalStringChannel =
                    _endpointChannelFactory.endpoint(DialogueEteEndpoints.optionalString);

            private final Deserializer<Optional<String>> optionalStringDeserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<Optional<String>>() {});

            private final EndpointChannel optionalEmptyChannel =
                    _endpointChannelFactory.endpoint(DialogueEteEndpoints.optionalEmpty);

            private final Deserializer<Optional<String>> optionalEmptyDeserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<Optional<String>>() {});

            private final EndpointChannel datetimeChannel =
                    _endpointChannelFactory.endpoint(DialogueEteEndpoints.datetime);

            private final Deserializer<OffsetDateTime> datetimeDeserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<OffsetDateTime>() {});

            private final EndpointChannel binaryChannel = _endpointChannelFactory.endpoint(DialogueEteEndpoints.binary);

            private final EndpointChannel pathChannel = _endpointChannelFactory.endpoint(DialogueEteEndpoints.path);

            private final Deserializer<String> pathDeserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<String>() {});

            private final EndpointChannel externalLongPathChannel =
                    _endpointChannelFactory.endpoint(DialogueEteEndpoints.externalLongPath);

            private final Deserializer<Long> externalLongPathDeserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<Long>() {});

            private final EndpointChannel optionalExternalLongQueryChannel =
                    _endpointChannelFactory.endpoint(DialogueEteEndpoints.optionalExternalLongQuery);

            private final Deserializer<Optional<Long>> optionalExternalLongQueryDeserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<Optional<Long>>() {});

            private final Serializer<StringAliasExample> notNullBodySerializer =
                    _runtime.bodySerDe().serializer(new TypeMarker<StringAliasExample>() {});

            private final EndpointChannel notNullBodyChannel =
                    _endpointChannelFactory.endpoint(DialogueEteEndpoints.notNullBody);

            private final Deserializer<StringAliasExample> notNullBodyDeserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<StringAliasExample>() {});

            private final EndpointChannel aliasOneChannel =
                    _endpointChannelFactory.endpoint(DialogueEteEndpoints.aliasOne);

            private final Deserializer<StringAliasExample> aliasOneDeserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<StringAliasExample>() {});

            private final EndpointChannel optionalAliasOneChannel =
                    _endpointChannelFactory.endpoint(DialogueEteEndpoints.optionalAliasOne);

            private final Deserializer<StringAliasExample> optionalAliasOneDeserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<StringAliasExample>() {});

            private final EndpointChannel aliasTwoChannel =
                    _endpointChannelFactory.endpoint(DialogueEteEndpoints.aliasTwo);

            private final Deserializer<NestedStringAliasExample> aliasTwoDeserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<NestedStringAliasExample>() {});

            private final Serializer<StringAliasExample> notNullBodyExternalImportSerializer =
                    _runtime.bodySerDe().serializer(new TypeMarker<StringAliasExample>() {});

            private final EndpointChannel notNullBodyExternalImportChannel =
                    _endpointChannelFactory.endpoint(DialogueEteEndpoints.notNullBodyExternalImport);

            private final Deserializer<StringAliasExample> notNullBodyExternalImportDeserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<StringAliasExample>() {});

            private final Serializer<Optional<StringAliasExample>> optionalBodyExternalImportSerializer =
                    _runtime.bodySerDe().serializer(new TypeMarker<Optional<StringAliasExample>>() {});

            private final EndpointChannel optionalBodyExternalImportChannel =
                    _endpointChannelFactory.endpoint(DialogueEteEndpoints.optionalBodyExternalImport);

            private final Deserializer<Optional<StringAliasExample>> optionalBodyExternalImportDeserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<Optional<StringAliasExample>>() {});

            private final EndpointChannel optionalQueryExternalImportChannel =
                    _endpointChannelFactory.endpoint(DialogueEteEndpoints.optionalQueryExternalImport);

            private final Deserializer<Optional<StringAliasExample>> optionalQueryExternalImportDeserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<Optional<StringAliasExample>>() {});

            private final EndpointChannel noReturnChannel =
                    _endpointChannelFactory.endpoint(DialogueEteEndpoints.noReturn);

            private final Deserializer<Void> noReturnDeserializer =
                    _runtime.bodySerDe().emptyBodyDeserializer();

            private final EndpointChannel enumQueryChannel =
                    _endpointChannelFactory.endpoint(DialogueEteEndpoints.enumQuery);

            private final Deserializer<SimpleEnum> enumQueryDeserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<SimpleEnum>() {});

            private final EndpointChannel enumListQueryChannel =
                    _endpointChannelFactory.endpoint(DialogueEteEndpoints.enumListQuery);

            private final Deserializer<List<SimpleEnum>> enumListQueryDeserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<List<SimpleEnum>>() {});

            private final EndpointChannel optionalEnumQueryChannel =
                    _endpointChannelFactory.endpoint(DialogueEteEndpoints.optionalEnumQuery);

            private final Deserializer<Optional<SimpleEnum>> optionalEnumQueryDeserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<Optional<SimpleEnum>>() {});

            private final EndpointChannel enumHeaderChannel =
                    _endpointChannelFactory.endpoint(DialogueEteEndpoints.enumHeader);

            private final Deserializer<SimpleEnum> enumHeaderDeserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<SimpleEnum>() {});

            private final EndpointChannel aliasLongEndpointChannel =
                    _endpointChannelFactory.endpoint(DialogueEteEndpoints.aliasLongEndpoint);

            private final Deserializer<Optional<LongAlias>> aliasLongEndpointDeserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<Optional<LongAlias>>() {});

            private final EndpointChannel complexQueryParametersChannel =
                    _endpointChannelFactory.endpoint(DialogueEteEndpoints.complexQueryParameters);

            private final Deserializer<Void> complexQueryParametersDeserializer =
                    _runtime.bodySerDe().emptyBodyDeserializer();

            private final Serializer<List<Optional<String>>> receiveListOfOptionalsSerializer =
                    _runtime.bodySerDe().serializer(new TypeMarker<List<Optional<String>>>() {});

            private final EndpointChannel receiveListOfOptionalsChannel =
                    _endpointChannelFactory.endpoint(DialogueEteEndpoints.receiveListOfOptionals);

            private final Deserializer<Void> receiveListOfOptionalsDeserializer =
                    _runtime.bodySerDe().emptyBodyDeserializer();

            private final Serializer<Set<Optional<String>>> receiveSetOfOptionalsSerializer =
                    _runtime.bodySerDe().serializer(new TypeMarker<Set<Optional<String>>>() {});

            private final EndpointChannel receiveSetOfOptionalsChannel =
                    _endpointChannelFactory.endpoint(DialogueEteEndpoints.receiveSetOfOptionals);

            private final Deserializer<Void> receiveSetOfOptionalsDeserializer =
                    _runtime.bodySerDe().emptyBodyDeserializer();

            @Override
            public String string(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                return _runtime.clients().callBlocking(stringChannel, _request.build(), stringDeserializer);
            }

            @Override
            public int integer(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                return _runtime.clients().callBlocking(integerChannel, _request.build(), integerDeserializer);
            }

            @Override
            public double double_(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                return _runtime.clients().callBlocking(double_Channel, _request.build(), double_Deserializer);
            }

            @Override
            public boolean boolean_(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                return _runtime.clients().callBlocking(boolean_Channel, _request.build(), boolean_Deserializer);
            }

            @Override
            public SafeLong safelong(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                return _runtime.clients().callBlocking(safelongChannel, _request.build(), safelongDeserializer);
            }

            @Override
            public ResourceIdentifier rid(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                return _runtime.clients().callBlocking(ridChannel, _request.build(), ridDeserializer);
            }

            @Override
            public BearerToken bearertoken(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                return _runtime.clients().callBlocking(bearertokenChannel, _request.build(), bearertokenDeserializer);
            }

            @Override
            public Optional<String> optionalString(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                return _runtime.clients()
                        .callBlocking(optionalStringChannel, _request.build(), optionalStringDeserializer);
            }

            @Override
            public Optional<String> optionalEmpty(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                return _runtime.clients()
                        .callBlocking(optionalEmptyChannel, _request.build(), optionalEmptyDeserializer);
            }

            @Override
            public OffsetDateTime datetime(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                return _runtime.clients().callBlocking(datetimeChannel, _request.build(), datetimeDeserializer);
            }

            @Override
            public InputStream binary(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                return _runtime.clients()
                        .callBlocking(
                                binaryChannel,
                                _request.build(),
                                _runtime.bodySerDe().inputStreamDeserializer());
            }

            @Override
            public String path(AuthHeader authHeader, String param) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.putPathParams("param", _plainSerDe.serializeString(param));
                return _runtime.clients().callBlocking(pathChannel, _request.build(), pathDeserializer);
            }

            @Override
            public long externalLongPath(AuthHeader authHeader, long param) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.putPathParams("param", Objects.toString(param));
                return _runtime.clients()
                        .callBlocking(externalLongPathChannel, _request.build(), externalLongPathDeserializer);
            }

            @Override
            public Optional<Long> optionalExternalLongQuery(AuthHeader authHeader, Optional<Long> param) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                if (param.isPresent()) {
                    _request.putQueryParams("param", Objects.toString(param.get()));
                }
                return _runtime.clients()
                        .callBlocking(
                                optionalExternalLongQueryChannel,
                                _request.build(),
                                optionalExternalLongQueryDeserializer);
            }

            @Override
            public StringAliasExample notNullBody(AuthHeader authHeader, StringAliasExample notNullBody) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.body(notNullBodySerializer.serialize(notNullBody));
                return _runtime.clients().callBlocking(notNullBodyChannel, _request.build(), notNullBodyDeserializer);
            }

            @Override
            public StringAliasExample aliasOne(AuthHeader authHeader, StringAliasExample queryParamName) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.putQueryParams("queryParamName", _plainSerDe.serializeString(queryParamName.get()));
                return _runtime.clients().callBlocking(aliasOneChannel, _request.build(), aliasOneDeserializer);
            }

            @Override
            public StringAliasExample optionalAliasOne(
                    AuthHeader authHeader, Optional<StringAliasExample> queryParamName) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                if (queryParamName.isPresent()) {
                    _request.putQueryParams(
                            "queryParamName",
                            _plainSerDe.serializeString(queryParamName.get().get()));
                }
                return _runtime.clients()
                        .callBlocking(optionalAliasOneChannel, _request.build(), optionalAliasOneDeserializer);
            }

            @Override
            public NestedStringAliasExample aliasTwo(AuthHeader authHeader, NestedStringAliasExample queryParamName) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.putQueryParams(
                        "queryParamName",
                        _plainSerDe.serializeString(queryParamName.get().get()));
                return _runtime.clients().callBlocking(aliasTwoChannel, _request.build(), aliasTwoDeserializer);
            }

            @Override
            public StringAliasExample notNullBodyExternalImport(AuthHeader authHeader, StringAliasExample notNullBody) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.body(notNullBodyExternalImportSerializer.serialize(notNullBody));
                return _runtime.clients()
                        .callBlocking(
                                notNullBodyExternalImportChannel,
                                _request.build(),
                                notNullBodyExternalImportDeserializer);
            }

            @Override
            public Optional<StringAliasExample> optionalBodyExternalImport(
                    AuthHeader authHeader, Optional<StringAliasExample> body) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.body(optionalBodyExternalImportSerializer.serialize(body));
                return _runtime.clients()
                        .callBlocking(
                                optionalBodyExternalImportChannel,
                                _request.build(),
                                optionalBodyExternalImportDeserializer);
            }

            @Override
            public Optional<StringAliasExample> optionalQueryExternalImport(
                    AuthHeader authHeader, Optional<StringAliasExample> query) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                if (query.isPresent()) {
                    _request.putQueryParams("query", Objects.toString(query.get()));
                }
                return _runtime.clients()
                        .callBlocking(
                                optionalQueryExternalImportChannel,
                                _request.build(),
                                optionalQueryExternalImportDeserializer);
            }

            @Override
            public void noReturn(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _runtime.clients().callBlocking(noReturnChannel, _request.build(), noReturnDeserializer);
            }

            @Override
            public SimpleEnum enumQuery(AuthHeader authHeader, SimpleEnum queryParamName) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.putQueryParams("queryParamName", Objects.toString(queryParamName));
                return _runtime.clients().callBlocking(enumQueryChannel, _request.build(), enumQueryDeserializer);
            }

            @Override
            public List<SimpleEnum> enumListQuery(AuthHeader authHeader, List<SimpleEnum> queryParamName) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                for (SimpleEnum queryParamNameElement : queryParamName) {
                    _request.putQueryParams("queryParamName", Objects.toString(queryParamNameElement));
                }
                return _runtime.clients()
                        .callBlocking(enumListQueryChannel, _request.build(), enumListQueryDeserializer);
            }

            @Override
            public Optional<SimpleEnum> optionalEnumQuery(AuthHeader authHeader, Optional<SimpleEnum> queryParamName) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                if (queryParamName.isPresent()) {
                    _request.putQueryParams("queryParamName", Objects.toString(queryParamName.get()));
                }
                return _runtime.clients()
                        .callBlocking(optionalEnumQueryChannel, _request.build(), optionalEnumQueryDeserializer);
            }

            @Override
            public SimpleEnum enumHeader(AuthHeader authHeader, SimpleEnum headerParameter) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.putHeaderParams("Custom-Header", Objects.toString(headerParameter));
                return _runtime.clients().callBlocking(enumHeaderChannel, _request.build(), enumHeaderDeserializer);
            }

            @Override
            public Optional<LongAlias> aliasLongEndpoint(AuthHeader authHeader, Optional<LongAlias> input) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                if (input.isPresent()) {
                    _request.putQueryParams(
                            "input", Objects.toString(input.get().get()));
                }
                return _runtime.clients()
                        .callBlocking(aliasLongEndpointChannel, _request.build(), aliasLongEndpointDeserializer);
            }

            @Override
            public void complexQueryParameters(
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
                _runtime.clients()
                        .callBlocking(
                                complexQueryParametersChannel, _request.build(), complexQueryParametersDeserializer);
            }

            @Override
            public void receiveListOfOptionals(AuthHeader authHeader, List<Optional<String>> value) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.body(receiveListOfOptionalsSerializer.serialize(value));
                _runtime.clients()
                        .callBlocking(
                                receiveListOfOptionalsChannel, _request.build(), receiveListOfOptionalsDeserializer);
            }

            @Override
            public void receiveSetOfOptionals(AuthHeader authHeader, Set<Optional<String>> value) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.body(receiveSetOfOptionalsSerializer.serialize(value));
                _runtime.clients()
                        .callBlocking(
                                receiveSetOfOptionalsChannel, _request.build(), receiveSetOfOptionalsDeserializer);
            }

            @Override
            public String toString() {
                return "EteServiceBlocking{_endpointChannelFactory=" + _endpointChannelFactory + ", runtime=" + _runtime
                        + '}';
            }
        };
    }

    /**
     * Creates an asynchronous/non-blocking client for a EteService service.
     */
    static EteServiceBlocking of(Channel _channel, ConjureRuntime _runtime) {
        if (_channel instanceof EndpointChannelFactory) {
            return of((EndpointChannelFactory) _channel, _runtime);
        }
        return of(
                new EndpointChannelFactory() {
                    @Override
                    public EndpointChannel endpoint(Endpoint endpoint) {
                        return _runtime.clients().bind(_channel, endpoint);
                    }
                },
                _runtime);
    }

    final class Factory implements DialogueServiceFactory<EteServiceBlocking> {
        @Override
        public EteServiceBlocking create(EndpointChannelFactory endpointChannelFactory, ConjureRuntime runtime) {
            return EteServiceBlocking.of(endpointChannelFactory, runtime);
        }
    }
}
