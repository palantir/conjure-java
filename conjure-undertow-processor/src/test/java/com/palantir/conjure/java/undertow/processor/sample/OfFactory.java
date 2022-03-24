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

package com.palantir.conjure.java.undertow.processor.sample;

import com.palantir.conjure.java.undertow.annotations.Handle;
import com.palantir.conjure.java.undertow.annotations.HttpMethod;

public interface OfFactory {

    @Handle(method = HttpMethod.GET, path = "/{pathVar}")
    void ping(@Handle.PathParam PathVariable pathVar);

    final class PathVariable {
        private final String value;

        private PathVariable(String value) {
            this.value = value;
        }

        public static PathVariable of(String value) {
            return new PathVariable(value);
        }

        @Override
        public String toString() {
            return "PathVariable{value='" + value + '\'' + '}';
        }
    }
}
