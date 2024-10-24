package com.company.product;

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
import com.fasterxml.jackson.annotation.Nulls;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.UnionGenerator")
public final class RecipeStep {
    private final Base value;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    private RecipeStep(Base value) {
        this.value = value;
    }

    @JsonValue
    private Base getValue() {
        return value;
    }

    public static RecipeStep mix(Set<Ingredient> value) {
        return new RecipeStep(new MixWrapper(value));
    }

    public static RecipeStep chop(Ingredient value) {
        return new RecipeStep(new ChopWrapper(value));
    }

    public static RecipeStep unknown(@Safe String type, Object value) {
        switch (Preconditions.checkNotNull(type, "Type is required")) {
            case "mix":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: mix");
            case "chop":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: chop");
            default:
                return new RecipeStep(new UnknownWrapper(type, Collections.singletonMap(type, value)));
        }
    }

    public <T> T accept(Visitor<T> visitor) {
        return value.accept(visitor);
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof RecipeStep && equalTo((RecipeStep) other));
    }

    private boolean equalTo(RecipeStep other) {
        return this.value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public String toString() {
        return "RecipeStep{value: " + value + '}';
    }

    public interface Visitor<T> {
        T visitMix(Set<Ingredient> value);

        T visitChop(Ingredient value);

        T visitUnknown(@Safe String unknownType);

        static <T> ChopStageVisitorBuilder<T> builder() {
            return new VisitorBuilder<T>();
        }
    }

    private static final class VisitorBuilder<T>
            implements ChopStageVisitorBuilder<T>,
                    MixStageVisitorBuilder<T>,
                    UnknownStageVisitorBuilder<T>,
                    Completed_StageVisitorBuilder<T> {
        private Function<Ingredient, T> chopVisitor;

        private Function<Set<Ingredient>, T> mixVisitor;

        private Function<String, T> unknownVisitor;

        @Override
        public MixStageVisitorBuilder<T> chop(@Nonnull Function<Ingredient, T> chopVisitor) {
            Preconditions.checkNotNull(chopVisitor, "chopVisitor cannot be null");
            this.chopVisitor = chopVisitor;
            return this;
        }

        @Override
        public UnknownStageVisitorBuilder<T> mix(@Nonnull Function<Set<Ingredient>, T> mixVisitor) {
            Preconditions.checkNotNull(mixVisitor, "mixVisitor cannot be null");
            this.mixVisitor = mixVisitor;
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
                        "Unknown variant of the 'RecipeStep' union", SafeArg.of("unknownType", unknownType));
            };
            return this;
        }

        @Override
        public Visitor<T> build() {
            final Function<Ingredient, T> chopVisitor = this.chopVisitor;
            final Function<Set<Ingredient>, T> mixVisitor = this.mixVisitor;
            final Function<String, T> unknownVisitor = this.unknownVisitor;
            return new Visitor<T>() {
                @Override
                public T visitChop(Ingredient value) {
                    return chopVisitor.apply(value);
                }

                @Override
                public T visitMix(Set<Ingredient> value) {
                    return mixVisitor.apply(value);
                }

                @Override
                public T visitUnknown(String value) {
                    return unknownVisitor.apply(value);
                }
            };
        }
    }

    public interface ChopStageVisitorBuilder<T> {
        MixStageVisitorBuilder<T> chop(@Nonnull Function<Ingredient, T> chopVisitor);
    }

    public interface MixStageVisitorBuilder<T> {
        UnknownStageVisitorBuilder<T> mix(@Nonnull Function<Set<Ingredient>, T> mixVisitor);
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
    @JsonSubTypes({@JsonSubTypes.Type(MixWrapper.class), @JsonSubTypes.Type(ChopWrapper.class)})
    @JsonIgnoreProperties(ignoreUnknown = true)
    private interface Base {
        <T> T accept(Visitor<T> visitor);
    }

    @JsonTypeName("mix")
    private static final class MixWrapper implements Base {
        private final Set<Ingredient> value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private MixWrapper(@JsonSetter(value = "mix", nulls = Nulls.AS_EMPTY) @Nonnull Set<Ingredient> value) {
            Preconditions.checkNotNull(value, "mix cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "mix";
        }

        @JsonProperty("mix")
        private Set<Ingredient> getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitMix(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof MixWrapper && equalTo((MixWrapper) other));
        }

        private boolean equalTo(MixWrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "MixWrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("chop")
    private static final class ChopWrapper implements Base {
        private final Ingredient value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private ChopWrapper(@JsonSetter("chop") @Nonnull Ingredient value) {
            Preconditions.checkNotNull(value, "chop cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "chop";
        }

        @JsonProperty("chop")
        private Ingredient getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitChop(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof ChopWrapper && equalTo((ChopWrapper) other));
        }

        private boolean equalTo(ChopWrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "ChopWrapper{value: " + value + '}';
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
