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
    record StringExample(sealedunions.com.palantir.product.StringExample value) implements UnionTypeExample {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        StringExample(@JsonSetter("stringExample") @Nonnull sealedunions.com.palantir.product.StringExample value) {
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
        public String toString() {
            return "StringExample{value: " + value + '}';
        }
    }

    @JsonTypeName("thisFieldIsAnInteger")
    record ThisFieldIsAnInteger(int value) implements UnionTypeExample {
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
        public String toString() {
            return "ThisFieldIsAnInteger{value: " + value + '}';
        }
    }

    @JsonTypeName("alsoAnInteger")
    record AlsoAnInteger(int value) implements UnionTypeExample {
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
        public String toString() {
            return "AlsoAnInteger{value: " + value + '}';
        }
    }

    @JsonTypeName("if")
    record If(int value) implements UnionTypeExample {
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
        public String toString() {
            return "If{value: " + value + '}';
        }
    }

    @JsonTypeName("new")
    record New(int value) implements UnionTypeExample {
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
        public String toString() {
            return "New{value: " + value + '}';
        }
    }

    @JsonTypeName("interface")
    record Interface(int value) implements UnionTypeExample {
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
        public String toString() {
            return "Interface{value: " + value + '}';
        }
    }

    @JsonTypeName("completed")
    record Completed(int value) implements UnionTypeExample {
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
        public String toString() {
            return "Completed{value: " + value + '}';
        }
    }

    @JsonTypeName("unknown")
    record Unknown_(int value) implements UnionTypeExample {
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
        public String toString() {
            return "Unknown_{value: " + value + '}';
        }
    }

    @JsonTypeName("optional")
    record Optional(java.util.Optional<String> value) implements UnionTypeExample {
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
        public String toString() {
            return "Optional{value: " + value + '}';
        }
    }

    @JsonTypeName("list")
    record List(java.util.List<String> value) implements UnionTypeExample {
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
        public String toString() {
            return "List{value: " + value + '}';
        }
    }

    @JsonTypeName("set")
    record Set(java.util.Set<String> value) implements UnionTypeExample {
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
        public String toString() {
            return "Set{value: " + value + '}';
        }
    }

    @JsonTypeName("map")
    record Map(java.util.Map<String, String> value) implements UnionTypeExample {
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
        public String toString() {
            return "Map{value: " + value + '}';
        }
    }

    @JsonTypeName("optionalAlias")
    record OptionalAlias(sealedunions.com.palantir.product.OptionalAlias value) implements UnionTypeExample {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        OptionalAlias(
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
        public String toString() {
            return "OptionalAlias{value: " + value + '}';
        }
    }

    @JsonTypeName("listAlias")
    record ListAlias(sealedunions.com.palantir.product.ListAlias value) implements UnionTypeExample {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        ListAlias(
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
        public String toString() {
            return "ListAlias{value: " + value + '}';
        }
    }

    @JsonTypeName("setAlias")
    record SetAlias(sealedunions.com.palantir.product.SetAlias value) implements UnionTypeExample {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        SetAlias(
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
        public String toString() {
            return "SetAlias{value: " + value + '}';
        }
    }

    @JsonTypeName("mapAlias")
    record MapAlias(MapAliasExample value) implements UnionTypeExample {
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
