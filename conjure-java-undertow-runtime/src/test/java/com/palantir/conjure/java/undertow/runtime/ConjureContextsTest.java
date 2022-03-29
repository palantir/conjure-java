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

package com.palantir.conjure.java.undertow.runtime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.conjure.java.undertow.lib.RequestContext;
import io.undertow.server.HttpServerExchange;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ConjureContextsTest {

    @Mock
    private RequestArgHandler requestArgHandler;

    @Mock
    private HttpServerExchange exchange;

    @Mock
    private Endpoint endpoint;

    private ConjureContexts conjureContexts;
    private RequestContext requestContext;

    @BeforeEach
    private void beforeEach() {
        conjureContexts = new ConjureContexts(requestArgHandler);
        requestContext = conjureContexts.createContext(exchange, endpoint);
    }

    @Test
    public void testSourceAddress() throws UnknownHostException {
        // Should return an IP address for resolved addresses.
        when(exchange.getSourceAddress()).thenReturn(new InetSocketAddress(InetAddress.getByName("172.18.0.1"), 8080));
        assertThat(requestContext.sourceAddress()).isEqualTo("172.18.0.1");
        when(exchange.getSourceAddress()).thenReturn(new InetSocketAddress(InetAddress.getByName("localhost"), 8080));
        assertThat(requestContext.sourceAddress()).isEqualTo("127.0.0.1");

        // Should return a hostname for unresolved addresses.
        when(exchange.getSourceAddress()).thenReturn(InetSocketAddress.createUnresolved("localhost", 8080));
        assertThat(requestContext.sourceAddress()).isEqualTo("localhost");
    }
}
