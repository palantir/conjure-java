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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Nonnull;

/**
 * A type which can either be a StringExample, a set of strings, or an integer.
 */
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
public sealed interface UnionTypeExample {
    /**
     * Docs for when UnionTypeExample is of type StringExample.
     */
    static UnionTypeExample stringExample(StringExample value) {
        return new StringExampleWrapper(value);
    }

    static UnionTypeExample thisFieldIsAnInteger(int value) {
        return new ThisFieldIsAnIntegerWrapper(value);
    }

    static UnionTypeExample alsoAnInteger(int value) {
        return new AlsoAnIntegerWrapper(value);
    }

    static UnionTypeExample if_(int value) {
        return new IfWrapper(value);
    }

    static UnionTypeExample new_(int value) {
        return new NewWrapper(value);
    }

    static UnionTypeExample interface_(int value) {
        return new InterfaceWrapper(value);
    }

    static UnionTypeExample completed(int value) {
        return new CompletedWrapper(value);
    }

    static UnionTypeExample unknown_(int value) {
        return new Unknown_Wrapper(value);
    }

    static UnionTypeExample optional(Optional<String> value) {
        return new OptionalWrapper(value);
    }

    static UnionTypeExample list(List<String> value) {
        return new ListWrapper(value);
    }

    static UnionTypeExample set(Set<String> value) {
        return new SetWrapper(value);
    }

    static UnionTypeExample map(Map<String, String> value) {
        return new MapWrapper(value);
    }

    static UnionTypeExample optionalAlias(OptionalAlias value) {
        return new OptionalAliasWrapper(value);
    }

    static UnionTypeExample listAlias(ListAlias value) {
        return new ListAliasWrapper(value);
    }

    static UnionTypeExample setAlias(SetAlias value) {
        return new SetAliasWrapper(value);
    }

    static UnionTypeExample mapAlias(MapAliasExample value) {
        return new MapAliasWrapper(value);
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
                return new UnknownWrapper(type, Collections.singletonMap(type, value));
        }
    }

    <T> void accept(Visitor<T> visitor);

    interface Visitor<T> {
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

        static <T> UnionTypeExample.AlsoAnIntegerStageVisitorBuilder<T> builder() {
            return new UnionTypeExample.VisitorBuilder<T>();
        }
    }

    @JsonTypeName("stringExample")
    final class StringExampleWrapper implements UnionTypeExample {
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
            return this == other
                    || (other instanceof sealedunions.com.palantir.product.StringExampleWrapper
                            && equalTo((sealedunions.com.palantir.product.StringExampleWrapper) other));
        }

        private boolean equalTo(sealedunions.com.palantir.product.StringExampleWrapper other) {
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
    final class ThisFieldIsAnIntegerWrapper implements UnionTypeExample {
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
                    || (other instanceof sealedunions.com.palantir.product.ThisFieldIsAnIntegerWrapper
                            && equalTo((sealedunions.com.palantir.product.ThisFieldIsAnIntegerWrapper) other));
        }

        private boolean equalTo(sealedunions.com.palantir.product.ThisFieldIsAnIntegerWrapper other) {
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
    final class AlsoAnIntegerWrapper implements UnionTypeExample {
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
            return this == other
                    || (other instanceof sealedunions.com.palantir.product.AlsoAnIntegerWrapper
                            && equalTo((sealedunions.com.palantir.product.AlsoAnIntegerWrapper) other));
        }

        private boolean equalTo(sealedunions.com.palantir.product.AlsoAnIntegerWrapper other) {
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
    final class IfWrapper implements UnionTypeExample {
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
            return this == other
                    || (other instanceof sealedunions.com.palantir.product.IfWrapper
                            && equalTo((sealedunions.com.palantir.product.IfWrapper) other));
        }

        private boolean equalTo(sealedunions.com.palantir.product.IfWrapper other) {
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
    final class NewWrapper implements UnionTypeExample {
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
            return this == other
                    || (other instanceof sealedunions.com.palantir.product.NewWrapper
                            && equalTo((sealedunions.com.palantir.product.NewWrapper) other));
        }

        private boolean equalTo(sealedunions.com.palantir.product.NewWrapper other) {
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
    final class InterfaceWrapper implements UnionTypeExample {
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
            return this == other
                    || (other instanceof sealedunions.com.palantir.product.InterfaceWrapper
                            && equalTo((sealedunions.com.palantir.product.InterfaceWrapper) other));
        }

        private boolean equalTo(sealedunions.com.palantir.product.InterfaceWrapper other) {
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
    final class CompletedWrapper implements UnionTypeExample {
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
            return this == other
                    || (other instanceof sealedunions.com.palantir.product.CompletedWrapper
                            && equalTo((sealedunions.com.palantir.product.CompletedWrapper) other));
        }

        private boolean equalTo(sealedunions.com.palantir.product.CompletedWrapper other) {
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
    final class Unknown_Wrapper implements UnionTypeExample {
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
            return this == other
                    || (other instanceof sealedunions.com.palantir.product.Unknown_Wrapper
                            && equalTo((sealedunions.com.palantir.product.Unknown_Wrapper) other));
        }

        private boolean equalTo(sealedunions.com.palantir.product.Unknown_Wrapper other) {
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
    final class OptionalWrapper implements UnionTypeExample {
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
            return this == other
                    || (other instanceof sealedunions.com.palantir.product.OptionalWrapper
                            && equalTo((sealedunions.com.palantir.product.OptionalWrapper) other));
        }

        private boolean equalTo(sealedunions.com.palantir.product.OptionalWrapper other) {
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
    final class ListWrapper implements UnionTypeExample {
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
            return this == other
                    || (other instanceof sealedunions.com.palantir.product.ListWrapper
                            && equalTo((sealedunions.com.palantir.product.ListWrapper) other));
        }

        private boolean equalTo(sealedunions.com.palantir.product.ListWrapper other) {
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
    final class SetWrapper implements UnionTypeExample {
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
            return this == other
                    || (other instanceof sealedunions.com.palantir.product.SetWrapper
                            && equalTo((sealedunions.com.palantir.product.SetWrapper) other));
        }

        private boolean equalTo(sealedunions.com.palantir.product.SetWrapper other) {
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
    final class MapWrapper implements UnionTypeExample {
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
            return this == other
                    || (other instanceof sealedunions.com.palantir.product.MapWrapper
                            && equalTo((sealedunions.com.palantir.product.MapWrapper) other));
        }

        private boolean equalTo(sealedunions.com.palantir.product.MapWrapper other) {
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
    final class OptionalAliasWrapper implements UnionTypeExample {
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
            return this == other
                    || (other instanceof sealedunions.com.palantir.product.OptionalAliasWrapper
                            && equalTo((sealedunions.com.palantir.product.OptionalAliasWrapper) other));
        }

        private boolean equalTo(sealedunions.com.palantir.product.OptionalAliasWrapper other) {
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
    final class ListAliasWrapper implements UnionTypeExample {
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
            return this == other
                    || (other instanceof sealedunions.com.palantir.product.ListAliasWrapper
                            && equalTo((sealedunions.com.palantir.product.ListAliasWrapper) other));
        }

        private boolean equalTo(sealedunions.com.palantir.product.ListAliasWrapper other) {
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
    final class SetAliasWrapper implements UnionTypeExample {
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
            return this == other
                    || (other instanceof sealedunions.com.palantir.product.SetAliasWrapper
                            && equalTo((sealedunions.com.palantir.product.SetAliasWrapper) other));
        }

        private boolean equalTo(sealedunions.com.palantir.product.SetAliasWrapper other) {
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
    final class MapAliasWrapper implements UnionTypeExample {
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
            return this == other
                    || (other instanceof sealedunions.com.palantir.product.MapAliasWrapper
                            && equalTo((sealedunions.com.palantir.product.MapAliasWrapper) other));
        }

        private boolean equalTo(sealedunions.com.palantir.product.MapAliasWrapper other) {
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

    final class UnknownWrapper implements UnionTypeExample {
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
            return this == other
                    || (other instanceof sealedunions.com.palantir.product.UnknownWrapper
                            && equalTo((sealedunions.com.palantir.product.UnknownWrapper) other));
        }

        private boolean equalTo(sealedunions.com.palantir.product.UnknownWrapper other) {
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
