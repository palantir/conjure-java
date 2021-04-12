package com.palantir.product;

import com.palantir.conjure.java.lib.internal.ClientEndpoint;
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
public interface EmptyPathService {
    @GET
    @ClientEndpoint(method = "GET", path = "/")
    boolean emptyPath();
}
