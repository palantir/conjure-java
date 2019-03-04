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
import com.palantir.conjure.java.undertow.lib.Service;
import com.palantir.conjure.java.undertow.lib.ServiceContext;
import com.palantir.conjure.java.undertow.runtime.ConjureContext;
import com.palantir.conjure.java.undertow.runtime.ConjureHandler;
import com.palantir.conjure.java.undertow.runtime.ConjureSerializerRegistry;
import com.palantir.conjure.verification.client.AutoDeserializeServiceEndpoints;
import com.palantir.conjure.verification.client.UndertowAutoDeserializeService;
import io.undertow.Handlers;
import io.undertow.Undertow;
import org.junit.rules.ExternalResource;

public final class UndertowServerUnderTestRule extends ExternalResource {

    private static final int PORT = 12346;

    private Undertow server;

    @Override
    protected void before() throws Throwable {
        UndertowAutoDeserializeService service = Reflection.newProxy(
                UndertowAutoDeserializeService.class, new EchoResourceInvocationHandler());
        Service endpoints = AutoDeserializeServiceEndpoints.of(service);

        ConjureHandler handler = new ConjureHandler();
        ServiceContext context = ConjureContext.builder()
                .serializerRegistry(ConjureSerializerRegistry.getDefault())
                .build();

        endpoints.create(context).register(handler);

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
