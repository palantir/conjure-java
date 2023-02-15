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

import static com.palantir.logsafe.Preconditions.checkState;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.palantir.conjure.java.util.JavaNameSanitizer;
import com.palantir.conjure.java.util.PrimitiveHelpers;
import com.palantir.conjure.spec.FieldName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collector;
import javax.annotation.Nullable;
import javax.lang.model.element.Modifier;

public final class MethodSpecs {

    private static final String MEMOIZED_HASH_CODE = "memoizedHashCode";

    public static MethodSpec createEquals(TypeName thisClass) {
        ParameterSpec other = ParameterSpec.builder(ClassName.OBJECT, "other")
                .addAnnotation(Nullable.class)
                .build();
        return MethodSpec.methodBuilder("equals")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(other)
                .returns(TypeName.BOOLEAN)
                .addStatement("return this == $1N || ($1N instanceof $2T && equalTo(($2T) $1N))", other, thisClass)
                .build();
    }

    public static MethodSpec createEqualTo(TypeName thisClass, Collection<FieldSpec> fields) {
        return createEqualTo(thisClass, fields, false);
    }

    public static MethodSpec createEqualTo(
            TypeName thisClass, Collection<FieldSpec> fields, boolean useMemoizedHashCode) {
        CodeBlock hashComparison = useMemoizedHashCode
                ? CodeBlock.builder()
                        .beginControlFlow(
                                "if ($1L != 0 && $2L != 0 && $1L != $2L)",
                                "this." + MEMOIZED_HASH_CODE,
                                "other." + MEMOIZED_HASH_CODE)
                        .addStatement("return false")
                        .endControlFlow()
                        .build()
                : CodeBlock.of("");

        CodeBlock equalsTo = fields.isEmpty()
                ? CodeBlock.of("$L", true)
                : CodeBlocks.of(
                        fields.stream().map(MethodSpecs::createEqualsStatement).collect(joining(CodeBlock.of(" && "))));

        return MethodSpec.methodBuilder("equalTo")
                .addModifiers(Modifier.PRIVATE)
                .addParameter(thisClass, "other")
                .returns(TypeName.BOOLEAN)
                .addCode(hashComparison)
                .addStatement("return $L", equalsTo)
                .build();
    }

    private static CodeBlock createEqualsStatement(FieldSpec field) {
        String thisField = "this." + field.name;
        String otherField = "other." + field.name;

        if (field.type.equals(TypeName.DOUBLE)) {
            return CodeBlock.of(
                    "$1T.doubleToLongBits($2L) == $1T.doubleToLongBits($3L)", Double.class, thisField, otherField);
        } else if (PrimitiveHelpers.isPrimitive(field.type)) {
            return CodeBlock.of("$L == $L", thisField, otherField);
        } else if (field.type.equals(ClassName.get(OffsetDateTime.class))) {
            return CodeBlock.of("$L.isEqual($L)", thisField, otherField);
        }

        return CodeBlock.of("$L.equals($L)", thisField, otherField);
    }

    public static MethodSpec createHashCode(Collection<FieldSpec> fields) {
        return MethodSpec.methodBuilder("hashCode")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.INT)
                .addCode(generateHashCode("return", fields))
                .build();
    }

    public static void addCachedHashCode(TypeSpec.Builder typeBuilder, Collection<FieldSpec> fields) {
        FieldSpec field = FieldSpec.builder(
                        TypeName.INT, JavaNameSanitizer.sanitize(MEMOIZED_HASH_CODE), Modifier.PRIVATE)
                .build();
        typeBuilder.addField(field);
        typeBuilder.addMethod(createCachedHashCode(fields, field));
    }

    private static MethodSpec createCachedHashCode(Collection<FieldSpec> fields, FieldSpec field) {
        return MethodSpec.methodBuilder("hashCode")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.INT)
                // Single volatile read
                .addStatement("int result = $N", field)
                .beginControlFlow("if (result == 0)")
                .addCode(generateHashCode("result =", fields))
                // Single volatile write
                .addStatement("$N = result", field)
                .endControlFlow()
                .addStatement("return result")
                .build();
    }

    private static CodeBlock generateHashCode(String prefix, Collection<FieldSpec> fields) {
        String varName = "hash";
        CodeBlock.Builder builder = CodeBlock.builder();
        List<CodeBlock> codeBlocks = computeHashCode(fields);
        if (codeBlocks.size() == 1) {
            builder.addStatement("$L $L", prefix, Iterables.getOnlyElement(codeBlocks));
        } else {
            builder.addStatement("int $N = 1", varName);
            for (CodeBlock codeBlock : codeBlocks) {
                builder.addStatement(codeBlock);
            }
            builder.addStatement("$L $N", prefix, varName);
        }
        return builder.build();
    }

    private static List<CodeBlock> computeHashCode(Collection<FieldSpec> fields) {
        if (fields.size() == 1) {
            return ImmutableList.of(computeHashCode(Iterables.getOnlyElement(fields)));
        }

        String varName = "hash";
        ImmutableList.Builder<CodeBlock> builder = ImmutableList.builder();
        for (FieldSpec field : fields) {
            builder.add(CodeBlock.of("$1N = 31 * $1N + $2L", varName, computeHashCode(field)));
        }
        return builder.build();
    }

    private static CodeBlock computeHashCode(FieldSpec fieldSpec) {
        if (PrimitiveHelpers.isPrimitive(fieldSpec.type)) {
            if (TypeName.INT.equals(fieldSpec.type)) {
                return createHashInput(fieldSpec);
            } else {
                return CodeBlock.of(
                        "$1T.$2N($3L)", PrimitiveHelpers.box(fieldSpec.type), "hashCode", createHashInput(fieldSpec));
            }
        }
        return CodeBlock.of("$L.hashCode()", createHashInput(fieldSpec));
    }

    static MethodSpec createToString(String thisClassName, List<FieldName> fieldNames) {
        return MethodSpec.methodBuilder("toString")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(ClassName.get(String.class))
                .addCode(
                        fieldNames.isEmpty()
                                ? CodeBlock.builder()
                                        .addStatement("return $S", thisClassName + "{}")
                                        .build()
                                : CodeBlock.builder()
                                        .addStatement("return $L", toStringConcatenation(thisClassName, fieldNames))
                                        .build())
                .build();
    }

    private static CodeBlock toStringConcatenation(String thisClassName, List<FieldName> fieldNames) {
        checkState(!fieldNames.isEmpty(), "String concatenation is only necessary if there are fields");
        CodeBlock.Builder builder = CodeBlock.builder()
                .add("$S\n", thisClassName + '{' + fieldNames.get(0).get() + ": ");
        for (int i = 0; i < fieldNames.size(); i++) {
            FieldName fieldName = fieldNames.get(i);
            // The name of the first field is included with the class name
            if (i != 0) {
                builder.add(" + $S", ", " + fieldName.get() + ": ");
            }
            builder.add(" + $N", JavaNameSanitizer.sanitize(fieldName));
        }
        return builder.add(" + '}'").build();
    }

    private static CodeBlock createHashInput(FieldSpec field) {
        if (field.type.equals(ClassName.get(OffsetDateTime.class))) {
            return CodeBlock.of("$N.toInstant()", "this." + field.name);
        }

        // Use 'this.' to avoid collisions with local variables
        return CodeBlock.of("$N", "this." + field.name);
    }

    private static <T> Collector<T, List<T>, List<T>> joining(T delim) {
        return Collector.of(
                ArrayList::new,
                (list, element) -> {
                    if (!list.isEmpty()) {
                        list.add(delim);
                    }
                    list.add(element);
                },
                (list1, list2) -> {
                    if (!list1.isEmpty()) {
                        list1.add(delim);
                    }
                    list1.addAll(list2);
                    return list1;
                });
    }

    private MethodSpecs() {}
}
