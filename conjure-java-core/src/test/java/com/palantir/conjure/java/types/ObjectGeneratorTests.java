/*
 * (c) Copyright 2018 Palantir Technologies Inc. All rights reserved.
 */

package com.palantir.conjure.java.types;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ImmutableList;
import com.palantir.conjure.defs.Conjure;
import com.palantir.conjure.java.FeatureFlags;
import com.palantir.conjure.spec.ConjureDefinition;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public final class ObjectGeneratorTests {

    private static final String REFERENCE_FILES_FOLDER = "src/integrationInput/java";

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void testObjectGenerator_allExamples() throws IOException {
        ConjureDefinition def = Conjure.parse(
                ImmutableList.of(new File("src/test/resources/example-types.yml")));
        List<Path> files = new ObjectGenerator(Collections.singleton(FeatureFlags.UseImmutableBytes))
                .emit(def, folder.getRoot());

        assertThatFilesAreTheSame(files, REFERENCE_FILES_FOLDER);
    }

    @Test
    public void testObjectGenerator_byteBufferCompatibility() throws IOException {
        ConjureDefinition def = Conjure.parse(
                ImmutableList.of(new File("src/test/resources/example-binary-types.yml")));
        List<Path> files = new ObjectGenerator(Collections.emptySet())
                .emit(def, folder.getRoot());

        assertThatFilesAreTheSame(files, REFERENCE_FILES_FOLDER);
    }


    @Test
    public void testObjectGenerator_insensitiveEnum() throws IOException {
        ConjureDefinition def = Conjure.parse(
                ImmutableList.of(new File("src/test/resources/example-compat-enum.yml")));
        List<Path> files = new ObjectGenerator(Collections.singleton(FeatureFlags.CaseInsensitiveEnums))
                .emit(def, folder.getRoot());

        assertThatFilesAreTheSame(files, REFERENCE_FILES_FOLDER);
    }

    @Test
    public void testConjureImports() throws IOException {
        ConjureDefinition conjure = Conjure.parse(
                ImmutableList.of(
                        new File("src/test/resources/example-conjure-imports.yml"),
                        new File("src/test/resources/example-types.yml"),
                        new File("src/test/resources/example-service.yml")));
        File src = folder.newFolder("src");
        ObjectGenerator generator = new ObjectGenerator(Collections.singleton(FeatureFlags.UseImmutableBytes));
        generator.emit(conjure, src);

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
        ConjureDefinition def = Conjure.parse(
                ImmutableList.of(new File("src/test/resources/example-errors.yml")));
        List<Path> files = new ObjectGenerator(Collections.singleton(FeatureFlags.UseImmutableBytes))
                .emit(def, folder.getRoot());

        assertThatFilesAreTheSame(files, REFERENCE_FILES_FOLDER);
    }

    private void assertThatFilesAreTheSame(List<Path> files, String referenceFilesFolder) throws IOException {
        for (Path file : files) {
            Path relativized = folder.getRoot().toPath().relativize(file);
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

    private static String compiledFileContent(File srcDir, String clazz) throws IOException {
        return new String(Files.readAllBytes(Paths.get(srcDir.getPath(), clazz)), StandardCharsets.UTF_8);
    }
}
