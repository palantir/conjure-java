package com.palantir.product;

import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.ri.ResourceIdentifier;
import com.palantir.tokens.auth.AuthHeader;
import com.palantir.tokens.auth.BearerToken;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import javax.annotation.Generated;
import okhttp3.ResponseBody;
import retrofit2.Call;
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
    @GET("./base/string")
    @Headers({"hr-path-template: /base/string", "Accept: application/json"})
    Call<String> string(@Header("Authorization") AuthHeader authHeader);

    /** one <em>two</em> three. */
    @GET("./base/integer")
    @Headers({"hr-path-template: /base/integer", "Accept: application/json"})
    Call<Integer> integer(@Header("Authorization") AuthHeader authHeader);

    @GET("./base/double")
    @Headers({"hr-path-template: /base/double", "Accept: application/json"})
    Call<Double> double_(@Header("Authorization") AuthHeader authHeader);

    @GET("./base/boolean")
    @Headers({"hr-path-template: /base/boolean", "Accept: application/json"})
    Call<Boolean> boolean_(@Header("Authorization") AuthHeader authHeader);

    @GET("./base/safelong")
    @Headers({"hr-path-template: /base/safelong", "Accept: application/json"})
    Call<SafeLong> safelong(@Header("Authorization") AuthHeader authHeader);

    @GET("./base/rid")
    @Headers({"hr-path-template: /base/rid", "Accept: application/json"})
    Call<ResourceIdentifier> rid(@Header("Authorization") AuthHeader authHeader);

    @GET("./base/bearertoken")
    @Headers({"hr-path-template: /base/bearertoken", "Accept: application/json"})
    Call<BearerToken> bearertoken(@Header("Authorization") AuthHeader authHeader);

    @GET("./base/optionalString")
    @Headers({"hr-path-template: /base/optionalString", "Accept: application/json"})
    Call<Optional<String>> optionalString(@Header("Authorization") AuthHeader authHeader);

    @GET("./base/optionalEmpty")
    @Headers({"hr-path-template: /base/optionalEmpty", "Accept: application/json"})
    Call<Optional<String>> optionalEmpty(@Header("Authorization") AuthHeader authHeader);

    @GET("./base/datetime")
    @Headers({"hr-path-template: /base/datetime", "Accept: application/json"})
    Call<OffsetDateTime> datetime(@Header("Authorization") AuthHeader authHeader);

    @GET("./base/binary")
    @Headers({"hr-path-template: /base/binary", "Accept: application/octet-stream"})
    @Streaming
    Call<ResponseBody> binary(@Header("Authorization") AuthHeader authHeader);

    @GET("./base/path/{param}")
    @Headers({"hr-path-template: /base/path/{param}", "Accept: application/json"})
    Call<String> path(@Header("Authorization") AuthHeader authHeader, @Path("param") String param);

    @GET("./base/externalLong/{param}")
    @Headers({"hr-path-template: /base/externalLong/{param}", "Accept: application/json"})
    Call<Long> externalLongPath(
            @Header("Authorization") AuthHeader authHeader, @Path("param") long param);

    @GET("./base/optionalExternalLong")
    @Headers({"hr-path-template: /base/optionalExternalLong", "Accept: application/json"})
    Call<Optional<Long>> optionalExternalLongQuery(
            @Header("Authorization") AuthHeader authHeader, @Query("param") Optional<Long> param);

    @POST("./base/notNullBody")
    @Headers({"hr-path-template: /base/notNullBody", "Accept: application/json"})
    Call<StringAliasExample> notNullBody(
            @Header("Authorization") AuthHeader authHeader, @Body StringAliasExample notNullBody);

    @GET("./base/aliasOne")
    @Headers({"hr-path-template: /base/aliasOne", "Accept: application/json"})
    Call<StringAliasExample> aliasOne(
            @Header("Authorization") AuthHeader authHeader,
            @Query("queryParamName") StringAliasExample queryParamName);

    @GET("./base/optionalAliasOne")
    @Headers({"hr-path-template: /base/optionalAliasOne", "Accept: application/json"})
    Call<StringAliasExample> optionalAliasOne(
            @Header("Authorization") AuthHeader authHeader,
            @Query("queryParamName") Optional<StringAliasExample> queryParamName);

    @GET("./base/aliasTwo")
    @Headers({"hr-path-template: /base/aliasTwo", "Accept: application/json"})
    Call<NestedStringAliasExample> aliasTwo(
            @Header("Authorization") AuthHeader authHeader,
            @Query("queryParamName") NestedStringAliasExample queryParamName);

    @POST("./base/external/notNullBody")
    @Headers({"hr-path-template: /base/external/notNullBody", "Accept: application/json"})
    Call<StringAliasExample> notNullBodyExternalImport(
            @Header("Authorization") AuthHeader authHeader, @Body StringAliasExample notNullBody);

    @POST("./base/external/optional-body")
    @Headers({"hr-path-template: /base/external/optional-body", "Accept: application/json"})
    Call<Optional<StringAliasExample>> optionalBodyExternalImport(
            @Header("Authorization") AuthHeader authHeader,
            @Body Optional<StringAliasExample> body);

    @POST("./base/external/optional-query")
    @Headers({"hr-path-template: /base/external/optional-query", "Accept: application/json"})
    Call<Optional<StringAliasExample>> optionalQueryExternalImport(
            @Header("Authorization") AuthHeader authHeader,
            @Query("query") Optional<StringAliasExample> query);

    @POST("./base/no-return")
    @Headers({"hr-path-template: /base/no-return", "Accept: application/json"})
    Call<Void> noReturn(@Header("Authorization") AuthHeader authHeader);

    @GET("./base/enum/query")
    @Headers({"hr-path-template: /base/enum/query", "Accept: application/json"})
    Call<SimpleEnum> enumQuery(
            @Header("Authorization") AuthHeader authHeader,
            @Query("queryParamName") SimpleEnum queryParamName);

    @GET("./base/enum/list/query")
    @Headers({"hr-path-template: /base/enum/list/query", "Accept: application/json"})
    Call<List<SimpleEnum>> enumListQuery(
            @Header("Authorization") AuthHeader authHeader,
            @Query("queryParamName") List<SimpleEnum> queryParamName);

    @GET("./base/enum/optional/query")
    @Headers({"hr-path-template: /base/enum/optional/query", "Accept: application/json"})
    Call<Optional<SimpleEnum>> optionalEnumQuery(
            @Header("Authorization") AuthHeader authHeader,
            @Query("queryParamName") Optional<SimpleEnum> queryParamName);

    @GET("./base/enum/header")
    @Headers({"hr-path-template: /base/enum/header", "Accept: application/json"})
    Call<SimpleEnum> enumHeader(
            @Header("Authorization") AuthHeader authHeader,
            @Header("Custom-Header") SimpleEnum headerParameter);
}
