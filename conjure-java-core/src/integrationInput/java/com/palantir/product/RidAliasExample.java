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
import com.palantir.ri.ResourceIdentifier;
import java.util.Objects;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
@JsonDeserialize(using = RidAliasExample.RidAliasExampleDeserializer.class)
public final class RidAliasExample {
    private final ResourceIdentifier value;

    private RidAliasExample(ResourceIdentifier value) {
        Objects.requireNonNull(value, "value cannot be null");
        this.value = value;
    }

    @JsonValue
    public ResourceIdentifier get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof RidAliasExample
                        && this.value.equals(((RidAliasExample) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    public static RidAliasExample valueOf(String value) {
        return new RidAliasExample(ResourceIdentifier.valueOf(value));
    }

    @JsonCreator
    public static RidAliasExample of(ResourceIdentifier value) {
        return new RidAliasExample(value);
    }

    public static final class RidAliasExampleConverter
            implements Converter<ResourceIdentifier, RidAliasExample> {
        @Override
        public RidAliasExample convert(ResourceIdentifier value) {
            return of(value);
        }

        @Override
        public JavaType getInputType(TypeFactory typeFactory) {
            return typeFactory.constructType(new TypeReference<ResourceIdentifier>() {});
        }

        @Override
        public JavaType getOutputType(TypeFactory typeFactory) {
            return typeFactory.constructType(RidAliasExample.class);
        }
    }

    public static final class RidAliasExampleDeserializer
            extends StdDelegatingDeserializer<RidAliasExample> {
        public RidAliasExampleDeserializer() {
            super(new RidAliasExampleConverter());
        }

        public RidAliasExampleDeserializer(
                Converter<Object, RidAliasExample> converter,
                JavaType delegateType,
                JsonDeserializer delegateDeserializer) {
            super(converter, delegateType, delegateDeserializer);
        }

        @Override
        public RidAliasExampleDeserializer withDelegate(
                Converter<Object, RidAliasExample> converter,
                JavaType delegateType,
                JsonDeserializer<?> delegateDeserializer) {
            return new RidAliasExampleDeserializer(converter, delegateType, delegateDeserializer);
        }

        @Override
        public RidAliasExample getNullValue(DeserializationContext context)
                throws JsonMappingException {
            // delegating deserializer does the right thing except for null values, which
            // short-circuit
            // since we may alias things that can deserialize from null, got to ask the delegate how
            // it handles nulls.
            return _converter.convert(_delegateDeserializer.getNullValue(context));
        }
    }
}
