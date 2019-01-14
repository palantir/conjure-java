package com.palantir.product;

import com.google.common.reflect.TypeToken;
import com.palantir.conjure.java.undertow.lib.BinaryResponseBody;
import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.conjure.java.undertow.lib.HandlerContext;
import com.palantir.conjure.java.undertow.lib.Routable;
import com.palantir.conjure.java.undertow.lib.RoutingRegistry;
import com.palantir.conjure.java.undertow.lib.SerializerRegistry;
import com.palantir.conjure.java.undertow.lib.internal.Auth;
import com.palantir.conjure.java.undertow.lib.internal.BinarySerializers;
import com.palantir.conjure.java.undertow.lib.internal.StringDeserializers;
import com.palantir.tokens.auth.AuthHeader;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderMap;
import io.undertow.util.PathTemplateMatch;
import java.io.IOException;
import java.util.Deque;
import java.util.Map;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceHandlerGenerator")
public final class DialogueSampleServiceEndpoint implements Endpoint {
    private final UndertowDialogueSampleService delegate;

    private DialogueSampleServiceEndpoint(UndertowDialogueSampleService delegate) {
        this.delegate = delegate;
    }

    public static Endpoint of(UndertowDialogueSampleService delegate) {
        return new DialogueSampleServiceEndpoint(delegate);
    }

    @Override
    public Routable create(HandlerContext context) {
        return new DialogueSampleServiceRoutable(context, delegate);
    }

    private static final class DialogueSampleServiceRoutable implements Routable {
        private final UndertowDialogueSampleService delegate;

        private final SerializerRegistry serializers;

        private DialogueSampleServiceRoutable(
                HandlerContext context, UndertowDialogueSampleService delegate) {
            this.serializers = context.serializerRegistry();
            this.delegate = delegate;
        }

        @Override
        public void register(RoutingRegistry routingRegistry) {
            routingRegistry
                    .get("/base/string", new StringHandler())
                    .get("/base/stringEcho", new StringEchoHandler())
                    .get("/base/integer", new IntegerHandler())
                    .get("/base/integerEcho/{integer}", new IntegerEchoHandler())
                    .get("/base/queryEcho", new QueryEchoHandler())
                    .get("/base/complex", new ComplexHandler())
                    .post("/base/complexEcho", new ComplexEchoHandler())
                    .post("/base/binaryEcho", new BinaryEchoHandler());
        }

        private class StringHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = Auth.header(exchange);
                String result = delegate.string(authHeader);
                serializers.serialize(result, exchange);
            }
        }

        private class StringEchoHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = Auth.header(exchange);
                HeaderMap headerParams = exchange.getRequestHeaders();
                String string =
                        StringDeserializers.deserializeString(headerParams.get("Header-String"));
                String result = delegate.stringEcho(authHeader, string);
                serializers.serialize(result, exchange);
            }
        }

        private class IntegerHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = Auth.header(exchange);
                int result = delegate.integer(authHeader);
                serializers.serialize(result, exchange);
            }
        }

        private class IntegerEchoHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = Auth.header(exchange);
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int integer = StringDeserializers.deserializeInteger(pathParams.get("integer"));
                int result = delegate.integerEcho(authHeader, integer);
                serializers.serialize(result, exchange);
            }
        }

        private class QueryEchoHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = Auth.header(exchange);
                Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
                int integer = StringDeserializers.deserializeInteger(queryParams.get("queryParam"));
                String result = delegate.queryEcho(authHeader, integer);
                serializers.serialize(result, exchange);
            }
        }

        private class ComplexHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = Auth.header(exchange);
                Complex result = delegate.complex(authHeader);
                serializers.serialize(result, exchange);
            }
        }

        private class ComplexEchoHandler implements HttpHandler {
            private final TypeToken<Complex> complexType = new TypeToken<Complex>() {};

            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = Auth.header(exchange);
                Complex complex = serializers.deserialize(complexType, exchange);
                Complex result = delegate.complexEcho(authHeader, complex);
                serializers.serialize(result, exchange);
            }
        }

        private class BinaryEchoHandler implements HttpHandler {
            private final TypeToken<String> stringType = new TypeToken<String>() {};

            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = Auth.header(exchange);
                String string = serializers.deserialize(stringType, exchange);
                BinaryResponseBody result = delegate.binaryEcho(authHeader, string);
                BinarySerializers.serialize(result, exchange);
            }
        }
    }
}
