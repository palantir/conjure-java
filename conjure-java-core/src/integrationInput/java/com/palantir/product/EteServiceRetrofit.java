package com.palantir.product;

import com.google.common.util.concurrent.ListenableFuture;
import com.palantir.conjure.java.lib.SafeLong;
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
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

@Generated("com.palantir.conjure.java.services.Retrofit2ServiceGenerator")
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
    ListenableFuture<String> string(@Header("Authorization") AuthHeader authHeader);

    /**
     * one <em>two</em> three.
     */
    @GET("./base/integer")
    @Headers({"hr-path-template: /base/integer", "Accept: application/json"})
    ListenableFuture<Integer> integer(@Header("Authorization") AuthHeader authHeader);

    @GET("./base/double")
    @Headers({"hr-path-template: /base/double", "Accept: application/json"})
    ListenableFuture<Double> double_(@Header("Authorization") AuthHeader authHeader);

    @GET("./base/boolean")
    @Headers({"hr-path-template: /base/boolean", "Accept: application/json"})
    ListenableFuture<Boolean> boolean_(@Header("Authorization") AuthHeader authHeader);

    @GET("./base/safelong")
    @Headers({"hr-path-template: /base/safelong", "Accept: application/json"})
    ListenableFuture<SafeLong> safelong(@Header("Authorization") AuthHeader authHeader);

    @GET("./base/rid")
    @Headers({"hr-path-template: /base/rid", "Accept: application/json"})
    ListenableFuture<ResourceIdentifier> rid(@Header("Authorization") AuthHeader authHeader);

    @GET("./base/bearertoken")
    @Headers({"hr-path-template: /base/bearertoken", "Accept: application/json"})
    ListenableFuture<BearerToken> bearertoken(@Header("Authorization") AuthHeader authHeader);

    @GET("./base/optionalString")
    @Headers({"hr-path-template: /base/optionalString", "Accept: application/json"})
    ListenableFuture<Optional<String>> optionalString(@Header("Authorization") AuthHeader authHeader);

    @GET("./base/optionalEmpty")
    @Headers({"hr-path-template: /base/optionalEmpty", "Accept: application/json"})
    ListenableFuture<Optional<String>> optionalEmpty(@Header("Authorization") AuthHeader authHeader);

    @GET("./base/datetime")
    @Headers({"hr-path-template: /base/datetime", "Accept: application/json"})
    ListenableFuture<OffsetDateTime> datetime(@Header("Authorization") AuthHeader authHeader);

    @GET("./base/binary")
    @Headers({"hr-path-template: /base/binary", "Accept: application/octet-stream"})
    @Streaming
    ListenableFuture<ResponseBody> binary(@Header("Authorization") AuthHeader authHeader);

    /**
     * Path endpoint.
     * @param param Documentation for <code>param</code>
     */
    @GET("./base/path/{param}")
    @Headers({"hr-path-template: /base/path/{param}", "Accept: application/json"})
    ListenableFuture<String> path(@Header("Authorization") AuthHeader authHeader, @Path("param") String param);

    @GET("./base/externalLong/{param}")
    @Headers({"hr-path-template: /base/externalLong/{param}", "Accept: application/json"})
    ListenableFuture<Long> externalLongPath(@Header("Authorization") AuthHeader authHeader, @Path("param") long param);

    @GET("./base/optionalExternalLong")
    @Headers({"hr-path-template: /base/optionalExternalLong", "Accept: application/json"})
    ListenableFuture<Optional<Long>> optionalExternalLongQuery(
            @Header("Authorization") AuthHeader authHeader, @Query("param") Optional<Long> param);

    @POST("./base/notNullBody")
    @Headers({"hr-path-template: /base/notNullBody", "Accept: application/json"})
    ListenableFuture<StringAliasExample> notNullBody(
            @Header("Authorization") AuthHeader authHeader, @Body StringAliasExample notNullBody);

    @GET("./base/aliasOne")
    @Headers({"hr-path-template: /base/aliasOne", "Accept: application/json"})
    ListenableFuture<StringAliasExample> aliasOne(
            @Header("Authorization") AuthHeader authHeader, @Query("queryParamName") StringAliasExample queryParamName);

    @GET("./base/optionalAliasOne")
    @Headers({"hr-path-template: /base/optionalAliasOne", "Accept: application/json"})
    ListenableFuture<StringAliasExample> optionalAliasOne(
            @Header("Authorization") AuthHeader authHeader,
            @Query("queryParamName") Optional<StringAliasExample> queryParamName);

    @GET("./base/aliasTwo")
    @Headers({"hr-path-template: /base/aliasTwo", "Accept: application/json"})
    ListenableFuture<NestedStringAliasExample> aliasTwo(
            @Header("Authorization") AuthHeader authHeader,
            @Query("queryParamName") NestedStringAliasExample queryParamName);

    @POST("./base/external/notNullBody")
    @Headers({"hr-path-template: /base/external/notNullBody", "Accept: application/json"})
    ListenableFuture<StringAliasExample> notNullBodyExternalImport(
            @Header("Authorization") AuthHeader authHeader, @Body StringAliasExample notNullBody);

    @POST("./base/external/optional-body")
    @Headers({"hr-path-template: /base/external/optional-body", "Accept: application/json"})
    ListenableFuture<Optional<StringAliasExample>> optionalBodyExternalImport(
            @Header("Authorization") AuthHeader authHeader, @Body Optional<StringAliasExample> body);

    @POST("./base/external/optional-query")
    @Headers({"hr-path-template: /base/external/optional-query", "Accept: application/json"})
    ListenableFuture<Optional<StringAliasExample>> optionalQueryExternalImport(
            @Header("Authorization") AuthHeader authHeader, @Query("query") Optional<StringAliasExample> query);

    @POST("./base/no-return")
    @Headers({"hr-path-template: /base/no-return", "Accept: application/json"})
    ListenableFuture<Void> noReturn(@Header("Authorization") AuthHeader authHeader);

    @GET("./base/enum/query")
    @Headers({"hr-path-template: /base/enum/query", "Accept: application/json"})
    ListenableFuture<SimpleEnum> enumQuery(
            @Header("Authorization") AuthHeader authHeader, @Query("queryParamName") SimpleEnum queryParamName);

    @GET("./base/enum/list/query")
    @Headers({"hr-path-template: /base/enum/list/query", "Accept: application/json"})
    ListenableFuture<List<SimpleEnum>> enumListQuery(
            @Header("Authorization") AuthHeader authHeader, @Query("queryParamName") List<SimpleEnum> queryParamName);

    @GET("./base/enum/optional/query")
    @Headers({"hr-path-template: /base/enum/optional/query", "Accept: application/json"})
    ListenableFuture<Optional<SimpleEnum>> optionalEnumQuery(
            @Header("Authorization") AuthHeader authHeader,
            @Query("queryParamName") Optional<SimpleEnum> queryParamName);

    @GET("./base/enum/header")
    @Headers({"hr-path-template: /base/enum/header", "Accept: application/json"})
    ListenableFuture<SimpleEnum> enumHeader(
            @Header("Authorization") AuthHeader authHeader, @Header("Custom-Header") SimpleEnum headerParameter);

    @GET("./base/alias-long")
    @Headers({"hr-path-template: /base/alias-long", "Accept: application/json"})
    ListenableFuture<Optional<LongAlias>> aliasLongEndpoint(
            @Header("Authorization") AuthHeader authHeader, @Query("input") Optional<LongAlias> input);

    @GET("./base/datasets/{datasetRid}/strings")
    @Headers({"hr-path-template: /base/datasets/{datasetRid}/strings", "Accept: application/json"})
    ListenableFuture<Void> complexQueryParameters(
            @Header("Authorization") AuthHeader authHeader,
            @Path("datasetRid") ResourceIdentifier datasetRid,
            @Query("strings") Set<StringAliasExample> strings,
            @Query("longs") Set<Long> longs,
            @Query("ints") Set<Integer> ints);

    @Deprecated
    default ListenableFuture<Optional<Long>> optionalExternalLongQuery(@Header("Authorization") AuthHeader authHeader) {
        return optionalExternalLongQuery(authHeader, Optional.empty());
    }

    @Deprecated
    default ListenableFuture<StringAliasExample> optionalAliasOne(@Header("Authorization") AuthHeader authHeader) {
        return optionalAliasOne(authHeader, Optional.empty());
    }

    @Deprecated
    default ListenableFuture<Optional<StringAliasExample>> optionalQueryExternalImport(
            @Header("Authorization") AuthHeader authHeader) {
        return optionalQueryExternalImport(authHeader, Optional.empty());
    }

    @Deprecated
    default ListenableFuture<List<SimpleEnum>> enumListQuery(@Header("Authorization") AuthHeader authHeader) {
        return enumListQuery(authHeader, Collections.emptyList());
    }

    @Deprecated
    default ListenableFuture<Optional<SimpleEnum>> optionalEnumQuery(@Header("Authorization") AuthHeader authHeader) {
        return optionalEnumQuery(authHeader, Optional.empty());
    }

    @Deprecated
    default ListenableFuture<Optional<LongAlias>> aliasLongEndpoint(@Header("Authorization") AuthHeader authHeader) {
        return aliasLongEndpoint(authHeader, Optional.empty());
    }

    @Deprecated
    default void complexQueryParameters(
            @Header("Authorization") AuthHeader authHeader, @Path("datasetRid") ResourceIdentifier datasetRid) {
        complexQueryParameters(
                authHeader, datasetRid, Collections.emptySet(), Collections.emptySet(), Collections.emptySet());
    }

    @Deprecated
    default void complexQueryParameters(
            @Header("Authorization") AuthHeader authHeader,
            @Path("datasetRid") ResourceIdentifier datasetRid,
            @Query("strings") Set<StringAliasExample> strings) {
        complexQueryParameters(authHeader, datasetRid, strings, Collections.emptySet(), Collections.emptySet());
    }

    @Deprecated
    default void complexQueryParameters(
            @Header("Authorization") AuthHeader authHeader,
            @Path("datasetRid") ResourceIdentifier datasetRid,
            @Query("strings") Set<StringAliasExample> strings,
            @Query("longs") Set<Long> longs) {
        complexQueryParameters(authHeader, datasetRid, strings, longs, Collections.emptySet());
    }
}
