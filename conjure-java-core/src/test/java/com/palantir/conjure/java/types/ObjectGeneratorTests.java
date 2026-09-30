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

import com.google.common.collect.ImmutableSet;
import com.google.common.util.concurrent.MoreExecutors;
import com.palantir.conjure.defs.Conjure;
import com.palantir.conjure.defs.ConjureArgs;
import com.palantir.conjure.defs.SafetyDeclarationRequirements;
import com.palantir.conjure.java.GenerationCoordinator;
import com.palantir.conjure.java.Options;
import com.palantir.conjure.spec.AliasDefinition;
import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.conjure.spec.EnumDefinition;
import com.palantir.conjure.spec.EnumValueDefinition;
import com.palantir.conjure.spec.ErrorCode;
import com.palantir.conjure.spec.ErrorDefinition;
import com.palantir.conjure.spec.ErrorNamespace;
import com.palantir.conjure.spec.ExternalReference;
import com.palantir.conjure.spec.FieldDefinition;
import com.palantir.conjure.spec.FieldName;
import com.palantir.conjure.spec.ListType;
import com.palantir.conjure.spec.LogSafety;
import com.palantir.conjure.spec.MapType;
import com.palantir.conjure.spec.ObjectDefinition;
import com.palantir.conjure.spec.OptionalType;
import com.palantir.conjure.spec.PrimitiveType;
import com.palantir.conjure.spec.SetType;
import com.palantir.conjure.spec.Type;
import com.palantir.conjure.spec.TypeDefinition;
import com.palantir.conjure.spec.TypeName;
import com.palantir.conjure.spec.UnionDefinition;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public final class ObjectGeneratorTests {

    @TempDir
    public File tempDir;

    @Test
    public void testConjureImports() throws IOException {
        ConjureDefinition conjure = Conjure.parse(ConjureArgs.builder()
                .addDefinitions(
                        new File("src/test/resources/example-conjure-imports.yml"),
                        new File("src/test/resources/example-types.yml"),
                        new File("src/test/resources/example-service.yml"))
                .safetyDeclarations(SafetyDeclarationRequirements.ALLOWED)
                .build());
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

    @Test
    void nestedComparator() {
        TypeName comparableAliasName = TypeName.of("ComparableAlias", "example");
        TypeName nonComparableAliasName = TypeName.of("NonComparableAlias", "example");
        TypeName objectName = TypeName.of("ReferencedObject", "example");
        TypeName enumName = TypeName.of("ReferencedEnum", "example");
        TypeName unionName = TypeName.of("ReferencedUnion", "example");
        FieldDefinition stringField = FieldDefinition.builder()
                .fieldName(FieldName.of("value"))
                .type(Type.primitive(PrimitiveType.STRING))
                .build();

        // References in the lists below must resolve to definitions in the generated schema.
        List<TypeDefinition> referencedTypes = List.of(
                TypeDefinition.alias(AliasDefinition.builder()
                        .typeName(comparableAliasName)
                        .alias(Type.primitive(PrimitiveType.DOUBLE))
                        .build()),
                TypeDefinition.alias(AliasDefinition.builder()
                        .typeName(nonComparableAliasName)
                        .alias(Type.primitive(PrimitiveType.BOOLEAN))
                        .build()),
                TypeDefinition.object(ObjectDefinition.builder()
                        .typeName(objectName)
                        .fields(stringField)
                        .build()),
                TypeDefinition.enum_(EnumDefinition.builder()
                        .typeName(enumName)
                        .values(EnumValueDefinition.builder().value("EXAMPLE").build())
                        .build()),
                TypeDefinition.union(UnionDefinition.builder()
                        .typeName(unionName)
                        .union(stringField)
                        .build()));

        List<Type> hasComparison = List.of(
                Type.primitive(PrimitiveType.DOUBLE),
                Type.primitive(PrimitiveType.INTEGER),
                Type.primitive(PrimitiveType.SAFELONG),
                Type.primitive(PrimitiveType.STRING),
                Type.primitive(PrimitiveType.DATETIME),
                Type.primitive(PrimitiveType.UUID),
                Type.reference(comparableAliasName));
        List<Type> noComparison = List.of(
                Type.primitive(PrimitiveType.BOOLEAN),
                Type.primitive(PrimitiveType.BINARY),
                Type.primitive(PrimitiveType.ANY),
                Type.primitive(PrimitiveType.RID),
                Type.primitive(PrimitiveType.BEARERTOKEN),
                Type.optional(OptionalType.of(Type.primitive(PrimitiveType.DOUBLE))),
                Type.list(ListType.of(Type.primitive(PrimitiveType.STRING))),
                Type.set(SetType.of(Type.primitive(PrimitiveType.STRING))),
                Type.map(MapType.of(Type.primitive(PrimitiveType.STRING), Type.primitive(PrimitiveType.DOUBLE))),
                Type.reference(nonComparableAliasName),
                Type.reference(objectName),
                Type.reference(enumName),
                Type.reference(unionName),
                Type.external(ExternalReference.builder()
                        .externalReference(TypeName.of("Long", "java.lang"))
                        .fallback(Type.primitive(PrimitiveType.STRING))
                        .build()));

        for (Type type : hasComparison) {
            String outerSource = generateNestedAliasSource(type, referencedTypes);
            assertThat(outerSource)
                    .as("An alias of an alias of %s should support comparison", type)
                    .contains(
                            "implements Comparable<OuterExample>",
                            "public int compareTo(OuterExample other)",
                            "return value.compareTo(other.get());");
        }

        for (Type type : noComparison) {
            String outerSource = generateNestedAliasSource(type, referencedTypes);
            assertThat(outerSource)
                    .as("An alias of an alias of %s should not support comparison", type)
                    .doesNotContain("compareTo(", "Comparable<");
        }
    }

    private static String generateNestedAliasSource(Type baseType, List<TypeDefinition> referencedTypes) {
        TypeName innerName = TypeName.of("InnerExample", "example");
        TypeName outerName = TypeName.of("OuterExample", "example");
        AliasDefinition inner =
                AliasDefinition.builder().typeName(innerName).alias(baseType).build();
        AliasDefinition outer = AliasDefinition.builder()
                .typeName(outerName)
                .alias(Type.reference(innerName))
                .build();
        ConjureDefinition definition = ConjureDefinition.builder()
                .version(1)
                .types(referencedTypes)
                .types(TypeDefinition.alias(inner))
                .types(TypeDefinition.alias(outer))
                .build();

        return new ObjectGenerator(Options.builder().build())
                .generate(definition)
                .filter(file -> file.typeSpec().name().equals(outerName.getName()))
                .map(Object::toString)
                .findFirst()
                .orElseThrow();
    }

    private static String compiledFileContent(File srcDir, String clazz) throws IOException {
        return new String(Files.readAllBytes(Paths.get(srcDir.getPath(), clazz)), StandardCharsets.UTF_8);
    }
}
