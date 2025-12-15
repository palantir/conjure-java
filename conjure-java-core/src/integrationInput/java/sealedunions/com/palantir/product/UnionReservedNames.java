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
import java.util.function.BiFunction;
import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.UnionGenerator")
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true,
        defaultImpl = UnionReservedNames.Unknown.class)
@JsonSubTypes({
    @JsonSubTypes.Type(value = UnionReservedNames.Known_.class, name = "known"),
    @JsonSubTypes.Type(value = UnionReservedNames.Unknown_.class, name = "unknown"),
    @JsonSubTypes.Type(value = UnionReservedNames.If.class, name = "if"),
    @JsonSubTypes.Type(value = UnionReservedNames.New.class, name = "new"),
    @JsonSubTypes.Type(value = UnionReservedNames.Interface.class, name = "interface"),
    @JsonSubTypes.Type(value = UnionReservedNames.Void.class, name = "void"),
    @JsonSubTypes.Type(value = UnionReservedNames.Return.class, name = "return"),
    @JsonSubTypes.Type(value = UnionReservedNames.Private.class, name = "private"),
    @JsonSubTypes.Type(value = UnionReservedNames.Public.class, name = "public"),
    @JsonSubTypes.Type(value = UnionReservedNames.Int.class, name = "int"),
    @JsonSubTypes.Type(value = UnionReservedNames.Import.class, name = "import"),
    @JsonSubTypes.Type(value = UnionReservedNames.Final.class, name = "final"),
    @JsonSubTypes.Type(value = UnionReservedNames.Throws.class, name = "throws"),
    @JsonSubTypes.Type(value = UnionReservedNames.Static.class, name = "static"),
    @JsonSubTypes.Type(value = UnionReservedNames.UnionReservedNames_.class, name = "unionReservedNames")
})
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract sealed class UnionReservedNames
        permits UnionReservedNames.Known_,
                UnionReservedNames.Unknown_,
                UnionReservedNames.If,
                UnionReservedNames.New,
                UnionReservedNames.Interface,
                UnionReservedNames.Void,
                UnionReservedNames.Return,
                UnionReservedNames.Private,
                UnionReservedNames.Public,
                UnionReservedNames.Int,
                UnionReservedNames.Import,
                UnionReservedNames.Final,
                UnionReservedNames.Throws,
                UnionReservedNames.Static,
                UnionReservedNames.UnionReservedNames_,
                UnionReservedNames.Unknown {
    public static UnionReservedNames known(String value) {
        return new Known_(value);
    }

    public static UnionReservedNames unknown_(String value) {
        return new Unknown_(value);
    }

    public static UnionReservedNames if_(String value) {
        return new If(value);
    }

    public static UnionReservedNames new_(String value) {
        return new New(value);
    }

    public static UnionReservedNames interface_(String value) {
        return new Interface(value);
    }

    public static UnionReservedNames void_(String value) {
        return new Void(value);
    }

    public static UnionReservedNames return_(String value) {
        return new Return(value);
    }

    public static UnionReservedNames private_(String value) {
        return new Private(value);
    }

    public static UnionReservedNames public_(String value) {
        return new Public(value);
    }

    public static UnionReservedNames int_(String value) {
        return new Int(value);
    }

    public static UnionReservedNames import_(String value) {
        return new Import(value);
    }

    public static UnionReservedNames final_(String value) {
        return new Final(value);
    }

    public static UnionReservedNames throws_(String value) {
        return new Throws(value);
    }

    public static UnionReservedNames static_(String value) {
        return new Static(value);
    }

    public static UnionReservedNames unionReservedNames(String value) {
        return new UnionReservedNames_(value);
    }

    public static UnionReservedNames unknown(@Safe String type, Object value) {
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
            case "unionReservedNames":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: unionReservedNames");
            default:
                return new Unknown(type, Collections.singletonMap(type, value));
        }
    }

    public Known throwOnUnknown() {
        if (this instanceof Unknown) {
            throw new SafeIllegalArgumentException(
                    "Unknown variant of the 'UnionReservedNames' union",
                    SafeArg.of("unknownType", ((Unknown) this).type()));
        } else {
            return (Known) this;
        }
    }

    public abstract <T> T accept(Visitor<T> visitor);

    public sealed interface Known
            permits Known_,
                    Unknown_,
                    If,
                    New,
                    Interface,
                    Void,
                    Return,
                    Private,
                    Public,
                    Int,
                    Import,
                    Final,
                    Throws,
                    Static,
                    UnionReservedNames_ {}

    @JsonTypeName("known")
    public static final class Known_ extends UnionReservedNames implements Known {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private Known_(@JsonSetter("known") @Nonnull String value) {
            Preconditions.checkNotNull(value, "known cannot be null");
            this.value = value;
        }

        @JsonProperty(index = 0)
        private String type() {
            return "known";
        }

        @JsonProperty("known")
        public String value() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitKnown(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof Known_ && equalTo((Known_) other));
        }

        private boolean equalTo(Known_ other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        /**
         * This method is not part of Conjure API. Users should not rely on consistent generation of the toString method
         * between versions of Conjure.
         */
        @Override
        public String toString() {
            return "UnionReservedNames{value: KnownWrapper{value: " + value + "}}";
        }
    }

    @JsonTypeName("unknown")
    public static final class Unknown_ extends UnionReservedNames implements Known {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private Unknown_(@JsonSetter("unknown") @Nonnull String value) {
            Preconditions.checkNotNull(value, "unknown_ cannot be null");
            this.value = value;
        }

        @JsonProperty(index = 0)
        private String type() {
            return "unknown";
        }

        @JsonProperty("unknown")
        public String value() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitUnknown_(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof Unknown_ && equalTo((Unknown_) other));
        }

        private boolean equalTo(Unknown_ other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        /**
         * This method is not part of Conjure API. Users should not rely on consistent generation of the toString method
         * between versions of Conjure.
         */
        @Override
        public String toString() {
            return "UnionReservedNames{value: Unknown_Wrapper{value: " + value + "}}";
        }
    }

    @JsonTypeName("if")
    public static final class If extends UnionReservedNames implements Known {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private If(@JsonSetter("if") @Nonnull String value) {
            Preconditions.checkNotNull(value, "if cannot be null");
            this.value = value;
        }

        @JsonProperty(index = 0)
        private String type() {
            return "if";
        }

        @JsonProperty("if")
        public String value() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitIf(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof If && equalTo((If) other));
        }

        private boolean equalTo(If other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        /**
         * This method is not part of Conjure API. Users should not rely on consistent generation of the toString method
         * between versions of Conjure.
         */
        @Override
        public String toString() {
            return "UnionReservedNames{value: IfWrapper{value: " + value + "}}";
        }
    }

    @JsonTypeName("new")
    public static final class New extends UnionReservedNames implements Known {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private New(@JsonSetter("new") @Nonnull String value) {
            Preconditions.checkNotNull(value, "new cannot be null");
            this.value = value;
        }

        @JsonProperty(index = 0)
        private String type() {
            return "new";
        }

        @JsonProperty("new")
        public String value() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitNew(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof New && equalTo((New) other));
        }

        private boolean equalTo(New other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        /**
         * This method is not part of Conjure API. Users should not rely on consistent generation of the toString method
         * between versions of Conjure.
         */
        @Override
        public String toString() {
            return "UnionReservedNames{value: NewWrapper{value: " + value + "}}";
        }
    }

    @JsonTypeName("interface")
    public static final class Interface extends UnionReservedNames implements Known {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private Interface(@JsonSetter("interface") @Nonnull String value) {
            Preconditions.checkNotNull(value, "interface cannot be null");
            this.value = value;
        }

        @JsonProperty(index = 0)
        private String type() {
            return "interface";
        }

        @JsonProperty("interface")
        public String value() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitInterface(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof Interface && equalTo((Interface) other));
        }

        private boolean equalTo(Interface other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        /**
         * This method is not part of Conjure API. Users should not rely on consistent generation of the toString method
         * between versions of Conjure.
         */
        @Override
        public String toString() {
            return "UnionReservedNames{value: InterfaceWrapper{value: " + value + "}}";
        }
    }

    @JsonTypeName("void")
    public static final class Void extends UnionReservedNames implements Known {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private Void(@JsonSetter("void") @Nonnull String value) {
            Preconditions.checkNotNull(value, "void cannot be null");
            this.value = value;
        }

        @JsonProperty(index = 0)
        private String type() {
            return "void";
        }

        @JsonProperty("void")
        public String value() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitVoid(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof Void && equalTo((Void) other));
        }

        private boolean equalTo(Void other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        /**
         * This method is not part of Conjure API. Users should not rely on consistent generation of the toString method
         * between versions of Conjure.
         */
        @Override
        public String toString() {
            return "UnionReservedNames{value: VoidWrapper{value: " + value + "}}";
        }
    }

    @JsonTypeName("return")
    public static final class Return extends UnionReservedNames implements Known {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private Return(@JsonSetter("return") @Nonnull String value) {
            Preconditions.checkNotNull(value, "return cannot be null");
            this.value = value;
        }

        @JsonProperty(index = 0)
        private String type() {
            return "return";
        }

        @JsonProperty("return")
        public String value() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitReturn(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof Return && equalTo((Return) other));
        }

        private boolean equalTo(Return other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        /**
         * This method is not part of Conjure API. Users should not rely on consistent generation of the toString method
         * between versions of Conjure.
         */
        @Override
        public String toString() {
            return "UnionReservedNames{value: ReturnWrapper{value: " + value + "}}";
        }
    }

    @JsonTypeName("private")
    public static final class Private extends UnionReservedNames implements Known {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private Private(@JsonSetter("private") @Nonnull String value) {
            Preconditions.checkNotNull(value, "private cannot be null");
            this.value = value;
        }

        @JsonProperty(index = 0)
        private String type() {
            return "private";
        }

        @JsonProperty("private")
        public String value() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitPrivate(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof Private && equalTo((Private) other));
        }

        private boolean equalTo(Private other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        /**
         * This method is not part of Conjure API. Users should not rely on consistent generation of the toString method
         * between versions of Conjure.
         */
        @Override
        public String toString() {
            return "UnionReservedNames{value: PrivateWrapper{value: " + value + "}}";
        }
    }

    @JsonTypeName("public")
    public static final class Public extends UnionReservedNames implements Known {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private Public(@JsonSetter("public") @Nonnull String value) {
            Preconditions.checkNotNull(value, "public cannot be null");
            this.value = value;
        }

        @JsonProperty(index = 0)
        private String type() {
            return "public";
        }

        @JsonProperty("public")
        public String value() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitPublic(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof Public && equalTo((Public) other));
        }

        private boolean equalTo(Public other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        /**
         * This method is not part of Conjure API. Users should not rely on consistent generation of the toString method
         * between versions of Conjure.
         */
        @Override
        public String toString() {
            return "UnionReservedNames{value: PublicWrapper{value: " + value + "}}";
        }
    }

    @JsonTypeName("int")
    public static final class Int extends UnionReservedNames implements Known {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private Int(@JsonSetter("int") @Nonnull String value) {
            Preconditions.checkNotNull(value, "int cannot be null");
            this.value = value;
        }

        @JsonProperty(index = 0)
        private String type() {
            return "int";
        }

        @JsonProperty("int")
        public String value() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitInt(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof Int && equalTo((Int) other));
        }

        private boolean equalTo(Int other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        /**
         * This method is not part of Conjure API. Users should not rely on consistent generation of the toString method
         * between versions of Conjure.
         */
        @Override
        public String toString() {
            return "UnionReservedNames{value: IntWrapper{value: " + value + "}}";
        }
    }

    @JsonTypeName("import")
    public static final class Import extends UnionReservedNames implements Known {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private Import(@JsonSetter("import") @Nonnull String value) {
            Preconditions.checkNotNull(value, "import cannot be null");
            this.value = value;
        }

        @JsonProperty(index = 0)
        private String type() {
            return "import";
        }

        @JsonProperty("import")
        public String value() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitImport(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof Import && equalTo((Import) other));
        }

        private boolean equalTo(Import other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        /**
         * This method is not part of Conjure API. Users should not rely on consistent generation of the toString method
         * between versions of Conjure.
         */
        @Override
        public String toString() {
            return "UnionReservedNames{value: ImportWrapper{value: " + value + "}}";
        }
    }

    @JsonTypeName("final")
    public static final class Final extends UnionReservedNames implements Known {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private Final(@JsonSetter("final") @Nonnull String value) {
            Preconditions.checkNotNull(value, "final cannot be null");
            this.value = value;
        }

        @JsonProperty(index = 0)
        private String type() {
            return "final";
        }

        @JsonProperty("final")
        public String value() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitFinal(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof Final && equalTo((Final) other));
        }

        private boolean equalTo(Final other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        /**
         * This method is not part of Conjure API. Users should not rely on consistent generation of the toString method
         * between versions of Conjure.
         */
        @Override
        public String toString() {
            return "UnionReservedNames{value: FinalWrapper{value: " + value + "}}";
        }
    }

    @JsonTypeName("throws")
    public static final class Throws extends UnionReservedNames implements Known {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private Throws(@JsonSetter("throws") @Nonnull String value) {
            Preconditions.checkNotNull(value, "throws cannot be null");
            this.value = value;
        }

        @JsonProperty(index = 0)
        private String type() {
            return "throws";
        }

        @JsonProperty("throws")
        public String value() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitThrows(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof Throws && equalTo((Throws) other));
        }

        private boolean equalTo(Throws other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        /**
         * This method is not part of Conjure API. Users should not rely on consistent generation of the toString method
         * between versions of Conjure.
         */
        @Override
        public String toString() {
            return "UnionReservedNames{value: ThrowsWrapper{value: " + value + "}}";
        }
    }

    @JsonTypeName("static")
    public static final class Static extends UnionReservedNames implements Known {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private Static(@JsonSetter("static") @Nonnull String value) {
            Preconditions.checkNotNull(value, "static cannot be null");
            this.value = value;
        }

        @JsonProperty(index = 0)
        private String type() {
            return "static";
        }

        @JsonProperty("static")
        public String value() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitStatic(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof Static && equalTo((Static) other));
        }

        private boolean equalTo(Static other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        /**
         * This method is not part of Conjure API. Users should not rely on consistent generation of the toString method
         * between versions of Conjure.
         */
        @Override
        public String toString() {
            return "UnionReservedNames{value: StaticWrapper{value: " + value + "}}";
        }
    }

    @JsonTypeName("unionReservedNames")
    public static final class UnionReservedNames_ extends UnionReservedNames implements Known {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private UnionReservedNames_(@JsonSetter("unionReservedNames") @Nonnull String value) {
            Preconditions.checkNotNull(value, "unionReservedNames cannot be null");
            this.value = value;
        }

        @JsonProperty(index = 0)
        private String type() {
            return "unionReservedNames";
        }

        @JsonProperty("unionReservedNames")
        public String value() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitUnionReservedNames(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof UnionReservedNames_ && equalTo((UnionReservedNames_) other));
        }

        private boolean equalTo(UnionReservedNames_ other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        /**
         * This method is not part of Conjure API. Users should not rely on consistent generation of the toString method
         * between versions of Conjure.
         */
        @Override
        public String toString() {
            return "UnionReservedNames{value: UnionReservedNamesWrapper{value: " + value + "}}";
        }
    }

    public static final class Unknown extends UnionReservedNames {
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
            return visitor.visitUnknown(type, value.get(type));
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
            return "UnionReservedNames.Unknown{type: " + type + ", value: " + value + '}';
        }
    }

    public interface Visitor<T> {
        T visitKnown(String value);

        T visitUnknown_(String value);

        T visitIf(String value);

        T visitNew(String value);

        T visitInterface(String value);

        T visitVoid(String value);

        T visitReturn(String value);

        T visitPrivate(String value);

        T visitPublic(String value);

        T visitInt(String value);

        T visitImport(String value);

        T visitFinal(String value);

        T visitThrows(String value);

        T visitStatic(String value);

        T visitUnionReservedNames(String value);

        T visitUnknown(@Safe String unknownType, Object unknownValue);

        static <T> FinalStageVisitorBuilder<T> builder() {
            return new VisitorBuilder<T>();
        }
    }

    private static final class VisitorBuilder<T>
            implements FinalStageVisitorBuilder<T>,
                    IfStageVisitorBuilder<T>,
                    ImportStageVisitorBuilder<T>,
                    IntStageVisitorBuilder<T>,
                    InterfaceStageVisitorBuilder<T>,
                    KnownStageVisitorBuilder<T>,
                    NewStageVisitorBuilder<T>,
                    PrivateStageVisitorBuilder<T>,
                    PublicStageVisitorBuilder<T>,
                    ReturnStageVisitorBuilder<T>,
                    StaticStageVisitorBuilder<T>,
                    ThrowsStageVisitorBuilder<T>,
                    UnionReservedNamesStageVisitorBuilder<T>,
                    Unknown_StageVisitorBuilder<T>,
                    VoidStageVisitorBuilder<T>,
                    UnknownStageVisitorBuilder<T>,
                    Completed_StageVisitorBuilder<T> {
        private Function<String, T> finalVisitor;

        private Function<String, T> ifVisitor;

        private Function<String, T> importVisitor;

        private Function<String, T> intVisitor;

        private Function<String, T> interfaceVisitor;

        private Function<String, T> knownVisitor;

        private Function<String, T> newVisitor;

        private Function<String, T> privateVisitor;

        private Function<String, T> publicVisitor;

        private Function<String, T> returnVisitor;

        private Function<String, T> staticVisitor;

        private Function<String, T> throwsVisitor;

        private Function<String, T> unionReservedNamesVisitor;

        private Function<String, T> unknown_Visitor;

        private Function<String, T> voidVisitor;

        private BiFunction<@Safe String, Object, T> unknownVisitor;

        @Override
        public IfStageVisitorBuilder<T> final_(@Nonnull Function<String, T> finalVisitor) {
            Preconditions.checkNotNull(finalVisitor, "finalVisitor cannot be null");
            this.finalVisitor = finalVisitor;
            return this;
        }

        @Override
        public ImportStageVisitorBuilder<T> if_(@Nonnull Function<String, T> ifVisitor) {
            Preconditions.checkNotNull(ifVisitor, "ifVisitor cannot be null");
            this.ifVisitor = ifVisitor;
            return this;
        }

        @Override
        public IntStageVisitorBuilder<T> import_(@Nonnull Function<String, T> importVisitor) {
            Preconditions.checkNotNull(importVisitor, "importVisitor cannot be null");
            this.importVisitor = importVisitor;
            return this;
        }

        @Override
        public InterfaceStageVisitorBuilder<T> int_(@Nonnull Function<String, T> intVisitor) {
            Preconditions.checkNotNull(intVisitor, "intVisitor cannot be null");
            this.intVisitor = intVisitor;
            return this;
        }

        @Override
        public KnownStageVisitorBuilder<T> interface_(@Nonnull Function<String, T> interfaceVisitor) {
            Preconditions.checkNotNull(interfaceVisitor, "interfaceVisitor cannot be null");
            this.interfaceVisitor = interfaceVisitor;
            return this;
        }

        @Override
        public NewStageVisitorBuilder<T> known(@Nonnull Function<String, T> knownVisitor) {
            Preconditions.checkNotNull(knownVisitor, "knownVisitor cannot be null");
            this.knownVisitor = knownVisitor;
            return this;
        }

        @Override
        public PrivateStageVisitorBuilder<T> new_(@Nonnull Function<String, T> newVisitor) {
            Preconditions.checkNotNull(newVisitor, "newVisitor cannot be null");
            this.newVisitor = newVisitor;
            return this;
        }

        @Override
        public PublicStageVisitorBuilder<T> private_(@Nonnull Function<String, T> privateVisitor) {
            Preconditions.checkNotNull(privateVisitor, "privateVisitor cannot be null");
            this.privateVisitor = privateVisitor;
            return this;
        }

        @Override
        public ReturnStageVisitorBuilder<T> public_(@Nonnull Function<String, T> publicVisitor) {
            Preconditions.checkNotNull(publicVisitor, "publicVisitor cannot be null");
            this.publicVisitor = publicVisitor;
            return this;
        }

        @Override
        public StaticStageVisitorBuilder<T> return_(@Nonnull Function<String, T> returnVisitor) {
            Preconditions.checkNotNull(returnVisitor, "returnVisitor cannot be null");
            this.returnVisitor = returnVisitor;
            return this;
        }

        @Override
        public ThrowsStageVisitorBuilder<T> static_(@Nonnull Function<String, T> staticVisitor) {
            Preconditions.checkNotNull(staticVisitor, "staticVisitor cannot be null");
            this.staticVisitor = staticVisitor;
            return this;
        }

        @Override
        public UnionReservedNamesStageVisitorBuilder<T> throws_(@Nonnull Function<String, T> throwsVisitor) {
            Preconditions.checkNotNull(throwsVisitor, "throwsVisitor cannot be null");
            this.throwsVisitor = throwsVisitor;
            return this;
        }

        @Override
        public Unknown_StageVisitorBuilder<T> unionReservedNames(
                @Nonnull Function<String, T> unionReservedNamesVisitor) {
            Preconditions.checkNotNull(unionReservedNamesVisitor, "unionReservedNamesVisitor cannot be null");
            this.unionReservedNamesVisitor = unionReservedNamesVisitor;
            return this;
        }

        @Override
        public VoidStageVisitorBuilder<T> unknown_(@Nonnull Function<String, T> unknown_Visitor) {
            Preconditions.checkNotNull(unknown_Visitor, "unknown_Visitor cannot be null");
            this.unknown_Visitor = unknown_Visitor;
            return this;
        }

        @Override
        public UnknownStageVisitorBuilder<T> void_(@Nonnull Function<String, T> voidVisitor) {
            Preconditions.checkNotNull(voidVisitor, "voidVisitor cannot be null");
            this.voidVisitor = voidVisitor;
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
                        "Unknown variant of the 'UnionReservedNames' union", SafeArg.of("unknownType", unknownType));
            };
            return this;
        }

        @Override
        public Visitor<T> build() {
            final Function<String, T> finalVisitor = this.finalVisitor;
            final Function<String, T> ifVisitor = this.ifVisitor;
            final Function<String, T> importVisitor = this.importVisitor;
            final Function<String, T> intVisitor = this.intVisitor;
            final Function<String, T> interfaceVisitor = this.interfaceVisitor;
            final Function<String, T> knownVisitor = this.knownVisitor;
            final Function<String, T> newVisitor = this.newVisitor;
            final Function<String, T> privateVisitor = this.privateVisitor;
            final Function<String, T> publicVisitor = this.publicVisitor;
            final Function<String, T> returnVisitor = this.returnVisitor;
            final Function<String, T> staticVisitor = this.staticVisitor;
            final Function<String, T> throwsVisitor = this.throwsVisitor;
            final Function<String, T> unionReservedNamesVisitor = this.unionReservedNamesVisitor;
            final Function<String, T> unknown_Visitor = this.unknown_Visitor;
            final Function<String, T> voidVisitor = this.voidVisitor;
            final BiFunction<@Safe String, Object, T> unknownVisitor = this.unknownVisitor;
            return new Visitor<T>() {
                @Override
                public T visitFinal(String value) {
                    return finalVisitor.apply(value);
                }

                @Override
                public T visitIf(String value) {
                    return ifVisitor.apply(value);
                }

                @Override
                public T visitImport(String value) {
                    return importVisitor.apply(value);
                }

                @Override
                public T visitInt(String value) {
                    return intVisitor.apply(value);
                }

                @Override
                public T visitInterface(String value) {
                    return interfaceVisitor.apply(value);
                }

                @Override
                public T visitKnown(String value) {
                    return knownVisitor.apply(value);
                }

                @Override
                public T visitNew(String value) {
                    return newVisitor.apply(value);
                }

                @Override
                public T visitPrivate(String value) {
                    return privateVisitor.apply(value);
                }

                @Override
                public T visitPublic(String value) {
                    return publicVisitor.apply(value);
                }

                @Override
                public T visitReturn(String value) {
                    return returnVisitor.apply(value);
                }

                @Override
                public T visitStatic(String value) {
                    return staticVisitor.apply(value);
                }

                @Override
                public T visitThrows(String value) {
                    return throwsVisitor.apply(value);
                }

                @Override
                public T visitUnionReservedNames(String value) {
                    return unionReservedNamesVisitor.apply(value);
                }

                @Override
                public T visitUnknown_(String value) {
                    return unknown_Visitor.apply(value);
                }

                @Override
                public T visitVoid(String value) {
                    return voidVisitor.apply(value);
                }

                @Override
                public T visitUnknown(String unknownType, Object unknownValue) {
                    return unknownVisitor.apply(unknownType, unknownValue);
                }
            };
        }
    }

    public interface FinalStageVisitorBuilder<T> {
        IfStageVisitorBuilder<T> final_(@Nonnull Function<String, T> finalVisitor);
    }

    public interface IfStageVisitorBuilder<T> {
        ImportStageVisitorBuilder<T> if_(@Nonnull Function<String, T> ifVisitor);
    }

    public interface ImportStageVisitorBuilder<T> {
        IntStageVisitorBuilder<T> import_(@Nonnull Function<String, T> importVisitor);
    }

    public interface IntStageVisitorBuilder<T> {
        InterfaceStageVisitorBuilder<T> int_(@Nonnull Function<String, T> intVisitor);
    }

    public interface InterfaceStageVisitorBuilder<T> {
        KnownStageVisitorBuilder<T> interface_(@Nonnull Function<String, T> interfaceVisitor);
    }

    public interface KnownStageVisitorBuilder<T> {
        NewStageVisitorBuilder<T> known(@Nonnull Function<String, T> knownVisitor);
    }

    public interface NewStageVisitorBuilder<T> {
        PrivateStageVisitorBuilder<T> new_(@Nonnull Function<String, T> newVisitor);
    }

    public interface PrivateStageVisitorBuilder<T> {
        PublicStageVisitorBuilder<T> private_(@Nonnull Function<String, T> privateVisitor);
    }

    public interface PublicStageVisitorBuilder<T> {
        ReturnStageVisitorBuilder<T> public_(@Nonnull Function<String, T> publicVisitor);
    }

    public interface ReturnStageVisitorBuilder<T> {
        StaticStageVisitorBuilder<T> return_(@Nonnull Function<String, T> returnVisitor);
    }

    public interface StaticStageVisitorBuilder<T> {
        ThrowsStageVisitorBuilder<T> static_(@Nonnull Function<String, T> staticVisitor);
    }

    public interface ThrowsStageVisitorBuilder<T> {
        UnionReservedNamesStageVisitorBuilder<T> throws_(@Nonnull Function<String, T> throwsVisitor);
    }

    public interface UnionReservedNamesStageVisitorBuilder<T> {
        Unknown_StageVisitorBuilder<T> unionReservedNames(@Nonnull Function<String, T> unionReservedNamesVisitor);
    }

    public interface Unknown_StageVisitorBuilder<T> {
        VoidStageVisitorBuilder<T> unknown_(@Nonnull Function<String, T> unknown_Visitor);
    }

    public interface VoidStageVisitorBuilder<T> {
        UnknownStageVisitorBuilder<T> void_(@Nonnull Function<String, T> voidVisitor);
    }

    public interface UnknownStageVisitorBuilder<T> {
        Completed_StageVisitorBuilder<T> unknown(@Nonnull BiFunction<@Safe String, Object, T> unknownVisitor);

        Completed_StageVisitorBuilder<T> unknown(@Nonnull Function<@Safe String, T> unknownVisitor);

        Completed_StageVisitorBuilder<T> throwOnUnknown();
    }

    public interface Completed_StageVisitorBuilder<T> {
        Visitor<T> build();
    }
}
