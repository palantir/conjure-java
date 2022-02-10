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
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.Cookie;
import java.io.IOException;

public final class CookieDeserializer<T> implements Deserializer<T> {

    private final String cookieName;
    private final ParamDecoder<? extends T> decoder;

    public CookieDeserializer(String cookieName, ParamDecoder<? extends T> decoder) {
        this.cookieName = Preconditions.checkNotNull(cookieName, "Cookie name is required");
        this.decoder = Preconditions.checkNotNull(decoder, "Decoder is required");
    }

    @Override
    public T deserialize(HttpServerExchange exchange) throws IOException {
        Cookie cookie = exchange.getRequestCookie(cookieName);
        if (cookie == null) {
            return decoder.noValuePresent()
                    .orElseThrow(() -> new SafeIllegalArgumentException(
                            "Cookie value is required", SafeArg.of("cookieName", cookieName)));
        }
        return decoder.decode(cookie.getValue());
    }

    @Override
    public String toString() {
        return "CookieDeserializer{cookieName='" + cookieName + '\'' + ", decoder=" + decoder + '}';
    }
}
