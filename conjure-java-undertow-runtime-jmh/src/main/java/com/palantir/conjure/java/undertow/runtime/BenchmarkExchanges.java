/*
 * (c) Copyright 2026 Palantir Technologies Inc. All rights reserved.
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

import io.undertow.io.Receiver;
import io.undertow.io.Sender;
import io.undertow.server.AbstractServerConnection;
import io.undertow.server.BlockingHttpExchange;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.HttpUpgradeListener;
import io.undertow.server.SSLSessionInfo;
import io.undertow.server.ServerConnection;
import io.undertow.util.Methods;
import io.undertow.util.Protocols;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.xnio.OptionMap;
import org.xnio.StreamConnection;
import org.xnio.conduits.StreamSinkConduit;

final class BenchmarkExchanges {
    private static final ServerConnection CONNECTION = new InMemoryConnection();

    private BenchmarkExchanges() {}

    static HttpServerExchange create() {
        HttpServerExchange exchange = new HttpServerExchange(CONNECTION);
        exchange.setProtocol(Protocols.HTTP_1_1);
        exchange.setRequestMethod(Methods.GET);
        exchange.startBlocking(new InMemoryExchange());
        return exchange;
    }

    private static final class InMemoryExchange implements BlockingHttpExchange {
        private final InputStream input = new ByteArrayInputStream(new byte[0]);
        private final ByteArrayOutputStream output = new ByteArrayOutputStream(256);

        @Override
        public InputStream getInputStream() {
            return input;
        }

        @Override
        public OutputStream getOutputStream() {
            return output;
        }

        @Override
        public Sender getSender() {
            throw new UnsupportedOperationException("Benchmark handlers use blocking streams");
        }

        @Override
        public Receiver getReceiver() {
            throw new UnsupportedOperationException("Benchmark handlers use blocking streams");
        }

        @Override
        public void close() throws IOException {
            input.close();
            output.close();
        }
    }

    /** Only the connection-open check is used; accidental access to network operations fails the benchmark. */
    private static final class InMemoryConnection extends AbstractServerConnection {
        InMemoryConnection() {
            super(null, null, null, OptionMap.EMPTY, 0);
        }

        @Override
        public boolean isOpen() {
            return true;
        }

        @Override
        public void close() {
            throw new UnsupportedOperationException("No network connection exists");
        }

        @Override
        public HttpServerExchange sendOutOfBandResponse(HttpServerExchange _exchange) {
            throw new UnsupportedOperationException("No network connection exists");
        }

        @Override
        public boolean isContinueResponseSupported() {
            return false;
        }

        @Override
        public void terminateRequestChannel(HttpServerExchange _exchange) {
            throw new UnsupportedOperationException("No network connection exists");
        }

        @Override
        public SSLSessionInfo getSslSessionInfo() {
            throw new UnsupportedOperationException("No network connection exists");
        }

        @Override
        public void setSslSessionInfo(SSLSessionInfo _sessionInfo) {
            throw new UnsupportedOperationException("No network connection exists");
        }

        @Override
        protected StreamConnection upgradeChannel() {
            throw new UnsupportedOperationException("No network connection exists");
        }

        @Override
        protected StreamSinkConduit getSinkConduit(HttpServerExchange _exchange, StreamSinkConduit _conduit) {
            throw new UnsupportedOperationException("No network connection exists");
        }

        @Override
        protected boolean isUpgradeSupported() {
            return false;
        }

        @Override
        protected boolean isConnectSupported() {
            return false;
        }

        @Override
        protected void exchangeComplete(HttpServerExchange _exchange) {
            throw new UnsupportedOperationException("No network connection exists");
        }

        @Override
        protected void setConnectListener(HttpUpgradeListener _connectListener) {
            throw new UnsupportedOperationException("No network connection exists");
        }

        @Override
        public String getTransportProtocol() {
            return "http/1.1";
        }

        @Override
        public boolean isRequestTrailerFieldsSupported() {
            return false;
        }
    }
}
