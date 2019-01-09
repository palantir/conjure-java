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
import com.palantir.conjure.java.lib.SafeLong;
import java.util.Objects;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
@JsonDeserialize(using = SafeLongAliasExample.SafeLongAliasExampleDeserializer.class)
public final class SafeLongAliasExample {
    private final SafeLong value;

    private SafeLongAliasExample(SafeLong value) {
        Objects.requireNonNull(value, "value cannot be null");
        this.value = value;
    }

    @JsonValue
    public SafeLong get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof SafeLongAliasExample
                        && this.value.equals(((SafeLongAliasExample) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    public static SafeLongAliasExample valueOf(String value) {
        return new SafeLongAliasExample(SafeLong.valueOf(value));
    }

    @JsonCreator
    public static SafeLongAliasExample of(SafeLong value) {
        return new SafeLongAliasExample(value);
    }

    public static final class SafeLongAliasExampleConverter
            implements Converter<SafeLong, SafeLongAliasExample> {
        @Override
        public SafeLongAliasExample convert(SafeLong value) {
            return of(value);
        }

        @Override
        public JavaType getInputType(TypeFactory typeFactory) {
            return typeFactory.constructType(new TypeReference<SafeLong>() {});
        }

        @Override
        public JavaType getOutputType(TypeFactory typeFactory) {
            return typeFactory.constructType(SafeLongAliasExample.class);
        }
    }

    public static final class SafeLongAliasExampleDeserializer
            extends StdDelegatingDeserializer<SafeLongAliasExample> {
        public SafeLongAliasExampleDeserializer() {
            super(new SafeLongAliasExampleConverter());
        }

        public SafeLongAliasExampleDeserializer(
                Converter<Object, SafeLongAliasExample> converter,
                JavaType delegateType,
                JsonDeserializer delegateDeserializer) {
            super(converter, delegateType, delegateDeserializer);
        }

        @Override
        public SafeLongAliasExampleDeserializer withDelegate(
                Converter<Object, SafeLongAliasExample> converter,
                JavaType delegateType,
                JsonDeserializer<?> delegateDeserializer) {
            return new SafeLongAliasExampleDeserializer(
                    converter, delegateType, delegateDeserializer);
        }

        @Override
        public SafeLongAliasExample getNullValue(DeserializationContext context)
                throws JsonMappingException {
            // delegating deserializer does the right thing except for null values, which
            // short-circuit
            // since we may alias things that can deserialize from null, got to ask the delegate how
            // it handles nulls.
            return _converter.convert(_delegateDeserializer.getNullValue(context));
        }
    }
}
