package com.palantir.product;

import com.google.common.util.concurrent.ListenableFuture;
import com.palantir.tokens.auth.AuthHeader;
import java.util.Optional;
import javax.annotation.Generated;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

@Generated("com.palantir.conjure.java.services.Retrofit2ServiceGenerator")
public interface EteBinaryServiceRetrofit {
    @POST("./binary")
    @Headers({"hr-path-template: /binary", "Accept: application/octet-stream"})
    @Streaming
    ListenableFuture<ResponseBody> postBinary(@Header("Authorization") AuthHeader authHeader, @Body RequestBody body);

    @POST("./binary/throws")
    @Headers({"hr-path-template: /binary/throws", "Accept: application/octet-stream"})
    @Streaming
    ListenableFuture<ResponseBody> postBinaryThrows(
            @Header("Authorization") AuthHeader authHeader,
            @Query("bytesToRead") int bytesToRead,
            @Body RequestBody body);

    @GET("./binary/optional/present")
    @Headers({"hr-path-template: /binary/optional/present", "Accept: application/octet-stream"})
    @Streaming
    ListenableFuture<Optional<ResponseBody>> getOptionalBinaryPresent(@Header("Authorization") AuthHeader authHeader);

    @GET("./binary/optional/empty")
    @Headers({"hr-path-template: /binary/optional/empty", "Accept: application/octet-stream"})
    @Streaming
    ListenableFuture<Optional<ResponseBody>> getOptionalBinaryEmpty(@Header("Authorization") AuthHeader authHeader);

    /**
     * Throws an exception after partially writing a binary response.
     */
    @GET("./binary/failure")
    @Headers({"hr-path-template: /binary/failure", "Accept: application/octet-stream"})
    @Streaming
    ListenableFuture<ResponseBody> getBinaryFailure(
            @Header("Authorization") AuthHeader authHeader, @Query("numBytes") int numBytes);

    @GET("./binary/aliased")
    @Headers({"hr-path-template: /binary/aliased", "Accept: application/octet-stream"})
    @Streaming
    ListenableFuture<Optional<ResponseBody>> getAliased(@Header("Authorization") AuthHeader authHeader);
}
