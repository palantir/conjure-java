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

import com.palantir.logsafe.Preconditions;
import io.undertow.server.ExchangeCompletionListener;
import io.undertow.server.HttpServerExchange;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Safe implementation of {@link ExchangeCompletionListener} which always calls the {@link NextListener} and logs
 * failures.
 */
public final class SafeExchangeCompletionListener implements ExchangeCompletionListener {

    private static final Logger log = LoggerFactory.getLogger(SafeExchangeCompletionListener.class);
    private final Consumer<HttpServerExchange> action;

    private SafeExchangeCompletionListener(Consumer<HttpServerExchange> action) {
        this.action = action;
    }

    public static ExchangeCompletionListener of(Consumer<HttpServerExchange> action) {
        return new SafeExchangeCompletionListener(Preconditions.checkNotNull(action, "An action is required"));
    }

    @Override
    public void exchangeEvent(HttpServerExchange exchange, NextListener nextListener) {
        try {
            action.accept(exchange);
        } catch (RuntimeException | Error e) {
            log.error("ExchangeCompletionListener threw an exception", e);
        } finally {
            nextListener.proceed();
        }
    }
}
