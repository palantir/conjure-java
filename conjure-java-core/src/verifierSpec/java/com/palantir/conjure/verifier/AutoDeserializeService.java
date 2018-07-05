package com.palantir.conjure.verifier;

import javax.annotation.Generated;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/")
@Generated("com.palantir.conjure.java.services.JerseyServiceGenerator")
public interface AutoDeserializeService {
    @GET
    @Path("receiveBearerTokenExample/{index}")
    BearerTokenExample receiveBearerTokenExample(@PathParam("index") int index);

    @GET
    @Path("receiveBinaryExample/{index}")
    BinaryExample receiveBinaryExample(@PathParam("index") int index);

    @GET
    @Path("receiveBooleanExample/{index}")
    BooleanExample receiveBooleanExample(@PathParam("index") int index);

    @GET
    @Path("receiveDateTimeExample/{index}")
    DateTimeExample receiveDateTimeExample(@PathParam("index") int index);

    @GET
    @Path("receiveDoubleExample/{index}")
    DoubleExample receiveDoubleExample(@PathParam("index") int index);

    @GET
    @Path("receiveIntegerExample/{index}")
    IntegerExample receiveIntegerExample(@PathParam("index") int index);

    @GET
    @Path("receiveRidExample/{index}")
    RidExample receiveRidExample(@PathParam("index") int index);

    @GET
    @Path("receiveSafeLongExample/{index}")
    SafeLongExample receiveSafeLongExample(@PathParam("index") int index);

    @GET
    @Path("receiveStringExample/{index}")
    StringExample receiveStringExample(@PathParam("index") int index);

    @GET
    @Path("receiveUuidExample/{index}")
    UuidExample receiveUuidExample(@PathParam("index") int index);

    @GET
    @Path("receiveAnyExample/{index}")
    AnyExample receiveAnyExample(@PathParam("index") int index);

    @GET
    @Path("receiveListExample/{index}")
    ListExample receiveListExample(@PathParam("index") int index);

    @GET
    @Path("receiveSetExample/{index}")
    SetExample receiveSetExample(@PathParam("index") int index);

    @GET
    @Path("receiveMapExample/{index}")
    MapExample receiveMapExample(@PathParam("index") int index);

    @GET
    @Path("receiveOptionalExample/{index}")
    OptionalExample receiveOptionalExample(@PathParam("index") int index);

    @GET
    @Path("receiveStringAliasExample/{index}")
    StringAliasExample receiveStringAliasExample(@PathParam("index") int index);

    @GET
    @Path("receiveDoubleAliasExample/{index}")
    DoubleAliasExample receiveDoubleAliasExample(@PathParam("index") int index);

    @GET
    @Path("receiveIntegerAliasExample/{index}")
    IntegerAliasExample receiveIntegerAliasExample(@PathParam("index") int index);

    @GET
    @Path("receiveBooleanAliasExample/{index}")
    BooleanAliasExample receiveBooleanAliasExample(@PathParam("index") int index);

    @GET
    @Path("receiveSafeLongAliasExample/{index}")
    SafeLongAliasExample receiveSafeLongAliasExample(@PathParam("index") int index);

    @GET
    @Path("receiveRidAliasExample/{index}")
    RidAliasExample receiveRidAliasExample(@PathParam("index") int index);

    @GET
    @Path("receiveBearerTokenAliasExample/{index}")
    BearerTokenAliasExample receiveBearerTokenAliasExample(@PathParam("index") int index);

    @GET
    @Path("receiveUuidAliasExample/{index}")
    UuidAliasExample receiveUuidAliasExample(@PathParam("index") int index);

    @GET
    @Path("receiveReferenceAliasExample/{index}")
    ReferenceAliasExample receiveReferenceAliasExample(@PathParam("index") int index);

    @GET
    @Path("receiveDateTimeAliasExample/{index}")
    DateTimeAliasExample receiveDateTimeAliasExample(@PathParam("index") int index);

    @GET
    @Path("receiveBinaryAliasExample/{index}")
    BinaryAliasExample receiveBinaryAliasExample(@PathParam("index") int index);

    @GET
    @Path("recieveEnumExample/{index}")
    EnumExample recieveEnumExample(@PathParam("index") int index);

    @GET
    @Path("recieveEnumFieldExample/{index}")
    EnumFieldExample recieveEnumFieldExample(@PathParam("index") int index);

    @GET
    @Path("recieveUnionExample/{index}")
    UnionExample recieveUnionExample(@PathParam("index") int index);
}
