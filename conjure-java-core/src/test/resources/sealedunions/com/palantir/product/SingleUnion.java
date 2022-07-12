package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.Collections;

@JsonSubTypes(@JsonSubTypes.Type(FooWrapper.class))
@JsonIgnoreProperties(ignoreUnknown = true)
public sealed interface SingleUnion {
    static SingleUnion foo(@Safe String value) {
        return new SingleUnion.FooWrapper(value);
    }

    static SingleUnion unknown(@Safe String type, Object value) {
        switch (Preconditions.checkNotNull(type, "Type is required")) {
            case "foo":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: foo");
            default:
                return new SingleUnion(new SingleUnion.UnknownWrapper(type, Collections.singletonMap(type, value)));
        }
    }

    <T> void accept(Visitor<T> visitor);

    interface Visitor<T> {
        T visitFoo(@Safe String value);

        T visitUnknown(@Safe String unknownType, Object unknownValue);

        static <T> SingleUnion.FooStageVisitorBuilder<T> builder() {
            return new SingleUnion.VisitorBuilder<T>();
        }
    }
}
