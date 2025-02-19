/*
 * (c) Copyright 2025 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java.parameterized;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.util.concurrent.MoreExecutors;
import com.palantir.conjure.defs.Conjure;
import com.palantir.conjure.java.GenerationCoordinator;
import com.palantir.conjure.java.parameterized.objects.ParameterizedTestCase;
import com.palantir.conjure.spec.ConjureDefinition;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public final class ParameterizedConjureGenerationTest {
    private static final String REFERENCE_FILES_FOLDER = "src/integrationInput/java";

    @TempDir
    public File tempDir;

    private static List<ParameterizedTestCase> getTestCases() {
        return TestCases.get();
    }

    @ParameterizedTest
    @MethodSource("getTestCases")
    void testGeneratedCode(ParameterizedTestCase testCase) throws IOException {
        ConjureDefinition def =
                Conjure.parse(testCase.files().stream().map(Path::toFile).toList());
        List<Path> files =
                new GenerationCoordinator(MoreExecutors.directExecutor(), testCase.generators()).emit(def, tempDir);

        assertThatFilesAreTheSame(files, testCase);
    }

    private void assertThatFilesAreTheSame(List<Path> files, ParameterizedTestCase testCase) throws IOException {
        boolean recreate = Boolean.parseBoolean(System.getProperty("recreate", "false"));

        if (recreate) {
            // Delete directory contents if exists
            Path testCaseDirectory = Paths.get(REFERENCE_FILES_FOLDER, testCase.packagePrefix());
            if (Files.exists(testCaseDirectory)) {
                try (Stream<Path> filePaths = Files.walk(testCaseDirectory)) {
                    filePaths.filter(Files::isRegularFile).map(Path::toFile).forEach(File::delete);
                }
            }
        }

        for (Path file : files) {
            Path relativized = tempDir.toPath().relativize(file);
            Path expectedFile = Paths.get(REFERENCE_FILES_FOLDER, relativized.toString());
            if (recreate) {
                Files.createDirectories(expectedFile.getParent());
                Files.copy(file, expectedFile);
            }
            assertThat(file).describedAs(testCase.docs()).hasSameTextualContentAs(expectedFile);
        }
    }
}
