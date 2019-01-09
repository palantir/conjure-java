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
@JsonDeserialize(using = ReferenceAliasExample.ReferenceAliasExampleDeserializer.class)
public final class ReferenceAliasExample {
    private final AnyExample value;

    private ReferenceAliasExample(AnyExample value) {
        Objects.requireNonNull(value, "value cannot be null");
        this.value = value;
    }

    @JsonValue
    public AnyExample get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof ReferenceAliasExample
                        && this.value.equals(((ReferenceAliasExample) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @JsonCreator
    public static ReferenceAliasExample of(AnyExample value) {
        return new ReferenceAliasExample(value);
    }

    public static final class ReferenceAliasExampleConverter
            implements Converter<AnyExample, ReferenceAliasExample> {
        @Override
        public ReferenceAliasExample convert(AnyExample value) {
            return of(value);
        }

        @Override
        public JavaType getInputType(TypeFactory typeFactory) {
            return typeFactory.constructType(new TypeReference<AnyExample>() {});
        }

        @Override
        public JavaType getOutputType(TypeFactory typeFactory) {
            return typeFactory.constructType(ReferenceAliasExample.class);
        }
    }

    public static final class ReferenceAliasExampleDeserializer
            extends StdDelegatingDeserializer<ReferenceAliasExample> {
        public ReferenceAliasExampleDeserializer() {
            super(new ReferenceAliasExampleConverter());
        }

        public ReferenceAliasExampleDeserializer(
                Converter<Object, ReferenceAliasExample> converter,
                JavaType delegateType,
                JsonDeserializer delegateDeserializer) {
            super(converter, delegateType, delegateDeserializer);
        }

        @Override
        public ReferenceAliasExampleDeserializer withDelegate(
                Converter<Object, ReferenceAliasExample> converter,
                JavaType delegateType,
                JsonDeserializer<?> delegateDeserializer) {
            return new ReferenceAliasExampleDeserializer(
                    converter, delegateType, delegateDeserializer);
        }

        @Override
        public ReferenceAliasExample getNullValue(DeserializationContext context)
                throws JsonMappingException {
            // delegating deserializer does the right thing except for null values, which
            // short-circuit
            // since we may alias things that can deserialize from null, got to ask the delegate how
            // it handles nulls.
            return _converter.convert(_delegateDeserializer.getNullValue(context));
        }
    }
}
