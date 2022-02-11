package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@JsonDeserialize(builder = DateTimeExample.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class DateTimeExample {
    private final OffsetDateTime datetime;

    private int memoizedHashCode;

    private DateTimeExample(OffsetDateTime datetime) {
        validateFields(datetime);
        this.datetime = datetime;
    }

    @JsonProperty("datetime")
    public OffsetDateTime getDatetime() {
        return this.datetime;
    }

    @Override
    public boolean equals(Object other) {
        return this == other || (other instanceof DateTimeExample && equalTo((DateTimeExample) other));
    }

    private boolean equalTo(DateTimeExample other) {
        return this.datetime.isEqual(other.datetime);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            result = this.datetime.toInstant().hashCode();
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    public String toString() {
        return "DateTimeExample{datetime: " + datetime + '}';
    }

    public static DateTimeExample of(OffsetDateTime datetime) {
        return builder().datetime(datetime).build();
    }

    private static void validateFields(OffsetDateTime datetime) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, datetime, "datetime");
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
    public static final class Builder {
        boolean _buildInvoked;

        private OffsetDateTime datetime;

        private Builder() {}

        public Builder from(DateTimeExample other) {
            checkNotBuilt();
            datetime(other.getDatetime());
            return this;
        }

        @JsonSetter("datetime")
        public Builder datetime(@Nonnull OffsetDateTime datetime) {
            checkNotBuilt();
            this.datetime = Preconditions.checkNotNull(datetime, "datetime cannot be null");
            return this;
        }

        public DateTimeExample build() {
            checkNotBuilt();
            this._buildInvoked = true;
            return new DateTimeExample(datetime);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
