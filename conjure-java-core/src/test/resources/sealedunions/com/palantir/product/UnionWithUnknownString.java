package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.Collections;

@JsonSubTypes(@JsonSubTypes.Type(Unknown_Wrapper.class))
@JsonIgnoreProperties(ignoreUnknown = true)
public sealed interface UnionWithUnknownString {
    static UnionWithUnknownString unknown_(String value) {
        return new UnionWithUnknownString.Unknown_Wrapper(value);
    }

    static UnionWithUnknownString unknown(@Safe String type, Object value) {
        switch (Preconditions.checkNotNull(type, "Type is required")) {
            case "unknown":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: unknown");
            default:
                return new UnionWithUnknownString(
                        new UnionWithUnknownString.UnknownWrapper(type, Collections.singletonMap(type, value)));
        }
    }

    <T> void accept(Visitor<T> visitor);

    interface Visitor<T> {
        T visitUnknown_(String value);

        T visitUnknown(@Safe String unknownType, Object unknownValue);

        static <T> UnionWithUnknownString.Unknown_StageVisitorBuilder<T> builder() {
            return new UnionWithUnknownString.VisitorBuilder<T>();
        }
    }
}
