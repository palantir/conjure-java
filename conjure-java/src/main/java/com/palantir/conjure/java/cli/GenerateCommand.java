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

package com.palantir.conjure.java.cli;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.google.common.annotations.VisibleForTesting;
import com.palantir.conjure.java.services.JerseyServiceGenerator;
import com.palantir.conjure.java.services.Retrofit2ServiceGenerator;
import com.palantir.conjure.java.services.ServiceGenerator;
import com.palantir.conjure.java.types.ObjectGenerator;
import com.palantir.conjure.java.types.TypeGenerator;
import com.palantir.conjure.spec.ConjureDefinition;
import java.io.File;
import java.io.IOException;
import java.util.List;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Unmatched;


@Command(name = "generate",
        description = "Generate Java bindings for a Conjure API",
        mixinStandardHelpOptions = true,
        usageHelpWidth = 120)
public final class GenerateCommand implements Runnable {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .registerModule(new Jdk8Module())
            .setSerializationInclusion(JsonInclude.Include.NON_ABSENT);

    @Parameters(paramLabel = "<input>", description = "Path to the input IR file", index = "0")
    private String input;
    @Parameters(paramLabel = "<output>", description = "Output directory for generated source", index = "1")
    private String output;

    @Option(names = "--objects",
            defaultValue = "false",
            description = "Generate POJOs for Conjure type definitions")
    private boolean generateObjects;

    @Option(names = "--jersey",
            defaultValue = "false",
            description = "Generate jax-rs annotated interfaces for client or server-usage")
    private boolean generateJersey;

    @Option(names = "--retrofit",
            defaultValue = "false",
            description = "Generate retrofit interfaces for streaming/async clients")
    private boolean generateRetrofit;

    @Option(names = "--retrofitCompletableFutures",
            defaultValue = "false",
            description = "Generate retrofit services which return Java8 CompletableFuture instead of OkHttp Call")
    private boolean retrofitCompletableFutures;

    @Option(names = "--retrofitListenableFutures",
            defaultValue = "false",
            description = "Generate retrofit services which return Guava ListenableFuture instead of OkHttp Call")
    private boolean retrofitListenableFutures;

    @Option(names = "--jerseyBinaryAsResponse",
            defaultValue = "false",
            description = "Generate jersey interfaces which return Response instead of StreamingOutput")
    private boolean jerseyBinaryAsReponse;

    @Option(names = "--requireNotNullAuthAndBodyParams",
            defaultValue = "false",
            description = "Generate @NotNull annotations for AuthHeaders and request body params")
    private boolean notNullAuthAndBody;


    @Unmatched private List<String> unmatchedOptions;

    @Override
    public void run() {
        CliConfiguration config = getConfiguration();
        try {
            ConjureDefinition conjureDefinition = OBJECT_MAPPER.readValue(config.input(), ConjureDefinition.class);
            TypeGenerator typeGenerator = new ObjectGenerator();
            ServiceGenerator jerseyGenerator = new JerseyServiceGenerator(config.featureFlags());
            ServiceGenerator retrofitGenerator = new Retrofit2ServiceGenerator(config.featureFlags());

            if (config.generateObjects()) {
                typeGenerator.emit(conjureDefinition, config.outputDirectory());
            }
            if (config.generateJersey()) {
                jerseyGenerator.emit(conjureDefinition, config.outputDirectory());
            }
            if (config.generateRetrofit()) {
                retrofitGenerator.emit(conjureDefinition, config.outputDirectory());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error parsing definition", e);
        }
    }

    @VisibleForTesting
    CliConfiguration getConfiguration() {
        return CliConfiguration.builder()
                .input(new File(input))
                .outputDirectory(new File(output))
                .generateJersey(generateJersey)
                .generateObjects(generateObjects)
                .generateRetrofit(generateRetrofit)
                .retrofitCompletableFutures(retrofitCompletableFutures)
                .retrofitListenableFutures(retrofitListenableFutures)
                .jerseyBinaryAsResponse(jerseyBinaryAsReponse)
                .notNullAuthAndBody(notNullAuthAndBody)
                .build();
    }

}
