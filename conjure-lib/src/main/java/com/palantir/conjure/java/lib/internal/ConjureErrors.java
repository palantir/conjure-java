/*
 * (c) Copyright 2025 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java.lib.internal;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.exceptions.SafeIllegalStateException;

public final class ConjureErrors {
    private ConjureErrors() {}

    public abstract static class BaseEndpointError<T> {
        @Safe
        private final String errorCode;

        @Safe
        private final String errorName;

        @Safe
        private final String errorInstanceId;

        private final T params;

        protected BaseEndpointError(String errorCode, String errorName, String errorInstanceId, T params) {
            this.errorCode = errorCode;
            this.errorName = errorName;
            this.errorInstanceId = errorInstanceId;
            this.params = params;
        }

        public final String getErrorCode() {
            return errorCode;
        }

        public final String getErrorName() {
            return errorName;
        }

        public final String getErrorInstanceId() {
            return errorInstanceId;
        }

        public final T getParams() {
            return params;
        }
    }

    public abstract static class NullToDefaultDeserializer<T> extends JsonDeserializer<T> {
        public abstract T create();

        /**
         * If a non-null value is deserialized as `null`, throw an exception.
         */
        @Override
        public T deserialize(JsonParser _parser, DeserializationContext _ctxt) {
            throw new SafeIllegalStateException("Attempted to deserialize non-null value as null");
        }

        /**
         * When `null` is deserialized, a new object of type `T` is created.
         */
        @Override
        public T getNullValue(DeserializationContext _ctxt) {
            return create();
        }
    }
}
