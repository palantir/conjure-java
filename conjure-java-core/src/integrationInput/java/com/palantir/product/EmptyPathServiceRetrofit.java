package com.palantir.product;

import javax.annotation.Generated;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

@Generated("com.palantir.conjure.java.services.Retrofit2ServiceGenerator")
public interface EmptyPathServiceRetrofit {
    @GET("./")
    @Headers({"hr-path-template: /", "Accept: application/json"})
    Call<Boolean> emptyPath();
}
