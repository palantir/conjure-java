/*
 * (c) Copyright 2018 Palantir Technologies Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.palantir.conjure.java;

import com.palantir.conjure.java.lib.Bytes;
import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.conjure.java.undertow.lib.BinaryResponseBody;
import com.palantir.ri.ResourceIdentifier;
import com.palantir.tokens.auth.AuthHeader;
import com.palantir.tokens.auth.BearerToken;
import errors.com.palantir.product.AnyExample;
import errors.com.palantir.product.CollectionAlias;
import errors.com.palantir.product.CollectionExample;
import errors.com.palantir.product.ComplexExample;
import errors.com.palantir.product.ConjureErrors;
import errors.com.palantir.product.EmptyObject;
import errors.com.palantir.product.EnumExample;
import errors.com.palantir.product.ExternalExample;
import errors.com.palantir.product.NestedAlias;
import errors.com.palantir.product.NestedCollectionExample;
import errors.com.palantir.product.ObjectReference;
import errors.com.palantir.product.OptionalAlias;
import errors.com.palantir.product.OptionalExample;
import errors.com.palantir.product.PrimitiveExample;
import errors.com.palantir.product.SafetyExample;
import errors.com.palantir.product.StringAliasEx;
import errors.com.palantir.product.UnionExample;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.StreamingOutput;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import undertow.com.palantir.product.LongAlias;
import undertow.com.palantir.product.NestedStringAliasExample;
import undertow.com.palantir.product.SimpleEnum;
import undertow.com.palantir.product.StringAliasExample;
import undertow.com.palantir.product.UndertowEteService;

public final class UndertowEteResource implements UndertowEteService {
    @Override
    public String string(AuthHeader _authHeader) {
        return "Hello, world!";
    }

    @Override
    public int integer(AuthHeader _authHeader) {
        return 1234;
    }

    @Override
    public double double_(AuthHeader _authHeader) {
        return 1 / 3d;
    }

    @Override
    public boolean boolean_(AuthHeader _authHeader) {
        return true;
    }

    @Override
    public SafeLong safelong(AuthHeader _authHeader) {
        return SafeLong.of(12345L);
    }

    @Override
    public ResourceIdentifier rid(AuthHeader _authHeader) {
        return ResourceIdentifier.of("ri.foundry.main.dataset.1234");
    }

    @Override
    public BearerToken bearertoken(AuthHeader _authHeader) {
        return BearerToken.valueOf("fake");
    }

    @Override
    public Optional<String> optionalString(AuthHeader _authHeader) {
        return Optional.of("foo");
    }

    @Override
    public Optional<String> optionalEmpty(AuthHeader _authHeader) {
        return Optional.empty();
    }

    @Override
    public OffsetDateTime datetime(AuthHeader _authHeader) {
        return OffsetDateTime.ofInstant(Instant.ofEpochMilli(1234L), ZoneId.from(ZoneOffset.UTC));
    }

    @Override
    public Streaming binary(AuthHeader _authHeader) {
        return outputStream -> outputStream.write("Hello, world!".getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String path(AuthHeader _authHeader, String param) {
        return param;
    }

    @Override
    public long externalLongPath(AuthHeader _authHeader, long param) {
        return param;
    }

    @Override
    public Optional<Long> optionalExternalLongQuery(AuthHeader _authHeader, Optional<Long> param) {
        return param;
    }

    @Override
    public StringAliasExample notNullBody(AuthHeader _authHeader, StringAliasExample notNullBody) {
        return notNullBody;
    }

    @Override
    public StringAliasExample aliasOne(AuthHeader _authHeader, StringAliasExample queryParamName) {
        return queryParamName;
    }

    @Override
    public StringAliasExample optionalAliasOne(
            @NotNull AuthHeader _authHeader, Optional<StringAliasExample> queryParamName) {
        return queryParamName.orElseGet(() -> StringAliasExample.of("foo"));
    }

    @Override
    public NestedStringAliasExample aliasTwo(@NotNull AuthHeader _authHeader, NestedStringAliasExample queryParamName) {
        return queryParamName;
    }

    @Override
    public allexamples.com.palantir.product.StringAliasExample notNullBodyExternalImport(
            AuthHeader _authHeader, allexamples.com.palantir.product.StringAliasExample notNullBody) {
        return notNullBody;
    }

    @Override
    public Optional<allexamples.com.palantir.product.StringAliasExample> optionalBodyExternalImport(
            AuthHeader _authHeader, Optional<allexamples.com.palantir.product.StringAliasExample> body) {
        return body;
    }

    @Override
    public Optional<allexamples.com.palantir.product.StringAliasExample> optionalQueryExternalImport(
            AuthHeader _authHeader, Optional<allexamples.com.palantir.product.StringAliasExample> query) {
        return query;
    }

    @Override
    public void noReturn(AuthHeader _authHeader) {
        // nop
    }

    @Override
    public SimpleEnum enumQuery(AuthHeader _authHeader, SimpleEnum value) {
        return value;
    }

    @Override
    public List<SimpleEnum> enumListQuery(AuthHeader _authHeader, List<SimpleEnum> value) {
        return value;
    }

    @Override
    public Optional<SimpleEnum> optionalEnumQuery(AuthHeader _authHeader, Optional<SimpleEnum> value) {
        return value;
    }

    @Override
    public SimpleEnum enumHeader(AuthHeader _authHeader, SimpleEnum headerParameter) {
        return headerParameter;
    }

    @Override
    public String jsonErrorsHeader(AuthHeader authHeader, String headerParameter) {
        throw ConjureErrors.invalidServiceDefinition("my-service-string", Optional.of(SimpleEnum.VALUE));
    }

    @Override
    public String errorParameterSerialization(AuthHeader authHeader, String headerParameter) {
        if (headerParameter.startsWith("JSON") || headerParameter.equals("TOSTRING")) {
            throw ConjureErrors.errorWithComplexArgs(
                    PrimitiveExample.builder()
                            .stringVal("example-string")
                            .intVal(42)
                            .longVal(SafeLong.of(42))
                            .doubleVal(3.14)
                            .boolVal(true)
                            .ridVal(ResourceIdentifier.of("ri.service.instance.folder.object"))
                            .uuidVal(UUID.fromString("aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee"))
                            .datetimeVal(OffsetDateTime.MIN)
                            .binaryVal(Bytes.from("hello".getBytes(StandardCharsets.UTF_8)))
                            .build(),
                    CollectionExample.builder()
                            .stringList(List.of("foo", "bar", "baz"))
                            .stringSet(Set.of("alpha", "beta"))
                            .stringMap(Map.of("key1", "value1", "key2", "value2"))
                            .build(),
                    NestedCollectionExample.builder()
                            .nestedList(List.of(List.of("nested1", "nested2"), List.of("nested3", "nested4")))
                            .nestedMap(Map.of(
                                    "outer1", Map.of("inner1", "value1", "inner2", "value2"),
                                    "outer2", Map.of("inner3", "value3")))
                            .mixedCollection(Map.of(
                                    "objects",
                                    List.of(
                                            ObjectReference.builder()
                                                    .name("obj1")
                                                    .value(100)
                                                    .build(),
                                            ObjectReference.builder()
                                                    .name("obj2")
                                                    .value(200)
                                                    .build())))
                            .build(),
                    OptionalExample.builder()
                            .optionalString(Optional.of("optional-value"))
                            .optionalObject(Optional.of(ObjectReference.builder()
                                    .name("optional-obj")
                                    .value(42)
                                    .build()))
                            .optionalCollection(Optional.of(List.of("opt1", "opt2")))
                            .build(),
                    ObjectReference.builder()
                            .name("reference-object")
                            .value(999)
                            .build(),
                    UnionExample.stringVariant("union-string-value"),
                    EnumExample.A,
                    StringAliasEx.of("aliased-string"),
                    OptionalAlias.of(Optional.of("optional-aliased-string")),
                    CollectionAlias.of(List.of("alias1", "alias2", "alias3")),
                    NestedAlias.of(StringAliasEx.of("nested-alias-value")),
                    ExternalExample.builder()
                            .externalLong(456L)
                            .optionalExternal(Optional.of(789L))
                            .build(),
                    AnyExample.builder()
                            .anyValue("any-type-value")
                            .anyMap(Map.of(
                                    "anyKey",
                                    "anyValue",
                                    "anotherKey",
                                    123,
                                    "complexObjectKey",
                                    ObjectReference.builder()
                                            .name("complex1")
                                            .value(1)
                                            .build()))
                            .build(),
                    EmptyObject.of(),
                    ComplexExample.builder()
                            .metadata(Map.of(
                                    StringAliasEx.of("meta1"),
                                            Optional.of(List.of(
                                                    ObjectReference.builder()
                                                            .name("complex1")
                                                            .value(1)
                                                            .build(),
                                                    ObjectReference.builder()
                                                            .name("complex2")
                                                            .value(2)
                                                            .build())),
                                    StringAliasEx.of("meta2"), Optional.empty()))
                            .status(EnumExample.B)
                            .variants(List.of(
                                    UnionExample.intVariant(42),
                                    UnionExample.objectVariant(ObjectReference.builder()
                                            .name("variant-obj")
                                            .value(99)
                                            .build())))
                            .external(Optional.of(999L))
                            .build(),
                    SafetyExample.builder()
                            .safeString("safe-string-value")
                            .unsafeDouble(2.718)
                            .build());
        }
        return "hello!";
    }

    @Override
    public Optional<LongAlias> aliasLongEndpoint(AuthHeader _authHeader, Optional<LongAlias> input) {
        return input;
    }

    @Override
    public void complexQueryParameters(
            AuthHeader _authHeader,
            ResourceIdentifier _datasetRid,
            Set<StringAliasExample> _strings,
            Set<Long> _longs,
            Set<Integer> _ints) {
        // nop
    }

    @Override
    public void receiveListOfOptionals(AuthHeader _authHeader, List<Optional<String>> _value) {}

    @Override
    public void receiveSetOfOptionals(AuthHeader _authHeader, Set<Optional<String>> _value) {}

    @Override
    public void receiveListOfStrings(AuthHeader _authHeader, List<String> _value) {}

    interface Streaming extends StreamingOutput, BinaryResponseBody {}
}
