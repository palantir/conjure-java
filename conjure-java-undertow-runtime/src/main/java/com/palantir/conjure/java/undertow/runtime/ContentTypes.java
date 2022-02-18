/*
 * (c) Copyright 2022 Palantir Technologies Inc. All rights reserved.
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

import com.google.common.collect.ImmutableList;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import com.palantir.logsafe.logger.SafeLogger;
import com.palantir.logsafe.logger.SafeLoggerFactory;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderValues;
import io.undertow.util.Headers;

final class ContentTypes {

    private static final SafeLogger log = SafeLoggerFactory.get(ContentTypes.class);

    /**
     * Gets the request {@code Content-Type} header if exactly one value exists, otherwise logs
     * a warning. This notifies us in the unexpected case when multiple
     * content-type headers are incorrectly sent to the server, it's not clear which should
     * be used.
     */
    static String getContentType(HttpServerExchange exchange) {

        HeaderValues contentTypeValues = exchange.getRequestHeaders().get(Headers.CONTENT_TYPE);
        if (contentTypeValues == null || contentTypeValues.isEmpty()) {
            throw new SafeIllegalArgumentException("Request is missing Content-Type header");
        } else if (contentTypeValues.size() != 1) {
            log.warn(
                    "Request has too many Content-Type headers",
                    SafeArg.of("contentTypes", ImmutableList.copyOf(contentTypeValues)));
            return contentTypeValues.getFirst();
        }
        return contentTypeValues.get(0);
    }

    private ContentTypes() {}
}
