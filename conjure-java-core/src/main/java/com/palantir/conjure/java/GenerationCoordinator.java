/*
 * (c) Copyright 2020 Palantir Technologies Inc. All rights reserved.
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

import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.goethe.Goethe;
import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

public class GenerationCoordinator {

    private final Set<Generator> generators;

    public GenerationCoordinator(Set<Generator> generators) {
        this.generators = generators;
    }

    /**
     * Temporary backcompat shim because this module is published, despite being considered internal api.
     * @deprecated use {@link GenerationCoordinator#GenerationCoordinator(Set)}.
     */
    @Deprecated
    public GenerationCoordinator(Executor _executor, Set<Generator> generators) {
        this(generators);
    }

    /**
     * Generates and emits to the given output directory all services and types of the given conjure definition, using
     * the instance's service and type generators.
     */
    public List<Path> emit(ConjureDefinition conjureDefinition, File outputDir) {
        return generators.stream()
                .flatMap(generator -> generator.generate(conjureDefinition))
                .map(f -> Goethe.formatAndEmit(f, outputDir.toPath()))
                .collect(Collectors.toList());
    }
}
