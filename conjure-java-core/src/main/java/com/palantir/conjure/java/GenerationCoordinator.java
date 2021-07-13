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

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.palantir.common.streams.MoreStreams;
import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.goethe.Goethe;
import com.squareup.javapoet.JavaFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

public class GenerationCoordinator {

    private final Executor executor;
    private final Set<Generator> generators;

    public GenerationCoordinator(Executor executor, Set<Generator> generators) {
        this.executor = executor;
        this.generators = generators;
    }

    /**
     * Generates and emits to the given output directory all services and types of the given conjure definition, using
     * the instance's service and type generators.
     */
    public List<Path> emit(ConjureDefinition conjureDefinition, File outputDir) {
        return MoreStreams.inCompletionOrder(
                        generators.stream().flatMap(generator -> generator.generate(conjureDefinition)),
                        f -> write(f, outputDir),
                        executor,
                        Runtime.getRuntime().availableProcessors())
                .collect(Collectors.toList());
    }

    private static Path write(JavaFile javaFile, File outputDir) {
        try {
            Path result = getFilePath(javaFile, outputDir.toPath());
            Files.writeString(result, Goethe.formatAsString(javaFile));
            return result;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write file: " + javaFile.typeSpec.name);
        }
    }

    /**
     * Returns the full path for the given Java file and Java base dir. In a nutshell, turns packages into directories,
     * e.g., {@code com.foo.bar.MyClass -> /<baseDir>/com/foo/bar/MyClass.java} and creates all directories.
     * Implementation taken from JavaPoet's {@link JavaFile#writeTo(File)}.
     */
    private static Path getFilePath(JavaFile file, Path baseDir) throws IOException {
        Preconditions.checkArgument(
                Files.notExists(baseDir) || Files.isDirectory(baseDir),
                "path %s exists but is not a directory.",
                baseDir);
        Path outputDirectory = baseDir;
        if (!file.packageName.isEmpty()) {
            for (String packageComponent : Splitter.on(".").split(file.packageName)) {
                outputDirectory = outputDirectory.resolve(packageComponent);
            }
            Files.createDirectories(outputDirectory);
        }

        return outputDirectory.resolve(file.typeSpec.name + ".java");
    }
}
