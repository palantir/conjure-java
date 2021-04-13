package com.palantir.product;

import com.palantir.conjure.java.lib.internal.ClientEndpoint;
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
    @ClientEndpoint(method = "GET", path = "/async/delay")
    String delay(@QueryParam("delayMillis") OptionalInt delayMillis);

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
