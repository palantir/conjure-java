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

import com.google.common.collect.ImmutableSet;
import com.google.common.util.concurrent.MoreExecutors;
import com.palantir.conjure.defs.Conjure;
import com.palantir.conjure.defs.ConjureArgs;
import com.palantir.conjure.defs.SafetyDeclarationRequirements;
import com.palantir.conjure.java.services.dialogue.DialogueServiceGenerator;
import com.palantir.conjure.spec.ConjureDefinition;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@Execution(ExecutionMode.CONCURRENT)
public final class DialogueServiceGeneratorTests extends TestBase {
    private static final String REFERENCE_FILES_FOLDER = "src/integrationInput/java";

    @TempDir
    public File folder;

    private static String compiledFileContent(File srcDir, String clazz) throws IOException {
        return new String(Files.readAllBytes(Paths.get(srcDir.getPath(), clazz)), StandardCharsets.UTF_8);
    }

    private String generateExampleTestService(Options options, String outputDirectory) throws IOException {
        ConjureDefinition conjure = Conjure.parse(ConjureArgs.builder()
                .addDefinitions(
                        new File("src/test/resources/example-types.yml"),
                        new File("src/test/resources/example-service.yml"))
                .safetyDeclarations(SafetyDeclarationRequirements.ALLOWED)
                .build());
        File src = generateDialogueServices(conjure, options, outputDirectory);
        return compiledFileContent(src, "com/palantir/another/TestServiceBlocking.java");
    }

    private File generateDialogueServices(ConjureDefinition conjure, Options options, String outputDirectory)
            throws IOException {
        File src =
                Files.createDirectory(folder.toPath().resolve(outputDirectory)).toFile();
        new GenerationCoordinator(
                        MoreExecutors.directExecutor(), ImmutableSet.of(new DialogueServiceGenerator(options)))
                .emit(conjure, src);
        return src;
    }

    @Test
    public void testConjureImports() throws IOException {
        ConjureDefinition conjure = Conjure.parse(ConjureArgs.builder()
                .addDefinitions(
                        new File("src/test/resources/example-conjure-imports.yml"),
                        new File("src/test/resources/example-types.yml"),
                        new File("src/test/resources/example-service.yml"))
                .safetyDeclarations(SafetyDeclarationRequirements.ALLOWED)
                .build());
        File src = Files.createDirectory(folder.toPath().resolve("src")).toFile();
        new GenerationCoordinator(
                        MoreExecutors.directExecutor(), ImmutableSet.of(new DialogueServiceGenerator(Options.empty())))
                .emit(conjure, src);

        // Generated files contain imports
        assertThat(compiledFileContent(src, "test/api/with/imports/ImportServiceBlocking.java"))
                .contains("import com.palantir.product.StringExample;");
    }

    @Test
    public void testSharedRequestAndResponseTypes() throws IOException {
        String testService = generateExampleTestService(Options.empty(), "shared-types");

        assertThat(testService)
                .as("Identical body types share a serializer")
                .containsOnlyOnce("private final Serializer<String> stringSerializer");
        assertThat(testService)
                .as("A type used in both requests and responses shares a TypeMarker")
                .containsOnlyOnce("new TypeMarker<Optional<String>>() {}")
                .contains(
                        "_runtime.bodySerDe().deserializer(optionalStringTypeMarker)",
                        "_runtime.bodySerDe().serializer(optionalStringTypeMarker)");
    }

    @Test
    public void testBinaryBodyAliasesDoNotProduceSerializerFields() throws IOException {
        String testService = generateExampleTestService(Options.empty(), "binary-aliases");

        assertThat(testService)
                .doesNotContain(
                        "private final Serializer<InputStream>",
                        "TypeMarker<InputStream>",
                        "private final Serializer<BinaryRequestBody>",
                        "TypeMarker<BinaryRequestBody>")
                .contains("_runtime.bodySerDe().serialize(input)");
    }

    @Test
    public void testEmptyResponseFields() throws IOException {
        String standardService = generateExampleTestService(Options.empty(), "standard-empty-response");
        assertThat(standardService)
                .as("Standard empty responses do not need a TypeMarker")
                .doesNotContain("TypeMarker<Void>")
                .containsOnlyOnce("private final Deserializer<Void> voidDeserializer")
                .contains("_runtime.bodySerDe().emptyBodyDeserializer()");

        Options errorParameterFormatRespecting = Options.builder()
                .generateErrorParameterFormatRespectingDialogueInterfaces(true)
                .build();
        String errorRespectingService =
                generateExampleTestService(errorParameterFormatRespecting, "error-respecting-empty-response");
        assertThat(errorRespectingService)
                .as("Error-respecting empty responses need shared exception deserialization fields")
                .containsOnlyOnce("new TypeMarker<Void>() {}")
                .containsOnlyOnce("private static final ExceptionDeserializerArgs<Void> voidExceptionArgs")
                .containsOnlyOnce("private final Deserializer<Void> voidDeserializer")
                .contains("_runtime.bodySerDe().emptyBodyDeserializer(voidExceptionArgs)");
    }

    @Test
    public void testParameterizedAndFlattenedTypeNamesAreDeconflicted() throws IOException {
        ConjureDefinition conjure = Conjure.parse(ConjureArgs.builder()
                .addDefinitions(new File("src/test/resources/dialogue-serde-field-name-conflicts.yml"))
                .safetyDeclarations(SafetyDeclarationRequirements.ALLOWED)
                .build());
        File src = generateDialogueServices(conjure, Options.empty(), "field-name-conflicts");
        String service = compiledFileContent(src, "test/dialogue/SerDeServiceBlocking.java");

        assertThat(service)
                .contains("private static final TypeMarker<List<String>> listStringTypeMarker")
                .containsPattern("private static final TypeMarker<ListString> listString[a-z0-9]+TypeMarker")
                .contains("private final Serializer<List<String>> listStringSerializer")
                .containsPattern("private final Serializer<ListString> listString[a-z0-9]+Serializer")
                .contains("private final Deserializer<List<String>> listStringDeserializer")
                .containsPattern("private final Deserializer<ListString> listString[a-z0-9]+Deserializer");
    }

    @Test
    public void testServiceGeneration_excludeDialogueAsyncInterfaces() throws IOException {
        Path testCaseDirectory = Paths.get(REFERENCE_FILES_FOLDER, "excludeasyncinterfaces");
        try (Stream<Path> filePaths = Files.walk(testCaseDirectory)) {
            List<String> fileNames = filePaths
                    .filter(Files::isRegularFile)
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .toList();
            assertThat(fileNames)
                    .noneMatch(name -> name.toLowerCase(Locale.ROOT).contains("async"));
        }
    }
}
