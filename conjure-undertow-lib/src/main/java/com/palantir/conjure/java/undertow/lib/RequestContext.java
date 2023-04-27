/*
 * (c) Copyright 2021 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java.undertow.lib;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ListMultimap;
import com.palantir.logsafe.Arg;
import com.palantir.logsafe.Unsafe;
import java.security.cert.Certificate;
import java.util.List;
import java.util.Optional;

/**
 * Interface providing a view over data provided by the the original HTTP request including request headers and query
 * parameters. This is a read only interface which should only be implemented by {@code conjure-java-undertow-runtime}.
 */
public interface RequestContext {

    /**
     * Returns the <a href="https://www.rfc-editor.org/rfc/rfc7230#section-5.3">request target</a>.
     *
     * This includes the query string and is not decoded in any way.
     */
    @Unsafe
    String requestTarget();

    /**
     * Returns all values of the header named {@code headerName}. The name is case insensitive. An empty list is
     * returned if no such header exists.
     */
    List<String> header(String headerName);

    /**
     * Returns the value of the first header named {@code headerName}. The name is case insensitive.
     * An {@link Optional#empty()} is returned if no such header exists.
     */
    Optional<String> firstHeader(String headerName);

    /**
     * Returns all query parameters associated with the current request.
     */
    ListMultimap<String, String> queryParameters();

    /**
     * Associates an {@link Arg} with the current request for observability.
     * Implementations may choose to include this data in the request log.
     */
    void requestArg(Arg<?> arg);

    /**
     * Returns the client certificates associated with the connection used to make the current request.
     * If the connection did not use TLS with client certificate authentication, an {@link ImmutableList#of() empty}
     * list will be returned. Otherwise the result is equivalent to
     * {@link javax.net.ssl.SSLSession#getPeerCertificates()}.
     *
     * @see javax.net.ssl.SSLSession#getPeerCertificates()
     */
    ImmutableList<Certificate> peerCertificates();
}
