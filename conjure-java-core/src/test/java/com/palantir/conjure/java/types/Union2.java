/*
 * (c) Copyright 2022 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java.types;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.palantir.conjure.java.types.Union2.Bar;
import com.palantir.conjure.java.types.Union2.Baz;
import com.palantir.conjure.java.types.Union2.Foo;
import com.palantir.conjure.java.types.Union2.UnknownVariant;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;

/**
 * This is hand-rolled, I just want to make sure it's compatible.
 */

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true,
        defaultImpl = UnknownVariant.class)
@JsonSubTypes({
        @JsonSubTypes.Type(Foo.class),
        @JsonSubTypes.Type(Bar.class),
        @JsonSubTypes.Type(Baz.class)
})
@JsonIgnoreProperties(ignoreUnknown = true)
public sealed interface Union2 permits Foo, Bar, Baz,
        UnknownVariant {

    sealed interface Known permits Foo, Bar, Baz {}

    static Union2 foo(String value) {
        return new Foo(value);
    }

    /**
     * @deprecated Int is deprecated.
     */
    @Deprecated
    static Union2 bar(int value) {
        return new Bar(value);
    }

    /**
     * 64-bit integer.
     * @deprecated Prefer <code>foo</code>.
     */
    @Deprecated
    static Union2 baz(long value) {
        return new Baz(value);
    }

    static Union2 unknown(@Safe String type, Object value) {
        return switch (Preconditions.checkNotNull(type, "Type is required")) {
            case "foo" -> throw new SafeIllegalArgumentException(
                    "Unknown type cannot be created as the provided type is known: foo");
            case "bar" -> throw new SafeIllegalArgumentException(
                    "Unknown type cannot be created as the provided type is known: bar");
            case "baz" -> throw new SafeIllegalArgumentException(
                    "Unknown type cannot be created as the provided type is known: baz");
            default -> new UnknownVariant(type, Collections.singletonMap(type, value));
        };
    }

    default Known throwOnUnknown() {
        if (this instanceof UnknownVariant) {
            throw new SafeIllegalArgumentException(
                    "Unknown variant of the 'Union' union",
                    SafeArg.of("unknownType", ((UnknownVariant) this).getType()));
        } else {
            return (Known) this;
        }
    }

    @JsonTypeName("foo")
    final class Foo implements Union2, Known {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private Foo(@JsonSetter("foo") @Nonnull String value) {
            Preconditions.checkNotNull(value, "foo cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        @SuppressWarnings("UnusedMethod")
        private String getType() {
            return "foo";
        }

        @JsonProperty("foo")
        public String getValue() {
            return value;
        }

        @Override
        public boolean equals(Object other) {
            return this == other || (other instanceof Foo
                    && equalTo((Foo) other));
        }

        private boolean equalTo(Foo other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "Union2_Foo{value: " + value + '}';
        }
    }

    @JsonTypeName("bar")
    final class Bar implements Union2, Known {
        private final int value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private Bar(@JsonSetter("bar") @Nonnull int value) {
            Preconditions.checkNotNull(value, "bar cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        @SuppressWarnings("UnusedMethod")
        private String getType() {
            return "bar";
        }

        @JsonProperty("bar")
        public int getValue() {
            return value;
        }

        @Override
        public boolean equals(Object other) {
            return this == other || (other instanceof Bar
                    && equalTo((Bar) other));
        }

        private boolean equalTo(Bar other) {
            return this.value == other.value;
        }

        @Override
        public int hashCode() {
            return this.value;
        }

        @Override
        public String toString() {
            return "Union2_Bar{value: " + value + '}';
        }
    }

    @JsonTypeName("baz")
    final class Baz implements Union2, Known {
        private final long value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private Baz(@JsonSetter("baz") @Nonnull long value) {
            Preconditions.checkNotNull(value, "baz cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        @SuppressWarnings("UnusedMethod")
        private String getType() {
            return "baz";
        }

        @JsonProperty("baz")
        public long getValue() {
            return value;
        }

        @Override
        public boolean equals(Object other) {
            return this == other || (other instanceof Baz
                    && equalTo((Baz) other));
        }

        private boolean equalTo(Baz other) {
            return this.value == other.value;
        }

        @Override
        public int hashCode() {
            return Long.hashCode(this.value);
        }

        @Override
        public String toString() {
            return "Union2_Baz{value: " + value + '}';
        }
    }

    final class UnknownVariant implements Union2 {
        private final String type;

        private final Map<String, Object> value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private UnknownVariant(@JsonProperty("type") String type) {
            this(type, new HashMap<String, Object>());
        }

        private UnknownVariant(@Nonnull String type, @Nonnull Map<String, Object> value) {
            Preconditions.checkNotNull(type, "type cannot be null");
            Preconditions.checkNotNull(value, "value cannot be null");
            this.type = type;
            this.value = value;
        }

        @JsonProperty
        @SuppressWarnings("UnusedMethod")
        private String getType() {
            return type;
        }

        @JsonAnyGetter
        public Map<String, Object> getValue() {
            return value;
        }

        @SuppressWarnings("UnusedMethod")
        @JsonAnySetter
        private void put(String key, Object val) {
            value.put(key, val);
        }

        @Override
        public boolean equals(Object other) {
            return this == other || (other instanceof UnknownVariant
                    && equalTo((UnknownVariant) other));
        }

        private boolean equalTo(UnknownVariant other) {
            return this.type.equals(other.type) && this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            int hash = 1;
            hash = 31 * hash + this.type.hashCode();
            hash = 31 * hash + this.value.hashCode();
            return hash;
        }

        @Override
        public String toString() {
            return "UnknownWrapper{type: " + type + ", value: " + value + '}';
        }
    }
}
