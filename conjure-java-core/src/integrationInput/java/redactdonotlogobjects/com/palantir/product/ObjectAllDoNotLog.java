package redactdonotlogobjects.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.errorprone.annotations.CheckReturnValue;
import com.palantir.logsafe.DoNotLog;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@DoNotLog
@JsonDeserialize(builder = ObjectAllDoNotLog.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class ObjectAllDoNotLog {
    private final String secret;

    private final String token;

    private int memoizedHashCode;

    private ObjectAllDoNotLog(String secret, String token) {
        validateFields(secret, token);
        this.secret = secret;
        this.token = token;
    }

    @JsonProperty("secret")
    @DoNotLog
    public String getSecret() {
        return this.secret;
    }

    @JsonProperty("token")
    @DoNotLog
    public String getToken() {
        return this.token;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof ObjectAllDoNotLog && equalTo((ObjectAllDoNotLog) other));
    }

    private boolean equalTo(ObjectAllDoNotLog other) {
        if (this.memoizedHashCode != 0
                && other.memoizedHashCode != 0
                && this.memoizedHashCode != other.memoizedHashCode) {
            return false;
        }
        return this.secret.equals(other.secret) && this.token.equals(other.token);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            int hash = 1;
            hash = 31 * hash + this.secret.hashCode();
            hash = 31 * hash + this.token.hashCode();
            result = hash;
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    @DoNotLog
    public String toString() {
        return "ObjectAllDoNotLog{secret: REDACTED" + ", token: REDACTED" + '}';
    }

    @DoNotLog
    public String dangerousToString() {
        return "ObjectAllDoNotLog{secret: " + secret + ", token: " + token + '}';
    }

    public static ObjectAllDoNotLog of(@DoNotLog String secret, @DoNotLog String token) {
        return builder().secret(secret).token(token).build();
    }

    private static void validateFields(String secret, String token) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, secret, "secret");
        missingFields = addFieldIfMissing(missingFields, token, "token");
        if (missingFields != null) {
            throw new SafeIllegalArgumentException(
                    "Some required fields have not been set", SafeArg.of("missingFields", missingFields));
        }
    }

    private static List<String> addFieldIfMissing(List<String> prev, Object fieldValue, String fieldName) {
        List<String> missingFields = prev;
        if (fieldValue == null) {
            if (missingFields == null) {
                missingFields = new ArrayList<>(2);
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

        private @DoNotLog String secret;

        private @DoNotLog String token;

        private Builder() {}

        public Builder from(ObjectAllDoNotLog other) {
            checkNotBuilt();
            secret(other.getSecret());
            token(other.getToken());
            return this;
        }

        @JsonSetter("secret")
        public Builder secret(@Nonnull @DoNotLog String secret) {
            checkNotBuilt();
            this.secret = Preconditions.checkNotNull(secret, "secret cannot be null");
            return this;
        }

        @JsonSetter("token")
        public Builder token(@Nonnull @DoNotLog String token) {
            checkNotBuilt();
            this.token = Preconditions.checkNotNull(token, "token cannot be null");
            return this;
        }

        @CheckReturnValue
        public ObjectAllDoNotLog build() {
            checkNotBuilt();
            this._buildInvoked = true;
            return new ObjectAllDoNotLog(secret, token);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
