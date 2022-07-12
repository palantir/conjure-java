package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.Collections;
import javax.annotation.Nonnull;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true,
        defaultImpl = Union.class)
@JsonSubTypes({
    @JsonSubTypes.Type(FooWrapper.class),
    @JsonSubTypes.Type(BarWrapper.class),
    @JsonSubTypes.Type(BazWrapper.class)
})
@JsonIgnoreProperties(ignoreUnknown = true)
public sealed interface Union {
    static Union foo(String value) {
        return new FooWrapper(value);
    }

    /**
     * @deprecated Int is deprecated.
     */
    @Deprecated
    static Union bar(int value) {
        return new BarWrapper(value);
    }

    /**
     * 64-bit integer.
     * @deprecated Prefer <code>foo</code>.
     */
    @Deprecated
    static Union baz(long value) {
        return new BazWrapper(value);
    }

    static Union unknown(@Safe String type, Object value) {
        switch (Preconditions.checkNotNull(type, "Type is required")) {
            case "foo":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: foo");
            case "bar":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: bar");
            case "baz":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: baz");
            default:
                return new Union(new Union.UnknownWrapper(type, Collections.singletonMap(type, value)));
        }
    }

    <T> void accept(Visitor<T> visitor);

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

        T visitUnknown(@Safe String unknownType, Object unknownValue);

        static <T> Union.BarStageVisitorBuilder<T> builder() {
            return new Union.VisitorBuilder<T>();
        }
    }

    @JsonTypeName("foo")
    final class FooWrapper implements Union {
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
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitFoo(value);
        }

        @Override
        public boolean equals(Object other) {
            return this == other
                    || (other instanceof com.palantir.product.FooWrapper
                            && equalTo((com.palantir.product.FooWrapper) other));
        }

        private boolean equalTo(com.palantir.product.FooWrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "FooWrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("bar")
    final class BarWrapper implements Union {
        private final int value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private BarWrapper(@JsonSetter("bar") @Nonnull int value) {
            Preconditions.checkNotNull(value, "bar cannot be null");
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
        @SuppressWarnings("deprecation")
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitBar(value);
        }

        @Override
        public boolean equals(Object other) {
            return this == other
                    || (other instanceof com.palantir.product.BarWrapper
                            && equalTo((com.palantir.product.BarWrapper) other));
        }

        private boolean equalTo(com.palantir.product.BarWrapper other) {
            return this.value == other.value;
        }

        @Override
        public int hashCode() {
            return this.value;
        }

        @Override
        public String toString() {
            return "BarWrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("baz")
    final class BazWrapper implements Union {
        private final long value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private BazWrapper(@JsonSetter("baz") @Nonnull long value) {
            Preconditions.checkNotNull(value, "baz cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "baz";
        }

        @JsonProperty("baz")
        private long getValue() {
            return value;
        }

        @Override
        @SuppressWarnings("deprecation")
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitBaz(value);
        }

        @Override
        public boolean equals(Object other) {
            return this == other
                    || (other instanceof com.palantir.product.BazWrapper
                            && equalTo((com.palantir.product.BazWrapper) other));
        }

        private boolean equalTo(com.palantir.product.BazWrapper other) {
            return this.value == other.value;
        }

        @Override
        public int hashCode() {
            return Long.hashCode(this.value);
        }

        @Override
        public String toString() {
            return "BazWrapper{value: " + value + '}';
        }
    }
}
