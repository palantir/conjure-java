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
import javax.annotation.processing.Generated;

@JsonDeserialize(builder = RiskyNames.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class RiskyNames {
    private final String var;

    private final String class_;

    private final String int_;

    private final String len;

    private int memoizedHashCode;

    private RiskyNames(String var, String class_, String int_, String len) {
        validateFields(var, class_, int_, len);
        this.var = var;
        this.class_ = class_;
        this.int_ = int_;
        this.len = len;
    }

    @JsonProperty("var")
    public String getVar() {
        return this.var;
    }

    @JsonProperty("class")
    public String getClass_() {
        return this.class_;
    }

    @JsonProperty("int")
    public String getInt() {
        return this.int_;
    }

    @JsonProperty("len")
    public String getLen() {
        return this.len;
    }

    @Override
    public boolean equals(Object other) {
        return this == other || (other instanceof RiskyNames && equalTo((RiskyNames) other));
    }

    private boolean equalTo(RiskyNames other) {
        if (this.memoizedHashCode != 0
                && other.memoizedHashCode != 0
                && this.memoizedHashCode != other.memoizedHashCode) {
            return false;
        }
        return this.var.equals(other.var)
                && this.class_.equals(other.class_)
                && this.int_.equals(other.int_)
                && this.len.equals(other.len);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            int hash = 1;
            hash = 31 * hash + this.var.hashCode();
            hash = 31 * hash + this.class_.hashCode();
            hash = 31 * hash + this.int_.hashCode();
            hash = 31 * hash + this.len.hashCode();
            result = hash;
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    public String toString() {
        return "RiskyNames{var: " + var + ", class: " + class_ + ", int: " + int_ + ", len: " + len + '}';
    }

    private static void validateFields(String var, String class_, String int_, String len) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, var, "var");
        missingFields = addFieldIfMissing(missingFields, class_, "class");
        missingFields = addFieldIfMissing(missingFields, int_, "int");
        missingFields = addFieldIfMissing(missingFields, len, "len");
        if (missingFields != null) {
            throw new SafeIllegalArgumentException(
                    "Some required fields have not been set", SafeArg.of("missingFields", missingFields));
        }
    }

    private static List<String> addFieldIfMissing(List<String> prev, Object fieldValue, String fieldName) {
        List<String> missingFields = prev;
        if (fieldValue == null) {
            if (missingFields == null) {
                missingFields = new ArrayList<>(4);
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

        private String var;

        private String class_;

        private String int_;

        private String len;

        private Builder() {}

        public Builder from(RiskyNames other) {
            checkNotBuilt();
            var(other.getVar());
            class_(other.getClass_());
            int_(other.getInt());
            len(other.getLen());
            return this;
        }

        @JsonSetter("var")
        public Builder var(@Nonnull String var) {
            checkNotBuilt();
            this.var = Preconditions.checkNotNull(var, "var cannot be null");
            return this;
        }

        @JsonSetter("class")
        public Builder class_(@Nonnull String class_) {
            checkNotBuilt();
            this.class_ = Preconditions.checkNotNull(class_, "class cannot be null");
            return this;
        }

        @JsonSetter("int")
        public Builder int_(@Nonnull String int_) {
            checkNotBuilt();
            this.int_ = Preconditions.checkNotNull(int_, "int cannot be null");
            return this;
        }

        @JsonSetter("len")
        public Builder len(@Nonnull String len) {
            checkNotBuilt();
            this.len = Preconditions.checkNotNull(len, "len cannot be null");
            return this;
        }

        public RiskyNames build() {
            checkNotBuilt();
            this._buildInvoked = true;
            return new RiskyNames(var, class_, int_, len);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
