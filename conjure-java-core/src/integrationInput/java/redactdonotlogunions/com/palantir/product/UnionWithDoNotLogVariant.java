package redactdonotlogunions.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
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
public final class UnionWithDoNotLogVariant {
    private final Base value;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    private UnionWithDoNotLogVariant(Base value) {
        this.value = value;
    }

    @JsonValue
    private Base getValue() {
        return value;
    }

    public static UnionWithDoNotLogVariant safeValue(@Safe String value) {
        return new UnionWithDoNotLogVariant(new SafeValueWrapper(value));
    }

    public static UnionWithDoNotLogVariant secretValue(@DoNotLog String value) {
        return new UnionWithDoNotLogVariant(new SecretValueWrapper(value));
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
                return new UnionWithDoNotLogVariant(new UnknownWrapper(type, Collections.singletonMap(type, value)));
        }
    }

    public <T> T accept(Visitor<T> visitor) {
        return value.accept(visitor);
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other
                || (other instanceof UnionWithDoNotLogVariant && equalTo((UnionWithDoNotLogVariant) other));
    }

    private boolean equalTo(UnionWithDoNotLogVariant other) {
        return this.value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    @DoNotLog
    public String toString() {
        return "UnionWithDoNotLogVariant{value: " + value + '}';
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

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.EXISTING_PROPERTY,
            property = "type",
            visible = true,
            defaultImpl = UnknownWrapper.class)
    @JsonSubTypes({@JsonSubTypes.Type(SafeValueWrapper.class), @JsonSubTypes.Type(SecretValueWrapper.class)})
    @JsonIgnoreProperties(ignoreUnknown = true)
    private interface Base {
        <T> T accept(Visitor<T> visitor);
    }

    @JsonTypeName("safeValue")
    private static final class SafeValueWrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private SafeValueWrapper(@JsonSetter("safeValue") @Nonnull String value) {
            Preconditions.checkNotNull(value, "safeValue cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "safeValue";
        }

        @JsonProperty("safeValue")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitSafeValue(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof SafeValueWrapper && equalTo((SafeValueWrapper) other));
        }

        private boolean equalTo(SafeValueWrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "SafeValueWrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("secretValue")
    private static final class SecretValueWrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private SecretValueWrapper(@JsonSetter("secretValue") @Nonnull String value) {
            Preconditions.checkNotNull(value, "secretValue cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "secretValue";
        }

        @JsonProperty("secretValue")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitSecretValue(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof SecretValueWrapper && equalTo((SecretValueWrapper) other));
        }

        private boolean equalTo(SecretValueWrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "SecretValueWrapper{value: REDACTED" + '}';
        }
    }

    private static final class UnknownWrapper implements Base {
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
            return visitor.visitUnknown(type);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof UnknownWrapper && equalTo((UnknownWrapper) other));
        }

        private boolean equalTo(UnknownWrapper other) {
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
