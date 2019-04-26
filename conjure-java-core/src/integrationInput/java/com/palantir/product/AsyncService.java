package com.palantir.product;

import com.palantir.tokens.auth.AuthHeader;
import java.util.Optional;
import javax.annotation.Generated;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.StreamingOutput;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/")
@Generated("com.palantir.conjure.java.services.JerseyServiceGenerator")
public interface AsyncService {
    @GET
    @Path("base/async/async/binary")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    StreamingOutput binaryAsync(@HeaderParam("Authorization") @NotNull AuthHeader authHeader);

    @GET
    @Path("base/async/async/optional/binary")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    Optional<StreamingOutput> binaryOptionalAsync(
            @HeaderParam("Authorization") @NotNull AuthHeader authHeader);

    @GET
    @Path("base/async/async/no-return")
    void noReturnAsync(@HeaderParam("Authorization") @NotNull AuthHeader authHeader);

    @GET
    @Path("base/async/async/no-return/throwing")
    void noReturnThrowingAsync(@HeaderParam("Authorization") @NotNull AuthHeader authHeader);

    @GET
    @Path("base/async/async/no-return/failed")
    void noReturnFailedFutureAsync(@HeaderParam("Authorization") @NotNull AuthHeader authHeader);

    @GET
    @Path("base/async/enum/optional/query")
    Optional<String> optionalEnumQueryAsync(
            @HeaderParam("Authorization") @NotNull AuthHeader authHeader,
            @QueryParam("queryParamName") Optional<String> queryParamName);

    @Deprecated
    default Optional<String> optionalEnumQueryAsync(AuthHeader authHeader) {
        return optionalEnumQueryAsync(authHeader, Optional.empty());
    }
}
