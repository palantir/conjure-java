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
 * The Undertow server Conjure generator emits implementations of {@link UndertowService} which produce an
 * {@link Endpoint} for each endpoint described in the Conjure definition. The server is responsible for providing an
 * {@link UndertowRuntime} and orchestrating registration with the
 *
 * <pre>ConjureHandler</pre>
 *
 * allowing API implementors to add APIs using <code>server.api(MyServiceEndpoints.of(myServiceImpl)</code>.
 */
public interface UndertowService {

    List<Endpoint> endpoints(UndertowRuntime runtime);
}
