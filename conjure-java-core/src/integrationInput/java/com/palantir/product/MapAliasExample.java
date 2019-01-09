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
import java.util.Map;
import java.util.Objects;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
@JsonDeserialize(using = MapAliasExample.MapAliasExampleDeserializer.class)
public final class MapAliasExample {
    private final Map<String, Object> value;

    private MapAliasExample(Map<String, Object> value) {
        Objects.requireNonNull(value, "value cannot be null");
        this.value = value;
    }

    @JsonValue
    public Map<String, Object> get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof MapAliasExample
                        && this.value.equals(((MapAliasExample) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @JsonCreator
    public static MapAliasExample of(Map<String, Object> value) {
        return new MapAliasExample(value);
    }

    public static final class MapAliasExampleConverter
            implements Converter<Map<String, Object>, MapAliasExample> {
        @Override
        public MapAliasExample convert(Map<String, Object> value) {
            return of(value);
        }

        @Override
        public JavaType getInputType(TypeFactory typeFactory) {
            return typeFactory.constructType(new TypeReference<Map<String, Object>>() {});
        }

        @Override
        public JavaType getOutputType(TypeFactory typeFactory) {
            return typeFactory.constructType(MapAliasExample.class);
        }
    }

    public static final class MapAliasExampleDeserializer
            extends StdDelegatingDeserializer<MapAliasExample> {
        public MapAliasExampleDeserializer() {
            super(new MapAliasExampleConverter());
        }

        public MapAliasExampleDeserializer(
                Converter<Object, MapAliasExample> converter,
                JavaType delegateType,
                JsonDeserializer delegateDeserializer) {
            super(converter, delegateType, delegateDeserializer);
        }

        @Override
        public MapAliasExampleDeserializer withDelegate(
                Converter<Object, MapAliasExample> converter,
                JavaType delegateType,
                JsonDeserializer<?> delegateDeserializer) {
            return new MapAliasExampleDeserializer(converter, delegateType, delegateDeserializer);
        }

        @Override
        public MapAliasExample getNullValue(DeserializationContext context)
                throws JsonMappingException {
            // delegating deserializer does the right thing except for null values, which
            // short-circuit
            // since we may alias things that can deserialize from null, got to ask the delegate how
            // it handles nulls.
            return _converter.convert(_delegateDeserializer.getNullValue(context));
        }
    }
}
