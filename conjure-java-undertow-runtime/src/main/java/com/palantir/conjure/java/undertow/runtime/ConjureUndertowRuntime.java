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

import com.google.common.collect.ImmutableList;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.palantir.conjure.java.undertow.lib.AsyncRequestProcessing;
import com.palantir.conjure.java.undertow.lib.AuthorizationExtractor;
import com.palantir.conjure.java.undertow.lib.BodySerDe;
import com.palantir.conjure.java.undertow.lib.Contexts;
import com.palantir.conjure.java.undertow.lib.ExceptionHandler;
import com.palantir.conjure.java.undertow.lib.MarkerCallback;
import com.palantir.conjure.java.undertow.lib.PlainSerDe;
import com.palantir.conjure.java.undertow.lib.UndertowRuntime;
import com.palantir.logsafe.Preconditions;
import com.palantir.tokens.auth.AuthHeader;
import com.palantir.tokens.auth.BearerToken;
import com.palantir.tokens.auth.UnverifiedJsonWebToken;
import io.undertow.server.HttpServerExchange;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

/** {@link ConjureUndertowRuntime} provides functionality required by generated handlers. */
public final class ConjureUndertowRuntime implements UndertowRuntime {

    private final BodySerDe bodySerDe;
    private final AuthorizationExtractor auth;
    private final MarkerCallback markerCallback;
    private final AsyncRequestProcessing async;
    private final ExceptionHandler exceptionHandler;
    private final Contexts contexts;

    private ConjureUndertowRuntime(Builder builder) {
        this.bodySerDe = new ConjureBodySerDe(
                builder.encodings.isEmpty()
                        ? ImmutableList.of(Encodings.json(), Encodings.smile(), Encodings.cbor())
                        : builder.encodings);
        this.auth = new OnSetRequestTokenAuthExtractor(
                new ConjureAuthorizationExtractor(plainSerDe()), builder.onSetRequestToken);
        this.exceptionHandler = builder.exceptionHandler;
        this.markerCallback = MarkerCallbacks.fold(builder.paramMarkers);
        this.async = new ConjureAsyncRequestProcessing(builder.asyncTimeout, builder.exceptionHandler);
        this.contexts = new ConjureContexts(builder.requestArgHandler);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public BodySerDe bodySerDe() {
        return bodySerDe;
    }

    @Override
    public PlainSerDe plainSerDe() {
        return ConjurePlainSerDe.INSTANCE;
    }

    @Override
    public MarkerCallback markers() {
        return markerCallback;
    }

    @Override
    public AuthorizationExtractor auth() {
        return auth;
    }

    @Override
    public AsyncRequestProcessing async() {
        return async;
    }

    @Override
    public ExceptionHandler exceptionHandler() {
        return exceptionHandler;
    }

    @Override
    public Contexts contexts() {
        return contexts;
    }

    public static final class Builder {

        private Duration asyncTimeout = Duration.ofMinutes(3);
        private ExceptionHandler exceptionHandler = ConjureExceptions.INSTANCE;
        private RequestArgHandler requestArgHandler = DefaultRequestArgHandler.INSTANCE;
        private BiConsumer<HttpServerExchange, Optional<UnverifiedJsonWebToken>> onSetRequestToken =
                (_exchange, _token) -> {};
        private final List<Encoding> encodings = new ArrayList<>();
        private final List<ParamMarker> paramMarkers = new ArrayList<>();

        private Builder() {}

        @CanIgnoreReturnValue
        public Builder asyncTimeout(Duration value) {
            asyncTimeout = Preconditions.checkNotNull(value, "asyncTimeout is required");
            return this;
        }

        @CanIgnoreReturnValue
        public Builder encodings(Encoding value) {
            encodings.add(Preconditions.checkNotNull(value, "encoding is required"));
            return this;
        }

        @CanIgnoreReturnValue
        public Builder paramMarker(ParamMarker value) {
            paramMarkers.add(Preconditions.checkNotNull(value, "paramMarker is required"));
            return this;
        }

        @CanIgnoreReturnValue
        public Builder exceptionHandler(ExceptionHandler value) {
            exceptionHandler = Preconditions.checkNotNull(value, "exceptionHandler is required");
            return this;
        }

        @CanIgnoreReturnValue
        public Builder requestArgHandler(RequestArgHandler value) {
            requestArgHandler = Preconditions.checkNotNull(value, "requestLogParameterHandler is required");
            return this;
        }

        @CanIgnoreReturnValue
        public Builder onSetRequestToken(BiConsumer<HttpServerExchange, Optional<UnverifiedJsonWebToken>> callback) {
            onSetRequestToken = callback;
            return this;
        }

        public ConjureUndertowRuntime build() {
            return new ConjureUndertowRuntime(this);
        }
    }

    private static final class OnSetRequestTokenAuthExtractor implements AuthorizationExtractor {
        private final AuthorizationExtractor delegate;
        private final BiConsumer<HttpServerExchange, Optional<UnverifiedJsonWebToken>> onSetRequestToken;

        private OnSetRequestTokenAuthExtractor(
                AuthorizationExtractor delegate,
                BiConsumer<HttpServerExchange, Optional<UnverifiedJsonWebToken>> onSetRequestToken) {
            this.delegate = delegate;
            this.onSetRequestToken = onSetRequestToken;
        }

        @Override
        public AuthHeader header(HttpServerExchange exchange) {
            AuthHeader result = delegate.header(exchange);
            Optional<UnverifiedJsonWebToken> token = exchange.getAttachment(Attachments.UNVERIFIED_JWT);
            Preconditions.checkNotNull(token, "jwt in exchange attachment is null");
            onSetRequestToken.accept(exchange, token);
            return result;
        }

        @Override
        public BearerToken cookie(HttpServerExchange exchange, String cookieName) {
            BearerToken result = delegate.cookie(exchange, cookieName);
            Optional<UnverifiedJsonWebToken> token = exchange.getAttachment(Attachments.UNVERIFIED_JWT);
            Preconditions.checkNotNull(token, "jwt in exchange attachment is null");
            onSetRequestToken.accept(exchange, token);
            return result;
        }

        @Override
        public void setRequestToken(HttpServerExchange exchange, Optional<UnverifiedJsonWebToken> token) {
            delegate.setRequestToken(exchange, token);
            // may throw
            onSetRequestToken.accept(exchange, token);
        }
    }
}
