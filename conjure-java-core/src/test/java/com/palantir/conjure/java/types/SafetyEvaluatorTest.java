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

import com.google.common.collect.Iterables;
import com.palantir.conjure.defs.validator.ConjureDefinitionValidator;
import com.palantir.conjure.spec.AliasDefinition;
import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.conjure.spec.Documentation;
import com.palantir.conjure.spec.EnumDefinition;
import com.palantir.conjure.spec.FieldDefinition;
import com.palantir.conjure.spec.FieldName;
import com.palantir.conjure.spec.LogSafety;
import com.palantir.conjure.spec.MapType;
import com.palantir.conjure.spec.ObjectDefinition;
import com.palantir.conjure.spec.PrimitiveType;
import com.palantir.conjure.spec.Type;
import com.palantir.conjure.spec.TypeDefinition;
import com.palantir.conjure.spec.TypeName;
import com.palantir.conjure.spec.UnionDefinition;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class SafetyEvaluatorTest {
    private static final String PACKAGE = "package";
    private static final TypeName FOO = TypeName.of("Foo", PACKAGE);
    private static final TypeName BAR = TypeName.of("Bar", PACKAGE);
    private static final Documentation DOCS = Documentation.of("docs");
    private static final TypeName SAFE_ALIAS_NAME = TypeName.of("SafeAlias", PACKAGE);
    private static final TypeDefinition SAFE_ALIAS = TypeDefinition.alias(AliasDefinition.builder()
            .typeName(SAFE_ALIAS_NAME)
            .alias(Type.primitive(PrimitiveType.STRING))
            .safety(LogSafety.SAFE)
            .build());
    private static final TypeName UNSAFE_ALIAS_NAME = TypeName.of("UnsafeAlias", PACKAGE);
    private static final TypeDefinition UNSAFE_ALIAS = TypeDefinition.alias(AliasDefinition.builder()
            .typeName(UNSAFE_ALIAS_NAME)
            .alias(Type.primitive(PrimitiveType.STRING))
            .safety(LogSafety.UNSAFE)
            .build());

    @Test
    public void testCombine() {
        assertThat(SafetyEvaluator.combine(Optional.empty(), Optional.empty())).isEmpty();
        evaluateCombination(Optional.of(LogSafety.SAFE), Optional.empty(), Optional.empty());
        evaluateCombination(Optional.of(LogSafety.UNSAFE), Optional.empty(), Optional.of(LogSafety.UNSAFE));
        evaluateCombination(Optional.of(LogSafety.DO_NOT_LOG), Optional.empty(), Optional.of(LogSafety.DO_NOT_LOG));
        evaluateCombination(Optional.of(LogSafety.SAFE), Optional.of(LogSafety.SAFE), Optional.of(LogSafety.SAFE));
        evaluateCombination(Optional.of(LogSafety.UNSAFE), Optional.of(LogSafety.SAFE), Optional.of(LogSafety.UNSAFE));
        evaluateCombination(
                Optional.of(LogSafety.DO_NOT_LOG), Optional.of(LogSafety.SAFE), Optional.of(LogSafety.DO_NOT_LOG));
        evaluateCombination(Optional.of(LogSafety.SAFE), Optional.of(LogSafety.UNSAFE), Optional.of(LogSafety.UNSAFE));
        evaluateCombination(
                Optional.of(LogSafety.UNSAFE), Optional.of(LogSafety.UNSAFE), Optional.of(LogSafety.UNSAFE));
        evaluateCombination(
                Optional.of(LogSafety.DO_NOT_LOG), Optional.of(LogSafety.UNSAFE), Optional.of(LogSafety.DO_NOT_LOG));
        evaluateCombination(
                Optional.of(LogSafety.SAFE), Optional.of(LogSafety.DO_NOT_LOG), Optional.of(LogSafety.DO_NOT_LOG));
        evaluateCombination(
                Optional.of(LogSafety.UNSAFE), Optional.of(LogSafety.DO_NOT_LOG), Optional.of(LogSafety.DO_NOT_LOG));
        evaluateCombination(
                Optional.of(LogSafety.DO_NOT_LOG), Optional.of(LogSafety.UNSAFE), Optional.of(LogSafety.DO_NOT_LOG));
    }

    private static void evaluateCombination(
            Optional<LogSafety> lhs, Optional<LogSafety> rhs, Optional<LogSafety> expected) {
        Optional<LogSafety> result1 = SafetyEvaluator.combine(lhs, rhs);
        Optional<LogSafety> result2 = SafetyEvaluator.combine(rhs, lhs);
        assertThat(result1).isEqualTo(expected);
        assertThat(result2).isEqualTo(expected);
    }

    @Test
    public void testAllows() {
        assertThat(SafetyEvaluator.allows(Optional.empty(), Optional.empty())).isTrue();
        assertThat(SafetyEvaluator.allows(Optional.of(LogSafety.SAFE), Optional.empty()))
                .isTrue();
        assertThat(SafetyEvaluator.allows(Optional.of(LogSafety.UNSAFE), Optional.empty()))
                .isTrue();
        assertThat(SafetyEvaluator.allows(Optional.of(LogSafety.DO_NOT_LOG), Optional.empty()))
                .isTrue();
        assertThat(SafetyEvaluator.allows(Optional.of(LogSafety.SAFE), Optional.of(LogSafety.SAFE)))
                .isTrue();
        assertThat(SafetyEvaluator.allows(Optional.of(LogSafety.SAFE), Optional.of(LogSafety.UNSAFE)))
                .isFalse();
        assertThat(SafetyEvaluator.allows(Optional.of(LogSafety.SAFE), Optional.of(LogSafety.DO_NOT_LOG)))
                .isFalse();
        assertThat(SafetyEvaluator.allows(Optional.of(LogSafety.UNSAFE), Optional.of(LogSafety.SAFE)))
                .isTrue();
        assertThat(SafetyEvaluator.allows(Optional.of(LogSafety.UNSAFE), Optional.of(LogSafety.UNSAFE)))
                .isTrue();
        assertThat(SafetyEvaluator.allows(Optional.of(LogSafety.UNSAFE), Optional.of(LogSafety.DO_NOT_LOG)))
                .isFalse();
        assertThat(SafetyEvaluator.allows(Optional.of(LogSafety.DO_NOT_LOG), Optional.of(LogSafety.SAFE)))
                .isTrue();
        assertThat(SafetyEvaluator.allows(Optional.of(LogSafety.DO_NOT_LOG), Optional.of(LogSafety.UNSAFE)))
                .isTrue();
        assertThat(SafetyEvaluator.allows(Optional.of(LogSafety.DO_NOT_LOG), Optional.of(LogSafety.DO_NOT_LOG)))
                .isTrue();
    }

    @Test
    void testMapSafeKey() {
        TypeDefinition object = TypeDefinition.object(ObjectDefinition.builder()
                .typeName(FOO)
                .fields(FieldDefinition.builder()
                        .fieldName(FieldName.of("aField"))
                        .type(Type.map(
                                MapType.of(Type.reference(SAFE_ALIAS_NAME), Type.primitive(PrimitiveType.STRING))))
                        .build())
                .build());
        ConjureDefinition conjureDef = ConjureDefinition.builder()
                .version(1)
                .types(object)
                .types(SAFE_ALIAS)
                .build();
        ConjureDefinitionValidator.validateAll(conjureDef);
        SafetyEvaluator evaluator = new SafetyEvaluator(conjureDef);
        assertThat(evaluator.evaluate(object)).isEmpty();
    }

    @Test
    void testMapSafetyUnsafeValue() {
        TypeDefinition object = TypeDefinition.object(ObjectDefinition.builder()
                .typeName(FOO)
                .fields(FieldDefinition.builder()
                        .fieldName(FieldName.of("aField"))
                        .type(Type.map(MapType.of(Type.reference(SAFE_ALIAS_NAME), Type.reference(UNSAFE_ALIAS_NAME))))
                        .build())
                .build());
        ConjureDefinition conjureDef = ConjureDefinition.builder()
                .version(1)
                .types(object)
                .types(SAFE_ALIAS)
                .types(UNSAFE_ALIAS)
                .build();
        ConjureDefinitionValidator.validateAll(conjureDef);
        SafetyEvaluator evaluator = new SafetyEvaluator(conjureDef);
        assertThat(evaluator.evaluate(object)).hasValue(LogSafety.UNSAFE);
    }

    @Test
    void testMapSafetySafeKeyAndValue() {
        TypeDefinition object = TypeDefinition.object(ObjectDefinition.builder()
                .typeName(FOO)
                .fields(FieldDefinition.builder()
                        .fieldName(FieldName.of("aField"))
                        .type(Type.map(MapType.of(Type.reference(SAFE_ALIAS_NAME), Type.reference(SAFE_ALIAS_NAME))))
                        .build())
                .build());
        ConjureDefinition conjureDef = ConjureDefinition.builder()
                .version(1)
                .types(object)
                .types(SAFE_ALIAS)
                .types(UNSAFE_ALIAS)
                .build();
        ConjureDefinitionValidator.validateAll(conjureDef);
        SafetyEvaluator evaluator = new SafetyEvaluator(conjureDef);
        assertThat(evaluator.evaluate(object)).hasValue(LogSafety.SAFE);
    }

    @Test
    void testNested() {
        TypeDefinition firstObject = TypeDefinition.object(ObjectDefinition.builder()
                .typeName(FOO)
                .fields(FieldDefinition.builder()
                        .fieldName(FieldName.of("bar"))
                        .type(Type.reference(BAR))
                        .build())
                .fields(FieldDefinition.builder()
                        .fieldName(FieldName.of("safeField"))
                        .type(Type.primitive(PrimitiveType.STRING))
                        .safety(LogSafety.SAFE)
                        .build())
                .build());

        TypeDefinition secondObject = TypeDefinition.object(ObjectDefinition.builder()
                .typeName(BAR)
                .fields(FieldDefinition.builder()
                        .fieldName(FieldName.of("aliasRef"))
                        .type(Type.reference(UNSAFE_ALIAS_NAME))
                        .build())
                .build());
        ConjureDefinition conjureDef = ConjureDefinition.builder()
                .version(1)
                .types(firstObject)
                .types(secondObject)
                .types(UNSAFE_ALIAS)
                .build();
        ConjureDefinitionValidator.validateAll(conjureDef);
        SafetyEvaluator evaluator = new SafetyEvaluator(conjureDef);
        assertThat(evaluator.evaluate(firstObject)).hasValue(LogSafety.UNSAFE);
    }

    @Test
    void testPrimitiveSafety() {
        ConjureDefinition conjureDef = ConjureDefinition.builder()
                .version(1)
                .types(TypeDefinition.object(ObjectDefinition.builder()
                        .typeName(FOO)
                        .fields(FieldDefinition.builder()
                                .fieldName(FieldName.of("bad"))
                                .type(Type.primitive(PrimitiveType.STRING))
                                .safety(LogSafety.UNSAFE)
                                .docs(DOCS)
                                .build())
                        .build()))
                .build();
        ConjureDefinitionValidator.validateAll(conjureDef);
        SafetyEvaluator evaluator = new SafetyEvaluator(conjureDef);
        assertThat(evaluator.evaluate(Iterables.getOnlyElement(conjureDef.getTypes())))
                .hasValue(LogSafety.UNSAFE);
    }

    @Test
    void testNoSafety() {
        ConjureDefinition conjureDef = ConjureDefinition.builder()
                .version(1)
                .types(TypeDefinition.object(ObjectDefinition.builder()
                        .typeName(FOO)
                        .fields(FieldDefinition.builder()
                                .fieldName(FieldName.of("bad"))
                                .type(Type.primitive(PrimitiveType.STRING))
                                .docs(DOCS)
                                .build())
                        .build()))
                .build();
        ConjureDefinitionValidator.validateAll(conjureDef);
        SafetyEvaluator evaluator = new SafetyEvaluator(conjureDef);
        assertThat(evaluator.evaluate(Iterables.getOnlyElement(conjureDef.getTypes())))
                .isEmpty();
    }

    @Test
    void testEmptyUnion() {
        ConjureDefinition conjureDef = ConjureDefinition.builder()
                .version(1)
                .types(TypeDefinition.union(
                        UnionDefinition.builder().typeName(FOO).build()))
                .build();
        ConjureDefinitionValidator.validateAll(conjureDef);
        SafetyEvaluator evaluator = new SafetyEvaluator(conjureDef);
        assertThat(evaluator.evaluate(Iterables.getOnlyElement(conjureDef.getTypes())))
                .as("No guarantees can be made about future union values, "
                        + "however we don't mark them unsafe to ease rollout")
                .isEmpty();
    }

    @Test
    void testUnsafeElementUnion() {
        ConjureDefinition conjureDef = ConjureDefinition.builder()
                .version(1)
                .types(TypeDefinition.union(UnionDefinition.builder()
                        .typeName(FOO)
                        .union(FieldDefinition.builder()
                                .fieldName(FieldName.of("field"))
                                .type(Type.primitive(PrimitiveType.STRING))
                                .safety(LogSafety.UNSAFE)
                                .build())
                        .build()))
                .build();
        ConjureDefinitionValidator.validateAll(conjureDef);
        SafetyEvaluator evaluator = new SafetyEvaluator(conjureDef);
        assertThat(evaluator.evaluate(Iterables.getOnlyElement(conjureDef.getTypes())))
                .hasValue(LogSafety.UNSAFE);
    }

    @Test
    void testDoNotLogElementUnion() {
        ConjureDefinition conjureDef = ConjureDefinition.builder()
                .version(1)
                .types(TypeDefinition.union(UnionDefinition.builder()
                        .typeName(FOO)
                        .union(FieldDefinition.builder()
                                .fieldName(FieldName.of("field"))
                                .type(Type.primitive(PrimitiveType.STRING))
                                .safety(LogSafety.DO_NOT_LOG)
                                .build())
                        .build()))
                .build();
        ConjureDefinitionValidator.validateAll(conjureDef);
        SafetyEvaluator evaluator = new SafetyEvaluator(conjureDef);
        assertThat(evaluator.evaluate(Iterables.getOnlyElement(conjureDef.getTypes())))
                .hasValue(LogSafety.DO_NOT_LOG);
    }

    @Test
    void testEmptyObject() {
        ConjureDefinition conjureDef = ConjureDefinition.builder()
                .version(1)
                .types(TypeDefinition.object(
                        ObjectDefinition.builder().typeName(FOO).build()))
                .build();
        ConjureDefinitionValidator.validateAll(conjureDef);
        SafetyEvaluator evaluator = new SafetyEvaluator(conjureDef);
        assertThat(evaluator.evaluate(Iterables.getOnlyElement(conjureDef.getTypes())))
                .hasValue(LogSafety.SAFE);
    }

    @Test
    void testEmptyEnum() {
        ConjureDefinition conjureDef = ConjureDefinition.builder()
                .version(1)
                .types(TypeDefinition.enum_(
                        EnumDefinition.builder().typeName(FOO).build()))
                .build();
        ConjureDefinitionValidator.validateAll(conjureDef);
        SafetyEvaluator evaluator = new SafetyEvaluator(conjureDef);
        assertThat(evaluator.evaluate(Iterables.getOnlyElement(conjureDef.getTypes())))
                .hasValue(LogSafety.SAFE);
    }
}
