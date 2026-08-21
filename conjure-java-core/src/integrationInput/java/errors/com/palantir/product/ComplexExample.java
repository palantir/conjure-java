package errors.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.errorprone.annotations.CheckReturnValue;
import com.palantir.conjure.java.lib.internal.ConjureCollections;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@JsonDeserialize(builder = ComplexExample.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class ComplexExample {
    private final Map<StringAliasEx, Optional<List<ObjectReference>>> metadata;

    private final EnumExample status;

    private final List<UnionExample> variants;

    private final Optional<Long> external;

    private int memoizedHashCode;

    private ComplexExample(
            Map<StringAliasEx, Optional<List<ObjectReference>>> metadata,
            EnumExample status,
            List<UnionExample> variants,
            Optional<Long> external) {
        validateFields(metadata, status, variants, external);
        this.metadata = Collections.unmodifiableMap(metadata);
        this.status = status;
        this.variants = ConjureCollections.unmodifiableList(variants);
        this.external = external;
    }

    @JsonProperty("metadata")
    public Map<StringAliasEx, Optional<List<ObjectReference>>> getMetadata() {
        return this.metadata;
    }

    @JsonProperty("status")
    public EnumExample getStatus() {
        return this.status;
    }

    @JsonProperty("variants")
    public List<UnionExample> getVariants() {
        return this.variants;
    }

    @JsonProperty("external")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    public Optional<Long> getExternal() {
        return this.external;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof ComplexExample && equalTo((ComplexExample) other));
    }

    private boolean equalTo(ComplexExample other) {
        if (this.memoizedHashCode != 0
                && other.memoizedHashCode != 0
                && this.memoizedHashCode != other.memoizedHashCode) {
            return false;
        }
        return this.metadata.equals(other.metadata)
                && this.status.equals(other.status)
                && this.variants.equals(other.variants)
                && this.external.equals(other.external);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            int hash = 1;
            hash = 31 * hash + this.metadata.hashCode();
            hash = 31 * hash + this.status.hashCode();
            hash = 31 * hash + this.variants.hashCode();
            hash = 31 * hash + this.external.hashCode();
            result = hash;
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    public String toString() {
        return "ComplexExample{metadata: " + metadata + ", status: " + status + ", variants: " + variants
                + ", external: " + external + '}';
    }

    private static void validateFields(
            Map<StringAliasEx, Optional<List<ObjectReference>>> metadata,
            EnumExample status,
            List<UnionExample> variants,
            Optional<Long> external) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, metadata, "metadata");
        missingFields = addFieldIfMissing(missingFields, status, "status");
        missingFields = addFieldIfMissing(missingFields, variants, "variants");
        missingFields = addFieldIfMissing(missingFields, external, "external");
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

        @JsonSetter(value = "metadata", nulls = Nulls.SKIP, contentNulls = Nulls.AS_EMPTY)
        private Map<StringAliasEx, Optional<List<ObjectReference>>> metadata = new LinkedHashMap<>();

        private EnumExample status;

        private List<UnionExample> variants = ConjureCollections.newList();

        private Optional<Long> external = Optional.empty();

        private Builder() {}

        public Builder from(ComplexExample other) {
            checkNotBuilt();
            metadata(other.getMetadata());
            status(other.getStatus());
            variants(other.getVariants());
            external(other.getExternal());
            return this;
        }

        public Builder metadata(@Nonnull Map<StringAliasEx, Optional<List<ObjectReference>>> metadata) {
            checkNotBuilt();
            this.metadata = new LinkedHashMap<>(Preconditions.checkNotNull(metadata, "metadata cannot be null"));
            return this;
        }

        public Builder putAllMetadata(@Nonnull Map<StringAliasEx, Optional<List<ObjectReference>>> metadata) {
            checkNotBuilt();
            this.metadata.putAll(Preconditions.checkNotNull(metadata, "metadata cannot be null"));
            return this;
        }

        public Builder metadata(StringAliasEx key, Optional<List<ObjectReference>> value) {
            checkNotBuilt();
            this.metadata.put(key, value);
            return this;
        }

        @JsonSetter("status")
        public Builder status(@Nonnull EnumExample status) {
            checkNotBuilt();
            this.status = Preconditions.checkNotNull(status, "status cannot be null");
            return this;
        }

        @JsonSetter(value = "variants", nulls = Nulls.SKIP)
        public Builder variants(@Nonnull Iterable<UnionExample> variants) {
            checkNotBuilt();
            this.variants = ConjureCollections.newList(Preconditions.checkNotNull(variants, "variants cannot be null"));
            return this;
        }

        public Builder addAllVariants(@Nonnull Iterable<UnionExample> variants) {
            checkNotBuilt();
            ConjureCollections.addAll(this.variants, Preconditions.checkNotNull(variants, "variants cannot be null"));
            return this;
        }

        public Builder variants(UnionExample variants) {
            checkNotBuilt();
            this.variants.add(variants);
            return this;
        }

        @JsonSetter(value = "external", nulls = Nulls.SKIP)
        public Builder external(@Nonnull Optional<? extends Long> external) {
            checkNotBuilt();
            this.external = Preconditions.checkNotNull(external, "external cannot be null")
                    .map(Function.identity());
            return this;
        }

        public Builder external(long external) {
            checkNotBuilt();
            this.external = Optional.of(Preconditions.checkNotNull(external, "external cannot be null"));
            return this;
        }

        @CheckReturnValue
        public ComplexExample build() {
            checkNotBuilt();
            this._buildInvoked = true;
            return new ComplexExample(metadata, status, variants, external);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
