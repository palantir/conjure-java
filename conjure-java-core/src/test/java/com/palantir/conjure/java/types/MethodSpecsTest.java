/*
 * (c) Copyright 2022 Palantir Technologies Inc. All rights reserved.
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

import com.google.common.collect.ImmutableList;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.util.List;
import org.junit.jupiter.api.Test;

class MethodSpecsTest {

    @Test
    void computeHashCode_singleField() {
        assertThat(MethodSpecs.computeHashCode(
                        FieldSpec.builder(ClassName.get(String.class), "value").build()))
                .asString()
                .isEqualTo("java.util.Objects.hashCode(this.value)");
        assertThat(MethodSpecs.computeHashCode(
                        FieldSpec.builder(TypeName.INT, "value").build()))
                .asString()
                .isEqualTo("java.lang.Integer.hashCode(this.value)");
        assertThat(MethodSpecs.computeHashCode(
                        FieldSpec.builder(TypeName.LONG, "value").build()))
                .asString()
                .isEqualTo("java.lang.Long.hashCode(this.value)");
    }

    @Test
    void computeHashCode_multipleFields() {
        List<FieldSpec> fields = ImmutableList.of(
                FieldSpec.builder(TypeName.INT, "primitiveInt").build(),
                FieldSpec.builder(ClassName.get(String.class), "string").build(),
                FieldSpec.builder(TypeName.LONG, "primitiveLong").build(),
                FieldSpec.builder(ClassName.get(Integer.class), "boxedInteger").build());
        assertThat(MethodSpecs.computeHashCode(fields))
                .asString()
                .isEqualTo("int hash = 4441;\n"
                        + "hash += (hash << 5) + java.lang.Integer.hashCode(this.primitiveInt);\n"
                        + "hash += (hash << 5) + java.util.Objects.hashCode(this.string);\n"
                        + "hash += (hash << 5) + java.lang.Long.hashCode(this.primitiveLong);\n"
                        + "hash += (hash << 5) + java.util.Objects.hashCode(this.boxedInteger);\n"
                        + "return hash;\n");
    }

    @Test
    void createComputeHashCode_single() {
        List<FieldSpec> fields = ImmutableList.of(
                FieldSpec.builder(ClassName.get(String.class), "value").build());
        assertThat(MethodSpecs.createComputeHashCode(fields))
                .asString()
                .isEqualTo("private int computeHashCode() {\n"
                        + "  return java.util.Objects.hashCode(this.value);\n"
                        + "}\n");
    }

    @Test
    void createComputeHashCode_multiple() {
        List<FieldSpec> fields = ImmutableList.of(
                FieldSpec.builder(TypeName.INT, "primitiveInt").build(),
                FieldSpec.builder(ClassName.get(String.class), "string").build(),
                FieldSpec.builder(ClassName.get(Integer.class), "boxedInteger").build());
        assertThat(MethodSpecs.createComputeHashCode(fields))
                .asString()
                .isEqualTo("private int computeHashCode() {\n"
                        + "  int hash = 4441;\n"
                        + "  hash += (hash << 5) + java.lang.Integer.hashCode(this.primitiveInt);\n"
                        + "  hash += (hash << 5) + java.util.Objects.hashCode(this.string);\n"
                        + "  hash += (hash << 5) + java.util.Objects.hashCode(this.boxedInteger);\n"
                        + "  return hash;\n"
                        + "}\n");
    }

    @Test
    void createHashCode() {
        List<FieldSpec> fields = ImmutableList.of(
                FieldSpec.builder(TypeName.INT, "primitiveInt").build(),
                FieldSpec.builder(ClassName.get(String.class), "string").build(),
                FieldSpec.builder(ClassName.get(Integer.class), "boxedInteger").build());
        assertThat(MethodSpecs.createHashCode(fields))
                .asString()
                .isEqualTo(
                        "@java.lang.Override\n" //
                                + "public int hashCode() {\n" //
                                + "  int hash = 4441;\n"
                                + "  hash += (hash << 5) + java.lang.Integer.hashCode(this.primitiveInt);\n"
                                + "  hash += (hash << 5) + java.util.Objects.hashCode(this.string);\n"
                                + "  hash += (hash << 5) + java.util.Objects.hashCode(this.boxedInteger);\n"
                                + "  return hash;\n"
                                + "}\n");
    }

    @Test
    void generateClass() {
        List<FieldSpec> fields = ImmutableList.of(
                FieldSpec.builder(TypeName.INT, "primitiveInt").build(),
                FieldSpec.builder(ClassName.get(String.class), "string").build(),
                FieldSpec.builder(ClassName.get(Integer.class), "boxedInteger").build());
        TypeSpec.Builder builder = TypeSpec.classBuilder("Test");
        builder.addFields(fields);
        MethodSpecs.addCachedHashCode(builder, fields);
        assertThat(builder.build())
                .asString()
                .isEqualTo("class Test {\n"
                        + "  int primitiveInt;\n"
                        + "\n"
                        + "  java.lang.String string;\n"
                        + "\n"
                        + "  java.lang.Integer boxedInteger;\n"
                        + "\n"
                        + "  private int memoizedHashCode;\n"
                        + "\n"
                        + "  @java.lang.Override\n"
                        + "  public int hashCode() {\n"
                        + "    int result = memoizedHashCode;\n"
                        + "    if (result == 0) {\n"
                        + "      result = computeHashCode();\n"
                        + "      memoizedHashCode = result;\n"
                        + "    }\n"
                        + "    return result;\n"
                        + "  }\n"
                        + "\n"
                        + "  private int computeHashCode() {\n"
                        + "    int hash = 4441;\n"
                        + "    hash += (hash << 5) + java.lang.Integer.hashCode(this.primitiveInt);\n"
                        + "    hash += (hash << 5) + java.util.Objects.hashCode(this.string);\n"
                        + "    hash += (hash << 5) + java.util.Objects.hashCode(this.boxedInteger);\n"
                        + "    return hash;\n"
                        + "  }\n"
                        + "}\n");
    }
}
