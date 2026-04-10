package exceptionthrowingdialogueinterfaces.com.palantir.product;

import com.google.common.util.concurrent.ListenableFuture;
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
import com.palantir.dialogue.ExceptionDeserializerArgs;
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
@DialogueService(EteServiceAsync.Factory.class)
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
     *
     * @apiNote {@code GET /base/string}
     */
    @ClientEndpoint(method = "GET", path = "/base/string")
    ListenableFuture<String> string(AuthHeader authHeader);

    /**
     * one <em>two</em> three.
     *
     * @apiNote {@code GET /base/integer}
     */
    @ClientEndpoint(method = "GET", path = "/base/integer")
    ListenableFuture<Integer> integer(AuthHeader authHeader);

    /** @apiNote {@code GET /base/double} */
    @ClientEndpoint(method = "GET", path = "/base/double")
    ListenableFuture<Double> double_(AuthHeader authHeader);

    /** @apiNote {@code GET /base/boolean} */
    @ClientEndpoint(method = "GET", path = "/base/boolean")
    ListenableFuture<Boolean> boolean_(AuthHeader authHeader);

    /** @apiNote {@code GET /base/safelong} */
    @ClientEndpoint(method = "GET", path = "/base/safelong")
    ListenableFuture<SafeLong> safelong(AuthHeader authHeader);

    /** @apiNote {@code GET /base/rid} */
    @ClientEndpoint(method = "GET", path = "/base/rid")
    ListenableFuture<ResourceIdentifier> rid(AuthHeader authHeader);

    /** @apiNote {@code GET /base/bearertoken} */
    @ClientEndpoint(method = "GET", path = "/base/bearertoken")
    ListenableFuture<BearerToken> bearertoken(AuthHeader authHeader);

    /** @apiNote {@code GET /base/optionalString} */
    @ClientEndpoint(method = "GET", path = "/base/optionalString")
    ListenableFuture<Optional<String>> optionalString(AuthHeader authHeader);

    /** @apiNote {@code GET /base/optionalEmpty} */
    @ClientEndpoint(method = "GET", path = "/base/optionalEmpty")
    ListenableFuture<Optional<String>> optionalEmpty(AuthHeader authHeader);

    /** @apiNote {@code GET /base/datetime} */
    @ClientEndpoint(method = "GET", path = "/base/datetime")
    ListenableFuture<OffsetDateTime> datetime(AuthHeader authHeader);

    /** @apiNote {@code GET /base/binary} */
    @ClientEndpoint(method = "GET", path = "/base/binary")
    ListenableFuture<InputStream> binary(AuthHeader authHeader);

    /**
     * Path endpoint.
     *
     * @apiNote {@code GET /base/path/{param}}
     * @param param Documentation for <code>param</code>
     */
    @ClientEndpoint(method = "GET", path = "/base/path/{param}")
    ListenableFuture<String> path(AuthHeader authHeader, String param);

    /** @apiNote {@code GET /base/externalLong/{param}} */
    @ClientEndpoint(method = "GET", path = "/base/externalLong/{param}")
    ListenableFuture<Long> externalLongPath(AuthHeader authHeader, long param);

    /** @apiNote {@code GET /base/optionalExternalLong} */
    @ClientEndpoint(method = "GET", path = "/base/optionalExternalLong")
    ListenableFuture<Optional<Long>> optionalExternalLongQuery(AuthHeader authHeader, Optional<Long> param);

    /** @apiNote {@code POST /base/notNullBody} */
    @ClientEndpoint(method = "POST", path = "/base/notNullBody")
    ListenableFuture<StringAliasExample> notNullBody(AuthHeader authHeader, StringAliasExample notNullBody);

    /** @apiNote {@code GET /base/aliasOne} */
    @ClientEndpoint(method = "GET", path = "/base/aliasOne")
    ListenableFuture<StringAliasExample> aliasOne(AuthHeader authHeader, StringAliasExample queryParamName);

    /** @apiNote {@code GET /base/optionalAliasOne} */
    @ClientEndpoint(method = "GET", path = "/base/optionalAliasOne")
    ListenableFuture<StringAliasExample> optionalAliasOne(
            AuthHeader authHeader, Optional<StringAliasExample> queryParamName);

    /** @apiNote {@code GET /base/aliasTwo} */
    @ClientEndpoint(method = "GET", path = "/base/aliasTwo")
    ListenableFuture<NestedStringAliasExample> aliasTwo(AuthHeader authHeader, NestedStringAliasExample queryParamName);

    /** @apiNote {@code POST /base/external/notNullBody} */
    @ClientEndpoint(method = "POST", path = "/base/external/notNullBody")
    ListenableFuture<allexamples.com.palantir.product.StringAliasExample> notNullBodyExternalImport(
            AuthHeader authHeader, allexamples.com.palantir.product.StringAliasExample notNullBody);

    /** @apiNote {@code POST /base/external/optional-body} */
    @ClientEndpoint(method = "POST", path = "/base/external/optional-body")
    ListenableFuture<Optional<allexamples.com.palantir.product.StringAliasExample>> optionalBodyExternalImport(
            AuthHeader authHeader, Optional<allexamples.com.palantir.product.StringAliasExample> body);

    /** @apiNote {@code POST /base/external/optional-query} */
    @ClientEndpoint(method = "POST", path = "/base/external/optional-query")
    ListenableFuture<Optional<allexamples.com.palantir.product.StringAliasExample>> optionalQueryExternalImport(
            AuthHeader authHeader, Optional<allexamples.com.palantir.product.StringAliasExample> query);

    /** @apiNote {@code POST /base/no-return} */
    @ClientEndpoint(method = "POST", path = "/base/no-return")
    ListenableFuture<Void> noReturn(AuthHeader authHeader);

    /** @apiNote {@code GET /base/enum/query} */
    @ClientEndpoint(method = "GET", path = "/base/enum/query")
    ListenableFuture<SimpleEnum> enumQuery(AuthHeader authHeader, SimpleEnum queryParamName);

    /** @apiNote {@code GET /base/enum/list/query} */
    @ClientEndpoint(method = "GET", path = "/base/enum/list/query")
    ListenableFuture<List<SimpleEnum>> enumListQuery(AuthHeader authHeader, List<SimpleEnum> queryParamName);

    /** @apiNote {@code GET /base/enum/optional/query} */
    @ClientEndpoint(method = "GET", path = "/base/enum/optional/query")
    ListenableFuture<Optional<SimpleEnum>> optionalEnumQuery(
            AuthHeader authHeader, Optional<SimpleEnum> queryParamName);

    /** @apiNote {@code GET /base/enum/header} */
    @ClientEndpoint(method = "GET", path = "/base/enum/header")
    ListenableFuture<SimpleEnum> enumHeader(AuthHeader authHeader, SimpleEnum headerParameter);

    /**
     * This endpoint is used to test that the <code>Accept-Conjure-Error-Parameter-Format</code> header is respected.
     * Specifically, that error parameters are serialized as JSON when the header is set to <code>JSON</code>.
     *
     * @apiNote {@code GET /base/errors/header}
     */
    @ClientEndpoint(method = "GET", path = "/base/errors/header")
    ListenableFuture<String> jsonErrorsHeader(AuthHeader authHeader, String headerParameter);

    /**
     * This endpoint is used to test that error parameters serialized as JSON or using <code>Objects.toString</code> are
     * both handled correctly by the clients that can deserialize &quot;rich&quot; exceptions (which are sub-types of
     * <code>RemoteException</code>).
     *
     * @apiNote {@code GET /base/errors/serialization}
     */
    @ClientEndpoint(method = "GET", path = "/base/errors/serialization")
    ListenableFuture<String> errorParameterSerialization(AuthHeader authHeader, String headerParameter);

    /** @apiNote {@code GET /base/alias-long} */
    @ClientEndpoint(method = "GET", path = "/base/alias-long")
    ListenableFuture<Optional<LongAlias>> aliasLongEndpoint(AuthHeader authHeader, Optional<LongAlias> input);

    /** @apiNote {@code GET /base/datasets/{datasetRid}/strings} */
    @ClientEndpoint(method = "GET", path = "/base/datasets/{datasetRid}/strings")
    ListenableFuture<Void> complexQueryParameters(
            AuthHeader authHeader,
            ResourceIdentifier datasetRid,
            Set<StringAliasExample> strings,
            Set<Long> longs,
            Set<Integer> ints);

    /** @apiNote {@code PUT /base/list/optionals} */
    @ClientEndpoint(method = "PUT", path = "/base/list/optionals")
    ListenableFuture<Void> receiveListOfOptionals(AuthHeader authHeader, List<Optional<String>> value);

    /** @apiNote {@code PUT /base/set/optionals} */
    @ClientEndpoint(method = "PUT", path = "/base/set/optionals")
    ListenableFuture<Void> receiveSetOfOptionals(AuthHeader authHeader, Set<Optional<String>> value);

    /** @apiNote {@code PUT /base/list/strings} */
    @ClientEndpoint(method = "PUT", path = "/base/list/strings")
    ListenableFuture<Void> receiveListOfStrings(AuthHeader authHeader, List<String> value);

    /** @apiNote {@code PUT /base/union} */
    @ClientEndpoint(method = "PUT", path = "/base/union")
    ListenableFuture<SimpleUnion> union(AuthHeader authHeader, SimpleUnion value);

    /** Creates an asynchronous/non-blocking client for a EteService service. */
    static EteServiceAsync of(EndpointChannelFactory _endpointChannelFactory, ConjureRuntime _runtime) {
        return new EteServiceAsync() {
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

            private static final TypeMarker<InputStream> inputStreamTypeMarker = new TypeMarker<InputStream>() {};

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

            private static final TypeMarker<Void> voidTypeMarker = new TypeMarker<Void>() {};

            private static final TypeMarker<SimpleEnum> simpleEnumTypeMarker = new TypeMarker<SimpleEnum>() {};

            private static final TypeMarker<List<SimpleEnum>> listSimpleEnumTypeMarker =
                    new TypeMarker<List<SimpleEnum>>() {};

            private static final TypeMarker<Optional<SimpleEnum>> optionalSimpleEnumTypeMarker =
                    new TypeMarker<Optional<SimpleEnum>>() {};

            private static final TypeMarker<Optional<LongAlias>> optionalLongAliasTypeMarker =
                    new TypeMarker<Optional<LongAlias>>() {};

            private static final TypeMarker<SimpleUnion> simpleUnionTypeMarker = new TypeMarker<SimpleUnion>() {};

            private static final ExceptionDeserializerArgs<String> stringExceptionArgs =
                    createExceptionDeserializerArgs(stringTypeMarker);

            private static final ExceptionDeserializerArgs<Integer> integerExceptionArgs =
                    createExceptionDeserializerArgs(integerTypeMarker);

            private static final ExceptionDeserializerArgs<Double> doubleExceptionArgs =
                    createExceptionDeserializerArgs(doubleTypeMarker);

            private static final ExceptionDeserializerArgs<Boolean> booleanExceptionArgs =
                    createExceptionDeserializerArgs(booleanTypeMarker);

            private static final ExceptionDeserializerArgs<SafeLong> safeLongExceptionArgs =
                    createExceptionDeserializerArgs(safeLongTypeMarker);

            private static final ExceptionDeserializerArgs<ResourceIdentifier> resourceIdentifierExceptionArgs =
                    createExceptionDeserializerArgs(resourceIdentifierTypeMarker);

            private static final ExceptionDeserializerArgs<BearerToken> bearerTokenExceptionArgs =
                    createExceptionDeserializerArgs(bearerTokenTypeMarker);

            private static final ExceptionDeserializerArgs<Optional<String>> optionalStringExceptionArgs =
                    createExceptionDeserializerArgs(optionalStringTypeMarker);

            private static final ExceptionDeserializerArgs<OffsetDateTime> offsetDateTimeExceptionArgs =
                    createExceptionDeserializerArgs(offsetDateTimeTypeMarker);

            private static final ExceptionDeserializerArgs<InputStream> inputStreamExceptionArgs =
                    createExceptionDeserializerArgs(inputStreamTypeMarker);

            private static final ExceptionDeserializerArgs<Long> longExceptionArgs =
                    createExceptionDeserializerArgs(longTypeMarker);

            private static final ExceptionDeserializerArgs<Optional<Long>> optionalLongExceptionArgs =
                    createExceptionDeserializerArgs(optionalLongTypeMarker);

            private static final ExceptionDeserializerArgs<StringAliasExample> stringAliasExampleExceptionArgs =
                    createExceptionDeserializerArgs(stringAliasExampleTypeMarker);

            private static final ExceptionDeserializerArgs<NestedStringAliasExample>
                    nestedStringAliasExampleExceptionArgs =
                            createExceptionDeserializerArgs(nestedStringAliasExampleTypeMarker);

            private static final ExceptionDeserializerArgs<allexamples.com.palantir.product.StringAliasExample>
                    stringAliasExample2ExceptionArgs = createExceptionDeserializerArgs(stringAliasExample2TypeMarker);

            private static final ExceptionDeserializerArgs<
                            Optional<allexamples.com.palantir.product.StringAliasExample>>
                    optionalStringAliasExampleExceptionArgs =
                            createExceptionDeserializerArgs(optionalStringAliasExampleTypeMarker);

            private static final ExceptionDeserializerArgs<Void> voidExceptionArgs =
                    createExceptionDeserializerArgs(voidTypeMarker);

            private static final ExceptionDeserializerArgs<SimpleEnum> simpleEnumExceptionArgs =
                    createExceptionDeserializerArgs(simpleEnumTypeMarker);

            private static final ExceptionDeserializerArgs<List<SimpleEnum>> listSimpleEnumExceptionArgs =
                    createExceptionDeserializerArgs(listSimpleEnumTypeMarker);

            private static final ExceptionDeserializerArgs<Optional<SimpleEnum>> optionalSimpleEnumExceptionArgs =
                    createExceptionDeserializerArgs(optionalSimpleEnumTypeMarker);

            private static final ExceptionDeserializerArgs<Optional<LongAlias>> optionalLongAliasExceptionArgs =
                    createExceptionDeserializerArgs(optionalLongAliasTypeMarker);

            private static final ExceptionDeserializerArgs<SimpleUnion> simpleUnionExceptionArgs =
                    createExceptionDeserializerArgs(simpleUnionTypeMarker);

            private final PlainSerDe _plainSerDe = _runtime.plainSerDe();

            private final Deserializer<String> stringDeserializer =
                    _runtime.bodySerDe().deserializer(stringExceptionArgs);

            private final Deserializer<Integer> integerDeserializer =
                    _runtime.bodySerDe().deserializer(integerExceptionArgs);

            private final Deserializer<Double> doubleDeserializer =
                    _runtime.bodySerDe().deserializer(doubleExceptionArgs);

            private final Deserializer<Boolean> booleanDeserializer =
                    _runtime.bodySerDe().deserializer(booleanExceptionArgs);

            private final Deserializer<SafeLong> safeLongDeserializer =
                    _runtime.bodySerDe().deserializer(safeLongExceptionArgs);

            private final Deserializer<ResourceIdentifier> resourceIdentifierDeserializer =
                    _runtime.bodySerDe().deserializer(resourceIdentifierExceptionArgs);

            private final Deserializer<BearerToken> bearerTokenDeserializer =
                    _runtime.bodySerDe().deserializer(bearerTokenExceptionArgs);

            private final Deserializer<Optional<String>> optionalStringDeserializer =
                    _runtime.bodySerDe().deserializer(optionalStringExceptionArgs);

            private final Deserializer<OffsetDateTime> offsetDateTimeDeserializer =
                    _runtime.bodySerDe().deserializer(offsetDateTimeExceptionArgs);

            private final Deserializer<InputStream> inputStreamDeserializer =
                    _runtime.bodySerDe().inputStreamDeserializer(inputStreamExceptionArgs);

            private final Deserializer<Long> longDeserializer =
                    _runtime.bodySerDe().deserializer(longExceptionArgs);

            private final Deserializer<Optional<Long>> optionalLongDeserializer =
                    _runtime.bodySerDe().deserializer(optionalLongExceptionArgs);

            private final Deserializer<StringAliasExample> stringAliasExampleDeserializer =
                    _runtime.bodySerDe().deserializer(stringAliasExampleExceptionArgs);

            private final Deserializer<NestedStringAliasExample> nestedStringAliasExampleDeserializer =
                    _runtime.bodySerDe().deserializer(nestedStringAliasExampleExceptionArgs);

            private final Deserializer<allexamples.com.palantir.product.StringAliasExample>
                    stringAliasExample2Deserializer =
                            _runtime.bodySerDe().deserializer(stringAliasExample2ExceptionArgs);

            private final Deserializer<Optional<allexamples.com.palantir.product.StringAliasExample>>
                    optionalStringAliasExampleDeserializer =
                            _runtime.bodySerDe().deserializer(optionalStringAliasExampleExceptionArgs);

            private final Deserializer<Void> voidDeserializer =
                    _runtime.bodySerDe().emptyBodyDeserializer(voidExceptionArgs);

            private final Deserializer<SimpleEnum> simpleEnumDeserializer =
                    _runtime.bodySerDe().deserializer(simpleEnumExceptionArgs);

            private final Deserializer<List<SimpleEnum>> listSimpleEnumDeserializer =
                    _runtime.bodySerDe().deserializer(listSimpleEnumExceptionArgs);

            private final Deserializer<Optional<SimpleEnum>> optionalSimpleEnumDeserializer =
                    _runtime.bodySerDe().deserializer(optionalSimpleEnumExceptionArgs);

            private final Deserializer<Optional<LongAlias>> optionalLongAliasDeserializer =
                    _runtime.bodySerDe().deserializer(optionalLongAliasExceptionArgs);

            private final Deserializer<SimpleUnion> simpleUnionDeserializer =
                    _runtime.bodySerDe().deserializer(simpleUnionExceptionArgs);

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

            private static <T> ExceptionDeserializerArgs<T> createExceptionDeserializerArgs(TypeMarker<T> returnType) {
                ExceptionDeserializerArgs.Builder<T> builder =
                        ExceptionDeserializerArgs.<T>builder().returnType(returnType);
                exceptionthrowingdialogueinterfaces.com.palantir.another.ConjureErrorsTypeMarkers.registerExceptions(
                        builder);
                ConjureErrorsTypeMarkers.registerExceptions(builder);
                ConjureJavaErrorsTypeMarkers.registerExceptions(builder);
                return builder.build();
            }

            @Override
            public ListenableFuture<String> string(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                if (_runtime.bodySerDe().errorParameterFormat().isPresent()) {
                    _request.putHeaderParams(
                            "Accept-Conjure-Error-Parameter-Format",
                            _runtime.bodySerDe().errorParameterFormat().get().toString());
                }
                return _runtime.clients().call(stringChannel, _request.build(), stringDeserializer);
            }

            @Override
            public ListenableFuture<Integer> integer(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                if (_runtime.bodySerDe().errorParameterFormat().isPresent()) {
                    _request.putHeaderParams(
                            "Accept-Conjure-Error-Parameter-Format",
                            _runtime.bodySerDe().errorParameterFormat().get().toString());
                }
                return _runtime.clients().call(integerChannel, _request.build(), integerDeserializer);
            }

            @Override
            public ListenableFuture<Double> double_(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                if (_runtime.bodySerDe().errorParameterFormat().isPresent()) {
                    _request.putHeaderParams(
                            "Accept-Conjure-Error-Parameter-Format",
                            _runtime.bodySerDe().errorParameterFormat().get().toString());
                }
                return _runtime.clients().call(double_Channel, _request.build(), doubleDeserializer);
            }

            @Override
            public ListenableFuture<Boolean> boolean_(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                if (_runtime.bodySerDe().errorParameterFormat().isPresent()) {
                    _request.putHeaderParams(
                            "Accept-Conjure-Error-Parameter-Format",
                            _runtime.bodySerDe().errorParameterFormat().get().toString());
                }
                return _runtime.clients().call(boolean_Channel, _request.build(), booleanDeserializer);
            }

            @Override
            public ListenableFuture<SafeLong> safelong(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                if (_runtime.bodySerDe().errorParameterFormat().isPresent()) {
                    _request.putHeaderParams(
                            "Accept-Conjure-Error-Parameter-Format",
                            _runtime.bodySerDe().errorParameterFormat().get().toString());
                }
                return _runtime.clients().call(safelongChannel, _request.build(), safeLongDeserializer);
            }

            @Override
            public ListenableFuture<ResourceIdentifier> rid(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                if (_runtime.bodySerDe().errorParameterFormat().isPresent()) {
                    _request.putHeaderParams(
                            "Accept-Conjure-Error-Parameter-Format",
                            _runtime.bodySerDe().errorParameterFormat().get().toString());
                }
                return _runtime.clients().call(ridChannel, _request.build(), resourceIdentifierDeserializer);
            }

            @Override
            public ListenableFuture<BearerToken> bearertoken(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                if (_runtime.bodySerDe().errorParameterFormat().isPresent()) {
                    _request.putHeaderParams(
                            "Accept-Conjure-Error-Parameter-Format",
                            _runtime.bodySerDe().errorParameterFormat().get().toString());
                }
                return _runtime.clients().call(bearertokenChannel, _request.build(), bearerTokenDeserializer);
            }

            @Override
            public ListenableFuture<Optional<String>> optionalString(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                if (_runtime.bodySerDe().errorParameterFormat().isPresent()) {
                    _request.putHeaderParams(
                            "Accept-Conjure-Error-Parameter-Format",
                            _runtime.bodySerDe().errorParameterFormat().get().toString());
                }
                return _runtime.clients().call(optionalStringChannel, _request.build(), optionalStringDeserializer);
            }

            @Override
            public ListenableFuture<Optional<String>> optionalEmpty(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                if (_runtime.bodySerDe().errorParameterFormat().isPresent()) {
                    _request.putHeaderParams(
                            "Accept-Conjure-Error-Parameter-Format",
                            _runtime.bodySerDe().errorParameterFormat().get().toString());
                }
                return _runtime.clients().call(optionalEmptyChannel, _request.build(), optionalStringDeserializer);
            }

            @Override
            public ListenableFuture<OffsetDateTime> datetime(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                if (_runtime.bodySerDe().errorParameterFormat().isPresent()) {
                    _request.putHeaderParams(
                            "Accept-Conjure-Error-Parameter-Format",
                            _runtime.bodySerDe().errorParameterFormat().get().toString());
                }
                return _runtime.clients().call(datetimeChannel, _request.build(), offsetDateTimeDeserializer);
            }

            @Override
            public ListenableFuture<InputStream> binary(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                if (_runtime.bodySerDe().errorParameterFormat().isPresent()) {
                    _request.putHeaderParams(
                            "Accept-Conjure-Error-Parameter-Format",
                            _runtime.bodySerDe().errorParameterFormat().get().toString());
                }
                return _runtime.clients().call(binaryChannel, _request.build(), inputStreamDeserializer);
            }

            @Override
            public ListenableFuture<String> path(AuthHeader authHeader, String param) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.putPathParams("param", _plainSerDe.serializeString(param));
                if (_runtime.bodySerDe().errorParameterFormat().isPresent()) {
                    _request.putHeaderParams(
                            "Accept-Conjure-Error-Parameter-Format",
                            _runtime.bodySerDe().errorParameterFormat().get().toString());
                }
                return _runtime.clients().call(pathChannel, _request.build(), stringDeserializer);
            }

            @Override
            public ListenableFuture<Long> externalLongPath(AuthHeader authHeader, long param) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.putPathParams("param", Objects.toString(param));
                if (_runtime.bodySerDe().errorParameterFormat().isPresent()) {
                    _request.putHeaderParams(
                            "Accept-Conjure-Error-Parameter-Format",
                            _runtime.bodySerDe().errorParameterFormat().get().toString());
                }
                return _runtime.clients().call(externalLongPathChannel, _request.build(), longDeserializer);
            }

            @Override
            public ListenableFuture<Optional<Long>> optionalExternalLongQuery(
                    AuthHeader authHeader, Optional<Long> param) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                if (param.isPresent()) {
                    _request.putQueryParams("param", Objects.toString(param.get()));
                }
                if (_runtime.bodySerDe().errorParameterFormat().isPresent()) {
                    _request.putHeaderParams(
                            "Accept-Conjure-Error-Parameter-Format",
                            _runtime.bodySerDe().errorParameterFormat().get().toString());
                }
                return _runtime.clients()
                        .call(optionalExternalLongQueryChannel, _request.build(), optionalLongDeserializer);
            }

            @Override
            public ListenableFuture<StringAliasExample> notNullBody(
                    AuthHeader authHeader, StringAliasExample notNullBody) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.body(notNullBodySerializer.serialize(notNullBody));
                if (_runtime.bodySerDe().errorParameterFormat().isPresent()) {
                    _request.putHeaderParams(
                            "Accept-Conjure-Error-Parameter-Format",
                            _runtime.bodySerDe().errorParameterFormat().get().toString());
                }
                return _runtime.clients().call(notNullBodyChannel, _request.build(), stringAliasExampleDeserializer);
            }

            @Override
            public ListenableFuture<StringAliasExample> aliasOne(
                    AuthHeader authHeader, StringAliasExample queryParamName) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.putQueryParams("queryParamName", _plainSerDe.serializeString(queryParamName.get()));
                if (_runtime.bodySerDe().errorParameterFormat().isPresent()) {
                    _request.putHeaderParams(
                            "Accept-Conjure-Error-Parameter-Format",
                            _runtime.bodySerDe().errorParameterFormat().get().toString());
                }
                return _runtime.clients().call(aliasOneChannel, _request.build(), stringAliasExampleDeserializer);
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
                if (_runtime.bodySerDe().errorParameterFormat().isPresent()) {
                    _request.putHeaderParams(
                            "Accept-Conjure-Error-Parameter-Format",
                            _runtime.bodySerDe().errorParameterFormat().get().toString());
                }
                return _runtime.clients()
                        .call(optionalAliasOneChannel, _request.build(), stringAliasExampleDeserializer);
            }

            @Override
            public ListenableFuture<NestedStringAliasExample> aliasTwo(
                    AuthHeader authHeader, NestedStringAliasExample queryParamName) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.putQueryParams(
                        "queryParamName",
                        _plainSerDe.serializeString(queryParamName.get().get()));
                if (_runtime.bodySerDe().errorParameterFormat().isPresent()) {
                    _request.putHeaderParams(
                            "Accept-Conjure-Error-Parameter-Format",
                            _runtime.bodySerDe().errorParameterFormat().get().toString());
                }
                return _runtime.clients().call(aliasTwoChannel, _request.build(), nestedStringAliasExampleDeserializer);
            }

            @Override
            public ListenableFuture<allexamples.com.palantir.product.StringAliasExample> notNullBodyExternalImport(
                    AuthHeader authHeader, allexamples.com.palantir.product.StringAliasExample notNullBody) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.body(notNullBodyExternalImportSerializer.serialize(notNullBody));
                if (_runtime.bodySerDe().errorParameterFormat().isPresent()) {
                    _request.putHeaderParams(
                            "Accept-Conjure-Error-Parameter-Format",
                            _runtime.bodySerDe().errorParameterFormat().get().toString());
                }
                return _runtime.clients()
                        .call(notNullBodyExternalImportChannel, _request.build(), stringAliasExample2Deserializer);
            }

            @Override
            public ListenableFuture<Optional<allexamples.com.palantir.product.StringAliasExample>>
                    optionalBodyExternalImport(
                            AuthHeader authHeader, Optional<allexamples.com.palantir.product.StringAliasExample> body) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.body(optionalBodyExternalImportSerializer.serialize(body));
                if (_runtime.bodySerDe().errorParameterFormat().isPresent()) {
                    _request.putHeaderParams(
                            "Accept-Conjure-Error-Parameter-Format",
                            _runtime.bodySerDe().errorParameterFormat().get().toString());
                }
                return _runtime.clients()
                        .call(
                                optionalBodyExternalImportChannel,
                                _request.build(),
                                optionalStringAliasExampleDeserializer);
            }

            @Override
            public ListenableFuture<Optional<allexamples.com.palantir.product.StringAliasExample>>
                    optionalQueryExternalImport(
                            AuthHeader authHeader,
                            Optional<allexamples.com.palantir.product.StringAliasExample> query) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                if (query.isPresent()) {
                    _request.putQueryParams("query", Objects.toString(query.get()));
                }
                if (_runtime.bodySerDe().errorParameterFormat().isPresent()) {
                    _request.putHeaderParams(
                            "Accept-Conjure-Error-Parameter-Format",
                            _runtime.bodySerDe().errorParameterFormat().get().toString());
                }
                return _runtime.clients()
                        .call(
                                optionalQueryExternalImportChannel,
                                _request.build(),
                                optionalStringAliasExampleDeserializer);
            }

            @Override
            public ListenableFuture<Void> noReturn(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                if (_runtime.bodySerDe().errorParameterFormat().isPresent()) {
                    _request.putHeaderParams(
                            "Accept-Conjure-Error-Parameter-Format",
                            _runtime.bodySerDe().errorParameterFormat().get().toString());
                }
                return _runtime.clients().call(noReturnChannel, _request.build(), voidDeserializer);
            }

            @Override
            public ListenableFuture<SimpleEnum> enumQuery(AuthHeader authHeader, SimpleEnum queryParamName) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.putQueryParams("queryParamName", Objects.toString(queryParamName));
                if (_runtime.bodySerDe().errorParameterFormat().isPresent()) {
                    _request.putHeaderParams(
                            "Accept-Conjure-Error-Parameter-Format",
                            _runtime.bodySerDe().errorParameterFormat().get().toString());
                }
                return _runtime.clients().call(enumQueryChannel, _request.build(), simpleEnumDeserializer);
            }

            @Override
            public ListenableFuture<List<SimpleEnum>> enumListQuery(
                    AuthHeader authHeader, List<SimpleEnum> queryParamName) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                for (SimpleEnum queryParamNameElement : queryParamName) {
                    _request.putQueryParams("queryParamName", Objects.toString(queryParamNameElement));
                }
                if (_runtime.bodySerDe().errorParameterFormat().isPresent()) {
                    _request.putHeaderParams(
                            "Accept-Conjure-Error-Parameter-Format",
                            _runtime.bodySerDe().errorParameterFormat().get().toString());
                }
                return _runtime.clients().call(enumListQueryChannel, _request.build(), listSimpleEnumDeserializer);
            }

            @Override
            public ListenableFuture<Optional<SimpleEnum>> optionalEnumQuery(
                    AuthHeader authHeader, Optional<SimpleEnum> queryParamName) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                if (queryParamName.isPresent()) {
                    _request.putQueryParams("queryParamName", Objects.toString(queryParamName.get()));
                }
                if (_runtime.bodySerDe().errorParameterFormat().isPresent()) {
                    _request.putHeaderParams(
                            "Accept-Conjure-Error-Parameter-Format",
                            _runtime.bodySerDe().errorParameterFormat().get().toString());
                }
                return _runtime.clients()
                        .call(optionalEnumQueryChannel, _request.build(), optionalSimpleEnumDeserializer);
            }

            @Override
            public ListenableFuture<SimpleEnum> enumHeader(AuthHeader authHeader, SimpleEnum headerParameter) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.putHeaderParams("Custom-Header", Objects.toString(headerParameter));
                if (_runtime.bodySerDe().errorParameterFormat().isPresent()) {
                    _request.putHeaderParams(
                            "Accept-Conjure-Error-Parameter-Format",
                            _runtime.bodySerDe().errorParameterFormat().get().toString());
                }
                return _runtime.clients().call(enumHeaderChannel, _request.build(), simpleEnumDeserializer);
            }

            @Override
            public ListenableFuture<String> jsonErrorsHeader(AuthHeader authHeader, String headerParameter) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.putHeaderParams(
                        "Accept-Conjure-Error-Parameter-Format", _plainSerDe.serializeString(headerParameter));
                if (_runtime.bodySerDe().errorParameterFormat().isPresent()) {
                    _request.putHeaderParams(
                            "Accept-Conjure-Error-Parameter-Format",
                            _runtime.bodySerDe().errorParameterFormat().get().toString());
                }
                return _runtime.clients().call(jsonErrorsHeaderChannel, _request.build(), stringDeserializer);
            }

            @Override
            public ListenableFuture<String> errorParameterSerialization(AuthHeader authHeader, String headerParameter) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.putHeaderParams(
                        "Accept-Conjure-Error-Parameter-Format", _plainSerDe.serializeString(headerParameter));
                if (_runtime.bodySerDe().errorParameterFormat().isPresent()) {
                    _request.putHeaderParams(
                            "Accept-Conjure-Error-Parameter-Format",
                            _runtime.bodySerDe().errorParameterFormat().get().toString());
                }
                return _runtime.clients()
                        .call(errorParameterSerializationChannel, _request.build(), stringDeserializer);
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
                if (_runtime.bodySerDe().errorParameterFormat().isPresent()) {
                    _request.putHeaderParams(
                            "Accept-Conjure-Error-Parameter-Format",
                            _runtime.bodySerDe().errorParameterFormat().get().toString());
                }
                return _runtime.clients()
                        .call(aliasLongEndpointChannel, _request.build(), optionalLongAliasDeserializer);
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
                if (_runtime.bodySerDe().errorParameterFormat().isPresent()) {
                    _request.putHeaderParams(
                            "Accept-Conjure-Error-Parameter-Format",
                            _runtime.bodySerDe().errorParameterFormat().get().toString());
                }
                return _runtime.clients().call(complexQueryParametersChannel, _request.build(), voidDeserializer);
            }

            @Override
            public ListenableFuture<Void> receiveListOfOptionals(AuthHeader authHeader, List<Optional<String>> value) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.body(receiveListOfOptionalsSerializer.serialize(value));
                if (_runtime.bodySerDe().errorParameterFormat().isPresent()) {
                    _request.putHeaderParams(
                            "Accept-Conjure-Error-Parameter-Format",
                            _runtime.bodySerDe().errorParameterFormat().get().toString());
                }
                return _runtime.clients().call(receiveListOfOptionalsChannel, _request.build(), voidDeserializer);
            }

            @Override
            public ListenableFuture<Void> receiveSetOfOptionals(AuthHeader authHeader, Set<Optional<String>> value) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.body(receiveSetOfOptionalsSerializer.serialize(value));
                if (_runtime.bodySerDe().errorParameterFormat().isPresent()) {
                    _request.putHeaderParams(
                            "Accept-Conjure-Error-Parameter-Format",
                            _runtime.bodySerDe().errorParameterFormat().get().toString());
                }
                return _runtime.clients().call(receiveSetOfOptionalsChannel, _request.build(), voidDeserializer);
            }

            @Override
            public ListenableFuture<Void> receiveListOfStrings(AuthHeader authHeader, List<String> value) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.body(receiveListOfStringsSerializer.serialize(value));
                if (_runtime.bodySerDe().errorParameterFormat().isPresent()) {
                    _request.putHeaderParams(
                            "Accept-Conjure-Error-Parameter-Format",
                            _runtime.bodySerDe().errorParameterFormat().get().toString());
                }
                return _runtime.clients().call(receiveListOfStringsChannel, _request.build(), voidDeserializer);
            }

            @Override
            public ListenableFuture<SimpleUnion> union(AuthHeader authHeader, SimpleUnion value) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.body(unionSerializer.serialize(value));
                if (_runtime.bodySerDe().errorParameterFormat().isPresent()) {
                    _request.putHeaderParams(
                            "Accept-Conjure-Error-Parameter-Format",
                            _runtime.bodySerDe().errorParameterFormat().get().toString());
                }
                return _runtime.clients().call(unionChannel, _request.build(), simpleUnionDeserializer);
            }

            @Override
            public String toString() {
                return "EteServiceAsync{_endpointChannelFactory=" + _endpointChannelFactory + ", runtime=" + _runtime
                        + '}';
            }
        };
    }

    /** Creates an asynchronous/non-blocking client for a EteService service. */
    static EteServiceAsync of(Channel _channel, ConjureRuntime _runtime) {
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

    final class Factory implements DialogueServiceFactory<EteServiceAsync> {
        @Override
        public EteServiceAsync create(EndpointChannelFactory endpointChannelFactory, ConjureRuntime runtime) {
            return EteServiceAsync.of(endpointChannelFactory, runtime);
        }
    }
}
