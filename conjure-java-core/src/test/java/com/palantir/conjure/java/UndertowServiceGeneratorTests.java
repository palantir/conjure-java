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

package com.palantir.conjure.java;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.palantir.conjure.defs.Conjure;
import com.palantir.conjure.java.services.ServiceGenerator;
import com.palantir.conjure.java.services.UndertowServiceGenerator;
import com.palantir.conjure.spec.ConjureDefinition;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@Execution(ExecutionMode.CONCURRENT)
public final class UndertowServiceGeneratorTests extends TestBase {
    @TempDir
    public File tempDir;

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
    public void testConjureImports() throws IOException {
        ConjureDefinition conjure = Conjure.parse(ImmutableList.of(
                new File("src/test/resources/example-conjure-imports.yml"),
                new File("src/test/resources/example-types.yml"),
                new File("src/test/resources/example-service.yml")));
        File src = Files.createDirectory(tempDir.toPath().resolve("src")).toFile();
        ServiceGenerator generator = new UndertowServiceGenerator(Collections.emptySet());
        generator.emit(conjure, src);

        // Generated files contain imports
        assertThat(compiledFileContent(src, "test/api/with/imports/ImportService.java"))
                .contains("import com.palantir.product.StringExample;");
    }

    @Test
    public void testBinaryReturnInputStream() throws IOException {
        ConjureDefinition def = Conjure.parse(ImmutableList.of(new File("src/test/resources/example-binary.yml")));
        List<Path> files = new UndertowServiceGenerator(Collections.emptySet()).emit(def, tempDir);
        validateGeneratorOutput(files, Paths.get("src/test/resources/test/api"), ".undertow.binary");
    }

    @Test
    public void testEndpointWithNameCollisions() throws IOException {
        ConjureDefinition conjure =
                Conjure.parse(ImmutableList.of(new File("src/test/resources/dangerous-name-service.yml")));
        File src = Files.createDirectory(tempDir.toPath().resolve("src")).toFile();
        ServiceGenerator generator =
                new UndertowServiceGenerator(Collections.singleton(FeatureFlags.undertowServicePrefix()));
        List<Path> files = generator.emit(conjure, src);
        validateGeneratorOutput(files, Paths.get("src/integrationInput/java/com/palantir/product"));
    }

    @Test
    public void testIndividualMethodAsync() throws IOException {
        ConjureDefinition def =
                Conjure.parse(ImmutableList.of(new File("src/test/resources/undertow-async-endpoint.yml")));
        List<Path> files = new UndertowServiceGenerator(
                        ImmutableSet.of(FeatureFlags.experimentalUndertowAsyncMarkers()))
                .emit(def, tempDir);
        validateGeneratorOutput(files, Paths.get("src/test/resources/test/api"), ".undertow.async");
    }

    @Test
    public void testIndividualMethodAsyncWithoutFlag() throws IOException {
        ConjureDefinition def =
                Conjure.parse(ImmutableList.of(new File("src/test/resources/undertow-async-endpoint.yml")));
        // Without FeatureFlags.ExperimentalUndertowAsyncMarkers this should generate blocking methods
        List<Path> files = new UndertowServiceGenerator(ImmutableSet.of()).emit(def, tempDir);
        validateGeneratorOutput(files, Paths.get("src/test/resources/test/api"), ".undertow");
    }

    private void testServiceGeneration(String conjureFile) throws IOException {
        ConjureDefinition def = Conjure.parse(ImmutableList.of(new File("src/test/resources/" + conjureFile + ".yml")));
        List<Path> files = new UndertowServiceGenerator(ImmutableSet.of()).emit(def, tempDir);
        validateGeneratorOutput(files, Paths.get("src/test/resources/test/api"), ".undertow");
    }
}
