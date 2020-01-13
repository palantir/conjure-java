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

import com.google.common.collect.Iterables;
import com.google.common.reflect.Reflection;
import com.palantir.conjure.java.undertow.lib.UndertowService;
import com.palantir.conjure.java.undertow.runtime.ConjureHandler;
import com.palantir.conjure.java.undertow.runtime.ConjureUndertowRuntime;
import com.palantir.conjure.verification.client.AutoDeserializeServiceEndpoints;
import com.palantir.conjure.verification.client.UndertowAutoDeserializeService;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import java.net.InetSocketAddress;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public final class UndertowServerUnderTestExtension implements BeforeAllCallback, AfterAllCallback {

    private Undertow server;

    @Override
    public void beforeAll(ExtensionContext _context) {
        UndertowAutoDeserializeService autoDeserialize =
                Reflection.newProxy(UndertowAutoDeserializeService.class, new EchoResourceInvocationHandler());
        UndertowService service = AutoDeserializeServiceEndpoints.of(autoDeserialize);

        HttpHandler handler = ConjureHandler.builder()
                .addAllEndpoints(service.endpoints(ConjureUndertowRuntime.builder().build()))
                .build();

        server = Undertow.builder()
                .addHttpListener(0, "0.0.0.0")
                .setHandler(Handlers.path().addPrefixPath("/test/api", handler))
                .build();
        server.start();
    }

    @Override
    public void afterAll(ExtensionContext _context) {
        if (server != null) {
            server.stop();
        }
    }

    public int getLocalPort() {
        return ((InetSocketAddress) Iterables.getOnlyElement(server.getListenerInfo()).getAddress())
                .getPort();
    }
}
