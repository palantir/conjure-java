package com.palantir.product;

import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.ri.ResourceIdentifier;
import com.palantir.tokens.auth.AuthHeader;
import com.palantir.tokens.auth.BearerToken;
import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.annotation.Generated;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/")
@Generated("com.palantir.conjure.java.services.JaxRsClientGenerator")
public interface EteServiceJaxRsClient {
    @GET
    @Path("base/string")
    String string(@HeaderParam("Authorization") AuthHeader authHeader);

    @GET
    @Path("base/integer")
    int integer(@HeaderParam("Authorization") AuthHeader authHeader);

    @GET
    @Path("base/double")
    double double_(@HeaderParam("Authorization") AuthHeader authHeader);

    @GET
    @Path("base/boolean")
    boolean boolean_(@HeaderParam("Authorization") AuthHeader authHeader);

    @GET
    @Path("base/safelong")
    SafeLong safelong(@HeaderParam("Authorization") AuthHeader authHeader);

    @GET
    @Path("base/rid")
    ResourceIdentifier rid(@HeaderParam("Authorization") AuthHeader authHeader);

    @GET
    @Path("base/bearertoken")
    BearerToken bearertoken(@HeaderParam("Authorization") AuthHeader authHeader);

    @GET
    @Path("base/optionalString")
    Optional<String> optionalString(@HeaderParam("Authorization") AuthHeader authHeader);

    @GET
    @Path("base/optionalEmpty")
    Optional<String> optionalEmpty(@HeaderParam("Authorization") AuthHeader authHeader);

    @GET
    @Path("base/datetime")
    OffsetDateTime datetime(@HeaderParam("Authorization") AuthHeader authHeader);

    @GET
    @Path("base/binary")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    InputStream binary(@HeaderParam("Authorization") AuthHeader authHeader);

    @GET
    @Path("base/path/{param}")
    String path(
            @HeaderParam("Authorization") AuthHeader authHeader, @PathParam("param") String param);

    @POST
    @Path("base/notNullBody")
    StringAliasExample notNullBody(
            @HeaderParam("Authorization") AuthHeader authHeader, StringAliasExample notNullBody);

    @GET
    @Path("base/aliasOne")
    StringAliasExample aliasOne(
            @HeaderParam("Authorization") AuthHeader authHeader,
            @QueryParam("queryParamName") StringAliasExample queryParamName);

    @GET
    @Path("base/optionalAliasOne")
    StringAliasExample optionalAliasOne(
            @HeaderParam("Authorization") AuthHeader authHeader,
            @QueryParam("queryParamName") Optional<StringAliasExample> queryParamName);

    @GET
    @Path("base/aliasTwo")
    NestedStringAliasExample aliasTwo(
            @HeaderParam("Authorization") AuthHeader authHeader,
            @QueryParam("queryParamName") NestedStringAliasExample queryParamName);

    @POST
    @Path("base/external/notNullBody")
    StringAliasExample notNullBodyExternalImport(
            @HeaderParam("Authorization") AuthHeader authHeader, StringAliasExample notNullBody);

    @POST
    @Path("base/external/optional-body")
    Optional<StringAliasExample> optionalBodyExternalImport(
            @HeaderParam("Authorization") AuthHeader authHeader, Optional<StringAliasExample> body);

    @POST
    @Path("base/external/optional-query")
    Optional<StringAliasExample> optionalQueryExternalImport(
            @HeaderParam("Authorization") AuthHeader authHeader,
            @QueryParam("query") Optional<StringAliasExample> query);

    @POST
    @Path("base/no-return")
    void noReturn(@HeaderParam("Authorization") AuthHeader authHeader);

    @GET
    @Path("base/enum/query")
    SimpleEnum enumQuery(
            @HeaderParam("Authorization") AuthHeader authHeader,
            @QueryParam("queryParamName") SimpleEnum queryParamName);

    @GET
    @Path("base/enum/list/query")
    List<SimpleEnum> enumListQuery(
            @HeaderParam("Authorization") AuthHeader authHeader,
            @QueryParam("queryParamName") List<SimpleEnum> queryParamName);

    @GET
    @Path("base/enum/optional/query")
    Optional<SimpleEnum> optionalEnumQuery(
            @HeaderParam("Authorization") AuthHeader authHeader,
            @QueryParam("queryParamName") Optional<SimpleEnum> queryParamName);

    @GET
    @Path("base/enum/header")
    SimpleEnum enumHeader(
            @HeaderParam("Authorization") AuthHeader authHeader,
            @HeaderParam("Custom-Header") SimpleEnum headerParameter);

    @Deprecated
    default StringAliasExample optionalAliasOne(AuthHeader authHeader) {
        return optionalAliasOne(authHeader, Optional.empty());
    }

    @Deprecated
    default Optional<StringAliasExample> optionalQueryExternalImport(AuthHeader authHeader) {
        return optionalQueryExternalImport(authHeader, Optional.empty());
    }

    @Deprecated
    default List<SimpleEnum> enumListQuery(AuthHeader authHeader) {
        return enumListQuery(authHeader, Collections.emptyList());
    }

    @Deprecated
    default Optional<SimpleEnum> optionalEnumQuery(AuthHeader authHeader) {
        return optionalEnumQuery(authHeader, Optional.empty());
    }
}
