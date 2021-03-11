package com.palantir.product;

import com.google.common.collect.ImmutableList;
import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.conjure.java.undertow.lib.UndertowRuntime;
import com.palantir.conjure.java.undertow.lib.UndertowService;
import com.palantir.tokens.auth.AuthHeader;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import io.undertow.util.Methods;
import io.undertow.util.StatusCodes;
import java.io.IOException;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceHandlerGenerator")
public final class AliasTestServiceEndpoints implements UndertowService {
    private final UndertowAliasTestService delegate;

    private AliasTestServiceEndpoints(UndertowAliasTestService delegate) {
        this.delegate = delegate;
    }

    public static UndertowService of(UndertowAliasTestService delegate) {
        return new AliasTestServiceEndpoints(delegate);
    }

    @Override
    public List<Endpoint> endpoints(UndertowRuntime runtime) {
        return ImmutableList.of(new TestOptionalAliasQueryParamsEndpoint(runtime, delegate));
    }

    private static final class TestOptionalAliasQueryParamsEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowAliasTestService delegate;

        TestOptionalAliasQueryParamsEndpoint(UndertowRuntime runtime, UndertowAliasTestService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
            OptionalInt optionalAliasIntRaw =
                    runtime.plainSerDe().deserializeOptionalInteger(queryParams.get("optionalAliasInt"));
            Optional<AliasedInteger> optionalAliasInt = Optional.ofNullable(
                    optionalAliasIntRaw.isPresent() ? AliasedInteger.of(optionalAliasIntRaw.getAsInt()) : null);
            OptionalDouble optionalAliasDoubleRaw =
                    runtime.plainSerDe().deserializeOptionalDouble(queryParams.get("optionalAliasDouble"));
            Optional<AliasedDouble> optionalAliasDouble = Optional.ofNullable(
                    optionalAliasDoubleRaw.isPresent() ? AliasedDouble.of(optionalAliasDoubleRaw.getAsDouble()) : null);
            Optional<SafeLong> optionalAliasSafeLongRaw =
                    runtime.plainSerDe().deserializeOptionalSafeLong(queryParams.get("optionalAliasSafeLong"));
            Optional<AliasedSafeLong> optionalAliasSafeLong = Optional.ofNullable(
                    optionalAliasSafeLongRaw.isPresent() ? AliasedSafeLong.of(optionalAliasSafeLongRaw.get()) : null);
            Optional<Boolean> optionalAliasBooleanRaw =
                    runtime.plainSerDe().deserializeOptionalBoolean(queryParams.get("optionalAliasBoolean"));
            Optional<AliasedBoolean> optionalAliasBoolean = Optional.ofNullable(
                    optionalAliasBooleanRaw.isPresent() ? AliasedBoolean.of(optionalAliasBooleanRaw.get()) : null);
            delegate.testOptionalAliasQueryParams(
                    authHeader, optionalAliasInt, optionalAliasDouble, optionalAliasSafeLong, optionalAliasBoolean);
            exchange.setStatusCode(StatusCodes.NO_CONTENT);
        }

        @Override
        public HttpString method() {
            return Methods.GET;
        }

        @Override
        public String template() {
            return "/alias/test";
        }

        @Override
        public String serviceName() {
            return "AliasTestService";
        }

        @Override
        public String name() {
            return "testOptionalAliasQueryParams";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }
}
