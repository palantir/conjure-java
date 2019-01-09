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
@JsonDeserialize(using = DoubleAliasExample.DoubleAliasExampleDeserializer.class)
public final class DoubleAliasExample {
    private final double value;

    private DoubleAliasExample(double value) {
        this.value = value;
    }

    @JsonValue
    public double get() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof DoubleAliasExample
                        && this.value == ((DoubleAliasExample) other).value);
    }

    @Override
    public int hashCode() {
        return Double.hashCode(value);
    }

    public static DoubleAliasExample valueOf(String value) {
        return new DoubleAliasExample(Double.parseDouble(value));
    }

    @JsonCreator
    public static DoubleAliasExample of(double value) {
        return new DoubleAliasExample(value);
    }

    @JsonCreator
    public static DoubleAliasExample of(int value) {
        return new DoubleAliasExample((double) value);
    }

    @JsonCreator
    public static DoubleAliasExample of(String value) {
        switch (value) {
            case "NaN":
                return DoubleAliasExample.of(Double.NaN);
            case "Infinity":
                return DoubleAliasExample.of(Double.POSITIVE_INFINITY);
            case "-Infinity":
                return DoubleAliasExample.of(Double.NEGATIVE_INFINITY);
            default:
                throw new IllegalArgumentException(
                        "Cannot deserialize string into double: " + value);
        }
    }

    public static final class DoubleAliasExampleConverter
            implements Converter<Double, DoubleAliasExample> {
        @Override
        public DoubleAliasExample convert(Double value) {
            return of(value);
        }

        @Override
        public JavaType getInputType(TypeFactory typeFactory) {
            return typeFactory.constructType(new TypeReference<Double>() {});
        }

        @Override
        public JavaType getOutputType(TypeFactory typeFactory) {
            return typeFactory.constructType(DoubleAliasExample.class);
        }
    }

    public static final class DoubleAliasExampleDeserializer
            extends StdDelegatingDeserializer<DoubleAliasExample> {
        public DoubleAliasExampleDeserializer() {
            super(new DoubleAliasExampleConverter());
        }

        public DoubleAliasExampleDeserializer(
                Converter<Object, DoubleAliasExample> converter,
                JavaType delegateType,
                JsonDeserializer delegateDeserializer) {
            super(converter, delegateType, delegateDeserializer);
        }

        @Override
        public DoubleAliasExampleDeserializer withDelegate(
                Converter<Object, DoubleAliasExample> converter,
                JavaType delegateType,
                JsonDeserializer<?> delegateDeserializer) {
            return new DoubleAliasExampleDeserializer(
                    converter, delegateType, delegateDeserializer);
        }

        @Override
        public DoubleAliasExample getNullValue(DeserializationContext context)
                throws JsonMappingException {
            // delegating deserializer does the right thing except for null values, which
            // short-circuit
            // since we may alias things that can deserialize from null, got to ask the delegate how
            // it handles nulls.
            return _converter.convert(_delegateDeserializer.getNullValue(context));
        }
    }
}
