# Conjure-Java ![Bintray](https://img.shields.io/bintray/v/palantir/releases/conjure-java.svg) [![License](https://img.shields.io/badge/License-Apache%202.0-lightgrey.svg)](https://opensource.org/licenses/Apache-2.0)




_CLI to generate Java POJOs and interfaces from [Conjure API definitions](https://github.com/palantir/conjure)._

## Usage

The recommended way to use conjure-java is via a build tool like [gradle-conjure](https://github.com/palantir/gradle-conjure). However, if you don't want to use gradle-conjure, there is also an executable which conforms to [RFC 002](https://github.com/palantir/conjure/blob/develop/rfc/002-contract-for-conjure-generators.md),  published on [bintray](https://bintray.com/palantir/releases/conjure-java).

    Usage: conjure-java generate [...options] <input> <output>

    Generate Java bindings for a Conjure API
        <input>      Path to the input IR file
        <output>     Output directory for generated source

    Options:
        --objects    Generate POJOs for Conjure type definitions
        --jersey     Generate jax-rs annotated interfaces for client or server-usage
        --undertow   Generate undertow handlers and interfaces for server-usage
        --retrofit   Generate retrofit interfaces for streaming/async clients
        --requireNotNullAuthAndBodyParams
                     Generate @NotNull annotations for AuthHeaders and request body params
        --retrofitCompletableFutures
                     Generate retrofit services which return Java8 CompletableFuture instead of OkHttp Call (deprecated)
        --retrofitListenableFutures
                     Generate retrofit services which return Guava ListenableFuture instead of OkHttp Call
        --undertowServicePrefixes
                     Generate service interfaces for Undertow with class names prefixed 'Undertow'
        --undertowListenableFutures
                     Generate Undertow services which return Guava ListenableFuture for asynchronous processing
        --useImmutableBytes
                     Generate binary fields using the immutable 'Bytes' type instead of 'ByteBuffer'

### Feature Flags

Conjure-java supports feature flags to enable additional opt-in features. To enable features provided by a feature
flag, simply supply the feature flag as an additional parameter when executing conjure-java. Available feature flags
can be found in the [FeatureFlags](./conjure-java-core/src/main/java/com/palantir/conjure/java/FeatureFlags.java) class.

## Example generated objects

Conjure-java objects are always immutable and thread-safe.  Fields are never null; instead, Java 8 Optionals are used. JSON serialization is handled using [Jackson](https://github.com/FasterXML/jackson) annotations.

- **Conjure object: [ManyFieldExample](./conjure-java-core/src/integrationInput/java/com/palantir/product/ManyFieldExample.java)**

  Objects can only be instantiated using the builder pattern:

    ```java
    ManyFieldExample example = ManyFieldExample.builder()
            .string("foo")
            .integer(123)
            .optionalItem("bar")
            .addAllItems(iterable)
            .build();
    ```

    Or using Jackson via [`conjure-jackson-serialization`](https://github.com/palantir/conjure-java-runtime):

    ```java
    ObjectMapper mapper = ObjectMappers.newServerObjectMapper();
    ManyFieldExample fromJson = mapper.readValue("{\"string\":\"foo\", ...}", ManyFieldExample.class);
    ```

- **Conjure union: [UnionTypeExample](./conjure-java-core/src/integrationInput/java/com/palantir/product/UnionTypeExample.java)**

    Union types can be one of a few variants. To interact with a union value, users should use the `.accept` method and define a Visitor that handles each of the possible variants, including the possibility of an unknown variant.

    ```java
    Foo output = unionTypeExample.accept(new Visitor<Foo>() {

        public Foo visitStringExample(StringExample value) {
            // your logic here!
        }

        public Foo visitSet(Set<String> value) {}

        // ...

        public Foo visitUnknown(String unknownType) {}

    });
    ```

    Visitors may seem clunky in Java, but they have the upside of compile-time assurance that you've handled all the possible variants.  If you upgrade an API dependency and the API author added a new variant, the Java compiler will force you to explicitly deal with this new variant.  We intentionally avoid `switch` statements and `instanceof` checks for this exact reason.

- **Conjure enum: [EnumExample](./conjure-java-core/src/integrationInput/java/com/palantir/product/EnumExample.java)**

  Enums are subtly different from regular Java enums because they tolerate deserializing unknown values.  This is important because it ensures server authors can add new variants to an enum without causing runtime errors for consumers that use older API jars.

  ```java
  EnumExample one = EnumExample.ONE;
  System.out.println(one); // prints: 'ONE'

  EnumExample fromJson = mapper.readValue("\"XYZ\"", EnumExample.class);
  System.out.println(fromJson); // prints: 'XYZ'
  ```

- **Conjure alias: [StringAliasExample](./conjure-java-core/src/integrationInput/java/com/palantir/product/StringAliasExample.java)**

  Aliases have exactly the same JSON representation as their inner type, so they are useful for making error-prone function signatures more bulletproof:

  ```diff
  -doSomething(String, String, String);
  +doSomething(ProductId, UserId, EmailAddress);
  ```

## Example Jersey interfaces

Conjure-java generates Java interfaces with [JAX-RS](http://jax-rs-spec.java.net/) annotations, so they can be used on the client side or on the server-side.

Example jersey interface: [EteService](./conjure-java-core/src/integrationInput/java/com/palantir/product/EteService.java)

```java
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/")
@Generated("com.palantir.conjure.java.services.JerseyServiceGenerator")
public interface EteService {
    @GET
    @Path("base/string")
    String string(@HeaderParam("Authorization") AuthHeader authHeader);
}
```

### Jersey clients

Use [conjure-java-runtime's `JaxRsClient`](https://github.com/palantir/conjure-java-runtime#conjure-java-jaxrs-client) which configures Feign with sensible defaults:

```java
RecipeBookService recipes = JaxRsClient.create(
        RecipeBookService.class,
        userAgent,
        hostMetrics,
        clientConfiguration);

List<Recipe> results = recipes.getRecipes();
```

### Jersey servers

You need a [Jersey](https://jersey.github.io/)-compatible server. We recommend Dropwizard (which is based on Jetty), but Grizzly, Tomcat, etc should also work fine.  Use [conjure-java-runtime's `ConjureJerseyFeature`](https://github.com/palantir/conjure-java-runtime#conjure-java-jersey-server) to configure Jackson and Exception mappers appropriately.  Then, you just need to implement the interface:

```java
public final class RecipeBookResource implements RecipeBookService {

    @Override
    public List<Recipe> getRecipes() {
        // ...
    }
}
```

Then in your server main method, [register your resource](https://www.dropwizard.io/1.3.5/docs/getting-started.html#registering-a-resource). For example, using Dropwizard:

```diff
 public void run(YourConfiguration configuration, Environment environment) {
     // ...
+    environment.jersey().register(new RecipeBookResource());
 }
```

## Example Retrofit2 interfaces

As an alternative to the JAX-RS interfaces above, conjure-java can generate equivalent interfaces with [Retrofit2](http://square.github.io/retrofit/) annotations. These clients are useful if you want to stream binary data or make non-blocking async calls:

Example Retrofit2 interface: [EteServiceRetrofit](./conjure-java-core/src/integrationInput/java/com/palantir/product/EteServiceRetrofit.java)

```java
public interface EteServiceRetrofit {

    @GET("./base/binary")
    @Streaming
    Call<ResponseBody> binary(@Header("Authorization") AuthHeader authHeader);
}
```

### Retrofit2 clients

Use [conjure-java-runtime's `Retrofit2Client`](https://github.com/palantir/conjure-java-runtime#conjure-java-retrofit2-client) which configures Retrofit2 with sensible defaults:


```java
RecipeBookServiceRetrofit recipes = Retrofit2Client.create(
            RecipeBookServiceRetrofit.class,
            userAgent,
            hostMetrics,
            clientConfiguration);

Call<List<Recipe>> asyncResults = recipes.getRecipes();
```

## Undertow

In the undertow setting, for a `ServiceName` conjure defined service, conjure will generate an interface: `ServiceName` to be extended by your resource and a [Service](https://github.com/palantir/conjure-java/blob/develop/conjure-undertow-lib/src/main/java/com/palantir/conjure/java/undertow/lib/Service.java) named `ServiceNameEndpoints`

To avoid conflicts with the equivalent jersey interface (when you are generating both), use in your build.gradle:

```groovy
conjure {
    java {
        undertowServicePrefixes = true
    }
}
```

With this option, Undertow generated interfaces will be prefixed with `Undertow`. Here the interface would be called: `UndertowServiceName`

To use the generated handlers:

```java
public static void main(String[] args) {
    Undertow server = Undertow.builder()
            .addHttpListener(8080, "0.0.0.0")
            .setHandler(ConjureHandler.builder()
                    .addAllEndpoints(RecipeBookServiceEndpoints.of(new RecipeBookResource())
                            .endpoints(ConjureUndertowRuntime.builder().build()))
                    .build())
            .build();
    server.start();
}
```

### Asynchronous Request Processing

The Conjure Undertow generator supports asynchronous request processing allowing all service methods to return a
[Guava ListenableFuture](https://github.com/google/guava/wiki/ListenableFutureExplained). These methods may be
implemented synchronously by replacing `return object` with `return Futures.immediateFuture(object)`.

Asynchronous request processing decouples the HTTP request lifecycle from server task threads, allowing you to replace
waiting on shared resources with callbacks, reducing the number of required threads.

This feature is enabled using:

```groovy
conjure {
    java {
        undertowListenableFutures = true
    }
}
```

#### Examples

*Asynchronous request processing is helpful for endpoints which do not need a thread for the entirety of
the request.*

:+1: Delegation to an asynchronous client, for instance either retrofit or dialogue :+1:

```java
@Override
public ListenableFuture<String> getValue() {
    // Assuming this retrofit client was compiled with --retrofitListenableFutures
    return retrofitClient.getValue();
}
```

:-1: Not for delegation to synchronous operations, Feign clients for example :-1:

This example is less efficient than `return Futures.immediateFuture(feignClient.getValue())` because you pay an
additional cost to switch threads, and maintain an additional executor beyond the configured server thread pool.

```java
ListeningExecutor executor = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());

@Override
public ListenableFuture<String> getValue() {
    // BAD: do not do this!
    return executor.submit(() -> feignClient.getValue());
}
```

:+1: Long polling :+1:

Long polling provides lower latency than simple repeated polling, but requests take a long time relative
to computation. A single thread can often handle updating all polling requests without blocking N request
threads waiting for results.

```java
@Override
public ListenableFuture<String> getValue() {
    SettableFuture<String> result = SettableFuture.create();
    registerFuture(result);
    return result;
}
```

## conjure-lib `Bytes` class

By default, conjure-java will use `java.nio.ByteByffer` to represent fields of Conjure type `binary`.  However, the ByteBuffer class has many subtleties, including interior mutability.

To avoid many of these foot-guns, we recommend using the `useImmutableBytes` feature flag to opt-in to using the [`com.palantir.conjure.java.lib.Bytes`](conjure-lib/src/main/java/com/palantir/conjure/java/lib/Bytes.java) class.
This will become the default behaviour in a future major release of conjure-java.

## Contributing

For instructions on how to set up your local development environment, check out the [Contributing document](./CONTRIBUTING.md).

## License
This project is made available under the [Apache 2.0 License](/LICENSE).
