package servicevanilla.com.palantir.product.datasets;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.errorprone.annotations.CheckReturnValue;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@JsonDeserialize(builder = BackingFileSystem.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class BackingFileSystem {
    private final String fileSystemId;

    private final String baseUri;

    private final Map<String, String> configuration;

    private int memoizedHashCode;

    private BackingFileSystem(String fileSystemId, String baseUri, Map<String, String> configuration) {
        validateFields(fileSystemId, baseUri, configuration);
        this.fileSystemId = fileSystemId;
        this.baseUri = baseUri;
        this.configuration = Collections.unmodifiableMap(configuration);
    }

    /** The name by which this file system is identified. */
    @JsonProperty("fileSystemId")
    public String getFileSystemId() {
        return this.fileSystemId;
    }

    @JsonProperty("baseUri")
    public String getBaseUri() {
        return this.baseUri;
    }

    @JsonProperty("configuration")
    public Map<String, String> getConfiguration() {
        return this.configuration;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof BackingFileSystem && equalTo((BackingFileSystem) other));
    }

    private boolean equalTo(BackingFileSystem other) {
        if (this.memoizedHashCode != 0
                && other.memoizedHashCode != 0
                && this.memoizedHashCode != other.memoizedHashCode) {
            return false;
        }
        return this.fileSystemId.equals(other.fileSystemId)
                && this.baseUri.equals(other.baseUri)
                && this.configuration.equals(other.configuration);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            int hash = 1;
            hash = 31 * hash + this.fileSystemId.hashCode();
            hash = 31 * hash + this.baseUri.hashCode();
            hash = 31 * hash + this.configuration.hashCode();
            result = hash;
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    public String toString() {
        return "BackingFileSystem{fileSystemId: " + fileSystemId + ", baseUri: " + baseUri + ", configuration: "
                + configuration + '}';
    }

    public static BackingFileSystem of(String fileSystemId, String baseUri, Map<String, String> configuration) {
        return builder()
                .fileSystemId(fileSystemId)
                .baseUri(baseUri)
                .configuration(configuration)
                .build();
    }

    private static void validateFields(String fileSystemId, String baseUri, Map<String, String> configuration) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, fileSystemId, "fileSystemId");
        missingFields = addFieldIfMissing(missingFields, baseUri, "baseUri");
        missingFields = addFieldIfMissing(missingFields, configuration, "configuration");
        if (missingFields != null) {
            throw new SafeIllegalArgumentException(
                    "Some required fields have not been set", SafeArg.of("missingFields", missingFields));
        }
    }

    private static List<String> addFieldIfMissing(List<String> prev, Object fieldValue, String fieldName) {
        List<String> missingFields = prev;
        if (fieldValue == null) {
            if (missingFields == null) {
                missingFields = new ArrayList<>(3);
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

        private String fileSystemId;

        private String baseUri;

        @JsonSetter(value = "configuration", nulls = Nulls.SKIP)
        private Map<String, String> configuration = new LinkedHashMap<>();

        private Builder() {}

        public Builder from(BackingFileSystem other) {
            checkNotBuilt();
            fileSystemId(other.getFileSystemId());
            baseUri(other.getBaseUri());
            configuration(other.getConfiguration());
            return this;
        }

        /** The name by which this file system is identified. */
        @JsonSetter("fileSystemId")
        public Builder fileSystemId(@Nonnull String fileSystemId) {
            checkNotBuilt();
            this.fileSystemId = Preconditions.checkNotNull(fileSystemId, "fileSystemId cannot be null");
            return this;
        }

        @JsonSetter("baseUri")
        public Builder baseUri(@Nonnull String baseUri) {
            checkNotBuilt();
            this.baseUri = Preconditions.checkNotNull(baseUri, "baseUri cannot be null");
            return this;
        }

        public Builder configuration(@Nonnull Map<String, String> configuration) {
            checkNotBuilt();
            this.configuration =
                    new LinkedHashMap<>(Preconditions.checkNotNull(configuration, "configuration cannot be null"));
            return this;
        }

        public Builder putAllConfiguration(@Nonnull Map<String, String> configuration) {
            checkNotBuilt();
            this.configuration.putAll(Preconditions.checkNotNull(configuration, "configuration cannot be null"));
            return this;
        }

        public Builder configuration(String key, String value) {
            checkNotBuilt();
            this.configuration.put(key, value);
            return this;
        }

        @CheckReturnValue
        public BackingFileSystem build() {
            checkNotBuilt();
            this._buildInvoked = true;
            return new BackingFileSystem(fileSystemId, baseUri, configuration);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
