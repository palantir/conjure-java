# Conjure-Java ![Bintray](https://img.shields.io/bintray/v/palantir/releases/conjure-java.svg) [![License](https://img.shields.io/badge/License-Apache%202.0-lightgrey.svg)](https://opensource.org/licenses/Apache-2.0)




_CLI to generate Java POJOs and interfaces from [Conjure API definitions](https://github.com/palantir/conjure)._

## Usage

The recommended way to use conjure-java is via a build tool like [gradle-conjure](https://github.com/palantir/gradle-conjure). However, if you don't want to use gradle-conjure, there is also an executable which conforms to [RFC 002](https://github.com/palantir/conjure/blob/develop/rfc/002-contract-for-conjure-generators.md),  published on [bintray](https://bintray.com/palantir/releases/conjure-java).

    Usage: conjure-java generate <input> <output> [...options]

        --objects       [boolean] Generate POJOs for Conjure type definitions
        --jersey        [boolean] Generate jax-rs annotated interfaces for client or server-usage
        --retrofit      [boolean] Generate retrofit interfaces for streaming/async clients
        --featureFlag   [string] Enable the specified feature flag during code generation
        
### Feature Flags

Conjure-java supports feature flags to enable additional opt-in features. To enable features provided by a feature
flag, simply supply the feature flag as an additional parameter when executing conjure-java. Available feature flags
can be found in the [FeatureFlags](./conjure-java-core/src/main/java/com/palantir/conjure/java/FeatureFlags.java) class.

## Example generated objects

Conjure-java objects are always immutable and thread-safe.  Fields are never null; instead, Java 8 Optionals are used. JSON serialization is handled using [Jackson](https://github.com/FasterXML/jackson) annotations.

- **Conjure object: [ManyFieldExample](./conjure-java-core/src/integrationInput/java/com/palantir/product/ManyFieldExample.java)**

  Objects can only be instantiated using the builder pattern, or by deserializing from JSON.

    ```java
    ManyFieldExample example = ManyFieldExample.builder()
                .string("foo")
                .integer(123)
                .optionalItem("bar")
                .addAllItems(iterable)
                .build();

    // or using Jackson (via com.palantir.conjure.java.runtime:conjure-jackson-serialization)
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

- Example jersey interface: [EteService](./conjure-java-core/src/integrationInput/java/com/palantir/product/EteService.java)

For **client-side usage**, use [http-remoting](https://github.com/palantir/http-remoting#jaxrs-clients) which configures Feign with sensible defaults.

For **server-side usage**, you need a [Jersey](https://jersey.github.io/)-compatible server. We recommend Dropwizard (which is based on Jetty), but Grizzly, Tomcat, etc should also work fine!  Use [http-remoting's jersey-servers](https://github.com/palantir/http-remoting#jersey-servers) to configure Jackson and Exception mappers appropriately.


## Example Retrofit interfaces

As an alternative to the JAX-RS interfaces above, conjure-java can generate equivalent interfaces with [Retrofit2](http://square.github.io/retrofit/) annotations. These clients are useful if you want to stream binary data or make non-blocking async calls:

- Example retrofit2 interface: [EteServiceRetrofit](./conjure-java-core/src/integrationInput/java/com/palantir/product/EteServiceRetrofit.java)

    ```java
    @GET("./base/binary")
    @Streaming
    Call<ResponseBody> binary(@Header("Authorization") AuthHeader authHeader);
    ```

You can also supply the `--retrofitCompletableFutures` flag if you prefer Java 8 CompletableFutures instead of OkHttp's Call.

## Contributing

For instructions on how to set up your local development environment, check out the [Contributing document](./CONTRIBUTING.md).
