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

package com.palantir.conjure.java.undertow.runtime;

import com.google.common.collect.ImmutableList;
import com.google.errorprone.annotations.CompileTimeConstant;
import com.palantir.conjure.java.api.errors.ErrorType;
import com.palantir.logsafe.Arg;
import com.palantir.logsafe.SafeLoggable;
import io.undertow.util.StatusCodes;
import java.util.List;

/** Internal type to signal a conjure protocol-level failure with a specific response code. */
public final class FrameworkException extends RuntimeException implements SafeLoggable {

    private static final ErrorType UNPROCESSABLE_ENTITY =
            ErrorType.create(ErrorType.Code.INVALID_ARGUMENT, "Conjure:UnprocessableEntity");
    private static final ErrorType UNSUPPORTED_MEDIA_TYPE =
            ErrorType.create(ErrorType.Code.INVALID_ARGUMENT, "Conjure:UnsupportedMediaType");

    private final String logMessage;
    private final List<Arg<?>> arguments;
    private final int statusCode;
    private final ErrorType errorType;

    private FrameworkException(String message, ErrorType errorType, int statusCode, Throwable cause, Arg<?>... args) {
        super(renderMessage(message, args), cause);
        this.logMessage = message;
        this.arguments = ImmutableList.copyOf(args);
        this.statusCode = statusCode;
        this.errorType = errorType;
    }

    static FrameworkException unprocessableEntity(
            @CompileTimeConstant String message, Throwable cause, Arg<?>... args) {
        return new FrameworkException(message, UNPROCESSABLE_ENTITY, StatusCodes.UNPROCESSABLE_ENTITY, cause, args);
    }

    static FrameworkException unsupportedMediaType(@CompileTimeConstant String message, Arg<?>... args) {
        return new FrameworkException(message, UNSUPPORTED_MEDIA_TYPE, StatusCodes.UNSUPPORTED_MEDIA_TYPE, null, args);
    }

    @Override
    public String getLogMessage() {
        return logMessage;
    }

    @Override
    public List<Arg<?>> getArgs() {
        return arguments;
    }

    int getStatusCode() {
        return statusCode;
    }

    ErrorType getErrorType() {
        return errorType;
    }

    private static String renderMessage(String message, Arg<?>... args) {
        if (args.length == 0) {
            return message;
        }

        StringBuilder builder = new StringBuilder();
        builder.append(message).append(": {");
        for (int i = 0; i < args.length; i++) {
            Arg<?> arg = args[i];
            if (i > 0) {
                builder.append(", ");
            }

            builder.append(arg.getName()).append("=").append(arg.getValue());
        }
        builder.append("}");

        return builder.toString();
    }
}
