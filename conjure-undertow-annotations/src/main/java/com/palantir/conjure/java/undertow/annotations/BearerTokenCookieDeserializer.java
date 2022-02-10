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
import com.palantir.conjure.java.undertow.lib.UndertowRuntime;
import com.palantir.logsafe.Preconditions;
import com.palantir.tokens.auth.BearerToken;
import io.undertow.server.HttpServerExchange;
import java.io.IOException;

public final class BearerTokenCookieDeserializer implements Deserializer<BearerToken> {

    private final UndertowRuntime runtime;
    private final String cookieName;

    public BearerTokenCookieDeserializer(UndertowRuntime runtime, String cookieName) {
        this.runtime = Preconditions.checkNotNull(runtime, "Undertow runtime is required");
        this.cookieName = Preconditions.checkNotNull(cookieName, "Cookie name is required");
    }

    @Override
    public BearerToken deserialize(HttpServerExchange exchange) throws IOException {
        return runtime.auth().cookie(exchange, cookieName);
    }

    @Override
    public String toString() {
        return "BearerTokenCookieDeserializer{runtime=" + runtime + ", cookieName='" + cookieName + '\'' + '}';
    }
}
