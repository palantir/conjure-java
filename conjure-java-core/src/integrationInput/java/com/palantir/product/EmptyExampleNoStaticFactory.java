package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.palantir.logsafe.Safe;
import javax.annotation.processing.Generated;

@Safe
@JsonSerialize
@JsonIgnoreProperties(ignoreUnknown = true)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class EmptyExampleNoStaticFactory {
    private static final EmptyExampleNoStaticFactory INSTANCE = new EmptyExampleNoStaticFactory();

    private EmptyExampleNoStaticFactory() {}

    @Override
    @Safe
    public String toString() {
        return "EmptyExampleNoStaticFactory{}";
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static EmptyExampleNoStaticFactory of() {
        return INSTANCE;
    }
}
