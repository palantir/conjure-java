package com.palantir.product;

import com.google.common.util.concurrent.ListenableFuture;
import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.conjure.java.lib.internal.ConjureEndpoint;
import com.palantir.conjure.java.lib.internal.ConjureService;
import com.palantir.ri.ResourceIdentifier;
import com.palantir.tokens.auth.AuthHeader;
import com.palantir.tokens.auth.BearerToken;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Generated;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

@Generated("com.palantir.conjure.java.services.Retrofit2ServiceGenerator")
@ConjureService(name = "EteService", package_ = "com.palantir.product")
public interface EteServiceRetrofit {
    /**
     * foo bar baz.
     * <h2>Very Important Documentation</h2>
     * <p>This documentation provides a <em>list</em>:</p>
     * <ul>
     * <li>Docs rule</li>
     * <li>Lists are wonderful</li>
     * </ul>
     */
    @GET("./base/string")
    @Headers({"hr-path-template: /base/string", "Accept: application/json"})
    @ConjureEndpoint(path = "/base/string", method = "GET")
    ListenableFuture<String> string(@Header("Authorization") AuthHeader authHeader);

    /**
     * one <em>two</em> three.
     */
    @GET("./base/integer")
    @Headers({"hr-path-template: /base/integer", "Accept: application/json"})
    @ConjureEndpoint(path = "/base/integer", method = "GET")
    ListenableFuture<Integer> integer(@Header("Authorization") AuthHeader authHeader);

    @GET("./base/double")
    @Headers({"hr-path-template: /base/double", "Accept: application/json"})
    @ConjureEndpoint(path = "/base/double", method = "GET")
    ListenableFuture<Double> double_(@Header("Authorization") AuthHeader authHeader);

    @GET("./base/boolean")
    @Headers({"hr-path-template: /base/boolean", "Accept: application/json"})
    @ConjureEndpoint(path = "/base/boolean", method = "GET")
    ListenableFuture<Boolean> boolean_(@Header("Authorization") AuthHeader authHeader);

    @GET("./base/safelong")
    @Headers({"hr-path-template: /base/safelong", "Accept: application/json"})
    @ConjureEndpoint(path = "/base/safelong", method = "GET")
    ListenableFuture<SafeLong> safelong(@Header("Authorization") AuthHeader authHeader);

    @GET("./base/rid")
    @Headers({"hr-path-template: /base/rid", "Accept: application/json"})
    @ConjureEndpoint(path = "/base/rid", method = "GET")
    ListenableFuture<ResourceIdentifier> rid(@Header("Authorization") AuthHeader authHeader);

    @GET("./base/bearertoken")
    @Headers({"hr-path-template: /base/bearertoken", "Accept: application/json"})
    @ConjureEndpoint(path = "/base/bearertoken", method = "GET")
    ListenableFuture<BearerToken> bearertoken(@Header("Authorization") AuthHeader authHeader);

    @GET("./base/optionalString")
    @Headers({"hr-path-template: /base/optionalString", "Accept: application/json"})
    @ConjureEndpoint(path = "/base/optionalString", method = "GET")
    ListenableFuture<Optional<String>> optionalString(@Header("Authorization") AuthHeader authHeader);

    @GET("./base/optionalEmpty")
    @Headers({"hr-path-template: /base/optionalEmpty", "Accept: application/json"})
    @ConjureEndpoint(path = "/base/optionalEmpty", method = "GET")
    ListenableFuture<Optional<String>> optionalEmpty(@Header("Authorization") AuthHeader authHeader);

    @GET("./base/datetime")
    @Headers({"hr-path-template: /base/datetime", "Accept: application/json"})
    @ConjureEndpoint(path = "/base/datetime", method = "GET")
    ListenableFuture<OffsetDateTime> datetime(@Header("Authorization") AuthHeader authHeader);

    @GET("./base/binary")
    @Headers({"hr-path-template: /base/binary", "Accept: application/octet-stream"})
    @Streaming
    @ConjureEndpoint(path = "/base/binary", method = "GET")
    ListenableFuture<ResponseBody> binary(@Header("Authorization") AuthHeader authHeader);

    /**
     * Path endpoint.
     * @param param Documentation for <code>param</code>
     */
    @GET("./base/path/{param}")
    @Headers({"hr-path-template: /base/path/{param}", "Accept: application/json"})
    @ConjureEndpoint(path = "/base/path/{param}", method = "GET")
    ListenableFuture<String> path(@Header("Authorization") AuthHeader authHeader, @Path("param") String param);

    @GET("./base/externalLong/{param}")
    @Headers({"hr-path-template: /base/externalLong/{param}", "Accept: application/json"})
    @ConjureEndpoint(path = "/base/externalLong/{param}", method = "GET")
    ListenableFuture<Long> externalLongPath(@Header("Authorization") AuthHeader authHeader, @Path("param") long param);

    @GET("./base/optionalExternalLong")
    @Headers({"hr-path-template: /base/optionalExternalLong", "Accept: application/json"})
    @ConjureEndpoint(path = "/base/optionalExternalLong", method = "GET")
    ListenableFuture<Optional<Long>> optionalExternalLongQuery(
            @Header("Authorization") AuthHeader authHeader, @Query("param") Optional<Long> param);

    @POST("./base/notNullBody")
    @Headers({"hr-path-template: /base/notNullBody", "Accept: application/json"})
    @ConjureEndpoint(path = "/base/notNullBody", method = "POST")
    ListenableFuture<StringAliasExample> notNullBody(
            @Header("Authorization") AuthHeader authHeader, @Body StringAliasExample notNullBody);

    @GET("./base/aliasOne")
    @Headers({"hr-path-template: /base/aliasOne", "Accept: application/json"})
    @ConjureEndpoint(path = "/base/aliasOne", method = "GET")
    ListenableFuture<StringAliasExample> aliasOne(
            @Header("Authorization") AuthHeader authHeader, @Query("queryParamName") StringAliasExample queryParamName);

    @GET("./base/optionalAliasOne")
    @Headers({"hr-path-template: /base/optionalAliasOne", "Accept: application/json"})
    @ConjureEndpoint(path = "/base/optionalAliasOne", method = "GET")
    ListenableFuture<StringAliasExample> optionalAliasOne(
            @Header("Authorization") AuthHeader authHeader,
            @Query("queryParamName") Optional<StringAliasExample> queryParamName);

    @GET("./base/aliasTwo")
    @Headers({"hr-path-template: /base/aliasTwo", "Accept: application/json"})
    @ConjureEndpoint(path = "/base/aliasTwo", method = "GET")
    ListenableFuture<NestedStringAliasExample> aliasTwo(
            @Header("Authorization") AuthHeader authHeader,
            @Query("queryParamName") NestedStringAliasExample queryParamName);

    @POST("./base/external/notNullBody")
    @Headers({"hr-path-template: /base/external/notNullBody", "Accept: application/json"})
    @ConjureEndpoint(path = "/base/external/notNullBody", method = "POST")
    ListenableFuture<StringAliasExample> notNullBodyExternalImport(
            @Header("Authorization") AuthHeader authHeader, @Body StringAliasExample notNullBody);

    @POST("./base/external/optional-body")
    @Headers({"hr-path-template: /base/external/optional-body", "Accept: application/json"})
    @ConjureEndpoint(path = "/base/external/optional-body", method = "POST")
    ListenableFuture<Optional<StringAliasExample>> optionalBodyExternalImport(
            @Header("Authorization") AuthHeader authHeader, @Body Optional<StringAliasExample> body);

    @POST("./base/external/optional-query")
    @Headers({"hr-path-template: /base/external/optional-query", "Accept: application/json"})
    @ConjureEndpoint(path = "/base/external/optional-query", method = "POST")
    ListenableFuture<Optional<StringAliasExample>> optionalQueryExternalImport(
            @Header("Authorization") AuthHeader authHeader, @Query("query") Optional<StringAliasExample> query);

    @POST("./base/no-return")
    @Headers({"hr-path-template: /base/no-return", "Accept: application/json"})
    @ConjureEndpoint(path = "/base/no-return", method = "POST")
    ListenableFuture<Void> noReturn(@Header("Authorization") AuthHeader authHeader);

    @GET("./base/enum/query")
    @Headers({"hr-path-template: /base/enum/query", "Accept: application/json"})
    @ConjureEndpoint(path = "/base/enum/query", method = "GET")
    ListenableFuture<SimpleEnum> enumQuery(
            @Header("Authorization") AuthHeader authHeader, @Query("queryParamName") SimpleEnum queryParamName);

    @GET("./base/enum/list/query")
    @Headers({"hr-path-template: /base/enum/list/query", "Accept: application/json"})
    @ConjureEndpoint(path = "/base/enum/list/query", method = "GET")
    ListenableFuture<List<SimpleEnum>> enumListQuery(
            @Header("Authorization") AuthHeader authHeader, @Query("queryParamName") List<SimpleEnum> queryParamName);

    @GET("./base/enum/optional/query")
    @Headers({"hr-path-template: /base/enum/optional/query", "Accept: application/json"})
    @ConjureEndpoint(path = "/base/enum/optional/query", method = "GET")
    ListenableFuture<Optional<SimpleEnum>> optionalEnumQuery(
            @Header("Authorization") AuthHeader authHeader,
            @Query("queryParamName") Optional<SimpleEnum> queryParamName);

    @GET("./base/enum/header")
    @Headers({"hr-path-template: /base/enum/header", "Accept: application/json"})
    @ConjureEndpoint(path = "/base/enum/header", method = "GET")
    ListenableFuture<SimpleEnum> enumHeader(
            @Header("Authorization") AuthHeader authHeader, @Header("Custom-Header") SimpleEnum headerParameter);

    @GET("./base/alias-long")
    @Headers({"hr-path-template: /base/alias-long", "Accept: application/json"})
    @ConjureEndpoint(path = "/base/alias-long", method = "GET")
    ListenableFuture<Optional<LongAlias>> aliasLongEndpoint(
            @Header("Authorization") AuthHeader authHeader, @Query("input") Optional<LongAlias> input);

    @GET("./base/datasets/{datasetRid}/strings")
    @Headers({"hr-path-template: /base/datasets/{datasetRid}/strings", "Accept: application/json"})
    @ConjureEndpoint(path = "/base/datasets/{datasetRid}/strings", method = "GET")
    ListenableFuture<Void> complexQueryParameters(
            @Header("Authorization") AuthHeader authHeader,
            @Path("datasetRid") ResourceIdentifier datasetRid,
            @Query("strings") Set<StringAliasExample> strings,
            @Query("longs") Set<Long> longs,
            @Query("ints") Set<Integer> ints);

    @PUT("./base/list/optionals")
    @Headers({"hr-path-template: /base/list/optionals", "Accept: application/json"})
    @ConjureEndpoint(path = "/base/list/optionals", method = "PUT")
    ListenableFuture<Void> receiveListOfOptionals(
            @Header("Authorization") AuthHeader authHeader, @Body List<Optional<String>> value);

    @PUT("./base/set/optionals")
    @Headers({"hr-path-template: /base/set/optionals", "Accept: application/json"})
    @ConjureEndpoint(path = "/base/set/optionals", method = "PUT")
    ListenableFuture<Void> receiveSetOfOptionals(
            @Header("Authorization") AuthHeader authHeader, @Body Set<Optional<String>> value);

    @Deprecated
    @ConjureEndpoint(path = "/base/optionalExternalLong", method = "GET")
    default ListenableFuture<Optional<Long>> optionalExternalLongQuery(@Header("Authorization") AuthHeader authHeader) {
        return optionalExternalLongQuery(authHeader, Optional.empty());
    }

    @Deprecated
    @ConjureEndpoint(path = "/base/optionalAliasOne", method = "GET")
    default ListenableFuture<StringAliasExample> optionalAliasOne(@Header("Authorization") AuthHeader authHeader) {
        return optionalAliasOne(authHeader, Optional.empty());
    }

    @Deprecated
    @ConjureEndpoint(path = "/base/external/optional-query", method = "POST")
    default ListenableFuture<Optional<StringAliasExample>> optionalQueryExternalImport(
            @Header("Authorization") AuthHeader authHeader) {
        return optionalQueryExternalImport(authHeader, Optional.empty());
    }

    @Deprecated
    @ConjureEndpoint(path = "/base/enum/list/query", method = "GET")
    default ListenableFuture<List<SimpleEnum>> enumListQuery(@Header("Authorization") AuthHeader authHeader) {
        return enumListQuery(authHeader, Collections.emptyList());
    }

    @Deprecated
    @ConjureEndpoint(path = "/base/enum/optional/query", method = "GET")
    default ListenableFuture<Optional<SimpleEnum>> optionalEnumQuery(@Header("Authorization") AuthHeader authHeader) {
        return optionalEnumQuery(authHeader, Optional.empty());
    }

    @Deprecated
    @ConjureEndpoint(path = "/base/alias-long", method = "GET")
    default ListenableFuture<Optional<LongAlias>> aliasLongEndpoint(@Header("Authorization") AuthHeader authHeader) {
        return aliasLongEndpoint(authHeader, Optional.empty());
    }

    @Deprecated
    @ConjureEndpoint(path = "/base/datasets/{datasetRid}/strings", method = "GET")
    default void complexQueryParameters(
            @Header("Authorization") AuthHeader authHeader, @Path("datasetRid") ResourceIdentifier datasetRid) {
        complexQueryParameters(
                authHeader, datasetRid, Collections.emptySet(), Collections.emptySet(), Collections.emptySet());
    }

    @Deprecated
    @ConjureEndpoint(path = "/base/datasets/{datasetRid}/strings", method = "GET")
    default void complexQueryParameters(
            @Header("Authorization") AuthHeader authHeader,
            @Path("datasetRid") ResourceIdentifier datasetRid,
            @Query("strings") Set<StringAliasExample> strings) {
        complexQueryParameters(authHeader, datasetRid, strings, Collections.emptySet(), Collections.emptySet());
    }

    @Deprecated
    @ConjureEndpoint(path = "/base/datasets/{datasetRid}/strings", method = "GET")
    default void complexQueryParameters(
            @Header("Authorization") AuthHeader authHeader,
            @Path("datasetRid") ResourceIdentifier datasetRid,
            @Query("strings") Set<StringAliasExample> strings,
            @Query("longs") Set<Long> longs) {
        complexQueryParameters(authHeader, datasetRid, strings, longs, Collections.emptySet());
    }
}
