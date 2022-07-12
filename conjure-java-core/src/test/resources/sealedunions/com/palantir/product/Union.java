package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.Collections;

@JsonSubTypes({
    @JsonSubTypes.Type(FooWrapper.class),
    @JsonSubTypes.Type(BarWrapper.class),
    @JsonSubTypes.Type(BazWrapper.class)
})
@JsonIgnoreProperties(ignoreUnknown = true)
public sealed interface Union {
    static Union foo(String value) {
        return new Union(new Union.FooWrapper(value));
    }

    /**
     * @deprecated Int is deprecated.
     */
    @Deprecated
    static Union bar(int value) {
        return new Union(new Union.BarWrapper(value));
    }

    /**
     * 64-bit integer.
     * @deprecated Prefer <code>foo</code>.
     */
    @Deprecated
    static Union baz(long value) {
        return new Union(new Union.BazWrapper(value));
    }

    static Union unknown(@Safe String type, Object value) {
        switch (Preconditions.checkNotNull(type, "Type is required")) {
            case "foo":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: foo");
            case "bar":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: bar");
            case "baz":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: baz");
            default:
                return new Union(new Union.UnknownWrapper(type, Collections.singletonMap(type, value)));
        }
    }

    interface Visitor<T> {
        T visitFoo(String value);

        /**
         * @deprecated Int is deprecated.
         */
        @Deprecated
        T visitBar(int value);

        /**
         * 64-bit integer.
         * @deprecated Prefer <code>foo</code>.
         */
        @Deprecated
        T visitBaz(long value);

        T visitUnknown(@Safe String unknownType, Object unknownValue);

        static <T> Union.BarStageVisitorBuilder<T> builder() {
            return new Union.VisitorBuilder<T>();
        }
    }
}
