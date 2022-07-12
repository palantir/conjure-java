package sealedunions.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
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
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("com.palantir.conjure.java.types.UnionGenerator")
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true,
        defaultImpl = UnknownWrapper.class)
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
                return new UnknownWrapper(type, Collections.singletonMap(type, value));
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
                    || (other instanceof sealedunions.com.palantir.product.FooWrapper
                            && equalTo((sealedunions.com.palantir.product.FooWrapper) other));
        }

        private boolean equalTo(sealedunions.com.palantir.product.FooWrapper other) {
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
                    || (other instanceof sealedunions.com.palantir.product.BarWrapper
                            && equalTo((sealedunions.com.palantir.product.BarWrapper) other));
        }

        private boolean equalTo(sealedunions.com.palantir.product.BarWrapper other) {
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
                    || (other instanceof sealedunions.com.palantir.product.BazWrapper
                            && equalTo((sealedunions.com.palantir.product.BazWrapper) other));
        }

        private boolean equalTo(sealedunions.com.palantir.product.BazWrapper other) {
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

    final class UnknownWrapper implements Union {
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
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitUnknown(type, value.get(type));
        }

        @Override
        public boolean equals(Object other) {
            return this == other
                    || (other instanceof sealedunions.com.palantir.product.UnknownWrapper
                            && equalTo((sealedunions.com.palantir.product.UnknownWrapper) other));
        }

        private boolean equalTo(sealedunions.com.palantir.product.UnknownWrapper other) {
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
