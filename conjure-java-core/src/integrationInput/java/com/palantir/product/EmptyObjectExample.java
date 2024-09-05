package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.palantir.logsafe.Safe;
import javax.annotation.processing.Generated;

@Safe
@JsonSerialize
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class EmptyObjectExample {
    private static final EmptyObjectExample INSTANCE = new EmptyObjectExample();

    private EmptyObjectExample() {}

    @Override
    @Safe
    public String toString() {
        return "EmptyObjectExample{}";
    }

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public static EmptyObjectExample of() {
        return INSTANCE;
    }
}
