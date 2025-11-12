package jersey.com.palantir.product;

import com.palantir.conjure.java.lib.internal.ClientEndpoint;
import com.palantir.conjure.java.lib.internal.ConjureGenerated;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/")
@ConjureGenerated("com.palantir.conjure.java.services.JerseyServiceGenerator")
public interface EmptyPathService {
    @GET
    @ClientEndpoint(method = "GET", path = "/")
    boolean emptyPath();
}
