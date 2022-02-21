package com.palantir.strict;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@JsonDeserialize(builder = SingleFieldNotStrict.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class SingleFieldNotStrict {
    private final int foo;

    private SingleFieldNotStrict(int foo) {
        this.foo = foo;
    }

    @JsonProperty("foo")
    public int getFoo() {
        return this.foo;
    }

    @Override
    public boolean equals(Object other) {
        return this == other || (other instanceof SingleFieldNotStrict && equalTo((SingleFieldNotStrict) other));
    }

    private boolean equalTo(SingleFieldNotStrict other) {
        return this.foo == other.foo;
    }

    @Override
    public int hashCode() {
        return this.foo;
    }

    @Override
    public String toString() {
        return "SingleFieldNotStrict{foo: " + foo + '}';
    }

    public static SingleFieldNotStrict of(int foo) {
        return builder().foo(foo).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    @Generated("com.palantir.conjure.java.types.BeanBuilderGenerator")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Builder {
        boolean _buildInvoked;

        private int foo;

        private boolean _fooInitialized = false;

        private Builder() {}

        public Builder from(SingleFieldNotStrict other) {
            checkNotBuilt();
            foo(other.getFoo());
            return this;
        }

        @JsonSetter("foo")
        public Builder foo(int foo) {
            checkNotBuilt();
            this.foo = foo;
            this._fooInitialized = true;
            return this;
        }

        private void validatePrimitiveFieldsHaveBeenInitialized() {
            List<String> missingFields = null;
            missingFields = addFieldIfMissing(missingFields, _fooInitialized, "foo");
            if (missingFields != null) {
                throw new SafeIllegalArgumentException(
                        "Some required fields have not been set", SafeArg.of("missingFields", missingFields));
            }
        }

        private static List<String> addFieldIfMissing(List<String> prev, boolean initialized, String fieldName) {
            List<String> missingFields = prev;
            if (!initialized) {
                if (missingFields == null) {
                    missingFields = new ArrayList<>(1);
                }
                missingFields.add(fieldName);
            }
            return missingFields;
        }

        public SingleFieldNotStrict build() {
            checkNotBuilt();
            this._buildInvoked = true;
            validatePrimitiveFieldsHaveBeenInitialized();
            return new SingleFieldNotStrict(foo);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
