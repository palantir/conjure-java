package com.palantir.product;

import com.palantir.conjure.java.undertow.lib.Deserializer;
import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.conjure.java.undertow.lib.Serializer;
import com.palantir.conjure.java.undertow.lib.TypeMarker;
import com.palantir.conjure.java.undertow.lib.UndertowRuntime;
import com.palantir.conjure.java.undertow.lib.UndertowService;
import com.palantir.tokens.auth.AuthHeader;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderMap;
import io.undertow.util.HttpString;
import io.undertow.util.Methods;
import io.undertow.util.PathTemplateMatch;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceInterfaceGenerator")
public interface UndertowNameCollisionService extends UndertowService {
    String int_(
            AuthHeader authHeader,
            String serializer,
            String runtime,
            String authHeader_,
            String long_,
            String delegate,
            String result,
            String deserializer);

    @Override
    default List<Endpoint> endpoints(UndertowRuntime runtime) {
        return Collections.unmodifiableList(Arrays.asList(new IntEndpoint(runtime, this)));
    }

    final class IntEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowNameCollisionService delegate;

        private final Deserializer<String> deserializer;

        private final Serializer<String> serializer;

        IntEndpoint(UndertowRuntime runtime, UndertowNameCollisionService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.deserializer = runtime.bodySerDe().deserializer(new TypeMarker<String>() {});
            this.serializer = runtime.bodySerDe().serializer(new TypeMarker<String>() {});
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            String deserializer_ = deserializer.deserialize(exchange);
            runtime.markers().param("com.palantir.redaction.Safe", "deserializer", deserializer_, exchange);
            Map<String, String> pathParams =
                    exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
            String runtime_ = runtime.plainSerDe().deserializeString(pathParams.get("runtime"));
            runtime.markers().param("com.palantir.redaction.Safe", "runtime", runtime_, exchange);
            HeaderMap headerParams = exchange.getRequestHeaders();
            String serializer_ = runtime.plainSerDe().deserializeString(headerParams.get("Serializer"));
            runtime.markers().param("com.palantir.redaction.Safe", "serializer", serializer_, exchange);
            Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
            String authHeader_ = runtime.plainSerDe().deserializeString(queryParams.get("authHeader"));
            runtime.markers().param("com.palantir.redaction.Safe", "authHeader", authHeader_, exchange);
            String long_ = runtime.plainSerDe().deserializeString(queryParams.get("long"));
            runtime.markers().param("com.palantir.redaction.Safe", "long", long_, exchange);
            String delegate_ = runtime.plainSerDe().deserializeString(queryParams.get("delegate"));
            runtime.markers().param("com.palantir.redaction.Safe", "delegate", delegate_, exchange);
            String result_ = runtime.plainSerDe().deserializeString(queryParams.get("result"));
            runtime.markers().param("com.palantir.redaction.Safe", "result", result_, exchange);
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
}
