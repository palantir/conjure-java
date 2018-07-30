package com.palantir.product;

import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.ri.ResourceIdentifier;
import com.palantir.tokens.auth.AuthHeader;
import com.palantir.tokens.auth.BearerToken;
import java.lang.String;
import java.time.OffsetDateTime;
import java.util.Optional;
import javax.annotation.Generated;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.StreamingOutput;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/")
@Generated("com.palantir.conjure.java.services.JerseyServiceGenerator")
public interface EteService {
    @GET
    @Path("base/string")
    String string(@HeaderParam("Authorization") @NotNull AuthHeader authHeader);

    @GET
    @Path("base/integer")
    int integer(@HeaderParam("Authorization") @NotNull AuthHeader authHeader);

    @GET
    @Path("base/double")
    double double_(@HeaderParam("Authorization") @NotNull AuthHeader authHeader);

    @GET
    @Path("base/boolean")
    boolean boolean_(@HeaderParam("Authorization") @NotNull AuthHeader authHeader);

    @GET
    @Path("base/safelong")
    SafeLong safelong(@HeaderParam("Authorization") @NotNull AuthHeader authHeader);

    @GET
    @Path("base/rid")
    ResourceIdentifier rid(@HeaderParam("Authorization") @NotNull AuthHeader authHeader);

    @GET
    @Path("base/bearertoken")
    BearerToken bearertoken(@HeaderParam("Authorization") @NotNull AuthHeader authHeader);

    @GET
    @Path("base/optionalString")
    Optional<String> optionalString(@HeaderParam("Authorization") @NotNull AuthHeader authHeader);

    @GET
    @Path("base/optionalEmpty")
    Optional<String> optionalEmpty(@HeaderParam("Authorization") @NotNull AuthHeader authHeader);

    @GET
    @Path("base/datetime")
    OffsetDateTime datetime(@HeaderParam("Authorization") @NotNull AuthHeader authHeader);

    @GET
    @Path("base/binary")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    StreamingOutput binary(@HeaderParam("Authorization") @NotNull AuthHeader authHeader);

    @POST
    @Path("base/notNullBody")
    StringAliasExample notNullBody(
            @HeaderParam("Authorization") @NotNull AuthHeader authHeader,
            @NotNull StringAliasExample notNullBody);
}
