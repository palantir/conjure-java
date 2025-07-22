package jersey.test.api;

import com.palantir.conjure.java.lib.internal.ClientEndpoint;
import com.palantir.tokens.auth.BearerToken;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import javax.annotation.processing.Generated;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/")
@Generated("com.palantir.conjure.java.services.JerseyServiceGenerator")
public interface CookieService {
    @GET
    @Path("cookies")
    @ClientEndpoint(method = "GET", path = "/cookies")
    void eatCookies(@CookieParam("PALANTIR_TOKEN") @NotNull BearerToken token);
}
