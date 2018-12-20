/*
 * (c) Copyright 2018 Palantir Technologies Inc. All rights reserved.
 */

package com.palantir.conjure.java.undertow.lib;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Streamed binary response data with Content-Type <code>application/octet-stream</code>.
 */
public interface BinaryResponseBody {

    /**
     * Invoked to write data to the response stream. Called exactly once.
     */
    void write(OutputStream responseBody) throws IOException;

}
