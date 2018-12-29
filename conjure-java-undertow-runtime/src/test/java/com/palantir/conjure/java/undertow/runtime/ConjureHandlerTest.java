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

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
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

    @Before
    public void before() {

        HttpHandler httpHandler = exchange -> {
            if (wrapperObserver.control() > 0) {
                innerObserver.control();
            }
        };
        ConjureHandler handler = new ConjureHandler();
        handler.get("/test", httpHandler);
        server = Undertow.builder()
                .addHttpListener(12345, "localhost")
                .setHandler(handler)
                .build();
        server.start();
    }

    @After
    public void after() {
        server.stop();
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
        when(wrapperObserver.control()).thenReturn(-1);  // don't call delegate
        execute();
        verify(wrapperObserver).control();
        verify(innerObserver, never()).control();
    }

    private static Response execute() {
        Request request = new Request.Builder()
                .get()
                .url("http://localhost:12345/test")
                .build();
        try {
            return client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
