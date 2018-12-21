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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.undertow.server.HttpServerExchange;
import io.undertow.server.ServerConnection;
import io.undertow.server.protocol.http.HttpServerConnection;
import io.undertow.util.HeaderMap;
import io.undertow.util.Protocols;
import org.xnio.OptionMap;
import org.xnio.StreamConnection;
import org.xnio.XnioIoThread;
import org.xnio.conduits.ConduitStreamSinkChannel;
import org.xnio.conduits.ConduitStreamSourceChannel;
import org.xnio.conduits.StreamSinkConduit;
import org.xnio.conduits.StreamSourceConduit;

public final class HttpServerExchanges {

    private HttpServerExchanges() {}

    public static HttpServerExchange createStub() {
        return createExchange(new HttpServerConnection(createStreamConnection(), null, null, OptionMap.EMPTY, 0, null));
    }

    private static StreamConnection createStreamConnection() {
        StreamConnection streamConnection = mock(StreamConnection.class);
        ConduitStreamSinkChannel sinkChannel = new ConduitStreamSinkChannel(null, mock(StreamSinkConduit.class));
        when(streamConnection.getSinkChannel()).thenReturn(sinkChannel);
        ConduitStreamSourceChannel sourceChannel =
                new ConduitStreamSourceChannel(null, mock(StreamSourceConduit.class));
        when(streamConnection.getSourceChannel()).thenReturn(sourceChannel);
        XnioIoThread ioThread = mock(XnioIoThread.class);
        when(streamConnection.getIoThread()).thenReturn(ioThread);
        return streamConnection;
    }

    private static HttpServerExchange createExchange(ServerConnection connection) {
        HttpServerExchange httpServerExchange =
                new HttpServerExchange(connection, new HeaderMap(), new HeaderMap(), 200);
        httpServerExchange.setProtocol(Protocols.HTTP_1_1);
        return httpServerExchange;
    }
}
