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
@JsonDeserialize(using = StringAliasExample.StringAliasExampleDeserializer.class)
public final class StringAliasExample {
    private final String value;

    private StringAliasExample(String value) {
        Objects.requireNonNull(value, "value cannot be null");
        this.value = value;
    }

    @JsonValue
    public String get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof StringAliasExample
                        && this.value.equals(((StringAliasExample) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    public static StringAliasExample valueOf(String value) {
        return new StringAliasExample(value);
    }

    @JsonCreator
    public static StringAliasExample of(String value) {
        return new StringAliasExample(value);
    }

    public static final class StringAliasExampleConverter
            implements Converter<String, StringAliasExample> {
        @Override
        public StringAliasExample convert(String value) {
            return of(value);
        }

        @Override
        public JavaType getInputType(TypeFactory typeFactory) {
            return typeFactory.constructType(new TypeReference<String>() {});
        }

        @Override
        public JavaType getOutputType(TypeFactory typeFactory) {
            return typeFactory.constructType(StringAliasExample.class);
        }
    }

    public static final class StringAliasExampleDeserializer
            extends StdDelegatingDeserializer<StringAliasExample> {
        public StringAliasExampleDeserializer() {
            super(new StringAliasExampleConverter());
        }

        public StringAliasExampleDeserializer(
                Converter<Object, StringAliasExample> converter,
                JavaType delegateType,
                JsonDeserializer delegateDeserializer) {
            super(converter, delegateType, delegateDeserializer);
        }

        @Override
        public StringAliasExampleDeserializer withDelegate(
                Converter<Object, StringAliasExample> converter,
                JavaType delegateType,
                JsonDeserializer<?> delegateDeserializer) {
            return new StringAliasExampleDeserializer(
                    converter, delegateType, delegateDeserializer);
        }

        @Override
        public StringAliasExample getNullValue(DeserializationContext context)
                throws JsonMappingException {
            // delegating deserializer does the right thing except for null values, which
            // short-circuit
            // since we may alias things that can deserialize from null, got to ask the delegate how
            // it handles nulls.
            return _converter.convert(_delegateDeserializer.getNullValue(context));
        }
    }
}
