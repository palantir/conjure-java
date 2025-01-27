/*
 * (c) Copyright 2025 Palantir Technologies Inc. All rights reserved.
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

import com.palantir.conjure.java.undertow.lib.BinaryResponseBody;
import com.palantir.product.EndpointSpecificServerErrors;
import com.palantir.product.EndpointSpecificServerErrors.EndpointError;
import com.palantir.product.EndpointSpecificTwoServerErrors;
import com.palantir.product.EndpointSpecificTwoServerErrors.DifferentNamespace;
import com.palantir.product.OptionalBinaryResponseMode;
import com.palantir.product.TestServerErrors;
import com.palantir.product.TestServerErrors.InvalidArgument;
import com.palantir.product.TestServerErrors.NotFound;
import com.palantir.product.UndertowErrorService;
import com.palantir.tokens.auth.AuthHeader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

interface ErrorResource {
    class Impl implements UndertowErrorService {
        public static final String SUCCESS = "success!";

        @Override
        public String testBasicError(AuthHeader authHeader, boolean shouldThrowError) throws InvalidArgument {
            if (shouldThrowError) {
                throw TestServerErrors.invalidArgument("field", "value");
            }
            return SUCCESS;
        }

        @Override
        public String testImportedError(AuthHeader authHeader, boolean shouldThrowError) throws EndpointError {
            if (shouldThrowError) {
                throw EndpointSpecificServerErrors.endpointError("typeName", "typeDef");
            }
            return SUCCESS;
        }

        @Override
        public String testMultipleErrorsAndPackages(AuthHeader authHeader, Optional<String> errorToThrow)
                throws InvalidArgument, NotFound, DifferentNamespace,
                        com.palantir.another.EndpointSpecificServerErrors.DifferentPackage {
            if (errorToThrow.isPresent()) {
                String error = errorToThrow.get();
                switch (error) {
                    case "invalidArgument":
                        throw TestServerErrors.invalidArgument("field", "value");
                    case "notFound":
                        throw TestServerErrors.notFound("resource");
                    case "differentNamespace":
                        throw EndpointSpecificTwoServerErrors.differentNamespace();
                    case "differentPackage":
                        throw com.palantir.another.EndpointSpecificServerErrors.differentPackage();
                    default:
                        throw new IllegalArgumentException("Unknown error: " + error);
                }
            }
            return SUCCESS;
        }

        @Override
        public void testEmptyBody(AuthHeader authHeader, boolean shouldThrowError) throws InvalidArgument {
            if (shouldThrowError) {
                throw TestServerErrors.invalidArgument("field", "value");
            }
        }

        @Override
        public BinaryResponseBody testBinary(AuthHeader authHeader, boolean shouldThrowError) throws InvalidArgument {
            if (shouldThrowError) {
                throw TestServerErrors.invalidArgument("field", "value");
            }
            return output -> output.write(SUCCESS.getBytes(StandardCharsets.UTF_8));
        }

        @Override
        public Optional<BinaryResponseBody> testOptionalBinary(AuthHeader authHeader, OptionalBinaryResponseMode mode)
                throws InvalidArgument {
            if (mode.equals(OptionalBinaryResponseMode.ERROR)) {
                throw TestServerErrors.invalidArgument("field", "value");
            } else if (mode.equals(OptionalBinaryResponseMode.PRESENT)) {
                return Optional.of(output -> output.write(SUCCESS.getBytes(StandardCharsets.UTF_8)));
            }
            return Optional.empty();
        }
    }
}
