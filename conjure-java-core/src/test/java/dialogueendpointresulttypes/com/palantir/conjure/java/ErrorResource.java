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

package dialogueendpointresulttypes.com.palantir.conjure.java;

import com.palantir.conjure.java.undertow.lib.BinaryResponseBody;
import com.palantir.tokens.auth.AuthHeader;
import dialogueendpointresulttypes.com.palantir.product.ComplicatedObject;
import dialogueendpointresulttypes.com.palantir.product.EndpointSpecificServerErrors;
import dialogueendpointresulttypes.com.palantir.product.EndpointSpecificServerErrors.EndpointError;
import dialogueendpointresulttypes.com.palantir.product.EndpointSpecificTwoServerErrors;
import dialogueendpointresulttypes.com.palantir.product.EndpointSpecificTwoServerErrors.DifferentNamespace;
import dialogueendpointresulttypes.com.palantir.product.ErrorService;
import dialogueendpointresulttypes.com.palantir.product.OptionalBinaryResponseMode;
import dialogueendpointresulttypes.com.palantir.product.StringAlias;
import dialogueendpointresulttypes.com.palantir.product.TestErrors;
import dialogueendpointresulttypes.com.palantir.product.TestServerErrors;
import dialogueendpointresulttypes.com.palantir.product.TestServerErrors.ComplicatedParameters;
import dialogueendpointresulttypes.com.palantir.product.TestServerErrors.InvalidArgument;
import dialogueendpointresulttypes.com.palantir.product.TestServerErrors.NotFound;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;

interface ErrorResource {
    final class Impl implements ErrorService {
        public static final String SUCCESS = "success!";

        @Override
        public String testNonEndpointAssociatedError(AuthHeader authHeader, boolean shouldThrowError) {
            if (shouldThrowError) {
                throw TestErrors.unassociatedComplicatedParameters(
                        Map.of(1, ComplicatedObject.of(List.of("string"), StringAlias.of("alias"))));
            }
            return SUCCESS;
        }

        @Override
        public String testBasicError(AuthHeader _authHeader, boolean shouldThrowError) throws InvalidArgument {
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

        @SuppressWarnings("for-rollout:StatementSwitchToExpressionSwitch")
        @Override
        public String testMultipleErrorsAndPackages(AuthHeader authHeader, Optional<String> errorToThrow)
                throws InvalidArgument, NotFound, DifferentNamespace,
                        dialogueendpointresulttypes.com.palantir.another.EndpointSpecificServerErrors.DifferentPackage,
                        ComplicatedParameters {
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
                        throw dialogueendpointresulttypes.com.palantir.another.EndpointSpecificServerErrors
                                .differentPackage();
                    case "complicatedParameters":
                        throw TestServerErrors.complicatedParameters(
                                Map.of(1, ComplicatedObject.of(List.of("string"), StringAlias.of("alias"))));
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
