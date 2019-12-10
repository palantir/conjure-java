package com.palantir.product;

import com.google.common.util.concurrent.ListenableFuture;
import javax.annotation.Generated;
import retrofit2.http.GET;
import retrofit2.http.Headers;

@Generated("com.palantir.conjure.java.services.Retrofit2ServiceGenerator")
public interface EmptyPathServiceRetrofit {
    @GET("./")
    @Headers({"hr-path-template: /", "Accept: application/json"})
    ListenableFuture<Boolean> emptyPath();
}
