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

import com.google.common.collect.Multimap;
import com.palantir.logsafe.SafeArg;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.AttachmentKey;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Parameters {

    private static final Logger log = LoggerFactory.getLogger(Parameters.class);

    public static final AttachmentKey<Multimap<String, Object>> SAFE_PARAMS_ATTACH_KEY =
            AttachmentKey.create(Multimap.class);
    public static final AttachmentKey<Multimap<String, Object>> UNSAFE_PARAMS_ATTACH_KEY =
            AttachmentKey.create(Multimap.class);

    private Parameters() {}

    public static void putSafePathParam(HttpServerExchange exchange, String key, Object value) {
        putParam(exchange, SAFE_PARAMS_ATTACH_KEY, key, value);
    }

    public static void putUnsafePathParam(HttpServerExchange exchange, String key, Object value) {
        putParam(exchange, UNSAFE_PARAMS_ATTACH_KEY, key, value);
    }

    public static void putSafeQueryParam(HttpServerExchange exchange, String key, Object value) {
        putParam(exchange, SAFE_PARAMS_ATTACH_KEY, key, value);
    }

    public static void putUnsafeQueryParam(HttpServerExchange exchange, String key, Object value) {
        putParam(exchange, UNSAFE_PARAMS_ATTACH_KEY, key, value);
    }

    public static void putSafeHeaderParam(HttpServerExchange exchange, String key, Object value) {
        putParam(exchange, SAFE_PARAMS_ATTACH_KEY, key, value);
    }

    public static void putUnsafeHeaderParam(HttpServerExchange exchange, String key, Object value) {
        putParam(exchange, UNSAFE_PARAMS_ATTACH_KEY, key, value);
    }

    private static void putParam(
            HttpServerExchange exchange,
            AttachmentKey<Multimap<String, Object>> attachmentKey,
            String key,
            Object value) {
        Optional<Multimap<String, Object>> multimap = Optional.ofNullable(exchange.getAttachment(attachmentKey));
        if (multimap.isPresent()) {
            multimap.get().put(key, value);
        } else {
            log.warn("Attachment key not found. Impossible to put param {}.", SafeArg.of("param-name", key));
        }
    }
}
