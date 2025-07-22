package jerseybinary.test.api;

import com.palantir.conjure.java.lib.internal.ClientEndpoint;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.StreamingOutput;
import javax.annotation.processing.Generated;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/")
@Generated("com.palantir.conjure.java.services.JerseyServiceGenerator")
public interface TestService {
    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @ClientEndpoint(method = "GET", path = "/")
    StreamingOutput getBinary();
}
