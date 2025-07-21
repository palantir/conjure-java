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
import org.junit.jupiter.api.Test;

public final class EndpointErrorGeneratorTests {
    private static final String ERROR_NAME = "TestError";
    private static final EndpointDefinition ENDPOINT_DEF = EndpointDefinition.builder()
            .endpointName(EndpointName.of("testEndpoint"))
            .httpMethod(HttpMethod.GET)
            .httpPath(HttpPath.of("/test"))
            .build();

    private static final EndpointDefinition ENDPOINT_DEF_WITH_ERROR = EndpointDefinition.builder()
            .from(ENDPOINT_DEF)
            .errors(EndpointError.builder()
                    .error(ErrorTypeName.builder()
                            .name(ERROR_NAME)
                            .package_("com.palantir.test")
                            .namespace(ErrorNamespace.of("TestService"))
                            .build())
                    .build())
            .build();

    private static final ServiceDefinition SERVICE_DEF = ServiceDefinition.builder()
            .serviceName(TypeName.of("TestService", "com.palantir.test"))
            .endpoints(List.of(ENDPOINT_DEF))
            .build();

    private static final ServiceDefinition SERVICE_DEF_WITH_ENDPOINT_ERROR = ServiceDefinition.builder()
            .from(SERVICE_DEF)
            .endpoints(List.of(ENDPOINT_DEF_WITH_ERROR))
            .build();

    private static final ConjureDefinition DEFINITION = ConjureDefinition.builder()
            .version(1)
            .services(List.of(ServiceDefinition.builder()
                    .serviceName(TypeName.of("TestService", "com.palantir.test"))
                    .endpoints(List.of(ENDPOINT_DEF))
                    .build()))
            .build();

    private static final ConjureDefinition DEFINITION_WITH_ENDPOINT_ERROR = ConjureDefinition.builder()
            .from(DEFINITION)
            .errors(List.of(ErrorDefinition.builder()
                    .errorName(TypeName.of(ERROR_NAME, "com.palantir.test"))
                    .code(ErrorCode.CUSTOM_SERVER)
                    .namespace(ErrorNamespace.of("TestService"))
                    .build()))
            .services(List.of(SERVICE_DEF_WITH_ENDPOINT_ERROR))
            .build();

    @Test
    public void testThrowsExceptionWhenOptionIsFalseAndEndpointErrorsExist() {
        // Create an EndpointErrorGenerator with the option set to false
        EndpointErrorGenerator generator = new EndpointErrorGenerator(Options.builder()
                .dangerousDoNotUseEnableEndpointAssociatedErrors(false)
                .build());

        // Verify that an exception is thrown, and that it contains the name of the error associated with the endpoint
        assertThatThrownBy(() -> generator.generate(DEFINITION_WITH_ENDPOINT_ERROR))
                .isInstanceOf(SafeIllegalStateException.class)
                .hasMessageContaining(EndpointErrorGenerator.NOT_SUPPORTED_ERROR_MESSAGE)
                .hasMessageContaining(ERROR_NAME);
    }

    @Test
    public void testNoExceptionThrownWhenOptionIsTrueAndEndpointErrorsExist() {
        // Create an EndpointErrorGenerator with the option set to true
        EndpointErrorGenerator generator = new EndpointErrorGenerator(Options.builder()
                .dangerousDoNotUseEnableEndpointAssociatedErrors(true)
                .build());

        // Verify that no exception is thrown
        assertThatCode(() -> generator.generate(DEFINITION_WITH_ENDPOINT_ERROR)).doesNotThrowAnyException();
    }

    @Test
    public void testNoExceptionThrownWhenOptionIsFalseeAndNoEndpointErrorsExist() {
        // Create an EndpointErrorGenerator with the option set to true
        EndpointErrorGenerator generator = new EndpointErrorGenerator(Options.builder()
                .dangerousDoNotUseEnableEndpointAssociatedErrors(false)
                .build());

        // Verify that no exception is thrown
        assertThatCode(() -> generator.generate(DEFINITION)).doesNotThrowAnyException();
    }
}
