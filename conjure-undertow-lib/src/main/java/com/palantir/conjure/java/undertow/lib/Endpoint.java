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

import com.palantir.tokens.auth.ImmutablesStyle;
import io.undertow.util.HttpString;
import java.util.Optional;
import org.immutables.value.Value;

@Value.Immutable(builder = false)
@ImmutablesStyle
public interface Endpoint {

    @Value.Parameter
    HttpString method();

    @Value.Parameter
    String template();

    @Value.Parameter
    Optional<String> serviceName();

    @Value.Parameter
    Optional<String> name();

    static Endpoint of(HttpString method, String template) {
        return ImmutableEndpoint.of(method, template, Optional.empty(), Optional.empty());
    }

    static Endpoint of(HttpString method, String template, String serviceName, String name) {
        return ImmutableEndpoint.of(method, template, Optional.of(serviceName), Optional.of(name));
    }

}
