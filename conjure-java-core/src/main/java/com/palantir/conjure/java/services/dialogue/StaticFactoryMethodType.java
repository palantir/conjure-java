/*
 * (c) Copyright 2021 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java.services.dialogue;

import com.palantir.logsafe.exceptions.SafeIllegalStateException;

enum StaticFactoryMethodType {
    BLOCKING,
    BLOCKING_WITH_ERRORS,
    ASYNC,
    ASYNC_WITH_ERRORS;

    public <R> R switchBy(R blocking, R async, R blockingWithErrors, R asyncWithErrors) {
        return switch (this) {
            case ASYNC -> async;
            case ASYNC_WITH_ERRORS -> asyncWithErrors;
            case BLOCKING -> blocking;
            case BLOCKING_WITH_ERRORS -> blockingWithErrors;
            default -> throw new SafeIllegalStateException("Unknown");
        };
    }
}
