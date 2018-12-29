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

package com.palantir.conjure.java;

import com.google.common.io.ByteStreams;
import com.palantir.conjure.java.undertow.lib.BinaryResponseBody;
import com.palantir.product.UndertowEteBinaryService;
import com.palantir.tokens.auth.AuthHeader;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

final class UndertowBinaryResource implements UndertowEteBinaryService {
    @Override
    public BinaryResponseBody postBinary(AuthHeader authHeader, InputStream body) {
        return output -> output.write(ByteStreams.toByteArray(body));
    }

    @Override
    public Optional<BinaryResponseBody> getOptionalBinaryPresent(AuthHeader authHeader) {
        return Optional.of(out -> out.write("Hello World!".getBytes(StandardCharsets.UTF_8)));
    }

    @Override
    public Optional<BinaryResponseBody> getOptionalBinaryEmpty(AuthHeader authHeader) {
        return Optional.empty();
    }
}
