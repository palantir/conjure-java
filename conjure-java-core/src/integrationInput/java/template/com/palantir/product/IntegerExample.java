package template.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.errorprone.annotations.CheckReturnValue;
import com.palantir.conjure.java.lib.internal.ConjureGenerated;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@JsonDeserialize(builder = IntegerExample.DefaultBuilder.class)
@ConjureGenerated("com.palantir.conjure.java.types.BeanGenerator")
public final class IntegerExample {
    private final int integer;

    private IntegerExample(int integer) {
        this.integer = integer;
    }

    @JsonProperty("integer")
    public int getInteger() {
        return this.integer;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof IntegerExample && equalTo((IntegerExample) other));
    }

    private boolean equalTo(IntegerExample other) {
        return this.integer == other.integer;
    }

    @Override
    public int hashCode() {
        return this.integer;
    }

    @Override
    public String toString() {
        return "IntegerExample{integer: " + integer + '}';
    }

    public static IntegerStageBuilder builder() {
        return new DefaultBuilder();
    }

    public interface IntegerStageBuilder {
        Completed_StageBuilder integer(@Nonnull int integer);

        Builder from(IntegerExample other);
    }

    public interface Completed_StageBuilder {
        @CheckReturnValue
        IntegerExample build();
    }

    public interface Builder extends IntegerStageBuilder, Completed_StageBuilder {
        @Override
        Builder integer(@Nonnull int integer);

        @Override
        Builder from(IntegerExample other);

        @CheckReturnValue
        @Override
        IntegerExample build();
    }

    @ConjureGenerated("com.palantir.conjure.java.types.BeanBuilderGenerator")
    static final class DefaultBuilder implements Builder {
        boolean _buildInvoked;

        private int integer;

        private boolean _integerInitialized = false;

        private DefaultBuilder() {}

        @Override
        public Builder from(IntegerExample other) {
            checkNotBuilt();
            integer(other.getInteger());
            return this;
        }

        @Override
        @JsonSetter("integer")
        public Builder integer(int integer) {
            checkNotBuilt();
            this.integer = integer;
            this._integerInitialized = true;
            return this;
        }

        private void validatePrimitiveFieldsHaveBeenInitialized() {
            List<String> missingFields = null;
            missingFields = addFieldIfMissing(missingFields, _integerInitialized, "integer");
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

        @Override
        @CheckReturnValue
        public IntegerExample build() {
            checkNotBuilt();
            this._buildInvoked = true;
            validatePrimitiveFieldsHaveBeenInitialized();
            return new IntegerExample(integer);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
