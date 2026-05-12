/*
 * (c) Copyright 2026 Palantir Technologies Inc. All rights reserved.
 *
 * Hand-modified copy of sealedunions.com.palantir.product.SimpleUnion (the sealed-class form):
 *   - @JsonTypeInfo no longer carries visible=true
 *   - new @JsonTypeResolver(ConjureUnionTypeResolverBuilder.class) annotation added
 * Visitor/builder boilerplate trimmed for test focus; deserialization surface is identical.
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
import com.fasterxml.jackson.databind.annotation.JsonTypeResolver;
import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.logsafe.Preconditions;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        // visible=true REMOVED — ConjureUnionTypeResolverBuilder handles selective weaving.
        defaultImpl = FastSealedSimpleUnion.Unknown.class)
@JsonTypeResolver(ConjureUnionTypeResolverBuilder.class)
@JsonSubTypes({
    @JsonSubTypes.Type(value = FastSealedSimpleUnion.Foo.class, name = "foo"),
    @JsonSubTypes.Type(value = FastSealedSimpleUnion.Bar.class, name = "bar"),
    @JsonSubTypes.Type(value = FastSealedSimpleUnion.Baz.class, name = "baz")
})
@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings({"PatternMatchingInstanceof", "UnusedMethod"})
public abstract sealed class FastSealedSimpleUnion
        permits FastSealedSimpleUnion.Foo,
                FastSealedSimpleUnion.Bar,
                FastSealedSimpleUnion.Baz,
                FastSealedSimpleUnion.Unknown {

    public sealed interface Known permits Foo, Bar, Baz {}

    @JsonTypeName("foo")
    public static final class Foo extends FastSealedSimpleUnion implements Known {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private Foo(@JsonSetter("foo") @Nonnull String value) {
            Preconditions.checkNotNull(value, "foo cannot be null");
            this.value = value;
        }

        @JsonProperty(index = 0)
        private String type() {
            return "foo";
        }

        @JsonProperty("foo")
        public String value() {
            return value;
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof Foo && ((Foo) other).value.equals(value));
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }

        @Override
        public String toString() {
            return "FastSealedSimpleUnion{Foo: " + value + '}';
        }
    }

    @JsonTypeName("bar")
    public static final class Bar extends FastSealedSimpleUnion implements Known {
        private final int value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private Bar(@JsonSetter("bar") int value) {
            this.value = value;
        }

        @JsonProperty(index = 0)
        private String type() {
            return "bar";
        }

        @JsonProperty("bar")
        public int value() {
            return value;
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof Bar && ((Bar) other).value == value);
        }

        @Override
        public int hashCode() {
            return value;
        }

        @Override
        public String toString() {
            return "FastSealedSimpleUnion{Bar: " + value + '}';
        }
    }

    @JsonTypeName("baz")
    public static final class Baz extends FastSealedSimpleUnion implements Known {
        private final SafeLong value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private Baz(@JsonSetter("baz") @Nonnull SafeLong value) {
            Preconditions.checkNotNull(value, "baz cannot be null");
            this.value = value;
        }

        @JsonProperty(index = 0)
        private String type() {
            return "baz";
        }

        @JsonProperty("baz")
        public SafeLong value() {
            return value;
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof Baz && ((Baz) other).value.equals(value));
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }

        @Override
        public String toString() {
            return "FastSealedSimpleUnion{Baz: " + value + '}';
        }
    }

    public static final class Unknown extends FastSealedSimpleUnion {
        private final String type;
        private final Map<String, Object> value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private Unknown(@JsonProperty("type") String type) {
            this(type, new HashMap<String, Object>());
        }

        private Unknown(@Nonnull String type, @Nonnull Map<String, Object> value) {
            Preconditions.checkNotNull(type, "type cannot be null");
            Preconditions.checkNotNull(value, "value cannot be null");
            this.type = type;
            this.value = value;
        }

        @JsonProperty
        public String type() {
            return type;
        }

        @JsonAnyGetter
        public Map<String, Object> value() {
            return value;
        }

        @JsonAnySetter
        private void put(String key, Object val) {
            value.put(key, val);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other
                    || (other instanceof Unknown
                            && ((Unknown) other).type.equals(type)
                            && ((Unknown) other).value.equals(value));
        }

        @Override
        public int hashCode() {
            return 31 * type.hashCode() + value.hashCode();
        }

        @Override
        public String toString() {
            return "FastSealedSimpleUnion{Unknown: type=" + type + ", value=" + value + '}';
        }
    }
}
