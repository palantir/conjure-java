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
        defaultImpl = Unknown.class)
@JsonSubTypes({@JsonSubTypes.Type(Foo.class), @JsonSubTypes.Type(Bar.class), @JsonSubTypes.Type(Baz.class)})
@JsonIgnoreProperties(ignoreUnknown = true)
public sealed interface Union {
    static Union foo(String value) {
        return new Foo(value);
    }

    /**
     * @deprecated Int is deprecated.
     */
    @Deprecated
    static Union bar(int value) {
        return new Bar(value);
    }

    /**
     * 64-bit integer.
     * @deprecated Prefer <code>foo</code>.
     */
    @Deprecated
    static Union baz(long value) {
        return new Baz(value);
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
                return new Unknown(type, Collections.singletonMap(type, value));
        }
    }

    @JsonTypeName("foo")
    final class Foo implements Union {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private Foo(@JsonSetter("foo") @Nonnull String value) {
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
        public boolean equals(Object other) {
            return this == other
                    || (other instanceof sealedunions.com.palantir.product.Foo
                            && equalTo((sealedunions.com.palantir.product.Foo) other));
        }

        private boolean equalTo(sealedunions.com.palantir.product.Foo other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "Foo{value: " + value + '}';
        }
    }

    @JsonTypeName("bar")
    final class Bar implements Union {
        private final int value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private Bar(@JsonSetter("bar") @Nonnull int value) {
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
        public boolean equals(Object other) {
            return this == other
                    || (other instanceof sealedunions.com.palantir.product.Bar
                            && equalTo((sealedunions.com.palantir.product.Bar) other));
        }

        private boolean equalTo(sealedunions.com.palantir.product.Bar other) {
            return this.value == other.value;
        }

        @Override
        public int hashCode() {
            return this.value;
        }

        @Override
        public String toString() {
            return "Bar{value: " + value + '}';
        }
    }

    @JsonTypeName("baz")
    final class Baz implements Union {
        private final long value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private Baz(@JsonSetter("baz") @Nonnull long value) {
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
        public boolean equals(Object other) {
            return this == other
                    || (other instanceof sealedunions.com.palantir.product.Baz
                            && equalTo((sealedunions.com.palantir.product.Baz) other));
        }

        private boolean equalTo(sealedunions.com.palantir.product.Baz other) {
            return this.value == other.value;
        }

        @Override
        public int hashCode() {
            return Long.hashCode(this.value);
        }

        @Override
        public String toString() {
            return "Baz{value: " + value + '}';
        }
    }

    final class Unknown implements Union {
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
        public boolean equals(Object other) {
            return this == other
                    || (other instanceof sealedunions.com.palantir.product.Unknown
                            && equalTo((sealedunions.com.palantir.product.Unknown) other));
        }

        private boolean equalTo(sealedunions.com.palantir.product.Unknown other) {
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
            return "Unknown{type: " + type + ", value: " + value + '}';
        }
    }
}
