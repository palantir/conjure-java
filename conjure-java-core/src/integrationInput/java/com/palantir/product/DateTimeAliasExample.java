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
import java.time.OffsetDateTime;
import java.util.Objects;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
@JsonDeserialize(using = DateTimeAliasExample.DateTimeAliasExampleDeserializer.class)
public final class DateTimeAliasExample {
    private final OffsetDateTime value;

    private DateTimeAliasExample(OffsetDateTime value) {
        Objects.requireNonNull(value, "value cannot be null");
        this.value = value;
    }

    @JsonValue
    public OffsetDateTime get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof DateTimeAliasExample
                        && this.value.equals(((DateTimeAliasExample) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    public static DateTimeAliasExample valueOf(String value) {
        return new DateTimeAliasExample(OffsetDateTime.parse(value));
    }

    @JsonCreator
    public static DateTimeAliasExample of(OffsetDateTime value) {
        return new DateTimeAliasExample(value);
    }

    public static final class DateTimeAliasExampleConverter
            implements Converter<OffsetDateTime, DateTimeAliasExample> {
        @Override
        public DateTimeAliasExample convert(OffsetDateTime value) {
            return of(value);
        }

        @Override
        public JavaType getInputType(TypeFactory typeFactory) {
            return typeFactory.constructType(new TypeReference<OffsetDateTime>() {});
        }

        @Override
        public JavaType getOutputType(TypeFactory typeFactory) {
            return typeFactory.constructType(DateTimeAliasExample.class);
        }
    }

    public static final class DateTimeAliasExampleDeserializer
            extends StdDelegatingDeserializer<DateTimeAliasExample> {
        public DateTimeAliasExampleDeserializer() {
            super(new DateTimeAliasExampleConverter());
        }

        public DateTimeAliasExampleDeserializer(
                Converter<Object, DateTimeAliasExample> converter,
                JavaType delegateType,
                JsonDeserializer delegateDeserializer) {
            super(converter, delegateType, delegateDeserializer);
        }

        @Override
        public DateTimeAliasExampleDeserializer withDelegate(
                Converter<Object, DateTimeAliasExample> converter,
                JavaType delegateType,
                JsonDeserializer<?> delegateDeserializer) {
            return new DateTimeAliasExampleDeserializer(
                    converter, delegateType, delegateDeserializer);
        }

        @Override
        public DateTimeAliasExample getNullValue(DeserializationContext context)
                throws JsonMappingException {
            // delegating deserializer does the right thing except for null values, which
            // short-circuit
            // since we may alias things that can deserialize from null, got to ask the delegate how
            // it handles nulls.
            return _converter.convert(_delegateDeserializer.getNullValue(context));
        }
    }
}
