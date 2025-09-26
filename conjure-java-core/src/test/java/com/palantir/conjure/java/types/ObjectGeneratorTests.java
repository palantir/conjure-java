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
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public final class ObjectGeneratorTests {

    @TempDir
    public File tempDir;

    @Test
    public void testConjureImports() throws IOException {
        @SuppressWarnings("for-rollout:deprecation")
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
                                .jetbrainsContractAnnotations(true)
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
    public void testErrorSafetyDisagreement() {
        ErrorGenerator errorGenerator = new ErrorGenerator(Options.builder()
                .useImmutableBytes(true)
                .excludeEmptyOptionals(true)
                .jetbrainsContractAnnotations(true)
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

    private static String compiledFileContent(File srcDir, String clazz) throws IOException {
        return new String(Files.readAllBytes(Paths.get(srcDir.getPath(), clazz)), StandardCharsets.UTF_8);
    }
}
