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
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemErrRule;
import org.junit.rules.TemporaryFolder;
import picocli.CommandLine;

public final class ConjureJavaCliTest {

    @Rule
    public final TemporaryFolder folder = new TemporaryFolder();

    @Rule
    public final SystemErrRule systemErr = new SystemErrRule().enableLog();

    private File targetFile;

    @Before
    public void before() throws IOException {
        targetFile = folder.newFile();
    }

    @Test
    public void correctlyParseArguments() {
        String[] args = {
                "generate",
                targetFile.getAbsolutePath(),
                folder.getRoot().getAbsolutePath(),
                "--objects"
        };
        CliConfiguration expectedConfiguration = CliConfiguration.builder()
                .input(targetFile)
                .outputDirectory(folder.getRoot())
                .generateObjects(true)
                .build();
        ConjureJavaCli.GenerateCommand cmd = new CommandLine(new ConjureJavaCli()).parse(args).get(1).getCommand();
        assertThat(cmd.getConfiguration()).isEqualTo(expectedConfiguration);
    }

    @Test
    public void parseFeatureFlags() {
        String[] args = {
                "generate",
                targetFile.getAbsolutePath(),
                folder.getRoot().getAbsolutePath(),
                "--objects",
                "--retrofitCompletableFutures",
                "--jerseyBinaryAsResponse",
                "--requireNotNullAuthAndBodyParams",
                "--useImmutableBytes"
        };
        CliConfiguration expectedConfiguration = CliConfiguration.builder()
                .input(targetFile)
                .outputDirectory(folder.getRoot())
                .generateObjects(true)
                .featureFlags(ImmutableSet.of(
                        FeatureFlags.RetrofitCompletableFutures,
                        FeatureFlags.JerseyBinaryAsResponse,
                        FeatureFlags.RequireNotNullAuthAndBodyParams,
                        FeatureFlags.UseImmutableBytes))
                .build();
        ConjureJavaCli.GenerateCommand cmd = new CommandLine(new ConjureJavaCli()).parse(args).get(1).getCommand();
        assertThat(cmd.getConfiguration()).isEqualTo(expectedConfiguration);
    }

    @Test
    public void doesNotThrowWhenUnexpectedFeature() {
        String[] args = {
                "generate",
                targetFile.getAbsolutePath(),
                folder.getRoot().getAbsolutePath(),
                "--objects",
                "--foo"
        };
        CliConfiguration expectedConfiguration = CliConfiguration.builder()
                .input(targetFile)
                .outputDirectory(folder.getRoot())
                .generateObjects(true)
                .build();
        ConjureJavaCli.GenerateCommand cmd = new CommandLine(new ConjureJavaCli()).parse(args).get(1).getCommand();
        assertThat(cmd.getConfiguration()).isEqualTo(expectedConfiguration);
    }

    @Test
    public void throwsWhenTargetDoesNotExist() {
        String[] args = {"generate", "foo", "bar"};
        assertThatThrownBy(() -> CommandLine.run(new ConjureJavaCli(), args))
                .isInstanceOf(CommandLine.ExecutionException.class)
                .hasMessageContaining("Target must exist and be a file");
    }

    @Test
    public void throwsWhenOutputDoesNotExist() {
        String[] args = {"generate", targetFile.getAbsolutePath(), "bar"};
        assertThatThrownBy(() -> CommandLine.run(new ConjureJavaCli(), args))
                .isInstanceOf(CommandLine.ExecutionException.class)
                .hasMessageContaining("Output must exist and be a directory");
    }

    @Test
    public void throwsWhenMissingGeneratorFlags() {
        String[] args = { "generate", targetFile.getAbsolutePath(), folder.getRoot().getAbsolutePath() };
        assertThatThrownBy(() -> CommandLine.run(new ConjureJavaCli(), args))
                .isInstanceOf(CommandLine.ExecutionException.class)
                .hasMessageContaining("Must specify exactly one project to generate");
    }

    @Test
    public void throwsWhenTooManyGeneratorFlags() {
        String[] args = {
                "generate",
                targetFile.getAbsolutePath(),
                folder.getRoot().getAbsolutePath(),
                "--objects",
                "--jersey"
        };
        assertThatThrownBy(() -> CommandLine.run(new ConjureJavaCli(), args))
                .isInstanceOf(CommandLine.ExecutionException.class)
                .hasMessageContaining("Must specify exactly one project to generate");
    }

    @Test
    public void generatesCode() throws Exception {
        File outputDirectory = folder.newFolder();
        String[] args = {
                "generate",
                "src/test/resources/conjure-api.json",
                outputDirectory.getAbsolutePath(),
                "--objects",
                "--useImmutableBytes"
        };
        CommandLine.run(new ConjureJavaCli(), args);
        assertThat(
                new File(outputDirectory, "com/palantir/conjure/spec/ConjureDefinition.java").isFile()).isTrue();
        assertThat(systemErr.getLog()).doesNotContain("[WARNING] Using deprecated ByteBuffer");
    }

    @Test
    public void throwsWhenInvalidDefinition() throws Exception {
        String[] args = { "generate", targetFile.getAbsolutePath(), folder.newFolder().getAbsolutePath(), "--objects" };
        assertThatThrownBy(() -> CommandLine.run(new ConjureJavaCli(), args))
                .isInstanceOf(CommandLine.ExecutionException.class)
                .hasMessageContaining("Error parsing definition");
    }

    @Test
    public void writesWarningWhenBytesIsDisabled() throws IOException {
        File outputDirectory = folder.newFolder();
        String[] args = {
                "generate",
                "src/test/resources/conjure-api.json",
                outputDirectory.getAbsolutePath(),
                "--objects"
        };
        CommandLine.run(new ConjureJavaCli(), args);
        assertThat(systemErr.getLog()).contains("[WARNING] Using deprecated ByteBuffer");
    }

    @Test
    public void doesNotWriteWarningWhenObjectsAreNotGenerated() throws IOException {
        File outputDirectory = folder.newFolder();
        String[] args = {
                "generate",
                "src/test/resources/conjure-api.json",
                outputDirectory.getAbsolutePath(),
                "--jersey"
        };
        CommandLine.run(new ConjureJavaCli(), args);
        assertThat(systemErr.getLog()).doesNotContain("[WARNING] Using deprecated ByteBuffer");
    }
}
