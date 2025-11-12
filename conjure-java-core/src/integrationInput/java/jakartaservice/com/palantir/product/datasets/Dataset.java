package jakartaservice.com.palantir.product.datasets;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

@JsonDeserialize(builder = Dataset.Builder.class)
@ConjureGenerated("com.palantir.conjure.java.types.BeanGenerator")
public final class Dataset {
    private final String fileSystemId;

    private final ResourceIdentifier rid;

    private int memoizedHashCode;

    private Dataset(String fileSystemId, ResourceIdentifier rid) {
        validateFields(fileSystemId, rid);
        this.fileSystemId = fileSystemId;
        this.rid = rid;
    }

    @JsonProperty("fileSystemId")
    public String getFileSystemId() {
        return this.fileSystemId;
    }

    /** Uniquely identifies this dataset. */
    @JsonProperty("rid")
    public ResourceIdentifier getRid() {
        return this.rid;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof Dataset && equalTo((Dataset) other));
    }

    private boolean equalTo(Dataset other) {
        if (this.memoizedHashCode != 0
                && other.memoizedHashCode != 0
                && this.memoizedHashCode != other.memoizedHashCode) {
            return false;
        }
        return this.fileSystemId.equals(other.fileSystemId) && this.rid.equals(other.rid);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            int hash = 1;
            hash = 31 * hash + this.fileSystemId.hashCode();
            hash = 31 * hash + this.rid.hashCode();
            result = hash;
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    public String toString() {
        return "Dataset{fileSystemId: " + fileSystemId + ", rid: " + rid + '}';
    }

    public static Dataset of(String fileSystemId, ResourceIdentifier rid) {
        return builder().fileSystemId(fileSystemId).rid(rid).build();
    }

    private static void validateFields(String fileSystemId, ResourceIdentifier rid) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, fileSystemId, "fileSystemId");
        missingFields = addFieldIfMissing(missingFields, rid, "rid");
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

    @ConjureGenerated("com.palantir.conjure.java.types.BeanBuilderGenerator")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Builder {
        boolean _buildInvoked;

        private String fileSystemId;

        private ResourceIdentifier rid;

        private Builder() {}

        public Builder from(Dataset other) {
            checkNotBuilt();
            fileSystemId(other.getFileSystemId());
            rid(other.getRid());
            return this;
        }

        @JsonSetter("fileSystemId")
        public Builder fileSystemId(@Nonnull String fileSystemId) {
            checkNotBuilt();
            this.fileSystemId = Preconditions.checkNotNull(fileSystemId, "fileSystemId cannot be null");
            return this;
        }

        /** Uniquely identifies this dataset. */
        @JsonSetter("rid")
        public Builder rid(@Nonnull ResourceIdentifier rid) {
            checkNotBuilt();
            this.rid = Preconditions.checkNotNull(rid, "rid cannot be null");
            return this;
        }

        @CheckReturnValue
        public Dataset build() {
            checkNotBuilt();
            this._buildInvoked = true;
            return new Dataset(fileSystemId, rid);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
