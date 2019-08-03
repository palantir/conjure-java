package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Preconditions;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntFunction;
import javax.annotation.Generated;

/** A type which can either be a StringExample, a set of strings, or an integer. */
@Generated("com.palantir.conjure.java.types.UnionGenerator")
public final class UnionTypeExample {
    private final Base value;

    @JsonCreator
    private UnionTypeExample(Base value) {
        this.value = value;
    }

    @JsonValue
    private Base getValue() {
        return value;
    }

    /** Docs for when UnionTypeExample is of type StringExample. */
    public static UnionTypeExample stringExample(StringExample value) {
        return new UnionTypeExample(new StringExampleWrapper(value));
    }

    public static UnionTypeExample set(Set<String> value) {
        return new UnionTypeExample(new SetWrapper(value));
    }

    public static UnionTypeExample thisFieldIsAnInteger(int value) {
        return new UnionTypeExample(new ThisFieldIsAnIntegerWrapper(value));
    }

    public static UnionTypeExample alsoAnInteger(int value) {
        return new UnionTypeExample(new AlsoAnIntegerWrapper(value));
    }

    public static UnionTypeExample if_(int value) {
        return new UnionTypeExample(new IfWrapper(value));
    }

    public static UnionTypeExample new_(int value) {
        return new UnionTypeExample(new NewWrapper(value));
    }

    public static UnionTypeExample interface_(int value) {
        return new UnionTypeExample(new InterfaceWrapper(value));
    }

    public <T> T accept(Visitor<T> visitor) {
        if (value instanceof StringExampleWrapper) {
            return visitor.visitStringExample(((StringExampleWrapper) value).value);
        } else if (value instanceof SetWrapper) {
            return visitor.visitSet(((SetWrapper) value).value);
        } else if (value instanceof ThisFieldIsAnIntegerWrapper) {
            return visitor.visitThisFieldIsAnInteger(((ThisFieldIsAnIntegerWrapper) value).value);
        } else if (value instanceof AlsoAnIntegerWrapper) {
            return visitor.visitAlsoAnInteger(((AlsoAnIntegerWrapper) value).value);
        } else if (value instanceof IfWrapper) {
            return visitor.visitIf(((IfWrapper) value).value);
        } else if (value instanceof NewWrapper) {
            return visitor.visitNew(((NewWrapper) value).value);
        } else if (value instanceof InterfaceWrapper) {
            return visitor.visitInterface(((InterfaceWrapper) value).value);
        } else if (value instanceof UnknownWrapper) {
            return visitor.visitUnknown(((UnknownWrapper) value).getType());
        }
        throw new IllegalStateException(
                String.format("Could not identify type %s", value.getClass()));
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof UnionTypeExample && equalTo((UnionTypeExample) other));
    }

    private boolean equalTo(UnionTypeExample other) {
        return this.value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return new StringBuilder("UnionTypeExample")
                .append('{')
                .append("value")
                .append(": ")
                .append(value)
                .append('}')
                .toString();
    }

    public interface Visitor<T> {
        T visitStringExample(StringExample value);

        T visitSet(Set<String> value);

        T visitThisFieldIsAnInteger(int value);

        T visitAlsoAnInteger(int value);

        T visitIf(int value);

        T visitNew(int value);

        T visitInterface(int value);

        T visitUnknown(String unknownType);

        static <T> AlsoAnIntegerStageVisitorBuilder<T> builder() {
            return new VisitorBuilder<T>();
        }
    }

    private static class VisitorBuilder<T>
            implements AlsoAnIntegerStageVisitorBuilder<T>,
                    IfStageVisitorBuilder<T>,
                    InterfaceStageVisitorBuilder<T>,
                    NewStageVisitorBuilder<T>,
                    SetStageVisitorBuilder<T>,
                    StringExampleStageVisitorBuilder<T>,
                    ThisFieldIsAnIntegerStageVisitorBuilder<T>,
                    UnknownStageVisitorBuilder<T>,
                    CompletedStageVisitorBuilder<T> {
        private Function<StringExample, T> stringExampleVisitor;

        private Function<Set<String>, T> setVisitor;

        private IntFunction<T> thisFieldIsAnIntegerVisitor;

        private IntFunction<T> alsoAnIntegerVisitor;

        private IntFunction<T> ifVisitor;

        private IntFunction<T> newVisitor;

        private IntFunction<T> interfaceVisitor;

        private Function<String, T> unknownVisitor;

        @Override
        public IfStageVisitorBuilder<T> alsoAnInteger(IntFunction<T> alsoAnIntegerVisitor) {
            Preconditions.checkNotNull(alsoAnIntegerVisitor, "alsoAnIntegerVisitor cannot be null");
            this.alsoAnIntegerVisitor = alsoAnIntegerVisitor;
            return this;
        }

        @Override
        public InterfaceStageVisitorBuilder<T> if_(IntFunction<T> ifVisitor) {
            Preconditions.checkNotNull(ifVisitor, "ifVisitor cannot be null");
            this.ifVisitor = ifVisitor;
            return this;
        }

        @Override
        public NewStageVisitorBuilder<T> interface_(IntFunction<T> interfaceVisitor) {
            Preconditions.checkNotNull(interfaceVisitor, "interfaceVisitor cannot be null");
            this.interfaceVisitor = interfaceVisitor;
            return this;
        }

        @Override
        public SetStageVisitorBuilder<T> new_(IntFunction<T> newVisitor) {
            Preconditions.checkNotNull(newVisitor, "newVisitor cannot be null");
            this.newVisitor = newVisitor;
            return this;
        }

        @Override
        public StringExampleStageVisitorBuilder<T> set(Function<Set<String>, T> setVisitor) {
            Preconditions.checkNotNull(setVisitor, "setVisitor cannot be null");
            this.setVisitor = setVisitor;
            return this;
        }

        @Override
        public ThisFieldIsAnIntegerStageVisitorBuilder<T> stringExample(
                Function<StringExample, T> stringExampleVisitor) {
            Preconditions.checkNotNull(stringExampleVisitor, "stringExampleVisitor cannot be null");
            this.stringExampleVisitor = stringExampleVisitor;
            return this;
        }

        @Override
        public UnknownStageVisitorBuilder<T> thisFieldIsAnInteger(
                IntFunction<T> thisFieldIsAnIntegerVisitor) {
            Preconditions.checkNotNull(
                    thisFieldIsAnIntegerVisitor, "thisFieldIsAnIntegerVisitor cannot be null");
            this.thisFieldIsAnIntegerVisitor = thisFieldIsAnIntegerVisitor;
            return this;
        }

        @Override
        public CompletedStageVisitorBuilder<T> unknown(Function<String, T> unknownVisitor) {
            Preconditions.checkNotNull(unknownVisitor, "unknownVisitor cannot be null");
            this.unknownVisitor = unknownVisitor;
            return this;
        }

        @Override
        public Visitor<T> build() {
            return new Visitor<T>() {
                @Override
                public T visitStringExample(StringExample value) {
                    return stringExampleVisitor.apply(value);
                }

                @Override
                public T visitSet(Set<String> value) {
                    return setVisitor.apply(value);
                }

                @Override
                public T visitThisFieldIsAnInteger(int value) {
                    return thisFieldIsAnIntegerVisitor.apply(value);
                }

                @Override
                public T visitAlsoAnInteger(int value) {
                    return alsoAnIntegerVisitor.apply(value);
                }

                @Override
                public T visitIf(int value) {
                    return ifVisitor.apply(value);
                }

                @Override
                public T visitNew(int value) {
                    return newVisitor.apply(value);
                }

                @Override
                public T visitInterface(int value) {
                    return interfaceVisitor.apply(value);
                }

                @Override
                public T visitUnknown(String value) {
                    return unknownVisitor.apply(value);
                }
            };
        }
    }

    public interface AlsoAnIntegerStageVisitorBuilder<T> {
        IfStageVisitorBuilder<T> alsoAnInteger(IntFunction<T> alsoAnIntegerVisitor);
    }

    public interface IfStageVisitorBuilder<T> {
        InterfaceStageVisitorBuilder<T> if_(IntFunction<T> ifVisitor);
    }

    public interface InterfaceStageVisitorBuilder<T> {
        NewStageVisitorBuilder<T> interface_(IntFunction<T> interfaceVisitor);
    }

    public interface NewStageVisitorBuilder<T> {
        SetStageVisitorBuilder<T> new_(IntFunction<T> newVisitor);
    }

    public interface SetStageVisitorBuilder<T> {
        StringExampleStageVisitorBuilder<T> set(Function<Set<String>, T> setVisitor);
    }

    public interface StringExampleStageVisitorBuilder<T> {
        ThisFieldIsAnIntegerStageVisitorBuilder<T> stringExample(
                Function<StringExample, T> stringExampleVisitor);
    }

    public interface ThisFieldIsAnIntegerStageVisitorBuilder<T> {
        UnknownStageVisitorBuilder<T> thisFieldIsAnInteger(
                IntFunction<T> thisFieldIsAnIntegerVisitor);
    }

    public interface UnknownStageVisitorBuilder<T> {
        CompletedStageVisitorBuilder<T> unknown(Function<String, T> unknownVisitor);
    }

    public interface CompletedStageVisitorBuilder<T> {
        Visitor<T> build();
    }

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            property = "type",
            visible = true,
            defaultImpl = UnknownWrapper.class)
    @JsonSubTypes({
        @JsonSubTypes.Type(StringExampleWrapper.class),
        @JsonSubTypes.Type(SetWrapper.class),
        @JsonSubTypes.Type(ThisFieldIsAnIntegerWrapper.class),
        @JsonSubTypes.Type(AlsoAnIntegerWrapper.class),
        @JsonSubTypes.Type(IfWrapper.class),
        @JsonSubTypes.Type(NewWrapper.class),
        @JsonSubTypes.Type(InterfaceWrapper.class)
    })
    @JsonIgnoreProperties(ignoreUnknown = true)
    private interface Base {}

    @JsonTypeName("stringExample")
    private static class StringExampleWrapper implements Base {
        private final StringExample value;

        @JsonCreator
        private StringExampleWrapper(@JsonProperty("stringExample") StringExample value) {
            Preconditions.checkNotNull(value, "stringExample cannot be null");
            this.value = value;
        }

        @JsonProperty("stringExample")
        private StringExample getValue() {
            return value;
        }

        @Override
        public boolean equals(Object other) {
            return this == other
                    || (other instanceof StringExampleWrapper
                            && equalTo((StringExampleWrapper) other));
        }

        private boolean equalTo(StringExampleWrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }

        @Override
        public String toString() {
            return new StringBuilder("StringExampleWrapper")
                    .append('{')
                    .append("value")
                    .append(": ")
                    .append(value)
                    .append('}')
                    .toString();
        }
    }

    @JsonTypeName("set")
    private static class SetWrapper implements Base {
        private final Set<String> value;

        @JsonCreator
        private SetWrapper(@JsonProperty("set") Set<String> value) {
            Preconditions.checkNotNull(value, "set cannot be null");
            this.value = value;
        }

        @JsonProperty("set")
        private Set<String> getValue() {
            return value;
        }

        @Override
        public boolean equals(Object other) {
            return this == other || (other instanceof SetWrapper && equalTo((SetWrapper) other));
        }

        private boolean equalTo(SetWrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }

        @Override
        public String toString() {
            return new StringBuilder("SetWrapper")
                    .append('{')
                    .append("value")
                    .append(": ")
                    .append(value)
                    .append('}')
                    .toString();
        }
    }

    @JsonTypeName("thisFieldIsAnInteger")
    private static class ThisFieldIsAnIntegerWrapper implements Base {
        private final int value;

        @JsonCreator
        private ThisFieldIsAnIntegerWrapper(@JsonProperty("thisFieldIsAnInteger") int value) {
            Preconditions.checkNotNull(value, "thisFieldIsAnInteger cannot be null");
            this.value = value;
        }

        @JsonProperty("thisFieldIsAnInteger")
        private int getValue() {
            return value;
        }

        @Override
        public boolean equals(Object other) {
            return this == other
                    || (other instanceof ThisFieldIsAnIntegerWrapper
                            && equalTo((ThisFieldIsAnIntegerWrapper) other));
        }

        private boolean equalTo(ThisFieldIsAnIntegerWrapper other) {
            return this.value == other.value;
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }

        @Override
        public String toString() {
            return new StringBuilder("ThisFieldIsAnIntegerWrapper")
                    .append('{')
                    .append("value")
                    .append(": ")
                    .append(value)
                    .append('}')
                    .toString();
        }
    }

    @JsonTypeName("alsoAnInteger")
    private static class AlsoAnIntegerWrapper implements Base {
        private final int value;

        @JsonCreator
        private AlsoAnIntegerWrapper(@JsonProperty("alsoAnInteger") int value) {
            Preconditions.checkNotNull(value, "alsoAnInteger cannot be null");
            this.value = value;
        }

        @JsonProperty("alsoAnInteger")
        private int getValue() {
            return value;
        }

        @Override
        public boolean equals(Object other) {
            return this == other
                    || (other instanceof AlsoAnIntegerWrapper
                            && equalTo((AlsoAnIntegerWrapper) other));
        }

        private boolean equalTo(AlsoAnIntegerWrapper other) {
            return this.value == other.value;
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }

        @Override
        public String toString() {
            return new StringBuilder("AlsoAnIntegerWrapper")
                    .append('{')
                    .append("value")
                    .append(": ")
                    .append(value)
                    .append('}')
                    .toString();
        }
    }

    @JsonTypeName("if")
    private static class IfWrapper implements Base {
        private final int value;

        @JsonCreator
        private IfWrapper(@JsonProperty("if") int value) {
            Preconditions.checkNotNull(value, "if cannot be null");
            this.value = value;
        }

        @JsonProperty("if")
        private int getValue() {
            return value;
        }

        @Override
        public boolean equals(Object other) {
            return this == other || (other instanceof IfWrapper && equalTo((IfWrapper) other));
        }

        private boolean equalTo(IfWrapper other) {
            return this.value == other.value;
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }

        @Override
        public String toString() {
            return new StringBuilder("IfWrapper")
                    .append('{')
                    .append("value")
                    .append(": ")
                    .append(value)
                    .append('}')
                    .toString();
        }
    }

    @JsonTypeName("new")
    private static class NewWrapper implements Base {
        private final int value;

        @JsonCreator
        private NewWrapper(@JsonProperty("new") int value) {
            Preconditions.checkNotNull(value, "new cannot be null");
            this.value = value;
        }

        @JsonProperty("new")
        private int getValue() {
            return value;
        }

        @Override
        public boolean equals(Object other) {
            return this == other || (other instanceof NewWrapper && equalTo((NewWrapper) other));
        }

        private boolean equalTo(NewWrapper other) {
            return this.value == other.value;
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }

        @Override
        public String toString() {
            return new StringBuilder("NewWrapper")
                    .append('{')
                    .append("value")
                    .append(": ")
                    .append(value)
                    .append('}')
                    .toString();
        }
    }

    @JsonTypeName("interface")
    private static class InterfaceWrapper implements Base {
        private final int value;

        @JsonCreator
        private InterfaceWrapper(@JsonProperty("interface") int value) {
            Preconditions.checkNotNull(value, "interface cannot be null");
            this.value = value;
        }

        @JsonProperty("interface")
        private int getValue() {
            return value;
        }

        @Override
        public boolean equals(Object other) {
            return this == other
                    || (other instanceof InterfaceWrapper && equalTo((InterfaceWrapper) other));
        }

        private boolean equalTo(InterfaceWrapper other) {
            return this.value == other.value;
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }

        @Override
        public String toString() {
            return new StringBuilder("InterfaceWrapper")
                    .append('{')
                    .append("value")
                    .append(": ")
                    .append(value)
                    .append('}')
                    .toString();
        }
    }

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.EXISTING_PROPERTY,
            property = "type",
            visible = true)
    private static class UnknownWrapper implements Base {
        private final String type;

        private final Map<String, Object> value;

        @JsonCreator
        private UnknownWrapper(@JsonProperty("type") String type) {
            this(type, new HashMap<String, Object>());
        }

        private UnknownWrapper(String type, Map<String, Object> value) {
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
        public boolean equals(Object other) {
            return this == other
                    || (other instanceof UnknownWrapper && equalTo((UnknownWrapper) other));
        }

        private boolean equalTo(UnknownWrapper other) {
            return this.type.equals(other.type) && this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(type, value);
        }

        @Override
        public String toString() {
            return new StringBuilder("UnknownWrapper")
                    .append('{')
                    .append("type")
                    .append(": ")
                    .append(type)
                    .append(", ")
                    .append("value")
                    .append(": ")
                    .append(value)
                    .append('}')
                    .toString();
        }
    }
}
