package com.palantir.product;

import com.palantir.conjure.java.lib.internal.ConjureClientEndpoint;
import java.util.Optional;
import java.util.OptionalInt;
import javax.annotation.Generated;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.StreamingOutput;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/")
@Generated("com.palantir.conjure.java.services.JerseyServiceGenerator")
public interface AsyncRequestProcessingTestService {
    @GET
    @Path("async/delay")
    @ConjureClientEndpoint(path = "/async/delay", method = "GET")
    String delay(@QueryParam("delayMillis") OptionalInt delayMillis);

    @GET
    @Path("async/throws")
    @ConjureClientEndpoint(path = "/async/throws", method = "GET")
    void throwsInHandler();

    @GET
    @Path("async/failed-future")
    @ConjureClientEndpoint(path = "/async/failed-future", method = "GET")
    void failedFuture(@QueryParam("delayMillis") OptionalInt delayMillis);

    @GET
    @Path("async/binary")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @ConjureClientEndpoint(path = "/async/binary", method = "GET")
    Optional<StreamingOutput> binary(@QueryParam("stringValue") Optional<String> stringValue);

    @GET
    @Path("async/future-trace")
    @ConjureClientEndpoint(path = "/async/future-trace", method = "GET")
    Object futureTraceId(@QueryParam("delayMillis") OptionalInt delayMillis);

    @Deprecated
    @ConjureClientEndpoint(path = "/async/delay", method = "GET")
    default String delay() {
        return delay(OptionalInt.empty());
    }

    @Deprecated
    @ConjureClientEndpoint(path = "/async/failed-future", method = "GET")
    default void failedFuture() {
        failedFuture(OptionalInt.empty());
    }

    @Deprecated
    @ConjureClientEndpoint(path = "/async/binary", method = "GET")
    default Optional<StreamingOutput> binary() {
        return binary(Optional.empty());
    }

    @Deprecated
    @ConjureClientEndpoint(path = "/async/future-trace", method = "GET")
    default Object futureTraceId() {
        return futureTraceId(OptionalInt.empty());
    }
}
