package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
import java.util.Collections;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true,
        defaultImpl = EmptyUnionTypeExample.class)
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

    <T> void accept(Visitor<T> visitor);

    interface Visitor<T> {
        T visitUnknown(@Safe String unknownType, Object unknownValue);

        static <T> EmptyUnionTypeExample.UnknownStageVisitorBuilder<T> builder() {
            return new EmptyUnionTypeExample.VisitorBuilder<T>();
        }
    }
}
