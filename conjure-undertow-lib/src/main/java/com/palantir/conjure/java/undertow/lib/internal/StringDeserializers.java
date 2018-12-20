/*
 * (c) Copyright 2018 Palantir Technologies Inc. All rights reserved.
 */

package com.palantir.conjure.java.undertow.lib.internal;

import com.google.common.collect.Iterables;
import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import com.palantir.ri.ResourceIdentifier;
import com.palantir.tokens.auth.BearerToken;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.UUID;

// TODO(nmiyake): figure out exception handling. Currently, throws empty IllegalArgumentException on any failure.
// Should method signatures be changed to include name of parameter or should exception handling be done in generated
// code?
public final class StringDeserializers {

    private StringDeserializers() {}

    // TODO(nmiyake): verify that "any" is not supported

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

    public static ByteBuffer deserializeBinary(String in) {
        try {
            return ByteBuffer.wrap(in.getBytes(StandardCharsets.UTF_8));
        } catch (RuntimeException ex) {
            throw new SafeIllegalArgumentException("failed to deserialize binary", ex);
        }
    }

    public static ByteBuffer deserializeBinary(Iterable<String> in) {
        return deserializeBinary(Iterables.getOnlyElement(in));
    }

    public static Optional<ByteBuffer> deserializeOptionalBinary(String in) {
        return Optional.of(deserializeBinary(in));
    }

    public static Optional<ByteBuffer> deserializeOptionalBinary(Iterable<String> in) {
        if (in == null || Iterables.isEmpty(in)) {
            return Optional.empty();
        }
        return Optional.of(deserializeBinary(Iterables.getOnlyElement(in)));
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
}
