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
import com.palantir.conjure.java.api.errors.CheckedServiceException;
import com.palantir.conjure.java.api.errors.EndpointServiceException;
import com.palantir.conjure.java.api.errors.ErrorType;
import com.palantir.conjure.java.api.errors.RemoteException;
import com.palantir.conjure.java.api.errors.SerializableError;
import com.palantir.conjure.java.api.errors.ServiceException;
import com.palantir.logsafe.Arg;
import com.palantir.logsafe.Preconditions;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

/**
 * A representation of a Conjure defined error that will be serialized and sent over the wire from a server to a client.
 * <p>Previously {@link SerializableError} was used for this purpose. The main difference is that
 * {@link SerializableError#parameters()} is a {@code Map<String, String>} while {@link ConjureError#parameters} is a
 * {@code Map<String, Object>}. When constructing a {@link SerializableError} from a {@link ServiceException}, the error
 * parameters were converted to strings using {@link Objects#toString} which made it difficult for clients to recover
 * the structured type. Storing the parameter value as an object allows servers to send Jackson-serialized objects which
 * the client can convert to their original type.
 *
 * @param errorCode A fixed code word identifying the type of error. For errors generated from {@link ServiceException}
 *                  and {@link CheckedServiceException}, this corresponds to the {@link ErrorType#code} and is part of
 *                  the service's API surface.
 * @param errorName A fixed name identifying the error. For errors generated from {@link ServiceException} and
 *                  {@link CheckedServiceException}, this corresponding to the {@link ErrorType#name} and is part of the
 *                  service's API surface.
 * @param errorInstanceId errorInstanceId A unique identifier for (this instance of) this error.
 * @param parameters params A list of parameters providing additional context about the error.
 */
record ConjureError(
        @JsonProperty("errorCode") String errorCode,
        @JsonProperty("errorName") String errorName,
        @JsonProperty("errorInstanceId") String errorInstanceId,
        @JsonProperty("parameters") Map<String, Object> parameters) {

    ConjureError {
        Preconditions.checkNotNull(errorCode, "errorCode cannot be null");
        Preconditions.checkNotNull(errorName, "errorName cannot be null");
        Preconditions.checkNotNull(errorInstanceId, "errorInstanceId cannot be null");
        Preconditions.checkNotNull(parameters, "parameters cannot be null");
    }

    static ConjureError fromCheckedServiceException(CheckedServiceException exception) {
        Map<String, Object> parameters = getParametersFromArgs(exception.getArgs());
        return new ConjureError(
                exception.getErrorType().code().name(),
                exception.getErrorType().name(),
                exception.getErrorInstanceId(),
                parameters);
    }

    static ConjureError fromEndpointServiceException(EndpointServiceException exception) {
        Map<String, Object> parameters = getParametersFromArgs(exception.getArgs());
        return new ConjureError(
                exception.getErrorType().code().name(),
                exception.getErrorType().name(),
                exception.getErrorInstanceId(),
                parameters);
    }

    static ConjureError fromServiceException(ServiceException exception) {
        Map<String, Object> parameters = new HashMap<>();
        for (Arg<?> arg : exception.getArgs()) {
            parameters.put(arg.getName(), Objects.toString(arg.getValue()));
        }
        ErrorType errorType = exception.getErrorType();
        return new ConjureError(errorType.code().name(), errorType.name(), exception.getErrorInstanceId(), parameters);
    }

    static ConjureError fromServiceExceptionWithJsonParameters(ServiceException exception) {
        Map<String, Object> parameters = getParametersFromArgs(exception.getArgs());
        ErrorType errorType = exception.getErrorType();
        return new ConjureError(errorType.code().name(), errorType.name(), exception.getErrorInstanceId(), parameters);
    }

    /**
     * {@link RemoteException}s are thrown by clients that send requests to remote services, and indicate an error with
     * the remote service. When a server forwards a {@link RemoteException}, the forwarded error is considered to be
     * internal to the server. The parameters from the {@link RemoteException} may not be relevant for the server
     * forwarding the error, and should not be included. {@link RemoteException}s are logged when received by the server,
     * so users can retrieve the parameters by finding logs corresponding to the {@code errorInstanceId}.
     */
    static ConjureError fromRemoteException(RemoteException exception) {
        SerializableError error = exception.getError();
        return new ConjureError(error.errorCode(), error.errorName(), error.errorInstanceId(), Collections.emptyMap());
    }

    private static boolean shouldIncludeArgInParameters(Arg<?> arg) {
        Object obj = arg.getValue();
        return obj != null
                && (!(obj instanceof Optional) || ((Optional<?>) obj).isPresent())
                && (!(obj instanceof OptionalInt optionalInt) || optionalInt.isPresent())
                && (!(obj instanceof OptionalLong optionalLong) || optionalLong.isPresent())
                && (!(obj instanceof OptionalDouble optionalDouble) || optionalDouble.isPresent());
    }

    private static Map<String, Object> getParametersFromArgs(Iterable<Arg<?>> args) {
        Map<String, Object> parameters = new HashMap<>();
        for (Arg<?> arg : args) {
            if (shouldIncludeArgInParameters(arg)) {
                parameters.put(arg.getName(), arg.getValue());
            }
        }
        return parameters;
    }
}
