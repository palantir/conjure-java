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

package com.palantir.conjure.java.undertow;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;
import io.netty.buffer.ByteBuf;
import io.netty.util.concurrent.EventExecutor;
import io.undertow.httpcore.BufferAllocator;
import io.undertow.httpcore.DefaultBlockingHttpExchange;
import io.undertow.httpcore.HttpExchange;
import io.undertow.httpcore.HttpExchangeBase;
import io.undertow.httpcore.InputChannel;
import io.undertow.httpcore.IoCallback;
import io.undertow.httpcore.SSLSessionInfo;
import io.undertow.httpcore.UndertowOptionMap;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Protocols;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public final class HttpServerExchanges {

    private HttpServerExchanges() {}

    @SuppressWarnings("checkstyle:MethodLength")
    public static HttpServerExchange createStub() {
        HttpExchange exchange = new HttpExchangeBase() {

            private ListMultimap<String, String> requestHeaders =
                    Multimaps.newListMultimap(new TreeMap<>(String.CASE_INSENSITIVE_ORDER), () -> new ArrayList<>(1));
            private ListMultimap<String, String> responseHeaders =
                    Multimaps.newListMultimap(new TreeMap<>(String.CASE_INSENSITIVE_ORDER), () -> new ArrayList<>(1));
            private int statusCode = 200;
            private String method = "GET";
            private UndertowOptionMap options = UndertowOptionMap.EMPTY;

            @Override
            public BufferAllocator getBufferAllocator() {
                return null;
            }

            @Override
            public HttpExchange setStatusCode(int code) {
                statusCode = code;
                return this;
            }

            @Override
            public int getStatusCode() {
                return statusCode;
            }

            @Override
            public String getRequestHeader(String name) {
                return Iterables.getFirst(requestHeaders.get(name), null);
            }

            @Override
            public List<String> getRequestHeaders(String name) {
                return requestHeaders.get(name);
            }

            @Override
            public boolean containsRequestHeader(String name) {
                return requestHeaders.containsKey(name);
            }

            @Override
            public void removeRequestHeader(String name) {
                requestHeaders.removeAll(name);
            }

            @Override
            public void setRequestHeader(String name, String value) {
                requestHeaders.replaceValues(name, ImmutableList.of(value));
            }

            @Override
            public Collection<String> getRequestHeaderNames() {
                return requestHeaders.keySet();
            }

            @Override
            public void addRequestHeader(String name, String value) {
                requestHeaders.put(name, value);
            }

            @Override
            public void clearRequestHeaders() {
                requestHeaders.clear();
            }

            @Override
            public List<String> getResponseHeaders(String name) {
                return responseHeaders.get(name);
            }

            @Override
            public boolean containsResponseHeader(String name) {
                return responseHeaders.containsKey(name);
            }

            @Override
            public void removeResponseHeader(String name) {
                responseHeaders.removeAll(name);
            }

            @Override
            public void setResponseHeader(String name, String value) {
                responseHeaders.replaceValues(name, ImmutableList.of(value));
            }

            @Override
            public Collection<String> getResponseHeaderNames() {
                return responseHeaders.keySet();
            }

            @Override
            public void addResponseHeader(String name, String value) {
                responseHeaders.put(name, value);
            }

            @Override
            public void clearResponseHeaders() {
                responseHeaders.clear();
            }

            @Override
            public String getResponseHeader(String name) {
                return Iterables.getFirst(responseHeaders.get(name), null);
            }

            @Override
            public String getRequestMethod() {
                return method;
            }

            @Override
            public String getRequestScheme() {
                return "https";
            }

            @Override
            public String getRequestURI() {
                return null;
            }

            @Override
            public String getProtocol() {
                return "HTTP/1.1";
            }

            @Override
            public boolean isInIoThread() {
                return false;
            }

            @Override
            public boolean isHttp2() {
                return false;
            }

            @Override
            public InputChannel getInputChannel() {
                return null;
            }

            @Override
            public InetSocketAddress getDestinationAddress() {
                return null;
            }

            @Override
            public InetSocketAddress getSourceAddress() {
                return null;
            }

            @Override
            public void close() {}

            @Override
            public EventExecutor getIoThread() {
                return null;
            }

            @Override
            public void setUpgradeListener(Consumer<Object> _listener) {}

            @Override
            public Executor getWorker() {
                return null;
            }

            @Override
            public UndertowOptionMap getUndertowOptions() {
                return options;
            }

            @Override
            public void setUndertowOptions(UndertowOptionMap value) {
                this.options = value;
            }

            @Override
            public void sendContinue() {}

            @Override
            public void discardRequest() {}

            @Override
            public boolean isUpgradeSupported() {
                return false;
            }

            @Override
            public SSLSessionInfo getSslSessionInfo() {
                return null;
            }

            @Override
            public boolean isIoOperationQueued() {
                return false;
            }

            @Override
            public void setMaxEntitySize(long _maxEntitySize) {}

            @Override
            public long getMaxEntitySize() {
                return -1L;
            }

            @Override
            public void setReadTimeout(long _readTimeoutMs) {}

            @Override
            public long getReadTimeout() {
                return -1L;
            }

            @Override
            protected <T> void writeAsync0(ByteBuf _data, boolean _last, IoCallback<T> _callback, T _context) {}

            @Override
            protected void writeBlocking0(ByteBuf _data, boolean _last) throws IOException {}
        };
        return createExchange(exchange);
    }

    private static HttpServerExchange createExchange(HttpExchange exchange) {
        HttpServerExchange httpServerExchange = new HttpServerExchange(exchange, -1);
        httpServerExchange.protocol(Protocols.HTTP_1_1_STRING);
        httpServerExchange.requestMethod("GET");
        httpServerExchange.startBlocking(new DefaultBlockingHttpExchange(httpServerExchange.getDelegate()));
        return httpServerExchange;
    }
}
