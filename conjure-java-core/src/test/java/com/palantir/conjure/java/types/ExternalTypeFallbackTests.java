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
import com.palantir.conjure.java.services.dialogue.DialogueServiceGenerator;
import com.palantir.conjure.spec.ConjureDefinition;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public final class ExternalTypeFallbackTests {

    private static final String REFERENCE_FILES_FOLDER = "src/integrationInput/java";

    @TempDir
    public File tempDir;

    @Test
    public void testExternalFallbackTypes() throws IOException {
        Options options = Options.builder()
                .useImmutableBytes(true)
                .strictObjects(true)
                .nonNullCollections(true)
                .excludeEmptyOptionals(true)
                .unionsWithUnknownValues(true)
                .jetbrainsContractAnnotations(true)
                .externalFallbackTypes(true)
                .build();
        ConjureDefinition def = Conjure.parse(ImmutableList.of(new File("src/test/resources/external-types.yml")));
        List<Path> files = new GenerationCoordinator(
                        MoreExecutors.directExecutor(),
                        ImmutableSet.of(new ObjectGenerator(options), new DialogueServiceGenerator(options)),
                        options)
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
}
