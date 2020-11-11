/*
 * (c) Copyright 2020 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.util.concurrent.MoreExecutors;
import com.palantir.conjure.defs.Conjure;
import com.palantir.conjure.java.services.dialogue.DialogueServiceGenerator;
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
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@Execution(ExecutionMode.CONCURRENT)
public final class DialogueServiceGeneratorTests extends TestBase {

    @TempDir
    public File folder;

    private static String compiledFileContent(File srcDir, String clazz) throws IOException {
        return new String(Files.readAllBytes(Paths.get(srcDir.getPath(), clazz)), StandardCharsets.UTF_8);
    }

    @Test
    public void testServiceGeneration_exampleService() throws IOException {
        testServiceGeneration("example-service");
    }

    @Test
    public void testServiceGeneration_cookieService() throws IOException {
        testServiceGeneration("cookie-service");
    }

    @Test
    void testPrefixedServices() throws IOException {
        ConjureDefinition def = Conjure.parse(ImmutableList.of(new File("src/test/resources/example-service.yml")));
        List<Path> files = new GenerationCoordinator(
                        MoreExecutors.directExecutor(),
                        ImmutableSet.of(new DialogueServiceGenerator(
                                Options.builder().packagePrefix("test.prefix").build())))
                .emit(def, folder);
        validateGeneratorOutput(files, Paths.get("src/test/resources/test/api"), ".dialogue.prefix");
    }

    @Test
    public void testConjureImports() throws IOException {
        ConjureDefinition conjure = Conjure.parse(ImmutableList.of(
                new File("src/test/resources/example-conjure-imports.yml"),
                new File("src/test/resources/example-types.yml"),
                new File("src/test/resources/example-service.yml")));
        File src = Files.createDirectory(folder.toPath().resolve("src")).toFile();
        new GenerationCoordinator(
                        MoreExecutors.directExecutor(), ImmutableSet.of(new DialogueServiceGenerator(Options.empty())))
                .emit(conjure, src);

        // Generated files contain imports
        assertThat(compiledFileContent(src, "test/api/with/imports/ImportServiceBlocking.java"))
                .contains("import com.palantir.product.StringExample;");
    }

    @Test
    public void generateEteServices() throws IOException {
        ConjureDefinition def = Conjure.parse(ImmutableList.of(
                new File("src/test/resources/cookie-service.yml"),
                new File("src/test/resources/ete-service.yml"),
                new File("src/test/resources/ete-binary.yml")));
        List<Path> files = new GenerationCoordinator(
                        MoreExecutors.directExecutor(),
                        ImmutableSet.of(new DialogueServiceGenerator(
                                Options.builder().apiVersion("1.2.3").build())))
                .emit(def, folder);
        validateGeneratorOutput(files, Paths.get("src/integrationInput/java/com/palantir/product"));
    }

    private void testServiceGeneration(String conjureFile) throws IOException {
        ConjureDefinition def = Conjure.parse(ImmutableList.of(new File("src/test/resources/" + conjureFile + ".yml")));
        List<Path> files = new GenerationCoordinator(
                        MoreExecutors.directExecutor(), ImmutableSet.of(new DialogueServiceGenerator(Options.empty())))
                .emit(def, folder);
        validateGeneratorOutput(files, Paths.get("src/test/resources/test/api"), ".dialogue");
    }
}
