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
        defaultImpl = UnionWithUnknownString.class)
@JsonSubTypes(@JsonSubTypes.Type(Unknown_Wrapper.class))
@JsonIgnoreProperties(ignoreUnknown = true)
public sealed interface UnionWithUnknownString {
    static UnionWithUnknownString unknown_(String value) {
        return new Unknown_Wrapper(value);
    }

    static UnionWithUnknownString unknown(@Safe String type, Object value) {
        switch (Preconditions.checkNotNull(type, "Type is required")) {
            case "unknown":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: unknown");
            default:
                return new UnionWithUnknownString(
                        new UnionWithUnknownString.UnknownWrapper(type, Collections.singletonMap(type, value)));
        }
    }

    <T> void accept(Visitor<T> visitor);

    interface Visitor<T> {
        T visitUnknown_(String value);

        T visitUnknown(@Safe String unknownType, Object unknownValue);

        static <T> UnionWithUnknownString.Unknown_StageVisitorBuilder<T> builder() {
            return new UnionWithUnknownString.VisitorBuilder<T>();
        }
    }

    @JsonTypeName("unknown")
    final class Unknown_Wrapper implements UnionWithUnknownString {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private Unknown_Wrapper(@JsonSetter("unknown") @Nonnull String value) {
            Preconditions.checkNotNull(value, "unknown_ cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "unknown";
        }

        @JsonProperty("unknown")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitUnknown_(value);
        }

        @Override
        public boolean equals(Object other) {
            return this == other
                    || (other instanceof com.palantir.product.Unknown_Wrapper
                            && equalTo((com.palantir.product.Unknown_Wrapper) other));
        }

        private boolean equalTo(com.palantir.product.Unknown_Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "Unknown_Wrapper{value: " + value + '}';
        }
    }
}
