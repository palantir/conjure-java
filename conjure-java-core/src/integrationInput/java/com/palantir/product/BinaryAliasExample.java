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
import java.nio.ByteBuffer;
import java.util.Objects;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
@JsonDeserialize(using = BinaryAliasExample.BinaryAliasExampleDeserializer.class)
public final class BinaryAliasExample {
    private final ByteBuffer value;

    private BinaryAliasExample(ByteBuffer value) {
        Objects.requireNonNull(value, "value cannot be null");
        this.value = value;
    }

    @JsonValue
    public ByteBuffer get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof BinaryAliasExample
                        && this.value.equals(((BinaryAliasExample) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @JsonCreator
    public static BinaryAliasExample of(ByteBuffer value) {
        return new BinaryAliasExample(value);
    }

    public static final class BinaryAliasExampleConverter
            implements Converter<ByteBuffer, BinaryAliasExample> {
        @Override
        public BinaryAliasExample convert(ByteBuffer value) {
            return of(value);
        }

        @Override
        public JavaType getInputType(TypeFactory typeFactory) {
            return typeFactory.constructType(new TypeReference<ByteBuffer>() {});
        }

        @Override
        public JavaType getOutputType(TypeFactory typeFactory) {
            return typeFactory.constructType(BinaryAliasExample.class);
        }
    }

    public static final class BinaryAliasExampleDeserializer
            extends StdDelegatingDeserializer<BinaryAliasExample> {
        public BinaryAliasExampleDeserializer() {
            super(new BinaryAliasExampleConverter());
        }

        public BinaryAliasExampleDeserializer(
                Converter<Object, BinaryAliasExample> converter,
                JavaType delegateType,
                JsonDeserializer delegateDeserializer) {
            super(converter, delegateType, delegateDeserializer);
        }

        @Override
        public BinaryAliasExampleDeserializer withDelegate(
                Converter<Object, BinaryAliasExample> converter,
                JavaType delegateType,
                JsonDeserializer<?> delegateDeserializer) {
            return new BinaryAliasExampleDeserializer(
                    converter, delegateType, delegateDeserializer);
        }

        @Override
        public BinaryAliasExample getNullValue(DeserializationContext context)
                throws JsonMappingException {
            // delegating deserializer does the right thing except for null values, which
            // short-circuit
            // since we may alias things that can deserialize from null, got to ask the delegate how
            // it handles nulls.
            return _converter.convert(_delegateDeserializer.getNullValue(context));
        }
    }
}
