package com.palantir.conjure.verifier;

import com.palantir.tokens.auth.AuthHeader;
import java.io.InputStream;
import java.lang.Deprecated;
import java.lang.String;
import java.util.List;
import java.util.Optional;
import javax.annotation.Generated;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.StreamingOutput;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/")
@Generated("com.palantir.conjure.java.services.JerseyServiceGenerator")
public interface TestService {
    @GET
    @Path("getString")
    String getString();

    @GET
    @Path("getStringAuth")
    String getStringAuth(@HeaderParam("Authorization") AuthHeader authHeader);

    @GET
    @Path("echoHeaderParam")
    String echoHeaderParam(@HeaderParam("Some-Header") String someHeader);

    @GET
    @Path("echoPathParam/{content}")
    String echoPathParam(@PathParam("content") String content);

    @GET
    @Path("echoQueryParam")
    Optional<String> echoQueryParam(@QueryParam("query") String query);

    @POST
    @Path("echoOptionalQueryParam")
    void echoOptionalQueryParam(
            @QueryParam("query") Optional<String> query, Optional<String> returns);

    @POST
    @Path("simpleBody")
    PrimitivesObject simpleBody(SimpleUnion body);

    @GET
    @Path("getPrimitivesObject")
    PrimitivesObject getPrimitivesObject();

    @GET
    @Path("getOptionalObject")
    OptionalObject getOptionalObject();

    @GET
    @Path("getUnions")
    List<SimpleUnion> getUnions();

    @GET
    @Path("getEnums")
    List<SimpleEnums> getEnums();

    @GET
    @Path("getAlias")
    StringAlias getAlias();

    @GET
    @Path("getBinary")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    StreamingOutput getBinary();

    @POST
    @Path("sendBinary")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    void sendBinary(InputStream body);

    @GET
    @Path("extraFields")
    PrimitivesObject getExtraFields();

    @POST
    @Path("echoBinaryAlias")
    BinaryAlias echoBinaryAlias(BinaryAlias body);

//    @Deprecated
//    default void echoOptionalQueryParam(Optional<String> returns) {
//        return echoOptionalQueryParam(Optional.empty(), returns);
//    }
}
