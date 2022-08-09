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
import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableSet;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.palantir.conjure.java.GenerationCoordinator;
import com.palantir.conjure.java.Generator;
import com.palantir.conjure.java.Options;
import com.palantir.conjure.java.services.JerseyServiceGenerator;
import com.palantir.conjure.java.services.Retrofit2ServiceGenerator;
import com.palantir.conjure.java.services.UndertowServiceGenerator;
import com.palantir.conjure.java.services.dialogue.DialogueServiceGenerator;
import com.palantir.conjure.java.types.ErrorGenerator;
import com.palantir.conjure.java.types.ObjectGenerator;
import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.logsafe.exceptions.SafeRuntimeException;
import com.squareup.javapoet.ClassName;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.annotation.Nullable;
import picocli.CommandLine;

@CommandLine.Command(
        name = "conjure-java",
        description = "CLI to generate Java POJOs and interfaces from Conjure API definitions.",
        mixinStandardHelpOptions = true,
        subcommands = ConjureJavaCli.GenerateCommand.class)
public final class ConjureJavaCli implements Runnable {
    // Load TypeName to prevent a deadlock
    // https://github.com/square/javapoet/issues/637
    @SuppressWarnings("unused")
    private static final ClassName _loaded = ClassName.get(Runnable.class);

    public static void main(String[] args) {
        CommandLine.run(new ConjureJavaCli(), args);
    }

    @Override
    public void run() {
        CommandLine.usage(this, System.out);
    }

    @CommandLine.Command(
            name = "generate",
            description = "Generate Java bindings for a Conjure API",
            mixinStandardHelpOptions = true,
            usageHelpWidth = 120)
    public static final class GenerateCommand implements Runnable {
        private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
                .registerModule(new Jdk8Module())
                .setSerializationInclusion(JsonInclude.Include.NON_ABSENT);

        @CommandLine.Parameters(paramLabel = "<input>", description = "Path to the input IR file", index = "0")
        private String input;

        @CommandLine.Parameters(
                paramLabel = "<output>",
                description = "Output directory for generated source",
                index = "1")
        private String output;

        @CommandLine.Option(
                names = "--objects",
                defaultValue = "false",
                description = "Generate POJOs for Conjure type definitions")
        private boolean generateObjects;

        @CommandLine.Option(
                names = "--jersey",
                defaultValue = "false",
                description = "Generate jax-rs annotated interfaces for client or server-usage")
        private boolean generateJersey;

        @CommandLine.Option(
                names = "--jakartaPackages",
                defaultValue = "false",
                description = "When generating jax-rs annotation interfaces, use the newer jakarta prefixes")
        private boolean jakartaPackages;

        @CommandLine.Option(
                names = "--undertow",
                defaultValue = "false",
                description = "Generate undertow service interfaces and endpoint wrappers for server usage")
        private boolean generateUndertow;

        @CommandLine.Option(
                names = "--dialogue",
                defaultValue = "false",
                description = "Generate interfaces for dialogue clients")
        private boolean generateDialogue;

        @CommandLine.Option(
                names = "--retrofit",
                defaultValue = "false",
                description = "Generate retrofit interfaces for streaming/async clients")
        private boolean generateRetrofit;

        @CommandLine.Option(
                names = "--jerseyBinaryAsResponse",
                defaultValue = "false",
                description = "Generate jersey interfaces which return Response instead of StreamingOutput")
        private boolean jerseyBinaryAsReponse;

        @CommandLine.Option(
                names = "--requireNotNullAuthAndBodyParams",
                defaultValue = "false",
                description = "Generate @NotNull annotations for AuthHeaders and request body params")
        private boolean notNullAuthAndBody;

        @CommandLine.Option(
                names = "--undertowServicePrefixes",
                defaultValue = "false",
                description = "Generate service interfaces for Undertow with class names prefixed 'Undertow'")
        private boolean undertowServicePrefix;

        @CommandLine.Option(
                names = "--undertowListenableFutures",
                defaultValue = "false",
                description = "Generate Undertow services which return Guava ListenableFuture for asynchronous "
                        + "processing")
        private boolean undertowListenableFutures;

        @CommandLine.Option(
                names = "--strictObjects",
                defaultValue = "false",
                description = "Generate POJOs that by default will fail to deserialize unknown fields")
        private boolean strictObjects;

        @CommandLine.Option(
                names = "--nonNullCollections",
                defaultValue = "false",
                description = "Generate POJOs that by default will fail to deserialize collections with null values")
        private boolean nonNullCollections;

        @CommandLine.Option(
                names = "--nonNullTopLevelCollectionValues",
                defaultValue = "false",
                description = "Generate services that by default will fail to deserialize top-level collections with "
                        + "null values. This option is always enabled when --nonNullCollections is used, this option "
                        + "exists to allow consumers which cannot use the broader flag yet.")
        private boolean nonNullTopLevelCollectionValues;

        @Beta
        @CommandLine.Option(
                names = "--experimentalUndertowAsyncMarkers",
                defaultValue = "false",
                description = "Experimental: Allows Undertow asynchronous processing to be enabled on individual "
                        + "endpoints")
        private boolean experimentalUndertowAsyncMarkers;

        @CommandLine.Option(
                names = "--useImmutableBytes",
                defaultValue = "false",
                description = "Generate binary fields using the immutable 'Bytes' type instead of 'ByteBuffer'")
        private boolean useImmutableBytes;

        @CommandLine.Option(names = "--packagePrefix", description = "Prefix to the package of all generated classes")
        @Nullable
        private String packagePrefix;

        @CommandLine.Option(
                names = "--apiVersion",
                description = "A optional version number (e.g. '1.2.3') to be embedded in the generated Java code. If"
                        + " omitted, the version will be read from the Jar Manifest's Implementation-Version.")
        @Nullable
        private String apiVersion;

        @CommandLine.Option(
                names = "--useStagedBuilders",
                defaultValue = "false",
                description = "Generates compile-time safe builders to ensure all required attributes are set.")
        private boolean useStagedBuilders;

        @CommandLine.Option(
                names = "--excludeEmptyOptionals",
                defaultValue = "false",
                description = "Objects exclude empty optionals in serialization based on the conjure spec.")
        private boolean excludeEmptyOptionals;

        @CommandLine.Option(
                names = "--experimentalExcludeEmptyCollections",
                defaultValue = "false",
                description = "Objects exclude empty collections in serialization based on the conjure spec. Note "
                        + "that this should not be enabled on servers due to shortcomings in the "
                        + "typescript implementation, and should only be used for clients with local codegen.")
        private boolean excludeEmptyCollections;

        @CommandLine.Option(
                names = "--unionsWithUnknownValues",
                defaultValue = "false",
                description = "Union visitors expose the values of unknowns in addition to their types.")
        private boolean unionsWithUnknownValues;

        @SuppressWarnings("unused")
        @CommandLine.Unmatched
        private List<String> unmatchedOptions;

        @Override
        @SuppressWarnings("BanSystemErr")
        public void run() {
            CliConfiguration config = getConfiguration();
            if (config.generateObjects() && !config.options().useImmutableBytes()) {
                System.err.println("[WARNING] Using deprecated ByteBuffer codegen, please enable the "
                        + "--useImmutableBytes feature flag to opt into the preferred implementation");
            }
            ExecutorService executor = Executors.newCachedThreadPool(
                    new ThreadFactoryBuilder().setDaemon(true).build());
            try {
                ConjureDefinition conjureDefinition = OBJECT_MAPPER.readValue(config.input(), ConjureDefinition.class);

                ImmutableSet.Builder<Generator> generatorBuilder = ImmutableSet.builder();
                if (config.generateObjects()) {
                    generatorBuilder.add(new ObjectGenerator(config.options()), new ErrorGenerator(config.options()));
                }
                if (config.generateJersey()) {
                    generatorBuilder.add(new JerseyServiceGenerator(config.options()));
                }
                if (config.generateRetrofit()) {
                    generatorBuilder.add(new Retrofit2ServiceGenerator(config.options()));
                }
                if (config.generateUndertow()) {
                    generatorBuilder.add(new UndertowServiceGenerator(config.options()));
                }
                if (config.generateDialogue()) {
                    generatorBuilder.add(new DialogueServiceGenerator(config.options()));
                }
                new GenerationCoordinator(executor, generatorBuilder.build())
                        .emit(conjureDefinition, config.outputDirectory());
            } catch (IOException e) {
                throw new SafeRuntimeException("Error parsing definition", e);
            } finally {
                executor.shutdown();
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
                    .generateUndertow(generateUndertow)
                    .generateDialogue(generateDialogue)
                    .options(Options.builder()
                            .jerseyBinaryAsResponse(jerseyBinaryAsReponse)
                            .requireNotNullAuthAndBodyParams(notNullAuthAndBody)
                            .undertowServicePrefix(undertowServicePrefix)
                            .useImmutableBytes(useImmutableBytes)
                            .undertowListenableFutures(undertowListenableFutures)
                            .experimentalUndertowAsyncMarkers(experimentalUndertowAsyncMarkers)
                            .jakartaPackages(jakartaPackages)
                            .strictObjects(strictObjects)
                            .nonNullCollections(nonNullCollections)
                            .nonNullTopLevelCollectionValues(nonNullCollections || nonNullTopLevelCollectionValues)
                            .packagePrefix(Optional.ofNullable(packagePrefix))
                            .apiVersion(Optional.ofNullable(apiVersion))
                            .useStagedBuilders(useStagedBuilders)
                            .excludeEmptyOptionals(excludeEmptyOptionals)
                            .excludeEmptyCollections(excludeEmptyCollections)
                            .unionsWithUnknownValues(unionsWithUnknownValues)
                            .build())
                    .build();
        }
    }
}
