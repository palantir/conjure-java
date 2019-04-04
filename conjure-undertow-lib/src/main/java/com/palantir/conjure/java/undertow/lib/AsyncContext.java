/*
 * (c) Copyright 2019 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java.undertow.lib;

public interface AsyncContext<T> {

    /**
     * Sets the value to be returned if the request has not already been completed. Returns true if the request has
     * not already completed.
     */
    boolean complete(T value);

    /**
     * Completes this request exceptionally if it has not already been completed. Returns true if the request has not
     * already completed.
     */
    boolean completeExceptionally(Throwable throwable);

    /**
     * Registers a callback to execute if the asynchronous request timeout is reached before this context is completed.
     * Cancellation should never be used for control flow, and represents an upper limit.
     * Timeout callbacks must be registered when a request is received and cannot be added asynchronously.
     */
    void onTimeout(Runnable onTimeoutCallback);
}
