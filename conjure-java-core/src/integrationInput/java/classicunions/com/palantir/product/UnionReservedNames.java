package classicunions.com.palantir.product;

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
public final class UnionReservedNames {
    private final Base value;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    private UnionReservedNames(Base value) {
        this.value = value;
    }

    @JsonValue
    private Base getValue() {
        return value;
    }

    public static UnionReservedNames known(String value) {
        return new UnionReservedNames(new KnownWrapper(value));
    }

    public static UnionReservedNames unknown_(String value) {
        return new UnionReservedNames(new Unknown_Wrapper(value));
    }

    public static UnionReservedNames if_(String value) {
        return new UnionReservedNames(new IfWrapper(value));
    }

    public static UnionReservedNames new_(String value) {
        return new UnionReservedNames(new NewWrapper(value));
    }

    public static UnionReservedNames interface_(String value) {
        return new UnionReservedNames(new InterfaceWrapper(value));
    }

    public static UnionReservedNames void_(String value) {
        return new UnionReservedNames(new VoidWrapper(value));
    }

    public static UnionReservedNames return_(String value) {
        return new UnionReservedNames(new ReturnWrapper(value));
    }

    public static UnionReservedNames private_(String value) {
        return new UnionReservedNames(new PrivateWrapper(value));
    }

    public static UnionReservedNames public_(String value) {
        return new UnionReservedNames(new PublicWrapper(value));
    }

    public static UnionReservedNames int_(String value) {
        return new UnionReservedNames(new IntWrapper(value));
    }

    public static UnionReservedNames import_(String value) {
        return new UnionReservedNames(new ImportWrapper(value));
    }

    public static UnionReservedNames final_(String value) {
        return new UnionReservedNames(new FinalWrapper(value));
    }

    public static UnionReservedNames throws_(String value) {
        return new UnionReservedNames(new ThrowsWrapper(value));
    }

    public static UnionReservedNames static_(String value) {
        return new UnionReservedNames(new StaticWrapper(value));
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
            default:
                return new UnionReservedNames(new UnknownWrapper(type, Collections.singletonMap(type, value)));
        }
    }

    public <T> T accept(Visitor<T> visitor) {
        return value.accept(visitor);
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof UnionReservedNames && equalTo((UnionReservedNames) other));
    }

    private boolean equalTo(UnionReservedNames other) {
        return this.value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public String toString() {
        return "UnionReservedNames{value: " + value + '}';
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
        Unknown_StageVisitorBuilder<T> throws_(@Nonnull Function<String, T> throwsVisitor);
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

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.EXISTING_PROPERTY,
            property = "type",
            visible = true,
            defaultImpl = UnknownWrapper.class)
    @JsonSubTypes({
        @JsonSubTypes.Type(KnownWrapper.class),
        @JsonSubTypes.Type(Unknown_Wrapper.class),
        @JsonSubTypes.Type(IfWrapper.class),
        @JsonSubTypes.Type(NewWrapper.class),
        @JsonSubTypes.Type(InterfaceWrapper.class),
        @JsonSubTypes.Type(VoidWrapper.class),
        @JsonSubTypes.Type(ReturnWrapper.class),
        @JsonSubTypes.Type(PrivateWrapper.class),
        @JsonSubTypes.Type(PublicWrapper.class),
        @JsonSubTypes.Type(IntWrapper.class),
        @JsonSubTypes.Type(ImportWrapper.class),
        @JsonSubTypes.Type(FinalWrapper.class),
        @JsonSubTypes.Type(ThrowsWrapper.class),
        @JsonSubTypes.Type(StaticWrapper.class)
    })
    @JsonIgnoreProperties(ignoreUnknown = true)
    private interface Base {
        <T> T accept(Visitor<T> visitor);
    }

    @JsonTypeName("known")
    private static final class KnownWrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private KnownWrapper(@JsonSetter("known") @Nonnull String value) {
            Preconditions.checkNotNull(value, "known cannot be null");
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
            return visitor.visitKnown(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof KnownWrapper && equalTo((KnownWrapper) other));
        }

        private boolean equalTo(KnownWrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "KnownWrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("unknown")
    private static final class Unknown_Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private Unknown_Wrapper(@JsonSetter("unknown") @Nonnull String value) {
            Preconditions.checkNotNull(value, "unknown_ cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "unknown";
        }

        @JsonProperty("unknown")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitUnknown_(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof Unknown_Wrapper && equalTo((Unknown_Wrapper) other));
        }

        private boolean equalTo(Unknown_Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "Unknown_Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("if")
    private static final class IfWrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private IfWrapper(@JsonSetter("if") @Nonnull String value) {
            Preconditions.checkNotNull(value, "if cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "if";
        }

        @JsonProperty("if")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitIf(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof IfWrapper && equalTo((IfWrapper) other));
        }

        private boolean equalTo(IfWrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "IfWrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("new")
    private static final class NewWrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private NewWrapper(@JsonSetter("new") @Nonnull String value) {
            Preconditions.checkNotNull(value, "new cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "new";
        }

        @JsonProperty("new")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitNew(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof NewWrapper && equalTo((NewWrapper) other));
        }

        private boolean equalTo(NewWrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "NewWrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("interface")
    private static final class InterfaceWrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private InterfaceWrapper(@JsonSetter("interface") @Nonnull String value) {
            Preconditions.checkNotNull(value, "interface cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "interface";
        }

        @JsonProperty("interface")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitInterface(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof InterfaceWrapper && equalTo((InterfaceWrapper) other));
        }

        private boolean equalTo(InterfaceWrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "InterfaceWrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("void")
    private static final class VoidWrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private VoidWrapper(@JsonSetter("void") @Nonnull String value) {
            Preconditions.checkNotNull(value, "void cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "void";
        }

        @JsonProperty("void")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitVoid(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof VoidWrapper && equalTo((VoidWrapper) other));
        }

        private boolean equalTo(VoidWrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "VoidWrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("return")
    private static final class ReturnWrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private ReturnWrapper(@JsonSetter("return") @Nonnull String value) {
            Preconditions.checkNotNull(value, "return cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "return";
        }

        @JsonProperty("return")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitReturn(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof ReturnWrapper && equalTo((ReturnWrapper) other));
        }

        private boolean equalTo(ReturnWrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "ReturnWrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("private")
    private static final class PrivateWrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private PrivateWrapper(@JsonSetter("private") @Nonnull String value) {
            Preconditions.checkNotNull(value, "private cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "private";
        }

        @JsonProperty("private")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitPrivate(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof PrivateWrapper && equalTo((PrivateWrapper) other));
        }

        private boolean equalTo(PrivateWrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "PrivateWrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("public")
    private static final class PublicWrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private PublicWrapper(@JsonSetter("public") @Nonnull String value) {
            Preconditions.checkNotNull(value, "public cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "public";
        }

        @JsonProperty("public")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitPublic(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof PublicWrapper && equalTo((PublicWrapper) other));
        }

        private boolean equalTo(PublicWrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "PublicWrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("int")
    private static final class IntWrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private IntWrapper(@JsonSetter("int") @Nonnull String value) {
            Preconditions.checkNotNull(value, "int cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "int";
        }

        @JsonProperty("int")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitInt(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof IntWrapper && equalTo((IntWrapper) other));
        }

        private boolean equalTo(IntWrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "IntWrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("import")
    private static final class ImportWrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private ImportWrapper(@JsonSetter("import") @Nonnull String value) {
            Preconditions.checkNotNull(value, "import cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "import";
        }

        @JsonProperty("import")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitImport(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof ImportWrapper && equalTo((ImportWrapper) other));
        }

        private boolean equalTo(ImportWrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "ImportWrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("final")
    private static final class FinalWrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private FinalWrapper(@JsonSetter("final") @Nonnull String value) {
            Preconditions.checkNotNull(value, "final cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "final";
        }

        @JsonProperty("final")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitFinal(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof FinalWrapper && equalTo((FinalWrapper) other));
        }

        private boolean equalTo(FinalWrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "FinalWrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("throws")
    private static final class ThrowsWrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private ThrowsWrapper(@JsonSetter("throws") @Nonnull String value) {
            Preconditions.checkNotNull(value, "throws cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "throws";
        }

        @JsonProperty("throws")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitThrows(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof ThrowsWrapper && equalTo((ThrowsWrapper) other));
        }

        private boolean equalTo(ThrowsWrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "ThrowsWrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("static")
    private static final class StaticWrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private StaticWrapper(@JsonSetter("static") @Nonnull String value) {
            Preconditions.checkNotNull(value, "static cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "static";
        }

        @JsonProperty("static")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitStatic(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof StaticWrapper && equalTo((StaticWrapper) other));
        }

        private boolean equalTo(StaticWrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "StaticWrapper{value: " + value + '}';
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
