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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Splitter;
import com.palantir.common.streams.MoreStreams;
import com.palantir.conjure.java.GeneratedFile.GeneratedJavaFile;
import com.palantir.conjure.java.GeneratedFile.GeneratedReachabilityMetadataFile;
import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.goethe.Goethe;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeRuntimeException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

public class GenerationCoordinator {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final Executor executor;
    private final Set<Generator> generators;
    private final Options options;

    public GenerationCoordinator(Executor executor, Set<Generator> generators, Options options) {
        this.executor = executor;
        this.generators = generators;
        this.options = options;
    }

    public GenerationCoordinator(Executor executor, Set<Generator> generators) {
        this(executor, generators, Options.empty());
    }

    /**
     * Generates and emits to the given output directory all services and types of the given conjure definition, using
     * the instance's service and type generators.
     */
    public List<Path> emit(ConjureDefinition conjureDefinition, File outputDir) {
        ConjureDefinition definition = new ExternalImportFilter(options).filter(conjureDefinition);
        return MoreStreams.inCompletionOrder(
                        generators.stream().flatMap(generator -> generator.generate(definition)),
                        f -> formatAndEmit(f, outputDir.toPath()),
                        executor,
                        Runtime.getRuntime().availableProcessors())
                .collect(Collectors.toList());
    }

    /**
     * TODO(pm): decide if we'll do one file per type? or one file per package?
     *  no harm in creating loads of files + they're localized. these aren't really going to be read by humans so we
     *  don't need to worry about "people can more easily see all the info in one file". it'd be easy to concat all the
     *  reachability-metadata.json files as well.
     */
    private static Path formatAndEmit(GeneratedFile generatedFile, Path outputDirectoryPath) {
        if (generatedFile instanceof GeneratedJavaFile generatedJavaFile) {
            return Goethe.formatAndEmit(generatedJavaFile.javaFile(), outputDirectoryPath);
        } else if (generatedFile instanceof GeneratedReachabilityMetadataFile generatedReachabilityMetadataFile) {
            try {
                Path outputDirectory = outputDirectoryPath;
                for (String packageComponent :
                        Splitter.on(".").split(generatedReachabilityMetadataFile.packageName())) {
                    outputDirectory = outputDirectory.resolve(packageComponent);
                }
                outputDirectory = outputDirectory.resolve(generatedReachabilityMetadataFile.typeName());
                Files.createDirectories(outputDirectory);
                Path output = outputDirectory.resolve("reachability-metadata.json");
                OBJECT_MAPPER.writeValue(output.toFile(), generatedReachabilityMetadataFile.reachabilityMetadata());
                return output;
            } catch (IOException e) {
                throw new SafeRuntimeException("Failed to emit reachability metadata file", e);
            }
        } else {
            throw new SafeRuntimeException(
                    "Unknown generated file type", SafeArg.of("class", generatedFile.getClass()));
        }
    }
}
