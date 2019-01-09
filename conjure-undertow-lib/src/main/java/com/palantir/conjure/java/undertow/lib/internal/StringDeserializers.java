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

package com.palantir.conjure.java.undertow.lib.internal;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.UnsafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import com.palantir.ri.ResourceIdentifier;
import com.palantir.tokens.auth.BearerToken;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import javax.annotation.Nullable;

// TODO(nmiyake): figure out exception handling. Currently, throws empty IllegalArgumentException on any failure.
// Should method signatures be changed to include name of parameter or should exception handling be done in generated
// code?
public final class StringDeserializers {

    private StringDeserializers() {}

    public static BearerToken deserializeBearerToken(String in) {
        try {
            return BearerToken.valueOf(in);
        } catch (RuntimeException ex) {
            throw new SafeIllegalArgumentException("failed to deserialize bearertoken", ex);
        }
    }

    public static BearerToken deserializeBearerToken(Iterable<String> in) {
        // BearerToken values should never be logged
        return deserializeBearerToken(getOnlyElementDoNotLogValues(in));
    }

    public static Optional<BearerToken> deserializeOptionalBearerToken(@Nullable String in) {
        if (Strings.isNullOrEmpty(in)) {
            return Optional.empty();
        }
        return Optional.of(deserializeBearerToken(in));
    }

    public static Optional<BearerToken> deserializeOptionalBearerToken(@Nullable Iterable<String> in) {
        if (in == null || Iterables.isEmpty(in)) {
            return Optional.empty();
        }
        // BearerToken values should never be logged
        return Optional.of(deserializeBearerToken(getOnlyElementDoNotLogValues(in)));
    }

    public static List<BearerToken> deserializeBearerTokenList(@Nullable Iterable<String> in) {
        if (in == null) {
            return Collections.emptyList();
        }
        ImmutableList.Builder<BearerToken> builder = ImmutableList.builder();
        for (String item : in) {
            builder.add(deserializeBearerToken(item));
        }
        return builder.build();
    }

    public static Set<BearerToken> deserializeBearerTokenSet(@Nullable Iterable<String> in) {
        if (in == null) {
            return Collections.emptySet();
        }
        ImmutableSet.Builder<BearerToken> builder = ImmutableSet.builder();
        for (String item : in) {
            builder.add(deserializeBearerToken(item));
        }
        return builder.build();
    }

    public static boolean deserializeBoolean(String in) {
        try {
            return Boolean.parseBoolean(in);
        } catch (RuntimeException ex) {
            throw new SafeIllegalArgumentException("failed to deserialize boolean", ex);
        }
    }

    public static boolean deserializeBoolean(@Nullable Iterable<String> in) {
        return deserializeBoolean(getOnlyElement(in));
    }

    public static Optional<Boolean> deserializeOptionalBoolean(String in) {
        return Optional.of(deserializeBoolean(in));
    }

    public static Optional<Boolean> deserializeOptionalBoolean(@Nullable Iterable<String> in) {
        if (in == null || Iterables.isEmpty(in)) {
            return Optional.empty();
        }
        return Optional.of(deserializeBoolean(getOnlyElement(in)));
    }

    public static List<Boolean> deserializeBooleanList(@Nullable Iterable<String> in) {
        if (in == null) {
            return Collections.emptyList();
        }
        ImmutableList.Builder<Boolean> builder = ImmutableList.builder();
        for (String item : in) {
            builder.add(deserializeBoolean(item));
        }
        return builder.build();
    }

    public static Set<Boolean> deserializeBooleanSet(@Nullable Iterable<String> in) {
        if (in == null) {
            return Collections.emptySet();
        }
        ImmutableSet.Builder<Boolean> builder = ImmutableSet.builder();
        for (String item : in) {
            builder.add(deserializeBoolean(item));
        }
        return builder.build();
    }

    public static OffsetDateTime deserializeDateTime(String in) {
        try {
            return OffsetDateTime.parse(in);
        } catch (RuntimeException ex) {
            throw new SafeIllegalArgumentException("failed to deserialize datetime", ex);
        }
    }

    public static OffsetDateTime deserializeDateTime(@Nullable Iterable<String> in) {
        return deserializeDateTime(getOnlyElement(in));
    }

    public static Optional<OffsetDateTime> deserializeOptionalDateTime(String in) {
        return Optional.of(deserializeDateTime(in));
    }

    public static Optional<OffsetDateTime> deserializeOptionalDateTime(@Nullable Iterable<String> in) {
        if (in == null || Iterables.isEmpty(in)) {
            return Optional.empty();
        }
        return Optional.of(deserializeDateTime(getOnlyElement(in)));
    }

    public static List<OffsetDateTime> deserializeDateTimeList(@Nullable Iterable<String> in) {
        if (in == null) {
            return Collections.emptyList();
        }
        ImmutableList.Builder<OffsetDateTime> builder = ImmutableList.builder();
        for (String item : in) {
            builder.add(deserializeDateTime(item));
        }
        return builder.build();
    }

    public static Set<OffsetDateTime> deserializeDateTimeSet(@Nullable Iterable<String> in) {
        if (in == null) {
            return Collections.emptySet();
        }
        ImmutableSet.Builder<OffsetDateTime> builder = ImmutableSet.builder();
        for (String item : in) {
            builder.add(deserializeDateTime(item));
        }
        return builder.build();
    }

    public static double deserializeDouble(String in) {
        try {
            return Double.parseDouble(in);
        } catch (RuntimeException ex) {
            throw new SafeIllegalArgumentException("failed to deserialize double", ex);
        }
    }

    public static double deserializeDouble(@Nullable Iterable<String> in) {
        return deserializeDouble(getOnlyElement(in));
    }

    public static OptionalDouble deserializeOptionalDouble(String in) {
        return OptionalDouble.of(deserializeDouble(in));
    }

    public static OptionalDouble deserializeOptionalDouble(@Nullable Iterable<String> in) {
        if (in == null || Iterables.isEmpty(in)) {
            return OptionalDouble.empty();
        }
        return OptionalDouble.of(deserializeDouble(getOnlyElement(in)));
    }

    public static List<Double> deserializeDoubleList(@Nullable Iterable<String> in) {
        if (in == null) {
            return Collections.emptyList();
        }
        ImmutableList.Builder<Double> builder = ImmutableList.builder();
        for (String item : in) {
            builder.add(deserializeDouble(item));
        }
        return builder.build();
    }

    public static Set<Double> deserializeDoubleSet(@Nullable Iterable<String> in) {
        if (in == null) {
            return Collections.emptySet();
        }
        ImmutableSet.Builder<Double> builder = ImmutableSet.builder();
        for (String item : in) {
            builder.add(deserializeDouble(item));
        }
        return builder.build();
    }

    public static int deserializeInteger(String in) {
        try {
            return Integer.parseInt(in);
        } catch (RuntimeException ex) {
            throw new SafeIllegalArgumentException("failed to deserialize integer", ex);
        }
    }

    public static int deserializeInteger(@Nullable Iterable<String> in) {
        return deserializeInteger(getOnlyElement(in));
    }

    public static OptionalInt deserializeOptionalInteger(String in) {
        return OptionalInt.of(deserializeInteger(in));
    }

    public static OptionalInt deserializeOptionalInteger(@Nullable Iterable<String> in) {
        if (in == null || Iterables.isEmpty(in)) {
            return OptionalInt.empty();
        }
        return OptionalInt.of(deserializeInteger(getOnlyElement(in)));
    }

    public static List<Integer> deserializeIntegerList(@Nullable Iterable<String> in) {
        if (in == null) {
            return Collections.emptyList();
        }
        ImmutableList.Builder<Integer> builder = ImmutableList.builder();
        for (String item : in) {
            builder.add(deserializeInteger(item));
        }
        return builder.build();
    }

    public static Set<Integer> deserializeIntegerSet(@Nullable Iterable<String> in) {
        if (in == null) {
            return Collections.emptySet();
        }
        ImmutableSet.Builder<Integer> builder = ImmutableSet.builder();
        for (String item : in) {
            builder.add(deserializeInteger(item));
        }
        return builder.build();
    }

    public static ResourceIdentifier deserializeRid(String in) {
        try {
            return ResourceIdentifier.valueOf(in);
        } catch (RuntimeException ex) {
            throw new SafeIllegalArgumentException("failed to deserialize rid", ex);
        }
    }

    public static ResourceIdentifier deserializeRid(@Nullable Iterable<String> in) {
        return deserializeRid(getOnlyElement(in));
    }

    public static Optional<ResourceIdentifier> deserializeOptionalRid(String in) {
        return Optional.of(deserializeRid(in));
    }

    public static Optional<ResourceIdentifier> deserializeOptionalRid(@Nullable Iterable<String> in) {
        if (in == null || Iterables.isEmpty(in)) {
            return Optional.empty();
        }
        return Optional.of(deserializeRid(getOnlyElement(in)));
    }

    public static List<ResourceIdentifier> deserializeRidList(@Nullable Iterable<String> in) {
        if (in == null) {
            return Collections.emptyList();
        }
        ImmutableList.Builder<ResourceIdentifier> builder = ImmutableList.builder();
        for (String item : in) {
            builder.add(deserializeRid(item));
        }
        return builder.build();
    }

    public static Set<ResourceIdentifier> deserializeRidSet(@Nullable Iterable<String> in) {
        if (in == null) {
            return Collections.emptySet();
        }
        ImmutableSet.Builder<ResourceIdentifier> builder = ImmutableSet.builder();
        for (String item : in) {
            builder.add(deserializeRid(item));
        }
        return builder.build();
    }

    public static SafeLong deserializeSafeLong(String in) {
        try {
            return SafeLong.valueOf(in);
        } catch (RuntimeException ex) {
            throw new SafeIllegalArgumentException("failed to deserialize safelong", ex);
        }
    }

    public static SafeLong deserializeSafeLong(@Nullable Iterable<String> in) {
        return deserializeSafeLong(getOnlyElement(in));
    }

    public static Optional<SafeLong> deserializeOptionalSafeLong(String in) {
        return Optional.of(deserializeSafeLong(in));
    }

    public static Optional<SafeLong> deserializeOptionalSafeLong(@Nullable Iterable<String> in) {
        if (in == null || Iterables.isEmpty(in)) {
            return Optional.empty();
        }
        return Optional.of(deserializeSafeLong(getOnlyElement(in)));
    }

    public static List<SafeLong> deserializeSafeLongList(@Nullable Iterable<String> in) {
        if (in == null) {
            return Collections.emptyList();
        }
        ImmutableList.Builder<SafeLong> builder = ImmutableList.builder();
        for (String item : in) {
            builder.add(deserializeSafeLong(item));
        }
        return builder.build();
    }

    public static Set<SafeLong> deserializeSafeLongSet(@Nullable Iterable<String> in) {
        if (in == null) {
            return Collections.emptySet();
        }
        ImmutableSet.Builder<SafeLong> builder = ImmutableSet.builder();
        for (String item : in) {
            builder.add(deserializeSafeLong(item));
        }
        return builder.build();
    }

    public static String deserializeString(String in) {
        return in;
    }

    public static String deserializeString(@Nullable Iterable<String> in) {
        return deserializeString(getOnlyElement(in));
    }

    public static Optional<String> deserializeOptionalString(String in) {
        return Optional.of(deserializeString(in));
    }

    public static Optional<String> deserializeOptionalString(@Nullable Iterable<String> in) {
        if (in == null || Iterables.isEmpty(in)) {
            return Optional.empty();
        }
        return Optional.of(deserializeString(getOnlyElement(in)));
    }

    public static List<String> deserializeStringList(@Nullable Iterable<String> in) {
        return in == null ? Collections.emptyList() : ImmutableList.copyOf(in);
    }

    public static Set<String> deserializeStringSet(@Nullable Iterable<String> in) {
        return in == null ? Collections.emptySet() : ImmutableSet.copyOf(in);
    }

    public static UUID deserializeUuid(String in) {
        try {
            return UUID.fromString(in);
        } catch (RuntimeException ex) {
            throw new SafeIllegalArgumentException("failed to deserialize uuid", ex);
        }
    }

    public static UUID deserializeUuid(@Nullable Iterable<String> in) {
        return deserializeUuid(getOnlyElement(in));
    }

    public static Optional<UUID> deserializeOptionalUuid(String in) {
        return Optional.of(deserializeUuid(in));
    }

    public static Optional<UUID> deserializeOptionalUuid(@Nullable Iterable<String> in) {
        if (in == null || Iterables.isEmpty(in)) {
            return Optional.empty();
        }
        return Optional.of(deserializeUuid(getOnlyElement(in)));
    }

    public static List<UUID> deserializeUuidList(@Nullable Iterable<String> in) {
        if (in == null) {
            return Collections.emptyList();
        }
        ImmutableList.Builder<UUID> builder = ImmutableList.builder();
        for (String item : in) {
            builder.add(deserializeUuid(item));
        }
        return builder.build();
    }

    public static Set<UUID> deserializeUuidSet(@Nullable Iterable<String> in) {
        if (in == null) {
            return Collections.emptySet();
        }
        ImmutableSet.Builder<UUID> builder = ImmutableSet.builder();
        for (String item : in) {
            builder.add(deserializeUuid(item));
        }
        return builder.build();
    }

    public static <T> T deserializeComplex(String in, Function<String, T> factory) {
        return factory.apply(deserializeString(in));
    }

    public static <T> T deserializeComplex(@Nullable Iterable<String> in, Function<String, T> factory) {
        return factory.apply(deserializeString(in));
    }

    public static <T> Optional<T> deserializeOptionalComplex(
            @Nullable Iterable<String> in, Function<String, T> factory) {
        return deserializeOptionalString(in).map(factory);
    }

    public static <T> List<T> deserializeComplexList(@Nullable Iterable<String> in, Function<String, T> factory) {
        if (in == null) {
            return Collections.emptyList();
        }
        ImmutableList.Builder<T> builder = ImmutableList.builder();
        for (String item : in) {
            builder.add(deserializeComplex(item, factory));
        }
        return builder.build();
    }

    public static <T> Set<T> deserializeComplexSet(@Nullable Iterable<String> in, Function<String, T> factory) {
        if (in == null) {
            return Collections.emptySet();
        }
        ImmutableSet.Builder<T> builder = ImmutableSet.builder();
        for (String item : in) {
            builder.add(deserializeComplex(item, factory));
        }
        return builder.build();
    }

    private static <T> T getOnlyElement(@Nullable Iterable<T> input) {
        return getOnlyElementInternal(input, true);
    }

    private static <T> T getOnlyElementDoNotLogValues(@Nullable Iterable<T> input) {
        return getOnlyElementInternal(input, false);
    }

    private static <T> T getOnlyElementInternal(@Nullable Iterable<T> input, boolean includeValues) {
        if (input == null) {
            throw new SafeIllegalArgumentException("Expected one element but received null");
        }
        Iterator<T> iterator = input.iterator();
        if (!iterator.hasNext()) {
            throw new SafeIllegalArgumentException("Expected one element but received none");
        }
        T first = iterator.next();
        if (!iterator.hasNext()) {
            return first;
        }
        int size = Iterables.size(input);
        if (includeValues) {
            throw new SafeIllegalArgumentException("Expected one element",
                    SafeArg.of("size", size), UnsafeArg.of("received", input));
        } else {
            throw new SafeIllegalArgumentException("Expected one element", SafeArg.of("size", size));
        }
    }
}
