package com.palantir.product;

import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.conjure.java.lib.internal.ConjureClientEndpoint;
import com.palantir.ri.ResourceIdentifier;
import com.palantir.tokens.auth.AuthHeader;
import com.palantir.tokens.auth.BearerToken;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Generated;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.StreamingOutput;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/")
@Generated("com.palantir.conjure.java.services.JerseyServiceGenerator")
public interface EteService {
    /**
     * foo bar baz.
     * <h2>Very Important Documentation</h2>
     * <p>This documentation provides a <em>list</em>:</p>
     * <ul>
     * <li>Docs rule</li>
     * <li>Lists are wonderful</li>
     * </ul>
     */
    @GET
    @Path("base/string")
    @ConjureClientEndpoint(path = "/base/string", method = "GET")
    String string(@HeaderParam("Authorization") @NotNull AuthHeader authHeader);

    /**
     * one <em>two</em> three.
     */
    @GET
    @Path("base/integer")
    @ConjureClientEndpoint(path = "/base/integer", method = "GET")
    int integer(@HeaderParam("Authorization") @NotNull AuthHeader authHeader);

    @GET
    @Path("base/double")
    @ConjureClientEndpoint(path = "/base/double", method = "GET")
    double double_(@HeaderParam("Authorization") @NotNull AuthHeader authHeader);

    @GET
    @Path("base/boolean")
    @ConjureClientEndpoint(path = "/base/boolean", method = "GET")
    boolean boolean_(@HeaderParam("Authorization") @NotNull AuthHeader authHeader);

    @GET
    @Path("base/safelong")
    @ConjureClientEndpoint(path = "/base/safelong", method = "GET")
    SafeLong safelong(@HeaderParam("Authorization") @NotNull AuthHeader authHeader);

    @GET
    @Path("base/rid")
    @ConjureClientEndpoint(path = "/base/rid", method = "GET")
    ResourceIdentifier rid(@HeaderParam("Authorization") @NotNull AuthHeader authHeader);

    @GET
    @Path("base/bearertoken")
    @ConjureClientEndpoint(path = "/base/bearertoken", method = "GET")
    BearerToken bearertoken(@HeaderParam("Authorization") @NotNull AuthHeader authHeader);

    @GET
    @Path("base/optionalString")
    @ConjureClientEndpoint(path = "/base/optionalString", method = "GET")
    Optional<String> optionalString(@HeaderParam("Authorization") @NotNull AuthHeader authHeader);

    @GET
    @Path("base/optionalEmpty")
    @ConjureClientEndpoint(path = "/base/optionalEmpty", method = "GET")
    Optional<String> optionalEmpty(@HeaderParam("Authorization") @NotNull AuthHeader authHeader);

    @GET
    @Path("base/datetime")
    @ConjureClientEndpoint(path = "/base/datetime", method = "GET")
    OffsetDateTime datetime(@HeaderParam("Authorization") @NotNull AuthHeader authHeader);

    @GET
    @Path("base/binary")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @ConjureClientEndpoint(path = "/base/binary", method = "GET")
    StreamingOutput binary(@HeaderParam("Authorization") @NotNull AuthHeader authHeader);

    /**
     * Path endpoint.
     * @param param Documentation for <code>param</code>
     */
    @GET
    @Path("base/path/{param}")
    @ConjureClientEndpoint(path = "/base/path/{param}", method = "GET")
    String path(@HeaderParam("Authorization") @NotNull AuthHeader authHeader, @PathParam("param") String param);

    @GET
    @Path("base/externalLong/{param}")
    @ConjureClientEndpoint(path = "/base/externalLong/{param}", method = "GET")
    long externalLongPath(@HeaderParam("Authorization") @NotNull AuthHeader authHeader, @PathParam("param") long param);

    @GET
    @Path("base/optionalExternalLong")
    @ConjureClientEndpoint(path = "/base/optionalExternalLong", method = "GET")
    Optional<Long> optionalExternalLongQuery(
            @HeaderParam("Authorization") @NotNull AuthHeader authHeader, @QueryParam("param") Optional<Long> param);

    @POST
    @Path("base/notNullBody")
    @ConjureClientEndpoint(path = "/base/notNullBody", method = "POST")
    StringAliasExample notNullBody(
            @HeaderParam("Authorization") @NotNull AuthHeader authHeader, @NotNull StringAliasExample notNullBody);

    @GET
    @Path("base/aliasOne")
    @ConjureClientEndpoint(path = "/base/aliasOne", method = "GET")
    StringAliasExample aliasOne(
            @HeaderParam("Authorization") @NotNull AuthHeader authHeader,
            @QueryParam("queryParamName") StringAliasExample queryParamName);

    @GET
    @Path("base/optionalAliasOne")
    @ConjureClientEndpoint(path = "/base/optionalAliasOne", method = "GET")
    StringAliasExample optionalAliasOne(
            @HeaderParam("Authorization") @NotNull AuthHeader authHeader,
            @QueryParam("queryParamName") Optional<StringAliasExample> queryParamName);

    @GET
    @Path("base/aliasTwo")
    @ConjureClientEndpoint(path = "/base/aliasTwo", method = "GET")
    NestedStringAliasExample aliasTwo(
            @HeaderParam("Authorization") @NotNull AuthHeader authHeader,
            @QueryParam("queryParamName") NestedStringAliasExample queryParamName);

    @POST
    @Path("base/external/notNullBody")
    @ConjureClientEndpoint(path = "/base/external/notNullBody", method = "POST")
    StringAliasExample notNullBodyExternalImport(
            @HeaderParam("Authorization") @NotNull AuthHeader authHeader, @NotNull StringAliasExample notNullBody);

    @POST
    @Path("base/external/optional-body")
    @ConjureClientEndpoint(path = "/base/external/optional-body", method = "POST")
    Optional<StringAliasExample> optionalBodyExternalImport(
            @HeaderParam("Authorization") @NotNull AuthHeader authHeader, Optional<StringAliasExample> body);

    @POST
    @Path("base/external/optional-query")
    @ConjureClientEndpoint(path = "/base/external/optional-query", method = "POST")
    Optional<StringAliasExample> optionalQueryExternalImport(
            @HeaderParam("Authorization") @NotNull AuthHeader authHeader,
            @QueryParam("query") Optional<StringAliasExample> query);

    @POST
    @Path("base/no-return")
    @ConjureClientEndpoint(path = "/base/no-return", method = "POST")
    void noReturn(@HeaderParam("Authorization") @NotNull AuthHeader authHeader);

    @GET
    @Path("base/enum/query")
    @ConjureClientEndpoint(path = "/base/enum/query", method = "GET")
    SimpleEnum enumQuery(
            @HeaderParam("Authorization") @NotNull AuthHeader authHeader,
            @QueryParam("queryParamName") SimpleEnum queryParamName);

    @GET
    @Path("base/enum/list/query")
    @ConjureClientEndpoint(path = "/base/enum/list/query", method = "GET")
    List<SimpleEnum> enumListQuery(
            @HeaderParam("Authorization") @NotNull AuthHeader authHeader,
            @QueryParam("queryParamName") List<SimpleEnum> queryParamName);

    @GET
    @Path("base/enum/optional/query")
    @ConjureClientEndpoint(path = "/base/enum/optional/query", method = "GET")
    Optional<SimpleEnum> optionalEnumQuery(
            @HeaderParam("Authorization") @NotNull AuthHeader authHeader,
            @QueryParam("queryParamName") Optional<SimpleEnum> queryParamName);

    @GET
    @Path("base/enum/header")
    @ConjureClientEndpoint(path = "/base/enum/header", method = "GET")
    SimpleEnum enumHeader(
            @HeaderParam("Authorization") @NotNull AuthHeader authHeader,
            @HeaderParam("Custom-Header") SimpleEnum headerParameter);

    @GET
    @Path("base/alias-long")
    @ConjureClientEndpoint(path = "/base/alias-long", method = "GET")
    Optional<LongAlias> aliasLongEndpoint(
            @HeaderParam("Authorization") @NotNull AuthHeader authHeader,
            @QueryParam("input") Optional<LongAlias> input);

    @GET
    @Path("base/datasets/{datasetRid}/strings")
    @ConjureClientEndpoint(path = "/base/datasets/{datasetRid}/strings", method = "GET")
    void complexQueryParameters(
            @HeaderParam("Authorization") @NotNull AuthHeader authHeader,
            @PathParam("datasetRid") ResourceIdentifier datasetRid,
            @QueryParam("strings") Set<StringAliasExample> strings,
            @QueryParam("longs") Set<Long> longs,
            @QueryParam("ints") Set<Integer> ints);

    @PUT
    @Path("base/list/optionals")
    @ConjureClientEndpoint(path = "/base/list/optionals", method = "PUT")
    void receiveListOfOptionals(
            @HeaderParam("Authorization") @NotNull AuthHeader authHeader, @NotNull List<Optional<String>> value);

    @PUT
    @Path("base/set/optionals")
    @ConjureClientEndpoint(path = "/base/set/optionals", method = "PUT")
    void receiveSetOfOptionals(
            @HeaderParam("Authorization") @NotNull AuthHeader authHeader, @NotNull Set<Optional<String>> value);

    @Deprecated
    @ConjureClientEndpoint(path = "/base/optionalExternalLong", method = "GET")
    default Optional<Long> optionalExternalLongQuery(AuthHeader authHeader) {
        return optionalExternalLongQuery(authHeader, Optional.empty());
    }

    @Deprecated
    @ConjureClientEndpoint(path = "/base/optionalAliasOne", method = "GET")
    default StringAliasExample optionalAliasOne(AuthHeader authHeader) {
        return optionalAliasOne(authHeader, Optional.empty());
    }

    @Deprecated
    @ConjureClientEndpoint(path = "/base/external/optional-query", method = "POST")
    default Optional<StringAliasExample> optionalQueryExternalImport(AuthHeader authHeader) {
        return optionalQueryExternalImport(authHeader, Optional.empty());
    }

    @Deprecated
    @ConjureClientEndpoint(path = "/base/enum/list/query", method = "GET")
    default List<SimpleEnum> enumListQuery(AuthHeader authHeader) {
        return enumListQuery(authHeader, Collections.emptyList());
    }

    @Deprecated
    @ConjureClientEndpoint(path = "/base/enum/optional/query", method = "GET")
    default Optional<SimpleEnum> optionalEnumQuery(AuthHeader authHeader) {
        return optionalEnumQuery(authHeader, Optional.empty());
    }

    @Deprecated
    @ConjureClientEndpoint(path = "/base/alias-long", method = "GET")
    default Optional<LongAlias> aliasLongEndpoint(AuthHeader authHeader) {
        return aliasLongEndpoint(authHeader, Optional.empty());
    }

    @Deprecated
    @ConjureClientEndpoint(path = "/base/datasets/{datasetRid}/strings", method = "GET")
    default void complexQueryParameters(AuthHeader authHeader, ResourceIdentifier datasetRid) {
        complexQueryParameters(
                authHeader, datasetRid, Collections.emptySet(), Collections.emptySet(), Collections.emptySet());
    }

    @Deprecated
    @ConjureClientEndpoint(path = "/base/datasets/{datasetRid}/strings", method = "GET")
    default void complexQueryParameters(
            AuthHeader authHeader, ResourceIdentifier datasetRid, Set<StringAliasExample> strings) {
        complexQueryParameters(authHeader, datasetRid, strings, Collections.emptySet(), Collections.emptySet());
    }

    @Deprecated
    @ConjureClientEndpoint(path = "/base/datasets/{datasetRid}/strings", method = "GET")
    default void complexQueryParameters(
            AuthHeader authHeader, ResourceIdentifier datasetRid, Set<StringAliasExample> strings, Set<Long> longs) {
        complexQueryParameters(authHeader, datasetRid, strings, longs, Collections.emptySet());
    }
}
