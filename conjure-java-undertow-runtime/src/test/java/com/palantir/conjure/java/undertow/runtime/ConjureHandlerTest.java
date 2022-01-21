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

package com.palantir.conjure.java.undertow.runtime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.palantir.conjure.java.undertow.lib.Endpoint;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.util.Methods;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public final class ConjureHandlerTest {

    private static final OkHttpClient client = new OkHttpClient.Builder().build();

    private Undertow server;

    interface Controller {
        int control();
    }

    @Mock
    private Controller wrapperObserver;

    @Mock
    private Controller innerObserver;

    private List<Integer> wrappersBeforeBlockingCallOrder;

    @BeforeEach
    public void before() {
        wrappersBeforeBlockingCallOrder = new ArrayList<>();
        HttpHandler httpHandler = _exchange -> {
            if (wrapperObserver.control() > 0) {
                innerObserver.control();
            }
        };
        HttpHandler handler = ConjureHandler.builder()
                .services(EndpointService.of(Endpoint.builder()
                        .method(Methods.GET)
                        .template("/test")
                        .handler(httpHandler)
                        .serviceName("TestService")
                        .name("test")
                        .handler(httpHandler)
                        .build()))
                .addWrapperBeforeBlocking(endpoint -> Optional.of(exchange -> {
                    if (exchange.isInIoThread()) {
                        wrappersBeforeBlockingCallOrder.add(1);
                        endpoint.handler().handleRequest(exchange);
                    }
                }))
                // check that an empty wrapper is similar in behavior to an identity wrapper
                .addWrapperBeforeBlocking(_endpoint -> Optional.empty())
                .addWrapperBeforeBlocking(endpoint -> Optional.of(exchange -> {
                    if (exchange.isInIoThread()) {
                        wrappersBeforeBlockingCallOrder.add(2);
                        endpoint.handler().handleRequest(exchange);
                    }
                }))
                .build();
        server = Undertow.builder()
                .addHttpListener(12345, "localhost")
                .setHandler(handler)
                .build();
        server.start();
    }

    @AfterEach
    public void after() {
        server.stop();
    }

    @Test
    public void invokesWrapperBeforeBlocking() {
        when(wrapperObserver.control()).thenReturn(1);
        execute();
        // check that the first wrapper (the one that adds 1) is called before the one that adds 2.
        assertThat(wrappersBeforeBlockingCallOrder).containsExactly(1, 2);
        verify(wrapperObserver).control();
        verify(innerObserver).control();
    }

    @Test
    public void invokesInnerWrapper() {
        when(wrapperObserver.control()).thenReturn(1);
        execute();
        verify(wrapperObserver).control();
        verify(innerObserver).control();
    }

    @Test
    public void innerWrapperCanShortCircuit() {
        when(wrapperObserver.control()).thenReturn(-1); // don't call delegate
        execute();
        verify(wrapperObserver).control();
        verify(innerObserver, never()).control();
    }

    private static Response execute() {
        Request request =
                new Request.Builder().get().url("http://localhost:12345/test").build();
        try {
            return client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
