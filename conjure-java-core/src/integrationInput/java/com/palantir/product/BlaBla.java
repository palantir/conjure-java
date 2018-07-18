/*
 * (c) Copyright 2018 Palantir Technologies Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Generated;

@JsonDeserialize(builder = BlaBla.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class BlaBla {
    private final String blaBla1;

    private volatile int memoizedHashCode;

    private BlaBla(String blaBla1) {
        validateFields(blaBla1);
        this.blaBla1 = blaBla1;
    }

    /** Blabablabl */
    @JsonProperty("blaBla1")
    public String getBlaBla1() {
        return this.blaBla1;
    }

    @Override
    public boolean equals(Object other) {
        return this == other || (other instanceof BlaBla && equalTo((BlaBla) other));
    }

    private boolean equalTo(BlaBla other) {
        return this.blaBla1.equals(other.blaBla1);
    }

    @Override
    public int hashCode() {
        if (memoizedHashCode == 0) {
            memoizedHashCode = Objects.hash(blaBla1);
        }
        return memoizedHashCode;
    }

    @Override
    public String toString() {
        return new StringBuilder("BlaBla")
                .append("{")
                .append("blaBla1")
                .append(": ")
                .append(blaBla1)
                .append("}")
                .toString();
    }

    public static BlaBla of(String blaBla1) {
        return builder().blaBla1(blaBla1).build();
    }

    private static void validateFields(String blaBla1) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, blaBla1, "blaBla1");
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
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Builder {
        private String blaBla1;

        private Builder() {}

        public Builder from(BlaBla other) {
            blaBla1(other.getBlaBla1());
            return this;
        }

        /** Blabablabl */
        @JsonSetter("blaBla1")
        public Builder blaBla1(String blaBla1) {
            this.blaBla1 = Objects.requireNonNull(blaBla1, "blaBla1 cannot be null");
            return this;
        }

        public BlaBla build() {
            return new BlaBla(blaBla1);
        }
    }
}