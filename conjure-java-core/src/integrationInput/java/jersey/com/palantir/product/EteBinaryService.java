package jersey.com.palantir.product;

import com.palantir.conjure.java.lib.internal.ClientEndpoint;
import com.palantir.conjure.java.lib.internal.ConjureGenerated;
import com.palantir.tokens.auth.AuthHeader;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.StreamingOutput;
import java.io.InputStream;
import java.util.Optional;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/")
@ConjureGenerated("com.palantir.conjure.java.services.JerseyServiceGenerator")
public interface EteBinaryService {
    @POST
    @Path("binary")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @ClientEndpoint(method = "POST", path = "/binary")
    StreamingOutput postBinary(@HeaderParam("Authorization") @NotNull AuthHeader authHeader, @NotNull InputStream body);

    @POST
    @Path("binary/throws")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @ClientEndpoint(method = "POST", path = "/binary/throws")
    StreamingOutput postBinaryThrows(
            @HeaderParam("Authorization") @NotNull AuthHeader authHeader,
            @QueryParam("bytesToRead") int bytesToRead,
            @NotNull InputStream body);

    @GET
    @Path("binary/optional/present")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @ClientEndpoint(method = "GET", path = "/binary/optional/present")
    Optional<StreamingOutput> getOptionalBinaryPresent(@HeaderParam("Authorization") @NotNull AuthHeader authHeader);

    @GET
    @Path("binary/optional/empty")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @ClientEndpoint(method = "GET", path = "/binary/optional/empty")
    Optional<StreamingOutput> getOptionalBinaryEmpty(@HeaderParam("Authorization") @NotNull AuthHeader authHeader);

    /** Throws an exception after partially writing a binary response. */
    @GET
    @Path("binary/failure")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @ClientEndpoint(method = "GET", path = "/binary/failure")
    StreamingOutput getBinaryFailure(
            @HeaderParam("Authorization") @NotNull AuthHeader authHeader,
            @QueryParam("numBytes") int numBytes,
            @QueryParam("useTryWithResources") boolean useTryWithResources);

    @GET
    @Path("binary/aliased")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @ClientEndpoint(method = "GET", path = "/binary/aliased")
    Optional<StreamingOutput> getAliased(@HeaderParam("Authorization") @NotNull AuthHeader authHeader);
}
