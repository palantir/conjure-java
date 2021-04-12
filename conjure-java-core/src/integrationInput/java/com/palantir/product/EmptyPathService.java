package com.palantir.product;

import com.palantir.conjure.java.lib.internal.ConjureClientEndpoint;
import com.palantir.conjure.java.lib.internal.ConjureClientService;
import javax.annotation.Generated;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/")
@Generated("com.palantir.conjure.java.services.JerseyServiceGenerator")
@ConjureClientService(name = "EmptyPathService", package_ = "com.palantir.product")
public interface EmptyPathService {
    @GET
    @ConjureClientEndpoint(path = "/", method = "GET")
    boolean emptyPath();
}
