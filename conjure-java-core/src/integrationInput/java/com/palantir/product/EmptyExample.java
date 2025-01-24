package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.palantir.logsafe.Safe;
import javax.annotation.processing.Generated;

/** There are no fields in this type. A static factory method (<code>of</code>) should be generated. */
@Safe
@JsonSerialize
@JsonIgnoreProperties(ignoreUnknown = true)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class EmptyExample {
    private static final EmptyExample INSTANCE = new EmptyExample();

    private EmptyExample() {}

    @Override
    @Safe
    public String toString() {
        return "EmptyExample{}";
    }

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public static EmptyExample of() {
        return INSTANCE;
    }
}
