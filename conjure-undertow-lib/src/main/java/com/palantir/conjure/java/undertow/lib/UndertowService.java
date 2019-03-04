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

package com.palantir.conjure.java.undertow.lib;

import java.util.List;

/**
 * A {@link UndertowService} provides a list of {@link Endpoint endpoints} when it is
 * given the {@link UndertowRuntime}. These {@link Endpoint endpoints} may be
 * registered with an {@link EndpointRegistry} in order to be exposed by a
 * web server. The server is responsible for providing an {@link UndertowRuntime} and
 * orchestrating registration with the {@link EndpointRegistry} allowing API
 * implementors to add APIs using
 * <code>server.api(MyServiceEndpoints.of(myServiceImpl)</code>.
 */
public interface UndertowService {

    List<Endpoint> endpoints(UndertowRuntime runtime);

}
