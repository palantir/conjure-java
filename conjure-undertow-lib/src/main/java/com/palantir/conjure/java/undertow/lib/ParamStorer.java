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
import java.util.Map;

/**
 * Utility class to put different types of safe or unsafe parameters in a MultiMap attached to the exchange.
 * It requires the exchange to have been priorly through a ParametersHandler which would have created the attached
 * MultiMaps.
 */
public interface ParamStorer {

    Map<String, Object> getParams(HttpServerExchange exchange);

    void putSafeParam(HttpServerExchange exchange, String key, Object value);
}
