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
        defaultImpl = SingleUnion.class)
@JsonSubTypes(@JsonSubTypes.Type(FooWrapper.class))
@JsonIgnoreProperties(ignoreUnknown = true)
public sealed interface SingleUnion {
    static SingleUnion foo(@Safe String value) {
        return new FooWrapper(value);
    }

    static SingleUnion unknown(@Safe String type, Object value) {
        switch (Preconditions.checkNotNull(type, "Type is required")) {
            case "foo":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: foo");
            default:
                return new SingleUnion(new SingleUnion.UnknownWrapper(type, Collections.singletonMap(type, value)));
        }
    }

    <T> void accept(Visitor<T> visitor);

    interface Visitor<T> {
        T visitFoo(@Safe String value);

        T visitUnknown(@Safe String unknownType, Object unknownValue);

        static <T> SingleUnion.FooStageVisitorBuilder<T> builder() {
            return new SingleUnion.VisitorBuilder<T>();
        }
    }

    @JsonTypeName("foo")
    final class FooWrapper implements SingleUnion {
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
}
