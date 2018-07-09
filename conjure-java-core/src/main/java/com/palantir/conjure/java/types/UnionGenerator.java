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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.google.common.collect.ImmutableList;
import com.palantir.conjure.java.ConjureAnnotations;
import com.palantir.conjure.java.util.JavaNameSanitizer;
import com.palantir.conjure.java.util.StableCollectors;
import com.palantir.conjure.spec.FieldDefinition;
import com.palantir.conjure.spec.FieldName;
import com.palantir.conjure.spec.ObjectDefinition;
import com.palantir.conjure.spec.OptionalType;
import com.palantir.conjure.spec.PrimitiveType;
import com.palantir.conjure.spec.Type;
import com.palantir.conjure.spec.UnionDefinition;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import javax.lang.model.element.Modifier;
import org.apache.commons.lang3.StringUtils;

public final class UnionGenerator {

    private static final String UNION_FIELD_NAME = "union";
    private static final String INNER_UNION_TYPE_NAME = "_Union_";
    private static final String TYPE_FIELD_NAME = "type";
    private static final String VALUE_PARAM = "value";
    private static final String VISIT_METHOD_NAME = "visit";
    private static final String VISIT_UNKNOWN_METHOD_NAME = "visitUnknown";
    private static final TypeVariableName TYPE_VARIABLE = TypeVariableName.get("T");

    public static JavaFile generateUnionType(TypeMapper typeMapper, UnionDefinition typeDef) {
        String typePackage = typeDef.getTypeName().getPackage();
        ClassName unionClass = ClassName.get(typePackage, typeDef.getTypeName().getName());

        ClassName visitorClass = ClassName.get(unionClass.packageName(), unionClass.simpleName(), "Visitor");
        Map<FieldName, TypeName> memberTypes = typeDef.getUnion().stream()
                .collect(StableCollectors.toLinkedMap(
                        FieldDefinition::getFieldName,
                        entry -> typeMapper.getClassName(entry.getType())));

        List<FieldSpec> fields = generateFields(unionClass);

        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(typeDef.getTypeName().getName())
                .addAnnotation(ConjureAnnotations.getConjureGeneratedAnnotation(UnionGenerator.class))
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addFields(fields)
                .addMethod(generateConstructor(typeMapper, unionClass))
                .addMethod(generateAcceptVisitMethod(typeMapper, visitorClass, typeDef.getUnion()))
                .addMethod(generateEquals(unionClass, memberTypes))
                .addMethod(MethodSpecs.createEqualTo(unionClass, fields))
                .addMethod(MethodSpec.methodBuilder("hashCode")
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(TypeName.INT)
                        .addStatement("return $1N.hashCode()", UNION_FIELD_NAME)
                        .build())
                .addMethod(MethodSpec.methodBuilder("toString")
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(ClassName.get(String.class))
                        .addStatement("return $1N.toString()", UNION_FIELD_NAME)
                        .build())
                .addType(generateVisitor(visitorClass, memberTypes))
                .addType(generateUnionBean(typeMapper, typeDef, typePackage, unionClass))
                .addMethods(generateStaticFactories(typeMapper, unionClass, typeDef.getUnion()));

        typeDef.getDocs().ifPresent(docs ->
                typeBuilder.addJavadoc("$L", StringUtils.appendIfMissing(docs.get(), "\n")));

        return JavaFile.builder(typePackage, typeBuilder.build())
                .skipJavaLangImports(true)
                .indent("    ")
                .build();
    }

    private static TypeSpec generateUnionBean(TypeMapper typeMapper, UnionDefinition typeDef, String typePackage,
            ClassName unionClass) {
        return BeanGenerator.generateBeanTypeSpec(typeMapper, ObjectDefinition.builder()
                .typeName(com.palantir.conjure.spec.TypeName.of(
                        INNER_UNION_TYPE_NAME, typePackage + "." + unionClass.simpleName()))
                .fields(FieldDefinition.builder()
                        .fieldName(FieldName.of("type"))
                        .type(Type.primitive(PrimitiveType.STRING))
                        .build())
                .addAllFields(typeDef.getUnion().stream()
                        .map(m -> FieldDefinition.builder()
                                .fieldName(m.getFieldName())
                                .type(Type.optional(OptionalType.of(m.getType())))
                                .build())
                        .collect(Collectors.toList()))
                .build(),
                true)
                .toBuilder()
                .addModifiers(Modifier.STATIC, Modifier.PRIVATE)
                .addAnnotation(AnnotationSpec.builder(JsonInclude.class)
                        .addMember("value", "$T.NON_ABSENT", JsonInclude.Include.class).build())
                .build();
    }

    private static List<FieldSpec> generateFields(ClassName unionClass) {
        return ImmutableList.of(
                FieldSpec.builder(
                        unionClass.nestedClass(INNER_UNION_TYPE_NAME),
                        UNION_FIELD_NAME,
                        Modifier.PRIVATE,
                        Modifier.FINAL)
                        .addAnnotation(JsonUnwrapped.class)
                        .build());
    }

    private static MethodSpec generateConstructor(TypeMapper typeMapper, ClassName unionClass) {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .addAnnotation(AnnotationSpec.builder(JsonCreator.class).build())
                .addParameter(ParameterSpec.builder(
                        unionClass.nestedClass(INNER_UNION_TYPE_NAME), UNION_FIELD_NAME).build())
                .addStatement(Expressions.requireNonNull(UNION_FIELD_NAME, "union must not be null"))
                .addStatement("this.$1L = $1L", UNION_FIELD_NAME)
                .build();
    }

    private static List<MethodSpec> generateStaticFactories(
            TypeMapper typeMapper, ClassName unionClass, List<FieldDefinition> memberTypeDefs) {
        return memberTypeDefs.stream().map(memberTypeDef -> {
            FieldName memberName = memberTypeDef.getFieldName();
            TypeName memberType = typeMapper.getClassName(memberTypeDef.getType());
            String safeName = JavaNameSanitizer.sanitize(memberName);

            // memberName is guarded to be a valid Java identifier and not to end in an underscore, so this is safe
            MethodSpec.Builder builder = MethodSpec.methodBuilder(safeName)
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .addParameter(memberType, VALUE_PARAM)
                    .addStatement("return new $1T($2T.builder().type($3S).$4N($5N).build())",
                            unionClass,
                            unionClass.nestedClass(INNER_UNION_TYPE_NAME),
                            memberName,
                            safeName,
                            VALUE_PARAM)
                    .returns(unionClass);
            memberTypeDef.getDocs()
                    .ifPresent(docs -> builder.addJavadoc("$L", StringUtils.appendIfMissing(docs.get(), "\n")));

            return builder.build();
        }).collect(Collectors.toList());
    }

    private static MethodSpec generateAcceptVisitMethod(TypeMapper typeMapper, ClassName visitorClass,
            List<FieldDefinition> fieldDefs) {
        ParameterizedTypeName parameterizedVisitorClass = ParameterizedTypeName.get(visitorClass, TYPE_VARIABLE);
        ParameterSpec visitor = ParameterSpec.builder(parameterizedVisitorClass, "visitor").build();
        MethodSpec.Builder visitBuilder = MethodSpec.methodBuilder("accept")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(visitor)
                .addTypeVariable(TYPE_VARIABLE)
                .returns(TYPE_VARIABLE);

        if (!fieldDefs.isEmpty()) {
            boolean first = true;
            CodeBlock.Builder block = CodeBlock.builder();
            for (FieldDefinition def : fieldDefs) {
                String getterName = BeanGenerator.asGetterName(def.getFieldName().get());
                if (first) {
                    block.beginControlFlow("if ($1N.$2L().isPresent())", UNION_FIELD_NAME, getterName);
                    first = false;
                } else {
                    block.nextControlFlow("else if ($1N.$2L().isPresent())", UNION_FIELD_NAME, getterName);
                }
                block.addStatement("return $1N.$2L($3N.$4L().$5L())",
                        visitor, asVisitName(def.getFieldName().get()), UNION_FIELD_NAME, getterName,
                        optionalTypeGetter(typeMapper, def.getType()));
            }
            block.endControlFlow();
            visitBuilder.addCode("$L", block.build());
        }
        visitBuilder.addStatement("return $1N.visitUnknown($2N.$3L())", visitor, UNION_FIELD_NAME,
                        BeanGenerator.asGetterName(TYPE_FIELD_NAME));
        return visitBuilder.build();
    }


    private static MethodSpec generateEquals(ClassName unionClass, Map<FieldName, TypeName> memberTypes) {
        ParameterSpec other = ParameterSpec.builder(TypeName.OBJECT, "other").build();
        return MethodSpec.methodBuilder("equals")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(other)
                .returns(TypeName.BOOLEAN)
                .addStatement("return this == $1N || ($1N instanceof $2T && equalTo(($2T) other))", other, unionClass)
                .build();
    }

    private static TypeSpec generateVisitor(ClassName visitorClass, Map<FieldName, TypeName> memberTypes) {
        return TypeSpec.interfaceBuilder(visitorClass)
                .addModifiers(Modifier.PUBLIC)
                .addTypeVariable(TYPE_VARIABLE)
                .addMethods(generateMemberVisitMethods(memberTypes))
                .addMethod(MethodSpec.methodBuilder(VISIT_UNKNOWN_METHOD_NAME)
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .addParameter(String.class, "unknownType")
                        .returns(TYPE_VARIABLE)
                        .build())
                .build();
    }

    private static List<MethodSpec> generateMemberVisitMethods(Map<FieldName, TypeName> memberTypes) {
        return memberTypes.entrySet().stream().map(entry ->
                MethodSpec.methodBuilder(asVisitName(entry.getKey().get()))
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .addParameter(entry.getValue(), VALUE_PARAM)
                        .returns(TYPE_VARIABLE)
                        .build())
                .collect(Collectors.toList());
    }

    private static String asVisitName(String key) {
        return VISIT_METHOD_NAME + StringUtils.capitalize(key);
    }

    private static String optionalTypeGetter(TypeMapper typeMapper, Type fieldType) {
        TypeName name = typeMapper.getClassName(Type.optional(OptionalType.of(fieldType)));
        if (name.equals(ClassName.get(OptionalDouble.class))) {
            return "getAsDouble";
        } else if (name.equals(ClassName.get(OptionalInt.class))) {
            return "getAsInt";
        }
        return "get";
    }

    private UnionGenerator() {}
}
