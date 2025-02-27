package defensivenonnullcollections.com.palantir.product;

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
import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.UnionGenerator")
public final class ExampleDefensiveCollectionUnion {
    private final Base value;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    private ExampleDefensiveCollectionUnion(Base value) {
        this.value = value;
    }

    @JsonValue
    private Base getValue() {
        return value;
    }

    public static ExampleDefensiveCollectionUnion string(String value) {
        return new ExampleDefensiveCollectionUnion(new StringWrapper(value));
    }

    public static ExampleDefensiveCollectionUnion list(List<String> value) {
        return new ExampleDefensiveCollectionUnion(new ListWrapper(value));
    }

    public static ExampleDefensiveCollectionUnion primitiveList(List<Double> value) {
        return new ExampleDefensiveCollectionUnion(new PrimitiveListWrapper(value));
    }

    public static ExampleDefensiveCollectionUnion listOptional(List<Optional<String>> value) {
        return new ExampleDefensiveCollectionUnion(new ListOptionalWrapper(value));
    }

    public static ExampleDefensiveCollectionUnion set(Set<String> value) {
        return new ExampleDefensiveCollectionUnion(new SetWrapper(value));
    }

    public static ExampleDefensiveCollectionUnion map(Map<String, String> value) {
        return new ExampleDefensiveCollectionUnion(new MapWrapper(value));
    }

    public static ExampleDefensiveCollectionUnion mapOptional(Map<String, Optional<String>> value) {
        return new ExampleDefensiveCollectionUnion(new MapOptionalWrapper(value));
    }

    public static ExampleDefensiveCollectionUnion unknown(@Safe String type, Object value) {
        switch (Preconditions.checkNotNull(type, "Type is required")) {
            case "string":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: string");
            case "list":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: list");
            case "primitiveList":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: primitiveList");
            case "listOptional":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: listOptional");
            case "set":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: set");
            case "map":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: map");
            case "mapOptional":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: mapOptional");
            default:
                return new ExampleDefensiveCollectionUnion(
                        new UnknownWrapper(type, Collections.singletonMap(type, value)));
        }
    }

    public <T> T accept(Visitor<T> visitor) {
        return value.accept(visitor);
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other
                || (other instanceof ExampleDefensiveCollectionUnion
                        && equalTo((ExampleDefensiveCollectionUnion) other));
    }

    private boolean equalTo(ExampleDefensiveCollectionUnion other) {
        return this.value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public String toString() {
        return "ExampleDefensiveCollectionUnion{value: " + value + '}';
    }

    public interface Visitor<T> {
        T visitString(String value);

        T visitList(List<String> value);

        T visitPrimitiveList(List<Double> value);

        T visitListOptional(List<Optional<String>> value);

        T visitSet(Set<String> value);

        T visitMap(Map<String, String> value);

        T visitMapOptional(Map<String, Optional<String>> value);

        T visitUnknown(@Safe String unknownType);

        static <T> ListStageVisitorBuilder<T> builder() {
            return new VisitorBuilder<T>();
        }
    }

    private static final class VisitorBuilder<T>
            implements ListStageVisitorBuilder<T>,
                    ListOptionalStageVisitorBuilder<T>,
                    MapStageVisitorBuilder<T>,
                    MapOptionalStageVisitorBuilder<T>,
                    PrimitiveListStageVisitorBuilder<T>,
                    SetStageVisitorBuilder<T>,
                    StringStageVisitorBuilder<T>,
                    UnknownStageVisitorBuilder<T>,
                    Completed_StageVisitorBuilder<T> {
        private Function<List<String>, T> listVisitor;

        private Function<List<Optional<String>>, T> listOptionalVisitor;

        private Function<Map<String, String>, T> mapVisitor;

        private Function<Map<String, Optional<String>>, T> mapOptionalVisitor;

        private Function<List<Double>, T> primitiveListVisitor;

        private Function<Set<String>, T> setVisitor;

        private Function<String, T> stringVisitor;

        private Function<String, T> unknownVisitor;

        @Override
        public ListOptionalStageVisitorBuilder<T> list(@Nonnull Function<List<String>, T> listVisitor) {
            Preconditions.checkNotNull(listVisitor, "listVisitor cannot be null");
            this.listVisitor = listVisitor;
            return this;
        }

        @Override
        public MapStageVisitorBuilder<T> listOptional(
                @Nonnull Function<List<Optional<String>>, T> listOptionalVisitor) {
            Preconditions.checkNotNull(listOptionalVisitor, "listOptionalVisitor cannot be null");
            this.listOptionalVisitor = listOptionalVisitor;
            return this;
        }

        @Override
        public MapOptionalStageVisitorBuilder<T> map(@Nonnull Function<Map<String, String>, T> mapVisitor) {
            Preconditions.checkNotNull(mapVisitor, "mapVisitor cannot be null");
            this.mapVisitor = mapVisitor;
            return this;
        }

        @Override
        public PrimitiveListStageVisitorBuilder<T> mapOptional(
                @Nonnull Function<Map<String, Optional<String>>, T> mapOptionalVisitor) {
            Preconditions.checkNotNull(mapOptionalVisitor, "mapOptionalVisitor cannot be null");
            this.mapOptionalVisitor = mapOptionalVisitor;
            return this;
        }

        @Override
        public SetStageVisitorBuilder<T> primitiveList(@Nonnull Function<List<Double>, T> primitiveListVisitor) {
            Preconditions.checkNotNull(primitiveListVisitor, "primitiveListVisitor cannot be null");
            this.primitiveListVisitor = primitiveListVisitor;
            return this;
        }

        @Override
        public StringStageVisitorBuilder<T> set(@Nonnull Function<Set<String>, T> setVisitor) {
            Preconditions.checkNotNull(setVisitor, "setVisitor cannot be null");
            this.setVisitor = setVisitor;
            return this;
        }

        @Override
        public UnknownStageVisitorBuilder<T> string(@Nonnull Function<String, T> stringVisitor) {
            Preconditions.checkNotNull(stringVisitor, "stringVisitor cannot be null");
            this.stringVisitor = stringVisitor;
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
                        "Unknown variant of the 'ExampleDefensiveCollectionUnion' union",
                        SafeArg.of("unknownType", unknownType));
            };
            return this;
        }

        @Override
        public Visitor<T> build() {
            final Function<List<String>, T> listVisitor = this.listVisitor;
            final Function<List<Optional<String>>, T> listOptionalVisitor = this.listOptionalVisitor;
            final Function<Map<String, String>, T> mapVisitor = this.mapVisitor;
            final Function<Map<String, Optional<String>>, T> mapOptionalVisitor = this.mapOptionalVisitor;
            final Function<List<Double>, T> primitiveListVisitor = this.primitiveListVisitor;
            final Function<Set<String>, T> setVisitor = this.setVisitor;
            final Function<String, T> stringVisitor = this.stringVisitor;
            final Function<String, T> unknownVisitor = this.unknownVisitor;
            return new Visitor<T>() {
                @Override
                public T visitList(List<String> value) {
                    return listVisitor.apply(value);
                }

                @Override
                public T visitListOptional(List<Optional<String>> value) {
                    return listOptionalVisitor.apply(value);
                }

                @Override
                public T visitMap(Map<String, String> value) {
                    return mapVisitor.apply(value);
                }

                @Override
                public T visitMapOptional(Map<String, Optional<String>> value) {
                    return mapOptionalVisitor.apply(value);
                }

                @Override
                public T visitPrimitiveList(List<Double> value) {
                    return primitiveListVisitor.apply(value);
                }

                @Override
                public T visitSet(Set<String> value) {
                    return setVisitor.apply(value);
                }

                @Override
                public T visitString(String value) {
                    return stringVisitor.apply(value);
                }

                @Override
                public T visitUnknown(String value) {
                    return unknownVisitor.apply(value);
                }
            };
        }
    }

    public interface ListStageVisitorBuilder<T> {
        ListOptionalStageVisitorBuilder<T> list(@Nonnull Function<List<String>, T> listVisitor);
    }

    public interface ListOptionalStageVisitorBuilder<T> {
        MapStageVisitorBuilder<T> listOptional(@Nonnull Function<List<Optional<String>>, T> listOptionalVisitor);
    }

    public interface MapStageVisitorBuilder<T> {
        MapOptionalStageVisitorBuilder<T> map(@Nonnull Function<Map<String, String>, T> mapVisitor);
    }

    public interface MapOptionalStageVisitorBuilder<T> {
        PrimitiveListStageVisitorBuilder<T> mapOptional(
                @Nonnull Function<Map<String, Optional<String>>, T> mapOptionalVisitor);
    }

    public interface PrimitiveListStageVisitorBuilder<T> {
        SetStageVisitorBuilder<T> primitiveList(@Nonnull Function<List<Double>, T> primitiveListVisitor);
    }

    public interface SetStageVisitorBuilder<T> {
        StringStageVisitorBuilder<T> set(@Nonnull Function<Set<String>, T> setVisitor);
    }

    public interface StringStageVisitorBuilder<T> {
        UnknownStageVisitorBuilder<T> string(@Nonnull Function<String, T> stringVisitor);
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
    @JsonSubTypes({
        @JsonSubTypes.Type(StringWrapper.class),
        @JsonSubTypes.Type(ListWrapper.class),
        @JsonSubTypes.Type(PrimitiveListWrapper.class),
        @JsonSubTypes.Type(ListOptionalWrapper.class),
        @JsonSubTypes.Type(SetWrapper.class),
        @JsonSubTypes.Type(MapWrapper.class),
        @JsonSubTypes.Type(MapOptionalWrapper.class)
    })
    @JsonIgnoreProperties(ignoreUnknown = true)
    private interface Base {
        <T> T accept(Visitor<T> visitor);
    }

    @JsonTypeName("string")
    private static final class StringWrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private StringWrapper(@JsonSetter("string") @Nonnull String value) {
            Preconditions.checkNotNull(value, "string cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "string";
        }

        @JsonProperty("string")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitString(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof StringWrapper && equalTo((StringWrapper) other));
        }

        private boolean equalTo(StringWrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "StringWrapper{value: " + value + '}';
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
        public boolean equals(@Nullable Object other) {
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

    @JsonTypeName("primitiveList")
    private static final class PrimitiveListWrapper implements Base {
        private final List<Double> value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private PrimitiveListWrapper(
                @JsonSetter(value = "primitiveList", nulls = Nulls.AS_EMPTY) @Nonnull List<Double> value) {
            Preconditions.checkNotNull(value, "primitiveList cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "primitiveList";
        }

        @JsonProperty("primitiveList")
        private List<Double> getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitPrimitiveList(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof PrimitiveListWrapper && equalTo((PrimitiveListWrapper) other));
        }

        private boolean equalTo(PrimitiveListWrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "PrimitiveListWrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("listOptional")
    private static final class ListOptionalWrapper implements Base {
        private final List<Optional<String>> value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private ListOptionalWrapper(
                @JsonSetter(value = "listOptional", nulls = Nulls.AS_EMPTY) @Nonnull List<Optional<String>> value) {
            Preconditions.checkNotNull(value, "listOptional cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "listOptional";
        }

        @JsonProperty("listOptional")
        private List<Optional<String>> getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitListOptional(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof ListOptionalWrapper && equalTo((ListOptionalWrapper) other));
        }

        private boolean equalTo(ListOptionalWrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "ListOptionalWrapper{value: " + value + '}';
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
        public boolean equals(@Nullable Object other) {
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
        public boolean equals(@Nullable Object other) {
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

    @JsonTypeName("mapOptional")
    private static final class MapOptionalWrapper implements Base {
        private final Map<String, Optional<String>> value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private MapOptionalWrapper(
                @JsonSetter(value = "mapOptional", nulls = Nulls.AS_EMPTY) @Nonnull
                        Map<String, Optional<String>> value) {
            Preconditions.checkNotNull(value, "mapOptional cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "mapOptional";
        }

        @JsonProperty("mapOptional")
        private Map<String, Optional<String>> getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitMapOptional(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof MapOptionalWrapper && equalTo((MapOptionalWrapper) other));
        }

        private boolean equalTo(MapOptionalWrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "MapOptionalWrapper{value: " + value + '}';
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
