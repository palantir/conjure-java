package com.palantir.product;

import com.google.common.util.concurrent.ListenableFuture;
import com.palantir.conjure.java.lib.internal.ConjureEndpoint;
import com.palantir.conjure.java.lib.internal.ConjureService;
import javax.annotation.Generated;
import retrofit2.http.GET;
import retrofit2.http.Headers;

@Generated("com.palantir.conjure.java.services.Retrofit2ServiceGenerator")
@ConjureService(name = "EmptyPathService", package_ = "com.palantir.product")
public interface EmptyPathServiceRetrofit {
    @GET("./")
    @Headers({"hr-path-template: /", "Accept: application/json"})
    @ConjureEndpoint(path = "/", method = "GET")
    ListenableFuture<Boolean> emptyPath();
}
