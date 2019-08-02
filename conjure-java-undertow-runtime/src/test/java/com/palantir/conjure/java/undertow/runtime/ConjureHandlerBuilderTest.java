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

import static com.palantir.logsafe.testing.Assertions.assertThatLoggableExceptionThrownBy;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.SafeLoggable;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import com.palantir.logsafe.exceptions.SafeIllegalStateException;
import io.undertow.server.handlers.ResponseCodeHandler;
import io.undertow.util.HttpString;
import io.undertow.util.Methods;
import org.junit.jupiter.api.Test;

public class ConjureHandlerBuilderTest {

    @Test
    public void addOverlappingEndpoints() {
        ConjureHandler.Builder builder = ConjureHandler.builder()
                .endpoints(buildEndpoint(Methods.GET, "/foo/{a}/foo/{b}", "serviceName1", "bar"))
                .endpoints(buildEndpoint(Methods.GET, "/foo/{c}/foo/{d}", "serviceName1", "bar2"))
                .endpoints(buildEndpoint(Methods.GET, "/foo/{e}/foo/{f}", "serviceName2", "bar"))
                .endpoints(buildEndpoint(Methods.POST, "/foo", "serviceName1", "bar"))
                .endpoints(buildEndpoint(Methods.POST, "/foo", "serviceName2", "bar"))
                .endpoints(buildEndpoint(Methods.GET, "/foo2", "serviceName1", "bar"));
        assertThatThrownBy(builder::build)
                .isInstanceOf(SafeIllegalArgumentException.class)
                .hasMessageContaining("The same route is declared by multiple UndertowServices")
                .matches(e -> ((SafeLoggable) e).getArgs().equals(ImmutableList.of(
                        SafeArg.of(
                                "duplicates",
                                ImmutableSet.of(
                                        "GET: /foo/{param}/foo/{param}: "
                                                + "serviceName1.bar, serviceName1.bar2, serviceName2.bar",
                                        "POST: /foo: serviceName1.bar, serviceName2.bar")))));
    }

    @Test
    public void testAddEndpoint_options() {
        assertThatLoggableExceptionThrownBy(() -> ConjureHandler.builder()
                .endpoints(Endpoint.builder()
                        .name("name")
                        .serviceName("service")
                        .handler(ResponseCodeHandler.HANDLE_200)
                        .method(Methods.OPTIONS)
                        .template("/template")
                        .build()))
                .isInstanceOf(SafeIllegalStateException.class)
                .hasLogMessage("Endpoint method is not recognized")
                .containsArgs(SafeArg.of("method", Methods.OPTIONS));
    }

    @Test
    public void testAddEndpoint_trace() {
        assertThatLoggableExceptionThrownBy(() -> ConjureHandler.builder()
                .endpoints(Endpoint.builder()
                        .name("name")
                        .serviceName("service")
                        .handler(ResponseCodeHandler.HANDLE_200)
                        .method(Methods.TRACE)
                        .template("/template")
                        .build()))
                .isInstanceOf(SafeIllegalStateException.class)
                .hasLogMessage("Endpoint method is not recognized")
                .containsArgs(SafeArg.of("method", Methods.TRACE));
    }

    private Endpoint buildEndpoint(HttpString method, String template, String serviceName, String name) {
        return Endpoint.builder()
                .method(method)
                .template(template)
                .serviceName(serviceName)
                .handler(exchange -> { })
                .name(name)
                .build();
    }
}
