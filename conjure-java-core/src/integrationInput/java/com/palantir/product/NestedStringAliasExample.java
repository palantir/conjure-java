package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import java.util.Objects;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
@JsonDeserialize(using = NestedStringAliasExample.NestedStringAliasExampleDeserializer.class)
public final class NestedStringAliasExample {
    private final StringAliasExample value;

    private NestedStringAliasExample(StringAliasExample value) {
        Objects.requireNonNull(value, "value cannot be null");
        this.value = value;
    }

    @JsonValue
    public StringAliasExample get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof NestedStringAliasExample
                        && this.value.equals(((NestedStringAliasExample) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    public static NestedStringAliasExample valueOf(String value) {
        return new NestedStringAliasExample(StringAliasExample.valueOf(value));
    }

    @JsonCreator
    public static NestedStringAliasExample of(StringAliasExample value) {
        return new NestedStringAliasExample(value);
    }

    public static final class NestedStringAliasExampleConverter
            implements Converter<StringAliasExample, NestedStringAliasExample> {
        @Override
        public NestedStringAliasExample convert(StringAliasExample value) {
            return of(value);
        }

        @Override
        public JavaType getInputType(TypeFactory typeFactory) {
            return typeFactory.constructType(new TypeReference<StringAliasExample>() {});
        }

        @Override
        public JavaType getOutputType(TypeFactory typeFactory) {
            return typeFactory.constructType(NestedStringAliasExample.class);
        }
    }

    public static final class NestedStringAliasExampleDeserializer
            extends StdDelegatingDeserializer<NestedStringAliasExample> {
        public NestedStringAliasExampleDeserializer() {
            super(new NestedStringAliasExampleConverter());
        }

        public NestedStringAliasExampleDeserializer(
                Converter<Object, NestedStringAliasExample> converter,
                JavaType delegateType,
                JsonDeserializer delegateDeserializer) {
            super(converter, delegateType, delegateDeserializer);
        }

        @Override
        public NestedStringAliasExampleDeserializer withDelegate(
                Converter<Object, NestedStringAliasExample> converter,
                JavaType delegateType,
                JsonDeserializer<?> delegateDeserializer) {
            return new NestedStringAliasExampleDeserializer(
                    converter, delegateType, delegateDeserializer);
        }

        @Override
        public NestedStringAliasExample getNullValue(DeserializationContext context)
                throws JsonMappingException {
            // delegating deserializer does the right thing except for null values, which
            // short-circuit
            // since we may alias things that can deserialize from null, got to ask the delegate how
            // it handles nulls.
            return _converter.convert(_delegateDeserializer.getNullValue(context));
        }
    }
}
