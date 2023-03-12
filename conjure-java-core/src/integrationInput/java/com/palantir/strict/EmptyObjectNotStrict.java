package com.palantir.strict;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.errorprone.annotations.Immutable;
import com.palantir.logsafe.Safe;
import javax.annotation.processing.Generated;

@Safe
@Immutable
@JsonSerialize
@JsonIgnoreProperties(ignoreUnknown = true)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class EmptyObjectNotStrict {
    private static final EmptyObjectNotStrict INSTANCE = new EmptyObjectNotStrict();

    private EmptyObjectNotStrict() {}

    @Override
    @Safe
    public String toString() {
        return "EmptyObjectNotStrict{}";
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static EmptyObjectNotStrict of() {
        return INSTANCE;
    }
}
