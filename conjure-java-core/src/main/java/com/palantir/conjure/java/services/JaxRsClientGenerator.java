/*
 * (c) Copyright 2019 Palantir Technologies Inc. All rights reserved.
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

import com.palantir.conjure.java.FeatureFlags;
import com.palantir.conjure.spec.ConjureDefinition;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.ParameterizedTypeName;
import java.io.InputStream;
import java.util.Optional;
import java.util.Set;

public final class JaxRsClientGenerator implements ServiceGenerator {

    private final JaxRsServiceFactory delegate;

    public JaxRsClientGenerator(Set<FeatureFlags> featureFlags) {
        this.delegate = new JaxRsServiceFactory(
                getClass(),
                featureFlags,
                serviceName -> serviceName + "JaxRsClient",
                // Clients return InputStream and Optional<InputStream>
                ClassName.get(InputStream.class),
                ParameterizedTypeName.get(Optional.class, InputStream.class),
                // Feign clients do not validate based on javax.validation.constraints
                false);
    }

    @Override
    public Set<JavaFile> generate(ConjureDefinition conjureDefinition) {
        return delegate.generate(conjureDefinition);
    }
}
