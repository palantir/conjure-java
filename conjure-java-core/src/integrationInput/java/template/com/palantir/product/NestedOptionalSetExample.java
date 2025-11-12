package template.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.errorprone.annotations.CheckReturnValue;
import com.palantir.conjure.java.lib.internal.ConjureCollections;
import com.palantir.conjure.java.lib.internal.ConjureGenerated;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@JsonDeserialize(builder = NestedOptionalSetExample.Builder.class)
@ConjureGenerated("com.palantir.conjure.java.types.BeanGenerator")
public final class NestedOptionalSetExample {
    private final Set<Set<Optional<String>>> set;

    private int memoizedHashCode;

    private NestedOptionalSetExample(Set<Set<Optional<String>>> set) {
        validateFields(set);
        this.set = Collections.unmodifiableSet(set);
    }

    @JsonProperty("set")
    public Set<Set<Optional<String>>> getSet() {
        return this.set;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other
                || (other instanceof NestedOptionalSetExample && equalTo((NestedOptionalSetExample) other));
    }

    private boolean equalTo(NestedOptionalSetExample other) {
        if (this.memoizedHashCode != 0
                && other.memoizedHashCode != 0
                && this.memoizedHashCode != other.memoizedHashCode) {
            return false;
        }
        return this.set.equals(other.set);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            result = this.set.hashCode();
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    public String toString() {
        return "NestedOptionalSetExample{set: " + set + '}';
    }

    private static void validateFields(Set<Set<Optional<String>>> set) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, set, "set");
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

    @ConjureGenerated("com.palantir.conjure.java.types.BeanBuilderGenerator")
    public static final class Builder {
        boolean _buildInvoked;

        private Set<Set<Optional<String>>> set = ConjureCollections.newNonNullSet();

        private Builder() {}

        public Builder from(NestedOptionalSetExample other) {
            checkNotBuilt();
            set(other.getSet());
            return this;
        }

        @JsonSetter(value = "set", nulls = Nulls.SKIP, contentNulls = Nulls.AS_EMPTY)
        public Builder set(@Nonnull Iterable<? extends Set<Optional<String>>> set) {
            checkNotBuilt();
            this.set = ConjureCollections.newNonNullSet(Preconditions.checkNotNull(set, "set cannot be null"));
            return this;
        }

        public Builder addAllSet(@Nonnull Iterable<? extends Set<Optional<String>>> set) {
            checkNotBuilt();
            ConjureCollections.addAllAndCheckNonNull(this.set, Preconditions.checkNotNull(set, "set cannot be null"));
            return this;
        }

        public Builder set(Set<Optional<String>> set) {
            checkNotBuilt();
            Preconditions.checkNotNull(set, "set cannot be null");
            this.set.add(set);
            return this;
        }

        @CheckReturnValue
        public NestedOptionalSetExample build() {
            checkNotBuilt();
            this._buildInvoked = true;
            return new NestedOptionalSetExample(set);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
