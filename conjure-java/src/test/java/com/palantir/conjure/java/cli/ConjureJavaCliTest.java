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

import com.palantir.conjure.java.Options;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
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
            "--undertowCompletableFutures",
            "--jerseyBinaryAsResponse",
            "--requireNotNullAuthAndBodyParams",
            "--useImmutableBytes",
            "--jakartaPackages"
        };
        CliConfiguration expectedConfiguration = CliConfiguration.builder()
                .input(targetFile)
                .outputDirectory(tempDir)
                .generateObjects(true)
                .options(Options.builder()
                        .jerseyBinaryAsResponse(true)
                        .requireNotNullAuthAndBodyParams(true)
                        .useImmutableBytes(true)
                        .jakartaPackages(true)
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
        ConjureJavaCli.GenerateCommand cmd = new CommandLine(new ConjureJavaCli())
                .parseArgs(args)
                .asCommandLineList()
                .get(1)
                .getCommand();
        assertThat(cmd.getConfiguration()).isEqualTo(expectedConfiguration);
    }

    @Test
    public void throwsWhenTargetDoesNotExist() {
        String[] args = {"generate", "foo", "bar"};

        StringWriter err = new StringWriter();

        int exitCode = new CommandLine(new ConjureJavaCli())
                .setErr(new PrintWriter(err))
                .execute(args);

        assertThat(exitCode).isEqualTo(1);
        assertThat(err.toString()).contains("Target must exist and be a file");
    }

    @Test
    public void throwsWhenOutputDoesNotExist() {
        String[] args = {"generate", targetFile.getAbsolutePath(), "bar"};

        StringWriter err = new StringWriter();

        int exitCode = new CommandLine(new ConjureJavaCli())
                .setErr(new PrintWriter(err))
                .execute(args);

        assertThat(exitCode).isEqualTo(1);
        assertThat(err.toString()).contains("Output must exist and be a directory");
    }

    @Test
    public void throwsWhenMissingGeneratorFlags() {
        String[] args = {"generate", targetFile.getAbsolutePath(), tempDir.getAbsolutePath()};

        StringWriter err = new StringWriter();

        int exitCode = new CommandLine(new ConjureJavaCli())
                .setErr(new PrintWriter(err))
                .execute(args);

        assertThat(exitCode).isEqualTo(1);
        assertThat(err.toString()).contains("Must specify exactly one project to generate");
    }

    @Test
    public void throwsWhenTooManyGeneratorFlags() {
        String[] args = {"generate", targetFile.getAbsolutePath(), tempDir.getAbsolutePath(), "--objects", "--jersey"};

        StringWriter err = new StringWriter();

        int exitCode = new CommandLine(new ConjureJavaCli())
                .setErr(new PrintWriter(err))
                .execute(args);

        assertThat(exitCode).isEqualTo(1);
        assertThat(err.toString()).contains("Must specify exactly one project to generate");
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

        StringWriter out = new StringWriter();

        int exitCode = new CommandLine(new ConjureJavaCli())
                .setOut(new PrintWriter(out))
                .execute(args);

        assertThat(exitCode).isEqualTo(0);
        assertThat(new File(tempDir, "com/palantir/conjure/spec/ConjureDefinition.java").isFile())
                .isTrue();
        assertThat(out.toString()).doesNotContain("[WARNING] Using deprecated ByteBuffer");
    }

    @Test
    public void throwsWhenInvalidDefinition() throws Exception {
        String[] args = {"generate", targetFile.getAbsolutePath(), tempDir.getAbsolutePath(), "--objects"};

        StringWriter err = new StringWriter();

        int exitCode = new CommandLine(new ConjureJavaCli())
                .setErr(new PrintWriter(err))
                .execute(args);

        assertThat(exitCode).isEqualTo(1);
        assertThat(err.toString()).contains("Error parsing definition");
    }

    @Test
    @Disabled("Unable to capture output")
    public void writesWarningWhenBytesIsDisabled() throws IOException {
        String[] args = {"generate", "src/test/resources/conjure-api.json", tempDir.getAbsolutePath(), "--objects"};
        new CommandLine(new ConjureJavaCli()).execute(args);
        // assertThat(systemErr.getLog()).contains("[WARNING] Using deprecated ByteBuffer");
    }

    @Test
    @Disabled("Unable to capture output")
    public void doesNotWriteWarningWhenObjectsAreNotGenerated() throws IOException {
        String[] args = {"generate", "src/test/resources/conjure-api.json", tempDir.getAbsolutePath(), "--jersey"};
        new CommandLine(new ConjureJavaCli()).execute(args);
        // assertThat(systemErr.getLog()).doesNotContain("[WARNING] Using deprecated ByteBuffer");
    }

    @Test
    public void mainExitsWithNonZeroOnGenerationFailure() throws Exception {
        String classpath = System.getProperty("java.class.path");
        String javaHome = System.getProperty("java.home");
        String javaBin = javaHome + File.separator + "bin" + File.separator + "java";

        Process process = new ProcessBuilder(
                        javaBin,
                        "-cp",
                        classpath,
                        "com.palantir.conjure.java.cli.ConjureJavaCli",
                        "generate",
                        "src/test/resources/do-not-log-unsafe-error.json",
                        tempDir.getAbsolutePath(),
                        "--objects",
                        "--useImmutableBytes")
                .redirectErrorStream(true)
                .start();

        String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        int exitCode = process.waitFor();

        assertThat(exitCode)
                .as("Expected non-zero exit code. Output:\n%s", output)
                .isNotEqualTo(0);
        assertThat(output).contains("Cannot use DO_NOT_LOG type");
    }
}
