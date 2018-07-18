package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.types.UnionGenerator")
public final class BlaBla1 {
    @JsonUnwrapped private final Union_ union;

    @JsonCreator
    private BlaBla1(Union_ union) {
        Objects.requireNonNull(union, "union must not be null");
        this.union = union;
    }

    public <T> T accept(Visitor<T> visitor) {
        if (union.getBlaBla().isPresent()) {
            return visitor.visitBlaBla(union.getBlaBla().get());
        }
        return visitor.visitUnknown(union.getType());
    }

    @Override
    public boolean equals(Object other) {
        return this == other || (other instanceof BlaBla1 && equalTo((BlaBla1) other));
    }

    private boolean equalTo(BlaBla1 other) {
        return this.union.equals(other.union);
    }

    @Override
    public int hashCode() {
        return union.hashCode();
    }

    @Override
    public String toString() {
        return union.toString();
    }

    public static BlaBla1 blaBla(BlaBla value) {
        return new BlaBla1(Union_.builder().type("blaBla").blaBla(value).build());
    }

    public interface Visitor<T> {
        T visitBlaBla(BlaBla value);

        T visitUnknown(String unknownType);
    }

    @JsonDeserialize(builder = Union_.Builder.class)
    @Generated("com.palantir.conjure.java.types.BeanGenerator")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private static final class Union_ {
        private final String type;

        private final Optional<BlaBla> blaBla;

        private final Map<String, Object> __unknownProperties;

        private volatile int memoizedHashCode;

        private Union_(
                String type, Optional<BlaBla> blaBla, Map<String, Object> __unknownProperties) {
            validateFields(type, blaBla);
            this.type = type;
            this.blaBla = blaBla;
            this.__unknownProperties = Collections.unmodifiableMap(__unknownProperties);
        }

        @JsonProperty("type")
        public String getType() {
            return this.type;
        }

        @JsonProperty("blaBla")
        public Optional<BlaBla> getBlaBla() {
            return this.blaBla;
        }

        @JsonAnyGetter
        Map<String, Object> unknownProperties() {
            return __unknownProperties;
        }

        @Override
        public boolean equals(Object other) {
            return this == other || (other instanceof Union_ && equalTo((Union_) other));
        }

        private boolean equalTo(Union_ other) {
            return this.type.equals(other.type)
                    && this.blaBla.equals(other.blaBla)
                    && this.__unknownProperties.equals(other.__unknownProperties);
        }

        @Override
        public int hashCode() {
            if (memoizedHashCode == 0) {
                memoizedHashCode = Objects.hash(type, blaBla, __unknownProperties);
            }
            return memoizedHashCode;
        }

        @Override
        public String toString() {
            return new StringBuilder("Union_")
                    .append("{")
                    .append("type")
                    .append(": ")
                    .append(type)
                    .append(", ")
                    .append("blaBla")
                    .append(": ")
                    .append(blaBla)
                    .append("}")
                    .toString();
        }

        public static Union_ of(
                String type, BlaBla blaBla, Map<String, Object> __unknownProperties) {
            return builder()
                    .type(type)
                    .blaBla(Optional.of(blaBla))
                    .__unknownProperties(__unknownProperties)
                    .build();
        }

        private static void validateFields(String type, Optional<BlaBla> blaBla) {
            List<String> missingFields = null;
            missingFields = addFieldIfMissing(missingFields, type, "type");
            missingFields = addFieldIfMissing(missingFields, blaBla, "blaBla");
            if (missingFields != null) {
                throw new IllegalArgumentException(
                        "Some required fields have not been set: " + missingFields);
            }
        }

        private static List<String> addFieldIfMissing(
                List<String> prev, Object fieldValue, String fieldName) {
            List<String> missingFields = prev;
            if (fieldValue == null) {
                if (missingFields == null) {
                    missingFields = new ArrayList<>(2);
                }
                missingFields.add(fieldName);
            }
            return missingFields;
        }

        public static Builder builder() {
            return new Builder();
        }

        @Generated("com.palantir.conjure.java.types.BeanBuilderGenerator")
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static final class Builder {
            private String type;

            private Optional<BlaBla> blaBla = Optional.empty();

            Map<String, Object> __unknownProperties = new LinkedHashMap<>();

            private Builder() {}

            public Builder from(Union_ other) {
                type(other.getType());
                blaBla(other.getBlaBla());
                return this;
            }

            @JsonSetter("type")
            public Builder type(String type) {
                this.type = Objects.requireNonNull(type, "type cannot be null");
                return this;
            }

            @JsonSetter("blaBla")
            public Builder blaBla(Optional<BlaBla> blaBla) {
                this.blaBla = Objects.requireNonNull(blaBla, "blaBla cannot be null");
                return this;
            }

            public Builder blaBla(BlaBla blaBla) {
                this.blaBla = Optional.of(Objects.requireNonNull(blaBla, "blaBla cannot be null"));
                return this;
            }

            public Union_ build() {
                return new Union_(type, blaBla, __unknownProperties);
            }

            @JsonAnySetter
            private void setUnknownProperties(String key, Object value) {
                __unknownProperties.put(key, value);
            }
        }
    }
}