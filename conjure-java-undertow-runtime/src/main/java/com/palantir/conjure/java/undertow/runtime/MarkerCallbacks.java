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

package com.palantir.conjure.java.undertow.runtime;

import com.palantir.conjure.java.undertow.lib.MarkerCallback;
import java.util.List;

/** Utility functionality for {@link MarkerCallback}. */
final class MarkerCallbacks {

    /**
     * Builds a {@link MarkerCallback} from the provided {@link ParamMarker parameter markers}.
     * Pre-compute callbacks here rather than iteration each time a marked parameter is encountered.
     */
    static MarkerCallback fold(List<ParamMarker> paramMarkers) {
        return paramMarkers.stream().reduce(
                (markerClass, parameterName, parameterValue, exchange) -> { },
                (paramMarker1, paramMarker2) -> (markerClass, parameterName, parameterValue, exchange) -> {
                    paramMarker1.mark(markerClass, parameterName, parameterValue, exchange);
                    paramMarker2.mark(markerClass, parameterName, parameterValue, exchange);
                })::mark;
    }

    private MarkerCallbacks() {}
}
