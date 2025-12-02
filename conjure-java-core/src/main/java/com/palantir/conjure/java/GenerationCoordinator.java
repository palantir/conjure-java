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

import com.palantir.common.streams.MoreStreams;
import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.goethe.Goethe;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

public class GenerationCoordinator {

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
        System.err.printf("[conjure-java] [%s] Command: %s", Instant.now(), System.getProperty("sun.java.command"));
        ManagementFactory.getRuntimeMXBean().getInputArguments().forEach(argument -> {
            System.err.printf(" %s", argument);
        });
        System.err.println("\n");

        Map<Thread, StackTraceElement[]> allStackTraces = Thread.getAllStackTraces();

        // Iterate through each thread and its stack trace
        for (Map.Entry<Thread, StackTraceElement[]> entry : allStackTraces.entrySet()) {
            Thread thread = entry.getKey();
            StackTraceElement[] stackTrace = entry.getValue();
            System.err.println("[conjure-java] Thread: " + thread.getName() + " (ID: " + thread.getId() + ", State: "
                    + thread.getState() + ")");
            if (stackTrace.length == 0) {
                System.err.println("\tNo stack trace available.");
            } else {
                for (StackTraceElement element : stackTrace) {
                    System.err.println("\t" + element.toString());
                }
            }
            System.err.println("----------------------------------------");
        }

        System.err.println("\n");

        ConjureDefinition definition = new ExternalImportFilter(options).filter(conjureDefinition);
        return MoreStreams.inCompletionOrder(
                        generators.stream().flatMap(generator -> generator.generate(definition)),
                        f -> {
                            System.err.printf(
                                    "[conjure-java] [%s] Formatting: %s\n",
                                    Instant.now(), f.typeSpec().name());
                            return Goethe.formatAndEmit(f, outputDir.toPath());
                        },
                        executor,
                        Runtime.getRuntime().availableProcessors())
                .collect(Collectors.toList());
    }
}
