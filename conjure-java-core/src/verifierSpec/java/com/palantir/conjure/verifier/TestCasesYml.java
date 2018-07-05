package com.palantir.conjure.verifier;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Generated;

@JsonDeserialize(builder = TestCasesYml.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class TestCasesYml {
    private final Map<EndpointName, PositiveAndNegativeTestCases> autoDeserialize;

    private final Map<EndpointName, List<ServiceTestStructure>> manualStuff;

    private volatile int memoizedHashCode;

    private TestCasesYml(
            Map<EndpointName, PositiveAndNegativeTestCases> autoDeserialize,
            Map<EndpointName, List<ServiceTestStructure>> manualStuff) {
        validateFields(autoDeserialize, manualStuff);
        this.autoDeserialize = Collections.unmodifiableMap(autoDeserialize);
        this.manualStuff = Collections.unmodifiableMap(manualStuff);
    }

    @JsonProperty("autoDeserialize")
    public Map<EndpointName, PositiveAndNegativeTestCases> getAutoDeserialize() {
        return this.autoDeserialize;
    }

    @JsonProperty("manualStuff")
    public Map<EndpointName, List<ServiceTestStructure>> getManualStuff() {
        return this.manualStuff;
    }

    @Override
    public boolean equals(Object other) {
        return this == other || (other instanceof TestCasesYml && equalTo((TestCasesYml) other));
    }

    private boolean equalTo(TestCasesYml other) {
        return this.autoDeserialize.equals(other.autoDeserialize)
                && this.manualStuff.equals(other.manualStuff);
    }

    @Override
    public int hashCode() {
        if (memoizedHashCode == 0) {
            memoizedHashCode = Objects.hash(autoDeserialize, manualStuff);
        }
        return memoizedHashCode;
    }

    @Override
    public String toString() {
        return new StringBuilder("TestCasesYml")
                .append("{")
                .append("autoDeserialize")
                .append(": ")
                .append(autoDeserialize)
                .append(", ")
                .append("manualStuff")
                .append(": ")
                .append(manualStuff)
                .append("}")
                .toString();
    }

    public static TestCasesYml of(
            Map<EndpointName, PositiveAndNegativeTestCases> autoDeserialize,
            Map<EndpointName, List<ServiceTestStructure>> manualStuff) {
        return builder().autoDeserialize(autoDeserialize).manualStuff(manualStuff).build();
    }

    private static void validateFields(
            Map<EndpointName, PositiveAndNegativeTestCases> autoDeserialize,
            Map<EndpointName, List<ServiceTestStructure>> manualStuff) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, autoDeserialize, "autoDeserialize");
        missingFields = addFieldIfMissing(missingFields, manualStuff, "manualStuff");
        if (missingFields != null) {
            throw new IllegalArgumentException(
                    "Some required fields have not been set: " + missingFields);
        }
    }

    private static List<String> addFieldIfMissing(
            List<String> prev, Object fieldValue, String fieldName) {
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
        private Map<EndpointName, PositiveAndNegativeTestCases> autoDeserialize =
                new LinkedHashMap<>();

        private Map<EndpointName, List<ServiceTestStructure>> manualStuff = new LinkedHashMap<>();

        private Builder() {}

        public Builder from(TestCasesYml other) {
            autoDeserialize(other.getAutoDeserialize());
            manualStuff(other.getManualStuff());
            return this;
        }

        @JsonSetter("autoDeserialize")
        public Builder autoDeserialize(
                Map<EndpointName, PositiveAndNegativeTestCases> autoDeserialize) {
            this.autoDeserialize.clear();
            this.autoDeserialize.putAll(
                    Objects.requireNonNull(autoDeserialize, "autoDeserialize cannot be null"));
            return this;
        }

        public Builder putAllAutoDeserialize(
                Map<EndpointName, PositiveAndNegativeTestCases> autoDeserialize) {
            this.autoDeserialize.putAll(
                    Objects.requireNonNull(autoDeserialize, "autoDeserialize cannot be null"));
            return this;
        }

        public Builder autoDeserialize(EndpointName key, PositiveAndNegativeTestCases value) {
            this.autoDeserialize.put(key, value);
            return this;
        }

        @JsonSetter("manualStuff")
        public Builder manualStuff(Map<EndpointName, List<ServiceTestStructure>> manualStuff) {
            this.manualStuff.clear();
            this.manualStuff.putAll(
                    Objects.requireNonNull(manualStuff, "manualStuff cannot be null"));
            return this;
        }

        public Builder putAllManualStuff(
                Map<EndpointName, List<ServiceTestStructure>> manualStuff) {
            this.manualStuff.putAll(
                    Objects.requireNonNull(manualStuff, "manualStuff cannot be null"));
            return this;
        }

        public Builder manualStuff(EndpointName key, List<ServiceTestStructure> value) {
            this.manualStuff.put(key, value);
            return this;
        }

        public TestCasesYml build() {
            return new TestCasesYml(autoDeserialize, manualStuff);
        }
    }
}
