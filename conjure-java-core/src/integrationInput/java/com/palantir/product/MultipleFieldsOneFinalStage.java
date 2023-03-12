package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.errorprone.annotations.Immutable;
import com.palantir.logsafe.DoNotLog;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import com.palantir.ri.ResourceIdentifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@DoNotLog
@Immutable
@JsonDeserialize(builder = MultipleFieldsOneFinalStage.DefaultBuilder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class MultipleFieldsOneFinalStage {
    private final Map<ResourceIdentifier, String> mappedRids;

    private final OneField token;

    private final Optional<OneField> optionalItem;

    private int memoizedHashCode;

    private MultipleFieldsOneFinalStage(
            Map<ResourceIdentifier, String> mappedRids, OneField token, Optional<OneField> optionalItem) {
        validateFields(mappedRids, token, optionalItem);
        this.mappedRids = Collections.unmodifiableMap(mappedRids);
        this.token = token;
        this.optionalItem = optionalItem;
    }

    @JsonProperty("mappedRids")
    public Map<ResourceIdentifier, String> getMappedRids() {
        return this.mappedRids;
    }

    @JsonProperty("token")
    public OneField getToken() {
        return this.token;
    }

    /**
     * @deprecated this optional is deprecated
     */
    @JsonProperty("optionalItem")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    @Deprecated
    public Optional<OneField> getOptionalItem() {
        return this.optionalItem;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other
                || (other instanceof MultipleFieldsOneFinalStage && equalTo((MultipleFieldsOneFinalStage) other));
    }

    private boolean equalTo(MultipleFieldsOneFinalStage other) {
        if (this.memoizedHashCode != 0
                && other.memoizedHashCode != 0
                && this.memoizedHashCode != other.memoizedHashCode) {
            return false;
        }
        return this.mappedRids.equals(other.mappedRids)
                && this.token.equals(other.token)
                && this.optionalItem.equals(other.optionalItem);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            int hash = 1;
            hash = 31 * hash + this.mappedRids.hashCode();
            hash = 31 * hash + this.token.hashCode();
            hash = 31 * hash + this.optionalItem.hashCode();
            result = hash;
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    @DoNotLog
    public String toString() {
        return "MultipleFieldsOneFinalStage{mappedRids: " + mappedRids + ", token: " + token + ", optionalItem: "
                + optionalItem + '}';
    }

    public static MultipleFieldsOneFinalStage of(
            Map<ResourceIdentifier, String> mappedRids, OneField token, OneField optionalItem) {
        return builder()
                .token(token)
                .mappedRids(mappedRids)
                .optionalItem(Optional.of(optionalItem))
                .build();
    }

    private static void validateFields(
            Map<ResourceIdentifier, String> mappedRids, OneField token, Optional<OneField> optionalItem) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, mappedRids, "mappedRids");
        missingFields = addFieldIfMissing(missingFields, token, "token");
        missingFields = addFieldIfMissing(missingFields, optionalItem, "optionalItem");
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

    public static TokenStageBuilder builder() {
        return new DefaultBuilder();
    }

    public interface TokenStageBuilder {
        Completed_StageBuilder token(@Nonnull OneField token);

        Builder from(MultipleFieldsOneFinalStage other);
    }

    public interface Completed_StageBuilder {
        MultipleFieldsOneFinalStage build();

        Completed_StageBuilder mappedRids(@Nonnull Map<ResourceIdentifier, String> mappedRids);

        Completed_StageBuilder putAllMappedRids(@Nonnull Map<ResourceIdentifier, String> mappedRids);

        Completed_StageBuilder mappedRids(ResourceIdentifier key, String value);

        /**
         * @deprecated this optional is deprecated
         */
        @Deprecated
        Completed_StageBuilder optionalItem(@Nonnull Optional<OneField> optionalItem);

        /**
         * @deprecated this optional is deprecated
         */
        @Deprecated
        Completed_StageBuilder optionalItem(@Nonnull OneField optionalItem);
    }

    public interface Builder extends TokenStageBuilder, Completed_StageBuilder {
        @Override
        Builder token(@Nonnull OneField token);

        @Override
        Builder from(MultipleFieldsOneFinalStage other);

        @Override
        MultipleFieldsOneFinalStage build();

        @Override
        Builder mappedRids(@Nonnull Map<ResourceIdentifier, String> mappedRids);

        @Override
        Builder putAllMappedRids(@Nonnull Map<ResourceIdentifier, String> mappedRids);

        @Override
        Builder mappedRids(ResourceIdentifier key, String value);

        /**
         * @deprecated this optional is deprecated
         */
        @Deprecated
        @Override
        Builder optionalItem(@Nonnull Optional<OneField> optionalItem);

        /**
         * @deprecated this optional is deprecated
         */
        @Deprecated
        @Override
        Builder optionalItem(@Nonnull OneField optionalItem);
    }

    @Immutable
    @Generated("com.palantir.conjure.java.types.BeanBuilderGenerator")
    @JsonIgnoreProperties(ignoreUnknown = true)
    static final class DefaultBuilder implements Builder {
        boolean _buildInvoked;

        private Map<ResourceIdentifier, String> mappedRids = new LinkedHashMap<>();

        private OneField token;

        private Optional<OneField> optionalItem = Optional.empty();

        private DefaultBuilder() {}

        @Override
        public Builder from(MultipleFieldsOneFinalStage other) {
            checkNotBuilt();
            mappedRids(other.getMappedRids());
            token(other.getToken());
            optionalItem(other.getOptionalItem());
            return this;
        }

        @Override
        @JsonSetter(value = "mappedRids", nulls = Nulls.SKIP)
        public Builder mappedRids(@Nonnull Map<ResourceIdentifier, String> mappedRids) {
            checkNotBuilt();
            this.mappedRids = new LinkedHashMap<>(Preconditions.checkNotNull(mappedRids, "mappedRids cannot be null"));
            return this;
        }

        @Override
        public Builder putAllMappedRids(@Nonnull Map<ResourceIdentifier, String> mappedRids) {
            checkNotBuilt();
            this.mappedRids.putAll(Preconditions.checkNotNull(mappedRids, "mappedRids cannot be null"));
            return this;
        }

        @Override
        public Builder mappedRids(ResourceIdentifier key, String value) {
            checkNotBuilt();
            this.mappedRids.put(key, value);
            return this;
        }

        @Override
        @JsonSetter("token")
        public Builder token(@Nonnull OneField token) {
            checkNotBuilt();
            this.token = Preconditions.checkNotNull(token, "token cannot be null");
            return this;
        }

        /**
         * @deprecated this optional is deprecated
         */
        @Deprecated
        @Override
        @JsonSetter(value = "optionalItem", nulls = Nulls.SKIP)
        public Builder optionalItem(@Nonnull Optional<OneField> optionalItem) {
            checkNotBuilt();
            this.optionalItem = Preconditions.checkNotNull(optionalItem, "optionalItem cannot be null");
            return this;
        }

        /**
         * @deprecated this optional is deprecated
         */
        @Deprecated
        @Override
        public Builder optionalItem(@Nonnull OneField optionalItem) {
            checkNotBuilt();
            this.optionalItem = Optional.of(Preconditions.checkNotNull(optionalItem, "optionalItem cannot be null"));
            return this;
        }

        @Override
        public MultipleFieldsOneFinalStage build() {
            checkNotBuilt();
            this._buildInvoked = true;
            return new MultipleFieldsOneFinalStage(mappedRids, token, optionalItem);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
