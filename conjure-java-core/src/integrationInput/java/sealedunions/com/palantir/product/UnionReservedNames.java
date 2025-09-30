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
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.UnionGenerator")
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true,
        defaultImpl = UnionReservedNames.UnknownVariant.class)
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
                    SafeArg.of("unknownType", ((UnknownVariant) this).type()));
        } else {
            return (Known) this;
        }
    }

    <T> T accept(Visitor<T> visitor);

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
            Preconditions.checkNotNull(value, "known_ cannot be null");
            this.value = value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitKnown_(value);
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
            Preconditions.checkNotNull(value, "unknown_ cannot be null");
            this.value = value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitUnknown_(value);
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
            Preconditions.checkNotNull(value, "if_ cannot be null");
            this.value = value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitIf_(value);
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
            Preconditions.checkNotNull(value, "new_ cannot be null");
            this.value = value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitNew_(value);
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
            Preconditions.checkNotNull(value, "interface_ cannot be null");
            this.value = value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitInterface_(value);
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
            Preconditions.checkNotNull(value, "void_ cannot be null");
            this.value = value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitVoid_(value);
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
            Preconditions.checkNotNull(value, "return_ cannot be null");
            this.value = value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitReturn_(value);
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
            Preconditions.checkNotNull(value, "private_ cannot be null");
            this.value = value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitPrivate_(value);
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
            Preconditions.checkNotNull(value, "public_ cannot be null");
            this.value = value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitPublic_(value);
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
            Preconditions.checkNotNull(value, "int_ cannot be null");
            this.value = value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitInt_(value);
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
            Preconditions.checkNotNull(value, "import_ cannot be null");
            this.value = value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitImport_(value);
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
            Preconditions.checkNotNull(value, "final_ cannot be null");
            this.value = value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitFinal_(value);
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
            Preconditions.checkNotNull(value, "throws_ cannot be null");
            this.value = value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitThrows_(value);
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
            Preconditions.checkNotNull(value, "static_ cannot be null");
            this.value = value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitStatic_(value);
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
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitUnknown(type, value.get(type));
        }

        @Override
        public String toString() {
            return "UnionReservedNames.UnknownVariant{value: " + value + '}';
        }
    }

    interface Visitor<T> {
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

        T visitUnknown(@Safe String unknownType, Object unknownValue);

        static <T> FinalStageVisitorBuilder<T> builder() {
            return new VisitorBuilder<T>();
        }
    }

    final class VisitorBuilder<T>
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
        public Unknown_StageVisitorBuilder<T> throws_(@Nonnull Function<String, T> throwsVisitor) {
            Preconditions.checkNotNull(throwsVisitor, "throwsVisitor cannot be null");
            this.throwsVisitor = throwsVisitor;
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

    interface FinalStageVisitorBuilder<T> {
        IfStageVisitorBuilder<T> final_(@Nonnull Function<String, T> finalVisitor);
    }

    interface IfStageVisitorBuilder<T> {
        ImportStageVisitorBuilder<T> if_(@Nonnull Function<String, T> ifVisitor);
    }

    interface ImportStageVisitorBuilder<T> {
        IntStageVisitorBuilder<T> import_(@Nonnull Function<String, T> importVisitor);
    }

    interface IntStageVisitorBuilder<T> {
        InterfaceStageVisitorBuilder<T> int_(@Nonnull Function<String, T> intVisitor);
    }

    interface InterfaceStageVisitorBuilder<T> {
        KnownStageVisitorBuilder<T> interface_(@Nonnull Function<String, T> interfaceVisitor);
    }

    interface KnownStageVisitorBuilder<T> {
        NewStageVisitorBuilder<T> known(@Nonnull Function<String, T> knownVisitor);
    }

    interface NewStageVisitorBuilder<T> {
        PrivateStageVisitorBuilder<T> new_(@Nonnull Function<String, T> newVisitor);
    }

    interface PrivateStageVisitorBuilder<T> {
        PublicStageVisitorBuilder<T> private_(@Nonnull Function<String, T> privateVisitor);
    }

    interface PublicStageVisitorBuilder<T> {
        ReturnStageVisitorBuilder<T> public_(@Nonnull Function<String, T> publicVisitor);
    }

    interface ReturnStageVisitorBuilder<T> {
        StaticStageVisitorBuilder<T> return_(@Nonnull Function<String, T> returnVisitor);
    }

    interface StaticStageVisitorBuilder<T> {
        ThrowsStageVisitorBuilder<T> static_(@Nonnull Function<String, T> staticVisitor);
    }

    interface ThrowsStageVisitorBuilder<T> {
        Unknown_StageVisitorBuilder<T> throws_(@Nonnull Function<String, T> throwsVisitor);
    }

    interface Unknown_StageVisitorBuilder<T> {
        VoidStageVisitorBuilder<T> unknown_(@Nonnull Function<String, T> unknown_Visitor);
    }

    interface VoidStageVisitorBuilder<T> {
        UnknownStageVisitorBuilder<T> void_(@Nonnull Function<String, T> voidVisitor);
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
