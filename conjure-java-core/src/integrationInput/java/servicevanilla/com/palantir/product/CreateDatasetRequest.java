package servicevanilla.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

@JsonDeserialize(builder = CreateDatasetRequest.Builder.class)
@ConjureGenerated("com.palantir.conjure.java.types.BeanGenerator")
public final class CreateDatasetRequest {
    private final String fileSystemId;

    private final String path;

    private int memoizedHashCode;

    private CreateDatasetRequest(String fileSystemId, String path) {
        validateFields(fileSystemId, path);
        this.fileSystemId = fileSystemId;
        this.path = path;
    }

    @JsonProperty("fileSystemId")
    public String getFileSystemId() {
        return this.fileSystemId;
    }

    @JsonProperty("path")
    public String getPath() {
        return this.path;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof CreateDatasetRequest && equalTo((CreateDatasetRequest) other));
    }

    private boolean equalTo(CreateDatasetRequest other) {
        if (this.memoizedHashCode != 0
                && other.memoizedHashCode != 0
                && this.memoizedHashCode != other.memoizedHashCode) {
            return false;
        }
        return this.fileSystemId.equals(other.fileSystemId) && this.path.equals(other.path);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            int hash = 1;
            hash = 31 * hash + this.fileSystemId.hashCode();
            hash = 31 * hash + this.path.hashCode();
            result = hash;
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    public String toString() {
        return "CreateDatasetRequest{fileSystemId: " + fileSystemId + ", path: " + path + '}';
    }

    public static CreateDatasetRequest of(String fileSystemId, String path) {
        return builder().fileSystemId(fileSystemId).path(path).build();
    }

    private static void validateFields(String fileSystemId, String path) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, fileSystemId, "fileSystemId");
        missingFields = addFieldIfMissing(missingFields, path, "path");
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

        private String path;

        private Builder() {}

        public Builder from(CreateDatasetRequest other) {
            checkNotBuilt();
            fileSystemId(other.getFileSystemId());
            path(other.getPath());
            return this;
        }

        @JsonSetter("fileSystemId")
        public Builder fileSystemId(@Nonnull String fileSystemId) {
            checkNotBuilt();
            this.fileSystemId = Preconditions.checkNotNull(fileSystemId, "fileSystemId cannot be null");
            return this;
        }

        @JsonSetter("path")
        public Builder path(@Nonnull String path) {
            checkNotBuilt();
            this.path = Preconditions.checkNotNull(path, "path cannot be null");
            return this;
        }

        @CheckReturnValue
        public CreateDatasetRequest build() {
            checkNotBuilt();
            this._buildInvoked = true;
            return new CreateDatasetRequest(fileSystemId, path);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
