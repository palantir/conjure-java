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
import com.palantir.tokens.auth.BearerToken;
import java.util.Objects;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
@JsonDeserialize(using = BearerTokenAliasExample.BearerTokenAliasExampleDeserializer.class)
public final class BearerTokenAliasExample {
    private final BearerToken value;

    private BearerTokenAliasExample(BearerToken value) {
        Objects.requireNonNull(value, "value cannot be null");
        this.value = value;
    }

    @JsonValue
    public BearerToken get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof BearerTokenAliasExample
                        && this.value.equals(((BearerTokenAliasExample) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    public static BearerTokenAliasExample valueOf(String value) {
        return new BearerTokenAliasExample(BearerToken.valueOf(value));
    }

    @JsonCreator
    public static BearerTokenAliasExample of(BearerToken value) {
        return new BearerTokenAliasExample(value);
    }

    public static final class BearerTokenAliasExampleConverter
            implements Converter<BearerToken, BearerTokenAliasExample> {
        @Override
        public BearerTokenAliasExample convert(BearerToken value) {
            return of(value);
        }

        @Override
        public JavaType getInputType(TypeFactory typeFactory) {
            return typeFactory.constructType(new TypeReference<BearerToken>() {});
        }

        @Override
        public JavaType getOutputType(TypeFactory typeFactory) {
            return typeFactory.constructType(BearerTokenAliasExample.class);
        }
    }

    public static final class BearerTokenAliasExampleDeserializer
            extends StdDelegatingDeserializer<BearerTokenAliasExample> {
        public BearerTokenAliasExampleDeserializer() {
            super(new BearerTokenAliasExampleConverter());
        }

        public BearerTokenAliasExampleDeserializer(
                Converter<Object, BearerTokenAliasExample> converter,
                JavaType delegateType,
                JsonDeserializer delegateDeserializer) {
            super(converter, delegateType, delegateDeserializer);
        }

        @Override
        public BearerTokenAliasExampleDeserializer withDelegate(
                Converter<Object, BearerTokenAliasExample> converter,
                JavaType delegateType,
                JsonDeserializer<?> delegateDeserializer) {
            return new BearerTokenAliasExampleDeserializer(
                    converter, delegateType, delegateDeserializer);
        }

        @Override
        public BearerTokenAliasExample getNullValue(DeserializationContext context)
                throws JsonMappingException {
            // delegating deserializer does the right thing except for null values, which
            // short-circuit
            // since we may alias things that can deserialize from null, got to ask the delegate how
            // it handles nulls.
            return _converter.convert(_delegateDeserializer.getNullValue(context));
        }
    }
}
