package com.palantir.product;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.palantir.conjure.java.undertow.lib.Deserializer;
import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.conjure.java.undertow.lib.RequestContext;
import com.palantir.conjure.java.undertow.lib.Serializer;
import com.palantir.conjure.java.undertow.lib.TypeMarker;
import com.palantir.conjure.java.undertow.lib.UndertowRuntime;
import com.palantir.conjure.java.undertow.lib.UndertowService;
import com.palantir.logsafe.SafeArg;
import com.palantir.tokens.auth.AuthHeader;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderMap;
import io.undertow.util.HttpString;
import io.undertow.util.Methods;
import io.undertow.util.PathTemplateMatch;
import io.undertow.util.StatusCodes;
import java.io.IOException;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceHandlerGenerator")
public final class NameCollisionServiceEndpoints implements UndertowService {
    private final UndertowNameCollisionService delegate;

    private NameCollisionServiceEndpoints(UndertowNameCollisionService delegate) {
        this.delegate = delegate;
    }

    public static UndertowService of(UndertowNameCollisionService delegate) {
        return new NameCollisionServiceEndpoints(delegate);
    }

    @Override
    public List<Endpoint> endpoints(UndertowRuntime runtime) {
        return ImmutableList.of(
                new IntEndpoint(runtime, delegate),
                new NoContextEndpoint(runtime, delegate),
                new ContextEndpoint(runtime, delegate));
    }

    private static final class IntEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowNameCollisionService delegate;

        private final Deserializer<String> deserializer;

        private final Serializer<String> serializer;

        IntEndpoint(UndertowRuntime runtime, UndertowNameCollisionService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.deserializer = runtime.bodySerDe().deserializer(new TypeMarker<String>() {}, this);
            this.serializer = runtime.bodySerDe().serializer(new TypeMarker<String>() {}, this);
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            RequestContext requestContext = runtime.contexts().createContext(exchange, this);
            String deserializer_ = deserializer.deserialize(exchange);
            Map<String, String> pathParams =
                    exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
            String runtime_ = runtime.plainSerDe().deserializeString(pathParams.get("runtime"));
            requestContext.requestArg(SafeArg.of("runtime", runtime_));
            HeaderMap headerParams = exchange.getRequestHeaders();
            String serializer_ = runtime.plainSerDe().deserializeString(headerParams.get("Serializer"));
            requestContext.requestArg(SafeArg.of("serializer", serializer_));
            Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
            String authHeader_ = runtime.plainSerDe().deserializeString(queryParams.get("authHeader"));
            requestContext.requestArg(SafeArg.of("authHeader", authHeader_));
            String long_ = runtime.plainSerDe().deserializeString(queryParams.get("long"));
            requestContext.requestArg(SafeArg.of("long", long_));
            String delegate_ = runtime.plainSerDe().deserializeString(queryParams.get("delegate"));
            requestContext.requestArg(SafeArg.of("delegate", delegate_));
            String result_ = runtime.plainSerDe().deserializeString(queryParams.get("result"));
            requestContext.requestArg(SafeArg.of("result", result_));
            String result = delegate.int_(
                    authHeader, serializer_, runtime_, authHeader_, long_, delegate_, result_, deserializer_);
            serializer.serialize(result, exchange);
        }

        @Override
        public HttpString method() {
            return Methods.POST;
        }

        @Override
        public String template() {
            return "/{runtime}";
        }

        @Override
        public String serviceName() {
            return "NameCollisionService";
        }

        @Override
        public String name() {
            return "int";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class NoContextEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowNameCollisionService delegate;

        private final Deserializer<String> deserializer;

        NoContextEndpoint(UndertowRuntime runtime, UndertowNameCollisionService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.deserializer = runtime.bodySerDe().deserializer(new TypeMarker<String>() {}, this);
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            String requestContext_ = deserializer.deserialize(exchange);
            delegate.noContext(requestContext_);
            exchange.setStatusCode(StatusCodes.NO_CONTENT);
        }

        @Override
        public HttpString method() {
            return Methods.POST;
        }

        @Override
        public String template() {
            return "/no/context";
        }

        @Override
        public String serviceName() {
            return "NameCollisionService";
        }

        @Override
        public String name() {
            return "noContext";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class ContextEndpoint implements HttpHandler, Endpoint {
        private static final ImmutableSet<String> TAGS = ImmutableSet.of("server-request-context");

        private final UndertowRuntime runtime;

        private final UndertowNameCollisionService delegate;

        private final Deserializer<String> deserializer;

        ContextEndpoint(UndertowRuntime runtime, UndertowNameCollisionService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.deserializer = runtime.bodySerDe().deserializer(new TypeMarker<String>() {}, this);
        }

        @Override
        public Set<String> tags() {
            return TAGS;
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            RequestContext requestContext = runtime.contexts().createContext(exchange, this);
            String requestContext_ = deserializer.deserialize(exchange);
            delegate.context(requestContext_, requestContext);
            exchange.setStatusCode(StatusCodes.NO_CONTENT);
        }

        @Override
        public HttpString method() {
            return Methods.POST;
        }

        @Override
        public String template() {
            return "/context";
        }

        @Override
        public String serviceName() {
            return "NameCollisionService";
        }

        @Override
        public String name() {
            return "context";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }
}
