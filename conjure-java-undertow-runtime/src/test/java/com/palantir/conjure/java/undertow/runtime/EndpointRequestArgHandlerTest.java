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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import com.palantir.conjure.java.undertow.HttpServerExchanges;
import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.logsafe.SafeArg;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.ResponseCodeHandler;
import io.undertow.util.Methods;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
final class EndpointRequestArgHandlerTest {

    @Mock
    private RequestArgHandler requestArgHandler;

    @Test
    void testEndpointArgs() throws Exception {
        Endpoint endpoint = Endpoint.builder()
                .name("name")
                .serviceName("service")
                .handler(ResponseCodeHandler.HANDLE_200)
                .method(Methods.OPTIONS)
                .template("/template")
                .build();
        HttpHandler handler = new EndpointRequestArgHandler(endpoint, new ConjureContexts(requestArgHandler));
        handler.handleRequest(HttpServerExchanges.createStub());

        verify(requestArgHandler).arg(any(), eq(SafeArg.of("_endpointName", "name")));
        verify(requestArgHandler).arg(any(), eq(SafeArg.of("_serviceName", "service")));
    }
}
