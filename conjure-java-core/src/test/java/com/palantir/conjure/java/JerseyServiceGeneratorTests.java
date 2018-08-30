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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.palantir.conjure.java.services.JerseyServiceGenerator;
import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.remoting3.ext.jackson.ObjectMappers;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public final class JerseyServiceGeneratorTests extends TestBase {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private static ObjectMapper OBJECT_MAPPER = ObjectMappers.newClientObjectMapper();

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
    public void testServiceGeneration_exampleService_requireNotNullAuthHeadersAndRequestBodies() throws IOException {
        ConjureDefinition def = OBJECT_MAPPER.readValue(
                this.getClass().getClassLoader().getResourceAsStream("example-service.conjure.json"),
                ConjureDefinition.class);
        List<Path> files = new JerseyServiceGenerator(
                ImmutableSet.of(FeatureFlags.RequireNotNullAuthAndBodyParams)).emit(def, folder.getRoot());

        for (Path file : files) {
            if (Boolean.valueOf(System.getProperty("recreate", "false"))) {
                Path output = Paths.get("src/test/resources/test/api/" + file.getFileName() + ".jersey");
                Files.delete(output);
                Files.copy(file, output);
            }

            assertThat(readFromFile(file)).isEqualTo(
                    readFromFile(Paths.get(
                            "src/test/resources/test/api/" + file.getFileName() + ".jersey_require_not_null")));
        }
    }

    @Test
    public void testConjureImports() throws IOException {
        ObjectNode importsNode = (ObjectNode) OBJECT_MAPPER.readTree(
                this.getClass().getClassLoader().getResourceAsStream("example-conjure-imports.conjure.json"));
        ObjectNode typesNode = (ObjectNode) OBJECT_MAPPER.readTree(
                this.getClass().getClassLoader().getResourceAsStream("example-types.conjure.json"));

        JsonNode combined = typesNode.setAll(importsNode);
        ConjureDefinition def = OBJECT_MAPPER.readValue(
                OBJECT_MAPPER.writeValueAsString(combined),
                ConjureDefinition.class);

        File src = folder.newFolder("src");
        JerseyServiceGenerator generator = new JerseyServiceGenerator(Collections.emptySet());
        generator.emit(def, src);

        // Generated files contain imports
        assertThat(compiledFileContent(src, "test/api/with/imports/ImportService.java"))
                .contains("import com.palantir.product.StringExample;");
    }

    @Test
    public void testBinaryReturnInputStream() throws IOException {
        ConjureDefinition def = ObjectMappers.newClientObjectMapper().readValue(
                this.getClass().getClassLoader().getResourceAsStream("example-binary.conjure.json"),
                ConjureDefinition.class);
        List<Path> files = new JerseyServiceGenerator(Collections.emptySet())
                .emit(def, folder.getRoot());

        for (Path file : files) {
            if (Boolean.valueOf(System.getProperty("recreate", "false"))) {
                Path output = Paths.get("src/test/resources/test/api/" + file.getFileName() + ".jersey.binary");
                Files.delete(output);
                Files.copy(file, output);
            }

            assertThat(readFromFile(file)).isEqualTo(
                    readFromFile(Paths.get("src/test/resources/test/api/" + file.getFileName() + ".jersey.binary")));
        }
    }

    @Test
    public void testBinaryReturnResponse() throws IOException {
        ConjureDefinition def = ObjectMappers.newClientObjectMapper().readValue(
                this.getClass().getClassLoader().getResourceAsStream("example-binary.conjure.json"),
                ConjureDefinition.class);

        List<Path> files = new JerseyServiceGenerator(ImmutableSet.of(FeatureFlags.JerseyBinaryAsResponse))
                .emit(def, folder.getRoot());

        for (Path file : files) {
            if (Boolean.valueOf(System.getProperty("recreate", "false"))) {
                Path output = Paths.get(
                        "src/test/resources/test/api/" + file.getFileName() + ".jersey.binary_as_response");
                Files.delete(output);
                Files.copy(file, output);
            }

            assertThat(readFromFile(file)).isEqualTo(
                    readFromFile(Paths.get(
                            "src/test/resources/test/api/" + file.getFileName() + ".jersey.binary_as_response")));
        }
    }

    private void testServiceGeneration(String conjureFile) throws IOException {
        ConjureDefinition def = ObjectMappers.newClientObjectMapper().readValue(
                this.getClass().getClassLoader().getResourceAsStream(conjureFile + ".conjure.json"),
                ConjureDefinition.class);

        List<Path> files = new JerseyServiceGenerator(Collections.emptySet()).emit(def, folder.getRoot());
        for (Path file : files) {
            if (Boolean.valueOf(System.getProperty("recreate", "false"))) {
                Path output = Paths.get("src/test/resources/test/api/" + file.getFileName() + ".jersey");
                Files.delete(output);
                Files.copy(file, output);
            }

            assertThat(readFromFile(file)).isEqualTo(
                    readFromFile(Paths.get("src/test/resources/test/api/" + file.getFileName() + ".jersey")));
        }
    }

}
