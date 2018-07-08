# Conjure-Java

_Generate Java POJOs and interfaces from [Conjure API definitions](https://github.com/palantir/conjure)._

## Usage

The recommended way to use conjure-java is via a build tool like [gradle-conjure](https://github.com/palantir/gradle-conjure). If you want to use it without gradle-conjure, there is also a standalone executable CLI which conforms to [RFC 002](https://github.com/palantir/conjure/blob/develop/rfc/002-contract-for-conjure-generators.md),  published on [bintray](https://bintray.com/palantir/releases/conjure-java).

    Usage: conjure-java generate <input> <output> [...options]

        --objects    Generate POJOs for Conjure type definitions
        --jersey     Generate jax-rs annotated interfaces for client or server-usage
        --retrofit   Generate retrofit interfaces for streaming/async clients

