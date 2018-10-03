package com.palantir.product;

import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.ri.ResourceIdentifier;
import com.palantir.tokens.auth.AuthHeader;
import com.palantir.tokens.auth.BearerToken;
import java.lang.Boolean;
import java.lang.Double;
import java.lang.Integer;
import java.lang.String;
import java.time.OffsetDateTime;
import java.util.Optional;
import javax.annotation.Generated;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

@Generated("com.palantir.conjure.java.services.Retrofit2ServiceGenerator")
public interface EteServiceRetrofit {
    @GET("./base/string")
    @Headers("hr-path-template: /base/string")
    Call<String> string(@Header("Authorization") AuthHeader authHeader);

    @GET("./base/integer")
    @Headers("hr-path-template: /base/integer")
    Call<Integer> integer(@Header("Authorization") AuthHeader authHeader);

    @GET("./base/double")
    @Headers("hr-path-template: /base/double")
    Call<Double> double_(@Header("Authorization") AuthHeader authHeader);

    @GET("./base/boolean")
    @Headers("hr-path-template: /base/boolean")
    Call<Boolean> boolean_(@Header("Authorization") AuthHeader authHeader);

    @GET("./base/safelong")
    @Headers("hr-path-template: /base/safelong")
    Call<SafeLong> safelong(@Header("Authorization") AuthHeader authHeader);

    @GET("./base/rid")
    @Headers("hr-path-template: /base/rid")
    Call<ResourceIdentifier> rid(@Header("Authorization") AuthHeader authHeader);

    @GET("./base/bearertoken")
    @Headers("hr-path-template: /base/bearertoken")
    Call<BearerToken> bearertoken(@Header("Authorization") AuthHeader authHeader);

    @GET("./base/optionalString")
    @Headers("hr-path-template: /base/optionalString")
    Call<Optional<String>> optionalString(@Header("Authorization") AuthHeader authHeader);

    @GET("./base/optionalEmpty")
    @Headers("hr-path-template: /base/optionalEmpty")
    Call<Optional<String>> optionalEmpty(@Header("Authorization") AuthHeader authHeader);

    @GET("./base/datetime")
    @Headers("hr-path-template: /base/datetime")
    Call<OffsetDateTime> datetime(@Header("Authorization") AuthHeader authHeader);

    @GET("./base/binary")
    @Headers("hr-path-template: /base/binary")
    @Streaming
    Call<ResponseBody> binary(@Header("Authorization") AuthHeader authHeader);

    @POST("./base/notNullBody")
    @Headers("hr-path-template: /base/notNullBody")
    Call<StringAliasExample> notNullBody(
            @Header("Authorization") AuthHeader authHeader, @Body StringAliasExample notNullBody);

    @GET("./base/optionalAliasOne")
    @Headers("hr-path-template: /base/optionalAliasOne")
    Call<StringAliasExample> optionalAliasOne(
            @Header("Authorization") AuthHeader authHeader,
            @Query("queryParamName") Optional<StringAliasExample> queryParamName);

    @GET("./base/aliasTwo")
    @Headers("hr-path-template: /base/aliasTwo")
    Call<NestedStringAliasExample> aliasTwo(
            @Header("Authorization") AuthHeader authHeader,
            @Query("queryParamName") NestedStringAliasExample queryParamName);
}
