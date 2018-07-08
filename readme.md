# Conjure-Java

_Generate Java POJOs and interfaces from [Conjure API definitions](https://github.com/palantir/conjure)._

## Usage

The recommended way to use conjure-java is via a build tool like [gradle-conjure](https://github.com/palantir/gradle-conjure). If you want to use it without gradle-conjure, there is also a standalone executable CLI which conforms to [RFC 002](https://github.com/palantir/conjure/blob/develop/rfc/002-contract-for-conjure-generators.md),  published on [bintray](https://bintray.com/palantir/releases/conjure-java).

    Usage: conjure-java generate <input> <output> [...options]

        --objects    Generate POJOs for Conjure type definitions
        --jersey     Generate jax-rs annotated interfaces for client or server-usage
        --retrofit   Generate retrofit interfaces for streaming/async clients

## Example generated objects

- Conjure object: [ManyFieldExample](./conjure-java-core/src/integrationInput/java/com/palantir/product/ManyFieldExample.java)
- Conjure alias: [StringAliasExample](./conjure-java-core/src/integrationInput/java/com/palantir/product/StringAliasExample.java)
- Conjure enum: [EnumExample](./conjure-java-core/src/integrationInput/java/com/palantir/product/EnumExample.java)
- Conjure union: [UnionTypeExample](./conjure-java-core/src/integrationInput/java/com/palantir/product/UnionTypeExample.java)

## Example Jersey interfaces

Conjure-java generates Java interfaces with [JAX-RS](http://jax-rs-spec.java.net/) annotations, so they can be used on the client side or on the server-side.

- Example jersey interface: [EteService](./conjure-java-core/src/integrationInput/java/com/palantir/product/EteService.java)

For **client-side usage**, use [http-remoting](https://github.com/palantir/http-remoting#jaxrs-clients) which configures Feign with sensible defaults.

For **server-side usage**, you need a [Jersey](https://jersey.github.io/)-compatible server. We recommend Dropwizard (which is based on Jetty), but Grizzly, Tomcat, etc should also work fine!  Use [http-remoting's jersey-servers](https://github.com/palantir/http-remoting#jersey-servers) to configure Jackson and Exception mappers appropriately.


## Example Retrofit interfaces

As an alternative to the JAX-RS interfaces above, conjure-java can generate equivalent interfaces with [Retrofit2](http://square.github.io/retrofit/) annotations. These clients are useful if you to to stream binary data or make non-blocking async calls:

- Example retrofit2 interface: [EteServiceRetrofit](./conjure-java-core/src/integrationInput/java/com/palantir/product/EteServiceRetrofit.java)

```java
    @GET("./base/binary")
    @Streaming
    Call<ResponseBody> binary(@Header("Authorization") AuthHeader authHeader);
```

You can also supply the `--retrofitCompletableFutures` flag if you prefer Java 8 CompletableFutures instead of OkHttp's Call.
