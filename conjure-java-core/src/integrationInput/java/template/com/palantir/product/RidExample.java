package template.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.errorprone.annotations.CheckReturnValue;
import com.palantir.conjure.java.lib.internal.ConjureGenerated;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import com.palantir.ri.ResourceIdentifier;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@JsonDeserialize(builder = RidExample.DefaultBuilder.class)
@ConjureGenerated("com.palantir.conjure.java.types.BeanGenerator")
public final class RidExample {
    private final ResourceIdentifier ridValue;

    private RidExample(ResourceIdentifier ridValue) {
        validateFields(ridValue);
        this.ridValue = ridValue;
    }

    @JsonProperty("ridValue")
    public ResourceIdentifier getRidValue() {
        return this.ridValue;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof RidExample && equalTo((RidExample) other));
    }

    private boolean equalTo(RidExample other) {
        return this.ridValue.equals(other.ridValue);
    }

    @Override
    public int hashCode() {
        return this.ridValue.hashCode();
    }

    @Override
    public String toString() {
        return "RidExample{ridValue: " + ridValue + '}';
    }

    private static void validateFields(ResourceIdentifier ridValue) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, ridValue, "ridValue");
        if (missingFields != null) {
            throw new SafeIllegalArgumentException(
                    "Some required fields have not been set", SafeArg.of("missingFields", missingFields));
        }
    }

    private static List<String> addFieldIfMissing(List<String> prev, Object fieldValue, String fieldName) {
        List<String> missingFields = prev;
        if (fieldValue == null) {
            if (missingFields == null) {
                missingFields = new ArrayList<>(1);
            }
            missingFields.add(fieldName);
        }
        return missingFields;
    }

    public static RidValueStageBuilder builder() {
        return new DefaultBuilder();
    }

    public interface RidValueStageBuilder {
        Completed_StageBuilder ridValue(@Nonnull ResourceIdentifier ridValue);

        Builder from(RidExample other);
    }

    public interface Completed_StageBuilder {
        @CheckReturnValue
        RidExample build();
    }

    public interface Builder extends RidValueStageBuilder, Completed_StageBuilder {
        @Override
        Builder ridValue(@Nonnull ResourceIdentifier ridValue);

        @Override
        Builder from(RidExample other);

        @CheckReturnValue
        @Override
        RidExample build();
    }

    @ConjureGenerated("com.palantir.conjure.java.types.BeanBuilderGenerator")
    static final class DefaultBuilder implements Builder {
        boolean _buildInvoked;

        private ResourceIdentifier ridValue;

        private DefaultBuilder() {}

        @Override
        public Builder from(RidExample other) {
            checkNotBuilt();
            ridValue(other.getRidValue());
            return this;
        }

        @Override
        @JsonSetter("ridValue")
        public Builder ridValue(@Nonnull ResourceIdentifier ridValue) {
            checkNotBuilt();
            this.ridValue = Preconditions.checkNotNull(ridValue, "ridValue cannot be null");
            return this;
        }

        @Override
        @CheckReturnValue
        public RidExample build() {
            checkNotBuilt();
            this._buildInvoked = true;
            return new RidExample(ridValue);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
