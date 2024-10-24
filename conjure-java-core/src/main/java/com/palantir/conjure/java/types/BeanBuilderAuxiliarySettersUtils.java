/*
 * (c) Copyright 2021 Palantir Technologies Inc. All rights reserved.
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

import com.palantir.conjure.java.ConjureAnnotations;
import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.conjure.java.types.BeanGenerator.EnrichedField;
import com.palantir.conjure.java.util.Javadoc;
import com.palantir.conjure.java.util.Primitives;
import com.palantir.conjure.java.visitor.DefaultTypeVisitor;
import com.palantir.conjure.spec.FieldDefinition;
import com.palantir.conjure.spec.LogSafety;
import com.palantir.conjure.spec.MapType;
import com.palantir.conjure.spec.OptionalType;
import com.palantir.conjure.spec.PrimitiveType;
import com.palantir.conjure.spec.Type;
import com.palantir.conjure.visitor.TypeVisitor;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.WildcardTypeName;
import java.util.Optional;
import javax.lang.model.element.Modifier;
import org.apache.commons.lang3.StringUtils;

final class BeanBuilderAuxiliarySettersUtils {

    private BeanBuilderAuxiliarySettersUtils() {}

    static MethodSpec.Builder createPrimitiveCollectionSetterBuilder(
            EnrichedField enriched, TypeMapper typeMapper, ClassName returnClass, SafetyEvaluator safetyEvaluator) {
        FieldSpec field = enriched.poetSpec();
        FieldDefinition definition = enriched.conjureDef();
        TypeName innerTypeName = extractInnerTypeFromList(definition, typeMapper, safetyEvaluator);

        return MethodSpec.methodBuilder("addAll" + StringUtils.capitalize(field.name))
                .addJavadoc(Javadoc.render(definition.getDocs(), definition.getDeprecated())
                        .map(rendered -> CodeBlock.of("$L", rendered))
                        .orElseGet(() -> CodeBlock.builder().build()))
                .addAnnotations(ConjureAnnotations.deprecation(definition.getDeprecated()))
                .addModifiers(Modifier.PUBLIC)
                // Forces the array argument to instead be variadic
                .varargs()
                .addParameter(Parameters.nonnullParameter(ArrayTypeName.of(innerTypeName), field.name))
                .returns(returnClass);
    }

    private static TypeName extractInnerTypeFromList(
            FieldDefinition conjureDef, TypeMapper typeMapper, SafetyEvaluator safetyEvaluator) {
        Type innerType = conjureDef.getType().accept(TypeVisitor.LIST).getItemType();
        TypeName boxedTypeName = typeMapper.getClassName(innerType);

        // SafeLong is just a special case of long
        if (boxedTypeName.equals(ClassName.get(SafeLong.class))) {
            return ConjureAnnotations.withSafety(TypeName.LONG, safetyEvaluator.getUsageTimeSafety(conjureDef));
        } else {
            return ConjureAnnotations.withSafety(
                    Primitives.unbox(boxedTypeName), safetyEvaluator.getUsageTimeSafety(conjureDef));
        }
    }

    static MethodSpec.Builder createCollectionSetterBuilder(
            String prefix,
            EnrichedField enriched,
            TypeMapper typeMapper,
            ClassName returnClass,
            SafetyEvaluator safetyEvaluator) {
        FieldSpec field = enriched.poetSpec();
        FieldDefinition definition = enriched.conjureDef();
        Type type = definition.getType();

        return MethodSpec.methodBuilder(prefix + StringUtils.capitalize(field.name))
                .addJavadoc(Javadoc.render(definition.getDocs(), definition.getDeprecated())
                        .map(rendered -> CodeBlock.of("$L", rendered))
                        .orElseGet(() -> CodeBlock.builder().build()))
                .addAnnotations(ConjureAnnotations.deprecation(definition.getDeprecated()))
                .addModifiers(Modifier.PUBLIC)
                .returns(returnClass)
                .addParameter(Parameters.nonnullParameter(
                        widenParameterIfPossible(
                                field.type, type, typeMapper, safetyEvaluator.getUsageTimeSafety(definition)),
                        field.name));
    }

    static MethodSpec.Builder createOptionalSetterBuilder(
            EnrichedField enriched, TypeMapper typeMapper, ClassName returnClass, SafetyEvaluator safetyEvaluator) {
        FieldSpec field = enriched.poetSpec();
        OptionalType type = enriched.conjureDef().getType().accept(TypeVisitor.OPTIONAL);
        return publicSetter(enriched, returnClass)
                .addParameter(Parameters.nonnullParameter(
                        ConjureAnnotations.withSafety(
                                typeMapper.getClassName(type.getItemType()),
                                safetyEvaluator.getUsageTimeSafety(enriched.conjureDef())),
                        field.name));
    }

    static MethodSpec.Builder createItemSetterBuilder(
            EnrichedField enriched,
            Type itemType,
            TypeMapper typeMapper,
            ClassName returnClass,
            Optional<LogSafety> safety) {
        FieldSpec field = enriched.poetSpec();
        return publicSetter(enriched, returnClass)
                .addParameter(ConjureAnnotations.withSafety(typeMapper.getClassName(itemType), safety), field.name);
    }

    static MethodSpec.Builder createMapSetterBuilder(
            EnrichedField enriched, TypeMapper typeMapper, ClassName returnClass) {
        MapType type = enriched.conjureDef().getType().accept(TypeVisitor.MAP);
        return publicSetter(enriched, returnClass)
                .addParameter(typeMapper.getClassName(type.getKeyType()), "key")
                .addParameter(typeMapper.getClassName(type.getValueType()), "value");
    }

    static MethodSpec.Builder publicSetter(EnrichedField enriched, ClassName returnClass) {
        FieldDefinition definition = enriched.conjureDef();
        return MethodSpec.methodBuilder(enriched.poetSpec().name)
                .addJavadoc(Javadoc.render(definition.getDocs(), definition.getDeprecated())
                        .map(rendered -> CodeBlock.of("$L", rendered))
                        .orElseGet(() -> CodeBlock.builder().build()))
                .addAnnotations(ConjureAnnotations.deprecation(definition.getDeprecated()))
                .addModifiers(Modifier.PUBLIC)
                .returns(returnClass);
    }

    static TypeName widenParameterIfPossible(
            TypeName current, Type type, TypeMapper typeMapper, Optional<LogSafety> safety) {
        if (type.accept(TypeVisitor.IS_LIST)) {
            Type innerType = type.accept(TypeVisitor.LIST).getItemType();
            TypeName innerTypeName =
                    ConjureAnnotations.withSafety(Primitives.box(typeMapper.getClassName(innerType)), safety);
            if (isWidenableContainedType(innerType)) {
                innerTypeName = WildcardTypeName.subtypeOf(innerTypeName);
            }
            return ParameterizedTypeName.get(ClassName.get(Iterable.class), innerTypeName);
        }

        if (type.accept(TypeVisitor.IS_SET)) {
            Type innerType = type.accept(TypeVisitor.SET).getItemType();
            TypeName innerTypeName =
                    ConjureAnnotations.withSafety(Primitives.box(typeMapper.getClassName(innerType)), safety);
            if (isWidenableContainedType(innerType)) {
                innerTypeName = WildcardTypeName.subtypeOf(innerTypeName);
            }

            return ParameterizedTypeName.get(ClassName.get(Iterable.class), innerTypeName);
        }

        if (type.accept(TypeVisitor.IS_OPTIONAL)) {
            Type innerType = type.accept(TypeVisitor.OPTIONAL).getItemType();
            if (!isWidenableContainedType(innerType)) {
                return current;
            }
            TypeName innerTypeName = Primitives.box(typeMapper.getClassName(innerType));
            return ParameterizedTypeName.get(
                    ClassName.get(Optional.class),
                    WildcardTypeName.subtypeOf(ConjureAnnotations.withSafety(innerTypeName, safety)));
        }

        return current;
    }

    // we want to widen containers of anything that's not a primitive, a conjure reference or an optional
    // since we know all of those are final.
    static boolean isWidenableContainedType(Type containedType) {
        return containedType.accept(new DefaultTypeVisitor<Boolean>() {
            @Override
            public Boolean visitPrimitive(PrimitiveType value) {
                return value.get() == PrimitiveType.Value.ANY;
            }

            @Override
            public Boolean visitOptional(OptionalType _value) {
                return false;
            }

            @Override
            public Boolean visitReference(com.palantir.conjure.spec.TypeName _value) {
                return false;
            }

            // collections, external references
            @Override
            public Boolean visitDefault() {
                return true;
            }
        });
    }
}
