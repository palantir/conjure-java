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

import com.palantir.conjure.java.undertow.lib.BinaryResponseBody;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import java.io.IOException;
import java.io.InputStream;

/** Utility functions for octet-stream serde. */
final class BinarySerializers {

    private static final String CONTENT_TYPE = "application/octet-stream";

    static void serialize(BinaryResponseBody value, HttpServerExchange exchange) throws IOException {
        Preconditions.checkNotNull(value, "A BinaryResponseBody value is required");
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, CONTENT_TYPE);
        value.write(exchange.getOutputStream());
    }

    static InputStream deserializeInputStream(HttpServerExchange exchange) {
        String contentType = exchange.getRequestHeaders().getFirst(Headers.CONTENT_TYPE);
        if (contentType == null) {
            throw new SafeIllegalArgumentException("Request is missing Content-Type header");
        }
        if (!contentType.startsWith(CONTENT_TYPE)) {
            throw new SafeIllegalArgumentException("Unsupported Content-Type",
                    SafeArg.of("Content-Type", contentType));
        }
        return exchange.getInputStream();
    }

    private BinarySerializers() {}
}
