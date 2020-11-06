package com.palantir.product;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntFunction;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

/**
 * A type which can either be a StringExample, a set of strings, or an integer.
 */
@Generated("com.palantir.conjure.java.types.UnionGenerator")
public final class UnionTypeExample {
    private final Base value;

    @JsonCreator
    private UnionTypeExample(Base value) {
        this.value = value;
    }

    @JsonValue
    private Base getValue() {
        return value;
    }

    /**
     * Docs for when UnionTypeExample is of type StringExample.
     */
    public static UnionTypeExample stringExample(StringExample value) {
        return new UnionTypeExample(new StringExampleWrapper(value));
    }

    public static UnionTypeExample set(Set<String> value) {
        return new UnionTypeExample(new SetWrapper(value));
    }

    public static UnionTypeExample thisFieldIsAnInteger(int value) {
        return new UnionTypeExample(new ThisFieldIsAnIntegerWrapper(value));
    }

    public static UnionTypeExample alsoAnInteger(int value) {
        return new UnionTypeExample(new AlsoAnIntegerWrapper(value));
    }

    public static UnionTypeExample if_(int value) {
        return new UnionTypeExample(new IfWrapper(value));
    }

    public static UnionTypeExample new_(int value) {
        return new UnionTypeExample(new NewWrapper(value));
    }

    public static UnionTypeExample interface_(int value) {
        return new UnionTypeExample(new InterfaceWrapper(value));
    }

    public static UnionTypeExample completed(int value) {
        return new UnionTypeExample(new CompletedWrapper(value));
    }

    public static UnionTypeExample unknown_(int value) {
        return new UnionTypeExample(new Unknown_Wrapper(value));
    }

    public static UnionTypeExample optional(Optional<String> value) {
        return new UnionTypeExample(new OptionalWrapper(value));
    }

    public static UnionTypeExample list(List<String> value) {
        return new UnionTypeExample(new ListWrapper(value));
    }

    public static UnionTypeExample map(Map<String, String> value) {
        return new UnionTypeExample(new MapWrapper(value));
    }

    public <T> T accept(Visitor<T> visitor) {
        return value.accept(visitor);
    }

    @Override
    public boolean equals(Object other) {
        return this == other || (other instanceof UnionTypeExample && equalTo((UnionTypeExample) other));
    }

    private boolean equalTo(UnionTypeExample other) {
        return this.value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.value);
    }

    @Override
    public String toString() {
        return "UnionTypeExample{value: " + value + '}';
    }

    public interface Visitor<T> {
        /**
         * Docs for when UnionTypeExample is of type StringExample.
         */
        T visitStringExample(StringExample value);

        T visitSet(Set<String> value);

        T visitThisFieldIsAnInteger(int value);

        T visitAlsoAnInteger(int value);

        T visitIf(int value);

        T visitNew(int value);

        T visitInterface(int value);

        T visitCompleted(int value);

        T visitUnknown_(int value);

        T visitOptional(Optional<String> value);

        T visitList(List<String> value);

        T visitMap(Map<String, String> value);

        T visitUnknown(String unknownType);

        static <T> AlsoAnIntegerStageVisitorBuilder<T> builder() {
            return new VisitorBuilder<T>();
        }
    }

    private static final class VisitorBuilder<T>
            implements AlsoAnIntegerStageVisitorBuilder<T>,
                    CompletedStageVisitorBuilder<T>,
                    IfStageVisitorBuilder<T>,
                    InterfaceStageVisitorBuilder<T>,
                    ListStageVisitorBuilder<T>,
                    MapStageVisitorBuilder<T>,
                    NewStageVisitorBuilder<T>,
                    OptionalStageVisitorBuilder<T>,
                    SetStageVisitorBuilder<T>,
                    StringExampleStageVisitorBuilder<T>,
                    ThisFieldIsAnIntegerStageVisitorBuilder<T>,
                    Unknown_StageVisitorBuilder<T>,
                    UnknownStageVisitorBuilder<T>,
                    Completed_StageVisitorBuilder<T> {
        private IntFunction<T> alsoAnIntegerVisitor;

        private IntFunction<T> completedVisitor;

        private IntFunction<T> ifVisitor;

        private IntFunction<T> interfaceVisitor;

        private Function<List<String>, T> listVisitor;

        private Function<Map<String, String>, T> mapVisitor;

        private IntFunction<T> newVisitor;

        private Function<Optional<String>, T> optionalVisitor;

        private Function<Set<String>, T> setVisitor;

        private Function<StringExample, T> stringExampleVisitor;

        private IntFunction<T> thisFieldIsAnIntegerVisitor;

        private IntFunction<T> unknown_Visitor;

        private Function<String, T> unknownVisitor;

        @Override
        public CompletedStageVisitorBuilder<T> alsoAnInteger(@Nonnull IntFunction<T> alsoAnIntegerVisitor) {
            Preconditions.checkNotNull(alsoAnIntegerVisitor, "alsoAnIntegerVisitor cannot be null");
            this.alsoAnIntegerVisitor = alsoAnIntegerVisitor;
            return this;
        }

        @Override
        public IfStageVisitorBuilder<T> completed(@Nonnull IntFunction<T> completedVisitor) {
            Preconditions.checkNotNull(completedVisitor, "completedVisitor cannot be null");
            this.completedVisitor = completedVisitor;
            return this;
        }

        @Override
        public InterfaceStageVisitorBuilder<T> if_(@Nonnull IntFunction<T> ifVisitor) {
            Preconditions.checkNotNull(ifVisitor, "ifVisitor cannot be null");
            this.ifVisitor = ifVisitor;
            return this;
        }

        @Override
        public ListStageVisitorBuilder<T> interface_(@Nonnull IntFunction<T> interfaceVisitor) {
            Preconditions.checkNotNull(interfaceVisitor, "interfaceVisitor cannot be null");
            this.interfaceVisitor = interfaceVisitor;
            return this;
        }

        @Override
        public MapStageVisitorBuilder<T> list(@Nonnull Function<List<String>, T> listVisitor) {
            Preconditions.checkNotNull(listVisitor, "listVisitor cannot be null");
            this.listVisitor = listVisitor;
            return this;
        }

        @Override
        public NewStageVisitorBuilder<T> map(@Nonnull Function<Map<String, String>, T> mapVisitor) {
            Preconditions.checkNotNull(mapVisitor, "mapVisitor cannot be null");
            this.mapVisitor = mapVisitor;
            return this;
        }

        @Override
        public OptionalStageVisitorBuilder<T> new_(@Nonnull IntFunction<T> newVisitor) {
            Preconditions.checkNotNull(newVisitor, "newVisitor cannot be null");
            this.newVisitor = newVisitor;
            return this;
        }

        @Override
        public SetStageVisitorBuilder<T> optional(@Nonnull Function<Optional<String>, T> optionalVisitor) {
            Preconditions.checkNotNull(optionalVisitor, "optionalVisitor cannot be null");
            this.optionalVisitor = optionalVisitor;
            return this;
        }

        @Override
        public StringExampleStageVisitorBuilder<T> set(@Nonnull Function<Set<String>, T> setVisitor) {
            Preconditions.checkNotNull(setVisitor, "setVisitor cannot be null");
            this.setVisitor = setVisitor;
            return this;
        }

        @Override
        public ThisFieldIsAnIntegerStageVisitorBuilder<T> stringExample(
                @Nonnull Function<StringExample, T> stringExampleVisitor) {
            Preconditions.checkNotNull(stringExampleVisitor, "stringExampleVisitor cannot be null");
            this.stringExampleVisitor = stringExampleVisitor;
            return this;
        }

        @Override
        public Unknown_StageVisitorBuilder<T> thisFieldIsAnInteger(
                @Nonnull IntFunction<T> thisFieldIsAnIntegerVisitor) {
            Preconditions.checkNotNull(thisFieldIsAnIntegerVisitor, "thisFieldIsAnIntegerVisitor cannot be null");
            this.thisFieldIsAnIntegerVisitor = thisFieldIsAnIntegerVisitor;
            return this;
        }

        @Override
        public UnknownStageVisitorBuilder<T> unknown_(@Nonnull IntFunction<T> unknown_Visitor) {
            Preconditions.checkNotNull(unknown_Visitor, "unknown_Visitor cannot be null");
            this.unknown_Visitor = unknown_Visitor;
            return this;
        }

        @Override
        public Completed_StageVisitorBuilder<T> unknown(@Nonnull Function<String, T> unknownVisitor) {
            Preconditions.checkNotNull(unknownVisitor, "unknownVisitor cannot be null");
            this.unknownVisitor = unknownVisitor;
            return this;
        }

        @Override
        public Visitor<T> build() {
            final IntFunction<T> alsoAnIntegerVisitor = this.alsoAnIntegerVisitor;
            final IntFunction<T> completedVisitor = this.completedVisitor;
            final IntFunction<T> ifVisitor = this.ifVisitor;
            final IntFunction<T> interfaceVisitor = this.interfaceVisitor;
            final Function<List<String>, T> listVisitor = this.listVisitor;
            final Function<Map<String, String>, T> mapVisitor = this.mapVisitor;
            final IntFunction<T> newVisitor = this.newVisitor;
            final Function<Optional<String>, T> optionalVisitor = this.optionalVisitor;
            final Function<Set<String>, T> setVisitor = this.setVisitor;
            final Function<StringExample, T> stringExampleVisitor = this.stringExampleVisitor;
            final IntFunction<T> thisFieldIsAnIntegerVisitor = this.thisFieldIsAnIntegerVisitor;
            final IntFunction<T> unknown_Visitor = this.unknown_Visitor;
            final Function<String, T> unknownVisitor = this.unknownVisitor;
            return new Visitor<T>() {
                @Override
                public T visitAlsoAnInteger(int value) {
                    return alsoAnIntegerVisitor.apply(value);
                }

                @Override
                public T visitCompleted(int value) {
                    return completedVisitor.apply(value);
                }

                @Override
                public T visitIf(int value) {
                    return ifVisitor.apply(value);
                }

                @Override
                public T visitInterface(int value) {
                    return interfaceVisitor.apply(value);
                }

                @Override
                public T visitList(List<String> value) {
                    return listVisitor.apply(value);
                }

                @Override
                public T visitMap(Map<String, String> value) {
                    return mapVisitor.apply(value);
                }

                @Override
                public T visitNew(int value) {
                    return newVisitor.apply(value);
                }

                @Override
                public T visitOptional(Optional<String> value) {
                    return optionalVisitor.apply(value);
                }

                @Override
                public T visitSet(Set<String> value) {
                    return setVisitor.apply(value);
                }

                @Override
                public T visitStringExample(StringExample value) {
                    return stringExampleVisitor.apply(value);
                }

                @Override
                public T visitThisFieldIsAnInteger(int value) {
                    return thisFieldIsAnIntegerVisitor.apply(value);
                }

                @Override
                public T visitUnknown_(int value) {
                    return unknown_Visitor.apply(value);
                }

                @Override
                public T visitUnknown(String value) {
                    return unknownVisitor.apply(value);
                }
            };
        }
    }

    public interface AlsoAnIntegerStageVisitorBuilder<T> {
        CompletedStageVisitorBuilder<T> alsoAnInteger(@Nonnull IntFunction<T> alsoAnIntegerVisitor);
    }

    public interface CompletedStageVisitorBuilder<T> {
        IfStageVisitorBuilder<T> completed(@Nonnull IntFunction<T> completedVisitor);
    }

    public interface IfStageVisitorBuilder<T> {
        InterfaceStageVisitorBuilder<T> if_(@Nonnull IntFunction<T> ifVisitor);
    }

    public interface InterfaceStageVisitorBuilder<T> {
        ListStageVisitorBuilder<T> interface_(@Nonnull IntFunction<T> interfaceVisitor);
    }

    public interface ListStageVisitorBuilder<T> {
        MapStageVisitorBuilder<T> list(@Nonnull Function<List<String>, T> listVisitor);
    }

    public interface MapStageVisitorBuilder<T> {
        NewStageVisitorBuilder<T> map(@Nonnull Function<Map<String, String>, T> mapVisitor);
    }

    public interface NewStageVisitorBuilder<T> {
        OptionalStageVisitorBuilder<T> new_(@Nonnull IntFunction<T> newVisitor);
    }

    public interface OptionalStageVisitorBuilder<T> {
        SetStageVisitorBuilder<T> optional(@Nonnull Function<Optional<String>, T> optionalVisitor);
    }

    public interface SetStageVisitorBuilder<T> {
        StringExampleStageVisitorBuilder<T> set(@Nonnull Function<Set<String>, T> setVisitor);
    }

    public interface StringExampleStageVisitorBuilder<T> {
        ThisFieldIsAnIntegerStageVisitorBuilder<T> stringExample(
                @Nonnull Function<StringExample, T> stringExampleVisitor);
    }

    public interface ThisFieldIsAnIntegerStageVisitorBuilder<T> {
        Unknown_StageVisitorBuilder<T> thisFieldIsAnInteger(@Nonnull IntFunction<T> thisFieldIsAnIntegerVisitor);
    }

    public interface Unknown_StageVisitorBuilder<T> {
        UnknownStageVisitorBuilder<T> unknown_(@Nonnull IntFunction<T> unknown_Visitor);
    }

    public interface UnknownStageVisitorBuilder<T> {
        Completed_StageVisitorBuilder<T> unknown(@Nonnull Function<String, T> unknownVisitor);
    }

    public interface Completed_StageVisitorBuilder<T> {
        Visitor<T> build();
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true, defaultImpl = UnknownWrapper.class)
    @JsonSubTypes({
        @JsonSubTypes.Type(StringExampleWrapper.class),
        @JsonSubTypes.Type(SetWrapper.class),
        @JsonSubTypes.Type(ThisFieldIsAnIntegerWrapper.class),
        @JsonSubTypes.Type(AlsoAnIntegerWrapper.class),
        @JsonSubTypes.Type(IfWrapper.class),
        @JsonSubTypes.Type(NewWrapper.class),
        @JsonSubTypes.Type(InterfaceWrapper.class),
        @JsonSubTypes.Type(CompletedWrapper.class),
        @JsonSubTypes.Type(Unknown_Wrapper.class),
        @JsonSubTypes.Type(OptionalWrapper.class),
        @JsonSubTypes.Type(ListWrapper.class),
        @JsonSubTypes.Type(MapWrapper.class)
    })
    @JsonIgnoreProperties(ignoreUnknown = true)
    private interface Base {
        <T> T accept(Visitor<T> visitor);
    }

    @JsonTypeName("stringExample")
    private static final class StringExampleWrapper implements Base {
        private final StringExample value;

        @JsonCreator
        private StringExampleWrapper(@JsonSetter("stringExample") @Nonnull StringExample value) {
            Preconditions.checkNotNull(value, "stringExample cannot be null");
            this.value = value;
        }

        @JsonProperty("stringExample")
        private StringExample getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitStringExample(value);
        }

        @Override
        public boolean equals(Object other) {
            return this == other || (other instanceof StringExampleWrapper && equalTo((StringExampleWrapper) other));
        }

        private boolean equalTo(StringExampleWrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(this.value);
        }

        @Override
        public String toString() {
            return "StringExampleWrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("set")
    private static final class SetWrapper implements Base {
        private final Set<String> value;

        @JsonCreator
        private SetWrapper(@JsonSetter(value = "set", nulls = Nulls.AS_EMPTY) @Nonnull Set<String> value) {
            Preconditions.checkNotNull(value, "set cannot be null");
            this.value = value;
        }

        @JsonProperty("set")
        private Set<String> getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitSet(value);
        }

        @Override
        public boolean equals(Object other) {
            return this == other || (other instanceof SetWrapper && equalTo((SetWrapper) other));
        }

        private boolean equalTo(SetWrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(this.value);
        }

        @Override
        public String toString() {
            return "SetWrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("thisFieldIsAnInteger")
    private static final class ThisFieldIsAnIntegerWrapper implements Base {
        private final int value;

        @JsonCreator
        private ThisFieldIsAnIntegerWrapper(@JsonSetter("thisFieldIsAnInteger") @Nonnull int value) {
            Preconditions.checkNotNull(value, "thisFieldIsAnInteger cannot be null");
            this.value = value;
        }

        @JsonProperty("thisFieldIsAnInteger")
        private int getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitThisFieldIsAnInteger(value);
        }

        @Override
        public boolean equals(Object other) {
            return this == other
                    || (other instanceof ThisFieldIsAnIntegerWrapper && equalTo((ThisFieldIsAnIntegerWrapper) other));
        }

        private boolean equalTo(ThisFieldIsAnIntegerWrapper other) {
            return this.value == other.value;
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(this.value);
        }

        @Override
        public String toString() {
            return "ThisFieldIsAnIntegerWrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("alsoAnInteger")
    private static final class AlsoAnIntegerWrapper implements Base {
        private final int value;

        @JsonCreator
        private AlsoAnIntegerWrapper(@JsonSetter("alsoAnInteger") @Nonnull int value) {
            Preconditions.checkNotNull(value, "alsoAnInteger cannot be null");
            this.value = value;
        }

        @JsonProperty("alsoAnInteger")
        private int getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitAlsoAnInteger(value);
        }

        @Override
        public boolean equals(Object other) {
            return this == other || (other instanceof AlsoAnIntegerWrapper && equalTo((AlsoAnIntegerWrapper) other));
        }

        private boolean equalTo(AlsoAnIntegerWrapper other) {
            return this.value == other.value;
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(this.value);
        }

        @Override
        public String toString() {
            return "AlsoAnIntegerWrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("if")
    private static final class IfWrapper implements Base {
        private final int value;

        @JsonCreator
        private IfWrapper(@JsonSetter("if") @Nonnull int value) {
            Preconditions.checkNotNull(value, "if cannot be null");
            this.value = value;
        }

        @JsonProperty("if")
        private int getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitIf(value);
        }

        @Override
        public boolean equals(Object other) {
            return this == other || (other instanceof IfWrapper && equalTo((IfWrapper) other));
        }

        private boolean equalTo(IfWrapper other) {
            return this.value == other.value;
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(this.value);
        }

        @Override
        public String toString() {
            return "IfWrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("new")
    private static final class NewWrapper implements Base {
        private final int value;

        @JsonCreator
        private NewWrapper(@JsonSetter("new") @Nonnull int value) {
            Preconditions.checkNotNull(value, "new cannot be null");
            this.value = value;
        }

        @JsonProperty("new")
        private int getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitNew(value);
        }

        @Override
        public boolean equals(Object other) {
            return this == other || (other instanceof NewWrapper && equalTo((NewWrapper) other));
        }

        private boolean equalTo(NewWrapper other) {
            return this.value == other.value;
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(this.value);
        }

        @Override
        public String toString() {
            return "NewWrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("interface")
    private static final class InterfaceWrapper implements Base {
        private final int value;

        @JsonCreator
        private InterfaceWrapper(@JsonSetter("interface") @Nonnull int value) {
            Preconditions.checkNotNull(value, "interface cannot be null");
            this.value = value;
        }

        @JsonProperty("interface")
        private int getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitInterface(value);
        }

        @Override
        public boolean equals(Object other) {
            return this == other || (other instanceof InterfaceWrapper && equalTo((InterfaceWrapper) other));
        }

        private boolean equalTo(InterfaceWrapper other) {
            return this.value == other.value;
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(this.value);
        }

        @Override
        public String toString() {
            return "InterfaceWrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("completed")
    private static final class CompletedWrapper implements Base {
        private final int value;

        @JsonCreator
        private CompletedWrapper(@JsonSetter("completed") @Nonnull int value) {
            Preconditions.checkNotNull(value, "completed cannot be null");
            this.value = value;
        }

        @JsonProperty("completed")
        private int getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitCompleted(value);
        }

        @Override
        public boolean equals(Object other) {
            return this == other || (other instanceof CompletedWrapper && equalTo((CompletedWrapper) other));
        }

        private boolean equalTo(CompletedWrapper other) {
            return this.value == other.value;
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(this.value);
        }

        @Override
        public String toString() {
            return "CompletedWrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("unknown")
    private static final class Unknown_Wrapper implements Base {
        private final int value;

        @JsonCreator
        private Unknown_Wrapper(@JsonSetter("unknown") @Nonnull int value) {
            Preconditions.checkNotNull(value, "unknown_ cannot be null");
            this.value = value;
        }

        @JsonProperty("unknown")
        private int getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitUnknown_(value);
        }

        @Override
        public boolean equals(Object other) {
            return this == other || (other instanceof Unknown_Wrapper && equalTo((Unknown_Wrapper) other));
        }

        private boolean equalTo(Unknown_Wrapper other) {
            return this.value == other.value;
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(this.value);
        }

        @Override
        public String toString() {
            return "Unknown_Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("optional")
    private static final class OptionalWrapper implements Base {
        private final Optional<String> value;

        @JsonCreator
        private OptionalWrapper(@JsonSetter("optional") @Nonnull Optional<String> value) {
            Preconditions.checkNotNull(value, "optional cannot be null");
            this.value = value;
        }

        @JsonProperty("optional")
        private Optional<String> getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitOptional(value);
        }

        @Override
        public boolean equals(Object other) {
            return this == other || (other instanceof OptionalWrapper && equalTo((OptionalWrapper) other));
        }

        private boolean equalTo(OptionalWrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(this.value);
        }

        @Override
        public String toString() {
            return "OptionalWrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("list")
    private static final class ListWrapper implements Base {
        private final List<String> value;

        @JsonCreator
        private ListWrapper(@JsonSetter(value = "list", nulls = Nulls.AS_EMPTY) @Nonnull List<String> value) {
            Preconditions.checkNotNull(value, "list cannot be null");
            this.value = value;
        }

        @JsonProperty("list")
        private List<String> getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitList(value);
        }

        @Override
        public boolean equals(Object other) {
            return this == other || (other instanceof ListWrapper && equalTo((ListWrapper) other));
        }

        private boolean equalTo(ListWrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(this.value);
        }

        @Override
        public String toString() {
            return "ListWrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("map")
    private static final class MapWrapper implements Base {
        private final Map<String, String> value;

        @JsonCreator
        private MapWrapper(@JsonSetter(value = "map", nulls = Nulls.AS_EMPTY) @Nonnull Map<String, String> value) {
            Preconditions.checkNotNull(value, "map cannot be null");
            this.value = value;
        }

        @JsonProperty("map")
        private Map<String, String> getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitMap(value);
        }

        @Override
        public boolean equals(Object other) {
            return this == other || (other instanceof MapWrapper && equalTo((MapWrapper) other));
        }

        private boolean equalTo(MapWrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(this.value);
        }

        @Override
        public String toString() {
            return "MapWrapper{value: " + value + '}';
        }
    }

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.EXISTING_PROPERTY,
            property = "type",
            visible = true)
    private static final class UnknownWrapper implements Base {
        private final String type;

        private final Map<String, Object> value;

        @JsonCreator
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
        public boolean equals(Object other) {
            return this == other || (other instanceof UnknownWrapper && equalTo((UnknownWrapper) other));
        }

        private boolean equalTo(UnknownWrapper other) {
            return this.type.equals(other.type) && this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.type, this.value);
        }

        @Override
        public String toString() {
            return "UnknownWrapper{type: " + type + ", value: " + value + '}';
        }
    }
}
