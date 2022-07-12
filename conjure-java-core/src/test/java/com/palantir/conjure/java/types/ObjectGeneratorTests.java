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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.util.concurrent.MoreExecutors;
import com.palantir.conjure.defs.Conjure;
import com.palantir.conjure.java.GenerationCoordinator;
import com.palantir.conjure.java.Options;
import com.palantir.conjure.spec.AliasDefinition;
import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.conjure.spec.ErrorCode;
import com.palantir.conjure.spec.ErrorDefinition;
import com.palantir.conjure.spec.ErrorNamespace;
import com.palantir.conjure.spec.FieldDefinition;
import com.palantir.conjure.spec.FieldName;
import com.palantir.conjure.spec.LogSafety;
import com.palantir.conjure.spec.PrimitiveType;
import com.palantir.conjure.spec.Type;
import com.palantir.conjure.spec.TypeDefinition;
import com.palantir.conjure.spec.TypeName;
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
                                .excludeEmptyOptionals(true)
                                .unionsWithUnknownValues(true)
                                .build())))
                .emit(def, tempDir);

        assertThatFilesAreTheSame(files, REFERENCE_FILES_FOLDER);
    }

    @Test
    public void testSealedUnions() throws IOException {
        ConjureDefinition def =
                Conjure.parse(ImmutableList.of(new File("src/test/resources/example-sealed-unions.yml")));
        List<Path> files = new GenerationCoordinator(
                        MoreExecutors.directExecutor(),
                        ImmutableSet.of(new ObjectGenerator(Options.builder()
                                .useImmutableBytes(true)
                                .strictObjects(true)
                                .nonNullCollections(true)
                                .excludeEmptyOptionals(true)
                                .unionsWithUnknownValues(true)
                                .sealedUnions(true)
                                .packagePrefix("sealedunions")
                                .build())))
                .emit(def, tempDir);

        assertThatFilesAreTheSame(files, "src/test/resources/sealedunions");
    }

    @Test
    public void testObjectGenerator_byteBufferCompatibility() throws IOException {
        ConjureDefinition def =
                Conjure.parse(ImmutableList.of(new File("src/test/resources/example-binary-types.yml")));
        List<Path> files = new GenerationCoordinator(
                        MoreExecutors.directExecutor(),
                        ImmutableSet.of(new ObjectGenerator(
                                Options.builder().excludeEmptyOptionals(true).build())))
                .emit(def, tempDir);

        assertThatFilesAreTheSame(files, REFERENCE_FILES_FOLDER);
    }

    @Test
    public void testObjectGenerator_allExamples_with_prefix() throws IOException {
        ConjureDefinition def = Conjure.parse(ImmutableList.of(new File("src/test/resources/example-types.yml")));
        List<Path> files = new GenerationCoordinator(
                        MoreExecutors.directExecutor(),
                        ImmutableSet.of(new ObjectGenerator(Options.builder()
                                .packagePrefix("test.prefix")
                                .excludeEmptyOptionals(true)
                                .build())))
                .emit(def, tempDir);

        assertThatFilesAreTheSame(files, REFERENCE_FILES_FOLDER);
    }

    @Test
    public void testObjectGenerator_stagedBuilder() throws IOException {
        ConjureDefinition def =
                Conjure.parse(ImmutableList.of(new File("src/test/resources/example-staged-types.yml")));
        List<Path> files = new GenerationCoordinator(
                        MoreExecutors.directExecutor(),
                        ImmutableSet.of(new ObjectGenerator(Options.builder()
                                .useStagedBuilders(true)
                                .excludeEmptyOptionals(true)
                                .build())))
                .emit(def, tempDir);

        assertThatFilesAreTheSame(files, REFERENCE_FILES_FOLDER);
    }

    @Test
    public void testObjectGenerator_excludeEmptyCollections() throws IOException {
        ConjureDefinition def =
                Conjure.parse(ImmutableList.of(new File("src/test/resources/exclude-empty-collections.yml")));
        List<Path> files = new GenerationCoordinator(
                        MoreExecutors.directExecutor(),
                        ImmutableSet.of(new ObjectGenerator(
                                Options.builder().excludeEmptyCollections(true).build())))
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
                        ImmutableSet.of(new ObjectGenerator(Options.builder()
                                .useImmutableBytes(true)
                                .excludeEmptyOptionals(true)
                                .build())))
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
        ConjureDefinition def = Conjure.parse(ImmutableList.of(
                new File("src/test/resources/example-errors.yml"),
                new File("src/test/resources/example-errors-other.yml")));
        List<Path> files = new GenerationCoordinator(
                        MoreExecutors.directExecutor(),
                        ImmutableSet.of(new ErrorGenerator(Options.builder()
                                .useImmutableBytes(true)
                                .excludeEmptyOptionals(true)
                                .build())))
                .emit(def, tempDir);

        assertThatFilesAreTheSame(files, REFERENCE_FILES_FOLDER);
    }

    @Test
    public void testErrorSafetyDisagreement() {
        ErrorGenerator errorGenerator = new ErrorGenerator(Options.builder()
                .useImmutableBytes(true)
                .excludeEmptyOptionals(true)
                .build());
        TypeName unsafeAliasName = TypeName.of("UnsafeAlias", "com.palantir.product");
        TypeDefinition unsafeAlias = TypeDefinition.alias(AliasDefinition.builder()
                .typeName(unsafeAliasName)
                .safety(LogSafety.UNSAFE)
                .alias(Type.primitive(PrimitiveType.STRING))
                .build());
        ConjureDefinition conjureDefinition = ConjureDefinition.builder()
                .version(1)
                .errors(ErrorDefinition.builder()
                        .errorName(TypeName.of("Name", "com.palantir.product"))
                        .code(ErrorCode.CUSTOM_SERVER)
                        .namespace(ErrorNamespace.of("Service"))
                        .safeArgs(FieldDefinition.builder()
                                .fieldName(FieldName.of("field"))
                                .type(Type.reference(unsafeAliasName))
                                .build())
                        .build())
                .types(unsafeAlias)
                .build();
        assertThatThrownBy(errorGenerator.generate(conjureDefinition)::count)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Cannot use UNSAFE type com.palantir.product.UnsafeAlias "
                        + "as a SAFE parameter in error Name -> field");
    }

    @Test
    public void testStrictFalse() throws IOException {
        ConjureDefinition def =
                Conjure.parse(ImmutableList.of(new File("src/test/resources/example-types-strict-objects.yml")));
        List<Path> files = new GenerationCoordinator(
                        MoreExecutors.directExecutor(),
                        ImmutableSet.of(new ObjectGenerator(Options.builder()
                                .useImmutableBytes(true)
                                .strictObjects(false)
                                .build())))
                .emit(def, tempDir);

        assertThatFilesAreTheSame(files, REFERENCE_FILES_FOLDER);
    }

    private void assertThatFilesAreTheSame(List<Path> files, String referenceFilesFolder) throws IOException {
        for (Path file : files) {
            Path relativized = tempDir.toPath().relativize(file);
            Path expectedFile = Paths.get(referenceFilesFolder, relativized.toString());
            if (true || Boolean.valueOf(System.getProperty("recreate", "false"))) {
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
