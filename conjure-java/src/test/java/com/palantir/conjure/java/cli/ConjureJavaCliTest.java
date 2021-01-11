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

import com.palantir.conjure.java.Options;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import picocli.CommandLine;

public final class ConjureJavaCliTest {

    @TempDir
    public File tempDir;

    private File targetFile;

    @BeforeEach
    public void before() throws IOException {
        targetFile = Files.createFile(tempDir.toPath().resolve("target")).toFile();
    }

    @Test
    public void correctlyParseArguments() {
        String[] args = {"generate", targetFile.getAbsolutePath(), tempDir.getAbsolutePath(), "--objects"};
        CliConfiguration expectedConfiguration = CliConfiguration.builder()
                .input(targetFile)
                .outputDirectory(tempDir)
                .generateObjects(true)
                .build();
        ConjureJavaCli.GenerateCommand cmd = new CommandLine(new ConjureJavaCli())
                .parseArgs(args)
                .asCommandLineList()
                .get(1)
                .getCommand();
        assertThat(cmd.getConfiguration()).isEqualTo(expectedConfiguration);
    }

    @Test
    public void parseFeatureFlags() {
        String[] args = {
            "generate",
            targetFile.getAbsolutePath(),
            tempDir.getAbsolutePath(),
            "--objects",
            "--jerseyBinaryAsResponse",
            "--requireNotNullAuthAndBodyParams",
            "--useImmutableBytes"
        };
        CliConfiguration expectedConfiguration = CliConfiguration.builder()
                .input(targetFile)
                .outputDirectory(tempDir)
                .generateObjects(true)
                .options(Options.builder()
                        .jerseyBinaryAsResponse(true)
                        .requireNotNullAuthAndBodyParams(true)
                        .useImmutableBytes(true)
                        .build())
                .build();
        ConjureJavaCli.GenerateCommand cmd = new CommandLine(new ConjureJavaCli())
                .parseArgs(args)
                .asCommandLineList()
                .get(1)
                .getCommand();
        assertThat(cmd.getConfiguration()).isEqualTo(expectedConfiguration);
    }

    @Test
    public void nonNullCollectionsImpliesTopLevelNonNullValues() {
        String[] args = {
            "generate", targetFile.getAbsolutePath(), tempDir.getAbsolutePath(), "--objects", "--nonNullCollections"
        };
        CliConfiguration expectedConfiguration = CliConfiguration.builder()
                .input(targetFile)
                .outputDirectory(tempDir)
                .generateObjects(true)
                .options(Options.builder()
                        .nonNullCollections(true)
                        .nonNullTopLevelCollectionValues(true)
                        .build())
                .build();
        ConjureJavaCli.GenerateCommand cmd = new CommandLine(new ConjureJavaCli())
                .parseArgs(args)
                .asCommandLineList()
                .get(1)
                .getCommand();
        assertThat(cmd.getConfiguration()).isEqualTo(expectedConfiguration);
    }

    @Test
    public void doesNotThrowWhenUnexpectedFeature() {
        String[] args = {"generate", targetFile.getAbsolutePath(), tempDir.getAbsolutePath(), "--objects", "--foo"};
        CliConfiguration expectedConfiguration = CliConfiguration.builder()
                .input(targetFile)
                .outputDirectory(tempDir)
                .generateObjects(true)
                .build();
        ConjureJavaCli.GenerateCommand cmd =
                new CommandLine(new ConjureJavaCli()).parse(args).get(1).getCommand();
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
        String[] args = {"generate", targetFile.getAbsolutePath(), tempDir.getAbsolutePath()};
        assertThatThrownBy(() -> CommandLine.run(new ConjureJavaCli(), args))
                .isInstanceOf(CommandLine.ExecutionException.class)
                .hasMessageContaining("Must specify exactly one project to generate");
    }

    @Test
    public void throwsWhenTooManyGeneratorFlags() {
        String[] args = {"generate", targetFile.getAbsolutePath(), tempDir.getAbsolutePath(), "--objects", "--jersey"};
        assertThatThrownBy(() -> CommandLine.run(new ConjureJavaCli(), args))
                .isInstanceOf(CommandLine.ExecutionException.class)
                .hasMessageContaining("Must specify exactly one project to generate");
    }

    @Test
    public void generatesCode() throws Exception {
        String[] args = {
            "generate",
            "src/test/resources/conjure-api.json",
            tempDir.getAbsolutePath(),
            "--objects",
            "--useImmutableBytes"
        };

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream recordingStream = new PrintStream(baos);

        CommandLine.run(new ConjureJavaCli(), recordingStream, args);
        assertThat(new File(tempDir, "com/palantir/conjure/spec/ConjureDefinition.java").isFile())
                .isTrue();
        assertThat(baos.toString()).doesNotContain("[WARNING] Using deprecated ByteBuffer");
    }

    @Test
    public void throwsWhenInvalidDefinition() throws Exception {
        String[] args = {"generate", targetFile.getAbsolutePath(), tempDir.getAbsolutePath(), "--objects"};
        assertThatThrownBy(() -> CommandLine.run(new ConjureJavaCli(), args))
                .isInstanceOf(CommandLine.ExecutionException.class)
                .hasMessageContaining("Error parsing definition");
    }

    @Test
    @Disabled("Unable to capture output")
    public void writesWarningWhenBytesIsDisabled() throws IOException {
        String[] args = {"generate", "src/test/resources/conjure-api.json", tempDir.getAbsolutePath(), "--objects"};
        CommandLine.run(new ConjureJavaCli(), args);
        // assertThat(systemErr.getLog()).contains("[WARNING] Using deprecated ByteBuffer");
    }

    @Test
    @Disabled("Unable to capture output")
    public void doesNotWriteWarningWhenObjectsAreNotGenerated() throws IOException {
        String[] args = {"generate", "src/test/resources/conjure-api.json", tempDir.getAbsolutePath(), "--jersey"};
        CommandLine.run(new ConjureJavaCli(), args);
        // assertThat(systemErr.getLog()).doesNotContain("[WARNING] Using deprecated ByteBuffer");
    }
}
