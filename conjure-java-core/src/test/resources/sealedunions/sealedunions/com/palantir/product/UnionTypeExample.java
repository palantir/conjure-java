package sealedunions.com.palantir.product;

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
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.Collections;
import java.util.HashMap;
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
        defaultImpl = Unknown.class)
@JsonSubTypes({
    @JsonSubTypes.Type(StringExample.class),
    @JsonSubTypes.Type(ThisFieldIsAnInteger.class),
    @JsonSubTypes.Type(AlsoAnInteger.class),
    @JsonSubTypes.Type(If.class),
    @JsonSubTypes.Type(New.class),
    @JsonSubTypes.Type(Interface.class),
    @JsonSubTypes.Type(Completed.class),
    @JsonSubTypes.Type(Unknown_.class),
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
    static UnionTypeExample stringExample(sealedunions.com.palantir.product.StringExample value) {
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

    static UnionTypeExample optionalAlias(sealedunions.com.palantir.product.OptionalAlias value) {
        return new OptionalAlias(value);
    }

    static UnionTypeExample listAlias(sealedunions.com.palantir.product.ListAlias value) {
        return new ListAlias(value);
    }

    static UnionTypeExample setAlias(sealedunions.com.palantir.product.SetAlias value) {
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

    @JsonTypeName("stringExample")
    final class StringExample implements UnionTypeExample {
        private final sealedunions.com.palantir.product.StringExample value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private StringExample(
                @JsonSetter("stringExample") @Nonnull sealedunions.com.palantir.product.StringExample value) {
            Preconditions.checkNotNull(value, "stringExample cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "stringExample";
        }

        @JsonProperty("stringExample")
        private sealedunions.com.palantir.product.StringExample getValue() {
            return value;
        }

        @Override
        public boolean equals(Object other) {
            return this == other
                    || (other instanceof sealedunions.com.palantir.product.StringExample
                            && equalTo((sealedunions.com.palantir.product.StringExample) other));
        }

        private boolean equalTo(sealedunions.com.palantir.product.StringExample other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "StringExample{value: " + value + '}';
        }
    }

    @JsonTypeName("thisFieldIsAnInteger")
    final class ThisFieldIsAnInteger implements UnionTypeExample {
        private final int value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private ThisFieldIsAnInteger(@JsonSetter("thisFieldIsAnInteger") @Nonnull int value) {
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
        public boolean equals(Object other) {
            return this == other
                    || (other instanceof sealedunions.com.palantir.product.ThisFieldIsAnInteger
                            && equalTo((sealedunions.com.palantir.product.ThisFieldIsAnInteger) other));
        }

        private boolean equalTo(sealedunions.com.palantir.product.ThisFieldIsAnInteger other) {
            return this.value == other.value;
        }

        @Override
        public int hashCode() {
            return this.value;
        }

        @Override
        public String toString() {
            return "ThisFieldIsAnInteger{value: " + value + '}';
        }
    }

    @JsonTypeName("alsoAnInteger")
    final class AlsoAnInteger implements UnionTypeExample {
        private final int value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private AlsoAnInteger(@JsonSetter("alsoAnInteger") @Nonnull int value) {
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
        public boolean equals(Object other) {
            return this == other
                    || (other instanceof sealedunions.com.palantir.product.AlsoAnInteger
                            && equalTo((sealedunions.com.palantir.product.AlsoAnInteger) other));
        }

        private boolean equalTo(sealedunions.com.palantir.product.AlsoAnInteger other) {
            return this.value == other.value;
        }

        @Override
        public int hashCode() {
            return this.value;
        }

        @Override
        public String toString() {
            return "AlsoAnInteger{value: " + value + '}';
        }
    }

    @JsonTypeName("if")
    final class If implements UnionTypeExample {
        private final int value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private If(@JsonSetter("if") @Nonnull int value) {
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
        public boolean equals(Object other) {
            return this == other
                    || (other instanceof sealedunions.com.palantir.product.If
                            && equalTo((sealedunions.com.palantir.product.If) other));
        }

        private boolean equalTo(sealedunions.com.palantir.product.If other) {
            return this.value == other.value;
        }

        @Override
        public int hashCode() {
            return this.value;
        }

        @Override
        public String toString() {
            return "If{value: " + value + '}';
        }
    }

    @JsonTypeName("new")
    final class New implements UnionTypeExample {
        private final int value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private New(@JsonSetter("new") @Nonnull int value) {
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
        public boolean equals(Object other) {
            return this == other
                    || (other instanceof sealedunions.com.palantir.product.New
                            && equalTo((sealedunions.com.palantir.product.New) other));
        }

        private boolean equalTo(sealedunions.com.palantir.product.New other) {
            return this.value == other.value;
        }

        @Override
        public int hashCode() {
            return this.value;
        }

        @Override
        public String toString() {
            return "New{value: " + value + '}';
        }
    }

    @JsonTypeName("interface")
    final class Interface implements UnionTypeExample {
        private final int value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private Interface(@JsonSetter("interface") @Nonnull int value) {
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
        public boolean equals(Object other) {
            return this == other
                    || (other instanceof sealedunions.com.palantir.product.Interface
                            && equalTo((sealedunions.com.palantir.product.Interface) other));
        }

        private boolean equalTo(sealedunions.com.palantir.product.Interface other) {
            return this.value == other.value;
        }

        @Override
        public int hashCode() {
            return this.value;
        }

        @Override
        public String toString() {
            return "Interface{value: " + value + '}';
        }
    }

    @JsonTypeName("completed")
    final class Completed implements UnionTypeExample {
        private final int value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private Completed(@JsonSetter("completed") @Nonnull int value) {
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
        public boolean equals(Object other) {
            return this == other
                    || (other instanceof sealedunions.com.palantir.product.Completed
                            && equalTo((sealedunions.com.palantir.product.Completed) other));
        }

        private boolean equalTo(sealedunions.com.palantir.product.Completed other) {
            return this.value == other.value;
        }

        @Override
        public int hashCode() {
            return this.value;
        }

        @Override
        public String toString() {
            return "Completed{value: " + value + '}';
        }
    }

    @JsonTypeName("unknown")
    final class Unknown_ implements UnionTypeExample {
        private final int value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private Unknown_(@JsonSetter("unknown") @Nonnull int value) {
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
        public boolean equals(Object other) {
            return this == other
                    || (other instanceof sealedunions.com.palantir.product.Unknown_
                            && equalTo((sealedunions.com.palantir.product.Unknown_) other));
        }

        private boolean equalTo(sealedunions.com.palantir.product.Unknown_ other) {
            return this.value == other.value;
        }

        @Override
        public int hashCode() {
            return this.value;
        }

        @Override
        public String toString() {
            return "Unknown_{value: " + value + '}';
        }
    }

    @JsonTypeName("optional")
    final class Optional implements UnionTypeExample {
        private final java.util.Optional<String> value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private Optional(
                @JsonSetter(value = "optional", nulls = Nulls.AS_EMPTY) @Nonnull java.util.Optional<String> value) {
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
        public boolean equals(Object other) {
            return this == other
                    || (other instanceof sealedunions.com.palantir.product.Optional
                            && equalTo((sealedunions.com.palantir.product.Optional) other));
        }

        private boolean equalTo(sealedunions.com.palantir.product.Optional other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "Optional{value: " + value + '}';
        }
    }

    @JsonTypeName("list")
    final class List implements UnionTypeExample {
        private final java.util.List<String> value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private List(@JsonSetter(value = "list", nulls = Nulls.AS_EMPTY) @Nonnull java.util.List<String> value) {
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
        public boolean equals(Object other) {
            return this == other
                    || (other instanceof sealedunions.com.palantir.product.List
                            && equalTo((sealedunions.com.palantir.product.List) other));
        }

        private boolean equalTo(sealedunions.com.palantir.product.List other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "List{value: " + value + '}';
        }
    }

    @JsonTypeName("set")
    final class Set implements UnionTypeExample {
        private final java.util.Set<String> value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private Set(@JsonSetter(value = "set", nulls = Nulls.AS_EMPTY) @Nonnull java.util.Set<String> value) {
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
        public boolean equals(Object other) {
            return this == other
                    || (other instanceof sealedunions.com.palantir.product.Set
                            && equalTo((sealedunions.com.palantir.product.Set) other));
        }

        private boolean equalTo(sealedunions.com.palantir.product.Set other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "Set{value: " + value + '}';
        }
    }

    @JsonTypeName("map")
    final class Map implements UnionTypeExample {
        private final java.util.Map<String, String> value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private Map(@JsonSetter(value = "map", nulls = Nulls.AS_EMPTY) @Nonnull java.util.Map<String, String> value) {
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
        public boolean equals(Object other) {
            return this == other
                    || (other instanceof sealedunions.com.palantir.product.Map
                            && equalTo((sealedunions.com.palantir.product.Map) other));
        }

        private boolean equalTo(sealedunions.com.palantir.product.Map other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "Map{value: " + value + '}';
        }
    }

    @JsonTypeName("optionalAlias")
    final class OptionalAlias implements UnionTypeExample {
        private final sealedunions.com.palantir.product.OptionalAlias value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private OptionalAlias(
                @JsonSetter(value = "optionalAlias", nulls = Nulls.AS_EMPTY) @Nonnull
                        sealedunions.com.palantir.product.OptionalAlias value) {
            Preconditions.checkNotNull(value, "optionalAlias cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "optionalAlias";
        }

        @JsonProperty("optionalAlias")
        private sealedunions.com.palantir.product.OptionalAlias getValue() {
            return value;
        }

        @Override
        public boolean equals(Object other) {
            return this == other
                    || (other instanceof sealedunions.com.palantir.product.OptionalAlias
                            && equalTo((sealedunions.com.palantir.product.OptionalAlias) other));
        }

        private boolean equalTo(sealedunions.com.palantir.product.OptionalAlias other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "OptionalAlias{value: " + value + '}';
        }
    }

    @JsonTypeName("listAlias")
    final class ListAlias implements UnionTypeExample {
        private final sealedunions.com.palantir.product.ListAlias value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private ListAlias(
                @JsonSetter(value = "listAlias", nulls = Nulls.AS_EMPTY) @Nonnull
                        sealedunions.com.palantir.product.ListAlias value) {
            Preconditions.checkNotNull(value, "listAlias cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "listAlias";
        }

        @JsonProperty("listAlias")
        private sealedunions.com.palantir.product.ListAlias getValue() {
            return value;
        }

        @Override
        public boolean equals(Object other) {
            return this == other
                    || (other instanceof sealedunions.com.palantir.product.ListAlias
                            && equalTo((sealedunions.com.palantir.product.ListAlias) other));
        }

        private boolean equalTo(sealedunions.com.palantir.product.ListAlias other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "ListAlias{value: " + value + '}';
        }
    }

    @JsonTypeName("setAlias")
    final class SetAlias implements UnionTypeExample {
        private final sealedunions.com.palantir.product.SetAlias value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private SetAlias(
                @JsonSetter(value = "setAlias", nulls = Nulls.AS_EMPTY) @Nonnull
                        sealedunions.com.palantir.product.SetAlias value) {
            Preconditions.checkNotNull(value, "setAlias cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "setAlias";
        }

        @JsonProperty("setAlias")
        private sealedunions.com.palantir.product.SetAlias getValue() {
            return value;
        }

        @Override
        public boolean equals(Object other) {
            return this == other
                    || (other instanceof sealedunions.com.palantir.product.SetAlias
                            && equalTo((sealedunions.com.palantir.product.SetAlias) other));
        }

        private boolean equalTo(sealedunions.com.palantir.product.SetAlias other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "SetAlias{value: " + value + '}';
        }
    }

    @JsonTypeName("mapAlias")
    final class MapAlias implements UnionTypeExample {
        private final MapAliasExample value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private MapAlias(@JsonSetter(value = "mapAlias", nulls = Nulls.AS_EMPTY) @Nonnull MapAliasExample value) {
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
        public boolean equals(Object other) {
            return this == other
                    || (other instanceof sealedunions.com.palantir.product.MapAlias
                            && equalTo((sealedunions.com.palantir.product.MapAlias) other));
        }

        private boolean equalTo(sealedunions.com.palantir.product.MapAlias other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
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
        public boolean equals(Object other) {
            return this == other
                    || (other instanceof sealedunions.com.palantir.product.Unknown
                            && equalTo((sealedunions.com.palantir.product.Unknown) other));
        }

        private boolean equalTo(sealedunions.com.palantir.product.Unknown other) {
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
}
