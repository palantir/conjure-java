package sealedunions.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
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
@JsonSubTypes
@JsonIgnoreProperties(ignoreUnknown = true)
public sealed interface EmptyUnionTypeExample {
    static EmptyUnionTypeExample unknown(@Safe String type, Object value) {
        switch (Preconditions.checkNotNull(type, "Type is required")) {
            default:
                return new UnknownWrapper(type, Collections.singletonMap(type, value));
        }
    }

    <T> void accept(Visitor<T> visitor);

    interface Visitor<T> {
        T visitUnknown(@Safe String unknownType, Object unknownValue);

        static <T> EmptyUnionTypeExample.UnknownStageVisitorBuilder<T> builder() {
            return new EmptyUnionTypeExample.VisitorBuilder<T>();
        }
    }

    final class UnknownWrapper implements EmptyUnionTypeExample {
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
