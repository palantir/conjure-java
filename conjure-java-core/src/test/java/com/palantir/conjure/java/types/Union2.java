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
import com.palantir.conjure.java.types.Union2.Union2_Bar;
import com.palantir.conjure.java.types.Union2.Union2_Baz;
import com.palantir.conjure.java.types.Union2.Union2_Foo;
import com.palantir.conjure.java.types.Union2.Union2_UnknownVariant;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.IntFunction;
import javax.annotation.Nonnull;

/**
 * This is hand-rolled, I just want to make sure it's compatible.
 */

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true,
        defaultImpl = Union2_UnknownVariant.class)
@JsonSubTypes({
        @JsonSubTypes.Type(Union2_Foo.class),
        @JsonSubTypes.Type(Union2_Bar.class),
        @JsonSubTypes.Type(Union2_Baz.class)
})
@JsonIgnoreProperties(ignoreUnknown = true)
public sealed interface Union2 permits Union2.Union2_Foo, Union2.Union2_Bar, Union2.Union2_Baz,
        Union2_UnknownVariant {

    sealed interface Known permits Union2.Union2_Foo, Union2.Union2_Bar, Union2.Union2_Baz {}

    static Union2 foo(String value) {
        return new Union2_Foo(value);
    }

    /**
     * @deprecated Int is deprecated.
     */
    @Deprecated
    static Union2 bar(int value) {
        return new Union2_Bar(value);
    }

    /**
     * 64-bit integer.
     * @deprecated Prefer <code>foo</code>.
     */
    @Deprecated
    static Union2 baz(long value) {
        return new Union2_Baz(value);
    }

    static Union2 unknown(@Safe String type, Object value) {
        return switch (Preconditions.checkNotNull(type, "Type is required")) {
            case "foo" -> throw new SafeIllegalArgumentException(
                    "Unknown type cannot be created as the provided type is known: foo");
            case "bar" -> throw new SafeIllegalArgumentException(
                    "Unknown type cannot be created as the provided type is known: bar");
            case "baz" -> throw new SafeIllegalArgumentException(
                    "Unknown type cannot be created as the provided type is known: baz");
            default -> new Union2_UnknownVariant(type, Collections.singletonMap(type, value));
        };
    }

    default Known throwOnUnknown() {
        if (this instanceof Union2_UnknownVariant u) {
            throw new SafeIllegalArgumentException(
                    "Unknown variant of the 'Union' union", SafeArg.of("unknownType", u.getType()));
        } else {
            return (Known) this;
        }
    }

    <T> T accept(Visitor<T> visitor);

    interface Visitor<T> {
        T visitFoo(String value);

        /**
         * @deprecated Int is deprecated.
         */
        @Deprecated
        T visitBar(int value);

        /**
         * 64-bit integer.
         * @deprecated Prefer <code>foo</code>.
         */
        @Deprecated
        T visitBaz(long value);

        T visitUnknown(@Safe String unknownType);

        static <T> BarStageVisitorBuilder<T> builder() {
            return new VisitorBuilder<T>();
        }
    }

    final class VisitorBuilder<T>
            implements BarStageVisitorBuilder<T>,
            BazStageVisitorBuilder<T>,
            FooStageVisitorBuilder<T>,
            UnknownStageVisitorBuilder<T>,
            Completed_StageVisitorBuilder<T> {
        private IntFunction<T> barVisitor;

        private Function<Long, T> bazVisitor;

        private Function<String, T> fooVisitor;

        private Function<String, T> unknownVisitor;

        @Override
        public BazStageVisitorBuilder<T> bar(@Nonnull IntFunction<T> barVisitor) {
            Preconditions.checkNotNull(barVisitor, "barVisitor cannot be null");
            this.barVisitor = barVisitor;
            return this;
        }

        @Override
        public FooStageVisitorBuilder<T> baz(@Nonnull Function<Long, T> bazVisitor) {
            Preconditions.checkNotNull(bazVisitor, "bazVisitor cannot be null");
            this.bazVisitor = bazVisitor;
            return this;
        }

        @Override
        public UnknownStageVisitorBuilder<T> foo(@Nonnull Function<String, T> fooVisitor) {
            Preconditions.checkNotNull(fooVisitor, "fooVisitor cannot be null");
            this.fooVisitor = fooVisitor;
            return this;
        }

        @Override
        public Completed_StageVisitorBuilder<T> unknown(@Nonnull Function<String, T> unknownVisitor) {
            Preconditions.checkNotNull(unknownVisitor, "unknownVisitor cannot be null");
            this.unknownVisitor = unknownVisitor;
            return this;
        }

        @Override
        public Completed_StageVisitorBuilder<T> throwOnUnknown() {
            this.unknownVisitor = unknownType -> {
                throw new SafeIllegalArgumentException(
                        "Unknown variant of the 'Union' union", SafeArg.of("unknownType", unknownType));
            };
            return this;
        }

        @Override
        public Visitor<T> build() {
            final IntFunction<T> barVisitor = this.barVisitor;
            final Function<Long, T> bazVisitor = this.bazVisitor;
            final Function<String, T> fooVisitor = this.fooVisitor;
            final Function<String, T> unknownVisitor = this.unknownVisitor;
            return new Visitor<T>() {
                @Override
                public T visitBar(int value) {
                    return barVisitor.apply(value);
                }

                @Override
                public T visitBaz(long value) {
                    return bazVisitor.apply(value);
                }

                @Override
                public T visitFoo(String value) {
                    return fooVisitor.apply(value);
                }

                @Override
                public T visitUnknown(String value) {
                    return unknownVisitor.apply(value);
                }
            };
        }
    }

    interface BarStageVisitorBuilder<T> {
        BazStageVisitorBuilder<T> bar(@Nonnull IntFunction<T> barVisitor);
    }

    interface BazStageVisitorBuilder<T> {
        FooStageVisitorBuilder<T> baz(@Nonnull Function<Long, T> bazVisitor);
    }

    interface FooStageVisitorBuilder<T> {
        UnknownStageVisitorBuilder<T> foo(@Nonnull Function<String, T> fooVisitor);
    }

    interface UnknownStageVisitorBuilder<T> {
        Completed_StageVisitorBuilder<T> unknown(@Nonnull Function<String, T> unknownVisitor);

        Completed_StageVisitorBuilder<T> throwOnUnknown();
    }

    interface Completed_StageVisitorBuilder<T> {
        Visitor<T> build();
    }

    @JsonTypeName("foo")
    final class Union2_Foo implements Union2, Known {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private Union2_Foo(@JsonSetter("foo") @Nonnull String value) {
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
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitFoo(value);
        }

        @Override
        public boolean equals(Object other) {
            return this == other || (other instanceof Union2_Foo
                    && equalTo((Union2_Foo) other));
        }

        private boolean equalTo(Union2_Foo other) {
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
    final class Union2_Bar implements Union2, Known {
        private final int value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private Union2_Bar(@JsonSetter("bar") @Nonnull int value) {
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
        @SuppressWarnings("deprecation")
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitBar(value);
        }

        @Override
        public boolean equals(Object other) {
            return this == other || (other instanceof Union2_Bar
                    && equalTo((Union2_Bar) other));
        }

        private boolean equalTo(Union2_Bar other) {
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
    final class Union2_Baz implements Union2, Known {
        private final long value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private Union2_Baz(@JsonSetter("baz") @Nonnull long value) {
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
        @SuppressWarnings("deprecation")
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitBaz(value);
        }

        @Override
        public boolean equals(Object other) {
            return this == other || (other instanceof Union2_Baz
                    && equalTo((Union2_Baz) other));
        }

        private boolean equalTo(Union2_Baz other) {
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

    final class Union2_UnknownVariant implements Union2 {
        private final String type;

        private final Map<String, Object> value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private Union2_UnknownVariant(@JsonProperty("type") String type) {
            this(type, new HashMap<String, Object>());
        }

        private Union2_UnknownVariant(@Nonnull String type, @Nonnull Map<String, Object> value) {
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
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitUnknown(type);
        }

        @Override
        public boolean equals(Object other) {
            return this == other || (other instanceof Union2_UnknownVariant
                    && equalTo((Union2_UnknownVariant) other));
        }

        private boolean equalTo(Union2_UnknownVariant other) {
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
