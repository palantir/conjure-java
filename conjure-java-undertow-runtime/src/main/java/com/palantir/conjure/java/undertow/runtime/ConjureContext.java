/*
 * (c) Copyright 2019 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java.undertow.runtime;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.reflect.TypeToken;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.conjure.java.undertow.lib.BinaryResponseBody;
import com.palantir.conjure.java.undertow.lib.ServiceContext;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.UnsafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import com.palantir.ri.ResourceIdentifier;
import com.palantir.tokens.auth.AuthHeader;
import com.palantir.tokens.auth.BearerToken;
import com.palantir.tokens.auth.UnverifiedJsonWebToken;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderValues;
import io.undertow.util.Headers;
import java.io.IOException;
import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.annotation.Nullable;
import org.slf4j.MDC;

/**
 * {@link ConjureContext} provides state required by generated handlers.
 */
public final class ConjureContext implements ServiceContext {

    private final SerializerRegistry serializerRegistry;
    private final ServiceInstrumenter serviceInstrumenter;

    private ConjureContext(Builder builder) {
        this.serializerRegistry = Preconditions.checkNotNull(builder.serializerRegistry,
                "Missing required SerializerRegistry");
        this.serviceInstrumenter = Preconditions.checkNotNull(builder.serviceInstrumenter,
                "Missing required ServiceInstrumenter");
    }

    @Override
    public void serialize(Object value, HttpServerExchange exchange) throws IOException {
        serializerRegistry.serialize(value, exchange);
    }

    @Override
    public void serialize(BinaryResponseBody value, HttpServerExchange exchange) throws IOException {
        BinarySerializers.serialize(value, exchange);
    }

    @Override
    public <T> T deserialize(TypeToken<T> type, HttpServerExchange exchange) throws IOException {
        return serializerRegistry.deserialize(type, exchange);
    }

    @Override
    public InputStream deserializeInputStream(HttpServerExchange exchange) {
        return BinarySerializers.deserializeInputStream(exchange);
    }

    /**
     * Parses an {@link AuthHeader} from the provided {@link HttpServerExchange} and applies
     * {@link UnverifiedJsonWebToken} information to the {@link MDC thread state} if present.
     */
    @Override
    public AuthHeader authHeader(HttpServerExchange exchange) {
        HeaderValues authorization = exchange.getRequestHeaders().get(Headers.AUTHORIZATION);
        // Do not use Iterables.getOnlyElement because it includes values in the exception message.
        // We do not want credential material logged to disk, even if it's marked unsafe.
        Preconditions.checkArgument(authorization != null && authorization.size() == 1,
                "One Authorization header value is required");
        return setState(exchange, AuthHeader.valueOf(authorization.get(0)));
    }

    /**
     * Parses a {@link BearerToken} from the provided {@link HttpServerExchange} and applies
     * {@link UnverifiedJsonWebToken} information to the {@link MDC thread state} if present.
     */
    @Override
    public BearerToken authCookie(HttpServerExchange exchange, String cookieName) {
        return setState(exchange, deserializeBearerToken(exchange.getRequestCookies().get(cookieName).getValue()));
    }

    private static final String USER_ID_KEY = "userId";
    private static final String SESSION_ID_KEY = "sessionId";
    private static final String TOKEN_ID_KEY = "tokenId";
    private static Consumer<String> sessionIdSetter = sessionId -> MDC.put(SESSION_ID_KEY, sessionId);
    private static Consumer<String> tokenIdSetter = tokenId -> MDC.put(TOKEN_ID_KEY, tokenId);

    /**
     * Attempts to extract a {@link UnverifiedJsonWebToken JSON Web Token} from the
     * {@link BearerToken} value, and populates the SLF4J {@link MDC} with
     * user id, session id, and token id extracted from the JWT. This is
     * best-effort and does not throw an exception in case any of these steps fail.
     */
    private static BearerToken setState(HttpServerExchange exchange, BearerToken token) {
        Optional<UnverifiedJsonWebToken> parsedJwt = UnverifiedJsonWebToken.tryParse(token.getToken());
        exchange.putAttachment(Attachments.UNVERIFIED_JWT, parsedJwt);
        if (parsedJwt.isPresent()) {
            UnverifiedJsonWebToken jwt = parsedJwt.get();
            MDC.put(USER_ID_KEY, jwt.getUnverifiedUserId());
            jwt.getUnverifiedSessionId().ifPresent(sessionIdSetter);
            jwt.getUnverifiedTokenId().ifPresent(tokenIdSetter);
        }
        return token;
    }

    private static AuthHeader setState(HttpServerExchange exchange, AuthHeader authHeader) {
        setState(exchange, authHeader.getBearerToken());
        return authHeader;
    }

    @Override
    public <T> T instrument(T serviceImplementation, Class<T> serviceInterface) {
        return serviceInstrumenter.instrument(serviceImplementation, serviceInterface);
    }

    @Override
    public BearerToken deserializeBearerToken(String in) {
        try {
            return BearerToken.valueOf(in);
        } catch (RuntimeException ex) {
            throw new SafeIllegalArgumentException("failed to deserialize bearertoken", ex);
        }
    }

    @Override
    public BearerToken deserializeBearerToken(Iterable<String> in) {
        // BearerToken values should never be logged
        return deserializeBearerToken(getOnlyElementDoNotLogValues(in));
    }

    @Override
    public Optional<BearerToken> deserializeOptionalBearerToken(@Nullable String in) {
        if (Strings.isNullOrEmpty(in)) {
            return Optional.empty();
        }
        return Optional.of(deserializeBearerToken(in));
    }

    @Override
    public Optional<BearerToken> deserializeOptionalBearerToken(@Nullable Iterable<String> in) {
        if (in == null || Iterables.isEmpty(in)) {
            return Optional.empty();
        }
        // BearerToken values should never be logged
        return Optional.of(deserializeBearerToken(getOnlyElementDoNotLogValues(in)));
    }

    @Override
    public List<BearerToken> deserializeBearerTokenList(@Nullable Iterable<String> in) {
        if (in == null) {
            return Collections.emptyList();
        }
        ImmutableList.Builder<BearerToken> builder = ImmutableList.builder();
        for (String item : in) {
            builder.add(deserializeBearerToken(item));
        }
        return builder.build();
    }

    @Override
    public Set<BearerToken> deserializeBearerTokenSet(@Nullable Iterable<String> in) {
        if (in == null) {
            return Collections.emptySet();
        }
        ImmutableSet.Builder<BearerToken> builder = ImmutableSet.builder();
        for (String item : in) {
            builder.add(deserializeBearerToken(item));
        }
        return builder.build();
    }

    @Override
    public boolean deserializeBoolean(String in) {
        try {
            return Boolean.parseBoolean(in);
        } catch (RuntimeException ex) {
            throw new SafeIllegalArgumentException("failed to deserialize boolean", ex);
        }
    }

    @Override
    public boolean deserializeBoolean(@Nullable Iterable<String> in) {
        return deserializeBoolean(getOnlyElement(in));
    }

    @Override
    public Optional<Boolean> deserializeOptionalBoolean(String in) {
        return Optional.of(deserializeBoolean(in));
    }

    @Override
    public Optional<Boolean> deserializeOptionalBoolean(@Nullable Iterable<String> in) {
        if (in == null || Iterables.isEmpty(in)) {
            return Optional.empty();
        }
        return Optional.of(deserializeBoolean(getOnlyElement(in)));
    }

    @Override
    public List<Boolean> deserializeBooleanList(@Nullable Iterable<String> in) {
        if (in == null) {
            return Collections.emptyList();
        }
        ImmutableList.Builder<Boolean> builder = ImmutableList.builder();
        for (String item : in) {
            builder.add(deserializeBoolean(item));
        }
        return builder.build();
    }

    @Override
    public Set<Boolean> deserializeBooleanSet(@Nullable Iterable<String> in) {
        if (in == null) {
            return Collections.emptySet();
        }
        ImmutableSet.Builder<Boolean> builder = ImmutableSet.builder();
        for (String item : in) {
            builder.add(deserializeBoolean(item));
        }
        return builder.build();
    }

    @Override
    public OffsetDateTime deserializeDateTime(String in) {
        try {
            return OffsetDateTime.parse(in);
        } catch (RuntimeException ex) {
            throw new SafeIllegalArgumentException("failed to deserialize datetime", ex);
        }
    }

    @Override
    public OffsetDateTime deserializeDateTime(@Nullable Iterable<String> in) {
        return deserializeDateTime(getOnlyElement(in));
    }

    @Override
    public Optional<OffsetDateTime> deserializeOptionalDateTime(String in) {
        return Optional.of(deserializeDateTime(in));
    }

    @Override
    public Optional<OffsetDateTime> deserializeOptionalDateTime(@Nullable Iterable<String> in) {
        if (in == null || Iterables.isEmpty(in)) {
            return Optional.empty();
        }
        return Optional.of(deserializeDateTime(getOnlyElement(in)));
    }

    @Override
    public List<OffsetDateTime> deserializeDateTimeList(@Nullable Iterable<String> in) {
        if (in == null) {
            return Collections.emptyList();
        }
        ImmutableList.Builder<OffsetDateTime> builder = ImmutableList.builder();
        for (String item : in) {
            builder.add(deserializeDateTime(item));
        }
        return builder.build();
    }

    @Override
    public Set<OffsetDateTime> deserializeDateTimeSet(@Nullable Iterable<String> in) {
        if (in == null) {
            return Collections.emptySet();
        }
        ImmutableSet.Builder<OffsetDateTime> builder = ImmutableSet.builder();
        for (String item : in) {
            builder.add(deserializeDateTime(item));
        }
        return builder.build();
    }

    @Override
    public double deserializeDouble(String in) {
        try {
            return Double.parseDouble(in);
        } catch (RuntimeException ex) {
            throw new SafeIllegalArgumentException("failed to deserialize double", ex);
        }
    }

    @Override
    public double deserializeDouble(@Nullable Iterable<String> in) {
        return deserializeDouble(getOnlyElement(in));
    }

    @Override
    public OptionalDouble deserializeOptionalDouble(String in) {
        return OptionalDouble.of(deserializeDouble(in));
    }

    @Override
    public OptionalDouble deserializeOptionalDouble(@Nullable Iterable<String> in) {
        if (in == null || Iterables.isEmpty(in)) {
            return OptionalDouble.empty();
        }
        return OptionalDouble.of(deserializeDouble(getOnlyElement(in)));
    }

    @Override
    public List<Double> deserializeDoubleList(@Nullable Iterable<String> in) {
        if (in == null) {
            return Collections.emptyList();
        }
        ImmutableList.Builder<Double> builder = ImmutableList.builder();
        for (String item : in) {
            builder.add(deserializeDouble(item));
        }
        return builder.build();
    }

    @Override
    public Set<Double> deserializeDoubleSet(@Nullable Iterable<String> in) {
        if (in == null) {
            return Collections.emptySet();
        }
        ImmutableSet.Builder<Double> builder = ImmutableSet.builder();
        for (String item : in) {
            builder.add(deserializeDouble(item));
        }
        return builder.build();
    }

    @Override
    public int deserializeInteger(String in) {
        try {
            return Integer.parseInt(in);
        } catch (RuntimeException ex) {
            throw new SafeIllegalArgumentException("failed to deserialize integer", ex);
        }
    }

    @Override
    public int deserializeInteger(@Nullable Iterable<String> in) {
        return deserializeInteger(getOnlyElement(in));
    }

    @Override
    public OptionalInt deserializeOptionalInteger(String in) {
        return OptionalInt.of(deserializeInteger(in));
    }

    @Override
    public OptionalInt deserializeOptionalInteger(@Nullable Iterable<String> in) {
        if (in == null || Iterables.isEmpty(in)) {
            return OptionalInt.empty();
        }
        return OptionalInt.of(deserializeInteger(getOnlyElement(in)));
    }

    @Override
    public List<Integer> deserializeIntegerList(@Nullable Iterable<String> in) {
        if (in == null) {
            return Collections.emptyList();
        }
        ImmutableList.Builder<Integer> builder = ImmutableList.builder();
        for (String item : in) {
            builder.add(deserializeInteger(item));
        }
        return builder.build();
    }

    @Override
    public Set<Integer> deserializeIntegerSet(@Nullable Iterable<String> in) {
        if (in == null) {
            return Collections.emptySet();
        }
        ImmutableSet.Builder<Integer> builder = ImmutableSet.builder();
        for (String item : in) {
            builder.add(deserializeInteger(item));
        }
        return builder.build();
    }

    @Override
    public ResourceIdentifier deserializeRid(String in) {
        try {
            return ResourceIdentifier.valueOf(in);
        } catch (RuntimeException ex) {
            throw new SafeIllegalArgumentException("failed to deserialize rid", ex);
        }
    }

    @Override
    public ResourceIdentifier deserializeRid(@Nullable Iterable<String> in) {
        return deserializeRid(getOnlyElement(in));
    }

    @Override
    public Optional<ResourceIdentifier> deserializeOptionalRid(String in) {
        return Optional.of(deserializeRid(in));
    }

    @Override
    public Optional<ResourceIdentifier> deserializeOptionalRid(@Nullable Iterable<String> in) {
        if (in == null || Iterables.isEmpty(in)) {
            return Optional.empty();
        }
        return Optional.of(deserializeRid(getOnlyElement(in)));
    }

    @Override
    public List<ResourceIdentifier> deserializeRidList(@Nullable Iterable<String> in) {
        if (in == null) {
            return Collections.emptyList();
        }
        ImmutableList.Builder<ResourceIdentifier> builder = ImmutableList.builder();
        for (String item : in) {
            builder.add(deserializeRid(item));
        }
        return builder.build();
    }

    @Override
    public Set<ResourceIdentifier> deserializeRidSet(@Nullable Iterable<String> in) {
        if (in == null) {
            return Collections.emptySet();
        }
        ImmutableSet.Builder<ResourceIdentifier> builder = ImmutableSet.builder();
        for (String item : in) {
            builder.add(deserializeRid(item));
        }
        return builder.build();
    }

    @Override
    public SafeLong deserializeSafeLong(String in) {
        try {
            return SafeLong.valueOf(in);
        } catch (RuntimeException ex) {
            throw new SafeIllegalArgumentException("failed to deserialize safelong", ex);
        }
    }

    @Override
    public SafeLong deserializeSafeLong(@Nullable Iterable<String> in) {
        return deserializeSafeLong(getOnlyElement(in));
    }

    @Override
    public Optional<SafeLong> deserializeOptionalSafeLong(String in) {
        return Optional.of(deserializeSafeLong(in));
    }

    @Override
    public Optional<SafeLong> deserializeOptionalSafeLong(@Nullable Iterable<String> in) {
        if (in == null || Iterables.isEmpty(in)) {
            return Optional.empty();
        }
        return Optional.of(deserializeSafeLong(getOnlyElement(in)));
    }

    @Override
    public List<SafeLong> deserializeSafeLongList(@Nullable Iterable<String> in) {
        if (in == null) {
            return Collections.emptyList();
        }
        ImmutableList.Builder<SafeLong> builder = ImmutableList.builder();
        for (String item : in) {
            builder.add(deserializeSafeLong(item));
        }
        return builder.build();
    }

    @Override
    public Set<SafeLong> deserializeSafeLongSet(@Nullable Iterable<String> in) {
        if (in == null) {
            return Collections.emptySet();
        }
        ImmutableSet.Builder<SafeLong> builder = ImmutableSet.builder();
        for (String item : in) {
            builder.add(deserializeSafeLong(item));
        }
        return builder.build();
    }

    @Override
    public String deserializeString(String in) {
        return in;
    }

    @Override
    public String deserializeString(@Nullable Iterable<String> in) {
        return deserializeString(getOnlyElement(in));
    }

    @Override
    public Optional<String> deserializeOptionalString(String in) {
        return Optional.of(deserializeString(in));
    }

    @Override
    public Optional<String> deserializeOptionalString(@Nullable Iterable<String> in) {
        if (in == null || Iterables.isEmpty(in)) {
            return Optional.empty();
        }
        return Optional.of(deserializeString(getOnlyElement(in)));
    }

    @Override
    public List<String> deserializeStringList(@Nullable Iterable<String> in) {
        return in == null ? Collections.emptyList() : ImmutableList.copyOf(in);
    }

    @Override
    public Set<String> deserializeStringSet(@Nullable Iterable<String> in) {
        return in == null ? Collections.emptySet() : ImmutableSet.copyOf(in);
    }

    @Override
    public UUID deserializeUuid(String in) {
        try {
            return UUID.fromString(in);
        } catch (RuntimeException ex) {
            throw new SafeIllegalArgumentException("failed to deserialize uuid", ex);
        }
    }

    @Override
    public UUID deserializeUuid(@Nullable Iterable<String> in) {
        return deserializeUuid(getOnlyElement(in));
    }

    @Override
    public Optional<UUID> deserializeOptionalUuid(String in) {
        return Optional.of(deserializeUuid(in));
    }

    @Override
    public Optional<UUID> deserializeOptionalUuid(@Nullable Iterable<String> in) {
        if (in == null || Iterables.isEmpty(in)) {
            return Optional.empty();
        }
        return Optional.of(deserializeUuid(getOnlyElement(in)));
    }

    @Override
    public List<UUID> deserializeUuidList(@Nullable Iterable<String> in) {
        if (in == null) {
            return Collections.emptyList();
        }
        ImmutableList.Builder<UUID> builder = ImmutableList.builder();
        for (String item : in) {
            builder.add(deserializeUuid(item));
        }
        return builder.build();
    }

    @Override
    public Set<UUID> deserializeUuidSet(@Nullable Iterable<String> in) {
        if (in == null) {
            return Collections.emptySet();
        }
        ImmutableSet.Builder<UUID> builder = ImmutableSet.builder();
        for (String item : in) {
            builder.add(deserializeUuid(item));
        }
        return builder.build();
    }

    @Override
    public <T> T deserializeComplex(String in, Function<String, T> factory) {
        return factory.apply(deserializeString(in));
    }

    @Override
    public <T> T deserializeComplex(@Nullable Iterable<String> in, Function<String, T> factory) {
        return factory.apply(deserializeString(in));
    }

    @Override
    public <T> Optional<T> deserializeOptionalComplex(
            @Nullable Iterable<String> in, Function<String, T> factory) {
        return deserializeOptionalString(in).map(factory);
    }

    @Override
    public <T> List<T> deserializeComplexList(@Nullable Iterable<String> in, Function<String, T> factory) {
        if (in == null) {
            return Collections.emptyList();
        }
        ImmutableList.Builder<T> builder = ImmutableList.builder();
        for (String item : in) {
            builder.add(deserializeComplex(item, factory));
        }
        return builder.build();
    }

    @Override
    public <T> Set<T> deserializeComplexSet(@Nullable Iterable<String> in, Function<String, T> factory) {
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

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private SerializerRegistry serializerRegistry;
        private ServiceInstrumenter serviceInstrumenter = new ServiceInstrumenter() {
            @Override
            public <T> T instrument(T serviceImplementation, Class<T> serviceInterface) {
                return serviceImplementation;
            }
        };

        private Builder() {}

        @CanIgnoreReturnValue
        public Builder serializerRegistry(SerializerRegistry value) {
            this.serializerRegistry = Preconditions.checkNotNull(value, "Value is required");
            return this;
        }

        @CanIgnoreReturnValue
        public Builder serviceInstrumenter(ServiceInstrumenter value) {
            this.serviceInstrumenter = Preconditions.checkNotNull(value, "Value is required");
            return this;
        }

        public ConjureContext build() {
            return new ConjureContext(this);
        }
    }
}
