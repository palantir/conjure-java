package redactdonotlogsealedunions.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.palantir.logsafe.DoNotLog;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@DoNotLog
@Generated("com.palantir.conjure.java.types.UnionGenerator")
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true,
        defaultImpl = UnionWithDoNotLogVariant.Unknown.class)
@JsonSubTypes({
    @JsonSubTypes.Type(value = UnionWithDoNotLogVariant.SafeValue.class, name = "safeValue"),
    @JsonSubTypes.Type(value = UnionWithDoNotLogVariant.SecretValue.class, name = "secretValue")
})
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract sealed class UnionWithDoNotLogVariant
        permits UnionWithDoNotLogVariant.SafeValue,
                UnionWithDoNotLogVariant.SecretValue,
                UnionWithDoNotLogVariant.Unknown {
    public static UnionWithDoNotLogVariant safeValue(@Safe String value) {
        return new SafeValue(value);
    }

    public static UnionWithDoNotLogVariant secretValue(@DoNotLog String value) {
        return new SecretValue(value);
    }

    public static UnionWithDoNotLogVariant unknown(@Safe String type, Object value) {
        switch (Preconditions.checkNotNull(type, "Type is required")) {
            case "safeValue":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: safeValue");
            case "secretValue":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: secretValue");
            default:
                return new Unknown(type, Collections.singletonMap(type, value));
        }
    }

    public Known throwOnUnknown() {
        if (this instanceof Unknown) {
            throw new SafeIllegalArgumentException(
                    "Unknown variant of the 'UnionWithDoNotLogVariant' union",
                    SafeArg.of("unknownType", ((Unknown) this).type()));
        } else {
            return (Known) this;
        }
    }

    public abstract <T> T accept(Visitor<T> visitor);

    public sealed interface Known permits SafeValue, SecretValue {}

    @JsonTypeName("safeValue")
    public static final class SafeValue extends UnionWithDoNotLogVariant implements Known {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private SafeValue(@JsonSetter("safeValue") @Nonnull String value) {
            Preconditions.checkNotNull(value, "safeValue cannot be null");
            this.value = value;
        }

        @JsonProperty(index = 0)
        private String type() {
            return "safeValue";
        }

        @JsonProperty("safeValue")
        public String value() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitSafeValue(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof SafeValue && equalTo((SafeValue) other));
        }

        private boolean equalTo(SafeValue other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "UnionWithDoNotLogVariant{value: SafeValueWrapper{value: " + value + "}}";
        }
    }

    @JsonTypeName("secretValue")
    public static final class SecretValue extends UnionWithDoNotLogVariant implements Known {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private SecretValue(@JsonSetter("secretValue") @Nonnull String value) {
            Preconditions.checkNotNull(value, "secretValue cannot be null");
            this.value = value;
        }

        @JsonProperty(index = 0)
        private String type() {
            return "secretValue";
        }

        @JsonProperty("secretValue")
        public String value() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitSecretValue(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof SecretValue && equalTo((SecretValue) other));
        }

        private boolean equalTo(SecretValue other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "UnionWithDoNotLogVariant{value: SecretValueWrapper{value: REDACTED" + "}}";
        }
    }

    public static final class Unknown extends UnionWithDoNotLogVariant {
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
        public String type() {
            return type;
        }

        @JsonAnyGetter
        public Map<String, Object> value() {
            return value;
        }

        @JsonAnySetter
        private void put(String key, Object val) {
            value.put(key, val);
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitUnknown(type);
        }

        @Override
        public boolean equals(@Nullable Object other) {
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
            return "UnionWithDoNotLogVariant{value: UnknownWrapper{value: " + value + "}}";
        }
    }

    public interface Visitor<T> {
        T visitSafeValue(@Safe String value);

        T visitSecretValue(@DoNotLog String value);

        T visitUnknown(@Safe String unknownType);

        static <T> SafeValueStageVisitorBuilder<T> builder() {
            return new VisitorBuilder<T>();
        }
    }

    private static final class VisitorBuilder<T>
            implements SafeValueStageVisitorBuilder<T>,
                    SecretValueStageVisitorBuilder<T>,
                    UnknownStageVisitorBuilder<T>,
                    Completed_StageVisitorBuilder<T> {
        private Function<@Safe String, T> safeValueVisitor;

        private Function<@DoNotLog String, T> secretValueVisitor;

        private Function<String, T> unknownVisitor;

        @Override
        public SecretValueStageVisitorBuilder<T> safeValue(@Nonnull Function<@Safe String, T> safeValueVisitor) {
            Preconditions.checkNotNull(safeValueVisitor, "safeValueVisitor cannot be null");
            this.safeValueVisitor = safeValueVisitor;
            return this;
        }

        @Override
        public UnknownStageVisitorBuilder<T> secretValue(@Nonnull Function<@DoNotLog String, T> secretValueVisitor) {
            Preconditions.checkNotNull(secretValueVisitor, "secretValueVisitor cannot be null");
            this.secretValueVisitor = secretValueVisitor;
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
                        "Unknown variant of the 'UnionWithDoNotLogVariant' union",
                        SafeArg.of("unknownType", unknownType));
            };
            return this;
        }

        @Override
        public Visitor<T> build() {
            final Function<@Safe String, T> safeValueVisitor = this.safeValueVisitor;
            final Function<@DoNotLog String, T> secretValueVisitor = this.secretValueVisitor;
            final Function<String, T> unknownVisitor = this.unknownVisitor;
            return new Visitor<T>() {
                @Override
                public T visitSafeValue(@Safe String value) {
                    return safeValueVisitor.apply(value);
                }

                @Override
                public T visitSecretValue(@DoNotLog String value) {
                    return secretValueVisitor.apply(value);
                }

                @Override
                public T visitUnknown(String value) {
                    return unknownVisitor.apply(value);
                }
            };
        }
    }

    public interface SafeValueStageVisitorBuilder<T> {
        SecretValueStageVisitorBuilder<T> safeValue(@Nonnull Function<@Safe String, T> safeValueVisitor);
    }

    public interface SecretValueStageVisitorBuilder<T> {
        UnknownStageVisitorBuilder<T> secretValue(@Nonnull Function<@DoNotLog String, T> secretValueVisitor);
    }

    public interface UnknownStageVisitorBuilder<T> {
        Completed_StageVisitorBuilder<T> unknown(@Nonnull Function<String, T> unknownVisitor);

        Completed_StageVisitorBuilder<T> throwOnUnknown();
    }

    public interface Completed_StageVisitorBuilder<T> {
        Visitor<T> build();
    }
}
