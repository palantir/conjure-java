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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import com.palantir.ri.ResourceIdentifier;
import com.palantir.tokens.auth.BearerToken;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.Set;
import java.util.UUID;

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
        return deserializeBearerToken(Iterables.getOnlyElement(in));
    }

    public static Optional<BearerToken> deserializeOptionalBearerToken(String in) {
        return Optional.of(deserializeBearerToken(in));
    }

    public static Optional<BearerToken> deserializeOptionalBearerToken(Iterable<String> in) {
        if (in == null || Iterables.isEmpty(in)) {
            return Optional.empty();
        }
        return Optional.of(deserializeBearerToken(Iterables.getOnlyElement(in)));
    }

    public static List<BearerToken> deserializeBearerTokenList(Iterable<String> in) {
        if (in == null) {
            return Collections.emptyList();
        }
        ImmutableList.Builder<BearerToken> builder = ImmutableList.builder();
        for (String item : in) {
            builder.add(deserializeBearerToken(item));
        }
        return builder.build();
    }

    public static Set<BearerToken> deserializeBearerTokenSet(Iterable<String> in) {
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

    public static boolean deserializeBoolean(Iterable<String> in) {
        return deserializeBoolean(Iterables.getOnlyElement(in));
    }

    public static Optional<Boolean> deserializeOptionalBoolean(String in) {
        return Optional.of(deserializeBoolean(in));
    }

    public static Optional<Boolean> deserializeOptionalBoolean(Iterable<String> in) {
        if (in == null || Iterables.isEmpty(in)) {
            return Optional.empty();
        }
        return Optional.of(deserializeBoolean(Iterables.getOnlyElement(in)));
    }

    public static List<Boolean> deserializeBooleanList(Iterable<String> in) {
        if (in == null) {
            return Collections.emptyList();
        }
        ImmutableList.Builder<Boolean> builder = ImmutableList.builder();
        for (String item : in) {
            builder.add(deserializeBoolean(item));
        }
        return builder.build();
    }

    public static Set<Boolean> deserializeBooleanSet(Iterable<String> in) {
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

    public static OffsetDateTime deserializeDateTime(Iterable<String> in) {
        return deserializeDateTime(Iterables.getOnlyElement(in));
    }

    public static Optional<OffsetDateTime> deserializeOptionalDateTime(String in) {
        return Optional.of(deserializeDateTime(in));
    }

    public static Optional<OffsetDateTime> deserializeOptionalDateTime(Iterable<String> in) {
        if (in == null || Iterables.isEmpty(in)) {
            return Optional.empty();
        }
        return Optional.of(deserializeDateTime(Iterables.getOnlyElement(in)));
    }

    public static List<OffsetDateTime> deserializeDateTimeList(Iterable<String> in) {
        if (in == null) {
            return Collections.emptyList();
        }
        ImmutableList.Builder<OffsetDateTime> builder = ImmutableList.builder();
        for (String item : in) {
            builder.add(deserializeDateTime(item));
        }
        return builder.build();
    }

    public static Set<OffsetDateTime> deserializeDateTimeSet(Iterable<String> in) {
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

    public static double deserializeDouble(Iterable<String> in) {
        return deserializeDouble(Iterables.getOnlyElement(in));
    }

    public static OptionalDouble deserializeOptionalDouble(String in) {
        return OptionalDouble.of(deserializeDouble(in));
    }

    public static OptionalDouble deserializeOptionalDouble(Iterable<String> in) {
        if (in == null || Iterables.isEmpty(in)) {
            return OptionalDouble.empty();
        }
        return OptionalDouble.of(deserializeDouble(Iterables.getOnlyElement(in)));
    }

    public static List<Double> deserializeDoubleList(Iterable<String> in) {
        if (in == null) {
            return Collections.emptyList();
        }
        ImmutableList.Builder<Double> builder = ImmutableList.builder();
        for (String item : in) {
            builder.add(deserializeDouble(item));
        }
        return builder.build();
    }

    public static Set<Double> deserializeDoubleSet(Iterable<String> in) {
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

    public static int deserializeInteger(Iterable<String> in) {
        return deserializeInteger(Iterables.getOnlyElement(in));
    }

    public static OptionalInt deserializeOptionalInteger(String in) {
        return OptionalInt.of(deserializeInteger(in));
    }

    public static OptionalInt deserializeOptionalInteger(Iterable<String> in) {
        if (in == null || Iterables.isEmpty(in)) {
            return OptionalInt.empty();
        }
        return OptionalInt.of(deserializeInteger(Iterables.getOnlyElement(in)));
    }

    public static List<Integer> deserializeIntegerList(Iterable<String> in) {
        if (in == null) {
            return Collections.emptyList();
        }
        ImmutableList.Builder<Integer> builder = ImmutableList.builder();
        for (String item : in) {
            builder.add(deserializeInteger(item));
        }
        return builder.build();
    }

    public static Set<Integer> deserializeIntegerSet(Iterable<String> in) {
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

    public static ResourceIdentifier deserializeRid(Iterable<String> in) {
        return deserializeRid(Iterables.getOnlyElement(in));
    }

    public static Optional<ResourceIdentifier> deserializeOptionalRid(String in) {
        return Optional.of(deserializeRid(in));
    }

    public static Optional<ResourceIdentifier> deserializeOptionalRid(Iterable<String> in) {
        if (in == null || Iterables.isEmpty(in)) {
            return Optional.empty();
        }
        return Optional.of(deserializeRid(Iterables.getOnlyElement(in)));
    }

    public static List<ResourceIdentifier> deserializeRidList(Iterable<String> in) {
        if (in == null) {
            return Collections.emptyList();
        }
        ImmutableList.Builder<ResourceIdentifier> builder = ImmutableList.builder();
        for (String item : in) {
            builder.add(deserializeRid(item));
        }
        return builder.build();
    }

    public static Set<ResourceIdentifier> deserializeRidSet(Iterable<String> in) {
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

    public static SafeLong deserializeSafeLong(Iterable<String> in) {
        return deserializeSafeLong(Iterables.getOnlyElement(in));
    }

    public static Optional<SafeLong> deserializeOptionalSafeLong(String in) {
        return Optional.of(deserializeSafeLong(in));
    }

    public static Optional<SafeLong> deserializeOptionalSafeLong(Iterable<String> in) {
        if (in == null || Iterables.isEmpty(in)) {
            return Optional.empty();
        }
        return Optional.of(deserializeSafeLong(Iterables.getOnlyElement(in)));
    }

    public static List<SafeLong> deserializeSafeLongList(Iterable<String> in) {
        if (in == null) {
            return Collections.emptyList();
        }
        ImmutableList.Builder<SafeLong> builder = ImmutableList.builder();
        for (String item : in) {
            builder.add(deserializeSafeLong(item));
        }
        return builder.build();
    }

    public static Set<SafeLong> deserializeSafeLongSet(Iterable<String> in) {
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

    public static String deserializeString(Iterable<String> in) {
        return deserializeString(Iterables.getOnlyElement(in));
    }

    public static Optional<String> deserializeOptionalString(String in) {
        return Optional.of(deserializeString(in));
    }

    public static Optional<String> deserializeOptionalString(Iterable<String> in) {
        if (in == null || Iterables.isEmpty(in)) {
            return Optional.empty();
        }
        return Optional.of(deserializeString(Iterables.getOnlyElement(in)));
    }

    public static List<String> deserializeStringList(Iterable<String> in) {
        return in == null ? Collections.emptyList() : ImmutableList.copyOf(in);
    }

    public static Set<String> deserializeStringSet(Iterable<String> in) {
        return in == null ? Collections.emptySet() : ImmutableSet.copyOf(in);
    }

    public static UUID deserializeUuid(String in) {
        try {
            return UUID.fromString(in);
        } catch (RuntimeException ex) {
            throw new SafeIllegalArgumentException("failed to deserialize uuid", ex);
        }
    }

    public static UUID deserializeUuid(Iterable<String> in) {
        return deserializeUuid(Iterables.getOnlyElement(in));
    }

    public static Optional<UUID> deserializeOptionalUuid(String in) {
        return Optional.of(deserializeUuid(in));
    }

    public static Optional<UUID> deserializeOptionalUuid(Iterable<String> in) {
        if (in == null || Iterables.isEmpty(in)) {
            return Optional.empty();
        }
        return Optional.of(deserializeUuid(Iterables.getOnlyElement(in)));
    }

    public static List<UUID> deserializeUuidList(Iterable<String> in) {
        if (in == null) {
            return Collections.emptyList();
        }
        ImmutableList.Builder<UUID> builder = ImmutableList.builder();
        for (String item : in) {
            builder.add(deserializeUuid(item));
        }
        return builder.build();
    }

    public static Set<UUID> deserializeUuidSet(Iterable<String> in) {
        if (in == null) {
            return Collections.emptySet();
        }
        ImmutableSet.Builder<UUID> builder = ImmutableSet.builder();
        for (String item : in) {
            builder.add(deserializeUuid(item));
        }
        return builder.build();
    }
}
