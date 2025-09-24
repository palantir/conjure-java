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
import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.UnionGenerator")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", defaultImpl = SimpleUnion.UnknownVariant.class)
@JsonSubTypes({
    @JsonSubTypes.Type(value = SimpleUnion.Foo.class, name = "foo"),
    @JsonSubTypes.Type(value = SimpleUnion.Bar.class, name = "bar"),
    @JsonSubTypes.Type(value = SimpleUnion.Baz.class, name = "baz")
})
@JsonIgnoreProperties(ignoreUnknown = true)
public sealed interface SimpleUnion {
    static SimpleUnion foo(String value) {
        return new Foo(value);
    }

    static SimpleUnion bar(int value) {
        return new Bar(value);
    }

    static SimpleUnion baz(SafeLong value) {
        return new Baz(value);
    }

    static SimpleUnion unknown(@Safe String type, Object value) {
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
                return new UnknownVariant(type, Collections.singletonMap(type, value));
        }
    }

    default Known throwOnUnknown() {
        if (this instanceof UnknownVariant) {
            throw new SafeIllegalArgumentException(
                    "Unknown variant of the 'SimpleUnion' union", SafeArg.of("type", ((UnknownVariant) this).type()));
        } else {
            return (Known) this;
        }
    }

    sealed interface Known extends SimpleUnion permits Foo, Bar, Baz {}

    @JsonTypeName("foo")
    record Foo(String value) implements Known {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public Foo(@JsonSetter("foo") @Nonnull String value) {
            Preconditions.checkNotNull(value, "foo cannot be null");
            this.value = value;
        }

        @Override
        public String toString() {
            return "SimpleUnion.Foo{value: " + value + '}';
        }
    }

    @JsonTypeName("bar")
    record Bar(int value) implements Known {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public Bar(@JsonSetter("bar") @Nonnull int value) {
            Preconditions.checkNotNull(value, "bar cannot be null");
            this.value = value;
        }

        @Override
        public String toString() {
            return "SimpleUnion.Bar{value: " + value + '}';
        }
    }

    @JsonTypeName("baz")
    record Baz(SafeLong value) implements Known {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public Baz(@JsonSetter("baz") @Nonnull SafeLong value) {
            Preconditions.checkNotNull(value, "baz cannot be null");
            this.value = value;
        }

        @Override
        public String toString() {
            return "SimpleUnion.Baz{value: " + value + '}';
        }
    }

    record UnknownVariant(String type, Map<String, Object> value) implements SimpleUnion {
        public UnknownVariant {
            Preconditions.checkNotNull(type, "type cannot be null");
            Preconditions.checkNotNull(value, "type cannot be null");
        }

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private UnknownVariant(@JsonProperty("type") String type) {
            this(type, new HashMap<String, Object>());
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
        public String toString() {
            return "SimpleUnion.UnknownVariant{value: " + value + '}';
        }
    }
}
