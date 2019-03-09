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

import com.google.common.collect.ImmutableMap;
import com.palantir.conjure.java.undertow.lib.ParamStorer;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.AttachmentKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

enum ConjureParamStorer implements ParamStorer {
    INSTANCE;

    private static final AttachmentKey<Map<String, Object>> SAFE_PARAMS_ATTACH_KEY =
            AttachmentKey.create(Map.class);

    @Override
    public Map<String, Object> getParams(HttpServerExchange exchange) {
        return Optional.ofNullable(exchange.getAttachment(SAFE_PARAMS_ATTACH_KEY))
                .map(ImmutableMap::copyOf)
                .orElseGet(ImmutableMap::of);
    }

    @Override
    public void putSafeParam(
            HttpServerExchange exchange,
            String key,
            Object value) {
        Map<String, Object> map = exchange.getAttachment(SAFE_PARAMS_ATTACH_KEY);
        if (map == null) {
            map = new HashMap<>(10);
            exchange.putAttachment(SAFE_PARAMS_ATTACH_KEY, map);
        }
        map.put(key, value);
    }
}
