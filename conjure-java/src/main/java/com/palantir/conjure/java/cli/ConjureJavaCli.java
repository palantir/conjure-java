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
import com.google.common.base.Preconditions;
import com.palantir.conjure.java.services.JerseyServiceGenerator;
import com.palantir.conjure.java.services.Retrofit2ServiceGenerator;
import com.palantir.conjure.java.services.ServiceGenerator;
import com.palantir.conjure.java.types.ObjectGenerator;
import com.palantir.conjure.java.types.TypeGenerator;
import com.palantir.conjure.spec.ConjureDefinition;
import java.io.IOException;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public final class ConjureJavaCli {
    public static final String GENERATE_COMMAND = "generate";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .registerModule(new Jdk8Module())
            .setSerializationInclusion(JsonInclude.Include.NON_ABSENT);

    private ConjureJavaCli() {}

    public static void main(String[] args) {
        generate(parseCliConfiguration(args));
    }

    static CliConfiguration parseCliConfiguration(String[] args) {
        CommandLineParser parser = new BasicParser();
        Options options = new Options();
        options.addOption(new Option(CliConfiguration.OBJECTS_OPTION,
                "Generate POJOs for Conjure type definitions"));
        options.addOption(new Option(CliConfiguration.JERSEY_OPTION,
                "Generate jax-rs annotated interfaces for client or server-usage"));
        options.addOption(new Option(CliConfiguration.RETROFIT_OPTION,
                "Generate retrofit interfaces for streaming/async clients"));
        options.addOption(new Option(CliConfiguration.RETROFIT_COMPLETABLE_FUTURES,
                "Generate retrofit services which return Java8 CompletableFuture instead of OkHttp Call"));
        options.addOption(new Option(
                CliConfiguration.REQUIRE_NOT_NULL_AUTH_AND_BODY_PARAMS,
                "Generate @NotNull annotations for AuthHeaders and request body params"));

        try {
            CommandLine cmd = parser.parse(options, args, false);
            String[] parsedArgs = cmd.getArgs();

            Preconditions.checkArgument(parsedArgs.length == 3 && GENERATE_COMMAND.equals(args[0]),
                    "Usage: conjure-java %s <input> <output> [...options]", GENERATE_COMMAND);

            return CliConfiguration.of(parsedArgs[1], parsedArgs[2], cmd.getOptions());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    static void generate(CliConfiguration config) {
        try {
            ConjureDefinition conjureDefinition = OBJECT_MAPPER.readValue(config.target(), ConjureDefinition.class);
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

}
