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
import com.fasterxml.jackson.annotation.Nulls;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.Collections;
import java.util.HashMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

/**
 * A type which can either be a StringExample, a set of strings, or an integer.
 */
@Generated("com.palantir.conjure.java.types.UnionGenerator")
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true,
        defaultImpl = UnionTypeExample.Unknown.class)
@JsonSubTypes({
    @JsonSubTypes.Type(StringExample.class),
    @JsonSubTypes.Type(ThisFieldIsAnInteger.class),
    @JsonSubTypes.Type(AlsoAnInteger.class),
    @JsonSubTypes.Type(If.class),
    @JsonSubTypes.Type(New.class),
    @JsonSubTypes.Type(Interface.class),
    @JsonSubTypes.Type(Completed.class),
    @JsonSubTypes.Type(Unknown_.class),
    @JsonSubTypes.Type(Known_.class),
    @JsonSubTypes.Type(Optional.class),
    @JsonSubTypes.Type(List.class),
    @JsonSubTypes.Type(Set.class),
    @JsonSubTypes.Type(Map.class),
    @JsonSubTypes.Type(OptionalAlias.class),
    @JsonSubTypes.Type(ListAlias.class),
    @JsonSubTypes.Type(SetAlias.class),
    @JsonSubTypes.Type(MapAlias.class)
})
@JsonIgnoreProperties(ignoreUnknown = true)
public sealed interface UnionTypeExample {
    /**
     * Docs for when UnionTypeExample is of type StringExample.
     */
    static UnionTypeExample stringExample(withvisitors.com.palantir.product.StringExample value) {
        return new StringExample(value);
    }

    static UnionTypeExample thisFieldIsAnInteger(int value) {
        return new ThisFieldIsAnInteger(value);
    }

    static UnionTypeExample alsoAnInteger(int value) {
        return new AlsoAnInteger(value);
    }

    static UnionTypeExample if_(int value) {
        return new If(value);
    }

    static UnionTypeExample new_(int value) {
        return new New(value);
    }

    static UnionTypeExample interface_(int value) {
        return new Interface(value);
    }

    static UnionTypeExample completed(int value) {
        return new Completed(value);
    }

    static UnionTypeExample unknown_(int value) {
        return new Unknown_(value);
    }

    static UnionTypeExample known_(String value) {
        return new Known_(value);
    }

    static UnionTypeExample optional(java.util.Optional<String> value) {
        return new Optional(value);
    }

    static UnionTypeExample list(java.util.List<String> value) {
        return new List(value);
    }

    static UnionTypeExample set(java.util.Set<String> value) {
        return new Set(value);
    }

    static UnionTypeExample map(java.util.Map<String, String> value) {
        return new Map(value);
    }

    static UnionTypeExample optionalAlias(withvisitors.com.palantir.product.OptionalAlias value) {
        return new OptionalAlias(value);
    }

    static UnionTypeExample listAlias(withvisitors.com.palantir.product.ListAlias value) {
        return new ListAlias(value);
    }

    static UnionTypeExample setAlias(withvisitors.com.palantir.product.SetAlias value) {
        return new SetAlias(value);
    }

    static UnionTypeExample mapAlias(MapAliasExample value) {
        return new MapAlias(value);
    }

    static UnionTypeExample unknown(@Safe String type, Object value) {
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
            case "known":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: known");
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

    sealed interface Known
            permits StringExample,
                    ThisFieldIsAnInteger,
                    AlsoAnInteger,
                    If,
                    New,
                    Interface,
                    Completed,
                    Unknown_,
                    Known_,
                    Optional,
                    List,
                    Set,
                    Map,
                    OptionalAlias,
                    ListAlias,
                    SetAlias,
                    MapAlias {}

    @JsonTypeName("stringExample")
    record StringExample(withvisitors.com.palantir.product.StringExample value) implements UnionTypeExample, Known {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        StringExample(@JsonSetter("stringExample") @Nonnull withvisitors.com.palantir.product.StringExample value) {
            Preconditions.checkNotNull(value, "stringExample cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "stringExample";
        }

        @JsonProperty("stringExample")
        private withvisitors.com.palantir.product.StringExample getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitStringExample(value);
        }

        @Override
        public String toString() {
            return "StringExample{value: " + value + '}';
        }
    }

    @JsonTypeName("thisFieldIsAnInteger")
    record ThisFieldIsAnInteger(int value) implements UnionTypeExample, Known {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        ThisFieldIsAnInteger(@JsonSetter("thisFieldIsAnInteger") @Nonnull int value) {
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
        public String toString() {
            return "ThisFieldIsAnInteger{value: " + value + '}';
        }
    }

    @JsonTypeName("alsoAnInteger")
    record AlsoAnInteger(int value) implements UnionTypeExample, Known {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        AlsoAnInteger(@JsonSetter("alsoAnInteger") @Nonnull int value) {
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
        public String toString() {
            return "AlsoAnInteger{value: " + value + '}';
        }
    }

    @JsonTypeName("if")
    record If(int value) implements UnionTypeExample, Known {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        If(@JsonSetter("if") @Nonnull int value) {
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
        public String toString() {
            return "If{value: " + value + '}';
        }
    }

    @JsonTypeName("new")
    record New(int value) implements UnionTypeExample, Known {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        New(@JsonSetter("new") @Nonnull int value) {
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
        public String toString() {
            return "New{value: " + value + '}';
        }
    }

    @JsonTypeName("interface")
    record Interface(int value) implements UnionTypeExample, Known {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        Interface(@JsonSetter("interface") @Nonnull int value) {
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
        public String toString() {
            return "Interface{value: " + value + '}';
        }
    }

    @JsonTypeName("completed")
    record Completed(int value) implements UnionTypeExample, Known {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        Completed(@JsonSetter("completed") @Nonnull int value) {
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
        public String toString() {
            return "Completed{value: " + value + '}';
        }
    }

    @JsonTypeName("unknown")
    record Unknown_(int value) implements UnionTypeExample, Known {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        Unknown_(@JsonSetter("unknown") @Nonnull int value) {
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
        public String toString() {
            return "Unknown_{value: " + value + '}';
        }
    }

    @JsonTypeName("known")
    record Known_(String value) implements UnionTypeExample, Known {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        Known_(@JsonSetter("known") @Nonnull String value) {
            Preconditions.checkNotNull(value, "known_ cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "known";
        }

        @JsonProperty("known")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitKnown_(value);
        }

        @Override
        public String toString() {
            return "Known_{value: " + value + '}';
        }
    }

    @JsonTypeName("optional")
    record Optional(java.util.Optional<String> value) implements UnionTypeExample, Known {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        Optional(@JsonSetter(value = "optional", nulls = Nulls.AS_EMPTY) @Nonnull java.util.Optional<String> value) {
            Preconditions.checkNotNull(value, "optional cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "optional";
        }

        @JsonProperty("optional")
        private java.util.Optional<String> getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitOptional(value);
        }

        @Override
        public String toString() {
            return "Optional{value: " + value + '}';
        }
    }

    @JsonTypeName("list")
    record List(java.util.List<String> value) implements UnionTypeExample, Known {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        List(@JsonSetter(value = "list", nulls = Nulls.AS_EMPTY) @Nonnull java.util.List<String> value) {
            Preconditions.checkNotNull(value, "list cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "list";
        }

        @JsonProperty("list")
        private java.util.List<String> getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitList(value);
        }

        @Override
        public String toString() {
            return "List{value: " + value + '}';
        }
    }

    @JsonTypeName("set")
    record Set(java.util.Set<String> value) implements UnionTypeExample, Known {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        Set(@JsonSetter(value = "set", nulls = Nulls.AS_EMPTY) @Nonnull java.util.Set<String> value) {
            Preconditions.checkNotNull(value, "set cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "set";
        }

        @JsonProperty("set")
        private java.util.Set<String> getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitSet(value);
        }

        @Override
        public String toString() {
            return "Set{value: " + value + '}';
        }
    }

    @JsonTypeName("map")
    record Map(java.util.Map<String, String> value) implements UnionTypeExample, Known {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        Map(@JsonSetter(value = "map", nulls = Nulls.AS_EMPTY) @Nonnull java.util.Map<String, String> value) {
            Preconditions.checkNotNull(value, "map cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "map";
        }

        @JsonProperty("map")
        private java.util.Map<String, String> getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitMap(value);
        }

        @Override
        public String toString() {
            return "Map{value: " + value + '}';
        }
    }

    @JsonTypeName("optionalAlias")
    record OptionalAlias(withvisitors.com.palantir.product.OptionalAlias value) implements UnionTypeExample, Known {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        OptionalAlias(
                @JsonSetter(value = "optionalAlias", nulls = Nulls.AS_EMPTY) @Nonnull
                        withvisitors.com.palantir.product.OptionalAlias value) {
            Preconditions.checkNotNull(value, "optionalAlias cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "optionalAlias";
        }

        @JsonProperty("optionalAlias")
        private withvisitors.com.palantir.product.OptionalAlias getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitOptionalAlias(value);
        }

        @Override
        public String toString() {
            return "OptionalAlias{value: " + value + '}';
        }
    }

    @JsonTypeName("listAlias")
    record ListAlias(withvisitors.com.palantir.product.ListAlias value) implements UnionTypeExample, Known {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        ListAlias(
                @JsonSetter(value = "listAlias", nulls = Nulls.AS_EMPTY) @Nonnull
                        withvisitors.com.palantir.product.ListAlias value) {
            Preconditions.checkNotNull(value, "listAlias cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "listAlias";
        }

        @JsonProperty("listAlias")
        private withvisitors.com.palantir.product.ListAlias getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitListAlias(value);
        }

        @Override
        public String toString() {
            return "ListAlias{value: " + value + '}';
        }
    }

    @JsonTypeName("setAlias")
    record SetAlias(withvisitors.com.palantir.product.SetAlias value) implements UnionTypeExample, Known {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        SetAlias(
                @JsonSetter(value = "setAlias", nulls = Nulls.AS_EMPTY) @Nonnull
                        withvisitors.com.palantir.product.SetAlias value) {
            Preconditions.checkNotNull(value, "setAlias cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "setAlias";
        }

        @JsonProperty("setAlias")
        private withvisitors.com.palantir.product.SetAlias getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitSetAlias(value);
        }

        @Override
        public String toString() {
            return "SetAlias{value: " + value + '}';
        }
    }

    @JsonTypeName("mapAlias")
    record MapAlias(MapAliasExample value) implements UnionTypeExample, Known {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        MapAlias(@JsonSetter(value = "mapAlias", nulls = Nulls.AS_EMPTY) @Nonnull MapAliasExample value) {
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
        public String toString() {
            return "MapAlias{value: " + value + '}';
        }
    }

    final class Unknown implements UnionTypeExample {
        private final String type;

        private final java.util.Map<String, Object> value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private Unknown(@JsonProperty("type") String type) {
            this(type, new HashMap<String, Object>());
        }

        private Unknown(@Nonnull String type, @Nonnull java.util.Map<String, Object> value) {
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
        private java.util.Map<String, Object> getValue() {
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
        /**
         * Docs for when UnionTypeExample is of type StringExample.
         */
        T visitStringExample(withvisitors.com.palantir.product.StringExample value);

        T visitThisFieldIsAnInteger(int value);

        T visitAlsoAnInteger(int value);

        T visitIf(int value);

        T visitNew(int value);

        T visitInterface(int value);

        T visitCompleted(int value);

        T visitUnknown_(int value);

        T visitKnown_(String value);

        T visitOptional(java.util.Optional<String> value);

        T visitList(java.util.List<String> value);

        T visitSet(java.util.Set<String> value);

        T visitMap(java.util.Map<String, String> value);

        T visitOptionalAlias(withvisitors.com.palantir.product.OptionalAlias value);

        T visitListAlias(withvisitors.com.palantir.product.ListAlias value);

        T visitSetAlias(withvisitors.com.palantir.product.SetAlias value);

        T visitMapAlias(MapAliasExample value);

        T visitUnknown(@Safe String unknownType, Object unknownValue);

        /**
         * @Deprecated - prefer Java 17 pattern matching switch expressions.
         */
        @Deprecated
        static <T> AlsoAnIntegerStageVisitorBuilder<T> builder() {
            return new VisitorBuilder<T>();
        }
    }

    final class VisitorBuilder<T>
            implements AlsoAnIntegerStageVisitorBuilder<T>,
                    CompletedStageVisitorBuilder<T>,
                    IfStageVisitorBuilder<T>,
                    InterfaceStageVisitorBuilder<T>,
                    Known_StageVisitorBuilder<T>,
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

        private Function<String, T> known_Visitor;

        private Function<java.util.List<String>, T> listVisitor;

        private Function<withvisitors.com.palantir.product.ListAlias, T> listAliasVisitor;

        private Function<java.util.Map<String, String>, T> mapVisitor;

        private Function<MapAliasExample, T> mapAliasVisitor;

        private IntFunction<T> newVisitor;

        private Function<java.util.Optional<String>, T> optionalVisitor;

        private Function<withvisitors.com.palantir.product.OptionalAlias, T> optionalAliasVisitor;

        private Function<java.util.Set<String>, T> setVisitor;

        private Function<withvisitors.com.palantir.product.SetAlias, T> setAliasVisitor;

        private Function<withvisitors.com.palantir.product.StringExample, T> stringExampleVisitor;

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
        public Known_StageVisitorBuilder<T> interface_(@Nonnull IntFunction<T> interfaceVisitor) {
            Preconditions.checkNotNull(interfaceVisitor, "interfaceVisitor cannot be null");
            this.interfaceVisitor = interfaceVisitor;
            return this;
        }

        @Override
        public ListStageVisitorBuilder<T> known_(@Nonnull Function<String, T> known_Visitor) {
            Preconditions.checkNotNull(known_Visitor, "known_Visitor cannot be null");
            this.known_Visitor = known_Visitor;
            return this;
        }

        @Override
        public ListAliasStageVisitorBuilder<T> list(@Nonnull Function<java.util.List<String>, T> listVisitor) {
            Preconditions.checkNotNull(listVisitor, "listVisitor cannot be null");
            this.listVisitor = listVisitor;
            return this;
        }

        @Override
        public MapStageVisitorBuilder<T> listAlias(
                @Nonnull Function<withvisitors.com.palantir.product.ListAlias, T> listAliasVisitor) {
            Preconditions.checkNotNull(listAliasVisitor, "listAliasVisitor cannot be null");
            this.listAliasVisitor = listAliasVisitor;
            return this;
        }

        @Override
        public MapAliasStageVisitorBuilder<T> map(@Nonnull Function<java.util.Map<String, String>, T> mapVisitor) {
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
        public OptionalAliasStageVisitorBuilder<T> optional(
                @Nonnull Function<java.util.Optional<String>, T> optionalVisitor) {
            Preconditions.checkNotNull(optionalVisitor, "optionalVisitor cannot be null");
            this.optionalVisitor = optionalVisitor;
            return this;
        }

        @Override
        public SetStageVisitorBuilder<T> optionalAlias(
                @Nonnull Function<withvisitors.com.palantir.product.OptionalAlias, T> optionalAliasVisitor) {
            Preconditions.checkNotNull(optionalAliasVisitor, "optionalAliasVisitor cannot be null");
            this.optionalAliasVisitor = optionalAliasVisitor;
            return this;
        }

        @Override
        public SetAliasStageVisitorBuilder<T> set(@Nonnull Function<java.util.Set<String>, T> setVisitor) {
            Preconditions.checkNotNull(setVisitor, "setVisitor cannot be null");
            this.setVisitor = setVisitor;
            return this;
        }

        @Override
        public StringExampleStageVisitorBuilder<T> setAlias(
                @Nonnull Function<withvisitors.com.palantir.product.SetAlias, T> setAliasVisitor) {
            Preconditions.checkNotNull(setAliasVisitor, "setAliasVisitor cannot be null");
            this.setAliasVisitor = setAliasVisitor;
            return this;
        }

        @Override
        public ThisFieldIsAnIntegerStageVisitorBuilder<T> stringExample(
                @Nonnull Function<withvisitors.com.palantir.product.StringExample, T> stringExampleVisitor) {
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
            final Function<String, T> known_Visitor = this.known_Visitor;
            final Function<java.util.List<String>, T> listVisitor = this.listVisitor;
            final Function<withvisitors.com.palantir.product.ListAlias, T> listAliasVisitor = this.listAliasVisitor;
            final Function<java.util.Map<String, String>, T> mapVisitor = this.mapVisitor;
            final Function<MapAliasExample, T> mapAliasVisitor = this.mapAliasVisitor;
            final IntFunction<T> newVisitor = this.newVisitor;
            final Function<java.util.Optional<String>, T> optionalVisitor = this.optionalVisitor;
            final Function<withvisitors.com.palantir.product.OptionalAlias, T> optionalAliasVisitor =
                    this.optionalAliasVisitor;
            final Function<java.util.Set<String>, T> setVisitor = this.setVisitor;
            final Function<withvisitors.com.palantir.product.SetAlias, T> setAliasVisitor = this.setAliasVisitor;
            final Function<withvisitors.com.palantir.product.StringExample, T> stringExampleVisitor =
                    this.stringExampleVisitor;
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
                public T visitKnown_(String value) {
                    return known_Visitor.apply(value);
                }

                @Override
                public T visitList(java.util.List<String> value) {
                    return listVisitor.apply(value);
                }

                @Override
                public T visitListAlias(withvisitors.com.palantir.product.ListAlias value) {
                    return listAliasVisitor.apply(value);
                }

                @Override
                public T visitMap(java.util.Map<String, String> value) {
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
                public T visitOptional(java.util.Optional<String> value) {
                    return optionalVisitor.apply(value);
                }

                @Override
                public T visitOptionalAlias(withvisitors.com.palantir.product.OptionalAlias value) {
                    return optionalAliasVisitor.apply(value);
                }

                @Override
                public T visitSet(java.util.Set<String> value) {
                    return setVisitor.apply(value);
                }

                @Override
                public T visitSetAlias(withvisitors.com.palantir.product.SetAlias value) {
                    return setAliasVisitor.apply(value);
                }

                @Override
                public T visitStringExample(withvisitors.com.palantir.product.StringExample value) {
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

    interface AlsoAnIntegerStageVisitorBuilder<T> {
        CompletedStageVisitorBuilder<T> alsoAnInteger(@Nonnull IntFunction<T> alsoAnIntegerVisitor);
    }

    interface CompletedStageVisitorBuilder<T> {
        IfStageVisitorBuilder<T> completed(@Nonnull IntFunction<T> completedVisitor);
    }

    interface IfStageVisitorBuilder<T> {
        InterfaceStageVisitorBuilder<T> if_(@Nonnull IntFunction<T> ifVisitor);
    }

    interface InterfaceStageVisitorBuilder<T> {
        Known_StageVisitorBuilder<T> interface_(@Nonnull IntFunction<T> interfaceVisitor);
    }

    interface Known_StageVisitorBuilder<T> {
        ListStageVisitorBuilder<T> known_(@Nonnull Function<String, T> known_Visitor);
    }

    interface ListStageVisitorBuilder<T> {
        ListAliasStageVisitorBuilder<T> list(@Nonnull Function<java.util.List<String>, T> listVisitor);
    }

    interface ListAliasStageVisitorBuilder<T> {
        MapStageVisitorBuilder<T> listAlias(
                @Nonnull Function<withvisitors.com.palantir.product.ListAlias, T> listAliasVisitor);
    }

    interface MapStageVisitorBuilder<T> {
        MapAliasStageVisitorBuilder<T> map(@Nonnull Function<java.util.Map<String, String>, T> mapVisitor);
    }

    interface MapAliasStageVisitorBuilder<T> {
        NewStageVisitorBuilder<T> mapAlias(@Nonnull Function<MapAliasExample, T> mapAliasVisitor);
    }

    interface NewStageVisitorBuilder<T> {
        OptionalStageVisitorBuilder<T> new_(@Nonnull IntFunction<T> newVisitor);
    }

    interface OptionalStageVisitorBuilder<T> {
        OptionalAliasStageVisitorBuilder<T> optional(@Nonnull Function<java.util.Optional<String>, T> optionalVisitor);
    }

    interface OptionalAliasStageVisitorBuilder<T> {
        SetStageVisitorBuilder<T> optionalAlias(
                @Nonnull Function<withvisitors.com.palantir.product.OptionalAlias, T> optionalAliasVisitor);
    }

    interface SetStageVisitorBuilder<T> {
        SetAliasStageVisitorBuilder<T> set(@Nonnull Function<java.util.Set<String>, T> setVisitor);
    }

    interface SetAliasStageVisitorBuilder<T> {
        StringExampleStageVisitorBuilder<T> setAlias(
                @Nonnull Function<withvisitors.com.palantir.product.SetAlias, T> setAliasVisitor);
    }

    interface StringExampleStageVisitorBuilder<T> {
        ThisFieldIsAnIntegerStageVisitorBuilder<T> stringExample(
                @Nonnull Function<withvisitors.com.palantir.product.StringExample, T> stringExampleVisitor);
    }

    interface ThisFieldIsAnIntegerStageVisitorBuilder<T> {
        Unknown_StageVisitorBuilder<T> thisFieldIsAnInteger(@Nonnull IntFunction<T> thisFieldIsAnIntegerVisitor);
    }

    interface Unknown_StageVisitorBuilder<T> {
        UnknownStageVisitorBuilder<T> unknown_(@Nonnull IntFunction<T> unknown_Visitor);
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
