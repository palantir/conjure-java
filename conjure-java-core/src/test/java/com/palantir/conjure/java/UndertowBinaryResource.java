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

import com.google.common.base.Strings;
import com.google.common.io.ByteStreams;
import com.palantir.conjure.java.api.errors.ErrorType;
import com.palantir.conjure.java.api.errors.ServiceException;
import com.palantir.conjure.java.undertow.lib.BinaryResponseBody;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeRuntimeException;
import com.palantir.product.UndertowEteBinaryService;
import com.palantir.tokens.auth.AuthHeader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Random;
import javax.validation.constraints.NotNull;

final class UndertowBinaryResource implements UndertowEteBinaryService {
    @Override
    public BinaryResponseBody postBinary(AuthHeader _authHeader, InputStream body) {
        return output -> output.write(ByteStreams.toByteArray(body));
    }

    @Override
    public BinaryResponseBody postBinaryThrows(
            @NotNull AuthHeader authHeader, int bytesToRead, @NotNull InputStream body) {
        if (bytesToRead > 0) {
            try {
                ByteStreams.exhaust(ByteStreams.limit(body, bytesToRead));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        throw new ServiceException(
                ErrorType.INVALID_ARGUMENT, SafeArg.of("large", Strings.repeat("Hello, World!", 1024 * 1024)));
    }

    @Override
    public Optional<BinaryResponseBody> getOptionalBinaryPresent(AuthHeader _authHeader) {
        return Optional.of(out -> out.write("Hello World!".getBytes(StandardCharsets.UTF_8)));
    }

    @Override
    public Optional<BinaryResponseBody> getOptionalBinaryEmpty(AuthHeader _authHeader) {
        return Optional.empty();
    }

    @Override
    public BinaryResponseBody getBinaryFailure(AuthHeader _authHeader, int numBytes) {
        return responseBody -> {
            byte[] data = new byte[numBytes];
            new Random().nextBytes(data);
            responseBody.write(data);
            throw new SafeRuntimeException("failure");
        };
    }
}
