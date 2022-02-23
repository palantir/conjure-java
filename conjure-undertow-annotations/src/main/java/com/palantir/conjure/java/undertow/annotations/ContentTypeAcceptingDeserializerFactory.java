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

package com.palantir.conjure.java.undertow.annotations;

import com.palantir.conjure.java.undertow.lib.Deserializer;
import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.conjure.java.undertow.lib.TypeMarker;
import com.palantir.conjure.java.undertow.lib.UndertowRuntime;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIoException;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import java.io.IOException;

public interface ContentTypeAcceptingDeserializerFactory<U> extends DeserializerFactory<U> {

    /** The content type that is accepted by this deserializer. */
    String supportedContentType();

    /** Deserialize the request body. */
    U deserialize(HttpServerExchange exchange) throws IOException;

    /** Per default, a content type is accepted if its value starts with the supported content type. */
    default boolean accepts(String contentType) {
        return contentType.startsWith(supportedContentType());
    }

    @Override
    @SuppressWarnings("unchecked")
    default <T extends U> Deserializer<T> deserializer(
            TypeMarker<T> _type, UndertowRuntime _runtime, Endpoint _endpoint) {
        return (Deserializer<T>) (Deserializer<U>) exchange -> {
            String maybeContentType = exchange.getRequestHeaders().getFirst(Headers.CONTENT_TYPE);
            if (maybeContentType == null || !accepts(maybeContentType)) {
                throw new SafeIoException("Unsupported content-type", SafeArg.of("contentType", maybeContentType));
            }
            return ContentTypeAcceptingDeserializerFactory.this.deserialize(exchange);
        };
    }
}
