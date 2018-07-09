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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.google.common.collect.ImmutableSet;
import com.palantir.conjure.java.FeatureFlags;
import java.io.File;
import java.io.IOException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public final class ConjureJavaCliTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private File targetFile;

    @Before
    public void before() throws IOException {
        targetFile = folder.newFile();
    }

    @Test
    public void correctlyParseArguments() {
        String[] args = {
                ConjureJavaCli.GENERATE_COMMAND,
                targetFile.getAbsolutePath(),
                folder.getRoot().getAbsolutePath(),
                "--objects"
        };
        CliConfiguration expectedConfiguration = CliConfiguration.builder()
                .target(targetFile)
                .outputDirectory(folder.getRoot())
                .generateObjects(true)
                .build();
        assertThat(ConjureJavaCli.parseCliConfiguration(args)).isEqualTo(expectedConfiguration);
    }

    @Test
    public void parseFeatureFlags() {
        String[] args = {
                ConjureJavaCli.GENERATE_COMMAND,
                targetFile.getAbsolutePath(),
                folder.getRoot().getAbsolutePath(),
                "--objects",
                "--retrofitCompletableFutures",
                "--requireNotNullAuthAndBodyParams"
        };
        CliConfiguration expectedConfiguration = CliConfiguration.builder()
                .target(targetFile)
                .outputDirectory(folder.getRoot())
                .generateObjects(true)
                .featureFlags(ImmutableSet.of(FeatureFlags.RetrofitCompletableFutures, FeatureFlags.RequireNotNullAuthAndBodyParams))
                .build();
        assertThat(ConjureJavaCli.parseCliConfiguration(args)).isEqualTo(expectedConfiguration);
    }

    @Test
    public void throwsWhenUnexpectedFeature() {
        String[] args = {
                ConjureJavaCli.GENERATE_COMMAND,
                targetFile.getAbsolutePath(),
                folder.getRoot().getAbsolutePath(),
                "--objects",
                "--foo"
        };
        assertThatThrownBy(() -> ConjureJavaCli.parseCliConfiguration(args))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Unrecognized option: --foo");
    }

    @Test
    public void throwsWhenMissingArguments() {
        String[] args = {};
        assertThatThrownBy(() -> ConjureJavaCli.parseCliConfiguration(args))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Usage: conjure-java generate <input> <output> [...options]");
    }

    @Test
    public void throwsWhenTargetDoesNotExist() {
        String[] args = {ConjureJavaCli.GENERATE_COMMAND, "foo", "bar"};
        assertThatThrownBy(() -> ConjureJavaCli.parseCliConfiguration(args))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Target must exist and be a file");
    }

    @Test
    public void throwsWhenOutputDoesNotExist() {
        String[] args = {ConjureJavaCli.GENERATE_COMMAND, targetFile.getAbsolutePath(), "bar"};
        assertThatThrownBy(() -> ConjureJavaCli.parseCliConfiguration(args))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Output must exist and be a directory");
    }

    @Test
    public void throwsWhenMissingGeneratorFlags() {
        String[] args = {
                ConjureJavaCli.GENERATE_COMMAND,
                targetFile.getAbsolutePath(),
                folder.getRoot().getAbsolutePath()
        };
        assertThatThrownBy(() -> ConjureJavaCli.parseCliConfiguration(args))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Must specify exactly one project to generate");
    }

    @Test
    public void throwsWhenTooManyGeneratorFlags() {
        String[] args = {
                ConjureJavaCli.GENERATE_COMMAND,
                targetFile.getAbsolutePath(),
                folder.getRoot().getAbsolutePath(),
                "--objects",
                "--jersey"
        };
        assertThatThrownBy(() -> ConjureJavaCli.parseCliConfiguration(args))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Must specify exactly one project to generate");
    }

    @Test
    public void generatesCode() throws Exception {
        File outputDirectory = folder.newFolder();
        CliConfiguration configuration = CliConfiguration.builder()
                .target(new File("src/test/resources/conjure-api.json"))
                .outputDirectory(outputDirectory)
                .generateObjects(true)
                .build();
        ConjureJavaCli.generate(configuration);
        assertThat(
                new File(outputDirectory, "com/palantir/conjure/spec/ConjureDefinition.java").isFile()).isTrue();
    }

    @Test
    public void throwsWhenInvalidDefinition() throws Exception {
        CliConfiguration configuration = CliConfiguration.builder()
                .target(targetFile)
                .outputDirectory(folder.newFolder())
                .generateObjects(true)
                .build();
        assertThatThrownBy(() -> ConjureJavaCli.generate(configuration))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Error parsing definition");
    }

    @Ignore
    @Test
    public void regenerateVendoredCode() throws Exception {
        File outputDirectory = new File("../conjure-java-core/src/main/java");
        CliConfiguration configuration = CliConfiguration.builder()
                .target(new File("src/test/resources/conjure-api.json"))
                .outputDirectory(outputDirectory)
                .generateObjects(true)
                .build();
        ConjureJavaCli.generate(configuration);
    }
}
