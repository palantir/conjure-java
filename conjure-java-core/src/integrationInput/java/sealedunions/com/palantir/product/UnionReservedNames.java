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
    @JsonSubTypes.Type(value = UnionReservedNames.Static.class, name = "static")
})
@JsonIgnoreProperties(ignoreUnknown = true)
public sealed interface UnionReservedNames permits UnionReservedNames.Known, UnionReservedNames.UnknownVariant {
    static UnionReservedNames known(String value) {
        return new Known_(value);
    }

    static UnionReservedNames unknown_(String value) {
        return new Unknown_(value);
    }

    static UnionReservedNames if_(String value) {
        return new If(value);
    }

    static UnionReservedNames new_(String value) {
        return new New(value);
    }

    static UnionReservedNames interface_(String value) {
        return new Interface(value);
    }

    static UnionReservedNames void_(String value) {
        return new Void(value);
    }

    static UnionReservedNames return_(String value) {
        return new Return(value);
    }

    static UnionReservedNames private_(String value) {
        return new Private(value);
    }

    static UnionReservedNames public_(String value) {
        return new Public(value);
    }

    static UnionReservedNames int_(String value) {
        return new Int(value);
    }

    static UnionReservedNames import_(String value) {
        return new Import(value);
    }

    static UnionReservedNames final_(String value) {
        return new Final(value);
    }

    static UnionReservedNames throws_(String value) {
        return new Throws(value);
    }

    static UnionReservedNames static_(String value) {
        return new Static(value);
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
                    Static {}

    @JsonTypeName("known")
    record Known_(String value) implements Known {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public Known_(@JsonSetter("known") @Nonnull String value) {
            Preconditions.checkNotNull(value, "known cannot be null");
            this.value = value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitKnown(value);
        }

        @Override
        public String toString() {
            return "UnionReservedNames.Known_{value: " + value + '}';
        }
    }

    @JsonTypeName("unknown")
    record Unknown_(String value) implements Known {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public Unknown_(@JsonSetter("unknown") @Nonnull String value) {
            Preconditions.checkNotNull(value, "unknown cannot be null");
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

    @JsonTypeName("if")
    record If(String value) implements Known {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public If(@JsonSetter("if") @Nonnull String value) {
            Preconditions.checkNotNull(value, "if cannot be null");
            this.value = value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitIf(value);
        }

        @Override
        public String toString() {
            return "UnionReservedNames.If{value: " + value + '}';
        }
    }

    @JsonTypeName("new")
    record New(String value) implements Known {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public New(@JsonSetter("new") @Nonnull String value) {
            Preconditions.checkNotNull(value, "new cannot be null");
            this.value = value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitNew(value);
        }

        @Override
        public String toString() {
            return "UnionReservedNames.New{value: " + value + '}';
        }
    }

    @JsonTypeName("interface")
    record Interface(String value) implements Known {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public Interface(@JsonSetter("interface") @Nonnull String value) {
            Preconditions.checkNotNull(value, "interface cannot be null");
            this.value = value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitInterface(value);
        }

        @Override
        public String toString() {
            return "UnionReservedNames.Interface{value: " + value + '}';
        }
    }

    @JsonTypeName("void")
    record Void(String value) implements Known {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public Void(@JsonSetter("void") @Nonnull String value) {
            Preconditions.checkNotNull(value, "void cannot be null");
            this.value = value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitVoid(value);
        }

        @Override
        public String toString() {
            return "UnionReservedNames.Void{value: " + value + '}';
        }
    }

    @JsonTypeName("return")
    record Return(String value) implements Known {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public Return(@JsonSetter("return") @Nonnull String value) {
            Preconditions.checkNotNull(value, "return cannot be null");
            this.value = value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitReturn(value);
        }

        @Override
        public String toString() {
            return "UnionReservedNames.Return{value: " + value + '}';
        }
    }

    @JsonTypeName("private")
    record Private(String value) implements Known {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public Private(@JsonSetter("private") @Nonnull String value) {
            Preconditions.checkNotNull(value, "private cannot be null");
            this.value = value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitPrivate(value);
        }

        @Override
        public String toString() {
            return "UnionReservedNames.Private{value: " + value + '}';
        }
    }

    @JsonTypeName("public")
    record Public(String value) implements Known {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public Public(@JsonSetter("public") @Nonnull String value) {
            Preconditions.checkNotNull(value, "public cannot be null");
            this.value = value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitPublic(value);
        }

        @Override
        public String toString() {
            return "UnionReservedNames.Public{value: " + value + '}';
        }
    }

    @JsonTypeName("int")
    record Int(String value) implements Known {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public Int(@JsonSetter("int") @Nonnull String value) {
            Preconditions.checkNotNull(value, "int cannot be null");
            this.value = value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitInt(value);
        }

        @Override
        public String toString() {
            return "UnionReservedNames.Int{value: " + value + '}';
        }
    }

    @JsonTypeName("import")
    record Import(String value) implements Known {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public Import(@JsonSetter("import") @Nonnull String value) {
            Preconditions.checkNotNull(value, "import cannot be null");
            this.value = value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitImport(value);
        }

        @Override
        public String toString() {
            return "UnionReservedNames.Import{value: " + value + '}';
        }
    }

    @JsonTypeName("final")
    record Final(String value) implements Known {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public Final(@JsonSetter("final") @Nonnull String value) {
            Preconditions.checkNotNull(value, "final cannot be null");
            this.value = value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitFinal(value);
        }

        @Override
        public String toString() {
            return "UnionReservedNames.Final{value: " + value + '}';
        }
    }

    @JsonTypeName("throws")
    record Throws(String value) implements Known {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public Throws(@JsonSetter("throws") @Nonnull String value) {
            Preconditions.checkNotNull(value, "throws cannot be null");
            this.value = value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitThrows(value);
        }

        @Override
        public String toString() {
            return "UnionReservedNames.Throws{value: " + value + '}';
        }
    }

    @JsonTypeName("static")
    record Static(String value) implements Known {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public Static(@JsonSetter("static") @Nonnull String value) {
            Preconditions.checkNotNull(value, "static cannot be null");
            this.value = value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitStatic(value);
        }

        @Override
        public String toString() {
            return "UnionReservedNames.Static{value: " + value + '}';
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
