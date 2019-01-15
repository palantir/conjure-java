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
import io.undertow.util.Methods;
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
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveBearerTokenExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveBearerTokenExample")
                                    .build(),
                            new ReceiveBearerTokenExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveBooleanExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveBooleanExample")
                                    .build(),
                            new ReceiveBooleanExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveDateTimeExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveDateTimeExample")
                                    .build(),
                            new ReceiveDateTimeExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveDoubleExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveDoubleExample")
                                    .build(),
                            new ReceiveDoubleExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveIntegerExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveIntegerExample")
                                    .build(),
                            new ReceiveIntegerExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveRidExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveRidExample")
                                    .build(),
                            new ReceiveRidExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveSafeLongExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveSafeLongExample")
                                    .build(),
                            new ReceiveSafeLongExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveStringExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveStringExample")
                                    .build(),
                            new ReceiveStringExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveUuidExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveUuidExample")
                                    .build(),
                            new ReceiveUuidExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveAnyExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveAnyExample")
                                    .build(),
                            new ReceiveAnyExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveEnumExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveEnumExample")
                                    .build(),
                            new ReceiveEnumExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveListExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveListExample")
                                    .build(),
                            new ReceiveListExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveSetStringExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveSetStringExample")
                                    .build(),
                            new ReceiveSetStringExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveSetDoubleExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveSetDoubleExample")
                                    .build(),
                            new ReceiveSetDoubleExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveMapExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveMapExample")
                                    .build(),
                            new ReceiveMapExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveOptionalExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveOptionalExample")
                                    .build(),
                            new ReceiveOptionalExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveOptionalBooleanExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveOptionalBooleanExample")
                                    .build(),
                            new ReceiveOptionalBooleanExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveOptionalIntegerExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveOptionalIntegerExample")
                                    .build(),
                            new ReceiveOptionalIntegerExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveLongFieldNameOptionalExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveLongFieldNameOptionalExample")
                                    .build(),
                            new ReceiveLongFieldNameOptionalExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveRawOptionalExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveRawOptionalExample")
                                    .build(),
                            new ReceiveRawOptionalExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveStringAliasExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveStringAliasExample")
                                    .build(),
                            new ReceiveStringAliasExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveDoubleAliasExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveDoubleAliasExample")
                                    .build(),
                            new ReceiveDoubleAliasExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveIntegerAliasExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveIntegerAliasExample")
                                    .build(),
                            new ReceiveIntegerAliasExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveBooleanAliasExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveBooleanAliasExample")
                                    .build(),
                            new ReceiveBooleanAliasExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveSafeLongAliasExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveSafeLongAliasExample")
                                    .build(),
                            new ReceiveSafeLongAliasExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveRidAliasExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveRidAliasExample")
                                    .build(),
                            new ReceiveRidAliasExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveBearerTokenAliasExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveBearerTokenAliasExample")
                                    .build(),
                            new ReceiveBearerTokenAliasExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveUuidAliasExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveUuidAliasExample")
                                    .build(),
                            new ReceiveUuidAliasExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveReferenceAliasExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveReferenceAliasExample")
                                    .build(),
                            new ReceiveReferenceAliasExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveDateTimeAliasExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveDateTimeAliasExample")
                                    .build(),
                            new ReceiveDateTimeAliasExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveKebabCaseObjectExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveKebabCaseObjectExample")
                                    .build(),
                            new ReceiveKebabCaseObjectExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveSnakeCaseObjectExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveSnakeCaseObjectExample")
                                    .build(),
                            new ReceiveSnakeCaseObjectExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template(
                                            "/body/receiveOptionalBearerTokenAliasExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveOptionalBearerTokenAliasExample")
                                    .build(),
                            new ReceiveOptionalBearerTokenAliasExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveOptionalBinaryAliasExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveOptionalBinaryAliasExample")
                                    .build(),
                            new ReceiveOptionalBinaryAliasExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveOptionalBooleanAliasExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveOptionalBooleanAliasExample")
                                    .build(),
                            new ReceiveOptionalBooleanAliasExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveOptionalDateTimeAliasExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveOptionalDateTimeAliasExample")
                                    .build(),
                            new ReceiveOptionalDateTimeAliasExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveOptionalDoubleAliasExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveOptionalDoubleAliasExample")
                                    .build(),
                            new ReceiveOptionalDoubleAliasExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveOptionalIntegerAliasExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveOptionalIntegerAliasExample")
                                    .build(),
                            new ReceiveOptionalIntegerAliasExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveOptionalRidAliasExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveOptionalRidAliasExample")
                                    .build(),
                            new ReceiveOptionalRidAliasExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveOptionalSafeLongAliasExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveOptionalSafeLongAliasExample")
                                    .build(),
                            new ReceiveOptionalSafeLongAliasExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveOptionalStringAliasExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveOptionalStringAliasExample")
                                    .build(),
                            new ReceiveOptionalStringAliasExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveOptionalUuidAliasExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveOptionalUuidAliasExample")
                                    .build(),
                            new ReceiveOptionalUuidAliasExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveOptionalAnyAliasExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveOptionalAnyAliasExample")
                                    .build(),
                            new ReceiveOptionalAnyAliasExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveListBearerTokenAliasExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveListBearerTokenAliasExample")
                                    .build(),
                            new ReceiveListBearerTokenAliasExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveListBinaryAliasExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveListBinaryAliasExample")
                                    .build(),
                            new ReceiveListBinaryAliasExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveListBooleanAliasExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveListBooleanAliasExample")
                                    .build(),
                            new ReceiveListBooleanAliasExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveListDateTimeAliasExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveListDateTimeAliasExample")
                                    .build(),
                            new ReceiveListDateTimeAliasExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveListDoubleAliasExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveListDoubleAliasExample")
                                    .build(),
                            new ReceiveListDoubleAliasExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveListIntegerAliasExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveListIntegerAliasExample")
                                    .build(),
                            new ReceiveListIntegerAliasExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveListRidAliasExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveListRidAliasExample")
                                    .build(),
                            new ReceiveListRidAliasExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveListSafeLongAliasExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveListSafeLongAliasExample")
                                    .build(),
                            new ReceiveListSafeLongAliasExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveListStringAliasExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveListStringAliasExample")
                                    .build(),
                            new ReceiveListStringAliasExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveListUuidAliasExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveListUuidAliasExample")
                                    .build(),
                            new ReceiveListUuidAliasExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveListAnyAliasExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveListAnyAliasExample")
                                    .build(),
                            new ReceiveListAnyAliasExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveSetBearerTokenAliasExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveSetBearerTokenAliasExample")
                                    .build(),
                            new ReceiveSetBearerTokenAliasExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveSetBinaryAliasExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveSetBinaryAliasExample")
                                    .build(),
                            new ReceiveSetBinaryAliasExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveSetBooleanAliasExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveSetBooleanAliasExample")
                                    .build(),
                            new ReceiveSetBooleanAliasExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveSetDateTimeAliasExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveSetDateTimeAliasExample")
                                    .build(),
                            new ReceiveSetDateTimeAliasExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveSetDoubleAliasExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveSetDoubleAliasExample")
                                    .build(),
                            new ReceiveSetDoubleAliasExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveSetIntegerAliasExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveSetIntegerAliasExample")
                                    .build(),
                            new ReceiveSetIntegerAliasExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveSetRidAliasExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveSetRidAliasExample")
                                    .build(),
                            new ReceiveSetRidAliasExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveSetSafeLongAliasExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveSetSafeLongAliasExample")
                                    .build(),
                            new ReceiveSetSafeLongAliasExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveSetStringAliasExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveSetStringAliasExample")
                                    .build(),
                            new ReceiveSetStringAliasExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveSetUuidAliasExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveSetUuidAliasExample")
                                    .build(),
                            new ReceiveSetUuidAliasExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveSetAnyAliasExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveSetAnyAliasExample")
                                    .build(),
                            new ReceiveSetAnyAliasExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveMapBearerTokenAliasExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveMapBearerTokenAliasExample")
                                    .build(),
                            new ReceiveMapBearerTokenAliasExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveMapBinaryAliasExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveMapBinaryAliasExample")
                                    .build(),
                            new ReceiveMapBinaryAliasExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveMapBooleanAliasExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveMapBooleanAliasExample")
                                    .build(),
                            new ReceiveMapBooleanAliasExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveMapDateTimeAliasExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveMapDateTimeAliasExample")
                                    .build(),
                            new ReceiveMapDateTimeAliasExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveMapDoubleAliasExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveMapDoubleAliasExample")
                                    .build(),
                            new ReceiveMapDoubleAliasExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveMapIntegerAliasExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveMapIntegerAliasExample")
                                    .build(),
                            new ReceiveMapIntegerAliasExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveMapRidAliasExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveMapRidAliasExample")
                                    .build(),
                            new ReceiveMapRidAliasExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveMapSafeLongAliasExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveMapSafeLongAliasExample")
                                    .build(),
                            new ReceiveMapSafeLongAliasExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveMapStringAliasExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveMapStringAliasExample")
                                    .build(),
                            new ReceiveMapStringAliasExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveMapUuidAliasExample/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveMapUuidAliasExample")
                                    .build(),
                            new ReceiveMapUuidAliasExampleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/body/receiveMapEnumExampleAlias/{index}")
                                    .serviceName("AutoDeserializeService")
                                    .name("receiveMapEnumExampleAlias")
                                    .build(),
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
