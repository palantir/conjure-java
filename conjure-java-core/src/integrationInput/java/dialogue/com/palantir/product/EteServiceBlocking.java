package dialogue.com.palantir.product;

import com.google.errorprone.annotations.MustBeClosed;
import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.conjure.java.lib.internal.ClientEndpoint;
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
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.services.dialogue.DialogueInterfaceGenerator")
@DialogueService(EteServiceBlocking.Factory.class)
public interface EteServiceBlocking {
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
     *
     * @apiNote {@code GET /base/string}
     */
    @ClientEndpoint(method = "GET", path = "/base/string")
    String string(AuthHeader authHeader);

    /**
     * one <em>two</em> three.
     *
     * @apiNote {@code GET /base/integer}
     */
    @ClientEndpoint(method = "GET", path = "/base/integer")
    int integer(AuthHeader authHeader);

    /** @apiNote {@code GET /base/double} */
    @ClientEndpoint(method = "GET", path = "/base/double")
    double double_(AuthHeader authHeader);

    /** @apiNote {@code GET /base/boolean} */
    @ClientEndpoint(method = "GET", path = "/base/boolean")
    boolean boolean_(AuthHeader authHeader);

    /** @apiNote {@code GET /base/safelong} */
    @ClientEndpoint(method = "GET", path = "/base/safelong")
    SafeLong safelong(AuthHeader authHeader);

    /** @apiNote {@code GET /base/rid} */
    @ClientEndpoint(method = "GET", path = "/base/rid")
    ResourceIdentifier rid(AuthHeader authHeader);

    /** @apiNote {@code GET /base/bearertoken} */
    @ClientEndpoint(method = "GET", path = "/base/bearertoken")
    BearerToken bearertoken(AuthHeader authHeader);

    /** @apiNote {@code GET /base/optionalString} */
    @ClientEndpoint(method = "GET", path = "/base/optionalString")
    Optional<String> optionalString(AuthHeader authHeader);

    /** @apiNote {@code GET /base/optionalEmpty} */
    @ClientEndpoint(method = "GET", path = "/base/optionalEmpty")
    Optional<String> optionalEmpty(AuthHeader authHeader);

    /** @apiNote {@code GET /base/datetime} */
    @ClientEndpoint(method = "GET", path = "/base/datetime")
    OffsetDateTime datetime(AuthHeader authHeader);

    /** @apiNote {@code GET /base/binary} */
    @ClientEndpoint(method = "GET", path = "/base/binary")
    @MustBeClosed
    InputStream binary(AuthHeader authHeader);

    /**
     * Path endpoint.
     *
     * @apiNote {@code GET /base/path/{param}}
     * @param param Documentation for <code>param</code>
     */
    @ClientEndpoint(method = "GET", path = "/base/path/{param}")
    String path(AuthHeader authHeader, String param);

    /** @apiNote {@code GET /base/externalLong/{param}} */
    @ClientEndpoint(method = "GET", path = "/base/externalLong/{param}")
    long externalLongPath(AuthHeader authHeader, long param);

    /** @apiNote {@code GET /base/optionalExternalLong} */
    @ClientEndpoint(method = "GET", path = "/base/optionalExternalLong")
    Optional<Long> optionalExternalLongQuery(AuthHeader authHeader, Optional<Long> param);

    /** @apiNote {@code POST /base/notNullBody} */
    @ClientEndpoint(method = "POST", path = "/base/notNullBody")
    StringAliasExample notNullBody(AuthHeader authHeader, StringAliasExample notNullBody);

    /** @apiNote {@code GET /base/aliasOne} */
    @ClientEndpoint(method = "GET", path = "/base/aliasOne")
    StringAliasExample aliasOne(AuthHeader authHeader, StringAliasExample queryParamName);

    /** @apiNote {@code GET /base/optionalAliasOne} */
    @ClientEndpoint(method = "GET", path = "/base/optionalAliasOne")
    StringAliasExample optionalAliasOne(AuthHeader authHeader, Optional<StringAliasExample> queryParamName);

    /** @apiNote {@code GET /base/aliasTwo} */
    @ClientEndpoint(method = "GET", path = "/base/aliasTwo")
    NestedStringAliasExample aliasTwo(AuthHeader authHeader, NestedStringAliasExample queryParamName);

    /** @apiNote {@code POST /base/external/notNullBody} */
    @ClientEndpoint(method = "POST", path = "/base/external/notNullBody")
    allexamples.com.palantir.product.StringAliasExample notNullBodyExternalImport(
            AuthHeader authHeader, allexamples.com.palantir.product.StringAliasExample notNullBody);

    /** @apiNote {@code POST /base/external/optional-body} */
    @ClientEndpoint(method = "POST", path = "/base/external/optional-body")
    Optional<allexamples.com.palantir.product.StringAliasExample> optionalBodyExternalImport(
            AuthHeader authHeader, Optional<allexamples.com.palantir.product.StringAliasExample> body);

    /** @apiNote {@code POST /base/external/optional-query} */
    @ClientEndpoint(method = "POST", path = "/base/external/optional-query")
    Optional<allexamples.com.palantir.product.StringAliasExample> optionalQueryExternalImport(
            AuthHeader authHeader, Optional<allexamples.com.palantir.product.StringAliasExample> query);

    /** @apiNote {@code POST /base/no-return} */
    @ClientEndpoint(method = "POST", path = "/base/no-return")
    void noReturn(AuthHeader authHeader);

    /** @apiNote {@code GET /base/enum/query} */
    @ClientEndpoint(method = "GET", path = "/base/enum/query")
    SimpleEnum enumQuery(AuthHeader authHeader, SimpleEnum queryParamName);

    /** @apiNote {@code GET /base/enum/list/query} */
    @ClientEndpoint(method = "GET", path = "/base/enum/list/query")
    List<SimpleEnum> enumListQuery(AuthHeader authHeader, List<SimpleEnum> queryParamName);

    /** @apiNote {@code GET /base/enum/optional/query} */
    @ClientEndpoint(method = "GET", path = "/base/enum/optional/query")
    Optional<SimpleEnum> optionalEnumQuery(AuthHeader authHeader, Optional<SimpleEnum> queryParamName);

    /** @apiNote {@code GET /base/enum/header} */
    @ClientEndpoint(method = "GET", path = "/base/enum/header")
    SimpleEnum enumHeader(AuthHeader authHeader, SimpleEnum headerParameter);

    /**
     * This endpoint is used to test that the <code>Accept-Conjure-Error-Parameter-Format</code> header is respected.
     * Specifically, that error parameters are serialized as JSON when the header is set to <code>JSON</code>.
     *
     * @apiNote {@code GET /base/errors/header}
     */
    @ClientEndpoint(method = "GET", path = "/base/errors/header")
    String jsonErrorsHeader(AuthHeader authHeader, String headerParameter);

    /**
     * This endpoint is used to test that error parameters serialized as JSON or using <code>Objects.toString</code> are
     * both handled correctly by the clients that can deserialize &quot;rich&quot; exceptions (which are sub-types of
     * <code>RemoteException</code>).
     *
     * @apiNote {@code GET /base/errors/serialization}
     */
    @ClientEndpoint(method = "GET", path = "/base/errors/serialization")
    String errorParameterSerialization(AuthHeader authHeader, String headerParameter);

    /** @apiNote {@code GET /base/alias-long} */
    @ClientEndpoint(method = "GET", path = "/base/alias-long")
    Optional<LongAlias> aliasLongEndpoint(AuthHeader authHeader, Optional<LongAlias> input);

    /** @apiNote {@code GET /base/datasets/{datasetRid}/strings} */
    @ClientEndpoint(method = "GET", path = "/base/datasets/{datasetRid}/strings")
    void complexQueryParameters(
            AuthHeader authHeader,
            ResourceIdentifier datasetRid,
            Set<StringAliasExample> strings,
            Set<Long> longs,
            Set<Integer> ints);

    /** @apiNote {@code PUT /base/list/optionals} */
    @ClientEndpoint(method = "PUT", path = "/base/list/optionals")
    void receiveListOfOptionals(AuthHeader authHeader, List<Optional<String>> value);

    /** @apiNote {@code PUT /base/set/optionals} */
    @ClientEndpoint(method = "PUT", path = "/base/set/optionals")
    void receiveSetOfOptionals(AuthHeader authHeader, Set<Optional<String>> value);

    /** @apiNote {@code PUT /base/list/strings} */
    @ClientEndpoint(method = "PUT", path = "/base/list/strings")
    void receiveListOfStrings(AuthHeader authHeader, List<String> value);

    /** @apiNote {@code PUT /base/union} */
    @ClientEndpoint(method = "PUT", path = "/base/union")
    SimpleUnion union(AuthHeader authHeader, SimpleUnion value);

    /** Creates a synchronous/blocking client for a EteService service. */
    static EteServiceBlocking of(EndpointChannelFactory _endpointChannelFactory, ConjureRuntime _runtime) {
        return new EteServiceBlocking() {
            private static final TypeMarker<String> stringTypeMarker = new TypeMarker<String>() {};

            private static final TypeMarker<Integer> integerTypeMarker = new TypeMarker<Integer>() {};

            private static final TypeMarker<Double> doubleTypeMarker = new TypeMarker<Double>() {};

            private static final TypeMarker<Boolean> booleanTypeMarker = new TypeMarker<Boolean>() {};

            private static final TypeMarker<SafeLong> safeLongTypeMarker = new TypeMarker<SafeLong>() {};

            private static final TypeMarker<ResourceIdentifier> resourceIdentifierTypeMarker =
                    new TypeMarker<ResourceIdentifier>() {};

            private static final TypeMarker<BearerToken> bearerTokenTypeMarker = new TypeMarker<BearerToken>() {};

            private static final TypeMarker<Optional<String>> optionalStringTypeMarker =
                    new TypeMarker<Optional<String>>() {};

            private static final TypeMarker<OffsetDateTime> offsetDateTimeTypeMarker =
                    new TypeMarker<OffsetDateTime>() {};

            private static final TypeMarker<Long> longTypeMarker = new TypeMarker<Long>() {};

            private static final TypeMarker<Optional<Long>> optionalLongTypeMarker =
                    new TypeMarker<Optional<Long>>() {};

            private static final TypeMarker<StringAliasExample> stringAliasExampleTypeMarker =
                    new TypeMarker<StringAliasExample>() {};

            private static final TypeMarker<NestedStringAliasExample> nestedStringAliasExampleTypeMarker =
                    new TypeMarker<NestedStringAliasExample>() {};

            private static final TypeMarker<allexamples.com.palantir.product.StringAliasExample>
                    stringAliasExample2TypeMarker =
                            new TypeMarker<allexamples.com.palantir.product.StringAliasExample>() {};

            private static final TypeMarker<Optional<allexamples.com.palantir.product.StringAliasExample>>
                    optionalStringAliasExampleTypeMarker =
                            new TypeMarker<Optional<allexamples.com.palantir.product.StringAliasExample>>() {};

            private static final TypeMarker<SimpleEnum> simpleEnumTypeMarker = new TypeMarker<SimpleEnum>() {};

            private static final TypeMarker<List<SimpleEnum>> listSimpleEnumTypeMarker =
                    new TypeMarker<List<SimpleEnum>>() {};

            private static final TypeMarker<Optional<SimpleEnum>> optionalSimpleEnumTypeMarker =
                    new TypeMarker<Optional<SimpleEnum>>() {};

            private static final TypeMarker<Optional<LongAlias>> optionalLongAliasTypeMarker =
                    new TypeMarker<Optional<LongAlias>>() {};

            private static final TypeMarker<SimpleUnion> simpleUnionTypeMarker = new TypeMarker<SimpleUnion>() {};

            private final PlainSerDe _plainSerDe = _runtime.plainSerDe();

            private final Deserializer<String> stringDeserializer =
                    _runtime.bodySerDe().deserializer(stringTypeMarker);

            private final Deserializer<Integer> integerDeserializer =
                    _runtime.bodySerDe().deserializer(integerTypeMarker);

            private final Deserializer<Double> doubleDeserializer =
                    _runtime.bodySerDe().deserializer(doubleTypeMarker);

            private final Deserializer<Boolean> booleanDeserializer =
                    _runtime.bodySerDe().deserializer(booleanTypeMarker);

            private final Deserializer<SafeLong> safeLongDeserializer =
                    _runtime.bodySerDe().deserializer(safeLongTypeMarker);

            private final Deserializer<ResourceIdentifier> resourceIdentifierDeserializer =
                    _runtime.bodySerDe().deserializer(resourceIdentifierTypeMarker);

            private final Deserializer<BearerToken> bearerTokenDeserializer =
                    _runtime.bodySerDe().deserializer(bearerTokenTypeMarker);

            private final Deserializer<Optional<String>> optionalStringDeserializer =
                    _runtime.bodySerDe().deserializer(optionalStringTypeMarker);

            private final Deserializer<OffsetDateTime> offsetDateTimeDeserializer =
                    _runtime.bodySerDe().deserializer(offsetDateTimeTypeMarker);

            private final Deserializer<Long> longDeserializer =
                    _runtime.bodySerDe().deserializer(longTypeMarker);

            private final Deserializer<Optional<Long>> optionalLongDeserializer =
                    _runtime.bodySerDe().deserializer(optionalLongTypeMarker);

            private final Deserializer<StringAliasExample> stringAliasExampleDeserializer =
                    _runtime.bodySerDe().deserializer(stringAliasExampleTypeMarker);

            private final Deserializer<NestedStringAliasExample> nestedStringAliasExampleDeserializer =
                    _runtime.bodySerDe().deserializer(nestedStringAliasExampleTypeMarker);

            private final Deserializer<allexamples.com.palantir.product.StringAliasExample>
                    stringAliasExample2Deserializer = _runtime.bodySerDe().deserializer(stringAliasExample2TypeMarker);

            private final Deserializer<Optional<allexamples.com.palantir.product.StringAliasExample>>
                    optionalStringAliasExampleDeserializer =
                            _runtime.bodySerDe().deserializer(optionalStringAliasExampleTypeMarker);

            private final Deserializer<Void> voidDeserializer =
                    _runtime.bodySerDe().emptyBodyDeserializer();

            private final Deserializer<SimpleEnum> simpleEnumDeserializer =
                    _runtime.bodySerDe().deserializer(simpleEnumTypeMarker);

            private final Deserializer<List<SimpleEnum>> listSimpleEnumDeserializer =
                    _runtime.bodySerDe().deserializer(listSimpleEnumTypeMarker);

            private final Deserializer<Optional<SimpleEnum>> optionalSimpleEnumDeserializer =
                    _runtime.bodySerDe().deserializer(optionalSimpleEnumTypeMarker);

            private final Deserializer<Optional<LongAlias>> optionalLongAliasDeserializer =
                    _runtime.bodySerDe().deserializer(optionalLongAliasTypeMarker);

            private final Deserializer<SimpleUnion> simpleUnionDeserializer =
                    _runtime.bodySerDe().deserializer(simpleUnionTypeMarker);

            private final EndpointChannel stringChannel = _endpointChannelFactory.endpoint(DialogueEteEndpoints.string);

            private final EndpointChannel integerChannel =
                    _endpointChannelFactory.endpoint(DialogueEteEndpoints.integer);

            private final EndpointChannel double_Channel =
                    _endpointChannelFactory.endpoint(DialogueEteEndpoints.double_);

            private final EndpointChannel boolean_Channel =
                    _endpointChannelFactory.endpoint(DialogueEteEndpoints.boolean_);

            private final EndpointChannel safelongChannel =
                    _endpointChannelFactory.endpoint(DialogueEteEndpoints.safelong);

            private final EndpointChannel ridChannel = _endpointChannelFactory.endpoint(DialogueEteEndpoints.rid);

            private final EndpointChannel bearertokenChannel =
                    _endpointChannelFactory.endpoint(DialogueEteEndpoints.bearertoken);

            private final EndpointChannel optionalStringChannel =
                    _endpointChannelFactory.endpoint(DialogueEteEndpoints.optionalString);

            private final EndpointChannel optionalEmptyChannel =
                    _endpointChannelFactory.endpoint(DialogueEteEndpoints.optionalEmpty);

            private final EndpointChannel datetimeChannel =
                    _endpointChannelFactory.endpoint(DialogueEteEndpoints.datetime);

            private final EndpointChannel binaryChannel = _endpointChannelFactory.endpoint(DialogueEteEndpoints.binary);

            private final EndpointChannel pathChannel = _endpointChannelFactory.endpoint(DialogueEteEndpoints.path);

            private final EndpointChannel externalLongPathChannel =
                    _endpointChannelFactory.endpoint(DialogueEteEndpoints.externalLongPath);

            private final EndpointChannel optionalExternalLongQueryChannel =
                    _endpointChannelFactory.endpoint(DialogueEteEndpoints.optionalExternalLongQuery);

            private final Serializer<StringAliasExample> notNullBodySerializer =
                    _runtime.bodySerDe().serializer(new TypeMarker<StringAliasExample>() {});

            private final EndpointChannel notNullBodyChannel =
                    _endpointChannelFactory.endpoint(DialogueEteEndpoints.notNullBody);

            private final EndpointChannel aliasOneChannel =
                    _endpointChannelFactory.endpoint(DialogueEteEndpoints.aliasOne);

            private final EndpointChannel optionalAliasOneChannel =
                    _endpointChannelFactory.endpoint(DialogueEteEndpoints.optionalAliasOne);

            private final EndpointChannel aliasTwoChannel =
                    _endpointChannelFactory.endpoint(DialogueEteEndpoints.aliasTwo);

            private final Serializer<allexamples.com.palantir.product.StringAliasExample>
                    notNullBodyExternalImportSerializer = _runtime.bodySerDe()
                            .serializer(new TypeMarker<allexamples.com.palantir.product.StringAliasExample>() {});

            private final EndpointChannel notNullBodyExternalImportChannel =
                    _endpointChannelFactory.endpoint(DialogueEteEndpoints.notNullBodyExternalImport);

            private final Serializer<Optional<allexamples.com.palantir.product.StringAliasExample>>
                    optionalBodyExternalImportSerializer = _runtime.bodySerDe()
                            .serializer(
                                    new TypeMarker<Optional<allexamples.com.palantir.product.StringAliasExample>>() {});

            private final EndpointChannel optionalBodyExternalImportChannel =
                    _endpointChannelFactory.endpoint(DialogueEteEndpoints.optionalBodyExternalImport);

            private final EndpointChannel optionalQueryExternalImportChannel =
                    _endpointChannelFactory.endpoint(DialogueEteEndpoints.optionalQueryExternalImport);

            private final EndpointChannel noReturnChannel =
                    _endpointChannelFactory.endpoint(DialogueEteEndpoints.noReturn);

            private final EndpointChannel enumQueryChannel =
                    _endpointChannelFactory.endpoint(DialogueEteEndpoints.enumQuery);

            private final EndpointChannel enumListQueryChannel =
                    _endpointChannelFactory.endpoint(DialogueEteEndpoints.enumListQuery);

            private final EndpointChannel optionalEnumQueryChannel =
                    _endpointChannelFactory.endpoint(DialogueEteEndpoints.optionalEnumQuery);

            private final EndpointChannel enumHeaderChannel =
                    _endpointChannelFactory.endpoint(DialogueEteEndpoints.enumHeader);

            private final EndpointChannel jsonErrorsHeaderChannel =
                    _endpointChannelFactory.endpoint(DialogueEteEndpoints.jsonErrorsHeader);

            private final EndpointChannel errorParameterSerializationChannel =
                    _endpointChannelFactory.endpoint(DialogueEteEndpoints.errorParameterSerialization);

            private final EndpointChannel aliasLongEndpointChannel =
                    _endpointChannelFactory.endpoint(DialogueEteEndpoints.aliasLongEndpoint);

            private final EndpointChannel complexQueryParametersChannel =
                    _endpointChannelFactory.endpoint(DialogueEteEndpoints.complexQueryParameters);

            private final Serializer<List<Optional<String>>> receiveListOfOptionalsSerializer =
                    _runtime.bodySerDe().serializer(new TypeMarker<List<Optional<String>>>() {});

            private final EndpointChannel receiveListOfOptionalsChannel =
                    _endpointChannelFactory.endpoint(DialogueEteEndpoints.receiveListOfOptionals);

            private final Serializer<Set<Optional<String>>> receiveSetOfOptionalsSerializer =
                    _runtime.bodySerDe().serializer(new TypeMarker<Set<Optional<String>>>() {});

            private final EndpointChannel receiveSetOfOptionalsChannel =
                    _endpointChannelFactory.endpoint(DialogueEteEndpoints.receiveSetOfOptionals);

            private final Serializer<List<String>> receiveListOfStringsSerializer =
                    _runtime.bodySerDe().serializer(new TypeMarker<List<String>>() {});

            private final EndpointChannel receiveListOfStringsChannel =
                    _endpointChannelFactory.endpoint(DialogueEteEndpoints.receiveListOfStrings);

            private final Serializer<SimpleUnion> unionSerializer =
                    _runtime.bodySerDe().serializer(new TypeMarker<SimpleUnion>() {});

            private final EndpointChannel unionChannel = _endpointChannelFactory.endpoint(DialogueEteEndpoints.union);

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
                return _runtime.clients().callBlocking(double_Channel, _request.build(), doubleDeserializer);
            }

            @Override
            public boolean boolean_(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                return _runtime.clients().callBlocking(boolean_Channel, _request.build(), booleanDeserializer);
            }

            @Override
            public SafeLong safelong(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                return _runtime.clients().callBlocking(safelongChannel, _request.build(), safeLongDeserializer);
            }

            @Override
            public ResourceIdentifier rid(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                return _runtime.clients().callBlocking(ridChannel, _request.build(), resourceIdentifierDeserializer);
            }

            @Override
            public BearerToken bearertoken(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                return _runtime.clients().callBlocking(bearertokenChannel, _request.build(), bearerTokenDeserializer);
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
                        .callBlocking(optionalEmptyChannel, _request.build(), optionalStringDeserializer);
            }

            @Override
            public OffsetDateTime datetime(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                return _runtime.clients().callBlocking(datetimeChannel, _request.build(), offsetDateTimeDeserializer);
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
                return _runtime.clients().callBlocking(pathChannel, _request.build(), stringDeserializer);
            }

            @Override
            public long externalLongPath(AuthHeader authHeader, long param) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.putPathParams("param", Objects.toString(param));
                return _runtime.clients().callBlocking(externalLongPathChannel, _request.build(), longDeserializer);
            }

            @Override
            public Optional<Long> optionalExternalLongQuery(AuthHeader authHeader, Optional<Long> param) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                if (param.isPresent()) {
                    _request.putQueryParams("param", Objects.toString(param.get()));
                }
                return _runtime.clients()
                        .callBlocking(optionalExternalLongQueryChannel, _request.build(), optionalLongDeserializer);
            }

            @Override
            public StringAliasExample notNullBody(AuthHeader authHeader, StringAliasExample notNullBody) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.body(notNullBodySerializer.serialize(notNullBody));
                return _runtime.clients()
                        .callBlocking(notNullBodyChannel, _request.build(), stringAliasExampleDeserializer);
            }

            @Override
            public StringAliasExample aliasOne(AuthHeader authHeader, StringAliasExample queryParamName) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.putQueryParams("queryParamName", _plainSerDe.serializeString(queryParamName.get()));
                return _runtime.clients()
                        .callBlocking(aliasOneChannel, _request.build(), stringAliasExampleDeserializer);
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
                        .callBlocking(optionalAliasOneChannel, _request.build(), stringAliasExampleDeserializer);
            }

            @Override
            public NestedStringAliasExample aliasTwo(AuthHeader authHeader, NestedStringAliasExample queryParamName) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.putQueryParams(
                        "queryParamName",
                        _plainSerDe.serializeString(queryParamName.get().get()));
                return _runtime.clients()
                        .callBlocking(aliasTwoChannel, _request.build(), nestedStringAliasExampleDeserializer);
            }

            @Override
            public allexamples.com.palantir.product.StringAliasExample notNullBodyExternalImport(
                    AuthHeader authHeader, allexamples.com.palantir.product.StringAliasExample notNullBody) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.body(notNullBodyExternalImportSerializer.serialize(notNullBody));
                return _runtime.clients()
                        .callBlocking(
                                notNullBodyExternalImportChannel, _request.build(), stringAliasExample2Deserializer);
            }

            @Override
            public Optional<allexamples.com.palantir.product.StringAliasExample> optionalBodyExternalImport(
                    AuthHeader authHeader, Optional<allexamples.com.palantir.product.StringAliasExample> body) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.body(optionalBodyExternalImportSerializer.serialize(body));
                return _runtime.clients()
                        .callBlocking(
                                optionalBodyExternalImportChannel,
                                _request.build(),
                                optionalStringAliasExampleDeserializer);
            }

            @Override
            public Optional<allexamples.com.palantir.product.StringAliasExample> optionalQueryExternalImport(
                    AuthHeader authHeader, Optional<allexamples.com.palantir.product.StringAliasExample> query) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                if (query.isPresent()) {
                    _request.putQueryParams("query", Objects.toString(query.get()));
                }
                return _runtime.clients()
                        .callBlocking(
                                optionalQueryExternalImportChannel,
                                _request.build(),
                                optionalStringAliasExampleDeserializer);
            }

            @Override
            public void noReturn(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _runtime.clients().callBlocking(noReturnChannel, _request.build(), voidDeserializer);
            }

            @Override
            public SimpleEnum enumQuery(AuthHeader authHeader, SimpleEnum queryParamName) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.putQueryParams("queryParamName", Objects.toString(queryParamName));
                return _runtime.clients().callBlocking(enumQueryChannel, _request.build(), simpleEnumDeserializer);
            }

            @Override
            public List<SimpleEnum> enumListQuery(AuthHeader authHeader, List<SimpleEnum> queryParamName) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                for (SimpleEnum queryParamNameElement : queryParamName) {
                    _request.putQueryParams("queryParamName", Objects.toString(queryParamNameElement));
                }
                return _runtime.clients()
                        .callBlocking(enumListQueryChannel, _request.build(), listSimpleEnumDeserializer);
            }

            @Override
            public Optional<SimpleEnum> optionalEnumQuery(AuthHeader authHeader, Optional<SimpleEnum> queryParamName) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                if (queryParamName.isPresent()) {
                    _request.putQueryParams("queryParamName", Objects.toString(queryParamName.get()));
                }
                return _runtime.clients()
                        .callBlocking(optionalEnumQueryChannel, _request.build(), optionalSimpleEnumDeserializer);
            }

            @Override
            public SimpleEnum enumHeader(AuthHeader authHeader, SimpleEnum headerParameter) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.putHeaderParams("Custom-Header", Objects.toString(headerParameter));
                return _runtime.clients().callBlocking(enumHeaderChannel, _request.build(), simpleEnumDeserializer);
            }

            @Override
            public String jsonErrorsHeader(AuthHeader authHeader, String headerParameter) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.putHeaderParams(
                        "Accept-Conjure-Error-Parameter-Format", _plainSerDe.serializeString(headerParameter));
                return _runtime.clients().callBlocking(jsonErrorsHeaderChannel, _request.build(), stringDeserializer);
            }

            @Override
            public String errorParameterSerialization(AuthHeader authHeader, String headerParameter) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.putHeaderParams(
                        "Accept-Conjure-Error-Parameter-Format", _plainSerDe.serializeString(headerParameter));
                return _runtime.clients()
                        .callBlocking(errorParameterSerializationChannel, _request.build(), stringDeserializer);
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
                        .callBlocking(aliasLongEndpointChannel, _request.build(), optionalLongAliasDeserializer);
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
                _runtime.clients().callBlocking(complexQueryParametersChannel, _request.build(), voidDeserializer);
            }

            @Override
            public void receiveListOfOptionals(AuthHeader authHeader, List<Optional<String>> value) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.body(receiveListOfOptionalsSerializer.serialize(value));
                _runtime.clients().callBlocking(receiveListOfOptionalsChannel, _request.build(), voidDeserializer);
            }

            @Override
            public void receiveSetOfOptionals(AuthHeader authHeader, Set<Optional<String>> value) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.body(receiveSetOfOptionalsSerializer.serialize(value));
                _runtime.clients().callBlocking(receiveSetOfOptionalsChannel, _request.build(), voidDeserializer);
            }

            @Override
            public void receiveListOfStrings(AuthHeader authHeader, List<String> value) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.body(receiveListOfStringsSerializer.serialize(value));
                _runtime.clients().callBlocking(receiveListOfStringsChannel, _request.build(), voidDeserializer);
            }

            @Override
            public SimpleUnion union(AuthHeader authHeader, SimpleUnion value) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.body(unionSerializer.serialize(value));
                return _runtime.clients().callBlocking(unionChannel, _request.build(), simpleUnionDeserializer);
            }

            @Override
            public String toString() {
                return "EteServiceBlocking{_endpointChannelFactory=" + _endpointChannelFactory + ", runtime=" + _runtime
                        + '}';
            }
        };
    }

    /** Creates an asynchronous/non-blocking client for a EteService service. */
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
