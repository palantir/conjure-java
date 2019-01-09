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
import java.util.UUID;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
@JsonDeserialize(using = UuidAliasExample.UuidAliasExampleDeserializer.class)
public final class UuidAliasExample {
    private final UUID value;

    private UuidAliasExample(UUID value) {
        Objects.requireNonNull(value, "value cannot be null");
        this.value = value;
    }

    @JsonValue
    public UUID get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof UuidAliasExample
                        && this.value.equals(((UuidAliasExample) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    public static UuidAliasExample valueOf(String value) {
        return new UuidAliasExample(UUID.fromString(value));
    }

    @JsonCreator
    public static UuidAliasExample of(UUID value) {
        return new UuidAliasExample(value);
    }

    public static final class UuidAliasExampleConverter
            implements Converter<UUID, UuidAliasExample> {
        @Override
        public UuidAliasExample convert(UUID value) {
            return of(value);
        }

        @Override
        public JavaType getInputType(TypeFactory typeFactory) {
            return typeFactory.constructType(new TypeReference<UUID>() {});
        }

        @Override
        public JavaType getOutputType(TypeFactory typeFactory) {
            return typeFactory.constructType(UuidAliasExample.class);
        }
    }

    public static final class UuidAliasExampleDeserializer
            extends StdDelegatingDeserializer<UuidAliasExample> {
        public UuidAliasExampleDeserializer() {
            super(new UuidAliasExampleConverter());
        }

        public UuidAliasExampleDeserializer(
                Converter<Object, UuidAliasExample> converter,
                JavaType delegateType,
                JsonDeserializer delegateDeserializer) {
            super(converter, delegateType, delegateDeserializer);
        }

        @Override
        public UuidAliasExampleDeserializer withDelegate(
                Converter<Object, UuidAliasExample> converter,
                JavaType delegateType,
                JsonDeserializer<?> delegateDeserializer) {
            return new UuidAliasExampleDeserializer(converter, delegateType, delegateDeserializer);
        }

        @Override
        public UuidAliasExample getNullValue(DeserializationContext context)
                throws JsonMappingException {
            // delegating deserializer does the right thing except for null values, which
            // short-circuit
            // since we may alias things that can deserialize from null, got to ask the delegate how
            // it handles nulls.
            return _converter.convert(_delegateDeserializer.getNullValue(context));
        }
    }
}
