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
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
@JsonDeserialize(using = IntegerAliasExample.IntegerAliasExampleDeserializer.class)
public final class IntegerAliasExample {
    private final int value;

    private IntegerAliasExample(int value) {
        this.value = value;
    }

    @JsonValue
    public int get() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof IntegerAliasExample
                        && this.value == ((IntegerAliasExample) other).value);
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(value);
    }

    public static IntegerAliasExample valueOf(String value) {
        return new IntegerAliasExample(Integer.parseInt(value));
    }

    @JsonCreator
    public static IntegerAliasExample of(int value) {
        return new IntegerAliasExample(value);
    }

    public static final class IntegerAliasExampleConverter
            implements Converter<Integer, IntegerAliasExample> {
        @Override
        public IntegerAliasExample convert(Integer value) {
            return of(value);
        }

        @Override
        public JavaType getInputType(TypeFactory typeFactory) {
            return typeFactory.constructType(new TypeReference<Integer>() {});
        }

        @Override
        public JavaType getOutputType(TypeFactory typeFactory) {
            return typeFactory.constructType(IntegerAliasExample.class);
        }
    }

    public static final class IntegerAliasExampleDeserializer
            extends StdDelegatingDeserializer<IntegerAliasExample> {
        public IntegerAliasExampleDeserializer() {
            super(new IntegerAliasExampleConverter());
        }

        public IntegerAliasExampleDeserializer(
                Converter<Object, IntegerAliasExample> converter,
                JavaType delegateType,
                JsonDeserializer delegateDeserializer) {
            super(converter, delegateType, delegateDeserializer);
        }

        @Override
        public IntegerAliasExampleDeserializer withDelegate(
                Converter<Object, IntegerAliasExample> converter,
                JavaType delegateType,
                JsonDeserializer<?> delegateDeserializer) {
            return new IntegerAliasExampleDeserializer(
                    converter, delegateType, delegateDeserializer);
        }

        @Override
        public IntegerAliasExample getNullValue(DeserializationContext context)
                throws JsonMappingException {
            // delegating deserializer does the right thing except for null values, which
            // short-circuit
            // since we may alias things that can deserialize from null, got to ask the delegate how
            // it handles nulls.
            return _converter.convert(_delegateDeserializer.getNullValue(context));
        }
    }
}
