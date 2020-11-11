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

package com.palantir.conjure.java.types;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.util.concurrent.MoreExecutors;
import com.palantir.conjure.defs.Conjure;
import com.palantir.conjure.java.GenerationCoordinator;
import com.palantir.conjure.java.Options;
import com.palantir.conjure.spec.ConjureDefinition;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public final class ObjectGeneratorTests {

    private static final String REFERENCE_FILES_FOLDER = "src/integrationInput/java";

    @TempDir
    public File tempDir;

    @Test
    public void testObjectGenerator_allExamples() throws IOException {
        ConjureDefinition def = Conjure.parse(ImmutableList.of(new File("src/test/resources/example-types.yml")));
        List<Path> files = new GenerationCoordinator(
                        MoreExecutors.directExecutor(),
                        ImmutableSet.of(new ObjectGenerator(Options.builder()
                                .useImmutableBytes(true)
                                .strictObjects(true)
                                .nonNullCollections(true)
                                .build())))
                .emit(def, tempDir);

        assertThatFilesAreTheSame(files, REFERENCE_FILES_FOLDER);
    }

    @Test
    public void testObjectGenerator_byteBufferCompatibility() throws IOException {
        ConjureDefinition def =
                Conjure.parse(ImmutableList.of(new File("src/test/resources/example-binary-types.yml")));
        List<Path> files = new GenerationCoordinator(
                        MoreExecutors.directExecutor(), ImmutableSet.of(new ObjectGenerator(Options.empty())))
                .emit(def, tempDir);

        assertThatFilesAreTheSame(files, REFERENCE_FILES_FOLDER);
    }

    @Test
    public void testObjectGenerator_allExamples_with_prefix() throws IOException {
        ConjureDefinition def = Conjure.parse(ImmutableList.of(new File("src/test/resources/example-types.yml")));
        List<Path> files = new GenerationCoordinator(
                        MoreExecutors.directExecutor(),
                        ImmutableSet.of(new ObjectGenerator(
                                Options.builder().packagePrefix("test.prefix").build())))
                .emit(def, tempDir);

        assertThatFilesAreTheSame(files, REFERENCE_FILES_FOLDER);
    }

    @Test
    public void testConjureImports() throws IOException {
        ConjureDefinition conjure = Conjure.parse(ImmutableList.of(
                new File("src/test/resources/example-conjure-imports.yml"),
                new File("src/test/resources/example-types.yml"),
                new File("src/test/resources/example-service.yml")));
        File src = Files.createDirectory(tempDir.toPath().resolve("src")).toFile();
        new GenerationCoordinator(
                        MoreExecutors.directExecutor(),
                        ImmutableSet.of(new ObjectGenerator(
                                Options.builder().useImmutableBytes(true).build())))
                .emit(conjure, src);

        // Generated files contain imports
        assertThat(compiledFileContent(src, "test/api/with/imports/ComplexObjectWithImports.java"))
                .contains("import com.palantir.product.StringExample;");

        // Imported files are not generated.
        assertThat(new File(src, "com/palantir/foundry/catalog/api/datasets/BackingFileSystem.java"))
                .doesNotExist();
        assertThat(new File(src, "test/api/StringExample.java")).doesNotExist();
    }

    @Test
    public void testConjureErrors() throws IOException {
        ConjureDefinition def = Conjure.parse(ImmutableList.of(new File("src/test/resources/example-errors.yml")));
        List<Path> files = new GenerationCoordinator(
                        MoreExecutors.directExecutor(),
                        ImmutableSet.of(new ErrorGenerator(
                                Options.builder().useImmutableBytes(true).build())))
                .emit(def, tempDir);

        assertThatFilesAreTheSame(files, REFERENCE_FILES_FOLDER);
    }

    private void assertThatFilesAreTheSame(List<Path> files, String referenceFilesFolder) throws IOException {
        for (Path file : files) {
            Path relativized = tempDir.toPath().relativize(file);
            Path expectedFile = Paths.get(referenceFilesFolder, relativized.toString());
            if (Boolean.valueOf(System.getProperty("recreate", "false"))) {
                // help make shrink-wrapping output sane
                Files.createDirectories(expectedFile.getParent());
                Files.deleteIfExists(expectedFile);
                Files.copy(file, expectedFile);
            }
            assertThat(file).hasSameContentAs(expectedFile);
        }
    }

    private static String compiledFileContent(File srcDir, String clazz) throws IOException {
        return new String(Files.readAllBytes(Paths.get(srcDir.getPath(), clazz)), StandardCharsets.UTF_8);
    }
}
