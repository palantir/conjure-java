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

import com.palantir.logsafe.Preconditions;
import io.undertow.conduits.ByteActivityCallback;
import io.undertow.conduits.BytesReceivedStreamSourceConduit;
import io.undertow.server.ConduitWrapper;
import io.undertow.server.HttpServerExchange;
import java.util.function.LongSupplier;
import org.xnio.conduits.StreamSourceConduit;

/** Tracks the number of bytes read from HTTP requests. */
final class BytesReadTracker {
    static final ConduitWrapper<StreamSourceConduit> REQUEST_WRAPPER = (factory, exchange) -> {
        BytesReadAccumulator accumulator = new BytesReadAccumulator();
        Preconditions.checkState(
                exchange.putAttachment(Attachments.BYTES_READ, accumulator) == null,
                "Bytes read tracker has already been registered");
        return new BytesReceivedStreamSourceConduit(factory.create(), accumulator);
    };

    static long getBytesRead(HttpServerExchange exchange) {
        LongSupplier longSupplier = exchange.getAttachment(Attachments.BYTES_READ);
        return longSupplier == null ? 0L : longSupplier.getAsLong();
    }

    /** Accumulates bytes read from the request and is attached to the exchange for retrieval. */
    private static final class BytesReadAccumulator implements ByteActivityCallback, LongSupplier {
        private long bytesRead = 0;

        @Override
        public void activity(long bytes) {
            bytesRead += bytes;
        }

        @Override
        public long getAsLong() {
            return bytesRead;
        }
    }

    private BytesReadTracker() {}
}
