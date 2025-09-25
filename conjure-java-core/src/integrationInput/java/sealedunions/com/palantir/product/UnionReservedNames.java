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
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.UnionGenerator")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", defaultImpl = UnionReservedNames.UnknownVariant.class)
@JsonSubTypes({
    @JsonSubTypes.Type(value = UnionReservedNames.Known_.class, name = "known_"),
    @JsonSubTypes.Type(value = UnionReservedNames.Unknown_.class, name = "unknown_"),
    @JsonSubTypes.Type(value = UnionReservedNames.If_.class, name = "if_"),
    @JsonSubTypes.Type(value = UnionReservedNames.New_.class, name = "new_"),
    @JsonSubTypes.Type(value = UnionReservedNames.Interface_.class, name = "interface_"),
    @JsonSubTypes.Type(value = UnionReservedNames.Void_.class, name = "void_"),
    @JsonSubTypes.Type(value = UnionReservedNames.Return_.class, name = "return_"),
    @JsonSubTypes.Type(value = UnionReservedNames.Private_.class, name = "private_"),
    @JsonSubTypes.Type(value = UnionReservedNames.Public_.class, name = "public_"),
    @JsonSubTypes.Type(value = UnionReservedNames.Int_.class, name = "int_"),
    @JsonSubTypes.Type(value = UnionReservedNames.Import_.class, name = "import_"),
    @JsonSubTypes.Type(value = UnionReservedNames.Final_.class, name = "final_"),
    @JsonSubTypes.Type(value = UnionReservedNames.Throws_.class, name = "throws_"),
    @JsonSubTypes.Type(value = UnionReservedNames.Static_.class, name = "static_")
})
@JsonIgnoreProperties(ignoreUnknown = true)
public sealed interface UnionReservedNames {
    static UnionReservedNames known_(String value) {
        return new Known_(value);
    }

    static UnionReservedNames unknown_(String value) {
        return new Unknown_(value);
    }

    static UnionReservedNames if_(String value) {
        return new If_(value);
    }

    static UnionReservedNames new_(String value) {
        return new New_(value);
    }

    static UnionReservedNames interface_(String value) {
        return new Interface_(value);
    }

    static UnionReservedNames void_(String value) {
        return new Void_(value);
    }

    static UnionReservedNames return_(String value) {
        return new Return_(value);
    }

    static UnionReservedNames private_(String value) {
        return new Private_(value);
    }

    static UnionReservedNames public_(String value) {
        return new Public_(value);
    }

    static UnionReservedNames int_(String value) {
        return new Int_(value);
    }

    static UnionReservedNames import_(String value) {
        return new Import_(value);
    }

    static UnionReservedNames final_(String value) {
        return new Final_(value);
    }

    static UnionReservedNames throws_(String value) {
        return new Throws_(value);
    }

    static UnionReservedNames static_(String value) {
        return new Static_(value);
    }

    static UnionReservedNames unknown(@Safe String type, Object value) {
        switch (Preconditions.checkNotNull(type, "Type is required")) {
            case "known":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: known");
            case "unknown":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: unknown");
            case "if":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: if");
            case "new":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: new");
            case "interface":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: interface");
            case "void":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: void");
            case "return":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: return");
            case "private":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: private");
            case "public":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: public");
            case "int":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: int");
            case "import":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: import");
            case "final":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: final");
            case "throws":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: throws");
            case "static":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: static");
            default:
                return new UnknownVariant(type, Collections.singletonMap(type, value));
        }
    }

    default Known throwOnUnknown() {
        if (this instanceof UnknownVariant) {
            throw new SafeIllegalArgumentException(
                    "Unknown variant of the 'UnionReservedNames' union",
                    SafeArg.of("type", ((UnknownVariant) this).type()));
        } else {
            return (Known) this;
        }
    }

    sealed interface Known extends UnionReservedNames
            permits Known_,
                    Unknown_,
                    If_,
                    New_,
                    Interface_,
                    Void_,
                    Return_,
                    Private_,
                    Public_,
                    Int_,
                    Import_,
                    Final_,
                    Throws_,
                    Static_ {}

    @JsonTypeName("Known_")
    record Known_(String value) implements Known {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public Known_(@JsonSetter("known") @Nonnull String value) {
            Preconditions.checkNotNull(value, "Known_ cannot be null");
            this.value = value;
        }

        @Override
        public String toString() {
            return "UnionReservedNames.Known_{value: " + value + '}';
        }
    }

    @JsonTypeName("Unknown_")
    record Unknown_(String value) implements Known {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public Unknown_(@JsonSetter("unknown") @Nonnull String value) {
            Preconditions.checkNotNull(value, "Unknown_ cannot be null");
            this.value = value;
        }

        @Override
        public String toString() {
            return "UnionReservedNames.Unknown_{value: " + value + '}';
        }
    }

    @JsonTypeName("If_")
    record If_(String value) implements Known {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public If_(@JsonSetter("if") @Nonnull String value) {
            Preconditions.checkNotNull(value, "If_ cannot be null");
            this.value = value;
        }

        @Override
        public String toString() {
            return "UnionReservedNames.If_{value: " + value + '}';
        }
    }

    @JsonTypeName("New_")
    record New_(String value) implements Known {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public New_(@JsonSetter("new") @Nonnull String value) {
            Preconditions.checkNotNull(value, "New_ cannot be null");
            this.value = value;
        }

        @Override
        public String toString() {
            return "UnionReservedNames.New_{value: " + value + '}';
        }
    }

    @JsonTypeName("Interface_")
    record Interface_(String value) implements Known {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public Interface_(@JsonSetter("interface") @Nonnull String value) {
            Preconditions.checkNotNull(value, "Interface_ cannot be null");
            this.value = value;
        }

        @Override
        public String toString() {
            return "UnionReservedNames.Interface_{value: " + value + '}';
        }
    }

    @JsonTypeName("Void_")
    record Void_(String value) implements Known {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public Void_(@JsonSetter("void") @Nonnull String value) {
            Preconditions.checkNotNull(value, "Void_ cannot be null");
            this.value = value;
        }

        @Override
        public String toString() {
            return "UnionReservedNames.Void_{value: " + value + '}';
        }
    }

    @JsonTypeName("Return_")
    record Return_(String value) implements Known {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public Return_(@JsonSetter("return") @Nonnull String value) {
            Preconditions.checkNotNull(value, "Return_ cannot be null");
            this.value = value;
        }

        @Override
        public String toString() {
            return "UnionReservedNames.Return_{value: " + value + '}';
        }
    }

    @JsonTypeName("Private_")
    record Private_(String value) implements Known {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public Private_(@JsonSetter("private") @Nonnull String value) {
            Preconditions.checkNotNull(value, "Private_ cannot be null");
            this.value = value;
        }

        @Override
        public String toString() {
            return "UnionReservedNames.Private_{value: " + value + '}';
        }
    }

    @JsonTypeName("Public_")
    record Public_(String value) implements Known {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public Public_(@JsonSetter("public") @Nonnull String value) {
            Preconditions.checkNotNull(value, "Public_ cannot be null");
            this.value = value;
        }

        @Override
        public String toString() {
            return "UnionReservedNames.Public_{value: " + value + '}';
        }
    }

    @JsonTypeName("Int_")
    record Int_(String value) implements Known {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public Int_(@JsonSetter("int") @Nonnull String value) {
            Preconditions.checkNotNull(value, "Int_ cannot be null");
            this.value = value;
        }

        @Override
        public String toString() {
            return "UnionReservedNames.Int_{value: " + value + '}';
        }
    }

    @JsonTypeName("Import_")
    record Import_(String value) implements Known {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public Import_(@JsonSetter("import") @Nonnull String value) {
            Preconditions.checkNotNull(value, "Import_ cannot be null");
            this.value = value;
        }

        @Override
        public String toString() {
            return "UnionReservedNames.Import_{value: " + value + '}';
        }
    }

    @JsonTypeName("Final_")
    record Final_(String value) implements Known {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public Final_(@JsonSetter("final") @Nonnull String value) {
            Preconditions.checkNotNull(value, "Final_ cannot be null");
            this.value = value;
        }

        @Override
        public String toString() {
            return "UnionReservedNames.Final_{value: " + value + '}';
        }
    }

    @JsonTypeName("Throws_")
    record Throws_(String value) implements Known {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public Throws_(@JsonSetter("throws") @Nonnull String value) {
            Preconditions.checkNotNull(value, "Throws_ cannot be null");
            this.value = value;
        }

        @Override
        public String toString() {
            return "UnionReservedNames.Throws_{value: " + value + '}';
        }
    }

    @JsonTypeName("Static_")
    record Static_(String value) implements Known {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public Static_(@JsonSetter("static") @Nonnull String value) {
            Preconditions.checkNotNull(value, "Static_ cannot be null");
            this.value = value;
        }

        @Override
        public String toString() {
            return "UnionReservedNames.Static_{value: " + value + '}';
        }
    }

    record UnknownVariant(String type, Map<String, Object> value) implements UnionReservedNames {
        public UnknownVariant {
            Preconditions.checkNotNull(type, "type cannot be null");
            Preconditions.checkNotNull(value, "type cannot be null");
        }

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private UnknownVariant(@JsonProperty("type") String type) {
            this(type, new HashMap<String, Object>());
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
        public String toString() {
            return "UnionReservedNames.UnknownVariant{value: " + value + '}';
        }
    }
}
