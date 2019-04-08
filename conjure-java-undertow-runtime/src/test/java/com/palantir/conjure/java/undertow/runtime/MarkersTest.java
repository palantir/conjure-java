/*
 * (c) Copyright 2019 Palantir Technologies Inc. All rights reserved.
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

import com.palantir.conjure.java.undertow.HttpServerExchanges;
import com.palantir.conjure.java.undertow.lib.UndertowRuntime;
import io.undertow.server.HttpServerExchange;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MarkersTest {

    @Mock
    private ParamMarker paramMarker;
    private HttpServerExchange exchange;
    private UndertowRuntime runtime;

    @Before
    public void before() {
        exchange = HttpServerExchanges.createStub();
        runtime = ConjureUndertowRuntime.builder()
                .paramMarker(paramMarker)
                .build();
    }

    @Test
    public void testMarkersIsCalled() {
        runtime.markers().param(MarkersTest.class, "foo", "bar", exchange);
        Mockito.verify(paramMarker).mark(MarkersTest.class, "foo", "bar", exchange);
    }
}
