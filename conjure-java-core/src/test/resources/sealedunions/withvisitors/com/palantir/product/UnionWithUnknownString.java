package withvisitors.com.palantir.product;

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
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("com.palantir.conjure.java.types.UnionGenerator")
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true,
        defaultImpl = UnionWithUnknownString.Unknown.class)
@JsonSubTypes(@JsonSubTypes.Type(Unknown_.class))
@JsonIgnoreProperties(ignoreUnknown = true)
public sealed interface UnionWithUnknownString {
    static UnionWithUnknownString unknown_(String value) {
        return new Unknown_(value);
    }

    static UnionWithUnknownString unknown(@Safe String type, Object value) {
        switch (Preconditions.checkNotNull(type, "Type is required")) {
            case "unknown":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: unknown");
            default:
                return new Unknown(type, Collections.singletonMap(type, value));
        }
    }

    default Known throwOnUnknown() {
        if (this instanceof Unknown) {
            throw new SafeIllegalArgumentException(
                    "Unknown variant of the 'Union' union", SafeArg.of("type", ((Unknown) this).getType()));
        } else {
            return (Known) this;
        }
    }

    <T> void accept(Visitor<T> visitor);

    sealed interface Known permits Unknown_ {}

    @JsonTypeName("unknown")
    record Unknown_(String value) implements UnionWithUnknownString, Known {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        Unknown_(@JsonSetter("unknown") @Nonnull String value) {
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
        public String toString() {
            return "Unknown_{value: " + value + '}';
        }
    }

    final class Unknown implements UnionWithUnknownString {
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
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitUnknown(type, value.get(type));
        }

        @Override
        public boolean equals(Object other) {
            return this == other || (other instanceof Unknown && equalTo((Unknown) other));
        }

        private boolean equalTo(Unknown other) {
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

    interface Visitor<T> {
        T visitUnknown_(String value);

        T visitUnknown(@Safe String unknownType, Object unknownValue);

        /**
         * @Deprecated - prefer Java 17 pattern matching switch expressions.
         */
        @Deprecated
        static <T> Unknown_StageVisitorBuilder<T> builder() {
            return new VisitorBuilder<T>();
        }
    }

    final class VisitorBuilder<T>
            implements Unknown_StageVisitorBuilder<T>, UnknownStageVisitorBuilder<T>, Completed_StageVisitorBuilder<T> {
        private Function<String, T> unknown_Visitor;

        private BiFunction<@Safe String, Object, T> unknownVisitor;

        @Override
        public UnknownStageVisitorBuilder<T> unknown_(@Nonnull Function<String, T> unknown_Visitor) {
            Preconditions.checkNotNull(unknown_Visitor, "unknown_Visitor cannot be null");
            this.unknown_Visitor = unknown_Visitor;
            return this;
        }

        @Override
        public Completed_StageVisitorBuilder<T> unknown(@Nonnull BiFunction<@Safe String, Object, T> unknownVisitor) {
            Preconditions.checkNotNull(unknownVisitor, "unknownVisitor cannot be null");
            this.unknownVisitor = unknownVisitor;
            return this;
        }

        @Override
        public Completed_StageVisitorBuilder<T> unknown(@Nonnull Function<@Safe String, T> unknownVisitor) {
            Preconditions.checkNotNull(unknownVisitor, "unknownVisitor cannot be null");
            this.unknownVisitor = (unknownType, _unknownValue) -> unknownVisitor.apply(unknownType);
            return this;
        }

        @Override
        public Completed_StageVisitorBuilder<T> throwOnUnknown() {
            this.unknownVisitor = (unknownType, _unknownValue) -> {
                throw new SafeIllegalArgumentException(
                        "Unknown variant of the 'UnionWithUnknownString' union",
                        SafeArg.of("unknownType", unknownType));
            };
            return this;
        }

        @Override
        public Visitor<T> build() {
            final Function<String, T> unknown_Visitor = this.unknown_Visitor;
            final BiFunction<@Safe String, Object, T> unknownVisitor = this.unknownVisitor;
            return new Visitor<T>() {
                @Override
                public T visitUnknown_(String value) {
                    return unknown_Visitor.apply(value);
                }

                @Override
                public T visitUnknown(String unknownType, Object unknownValue) {
                    return unknownVisitor.apply(unknownType, unknownValue);
                }
            };
        }
    }

    interface Unknown_StageVisitorBuilder<T> {
        UnknownStageVisitorBuilder<T> unknown_(@Nonnull Function<String, T> unknown_Visitor);
    }

    interface UnknownStageVisitorBuilder<T> {
        Completed_StageVisitorBuilder<T> unknown(@Nonnull BiFunction<@Safe String, Object, T> unknownVisitor);

        Completed_StageVisitorBuilder<T> unknown(@Nonnull Function<@Safe String, T> unknownVisitor);

        Completed_StageVisitorBuilder<T> throwOnUnknown();
    }

    interface Completed_StageVisitorBuilder<T> {
        Visitor<T> build();
    }
}
