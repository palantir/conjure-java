package com.palantir.conjure.java.undertow.runtime;

import com.palantir.conjure.java.undertow.lib.Contexts;
import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.conjure.java.undertow.lib.RequestContext;
import com.palantir.logsafe.Arg;
import com.palantir.logsafe.SafeArg;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

final class EndpointRequestArgHandler implements HttpHandler {

    private final Endpoint endpoint;
    private final Contexts contexts;
    private final Arg<String> endpointName;
    private final Arg<String> serviceName;

    EndpointRequestArgHandler(Endpoint endpoint, Contexts contexts) {
        this.endpoint = endpoint;
        this.contexts = contexts;
        this.endpointName = SafeArg.of("_endpointName", endpoint.name());
        this.serviceName = SafeArg.of("_serviceName", endpoint.serviceName());
    }

    /**
     * Adds request args that identify the {@link Endpoint}.
     */
    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        RequestContext requestContext = contexts.createContext(exchange, endpoint);
        requestContext.requestArg(endpointName);
        requestContext.requestArg(serviceName);
        endpoint.handler().handleRequest(exchange);
    }
}
