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

@JsonDeserialize(builder = AnyExample.DefaultBuilder.class)
@ConjureGenerated("com.palantir.conjure.java.types.BeanGenerator")
public final class AnyExample {
    private final Object any;

    private AnyExample(Object any) {
        validateFields(any);
        this.any = any;
    }

    @JsonProperty("any")
    public Object getAny() {
        return this.any;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof AnyExample && equalTo((AnyExample) other));
    }

    private boolean equalTo(AnyExample other) {
        return this.any.equals(other.any);
    }

    @Override
    public int hashCode() {
        return this.any.hashCode();
    }

    @Override
    public String toString() {
        return "AnyExample{any: " + any + '}';
    }

    private static void validateFields(Object any) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, any, "any");
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

    public static AnyStageBuilder builder() {
        return new DefaultBuilder();
    }

    public interface AnyStageBuilder {
        Completed_StageBuilder any(@Nonnull Object any);

        Builder from(AnyExample other);
    }

    public interface Completed_StageBuilder {
        @CheckReturnValue
        AnyExample build();
    }

    public interface Builder extends AnyStageBuilder, Completed_StageBuilder {
        @Override
        Builder any(@Nonnull Object any);

        @Override
        Builder from(AnyExample other);

        @CheckReturnValue
        @Override
        AnyExample build();
    }

    @ConjureGenerated("com.palantir.conjure.java.types.BeanBuilderGenerator")
    static final class DefaultBuilder implements Builder {
        boolean _buildInvoked;

        private Object any;

        private DefaultBuilder() {}

        @Override
        public Builder from(AnyExample other) {
            checkNotBuilt();
            any(other.getAny());
            return this;
        }

        @Override
        @JsonSetter("any")
        public Builder any(@Nonnull Object any) {
            checkNotBuilt();
            this.any = Preconditions.checkNotNull(any, "any cannot be null");
            return this;
        }

        @Override
        @CheckReturnValue
        public AnyExample build() {
            checkNotBuilt();
            this._buildInvoked = true;
            return new AnyExample(any);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
