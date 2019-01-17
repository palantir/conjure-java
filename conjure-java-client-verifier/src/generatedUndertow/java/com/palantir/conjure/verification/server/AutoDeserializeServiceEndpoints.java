package com.palantir.conjure.verification.server;

import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.conjure.java.undertow.lib.EndpointRegistry;
import com.palantir.conjure.java.undertow.lib.Registrable;
import com.palantir.conjure.java.undertow.lib.SerializerRegistry;
import com.palantir.conjure.java.undertow.lib.Service;
import com.palantir.conjure.java.undertow.lib.ServiceContext;
import com.palantir.conjure.java.undertow.lib.internal.StringDeserializers;
import com.palantir.conjure.verification.types.AnyExample;
import com.palantir.conjure.verification.types.BearerTokenAliasExample;
import com.palantir.conjure.verification.types.BearerTokenExample;
import com.palantir.conjure.verification.types.BooleanAliasExample;
import com.palantir.conjure.verification.types.BooleanExample;
import com.palantir.conjure.verification.types.DateTimeAliasExample;
import com.palantir.conjure.verification.types.DateTimeExample;
import com.palantir.conjure.verification.types.DoubleAliasExample;
import com.palantir.conjure.verification.types.DoubleExample;
import com.palantir.conjure.verification.types.EnumExample;
import com.palantir.conjure.verification.types.IntegerAliasExample;
import com.palantir.conjure.verification.types.IntegerExample;
import com.palantir.conjure.verification.types.KebabCaseObjectExample;
import com.palantir.conjure.verification.types.ListAnyAliasExample;
import com.palantir.conjure.verification.types.ListBearerTokenAliasExample;
import com.palantir.conjure.verification.types.ListBinaryAliasExample;
import com.palantir.conjure.verification.types.ListBooleanAliasExample;
import com.palantir.conjure.verification.types.ListDateTimeAliasExample;
import com.palantir.conjure.verification.types.ListDoubleAliasExample;
import com.palantir.conjure.verification.types.ListExample;
import com.palantir.conjure.verification.types.ListIntegerAliasExample;
import com.palantir.conjure.verification.types.ListRidAliasExample;
import com.palantir.conjure.verification.types.ListSafeLongAliasExample;
import com.palantir.conjure.verification.types.ListStringAliasExample;
import com.palantir.conjure.verification.types.ListUuidAliasExample;
import com.palantir.conjure.verification.types.LongFieldNameOptionalExample;
import com.palantir.conjure.verification.types.MapBearerTokenAliasExample;
import com.palantir.conjure.verification.types.MapBinaryAliasExample;
import com.palantir.conjure.verification.types.MapBooleanAliasExample;
import com.palantir.conjure.verification.types.MapDateTimeAliasExample;
import com.palantir.conjure.verification.types.MapDoubleAliasExample;
import com.palantir.conjure.verification.types.MapEnumExampleAlias;
import com.palantir.conjure.verification.types.MapExample;
import com.palantir.conjure.verification.types.MapIntegerAliasExample;
import com.palantir.conjure.verification.types.MapRidAliasExample;
import com.palantir.conjure.verification.types.MapSafeLongAliasExample;
import com.palantir.conjure.verification.types.MapStringAliasExample;
import com.palantir.conjure.verification.types.MapUuidAliasExample;
import com.palantir.conjure.verification.types.OptionalAnyAliasExample;
import com.palantir.conjure.verification.types.OptionalBearerTokenAliasExample;
import com.palantir.conjure.verification.types.OptionalBinaryAliasExample;
import com.palantir.conjure.verification.types.OptionalBooleanAliasExample;
import com.palantir.conjure.verification.types.OptionalBooleanExample;
import com.palantir.conjure.verification.types.OptionalDateTimeAliasExample;
import com.palantir.conjure.verification.types.OptionalDoubleAliasExample;
import com.palantir.conjure.verification.types.OptionalExample;
import com.palantir.conjure.verification.types.OptionalIntegerAliasExample;
import com.palantir.conjure.verification.types.OptionalIntegerExample;
import com.palantir.conjure.verification.types.OptionalRidAliasExample;
import com.palantir.conjure.verification.types.OptionalSafeLongAliasExample;
import com.palantir.conjure.verification.types.OptionalStringAliasExample;
import com.palantir.conjure.verification.types.OptionalUuidAliasExample;
import com.palantir.conjure.verification.types.RawOptionalExample;
import com.palantir.conjure.verification.types.ReferenceAliasExample;
import com.palantir.conjure.verification.types.RidAliasExample;
import com.palantir.conjure.verification.types.RidExample;
import com.palantir.conjure.verification.types.SafeLongAliasExample;
import com.palantir.conjure.verification.types.SafeLongExample;
import com.palantir.conjure.verification.types.SetAnyAliasExample;
import com.palantir.conjure.verification.types.SetBearerTokenAliasExample;
import com.palantir.conjure.verification.types.SetBinaryAliasExample;
import com.palantir.conjure.verification.types.SetBooleanAliasExample;
import com.palantir.conjure.verification.types.SetDateTimeAliasExample;
import com.palantir.conjure.verification.types.SetDoubleAliasExample;
import com.palantir.conjure.verification.types.SetDoubleExample;
import com.palantir.conjure.verification.types.SetIntegerAliasExample;
import com.palantir.conjure.verification.types.SetRidAliasExample;
import com.palantir.conjure.verification.types.SetSafeLongAliasExample;
import com.palantir.conjure.verification.types.SetStringAliasExample;
import com.palantir.conjure.verification.types.SetStringExample;
import com.palantir.conjure.verification.types.SetUuidAliasExample;
import com.palantir.conjure.verification.types.SnakeCaseObjectExample;
import com.palantir.conjure.verification.types.StringAliasExample;
import com.palantir.conjure.verification.types.StringExample;
import com.palantir.conjure.verification.types.UuidAliasExample;
import com.palantir.conjure.verification.types.UuidExample;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.PathTemplateMatch;
import io.undertow.util.StatusCodes;
import java.io.IOException;
import java.util.Map;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceHandlerGenerator")
public final class AutoDeserializeServiceEndpoints implements Service {
    private final UndertowAutoDeserializeService delegate;

    private AutoDeserializeServiceEndpoints(UndertowAutoDeserializeService delegate) {
        this.delegate = delegate;
    }

    public static Service of(UndertowAutoDeserializeService delegate) {
        return new AutoDeserializeServiceEndpoints(delegate);
    }

    @Override
    public Registrable create(ServiceContext context) {
        return new AutoDeserializeServiceRegistrable(context, delegate);
    }

    private static final class AutoDeserializeServiceRegistrable implements Registrable {
        private final UndertowAutoDeserializeService delegate;

        private final SerializerRegistry serializers;

        private AutoDeserializeServiceRegistrable(
                ServiceContext context, UndertowAutoDeserializeService delegate) {
            this.serializers = context.serializerRegistry();
            this.delegate = delegate;
        }

        @Override
        public void register(EndpointRegistry endpointRegistry) {
            endpointRegistry
                    .add(
                            Endpoint.get(
                                    "/body/receiveBearerTokenExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveBearerTokenExample"),
                            new ReceiveBearerTokenExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveBooleanExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveBooleanExample"),
                            new ReceiveBooleanExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveDateTimeExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveDateTimeExample"),
                            new ReceiveDateTimeExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveDoubleExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveDoubleExample"),
                            new ReceiveDoubleExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveIntegerExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveIntegerExample"),
                            new ReceiveIntegerExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveRidExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveRidExample"),
                            new ReceiveRidExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveSafeLongExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveSafeLongExample"),
                            new ReceiveSafeLongExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveStringExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveStringExample"),
                            new ReceiveStringExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveUuidExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveUuidExample"),
                            new ReceiveUuidExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveAnyExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveAnyExample"),
                            new ReceiveAnyExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveEnumExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveEnumExample"),
                            new ReceiveEnumExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveListExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveListExample"),
                            new ReceiveListExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveSetStringExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveSetStringExample"),
                            new ReceiveSetStringExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveSetDoubleExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveSetDoubleExample"),
                            new ReceiveSetDoubleExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveMapExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveMapExample"),
                            new ReceiveMapExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveOptionalExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveOptionalExample"),
                            new ReceiveOptionalExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveOptionalBooleanExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveOptionalBooleanExample"),
                            new ReceiveOptionalBooleanExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveOptionalIntegerExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveOptionalIntegerExample"),
                            new ReceiveOptionalIntegerExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveLongFieldNameOptionalExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveLongFieldNameOptionalExample"),
                            new ReceiveLongFieldNameOptionalExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveRawOptionalExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveRawOptionalExample"),
                            new ReceiveRawOptionalExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveStringAliasExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveStringAliasExample"),
                            new ReceiveStringAliasExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveDoubleAliasExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveDoubleAliasExample"),
                            new ReceiveDoubleAliasExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveIntegerAliasExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveIntegerAliasExample"),
                            new ReceiveIntegerAliasExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveBooleanAliasExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveBooleanAliasExample"),
                            new ReceiveBooleanAliasExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveSafeLongAliasExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveSafeLongAliasExample"),
                            new ReceiveSafeLongAliasExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveRidAliasExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveRidAliasExample"),
                            new ReceiveRidAliasExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveBearerTokenAliasExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveBearerTokenAliasExample"),
                            new ReceiveBearerTokenAliasExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveUuidAliasExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveUuidAliasExample"),
                            new ReceiveUuidAliasExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveReferenceAliasExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveReferenceAliasExample"),
                            new ReceiveReferenceAliasExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveDateTimeAliasExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveDateTimeAliasExample"),
                            new ReceiveDateTimeAliasExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveKebabCaseObjectExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveKebabCaseObjectExample"),
                            new ReceiveKebabCaseObjectExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveSnakeCaseObjectExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveSnakeCaseObjectExample"),
                            new ReceiveSnakeCaseObjectExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveOptionalBearerTokenAliasExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveOptionalBearerTokenAliasExample"),
                            new ReceiveOptionalBearerTokenAliasExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveOptionalBinaryAliasExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveOptionalBinaryAliasExample"),
                            new ReceiveOptionalBinaryAliasExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveOptionalBooleanAliasExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveOptionalBooleanAliasExample"),
                            new ReceiveOptionalBooleanAliasExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveOptionalDateTimeAliasExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveOptionalDateTimeAliasExample"),
                            new ReceiveOptionalDateTimeAliasExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveOptionalDoubleAliasExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveOptionalDoubleAliasExample"),
                            new ReceiveOptionalDoubleAliasExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveOptionalIntegerAliasExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveOptionalIntegerAliasExample"),
                            new ReceiveOptionalIntegerAliasExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveOptionalRidAliasExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveOptionalRidAliasExample"),
                            new ReceiveOptionalRidAliasExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveOptionalSafeLongAliasExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveOptionalSafeLongAliasExample"),
                            new ReceiveOptionalSafeLongAliasExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveOptionalStringAliasExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveOptionalStringAliasExample"),
                            new ReceiveOptionalStringAliasExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveOptionalUuidAliasExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveOptionalUuidAliasExample"),
                            new ReceiveOptionalUuidAliasExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveOptionalAnyAliasExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveOptionalAnyAliasExample"),
                            new ReceiveOptionalAnyAliasExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveListBearerTokenAliasExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveListBearerTokenAliasExample"),
                            new ReceiveListBearerTokenAliasExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveListBinaryAliasExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveListBinaryAliasExample"),
                            new ReceiveListBinaryAliasExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveListBooleanAliasExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveListBooleanAliasExample"),
                            new ReceiveListBooleanAliasExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveListDateTimeAliasExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveListDateTimeAliasExample"),
                            new ReceiveListDateTimeAliasExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveListDoubleAliasExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveListDoubleAliasExample"),
                            new ReceiveListDoubleAliasExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveListIntegerAliasExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveListIntegerAliasExample"),
                            new ReceiveListIntegerAliasExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveListRidAliasExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveListRidAliasExample"),
                            new ReceiveListRidAliasExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveListSafeLongAliasExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveListSafeLongAliasExample"),
                            new ReceiveListSafeLongAliasExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveListStringAliasExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveListStringAliasExample"),
                            new ReceiveListStringAliasExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveListUuidAliasExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveListUuidAliasExample"),
                            new ReceiveListUuidAliasExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveListAnyAliasExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveListAnyAliasExample"),
                            new ReceiveListAnyAliasExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveSetBearerTokenAliasExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveSetBearerTokenAliasExample"),
                            new ReceiveSetBearerTokenAliasExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveSetBinaryAliasExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveSetBinaryAliasExample"),
                            new ReceiveSetBinaryAliasExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveSetBooleanAliasExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveSetBooleanAliasExample"),
                            new ReceiveSetBooleanAliasExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveSetDateTimeAliasExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveSetDateTimeAliasExample"),
                            new ReceiveSetDateTimeAliasExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveSetDoubleAliasExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveSetDoubleAliasExample"),
                            new ReceiveSetDoubleAliasExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveSetIntegerAliasExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveSetIntegerAliasExample"),
                            new ReceiveSetIntegerAliasExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveSetRidAliasExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveSetRidAliasExample"),
                            new ReceiveSetRidAliasExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveSetSafeLongAliasExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveSetSafeLongAliasExample"),
                            new ReceiveSetSafeLongAliasExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveSetStringAliasExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveSetStringAliasExample"),
                            new ReceiveSetStringAliasExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveSetUuidAliasExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveSetUuidAliasExample"),
                            new ReceiveSetUuidAliasExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveSetAnyAliasExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveSetAnyAliasExample"),
                            new ReceiveSetAnyAliasExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveMapBearerTokenAliasExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveMapBearerTokenAliasExample"),
                            new ReceiveMapBearerTokenAliasExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveMapBinaryAliasExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveMapBinaryAliasExample"),
                            new ReceiveMapBinaryAliasExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveMapBooleanAliasExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveMapBooleanAliasExample"),
                            new ReceiveMapBooleanAliasExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveMapDateTimeAliasExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveMapDateTimeAliasExample"),
                            new ReceiveMapDateTimeAliasExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveMapDoubleAliasExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveMapDoubleAliasExample"),
                            new ReceiveMapDoubleAliasExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveMapIntegerAliasExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveMapIntegerAliasExample"),
                            new ReceiveMapIntegerAliasExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveMapRidAliasExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveMapRidAliasExample"),
                            new ReceiveMapRidAliasExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveMapSafeLongAliasExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveMapSafeLongAliasExample"),
                            new ReceiveMapSafeLongAliasExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveMapStringAliasExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveMapStringAliasExample"),
                            new ReceiveMapStringAliasExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveMapUuidAliasExample/{index}",
                                    "AutoDeserializeService",
                                    "receiveMapUuidAliasExample"),
                            new ReceiveMapUuidAliasExampleHandler())
                    .add(
                            Endpoint.get(
                                    "/body/receiveMapEnumExampleAlias/{index}",
                                    "AutoDeserializeService",
                                    "receiveMapEnumExampleAlias"),
                            new ReceiveMapEnumExampleAliasHandler());
        }

        private class ReceiveBearerTokenExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                BearerTokenExample result = delegate.receiveBearerTokenExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveBooleanExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                BooleanExample result = delegate.receiveBooleanExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveDateTimeExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                DateTimeExample result = delegate.receiveDateTimeExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveDoubleExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                DoubleExample result = delegate.receiveDoubleExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveIntegerExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                IntegerExample result = delegate.receiveIntegerExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveRidExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                RidExample result = delegate.receiveRidExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveSafeLongExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                SafeLongExample result = delegate.receiveSafeLongExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveStringExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                StringExample result = delegate.receiveStringExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveUuidExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                UuidExample result = delegate.receiveUuidExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveAnyExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                AnyExample result = delegate.receiveAnyExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveEnumExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                EnumExample result = delegate.receiveEnumExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveListExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                ListExample result = delegate.receiveListExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveSetStringExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                SetStringExample result = delegate.receiveSetStringExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveSetDoubleExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                SetDoubleExample result = delegate.receiveSetDoubleExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveMapExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                MapExample result = delegate.receiveMapExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveOptionalExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                OptionalExample result = delegate.receiveOptionalExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveOptionalBooleanExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                OptionalBooleanExample result = delegate.receiveOptionalBooleanExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveOptionalIntegerExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                OptionalIntegerExample result = delegate.receiveOptionalIntegerExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveLongFieldNameOptionalExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                LongFieldNameOptionalExample result =
                        delegate.receiveLongFieldNameOptionalExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveRawOptionalExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                RawOptionalExample result = delegate.receiveRawOptionalExample(index);
                if (result.get().isPresent()) {
                    serializers.serialize(result, exchange);
                } else {
                    exchange.setStatusCode(StatusCodes.NO_CONTENT);
                }
            }
        }

        private class ReceiveStringAliasExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                StringAliasExample result = delegate.receiveStringAliasExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveDoubleAliasExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                DoubleAliasExample result = delegate.receiveDoubleAliasExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveIntegerAliasExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                IntegerAliasExample result = delegate.receiveIntegerAliasExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveBooleanAliasExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                BooleanAliasExample result = delegate.receiveBooleanAliasExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveSafeLongAliasExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                SafeLongAliasExample result = delegate.receiveSafeLongAliasExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveRidAliasExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                RidAliasExample result = delegate.receiveRidAliasExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveBearerTokenAliasExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                BearerTokenAliasExample result = delegate.receiveBearerTokenAliasExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveUuidAliasExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                UuidAliasExample result = delegate.receiveUuidAliasExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveReferenceAliasExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                ReferenceAliasExample result = delegate.receiveReferenceAliasExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveDateTimeAliasExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                DateTimeAliasExample result = delegate.receiveDateTimeAliasExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveKebabCaseObjectExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                KebabCaseObjectExample result = delegate.receiveKebabCaseObjectExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveSnakeCaseObjectExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                SnakeCaseObjectExample result = delegate.receiveSnakeCaseObjectExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveOptionalBearerTokenAliasExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                OptionalBearerTokenAliasExample result =
                        delegate.receiveOptionalBearerTokenAliasExample(index);
                if (result.get().isPresent()) {
                    serializers.serialize(result, exchange);
                } else {
                    exchange.setStatusCode(StatusCodes.NO_CONTENT);
                }
            }
        }

        private class ReceiveOptionalBinaryAliasExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                OptionalBinaryAliasExample result =
                        delegate.receiveOptionalBinaryAliasExample(index);
                if (result.get().isPresent()) {
                    serializers.serialize(result, exchange);
                } else {
                    exchange.setStatusCode(StatusCodes.NO_CONTENT);
                }
            }
        }

        private class ReceiveOptionalBooleanAliasExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                OptionalBooleanAliasExample result =
                        delegate.receiveOptionalBooleanAliasExample(index);
                if (result.get().isPresent()) {
                    serializers.serialize(result, exchange);
                } else {
                    exchange.setStatusCode(StatusCodes.NO_CONTENT);
                }
            }
        }

        private class ReceiveOptionalDateTimeAliasExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                OptionalDateTimeAliasExample result =
                        delegate.receiveOptionalDateTimeAliasExample(index);
                if (result.get().isPresent()) {
                    serializers.serialize(result, exchange);
                } else {
                    exchange.setStatusCode(StatusCodes.NO_CONTENT);
                }
            }
        }

        private class ReceiveOptionalDoubleAliasExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                OptionalDoubleAliasExample result =
                        delegate.receiveOptionalDoubleAliasExample(index);
                if (result.get().isPresent()) {
                    serializers.serialize(result, exchange);
                } else {
                    exchange.setStatusCode(StatusCodes.NO_CONTENT);
                }
            }
        }

        private class ReceiveOptionalIntegerAliasExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                OptionalIntegerAliasExample result =
                        delegate.receiveOptionalIntegerAliasExample(index);
                if (result.get().isPresent()) {
                    serializers.serialize(result, exchange);
                } else {
                    exchange.setStatusCode(StatusCodes.NO_CONTENT);
                }
            }
        }

        private class ReceiveOptionalRidAliasExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                OptionalRidAliasExample result = delegate.receiveOptionalRidAliasExample(index);
                if (result.get().isPresent()) {
                    serializers.serialize(result, exchange);
                } else {
                    exchange.setStatusCode(StatusCodes.NO_CONTENT);
                }
            }
        }

        private class ReceiveOptionalSafeLongAliasExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                OptionalSafeLongAliasExample result =
                        delegate.receiveOptionalSafeLongAliasExample(index);
                if (result.get().isPresent()) {
                    serializers.serialize(result, exchange);
                } else {
                    exchange.setStatusCode(StatusCodes.NO_CONTENT);
                }
            }
        }

        private class ReceiveOptionalStringAliasExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                OptionalStringAliasExample result =
                        delegate.receiveOptionalStringAliasExample(index);
                if (result.get().isPresent()) {
                    serializers.serialize(result, exchange);
                } else {
                    exchange.setStatusCode(StatusCodes.NO_CONTENT);
                }
            }
        }

        private class ReceiveOptionalUuidAliasExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                OptionalUuidAliasExample result = delegate.receiveOptionalUuidAliasExample(index);
                if (result.get().isPresent()) {
                    serializers.serialize(result, exchange);
                } else {
                    exchange.setStatusCode(StatusCodes.NO_CONTENT);
                }
            }
        }

        private class ReceiveOptionalAnyAliasExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                OptionalAnyAliasExample result = delegate.receiveOptionalAnyAliasExample(index);
                if (result.get().isPresent()) {
                    serializers.serialize(result, exchange);
                } else {
                    exchange.setStatusCode(StatusCodes.NO_CONTENT);
                }
            }
        }

        private class ReceiveListBearerTokenAliasExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                ListBearerTokenAliasExample result =
                        delegate.receiveListBearerTokenAliasExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveListBinaryAliasExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                ListBinaryAliasExample result = delegate.receiveListBinaryAliasExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveListBooleanAliasExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                ListBooleanAliasExample result = delegate.receiveListBooleanAliasExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveListDateTimeAliasExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                ListDateTimeAliasExample result = delegate.receiveListDateTimeAliasExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveListDoubleAliasExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                ListDoubleAliasExample result = delegate.receiveListDoubleAliasExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveListIntegerAliasExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                ListIntegerAliasExample result = delegate.receiveListIntegerAliasExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveListRidAliasExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                ListRidAliasExample result = delegate.receiveListRidAliasExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveListSafeLongAliasExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                ListSafeLongAliasExample result = delegate.receiveListSafeLongAliasExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveListStringAliasExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                ListStringAliasExample result = delegate.receiveListStringAliasExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveListUuidAliasExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                ListUuidAliasExample result = delegate.receiveListUuidAliasExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveListAnyAliasExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                ListAnyAliasExample result = delegate.receiveListAnyAliasExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveSetBearerTokenAliasExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                SetBearerTokenAliasExample result =
                        delegate.receiveSetBearerTokenAliasExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveSetBinaryAliasExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                SetBinaryAliasExample result = delegate.receiveSetBinaryAliasExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveSetBooleanAliasExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                SetBooleanAliasExample result = delegate.receiveSetBooleanAliasExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveSetDateTimeAliasExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                SetDateTimeAliasExample result = delegate.receiveSetDateTimeAliasExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveSetDoubleAliasExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                SetDoubleAliasExample result = delegate.receiveSetDoubleAliasExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveSetIntegerAliasExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                SetIntegerAliasExample result = delegate.receiveSetIntegerAliasExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveSetRidAliasExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                SetRidAliasExample result = delegate.receiveSetRidAliasExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveSetSafeLongAliasExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                SetSafeLongAliasExample result = delegate.receiveSetSafeLongAliasExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveSetStringAliasExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                SetStringAliasExample result = delegate.receiveSetStringAliasExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveSetUuidAliasExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                SetUuidAliasExample result = delegate.receiveSetUuidAliasExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveSetAnyAliasExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                SetAnyAliasExample result = delegate.receiveSetAnyAliasExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveMapBearerTokenAliasExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                MapBearerTokenAliasExample result =
                        delegate.receiveMapBearerTokenAliasExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveMapBinaryAliasExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                MapBinaryAliasExample result = delegate.receiveMapBinaryAliasExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveMapBooleanAliasExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                MapBooleanAliasExample result = delegate.receiveMapBooleanAliasExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveMapDateTimeAliasExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                MapDateTimeAliasExample result = delegate.receiveMapDateTimeAliasExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveMapDoubleAliasExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                MapDoubleAliasExample result = delegate.receiveMapDoubleAliasExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveMapIntegerAliasExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                MapIntegerAliasExample result = delegate.receiveMapIntegerAliasExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveMapRidAliasExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                MapRidAliasExample result = delegate.receiveMapRidAliasExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveMapSafeLongAliasExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                MapSafeLongAliasExample result = delegate.receiveMapSafeLongAliasExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveMapStringAliasExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                MapStringAliasExample result = delegate.receiveMapStringAliasExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveMapUuidAliasExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                MapUuidAliasExample result = delegate.receiveMapUuidAliasExample(index);
                serializers.serialize(result, exchange);
            }
        }

        private class ReceiveMapEnumExampleAliasHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                MapEnumExampleAlias result = delegate.receiveMapEnumExampleAlias(index);
                serializers.serialize(result, exchange);
            }
        }
    }
}
