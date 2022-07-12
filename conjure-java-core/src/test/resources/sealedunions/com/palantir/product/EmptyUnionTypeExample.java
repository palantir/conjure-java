package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
import java.util.Collections;

@JsonSubTypes
@JsonIgnoreProperties(ignoreUnknown = true)
public sealed interface EmptyUnionTypeExample {
    static EmptyUnionTypeExample unknown(@Safe String type, Object value) {
        switch (Preconditions.checkNotNull(type, "Type is required")) {
            default:
                return new EmptyUnionTypeExample(
                        new EmptyUnionTypeExample.UnknownWrapper(type, Collections.singletonMap(type, value)));
        }
    }

    interface Visitor<T> {
        T visitUnknown(@Safe String unknownType, Object unknownValue);

        static <T> EmptyUnionTypeExample.UnknownStageVisitorBuilder<T> builder() {
            return new EmptyUnionTypeExample.VisitorBuilder<T>();
        }
    }
}
