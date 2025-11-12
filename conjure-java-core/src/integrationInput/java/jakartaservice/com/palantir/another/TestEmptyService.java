package jakartaservice.com.palantir.another;

import com.palantir.conjure.java.lib.internal.ConjureGenerated;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/** This service has no endpoints. */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/")
@ConjureGenerated("com.palantir.conjure.java.services.JerseyServiceGenerator")
public interface TestEmptyService {}
