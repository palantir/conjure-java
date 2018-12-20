/*
 * (c) Copyright 2018 Palantir Technologies Inc. All rights reserved.
 */

package com.palantir.conjure.java.undertow.lib.internal;

import com.palantir.conjure.java.undertow.lib.BinaryResponseBody;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import java.io.IOException;
import java.io.InputStream;

public final class BinarySerializers {

    private static final String CONTENT_TYPE = "application/octet-stream";

    public static void serialize(BinaryResponseBody value, HttpServerExchange exchange) throws IOException {
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, CONTENT_TYPE);
        value.write(exchange.getOutputStream());
    }

    public static InputStream deserializeInputStream(HttpServerExchange exchange) {
        String contentType = exchange.getRequestHeaders().getFirst(Headers.CONTENT_TYPE);
        if (contentType == null) {
            throw new SafeIllegalArgumentException("Request is missing Content-Type header");
        }
        if (!contentType.startsWith(CONTENT_TYPE)) {
            throw new SafeIllegalArgumentException("Unsupported Content-Type",
                    SafeArg.of("Content-Type", contentType));
        }
        return exchange.getInputStream();
    }

    private BinarySerializers() {}
}
