package asyncrequest.com.palantir.product;

import com.palantir.conjure.java.lib.internal.ClientEndpoint;
import com.palantir.conjure.java.lib.internal.ConjureGenerated;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.StreamingOutput;
import java.util.Optional;
import java.util.OptionalInt;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/")
@ConjureGenerated("com.palantir.conjure.java.services.JerseyServiceGenerator")
public interface AsyncRequestProcessingTestService {
    @GET
    @Path("async/delay")
    @ClientEndpoint(method = "GET", path = "/async/delay")
    String delay(@QueryParam("delayMillis") OptionalInt delayMillis);

    @GET
    @Path("async/delay-5s-timeout")
    @ClientEndpoint(method = "GET", path = "/async/delay-5s-timeout")
    String delayFiveSecondTimeout(@QueryParam("delayMillis") OptionalInt delayMillis);

    @GET
    @Path("async/throws")
    @ClientEndpoint(method = "GET", path = "/async/throws")
    void throwsInHandler();

    @GET
    @Path("async/failed-future")
    @ClientEndpoint(method = "GET", path = "/async/failed-future")
    void failedFuture(@QueryParam("delayMillis") OptionalInt delayMillis);

    @GET
    @Path("async/binary")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @ClientEndpoint(method = "GET", path = "/async/binary")
    Optional<StreamingOutput> binary(@QueryParam("stringValue") Optional<String> stringValue);

    @GET
    @Path("async/future-trace")
    @ClientEndpoint(method = "GET", path = "/async/future-trace")
    Object futureTraceId(@QueryParam("delayMillis") OptionalInt delayMillis);

    @Deprecated
    @ClientEndpoint(method = "GET", path = "/async/delay")
    default String delay() {
        return delay(OptionalInt.empty());
    }

    @Deprecated
    @ClientEndpoint(method = "GET", path = "/async/delay-5s-timeout")
    default String delayFiveSecondTimeout() {
        return delayFiveSecondTimeout(OptionalInt.empty());
    }

    @Deprecated
    @ClientEndpoint(method = "GET", path = "/async/failed-future")
    default void failedFuture() {
        failedFuture(OptionalInt.empty());
    }

    @Deprecated
    @ClientEndpoint(method = "GET", path = "/async/binary")
    default Optional<StreamingOutput> binary() {
        return binary(Optional.empty());
    }

    @Deprecated
    @ClientEndpoint(method = "GET", path = "/async/future-trace")
    default Object futureTraceId() {
        return futureTraceId(OptionalInt.empty());
    }
}
