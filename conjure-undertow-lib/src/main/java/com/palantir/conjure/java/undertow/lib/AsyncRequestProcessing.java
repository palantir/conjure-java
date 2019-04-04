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

import io.undertow.server.HttpServerExchange;

/**
 * Provides functionality to process requests asynchronously. This decouples the lifecycle of a request
 * from the execution of the handler method.
 */
public interface AsyncRequestProcessing {

    /** Creates a new {@link AsyncContext} for the provided {@link HttpServerExchange request}. */
    <T> AsyncContext<T> context(ReturnValueWriter<T> returnValueWriter, HttpServerExchange exchange);

}
