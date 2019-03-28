package com.palantir.product;

import com.palantir.tokens.auth.AuthHeader;
import java.io.InputStream;
import java.util.Optional;
import javax.annotation.Generated;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/")
@Generated("com.palantir.conjure.java.services.JaxRsClientGenerator")
public interface EteBinaryServiceJaxRsClient {
    @POST
    @Path("binary")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    InputStream postBinary(@HeaderParam("Authorization") AuthHeader authHeader, InputStream body);

    @GET
    @Path("binary/optional/present")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    Optional<InputStream> getOptionalBinaryPresent(
            @HeaderParam("Authorization") AuthHeader authHeader);

    @GET
    @Path("binary/optional/empty")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    Optional<InputStream> getOptionalBinaryEmpty(
            @HeaderParam("Authorization") AuthHeader authHeader);

    /** Throws an exception after partially writing a binary response. */
    @GET
    @Path("binary/failure")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    InputStream getBinaryFailure(
            @HeaderParam("Authorization") AuthHeader authHeader,
            @QueryParam("numBytes") int numBytes);
}
