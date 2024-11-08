/*
 * (c) Copyright 2024 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java.undertow.runtime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.palantir.conjure.java.undertow.lib.CheckedServiceException;
import com.palantir.logsafe.Preconditions;
import java.util.Map;
import java.util.stream.Collectors;

record ConjureError(
        @JsonProperty("errorCode") String errorCode,
        @JsonProperty("errorName") String errorName,
        @JsonProperty("errorInstanceId") String errorInstanceId,
        @JsonProperty("parameters") Map<String, Object> parameters) {

    ConjureError {
        Preconditions.checkNotNull(errorCode, "errorCode cannot be null");
        Preconditions.checkNotNull(errorName, "errorName cannot be null");
        Preconditions.checkNotNull(parameters, "parameters cannot be null");
        // TODO(pm): why was there a default empty string value for errorInstanceId in SerializableError?
        Preconditions.checkNotNull(errorInstanceId, "errorInstanceId cannot be null");
    }

    static ConjureError fromCheckedServiceException(CheckedServiceException exception) {
        Map<String, Object> parameters = exception.getArgs().stream()
                .filter(arg -> arg.getValue() != null)
                .map(arg -> Map.entry(arg.getName(), arg.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return new ConjureError(
                exception.getErrorType().code().name(),
                exception.getErrorType().name(),
                exception.getErrorInstanceId(),
                parameters);
    }
}
