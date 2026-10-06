/*
 * (c) Copyright 2026 Palantir Technologies Inc. All rights reserved.
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

import com.palantir.conjure.java.api.errors.ErrorType;
import com.palantir.conjure.java.api.errors.QosException;
import com.palantir.conjure.java.api.errors.RemoteException;
import com.palantir.conjure.java.api.errors.SerializableError;
import com.palantir.conjure.java.api.errors.ServiceException;
import com.palantir.conjure.java.api.errors.UnknownRemoteException;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeRuntimeException;
import java.io.IOException;

public enum ExceptionScenario {
    SERVICE_LOCAL(Kind.SERVICE, 0, 0, false),
    REMOTE_NO_DIAGNOSTICS(Kind.REMOTE, 0, 0, false),
    REMOTE_UNMARKED(Kind.REMOTE, 0, 1, false),
    REMOTE_MARKED(Kind.REMOTE, 0, 1, true),
    QOS_UNMARKED(Kind.QOS, 0, 1, false),
    QOS_MARKED(Kind.QOS, 0, 1, true),
    UNKNOWN_UNMARKED(Kind.UNKNOWN, 0, 1, false),
    UNKNOWN_MARKED(Kind.UNKNOWN, 0, 1, true),
    IO_UNMARKED(Kind.IO, 0, 0, false),
    IO_MARKED(Kind.IO, 0, 1, true),
    REMOTE_WRAPPED_MARKED(Kind.REMOTE, 3, 1, true),
    REMOTE_DEEP_UNMARKED(Kind.REMOTE, 32, 1, false),
    REMOTE_DEEP_MARKED(Kind.REMOTE, 32, 1, true),
    MANY_DIAGNOSTICS_UNMARKED(Kind.REMOTE, 0, 32, false),
    MANY_DIAGNOSTICS_MARKED(Kind.REMOTE, 0, 32, true),
    MARKER_AT_DEPTH_LIMIT(Kind.REMOTE, 99, 1, true),
    MARKER_BEYOND_DEPTH_LIMIT(Kind.REMOTE, 100, 1, true),
    CAUSE_CYCLE(Kind.CYCLE, 0, 0, false);

    private final Kind kind;
    private final int wrapperDepth;
    private final int diagnostics;
    private final boolean marked;

    ExceptionScenario(Kind kind, int wrapperDepth, int diagnostics, boolean marked) {
        this.kind = kind;
        this.wrapperDepth = wrapperDepth;
        this.diagnostics = diagnostics;
        this.marked = marked;
    }

    Throwable createFailure() {
        Throwable failure = switch (kind) {
            case SERVICE -> new ServiceException(ErrorType.INTERNAL);
            case REMOTE ->
                new RemoteException(SerializableError.forException(new ServiceException(ErrorType.INTERNAL)), 500);
            case QOS -> QosException.unavailable();
            case UNKNOWN -> new UnknownRemoteException(500, "Downstream failed");
            case IO -> new IOException("Connection closed before a response was received");
            case CYCLE -> causeCycle();
        };
        for (int index = 0; index < diagnostics; index++) {
            // Real HTTP error diagnostics can contain unrelated safe args before the exhaustion marker.
            failure.addSuppressed(new SafeRuntimeException(
                    "Response diagnostic",
                    SafeArg.of("status", 500),
                    SafeArg.of("service", "benchmark"),
                    marked && index == diagnostics - 1
                            ? SafeArg.of("Dialogue-Retries-Exhausted", "true")
                            : SafeArg.of("contentType", "application/json")));
        }
        for (int depth = 0; depth < wrapperDepth; depth++) {
            failure = new RuntimeException("Application wrapper", failure);
        }
        return failure;
    }

    boolean expectsMarker() {
        // The handler examines positions 0 through 99 in the cause chain.
        return marked && wrapperDepth < 100;
    }

    private static Throwable causeCycle() {
        RuntimeException first = new RuntimeException("First cause");
        RuntimeException second = new RuntimeException("Second cause", first);
        first.initCause(second);
        return first;
    }

    private enum Kind {
        SERVICE,
        REMOTE,
        QOS,
        UNKNOWN,
        IO,
        CYCLE
    }
}
