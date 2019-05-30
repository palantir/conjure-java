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

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.palantir.conjure.java.util.Goethe;
import com.palantir.conjure.java.util.Javadoc;
import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.conjure.spec.EndpointDefinition;
import com.squareup.javapoet.JavaFile;
import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

public interface ServiceGenerator {

    /** Returns the set of Java files generated from the service definitions in the given conjure specification. */
    Set<JavaFile> generate(ConjureDefinition conjureDefinition);

    /**
     * Generates and emits to the given output directory all services and types of the given conjure definition, using
     * the instance's service and type generators.
     */
    default List<Path> emit(ConjureDefinition conjureDefinition, File outputDir) {
        List<Path> emittedPaths = Lists.newArrayList();
        generate(conjureDefinition).forEach(f -> {
            Path emittedPath = Goethe.formatAndEmit(f, outputDir.toPath());
            emittedPaths.add(emittedPath);
        });
        return emittedPaths;
    }

    static Optional<String> getJavaDoc(EndpointDefinition endpointDef) {
        Optional<String> depr = endpointDef.getDeprecated()
                .map(v -> StringUtils.appendIfMissing("@deprecated " + v, "\n"));

        Optional<String> docs = endpointDef.getDocs().map(Javadoc::render);

        Optional<String> params = Optional.ofNullable(Strings.emptyToNull(endpointDef.getArgs().stream()
                .filter(argument -> argument.getDocs().isPresent())
                .map(argument -> Javadoc.getParameterJavadoc(argument, endpointDef))
                .collect(Collectors.joining("\n"))));

        StringBuilder sb = new StringBuilder();
        docs.ifPresent(sb::append);
        params.ifPresent(sb::append);
        depr.ifPresent(sb::append);
        return sb.length() > 0 ? Optional.of(sb.toString()) : Optional.empty();
    }
}
