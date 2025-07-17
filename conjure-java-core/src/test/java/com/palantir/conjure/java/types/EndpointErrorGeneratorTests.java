/*
 * (c) Copyright 2024 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java.types;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.palantir.conjure.java.Options;
import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.conjure.spec.EndpointDefinition;
import com.palantir.conjure.spec.EndpointError;
import com.palantir.conjure.spec.EndpointName;
import com.palantir.conjure.spec.ErrorCode;
import com.palantir.conjure.spec.ErrorDefinition;
import com.palantir.conjure.spec.ErrorNamespace;
import com.palantir.conjure.spec.ErrorTypeName;
import com.palantir.conjure.spec.HttpMethod;
import com.palantir.conjure.spec.HttpPath;
import com.palantir.conjure.spec.ServiceDefinition;
import com.palantir.conjure.spec.TypeName;
import com.palantir.logsafe.exceptions.SafeIllegalStateException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public final class EndpointErrorGeneratorTests {

    private ConjureDefinition definition;
    private static final String ERROR_NAME = "TestError";

    @BeforeEach
    public void beforeEach() {
        // Create a ConjureDefinition with an error and associate the error with an endpoint
        ErrorDefinition errorDefinition = ErrorDefinition.builder()
                .errorName(TypeName.of(ERROR_NAME, "com.palantir.test"))
                .code(ErrorCode.CUSTOM_SERVER)
                .namespace(ErrorNamespace.of("TestService"))
                .build();

        EndpointDefinition endpoint = EndpointDefinition.builder()
                .endpointName(EndpointName.of("testEndpoint"))
                .httpMethod(HttpMethod.GET)
                .httpPath(HttpPath.of("/test"))
                .errors(EndpointError.builder()
                        .error(ErrorTypeName.builder()
                                .name(ERROR_NAME)
                                .package_("com.palantir.test")
                                .namespace(ErrorNamespace.of("TestService"))
                                .build())
                        .build())
                .build();

        ServiceDefinition service = ServiceDefinition.builder()
                .serviceName(TypeName.of("TestService", "com.palantir.test"))
                .endpoints(List.of(endpoint))
                .build();

        definition = ConjureDefinition.builder()
                .version(1)
                .errors(List.of(errorDefinition))
                .services(List.of(service))
                .build();
    }

    @Test
    public void testThrowsExceptionWhenOptionIsFalseAndEndpointErrorsExist() {
        // Create an EndpointErrorGenerator with the option set to false
        EndpointErrorGenerator generator = new EndpointErrorGenerator(Options.builder()
                .dangerousDoNotUseEnableEndpointAssociatedErrors(false)
                .build());

        // Verify that an exception is thrown, and that it contains the name of the error associated with the endpoint
        assertThatThrownBy(() -> generator.generate(definition))
                .isInstanceOf(SafeIllegalStateException.class)
                .hasMessageContaining("Errors are associated with endpoints. This feature is currently not supported.")
                .hasMessageContaining(ERROR_NAME);
    }

    @Test
    public void testNoExceptionThrownWhenOptionIsTrueAndEndpointErrorsExist() {
        // Create an EndpointErrorGenerator with the option set to true
        EndpointErrorGenerator generator = new EndpointErrorGenerator(Options.builder()
                .dangerousDoNotUseEnableEndpointAssociatedErrors(true)
                .build());

        // Verify that no exception is thrown
        assertThatCode(() -> generator.generate(definition)).doesNotThrowAnyException();
    }
}
