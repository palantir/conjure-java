package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import javax.annotation.Generated;

/** A type which can either be a StringExample, a set of strings, or an integer. */
@Generated("com.palantir.conjure.java.types.UnionGenerator")
public final class UnionTypeExample {
    @JsonUnwrapped private final Inner_Union union;

    @JsonCreator
    private UnionTypeExample(Inner_Union union) {
        Objects.requireNonNull(union, "union must not be null");
        this.union = union;
    }

    public <T> T accept(Visitor<T> visitor) {
        if (union.getStringExample().isPresent()) {
            return visitor.visitStringExample(union.getStringExample().get());
        } else if (union.getSet().isPresent()) {
            return visitor.visitSet(union.getSet().get());
        } else if (union.getThisFieldIsAnInteger().isPresent()) {
            return visitor.visitThisFieldIsAnInteger(union.getThisFieldIsAnInteger().getAsInt());
        } else if (union.getAlsoAnInteger().isPresent()) {
            return visitor.visitAlsoAnInteger(union.getAlsoAnInteger().getAsInt());
        } else if (union.getIf().isPresent()) {
            return visitor.visitIf(union.getIf().getAsInt());
        } else if (union.getNew().isPresent()) {
            return visitor.visitNew(union.getNew().getAsInt());
        } else if (union.getInterface().isPresent()) {
            return visitor.visitInterface(union.getInterface().getAsInt());
        }
        return visitor.visitUnknown(union.getType());
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof UnionTypeExample && equalTo((UnionTypeExample) other));
    }

    private boolean equalTo(UnionTypeExample other) {
        return this.union.equals(other.union);
    }

    @Override
    public int hashCode() {
        return union.hashCode();
    }

    @Override
    public String toString() {
        return union.toString();
    }

    /** Docs for when UnionTypeExample is of type StringExample. */
    public static UnionTypeExample stringExample(StringExample value) {
        return new UnionTypeExample(
                Inner_Union.builder().type("stringExample").stringExample(value).build());
    }

    public static UnionTypeExample set(Set<String> value) {
        return new UnionTypeExample(Inner_Union.builder().type("set").set(value).build());
    }

    public static UnionTypeExample thisFieldIsAnInteger(int value) {
        return new UnionTypeExample(
                Inner_Union.builder()
                        .type("thisFieldIsAnInteger")
                        .thisFieldIsAnInteger(value)
                        .build());
    }

    public static UnionTypeExample alsoAnInteger(int value) {
        return new UnionTypeExample(
                Inner_Union.builder().type("alsoAnInteger").alsoAnInteger(value).build());
    }

    public static UnionTypeExample if_(int value) {
        return new UnionTypeExample(Inner_Union.builder().type("if").if_(value).build());
    }

    public static UnionTypeExample new_(int value) {
        return new UnionTypeExample(Inner_Union.builder().type("new").new_(value).build());
    }

    public static UnionTypeExample interface_(int value) {
        return new UnionTypeExample(
                Inner_Union.builder().type("interface").interface_(value).build());
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
    }

    @JsonDeserialize(builder = Inner_Union.Builder.class)
    @Generated("com.palantir.conjure.java.types.BeanGenerator")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private static final class Inner_Union {
        private final String type;

        private final Optional<StringExample> stringExample;

        private final Optional<Set<String>> set;

        private final OptionalInt thisFieldIsAnInteger;

        private final OptionalInt alsoAnInteger;

        private final OptionalInt if_;

        private final OptionalInt new_;

        private final OptionalInt interface_;

        private final Map<String, Object> __unknownProperties;

        private volatile int memoizedHashCode;

        private Inner_Union(
                String type,
                Optional<StringExample> stringExample,
                Optional<Set<String>> set,
                OptionalInt thisFieldIsAnInteger,
                OptionalInt alsoAnInteger,
                OptionalInt if_,
                OptionalInt new_,
                OptionalInt interface_,
                Map<String, Object> __unknownProperties) {
            validateFields(
                    type,
                    stringExample,
                    set,
                    thisFieldIsAnInteger,
                    alsoAnInteger,
                    if_,
                    new_,
                    interface_);
            this.type = type;
            this.stringExample = stringExample;
            this.set = set;
            this.thisFieldIsAnInteger = thisFieldIsAnInteger;
            this.alsoAnInteger = alsoAnInteger;
            this.if_ = if_;
            this.new_ = new_;
            this.interface_ = interface_;
            this.__unknownProperties = Collections.unmodifiableMap(__unknownProperties);
        }

        @JsonProperty("type")
        public String getType() {
            return this.type;
        }

        @JsonProperty("stringExample")
        public Optional<StringExample> getStringExample() {
            return this.stringExample;
        }

        @JsonProperty("set")
        public Optional<Set<String>> getSet() {
            return this.set;
        }

        @JsonProperty("thisFieldIsAnInteger")
        public OptionalInt getThisFieldIsAnInteger() {
            return this.thisFieldIsAnInteger;
        }

        @JsonProperty("alsoAnInteger")
        public OptionalInt getAlsoAnInteger() {
            return this.alsoAnInteger;
        }

        @JsonProperty("if")
        public OptionalInt getIf() {
            return this.if_;
        }

        @JsonProperty("new")
        public OptionalInt getNew() {
            return this.new_;
        }

        @JsonProperty("interface")
        public OptionalInt getInterface() {
            return this.interface_;
        }

        @JsonAnyGetter
        Map<String, Object> unknownProperties() {
            return __unknownProperties;
        }

        @Override
        public boolean equals(Object other) {
            return this == other || (other instanceof Inner_Union && equalTo((Inner_Union) other));
        }

        private boolean equalTo(Inner_Union other) {
            return this.type.equals(other.type)
                    && this.stringExample.equals(other.stringExample)
                    && this.set.equals(other.set)
                    && this.thisFieldIsAnInteger.equals(other.thisFieldIsAnInteger)
                    && this.alsoAnInteger.equals(other.alsoAnInteger)
                    && this.if_.equals(other.if_)
                    && this.new_.equals(other.new_)
                    && this.interface_.equals(other.interface_)
                    && this.__unknownProperties.equals(other.__unknownProperties);
        }

        @Override
        public int hashCode() {
            if (memoizedHashCode == 0) {
                memoizedHashCode =
                        Objects.hash(
                                type,
                                stringExample,
                                set,
                                thisFieldIsAnInteger,
                                alsoAnInteger,
                                if_,
                                new_,
                                interface_,
                                __unknownProperties);
            }
            return memoizedHashCode;
        }

        @Override
        public String toString() {
            return new StringBuilder("Inner_Union")
                    .append("{")
                    .append("type")
                    .append(": ")
                    .append(type)
                    .append(", ")
                    .append("stringExample")
                    .append(": ")
                    .append(stringExample)
                    .append(", ")
                    .append("set")
                    .append(": ")
                    .append(set)
                    .append(", ")
                    .append("thisFieldIsAnInteger")
                    .append(": ")
                    .append(thisFieldIsAnInteger)
                    .append(", ")
                    .append("alsoAnInteger")
                    .append(": ")
                    .append(alsoAnInteger)
                    .append(", ")
                    .append("if")
                    .append(": ")
                    .append(if_)
                    .append(", ")
                    .append("new")
                    .append(": ")
                    .append(new_)
                    .append(", ")
                    .append("interface")
                    .append(": ")
                    .append(interface_)
                    .append("}")
                    .toString();
        }

        private static void validateFields(
                String type,
                Optional<StringExample> stringExample,
                Optional<Set<String>> set,
                OptionalInt thisFieldIsAnInteger,
                OptionalInt alsoAnInteger,
                OptionalInt if_,
                OptionalInt new_,
                OptionalInt interface_) {
            List<String> missingFields = null;
            missingFields = addFieldIfMissing(missingFields, type, "type");
            missingFields = addFieldIfMissing(missingFields, stringExample, "stringExample");
            missingFields = addFieldIfMissing(missingFields, set, "set");
            missingFields =
                    addFieldIfMissing(missingFields, thisFieldIsAnInteger, "thisFieldIsAnInteger");
            missingFields = addFieldIfMissing(missingFields, alsoAnInteger, "alsoAnInteger");
            missingFields = addFieldIfMissing(missingFields, if_, "if");
            missingFields = addFieldIfMissing(missingFields, new_, "new");
            missingFields = addFieldIfMissing(missingFields, interface_, "interface");
            if (missingFields != null) {
                throw new IllegalArgumentException(
                        "Some required fields have not been set: " + missingFields);
            }
        }

        private static List<String> addFieldIfMissing(
                List<String> prev, Object fieldValue, String fieldName) {
            List<String> missingFields = prev;
            if (fieldValue == null) {
                if (missingFields == null) {
                    missingFields = new ArrayList<>(8);
                }
                missingFields.add(fieldName);
            }
            return missingFields;
        }

        public static Builder builder() {
            return new Builder();
        }

        @Generated("com.palantir.conjure.java.types.BeanBuilderGenerator")
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static final class Builder {
            private String type;

            private Optional<StringExample> stringExample = Optional.empty();

            private Optional<Set<String>> set = Optional.empty();

            private OptionalInt thisFieldIsAnInteger = OptionalInt.empty();

            private OptionalInt alsoAnInteger = OptionalInt.empty();

            private OptionalInt if_ = OptionalInt.empty();

            private OptionalInt new_ = OptionalInt.empty();

            private OptionalInt interface_ = OptionalInt.empty();

            Map<String, Object> __unknownProperties = new LinkedHashMap<>();

            private Builder() {}

            public Builder from(Inner_Union other) {
                type(other.getType());
                stringExample(other.getStringExample());
                set(other.getSet());
                thisFieldIsAnInteger(other.getThisFieldIsAnInteger());
                alsoAnInteger(other.getAlsoAnInteger());
                if_(other.getIf());
                new_(other.getNew());
                interface_(other.getInterface());
                return this;
            }

            @JsonSetter("type")
            public Builder type(String type) {
                this.type = Objects.requireNonNull(type, "type cannot be null");
                return this;
            }

            @JsonSetter("stringExample")
            public Builder stringExample(Optional<StringExample> stringExample) {
                this.stringExample =
                        Objects.requireNonNull(stringExample, "stringExample cannot be null");
                return this;
            }

            public Builder stringExample(StringExample stringExample) {
                this.stringExample =
                        Optional.of(
                                Objects.requireNonNull(
                                        stringExample, "stringExample cannot be null"));
                return this;
            }

            @JsonSetter("set")
            public Builder set(Optional<Set<String>> set) {
                this.set = Objects.requireNonNull(set, "set cannot be null");
                return this;
            }

            public Builder set(Set<String> set) {
                this.set = Optional.of(Objects.requireNonNull(set, "set cannot be null"));
                return this;
            }

            @JsonSetter("thisFieldIsAnInteger")
            public Builder thisFieldIsAnInteger(OptionalInt thisFieldIsAnInteger) {
                this.thisFieldIsAnInteger =
                        Objects.requireNonNull(
                                thisFieldIsAnInteger, "thisFieldIsAnInteger cannot be null");
                return this;
            }

            public Builder thisFieldIsAnInteger(int thisFieldIsAnInteger) {
                this.thisFieldIsAnInteger = OptionalInt.of(thisFieldIsAnInteger);
                return this;
            }

            @JsonSetter("alsoAnInteger")
            public Builder alsoAnInteger(OptionalInt alsoAnInteger) {
                this.alsoAnInteger =
                        Objects.requireNonNull(alsoAnInteger, "alsoAnInteger cannot be null");
                return this;
            }

            public Builder alsoAnInteger(int alsoAnInteger) {
                this.alsoAnInteger = OptionalInt.of(alsoAnInteger);
                return this;
            }

            @JsonSetter("if")
            public Builder if_(OptionalInt if_) {
                this.if_ = Objects.requireNonNull(if_, "if cannot be null");
                return this;
            }

            public Builder if_(int if_) {
                this.if_ = OptionalInt.of(if_);
                return this;
            }

            @JsonSetter("new")
            public Builder new_(OptionalInt new_) {
                this.new_ = Objects.requireNonNull(new_, "new cannot be null");
                return this;
            }

            public Builder new_(int new_) {
                this.new_ = OptionalInt.of(new_);
                return this;
            }

            @JsonSetter("interface")
            public Builder interface_(OptionalInt interface_) {
                this.interface_ = Objects.requireNonNull(interface_, "interface cannot be null");
                return this;
            }

            public Builder interface_(int interface_) {
                this.interface_ = OptionalInt.of(interface_);
                return this;
            }

            public Inner_Union build() {
                return new Inner_Union(
                        type,
                        stringExample,
                        set,
                        thisFieldIsAnInteger,
                        alsoAnInteger,
                        if_,
                        new_,
                        interface_,
                        __unknownProperties);
            }

            @JsonAnySetter
            private void setUnknownProperties(String key, Object value) {
                __unknownProperties.put(key, value);
            }
        }
    }
}
