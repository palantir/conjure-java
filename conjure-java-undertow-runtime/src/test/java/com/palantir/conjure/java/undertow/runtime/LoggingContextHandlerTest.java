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

import static org.assertj.core.api.Assertions.assertThat;

import com.palantir.conjure.java.undertow.HttpServerExchanges;
import io.undertow.server.HttpHandler;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

public class LoggingContextHandlerTest {

    @Test
    public void testMdcIsImplemented() {
        // Test to make sure these tests work. If slf4j-simple is on the classpath instead of a fully featured
        // logging framework, MDC operations will all no-op
        MDC.put("foo", "bar");
        assertThat(MDC.getCopyOfContextMap()).isEqualTo(Collections.singletonMap("foo", "bar"));
        MDC.clear();
    }

    @Test
    public void testClearsExistingValues() throws Exception {
        HttpHandler handler = new LoggingContextHandler(exchange ->
                assertThat(MDC.getCopyOfContextMap()).isNullOrEmpty());
        MDC.put("foo", "bar");
        handler.handleRequest(HttpServerExchanges.createStub());
        assertThat(MDC.getCopyOfContextMap()).isNullOrEmpty();
    }

    @Test
    public void testValuesSetInHandlerAreCleared() throws Exception {
        HttpHandler handler = new LoggingContextHandler(exchange -> MDC.put("foo", "bar"));
        handler.handleRequest(HttpServerExchanges.createStub());
        assertThat(MDC.getCopyOfContextMap()).isNullOrEmpty();
    }
}
