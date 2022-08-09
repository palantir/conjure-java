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
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import javax.annotation.Nonnull;
import javax.annotation.processing.Generated;

/**
 * A type which can either be a StringExample, a set of strings, or an integer.
 */
@Generated("com.palantir.conjure.java.types.UnionGenerator")
public final class UnionTypeExample {
    private final Base value;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
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

    public static UnionTypeExample set(Set<String> value) {
        return new UnionTypeExample(new SetWrapper(value));
    }

    public static UnionTypeExample map(Map<String, String> value) {
        return new UnionTypeExample(new MapWrapper(value));
    }

    public static UnionTypeExample optionalAlias(OptionalAlias value) {
        return new UnionTypeExample(new OptionalAliasWrapper(value));
    }

    public static UnionTypeExample listAlias(ListAlias value) {
        return new UnionTypeExample(new ListAliasWrapper(value));
    }

    public static UnionTypeExample setAlias(SetAlias value) {
        return new UnionTypeExample(new SetAliasWrapper(value));
    }

    public static UnionTypeExample mapAlias(MapAliasExample value) {
        return new UnionTypeExample(new MapAliasWrapper(value));
    }

    public static UnionTypeExample unknown(@Safe String type, Object value) {
        switch (Preconditions.checkNotNull(type, "Type is required")) {
            case "stringExample":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: stringExample");
            case "thisFieldIsAnInteger":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: thisFieldIsAnInteger");
            case "alsoAnInteger":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: alsoAnInteger");
            case "if":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: if");
            case "new":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: new");
            case "interface":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: interface");
            case "completed":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: completed");
            case "unknown":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: unknown");
            case "optional":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: optional");
            case "list":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: list");
            case "set":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: set");
            case "map":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: map");
            case "optionalAlias":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: optionalAlias");
            case "listAlias":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: listAlias");
            case "setAlias":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: setAlias");
            case "mapAlias":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: mapAlias");
            default:
                return new UnionTypeExample(new UnknownWrapper(type, Collections.singletonMap(type, value)));
        }
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
        return this.value.hashCode();
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

        T visitThisFieldIsAnInteger(int value);

        T visitAlsoAnInteger(int value);

        T visitIf(int value);

        T visitNew(int value);

        T visitInterface(int value);

        T visitCompleted(int value);

        T visitUnknown_(int value);

        T visitOptional(Optional<String> value);

        T visitList(List<String> value);

        T visitSet(Set<String> value);

        T visitMap(Map<String, String> value);

        T visitOptionalAlias(OptionalAlias value);

        T visitListAlias(ListAlias value);

        T visitSetAlias(SetAlias value);

        T visitMapAlias(MapAliasExample value);

        T visitUnknown(@Safe String unknownType, Object unknownValue);

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
                    ListAliasStageVisitorBuilder<T>,
                    MapStageVisitorBuilder<T>,
                    MapAliasStageVisitorBuilder<T>,
                    NewStageVisitorBuilder<T>,
                    OptionalStageVisitorBuilder<T>,
                    OptionalAliasStageVisitorBuilder<T>,
                    SetStageVisitorBuilder<T>,
                    SetAliasStageVisitorBuilder<T>,
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

        private Function<ListAlias, T> listAliasVisitor;

        private Function<Map<String, String>, T> mapVisitor;

        private Function<MapAliasExample, T> mapAliasVisitor;

        private IntFunction<T> newVisitor;

        private Function<Optional<String>, T> optionalVisitor;

        private Function<OptionalAlias, T> optionalAliasVisitor;

        private Function<Set<String>, T> setVisitor;

        private Function<SetAlias, T> setAliasVisitor;

        private Function<StringExample, T> stringExampleVisitor;

        private IntFunction<T> thisFieldIsAnIntegerVisitor;

        private IntFunction<T> unknown_Visitor;

        private BiFunction<@Safe String, Object, T> unknownVisitor;

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
        public ListAliasStageVisitorBuilder<T> list(@Nonnull Function<List<String>, T> listVisitor) {
            Preconditions.checkNotNull(listVisitor, "listVisitor cannot be null");
            this.listVisitor = listVisitor;
            return this;
        }

        @Override
        public MapStageVisitorBuilder<T> listAlias(@Nonnull Function<ListAlias, T> listAliasVisitor) {
            Preconditions.checkNotNull(listAliasVisitor, "listAliasVisitor cannot be null");
            this.listAliasVisitor = listAliasVisitor;
            return this;
        }

        @Override
        public MapAliasStageVisitorBuilder<T> map(@Nonnull Function<Map<String, String>, T> mapVisitor) {
            Preconditions.checkNotNull(mapVisitor, "mapVisitor cannot be null");
            this.mapVisitor = mapVisitor;
            return this;
        }

        @Override
        public NewStageVisitorBuilder<T> mapAlias(@Nonnull Function<MapAliasExample, T> mapAliasVisitor) {
            Preconditions.checkNotNull(mapAliasVisitor, "mapAliasVisitor cannot be null");
            this.mapAliasVisitor = mapAliasVisitor;
            return this;
        }

        @Override
        public OptionalStageVisitorBuilder<T> new_(@Nonnull IntFunction<T> newVisitor) {
            Preconditions.checkNotNull(newVisitor, "newVisitor cannot be null");
            this.newVisitor = newVisitor;
            return this;
        }

        @Override
        public OptionalAliasStageVisitorBuilder<T> optional(@Nonnull Function<Optional<String>, T> optionalVisitor) {
            Preconditions.checkNotNull(optionalVisitor, "optionalVisitor cannot be null");
            this.optionalVisitor = optionalVisitor;
            return this;
        }

        @Override
        public SetStageVisitorBuilder<T> optionalAlias(@Nonnull Function<OptionalAlias, T> optionalAliasVisitor) {
            Preconditions.checkNotNull(optionalAliasVisitor, "optionalAliasVisitor cannot be null");
            this.optionalAliasVisitor = optionalAliasVisitor;
            return this;
        }

        @Override
        public SetAliasStageVisitorBuilder<T> set(@Nonnull Function<Set<String>, T> setVisitor) {
            Preconditions.checkNotNull(setVisitor, "setVisitor cannot be null");
            this.setVisitor = setVisitor;
            return this;
        }

        @Override
        public StringExampleStageVisitorBuilder<T> setAlias(@Nonnull Function<SetAlias, T> setAliasVisitor) {
            Preconditions.checkNotNull(setAliasVisitor, "setAliasVisitor cannot be null");
            this.setAliasVisitor = setAliasVisitor;
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
                        "Unknown variant of the 'UnionTypeExample' union", SafeArg.of("unknownType", unknownType));
            };
            return this;
        }

        @Override
        public Visitor<T> build() {
            final IntFunction<T> alsoAnIntegerVisitor = this.alsoAnIntegerVisitor;
            final IntFunction<T> completedVisitor = this.completedVisitor;
            final IntFunction<T> ifVisitor = this.ifVisitor;
            final IntFunction<T> interfaceVisitor = this.interfaceVisitor;
            final Function<List<String>, T> listVisitor = this.listVisitor;
            final Function<ListAlias, T> listAliasVisitor = this.listAliasVisitor;
            final Function<Map<String, String>, T> mapVisitor = this.mapVisitor;
            final Function<MapAliasExample, T> mapAliasVisitor = this.mapAliasVisitor;
            final IntFunction<T> newVisitor = this.newVisitor;
            final Function<Optional<String>, T> optionalVisitor = this.optionalVisitor;
            final Function<OptionalAlias, T> optionalAliasVisitor = this.optionalAliasVisitor;
            final Function<Set<String>, T> setVisitor = this.setVisitor;
            final Function<SetAlias, T> setAliasVisitor = this.setAliasVisitor;
            final Function<StringExample, T> stringExampleVisitor = this.stringExampleVisitor;
            final IntFunction<T> thisFieldIsAnIntegerVisitor = this.thisFieldIsAnIntegerVisitor;
            final IntFunction<T> unknown_Visitor = this.unknown_Visitor;
            final BiFunction<@Safe String, Object, T> unknownVisitor = this.unknownVisitor;
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
                public T visitListAlias(ListAlias value) {
                    return listAliasVisitor.apply(value);
                }

                @Override
                public T visitMap(Map<String, String> value) {
                    return mapVisitor.apply(value);
                }

                @Override
                public T visitMapAlias(MapAliasExample value) {
                    return mapAliasVisitor.apply(value);
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
                public T visitOptionalAlias(OptionalAlias value) {
                    return optionalAliasVisitor.apply(value);
                }

                @Override
                public T visitSet(Set<String> value) {
                    return setVisitor.apply(value);
                }

                @Override
                public T visitSetAlias(SetAlias value) {
                    return setAliasVisitor.apply(value);
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
                public T visitUnknown(String unknownType, Object unknownValue) {
                    return unknownVisitor.apply(unknownType, unknownValue);
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
        ListAliasStageVisitorBuilder<T> list(@Nonnull Function<List<String>, T> listVisitor);
    }

    public interface ListAliasStageVisitorBuilder<T> {
        MapStageVisitorBuilder<T> listAlias(@Nonnull Function<ListAlias, T> listAliasVisitor);
    }

    public interface MapStageVisitorBuilder<T> {
        MapAliasStageVisitorBuilder<T> map(@Nonnull Function<Map<String, String>, T> mapVisitor);
    }

    public interface MapAliasStageVisitorBuilder<T> {
        NewStageVisitorBuilder<T> mapAlias(@Nonnull Function<MapAliasExample, T> mapAliasVisitor);
    }

    public interface NewStageVisitorBuilder<T> {
        OptionalStageVisitorBuilder<T> new_(@Nonnull IntFunction<T> newVisitor);
    }

    public interface OptionalStageVisitorBuilder<T> {
        OptionalAliasStageVisitorBuilder<T> optional(@Nonnull Function<Optional<String>, T> optionalVisitor);
    }

    public interface OptionalAliasStageVisitorBuilder<T> {
        SetStageVisitorBuilder<T> optionalAlias(@Nonnull Function<OptionalAlias, T> optionalAliasVisitor);
    }

    public interface SetStageVisitorBuilder<T> {
        SetAliasStageVisitorBuilder<T> set(@Nonnull Function<Set<String>, T> setVisitor);
    }

    public interface SetAliasStageVisitorBuilder<T> {
        StringExampleStageVisitorBuilder<T> setAlias(@Nonnull Function<SetAlias, T> setAliasVisitor);
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
        Completed_StageVisitorBuilder<T> unknown(@Nonnull BiFunction<@Safe String, Object, T> unknownVisitor);

        Completed_StageVisitorBuilder<T> unknown(@Nonnull Function<@Safe String, T> unknownVisitor);

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
    @JsonSubTypes({
        @JsonSubTypes.Type(StringExampleWrapper.class),
        @JsonSubTypes.Type(ThisFieldIsAnIntegerWrapper.class),
        @JsonSubTypes.Type(AlsoAnIntegerWrapper.class),
        @JsonSubTypes.Type(IfWrapper.class),
        @JsonSubTypes.Type(NewWrapper.class),
        @JsonSubTypes.Type(InterfaceWrapper.class),
        @JsonSubTypes.Type(CompletedWrapper.class),
        @JsonSubTypes.Type(Unknown_Wrapper.class),
        @JsonSubTypes.Type(OptionalWrapper.class),
        @JsonSubTypes.Type(ListWrapper.class),
        @JsonSubTypes.Type(SetWrapper.class),
        @JsonSubTypes.Type(MapWrapper.class),
        @JsonSubTypes.Type(OptionalAliasWrapper.class),
        @JsonSubTypes.Type(ListAliasWrapper.class),
        @JsonSubTypes.Type(SetAliasWrapper.class),
        @JsonSubTypes.Type(MapAliasWrapper.class)
    })
    @JsonIgnoreProperties(ignoreUnknown = true)
    private interface Base {
        <T> T accept(Visitor<T> visitor);
    }

    @JsonTypeName("stringExample")
    private static final class StringExampleWrapper implements Base {
        private final StringExample value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private StringExampleWrapper(@JsonSetter("stringExample") @Nonnull StringExample value) {
            Preconditions.checkNotNull(value, "stringExample cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "stringExample";
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
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "StringExampleWrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("thisFieldIsAnInteger")
    private static final class ThisFieldIsAnIntegerWrapper implements Base {
        private final int value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private ThisFieldIsAnIntegerWrapper(@JsonSetter("thisFieldIsAnInteger") @Nonnull int value) {
            Preconditions.checkNotNull(value, "thisFieldIsAnInteger cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "thisFieldIsAnInteger";
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
            return this.value;
        }

        @Override
        public String toString() {
            return "ThisFieldIsAnIntegerWrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("alsoAnInteger")
    private static final class AlsoAnIntegerWrapper implements Base {
        private final int value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private AlsoAnIntegerWrapper(@JsonSetter("alsoAnInteger") @Nonnull int value) {
            Preconditions.checkNotNull(value, "alsoAnInteger cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "alsoAnInteger";
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
            return this.value;
        }

        @Override
        public String toString() {
            return "AlsoAnIntegerWrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("if")
    private static final class IfWrapper implements Base {
        private final int value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private IfWrapper(@JsonSetter("if") @Nonnull int value) {
            Preconditions.checkNotNull(value, "if cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "if";
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
            return this.value;
        }

        @Override
        public String toString() {
            return "IfWrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("new")
    private static final class NewWrapper implements Base {
        private final int value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private NewWrapper(@JsonSetter("new") @Nonnull int value) {
            Preconditions.checkNotNull(value, "new cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "new";
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
            return this.value;
        }

        @Override
        public String toString() {
            return "NewWrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("interface")
    private static final class InterfaceWrapper implements Base {
        private final int value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private InterfaceWrapper(@JsonSetter("interface") @Nonnull int value) {
            Preconditions.checkNotNull(value, "interface cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "interface";
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
            return this.value;
        }

        @Override
        public String toString() {
            return "InterfaceWrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("completed")
    private static final class CompletedWrapper implements Base {
        private final int value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private CompletedWrapper(@JsonSetter("completed") @Nonnull int value) {
            Preconditions.checkNotNull(value, "completed cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "completed";
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
            return this.value;
        }

        @Override
        public String toString() {
            return "CompletedWrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("unknown")
    private static final class Unknown_Wrapper implements Base {
        private final int value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private Unknown_Wrapper(@JsonSetter("unknown") @Nonnull int value) {
            Preconditions.checkNotNull(value, "unknown_ cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "unknown";
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
            return this.value;
        }

        @Override
        public String toString() {
            return "Unknown_Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("optional")
    private static final class OptionalWrapper implements Base {
        private final Optional<String> value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private OptionalWrapper(
                @JsonSetter(value = "optional", nulls = Nulls.AS_EMPTY) @Nonnull Optional<String> value) {
            Preconditions.checkNotNull(value, "optional cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "optional";
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
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "OptionalWrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("list")
    private static final class ListWrapper implements Base {
        private final List<String> value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private ListWrapper(@JsonSetter(value = "list", nulls = Nulls.AS_EMPTY) @Nonnull List<String> value) {
            Preconditions.checkNotNull(value, "list cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "list";
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
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "ListWrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("set")
    private static final class SetWrapper implements Base {
        private final Set<String> value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private SetWrapper(@JsonSetter(value = "set", nulls = Nulls.AS_EMPTY) @Nonnull Set<String> value) {
            Preconditions.checkNotNull(value, "set cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "set";
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
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "SetWrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("map")
    private static final class MapWrapper implements Base {
        private final Map<String, String> value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private MapWrapper(@JsonSetter(value = "map", nulls = Nulls.AS_EMPTY) @Nonnull Map<String, String> value) {
            Preconditions.checkNotNull(value, "map cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "map";
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
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "MapWrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("optionalAlias")
    private static final class OptionalAliasWrapper implements Base {
        private final OptionalAlias value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private OptionalAliasWrapper(
                @JsonSetter(value = "optionalAlias", nulls = Nulls.AS_EMPTY) @Nonnull OptionalAlias value) {
            Preconditions.checkNotNull(value, "optionalAlias cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "optionalAlias";
        }

        @JsonProperty("optionalAlias")
        private OptionalAlias getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitOptionalAlias(value);
        }

        @Override
        public boolean equals(Object other) {
            return this == other || (other instanceof OptionalAliasWrapper && equalTo((OptionalAliasWrapper) other));
        }

        private boolean equalTo(OptionalAliasWrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "OptionalAliasWrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("listAlias")
    private static final class ListAliasWrapper implements Base {
        private final ListAlias value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private ListAliasWrapper(@JsonSetter(value = "listAlias", nulls = Nulls.AS_EMPTY) @Nonnull ListAlias value) {
            Preconditions.checkNotNull(value, "listAlias cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "listAlias";
        }

        @JsonProperty("listAlias")
        private ListAlias getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitListAlias(value);
        }

        @Override
        public boolean equals(Object other) {
            return this == other || (other instanceof ListAliasWrapper && equalTo((ListAliasWrapper) other));
        }

        private boolean equalTo(ListAliasWrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "ListAliasWrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("setAlias")
    private static final class SetAliasWrapper implements Base {
        private final SetAlias value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private SetAliasWrapper(@JsonSetter(value = "setAlias", nulls = Nulls.AS_EMPTY) @Nonnull SetAlias value) {
            Preconditions.checkNotNull(value, "setAlias cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "setAlias";
        }

        @JsonProperty("setAlias")
        private SetAlias getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitSetAlias(value);
        }

        @Override
        public boolean equals(Object other) {
            return this == other || (other instanceof SetAliasWrapper && equalTo((SetAliasWrapper) other));
        }

        private boolean equalTo(SetAliasWrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "SetAliasWrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("mapAlias")
    private static final class MapAliasWrapper implements Base {
        private final MapAliasExample value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private MapAliasWrapper(
                @JsonSetter(value = "mapAlias", nulls = Nulls.AS_EMPTY) @Nonnull MapAliasExample value) {
            Preconditions.checkNotNull(value, "mapAlias cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "mapAlias";
        }

        @JsonProperty("mapAlias")
        private MapAliasExample getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitMapAlias(value);
        }

        @Override
        public boolean equals(Object other) {
            return this == other || (other instanceof MapAliasWrapper && equalTo((MapAliasWrapper) other));
        }

        private boolean equalTo(MapAliasWrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "MapAliasWrapper{value: " + value + '}';
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
            return visitor.visitUnknown(type, value.get(type));
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
