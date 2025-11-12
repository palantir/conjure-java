package dialogue.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.Nulls;
import com.palantir.conjure.java.lib.internal.ConjureGenerated;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.IntFunction;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@ConjureGenerated("com.palantir.conjure.java.types.UnionGenerator")
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true,
        defaultImpl = UnionExample.Unknown.class)
@JsonSubTypes({
    @JsonSubTypes.Type(value = UnionExample.StringVariant.class, name = "stringVariant"),
    @JsonSubTypes.Type(value = UnionExample.IntVariant.class, name = "intVariant"),
    @JsonSubTypes.Type(value = UnionExample.ObjectVariant.class, name = "objectVariant"),
    @JsonSubTypes.Type(value = UnionExample.CollectionVariant.class, name = "collectionVariant"),
    @JsonSubTypes.Type(value = UnionExample.OptionalVariant.class, name = "optionalVariant")
})
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract sealed class UnionExample
        permits UnionExample.StringVariant,
                UnionExample.IntVariant,
                UnionExample.ObjectVariant,
                UnionExample.CollectionVariant,
                UnionExample.OptionalVariant,
                UnionExample.Unknown {
    public static UnionExample stringVariant(String value) {
        return new StringVariant(value);
    }

    public static UnionExample intVariant(int value) {
        return new IntVariant(value);
    }

    public static UnionExample objectVariant(ObjectReference value) {
        return new ObjectVariant(value);
    }

    public static UnionExample collectionVariant(List<String> value) {
        return new CollectionVariant(value);
    }

    public static UnionExample optionalVariant(Optional<String> value) {
        return new OptionalVariant(value);
    }

    public static UnionExample unknown(@Safe String type, Object value) {
        switch (Preconditions.checkNotNull(type, "Type is required")) {
            case "stringVariant":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: stringVariant");
            case "intVariant":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: intVariant");
            case "objectVariant":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: objectVariant");
            case "collectionVariant":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: collectionVariant");
            case "optionalVariant":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: optionalVariant");
            default:
                return new Unknown(type, Collections.singletonMap(type, value));
        }
    }

    public Known throwOnUnknown() {
        if (this instanceof Unknown) {
            throw new SafeIllegalArgumentException(
                    "Unknown variant of the 'UnionExample' union", SafeArg.of("unknownType", ((Unknown) this).type()));
        } else {
            return (Known) this;
        }
    }

    public abstract <T> T accept(Visitor<T> visitor);

    public sealed interface Known
            permits StringVariant, IntVariant, ObjectVariant, CollectionVariant, OptionalVariant {}

    @JsonTypeName("stringVariant")
    public static final class StringVariant extends UnionExample implements Known {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private StringVariant(@JsonSetter("stringVariant") @Nonnull String value) {
            Preconditions.checkNotNull(value, "stringVariant cannot be null");
            this.value = value;
        }

        @JsonProperty(index = 0)
        private String type() {
            return "stringVariant";
        }

        @JsonProperty("stringVariant")
        public String value() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitStringVariant(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof StringVariant && equalTo((StringVariant) other));
        }

        private boolean equalTo(StringVariant other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "UnionExample.StringVariant{value: " + value + '}';
        }
    }

    @JsonTypeName("intVariant")
    public static final class IntVariant extends UnionExample implements Known {
        private final int value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private IntVariant(@JsonSetter("intVariant") @Nonnull int value) {
            Preconditions.checkNotNull(value, "intVariant cannot be null");
            this.value = value;
        }

        @JsonProperty(index = 0)
        private String type() {
            return "intVariant";
        }

        @JsonProperty("intVariant")
        public int value() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitIntVariant(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof IntVariant && equalTo((IntVariant) other));
        }

        private boolean equalTo(IntVariant other) {
            return this.value == other.value;
        }

        @Override
        public int hashCode() {
            return this.value;
        }

        @Override
        public String toString() {
            return "UnionExample.IntVariant{value: " + value + '}';
        }
    }

    @JsonTypeName("objectVariant")
    public static final class ObjectVariant extends UnionExample implements Known {
        private final ObjectReference value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private ObjectVariant(@JsonSetter("objectVariant") @Nonnull ObjectReference value) {
            Preconditions.checkNotNull(value, "objectVariant cannot be null");
            this.value = value;
        }

        @JsonProperty(index = 0)
        private String type() {
            return "objectVariant";
        }

        @JsonProperty("objectVariant")
        public ObjectReference value() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitObjectVariant(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof ObjectVariant && equalTo((ObjectVariant) other));
        }

        private boolean equalTo(ObjectVariant other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "UnionExample.ObjectVariant{value: " + value + '}';
        }
    }

    @JsonTypeName("collectionVariant")
    public static final class CollectionVariant extends UnionExample implements Known {
        private final List<String> value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private CollectionVariant(
                @JsonSetter(value = "collectionVariant", nulls = Nulls.AS_EMPTY) @Nonnull List<String> value) {
            Preconditions.checkNotNull(value, "collectionVariant cannot be null");
            this.value = value;
        }

        @JsonProperty(index = 0)
        private String type() {
            return "collectionVariant";
        }

        @JsonProperty("collectionVariant")
        public List<String> value() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitCollectionVariant(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof CollectionVariant && equalTo((CollectionVariant) other));
        }

        private boolean equalTo(CollectionVariant other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "UnionExample.CollectionVariant{value: " + value + '}';
        }
    }

    @JsonTypeName("optionalVariant")
    public static final class OptionalVariant extends UnionExample implements Known {
        private final Optional<String> value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private OptionalVariant(
                @JsonSetter(value = "optionalVariant", nulls = Nulls.AS_EMPTY) @Nonnull Optional<String> value) {
            Preconditions.checkNotNull(value, "optionalVariant cannot be null");
            this.value = value;
        }

        @JsonProperty(index = 0)
        private String type() {
            return "optionalVariant";
        }

        @JsonProperty("optionalVariant")
        public Optional<String> value() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitOptionalVariant(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof OptionalVariant && equalTo((OptionalVariant) other));
        }

        private boolean equalTo(OptionalVariant other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "UnionExample.OptionalVariant{value: " + value + '}';
        }
    }

    public static final class Unknown extends UnionExample {
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
            return "UnionExample.Unknown{type: " + type + ", value: " + value + '}';
        }
    }

    public interface Visitor<T> {
        T visitStringVariant(String value);

        T visitIntVariant(int value);

        T visitObjectVariant(ObjectReference value);

        T visitCollectionVariant(List<String> value);

        T visitOptionalVariant(Optional<String> value);

        T visitUnknown(@Safe String unknownType);

        static <T> CollectionVariantStageVisitorBuilder<T> builder() {
            return new VisitorBuilder<T>();
        }
    }

    private static final class VisitorBuilder<T>
            implements CollectionVariantStageVisitorBuilder<T>,
                    IntVariantStageVisitorBuilder<T>,
                    ObjectVariantStageVisitorBuilder<T>,
                    OptionalVariantStageVisitorBuilder<T>,
                    StringVariantStageVisitorBuilder<T>,
                    UnknownStageVisitorBuilder<T>,
                    Completed_StageVisitorBuilder<T> {
        private Function<List<String>, T> collectionVariantVisitor;

        private IntFunction<T> intVariantVisitor;

        private Function<ObjectReference, T> objectVariantVisitor;

        private Function<Optional<String>, T> optionalVariantVisitor;

        private Function<String, T> stringVariantVisitor;

        private Function<String, T> unknownVisitor;

        @Override
        public IntVariantStageVisitorBuilder<T> collectionVariant(
                @Nonnull Function<List<String>, T> collectionVariantVisitor) {
            Preconditions.checkNotNull(collectionVariantVisitor, "collectionVariantVisitor cannot be null");
            this.collectionVariantVisitor = collectionVariantVisitor;
            return this;
        }

        @Override
        public ObjectVariantStageVisitorBuilder<T> intVariant(@Nonnull IntFunction<T> intVariantVisitor) {
            Preconditions.checkNotNull(intVariantVisitor, "intVariantVisitor cannot be null");
            this.intVariantVisitor = intVariantVisitor;
            return this;
        }

        @Override
        public OptionalVariantStageVisitorBuilder<T> objectVariant(
                @Nonnull Function<ObjectReference, T> objectVariantVisitor) {
            Preconditions.checkNotNull(objectVariantVisitor, "objectVariantVisitor cannot be null");
            this.objectVariantVisitor = objectVariantVisitor;
            return this;
        }

        @Override
        public StringVariantStageVisitorBuilder<T> optionalVariant(
                @Nonnull Function<Optional<String>, T> optionalVariantVisitor) {
            Preconditions.checkNotNull(optionalVariantVisitor, "optionalVariantVisitor cannot be null");
            this.optionalVariantVisitor = optionalVariantVisitor;
            return this;
        }

        @Override
        public UnknownStageVisitorBuilder<T> stringVariant(@Nonnull Function<String, T> stringVariantVisitor) {
            Preconditions.checkNotNull(stringVariantVisitor, "stringVariantVisitor cannot be null");
            this.stringVariantVisitor = stringVariantVisitor;
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
                        "Unknown variant of the 'UnionExample' union", SafeArg.of("unknownType", unknownType));
            };
            return this;
        }

        @Override
        public Visitor<T> build() {
            final Function<List<String>, T> collectionVariantVisitor = this.collectionVariantVisitor;
            final IntFunction<T> intVariantVisitor = this.intVariantVisitor;
            final Function<ObjectReference, T> objectVariantVisitor = this.objectVariantVisitor;
            final Function<Optional<String>, T> optionalVariantVisitor = this.optionalVariantVisitor;
            final Function<String, T> stringVariantVisitor = this.stringVariantVisitor;
            final Function<String, T> unknownVisitor = this.unknownVisitor;
            return new Visitor<T>() {
                @Override
                public T visitCollectionVariant(List<String> value) {
                    return collectionVariantVisitor.apply(value);
                }

                @Override
                public T visitIntVariant(int value) {
                    return intVariantVisitor.apply(value);
                }

                @Override
                public T visitObjectVariant(ObjectReference value) {
                    return objectVariantVisitor.apply(value);
                }

                @Override
                public T visitOptionalVariant(Optional<String> value) {
                    return optionalVariantVisitor.apply(value);
                }

                @Override
                public T visitStringVariant(String value) {
                    return stringVariantVisitor.apply(value);
                }

                @Override
                public T visitUnknown(String value) {
                    return unknownVisitor.apply(value);
                }
            };
        }
    }

    public interface CollectionVariantStageVisitorBuilder<T> {
        IntVariantStageVisitorBuilder<T> collectionVariant(@Nonnull Function<List<String>, T> collectionVariantVisitor);
    }

    public interface IntVariantStageVisitorBuilder<T> {
        ObjectVariantStageVisitorBuilder<T> intVariant(@Nonnull IntFunction<T> intVariantVisitor);
    }

    public interface ObjectVariantStageVisitorBuilder<T> {
        OptionalVariantStageVisitorBuilder<T> objectVariant(@Nonnull Function<ObjectReference, T> objectVariantVisitor);
    }

    public interface OptionalVariantStageVisitorBuilder<T> {
        StringVariantStageVisitorBuilder<T> optionalVariant(
                @Nonnull Function<Optional<String>, T> optionalVariantVisitor);
    }

    public interface StringVariantStageVisitorBuilder<T> {
        UnknownStageVisitorBuilder<T> stringVariant(@Nonnull Function<String, T> stringVariantVisitor);
    }

    public interface UnknownStageVisitorBuilder<T> {
        Completed_StageVisitorBuilder<T> unknown(@Nonnull Function<String, T> unknownVisitor);

        Completed_StageVisitorBuilder<T> throwOnUnknown();
    }

    public interface Completed_StageVisitorBuilder<T> {
        Visitor<T> build();
    }
}
