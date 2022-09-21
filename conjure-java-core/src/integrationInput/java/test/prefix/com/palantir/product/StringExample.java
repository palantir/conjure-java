package test.prefix.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@JsonDeserialize(builder = StringExample.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class StringExample {
    private final String string;

    private StringExample(String string) {
        validateFields(string);
        this.string = string;
    }

    @JsonProperty("string")
    public String getString() {
        return this.string;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof StringExample && equalTo((StringExample) other));
    }

    private boolean equalTo(StringExample other) {
        return this.string.equals(other.string);
    }

    @Override
    public int hashCode() {
        return this.string.hashCode();
    }

    @Override
    public String toString() {
        return "StringExample{string: " + string + '}';
    }

    public static StringExample of(String string) {
        return builder().string(string).build();
    }

    private static void validateFields(String string) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, string, "string");
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

    public static Builder builder() {
        return new Builder();
    }

    @Generated("com.palantir.conjure.java.types.BeanBuilderGenerator")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Builder {
        boolean _buildInvoked;

        private String string;

        private Builder() {}

        public Builder from(StringExample other) {
            checkNotBuilt();
            string(other.getString());
            return this;
        }

        @JsonSetter("string")
        public Builder string(@Nonnull String string) {
            checkNotBuilt();
            this.string = Preconditions.checkNotNull(string, "string cannot be null");
            return this;
        }

        public StringExample build() {
            checkNotBuilt();
            this._buildInvoked = true;
            return new StringExample(string);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
