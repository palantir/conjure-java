/*
 * (c) Copyright 2021 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java.undertow.example;

import com.google.common.collect.Iterables;
import com.palantir.conjure.java.undertow.lib.UndertowService;
import com.palantir.conjure.java.undertow.runtime.ConjureHandler;
import com.palantir.conjure.java.undertow.runtime.ConjureUndertowRuntime;
import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import java.net.InetSocketAddress;

public final class TestHelper {

    public static int getPort(Undertow server) {
        InetSocketAddress address = (InetSocketAddress)
                Iterables.getOnlyElement(server.getListenerInfo()).getAddress();
        return address.getPort();
    }

    public static Undertow started(UndertowService service) {
        Undertow server = Undertow.builder()
                .setIoThreads(1)
                .setWorkerThreads(1)
                .setServerOption(UndertowOptions.DECODE_URL, false)
                .addHttpListener(0, "localhost")
                .setHandler(ConjureHandler.builder()
                        .runtime(ConjureUndertowRuntime.builder().build())
                        .services(service)
                        .build())
                .build();
        server.start();
        return server;
    }

    private TestHelper() {}
}
