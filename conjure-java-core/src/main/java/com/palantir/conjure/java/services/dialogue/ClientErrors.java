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

package com.palantir.conjure.java.services.dialogue;

import com.palantir.conjure.java.Generator;
import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.conjure.spec.ErrorDefinition;
import com.palantir.conjure.spec.ErrorNamespace;
import com.squareup.javapoet.JavaFile;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ClientErrors implements Generator {
    public ClientErrors() {}

    @Override
    public Stream<JavaFile> generate(ConjureDefinition definition) {
        return splitErrorDefsByNamespace(definition.getErrors()).entrySet().stream()
                .map(entry -> {

                });
    }

    private static Stream<JavaFile>

    private static Map<String, Map<ErrorNamespace, List<ErrorDefinition>>> splitErrorDefsByNamespace(
            List<ErrorDefinition> errors) {
        return errors.stream()
                .collect(Collectors.groupingBy(
                        error -> error.getErrorName().getPackage(),
                        Collectors.groupingBy(ErrorDefinition::getNamespace)));
    }
}
