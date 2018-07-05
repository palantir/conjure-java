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
}
