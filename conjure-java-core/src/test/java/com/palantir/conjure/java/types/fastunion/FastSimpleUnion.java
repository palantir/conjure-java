/*
 * (c) Copyright 2026 Palantir Technologies Inc. All rights reserved.
 *
 * Hand-modified copy of jersey.com.palantir.product.SimpleUnion: the only changes are
 * (a) @JsonTypeInfo no longer carries visible=true, and
 * (b) the new @JsonTypeResolver(ConjureUnionTypeResolverBuilder.class) annotation.
 *
 * Everything else, including UnknownWrapper, is byte-for-byte identical to the generated form.
 */

package com.palantir.conjure.java.types.fastunion;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonTypeResolver;
import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.logsafe.Preconditions;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings({"PatternMatchingInstanceof", "UnusedMethod"})
public final class FastSimpleUnion {
    private final Base value;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    private FastSimpleUnion(Base value) {
        this.value = value;
    }

    @JsonValue
    private Base getValue() {
        return value;
    }

    public static FastSimpleUnion foo(String value) {
        return new FastSimpleUnion(new FooWrapper(value));
    }

    public static FastSimpleUnion bar(int value) {
        return new FastSimpleUnion(new BarWrapper(value));
    }

    public static FastSimpleUnion baz(SafeLong value) {
        return new FastSimpleUnion(new BazWrapper(value));
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof FastSimpleUnion && ((FastSimpleUnion) other).value.equals(value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "FastSimpleUnion{value: " + value + '}';
    }

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.EXISTING_PROPERTY,
            property = "type",
            // visible=true REMOVED — ConjureUnionTypeResolverBuilder handles selective weaving.
            defaultImpl = UnknownWrapper.class)
    @JsonTypeResolver(ConjureUnionTypeResolverBuilder.class)
    @JsonSubTypes({
        @JsonSubTypes.Type(FooWrapper.class),
        @JsonSubTypes.Type(BarWrapper.class),
        @JsonSubTypes.Type(BazWrapper.class)
    })
    @JsonIgnoreProperties(ignoreUnknown = true)
    private interface Base {}

    @JsonTypeName("foo")
    private static final class FooWrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private FooWrapper(@JsonSetter("foo") @Nonnull String value) {
            Preconditions.checkNotNull(value, "foo cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "foo";
        }

        @JsonProperty("foo")
        private String getValue() {
            return value;
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof FooWrapper && ((FooWrapper) other).value.equals(value));
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }

        @Override
        public String toString() {
            return "FooWrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("bar")
    private static final class BarWrapper implements Base {
        private final int value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private BarWrapper(@JsonSetter("bar") int value) {
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "bar";
        }

        @JsonProperty("bar")
        private int getValue() {
            return value;
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof BarWrapper && ((BarWrapper) other).value == value);
        }

        @Override
        public int hashCode() {
            return value;
        }

        @Override
        public String toString() {
            return "BarWrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("baz")
    private static final class BazWrapper implements Base {
        private final SafeLong value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private BazWrapper(@JsonSetter("baz") @Nonnull SafeLong value) {
            Preconditions.checkNotNull(value, "baz cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "baz";
        }

        @JsonProperty("baz")
        private SafeLong getValue() {
            return value;
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof BazWrapper && ((BazWrapper) other).value.equals(value));
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }

        @Override
        public String toString() {
            return "BazWrapper{value: " + value + '}';
        }
    }

    private static final class UnknownWrapper implements Base {
        private final String type;
        private final Map<String, Object> value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private UnknownWrapper(@JsonProperty("type") String type) {
            this(type, new HashMap<String, Object>());
        }

        private UnknownWrapper(@Nonnull String type, @Nonnull Map<String, Object> value) {
            Preconditions.checkNotNull(type, "type cannot be null");
            Preconditions.checkNotNull(value, "value cannot be null");
            this.type = type;
            this.value = value;
        }

        @JsonProperty
        private String getType() {
            return type;
        }

        @JsonAnyGetter
        private Map<String, Object> getValue() {
            return value;
        }

        @JsonAnySetter
        private void put(String key, Object val) {
            value.put(key, val);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other
                    || (other instanceof UnknownWrapper
                            && ((UnknownWrapper) other).type.equals(type)
                            && ((UnknownWrapper) other).value.equals(value));
        }

        @Override
        public int hashCode() {
            return 31 * type.hashCode() + value.hashCode();
        }

        @Override
        public String toString() {
            return "UnknownWrapper{type: " + type + ", value: " + value + '}';
        }
    }

    @SuppressWarnings("unused")
    private static FastSimpleUnion unknownForTests(String type, Object value) {
        return new FastSimpleUnion(new UnknownWrapper(type, Collections.singletonMap(type, value)));
    }
}
