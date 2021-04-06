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

package com.palantir.conjure.java.undertow.lib;

import com.google.common.annotations.Beta;
import com.google.common.collect.ListMultimap;
import com.palantir.logsafe.Arg;
import java.util.List;
import java.util.Optional;

/**
 * Interface providing a view over data provided by the the original HTTP request including request headers and query
 * parameters. This is a read only interface which should only be implemented by {@code conjure-java-undertow-runtime}.
 */
@Beta
public interface RequestContext {

    /**
     * Returns all values of the header named {@code headerName}. The name is case insensitive. An empty list is
     * returned if no such header exists.
     */
    List<String> header(String headerName);

    /**
     * Returns the value of the first header named {@code headerName}. The name is case insensitive.
     * An {@link Optional#empty()} is returned if no such header exists.
     */
    Optional<String> firstHeader(String headerName);

    /**
     * Returns all query parameters associated with the current request.
     */
    ListMultimap<String, String> queryParameters();

    /**
     * Associates an {@link Arg} with the current request for observability.
     * Implementations may choose to include this data in the request log.
     */
    void requestArg(Arg<?> arg);
}
