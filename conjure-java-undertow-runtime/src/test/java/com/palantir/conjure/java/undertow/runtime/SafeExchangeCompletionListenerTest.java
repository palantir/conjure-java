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

import com.palantir.conjure.java.undertow.HttpServerExchanges;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import io.undertow.server.ExchangeCompletionListener;
import io.undertow.server.HttpServerExchange;
import java.util.function.Consumer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SafeExchangeCompletionListenerTest {

    private static final HttpServerExchange exchange = HttpServerExchanges.createStub();

    @Mock
    private ExchangeCompletionListener.NextListener nextListener;

    @Mock
    private Consumer<HttpServerExchange> action;

    @Test
    public void test_normalExecution() {
        ExchangeCompletionListener listener = SafeExchangeCompletionListener.of(action);
        listener.exchangeEvent(exchange, nextListener);
        Mockito.verify(action).accept(exchange);
        Mockito.verify(nextListener).proceed();
        Mockito.verifyNoMoreInteractions(nextListener, action);
    }

    @Test
    public void test_throwsRuntimeException() {
        Mockito.doThrow(new SafeIllegalArgumentException()).when(action).accept(exchange);
        ExchangeCompletionListener listener = SafeExchangeCompletionListener.of(action);
        listener.exchangeEvent(exchange, nextListener);
        Mockito.verify(action).accept(exchange);
        Mockito.verify(nextListener).proceed();
        Mockito.verifyNoMoreInteractions(nextListener, action);
    }

    @Test
    public void test_throwsError() {
        Mockito.doThrow(new NoClassDefFoundError("com.palantir.Example"))
                .when(action)
                .accept(exchange);
        ExchangeCompletionListener listener = SafeExchangeCompletionListener.of(action);
        listener.exchangeEvent(exchange, nextListener);
        Mockito.verify(action).accept(exchange);
        Mockito.verify(nextListener).proceed();
        Mockito.verifyNoMoreInteractions(nextListener, action);
    }

    @Test
    public void test_proceedThrowsRuntimeException() {
        Mockito.doThrow(new SafeIllegalArgumentException()).when(nextListener).proceed();
        ExchangeCompletionListener listener = SafeExchangeCompletionListener.of(action);
        listener.exchangeEvent(exchange, nextListener);
        Mockito.verify(action).accept(exchange);
        Mockito.verify(nextListener).proceed();
        Mockito.verifyNoMoreInteractions(nextListener, action);
    }
}
