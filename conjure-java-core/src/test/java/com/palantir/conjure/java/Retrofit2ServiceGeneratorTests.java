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
import com.palantir.conjure.java.services.Retrofit2ServiceGenerator;
import com.palantir.conjure.spec.ConjureDefinition;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public final class Retrofit2ServiceGeneratorTests extends TestBase {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void testCompositionVanilla() throws IOException {
        ConjureDefinition def = Conjure.parse(
                ImmutableList.of(new File("src/test/resources/example-service.yml")));

        List<Path> files = new Retrofit2ServiceGenerator(ImmutableSet.of())
                .emit(def, folder.getRoot());
        validateGeneratorOutput(files, Paths.get("src/test/resources/test/api"), ".retrofit");
    }

    @Test
    public void testCompositionCompletableFuture() throws IOException {
        ConjureDefinition def = Conjure.parse(
                ImmutableList.of(new File("src/test/resources/example-service.yml")));

        List<Path> files = new Retrofit2ServiceGenerator(ImmutableSet.of(FeatureFlags.RetrofitCompletableFutures))
                .emit(def, folder.getRoot());
        validateGeneratorOutput(files, Paths.get("src/test/resources/test/api"), ".retrofit_completable_future");
    }

    @Test
    public void testCompositionListenableFuture() throws IOException {
        ConjureDefinition def = Conjure.parse(
                ImmutableList.of(new File("src/test/resources/example-service.yml")));

        List<Path> files = new Retrofit2ServiceGenerator(ImmutableSet.of(FeatureFlags.RetrofitListenableFutures))
                .emit(def, folder.getRoot());
        validateGeneratorOutput(files, Paths.get("src/test/resources/test/api"), ".retrofit_listenable_future");
    }

    @Test
    public void testConjureImports() throws IOException {
        ConjureDefinition conjure = Conjure.parse(
                ImmutableList.of(
                        new File("src/test/resources/example-conjure-imports.yml"),
                        new File("src/test/resources/example-types.yml"),
                        new File("src/test/resources/example-service.yml")));

        File src = folder.newFolder("src");
        Retrofit2ServiceGenerator generator = new Retrofit2ServiceGenerator(ImmutableSet.of());
        generator.emit(conjure, src);

        // Generated files contain imports
        assertThat(compiledFileContent(src, "test/api/with/imports/ImportServiceRetrofit.java"))
                .contains("import com.palantir.product.StringExample;");
    }

    private static String compiledFileContent(File srcDir, String clazz) throws IOException {
        return new String(Files.readAllBytes(Paths.get(srcDir.getPath(), clazz)), StandardCharsets.UTF_8);
    }

}
