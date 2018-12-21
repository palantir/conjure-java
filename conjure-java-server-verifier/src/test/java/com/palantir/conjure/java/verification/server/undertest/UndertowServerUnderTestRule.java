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
import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.conjure.java.undertow.lib.HandlerContext;
import com.palantir.conjure.java.undertow.lib.RoutingRegistry;
import com.palantir.conjure.java.undertow.lib.SerializerRegistry;
import com.palantir.conjure.java.undertow.runtime.ConjureRoutingRegistry;
import com.palantir.conjure.java.undertow.runtime.Serializers;
import com.palantir.conjure.verification.client.AutoDeserializeServiceEndpoint;
import com.palantir.conjure.verification.client.UndertowAutoDeserializeService;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.RoutingHandler;
import org.junit.rules.ExternalResource;

public final class UndertowServerUnderTestRule extends ExternalResource {

    private static final int PORT = 12346;

    private Undertow server;

    @Override
    protected void before() throws Throwable {
        UndertowAutoDeserializeService service = Reflection.newProxy(
                UndertowAutoDeserializeService.class, new EchoResourceInvocationHandler());
        Endpoint endpoint = AutoDeserializeServiceEndpoint.of(service);

        RoutingHandler routingHandler = Handlers.routing();

        RoutingRegistry routingRegistry = new ConjureRoutingRegistry(routingHandler);
        SerializerRegistry serializers = new SerializerRegistry(Serializers.json(), Serializers.cbor());
        HandlerContext context = HandlerContext.builder()
                .serializerRegistry(serializers)
                .build();

        endpoint.create(context).register(routingRegistry);

        server = Undertow.builder()
                .addHttpListener(PORT, "0.0.0.0")
                .setHandler(Handlers.path().addPrefixPath("/test/api", routingHandler))
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
