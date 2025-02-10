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

package com.palantir.conjure.java.expecttest.core;

import static org.assertj.core.api.Assertions.fail;

import com.google.common.collect.ImmutableSet;
import com.google.common.util.concurrent.MoreExecutors;
import com.palantir.conjure.java.GenerationCoordinator;
import com.palantir.conjure.java.Generator;
import com.palantir.conjure.spec.ConjureDefinition;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class SnapshotTester {
    private final String testClass;
    private final String testMethod;

    public SnapshotTester(String testClass, String testMethod) {
        this.testClass = testClass;
        this.testMethod = testMethod;
    }

    public void expect(Path tempDir, Generator codeGenerator, ConjureDefinition def) throws IOException {
        File src = Files.createDirectory(tempDir.resolve("src")).toFile();
        List<Path> files = new GenerationCoordinator(MoreExecutors.directExecutor(), ImmutableSet.of(codeGenerator))
                .emit(def, src);
        Path outputDir = testDirectory();
        // The output dir should actually create directories for the packages in the generated code
        if (!outputDir.toFile().exists()) {
            Files.createDirectories(outputDir);
        }
        for (Path file : files) {
            Path relativePath = src.toPath().relativize(file);
            Path output = outputDir.resolve(relativePath);
            if (Boolean.parseBoolean(System.getProperty("recreate", "false"))) {
                Files.createDirectories(output.getParent());
                Files.deleteIfExists(output);
                Files.copy(file, output);
            }
            if (!readFromFile(file).equals(readFromFile(output))) {
                fail("Generated file does not match expected file: " + relativePath);
            }
        }
    }

    private static String readFromFile(Path file) {
        try {
            return Files.readString(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Path testDirectory() {
        return Path.of("src/expect/" + testClass + "/" + testMethod);
    }

    // Smarter diff
    private String diff(String contents1, String contents2) {}
}
