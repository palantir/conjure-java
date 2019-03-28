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

package com.palantir.conjure.java.services;

import com.google.common.collect.ImmutableSet;
import com.palantir.conjure.java.FeatureFlags;
import com.palantir.conjure.spec.ConjureDefinition;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

public final class JerseyServiceGenerator implements ServiceGenerator {

    private static final ClassName BINARY_RETURN_TYPE_RESPONSE = ClassName.get(Response.class);
    private static final ClassName BINARY_RETURN_TYPE_OUTPUT = ClassName.get(StreamingOutput.class);
    private static final TypeName OPTIONAL_BINARY_RETURN_TYPE =
            ParameterizedTypeName.get(ClassName.get(Optional.class), BINARY_RETURN_TYPE_OUTPUT);

    private final JaxRsServiceFactory delegate;

    public JerseyServiceGenerator() {
        this(ImmutableSet.of());
    }

    public JerseyServiceGenerator(Set<FeatureFlags> featureFlags) {
        ClassName binaryReturnType = featureFlags.contains(FeatureFlags.JerseyBinaryAsResponse)
                ? BINARY_RETURN_TYPE_RESPONSE : BINARY_RETURN_TYPE_OUTPUT;
        TypeName optionalBinaryReturnType = featureFlags.contains(FeatureFlags.JerseyBinaryAsResponse)
                ? BINARY_RETURN_TYPE_RESPONSE : OPTIONAL_BINARY_RETURN_TYPE;
        boolean requireNotNullAuthAndBodyParams = featureFlags.contains(FeatureFlags.RequireNotNullAuthAndBodyParams);
        this.delegate = new JaxRsServiceFactory(
                getClass(),
                featureFlags,
                Function.identity(),
                binaryReturnType,
                optionalBinaryReturnType,
                requireNotNullAuthAndBodyParams);
    }

    @Override
    public Set<JavaFile> generate(ConjureDefinition conjureDefinition) {
        return delegate.generate(conjureDefinition);
    }
}
