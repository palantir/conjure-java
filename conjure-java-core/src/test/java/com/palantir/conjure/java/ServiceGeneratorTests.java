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
import com.google.common.util.concurrent.MoreExecutors;
import com.palantir.conjure.defs.Conjure;
import com.palantir.conjure.java.services.JerseyServiceGenerator;
import com.palantir.conjure.spec.ConjureDefinition;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@Execution(ExecutionMode.CONCURRENT)
public final class ServiceGeneratorTests extends TestBase {

    @TempDir
    public File folder;

    private static String compiledFileContent(File srcDir, String clazz) throws IOException {
        return new String(Files.readAllBytes(Paths.get(srcDir.getPath(), clazz)), StandardCharsets.UTF_8);
    }

    @Test
    public void testConjureIncubatingJavadoc() throws IOException {
        ConjureDefinition conjure =
                Conjure.parse(ImmutableList.of(new File("src/test/resources/example-conjure-incubating.yml")));
        File src = Files.createDirectory(folder.toPath().resolve("src")).toFile();
        new GenerationCoordinator(
                        MoreExecutors.directExecutor(), ImmutableSet.of(new JerseyServiceGenerator(Options.empty())))
                .emit(conjure, src);

        // Generated files contain imports
        assertThat(compiledFileContent(src, "test/api/with/imports/IncubatingEndpoint.java"))
                .contains("* @Incubating To be removed after 1.2.x.");
    }
}
