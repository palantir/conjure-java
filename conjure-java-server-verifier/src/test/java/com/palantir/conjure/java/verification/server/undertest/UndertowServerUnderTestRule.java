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

package com.palantir.conjure.java.verification.server.undertest;

import com.google.common.reflect.Reflection;
import com.palantir.conjure.java.undertow.lib.UndertowRuntime;
import com.palantir.conjure.java.undertow.lib.UndertowService;
import com.palantir.conjure.java.undertow.runtime.ConjureHandler;
import com.palantir.conjure.java.undertow.runtime.ConjureUndertowRuntime;
import com.palantir.conjure.verification.client.AutoDeserializeServiceEndpoints;
import com.palantir.conjure.verification.client.UndertowAutoDeserializeService;
import io.undertow.Handlers;
import io.undertow.Undertow;
import org.junit.rules.ExternalResource;

public final class UndertowServerUnderTestRule extends ExternalResource {

    private static final int PORT = 12346;

    private Undertow server;

    @Override
    protected void before() {
        UndertowAutoDeserializeService autoDeserialize = Reflection.newProxy(
                UndertowAutoDeserializeService.class, new EchoResourceInvocationHandler());
        UndertowService service = AutoDeserializeServiceEndpoints.of(autoDeserialize);

        ConjureHandler handler = new ConjureHandler();
        UndertowRuntime context = ConjureUndertowRuntime.builder().build();

        service.endpoints(context).forEach(handler::add);

        server = Undertow.builder()
                .addHttpListener(PORT, "0.0.0.0")
                .setHandler(Handlers.path().addPrefixPath("/test/api", handler))
                .build();
        server.start();
    }

    @Override
    protected void after() {
        if (server != null) {
            server.stop();
        }
    }

    public int getLocalPort() {
        return PORT;
    }
}
