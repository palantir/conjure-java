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
@JsonDeserialize(using = BooleanAliasExample.BooleanAliasExampleDeserializer.class)
public final class BooleanAliasExample {
    private final boolean value;

    private BooleanAliasExample(boolean value) {
        this.value = value;
    }

    @JsonValue
    public boolean get() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof BooleanAliasExample
                        && this.value == ((BooleanAliasExample) other).value);
    }

    @Override
    public int hashCode() {
        return Boolean.hashCode(value);
    }

    public static BooleanAliasExample valueOf(String value) {
        return new BooleanAliasExample(Boolean.parseBoolean(value));
    }

    @JsonCreator
    public static BooleanAliasExample of(boolean value) {
        return new BooleanAliasExample(value);
    }

    public static final class BooleanAliasExampleConverter
            implements Converter<Boolean, BooleanAliasExample> {
        @Override
        public BooleanAliasExample convert(Boolean value) {
            return of(value);
        }

        @Override
        public JavaType getInputType(TypeFactory typeFactory) {
            return typeFactory.constructType(new TypeReference<Boolean>() {});
        }

        @Override
        public JavaType getOutputType(TypeFactory typeFactory) {
            return typeFactory.constructType(BooleanAliasExample.class);
        }
    }

    public static final class BooleanAliasExampleDeserializer
            extends StdDelegatingDeserializer<BooleanAliasExample> {
        public BooleanAliasExampleDeserializer() {
            super(new BooleanAliasExampleConverter());
        }

        public BooleanAliasExampleDeserializer(
                Converter<Object, BooleanAliasExample> converter,
                JavaType delegateType,
                JsonDeserializer delegateDeserializer) {
            super(converter, delegateType, delegateDeserializer);
        }

        @Override
        public BooleanAliasExampleDeserializer withDelegate(
                Converter<Object, BooleanAliasExample> converter,
                JavaType delegateType,
                JsonDeserializer<?> delegateDeserializer) {
            return new BooleanAliasExampleDeserializer(
                    converter, delegateType, delegateDeserializer);
        }

        @Override
        public BooleanAliasExample getNullValue(DeserializationContext context)
                throws JsonMappingException {
            // delegating deserializer does the right thing except for null values, which
            // short-circuit
            // since we may alias things that can deserialize from null, got to ask the delegate how
            // it handles nulls.
            return _converter.convert(_delegateDeserializer.getNullValue(context));
        }
    }
}
