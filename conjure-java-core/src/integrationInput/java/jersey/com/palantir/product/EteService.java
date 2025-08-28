package jersey.com.palantir.product;

import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.conjure.java.lib.internal.ClientEndpoint;
import com.palantir.ri.ResourceIdentifier;
import com.palantir.tokens.auth.AuthHeader;
import com.palantir.tokens.auth.BearerToken;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.StreamingOutput;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.annotation.processing.Generated;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/")
@Generated("com.palantir.conjure.java.services.JerseyServiceGenerator")
public interface EteService {
    /**
     * foo bar baz.
     *
     * <h2>Very Important Documentation</h2>
     *
     * <p>This documentation provides a <em>list</em>:
     *
     * <ul>
     *   <li>Docs rule
     *   <li>Lists are wonderful
     * </ul>
     */
    @GET
    @Path("base/string")
    @ClientEndpoint(method = "GET", path = "/base/string")
    String string(@HeaderParam("Authorization") @NotNull AuthHeader authHeader);

    /** one <em>two</em> three. */
    @GET
    @Path("base/integer")
    @ClientEndpoint(method = "GET", path = "/base/integer")
    int integer(@HeaderParam("Authorization") @NotNull AuthHeader authHeader);

    @GET
    @Path("base/double")
    @ClientEndpoint(method = "GET", path = "/base/double")
    double double_(@HeaderParam("Authorization") @NotNull AuthHeader authHeader);

    @GET
    @Path("base/boolean")
    @ClientEndpoint(method = "GET", path = "/base/boolean")
    boolean boolean_(@HeaderParam("Authorization") @NotNull AuthHeader authHeader);

    @GET
    @Path("base/safelong")
    @ClientEndpoint(method = "GET", path = "/base/safelong")
    SafeLong safelong(@HeaderParam("Authorization") @NotNull AuthHeader authHeader);

    @GET
    @Path("base/rid")
    @ClientEndpoint(method = "GET", path = "/base/rid")
    ResourceIdentifier rid(@HeaderParam("Authorization") @NotNull AuthHeader authHeader);

    @GET
    @Path("base/bearertoken")
    @ClientEndpoint(method = "GET", path = "/base/bearertoken")
    BearerToken bearertoken(@HeaderParam("Authorization") @NotNull AuthHeader authHeader);

    @GET
    @Path("base/optionalString")
    @ClientEndpoint(method = "GET", path = "/base/optionalString")
    Optional<String> optionalString(@HeaderParam("Authorization") @NotNull AuthHeader authHeader);

    @GET
    @Path("base/optionalEmpty")
    @ClientEndpoint(method = "GET", path = "/base/optionalEmpty")
    Optional<String> optionalEmpty(@HeaderParam("Authorization") @NotNull AuthHeader authHeader);

    @GET
    @Path("base/datetime")
    @ClientEndpoint(method = "GET", path = "/base/datetime")
    OffsetDateTime datetime(@HeaderParam("Authorization") @NotNull AuthHeader authHeader);

    @GET
    @Path("base/binary")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @ClientEndpoint(method = "GET", path = "/base/binary")
    StreamingOutput binary(@HeaderParam("Authorization") @NotNull AuthHeader authHeader);

    /**
     * Path endpoint.
     *
     * @param param Documentation for <code>param</code>
     */
    @GET
    @Path("base/path/{param}")
    @ClientEndpoint(method = "GET", path = "/base/path/{param}")
    String path(@HeaderParam("Authorization") @NotNull AuthHeader authHeader, @PathParam("param") String param);

    @GET
    @Path("base/externalLong/{param}")
    @ClientEndpoint(method = "GET", path = "/base/externalLong/{param}")
    long externalLongPath(@HeaderParam("Authorization") @NotNull AuthHeader authHeader, @PathParam("param") long param);

    @GET
    @Path("base/optionalExternalLong")
    @ClientEndpoint(method = "GET", path = "/base/optionalExternalLong")
    Optional<Long> optionalExternalLongQuery(
            @HeaderParam("Authorization") @NotNull AuthHeader authHeader, @QueryParam("param") Optional<Long> param);

    @POST
    @Path("base/notNullBody")
    @ClientEndpoint(method = "POST", path = "/base/notNullBody")
    StringAliasExample notNullBody(
            @HeaderParam("Authorization") @NotNull AuthHeader authHeader, @NotNull StringAliasExample notNullBody);

    @GET
    @Path("base/aliasOne")
    @ClientEndpoint(method = "GET", path = "/base/aliasOne")
    StringAliasExample aliasOne(
            @HeaderParam("Authorization") @NotNull AuthHeader authHeader,
            @QueryParam("queryParamName") StringAliasExample queryParamName);

    @GET
    @Path("base/optionalAliasOne")
    @ClientEndpoint(method = "GET", path = "/base/optionalAliasOne")
    StringAliasExample optionalAliasOne(
            @HeaderParam("Authorization") @NotNull AuthHeader authHeader,
            @QueryParam("queryParamName") Optional<StringAliasExample> queryParamName);

    @GET
    @Path("base/aliasTwo")
    @ClientEndpoint(method = "GET", path = "/base/aliasTwo")
    NestedStringAliasExample aliasTwo(
            @HeaderParam("Authorization") @NotNull AuthHeader authHeader,
            @QueryParam("queryParamName") NestedStringAliasExample queryParamName);

    @POST
    @Path("base/external/notNullBody")
    @ClientEndpoint(method = "POST", path = "/base/external/notNullBody")
    allexamples.com.palantir.product.StringAliasExample notNullBodyExternalImport(
            @HeaderParam("Authorization") @NotNull AuthHeader authHeader,
            @NotNull allexamples.com.palantir.product.StringAliasExample notNullBody);

    @POST
    @Path("base/external/optional-body")
    @ClientEndpoint(method = "POST", path = "/base/external/optional-body")
    Optional<allexamples.com.palantir.product.StringAliasExample> optionalBodyExternalImport(
            @HeaderParam("Authorization") @NotNull AuthHeader authHeader,
            Optional<allexamples.com.palantir.product.StringAliasExample> body);

    @POST
    @Path("base/external/optional-query")
    @ClientEndpoint(method = "POST", path = "/base/external/optional-query")
    Optional<allexamples.com.palantir.product.StringAliasExample> optionalQueryExternalImport(
            @HeaderParam("Authorization") @NotNull AuthHeader authHeader,
            @QueryParam("query") Optional<allexamples.com.palantir.product.StringAliasExample> query);

    @POST
    @Path("base/no-return")
    @ClientEndpoint(method = "POST", path = "/base/no-return")
    void noReturn(@HeaderParam("Authorization") @NotNull AuthHeader authHeader);

    @GET
    @Path("base/enum/query")
    @ClientEndpoint(method = "GET", path = "/base/enum/query")
    SimpleEnum enumQuery(
            @HeaderParam("Authorization") @NotNull AuthHeader authHeader,
            @QueryParam("queryParamName") SimpleEnum queryParamName);

    @GET
    @Path("base/enum/list/query")
    @ClientEndpoint(method = "GET", path = "/base/enum/list/query")
    List<SimpleEnum> enumListQuery(
            @HeaderParam("Authorization") @NotNull AuthHeader authHeader,
            @QueryParam("queryParamName") List<SimpleEnum> queryParamName);

    @GET
    @Path("base/enum/optional/query")
    @ClientEndpoint(method = "GET", path = "/base/enum/optional/query")
    Optional<SimpleEnum> optionalEnumQuery(
            @HeaderParam("Authorization") @NotNull AuthHeader authHeader,
            @QueryParam("queryParamName") Optional<SimpleEnum> queryParamName);

    @GET
    @Path("base/enum/header")
    @ClientEndpoint(method = "GET", path = "/base/enum/header")
    SimpleEnum enumHeader(
            @HeaderParam("Authorization") @NotNull AuthHeader authHeader,
            @HeaderParam("Custom-Header") SimpleEnum headerParameter);

    /**
     * This endpoint is used to test that the <code>Accept-Conjure-Error-Parameter-Format</code> header is respected.
     * Specifically, that error parameters are serialized as JSON when the header is set to <code>JSON</code>.
     */
    @GET
    @Path("base/errors/header")
    @ClientEndpoint(method = "GET", path = "/base/errors/header")
    String jsonErrorsHeader(
            @HeaderParam("Authorization") @NotNull AuthHeader authHeader,
            @HeaderParam("Accept-Conjure-Error-Parameter-Format") String headerParameter);

    /**
     * This endpoint is used to test that error parameters serialized as JSON or using <code>Objects.toString</code> are
     * both handled correctly by the clients that can deserialize &quot;rich&quot; exceptions (which are sub-types of
     * <code>RemoteException</code>).
     */
    @GET
    @Path("base/errors/serialization")
    @ClientEndpoint(method = "GET", path = "/base/errors/serialization")
    String errorParameterSerialization(
            @HeaderParam("Authorization") @NotNull AuthHeader authHeader,
            @HeaderParam("Accept-Conjure-Error-Parameter-Format") String headerParameter);

    @GET
    @Path("base/alias-long")
    @ClientEndpoint(method = "GET", path = "/base/alias-long")
    Optional<LongAlias> aliasLongEndpoint(
            @HeaderParam("Authorization") @NotNull AuthHeader authHeader,
            @QueryParam("input") Optional<LongAlias> input);

    @GET
    @Path("base/datasets/{datasetRid}/strings")
    @ClientEndpoint(method = "GET", path = "/base/datasets/{datasetRid}/strings")
    void complexQueryParameters(
            @HeaderParam("Authorization") @NotNull AuthHeader authHeader,
            @PathParam("datasetRid") ResourceIdentifier datasetRid,
            @QueryParam("strings") Set<StringAliasExample> strings,
            @QueryParam("longs") Set<Long> longs,
            @QueryParam("ints") Set<Integer> ints);

    @PUT
    @Path("base/list/optionals")
    @ClientEndpoint(method = "PUT", path = "/base/list/optionals")
    void receiveListOfOptionals(
            @HeaderParam("Authorization") @NotNull AuthHeader authHeader, @NotNull List<Optional<String>> value);

    @PUT
    @Path("base/set/optionals")
    @ClientEndpoint(method = "PUT", path = "/base/set/optionals")
    void receiveSetOfOptionals(
            @HeaderParam("Authorization") @NotNull AuthHeader authHeader, @NotNull Set<Optional<String>> value);

    @PUT
    @Path("base/list/strings")
    @ClientEndpoint(method = "PUT", path = "/base/list/strings")
    void receiveListOfStrings(
            @HeaderParam("Authorization") @NotNull AuthHeader authHeader, @NotNull List<String> value);

    @Deprecated
    @ClientEndpoint(method = "GET", path = "/base/optionalExternalLong")
    default Optional<Long> optionalExternalLongQuery(AuthHeader authHeader) {
        return optionalExternalLongQuery(authHeader, Optional.empty());
    }

    @Deprecated
    @ClientEndpoint(method = "GET", path = "/base/optionalAliasOne")
    default StringAliasExample optionalAliasOne(AuthHeader authHeader) {
        return optionalAliasOne(authHeader, Optional.empty());
    }

    @Deprecated
    @ClientEndpoint(method = "POST", path = "/base/external/optional-query")
    default Optional<allexamples.com.palantir.product.StringAliasExample> optionalQueryExternalImport(
            AuthHeader authHeader) {
        return optionalQueryExternalImport(authHeader, Optional.empty());
    }

    @Deprecated
    @ClientEndpoint(method = "GET", path = "/base/enum/list/query")
    default List<SimpleEnum> enumListQuery(AuthHeader authHeader) {
        return enumListQuery(authHeader, Collections.emptyList());
    }

    @Deprecated
    @ClientEndpoint(method = "GET", path = "/base/enum/optional/query")
    default Optional<SimpleEnum> optionalEnumQuery(AuthHeader authHeader) {
        return optionalEnumQuery(authHeader, Optional.empty());
    }

    @Deprecated
    @ClientEndpoint(method = "GET", path = "/base/alias-long")
    default Optional<LongAlias> aliasLongEndpoint(AuthHeader authHeader) {
        return aliasLongEndpoint(authHeader, Optional.empty());
    }

    @Deprecated
    @ClientEndpoint(method = "GET", path = "/base/datasets/{datasetRid}/strings")
    default void complexQueryParameters(AuthHeader authHeader, ResourceIdentifier datasetRid) {
        complexQueryParameters(
                authHeader, datasetRid, Collections.emptySet(), Collections.emptySet(), Collections.emptySet());
    }

    @Deprecated
    @ClientEndpoint(method = "GET", path = "/base/datasets/{datasetRid}/strings")
    default void complexQueryParameters(
            AuthHeader authHeader, ResourceIdentifier datasetRid, Set<StringAliasExample> strings) {
        complexQueryParameters(authHeader, datasetRid, strings, Collections.emptySet(), Collections.emptySet());
    }

    @Deprecated
    @ClientEndpoint(method = "GET", path = "/base/datasets/{datasetRid}/strings")
    default void complexQueryParameters(
            AuthHeader authHeader, ResourceIdentifier datasetRid, Set<StringAliasExample> strings, Set<Long> longs) {
        complexQueryParameters(authHeader, datasetRid, strings, longs, Collections.emptySet());
    }
}
