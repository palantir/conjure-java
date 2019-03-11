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

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.collect.ImmutableList;
import com.palantir.conjure.java.ConjureAnnotations;
import com.palantir.conjure.java.util.JavaNameSanitizer;
import com.palantir.conjure.java.util.StableCollectors;
import com.palantir.conjure.spec.FieldDefinition;
import com.palantir.conjure.spec.FieldName;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.lang.model.element.Modifier;
import org.apache.commons.lang3.StringUtils;

public final class UnionGenerator {

    private static final String VALUE_FIELD_NAME = "value";
    private static final String UNKNOWN_WRAPPER_CLASS_NAME = "UnknownWrapper";
    private static final String VISIT_METHOD_NAME = "visit";
    private static final String VISIT_UNKNOWN_METHOD_NAME = "visitUnknown";
    private static final TypeVariableName TYPE_VARIABLE = TypeVariableName.get("T");

    public static JavaFile generateUnionType(TypeMapper typeMapper, UnionDefinition typeDef) {

        String typePackage = typeDef.getTypeName().getPackage();
        ClassName unionClass = ClassName.get(typePackage, typeDef.getTypeName().getName());
        ClassName baseClass = ClassName.get(unionClass.packageName(), unionClass.simpleName(), "Base");
        ClassName visitorClass = ClassName.get(unionClass.packageName(), unionClass.simpleName(), "Visitor");
        Map<FieldName, TypeName> memberTypes = typeDef.getUnion().stream()
                .collect(StableCollectors.toLinkedMap(
                        FieldDefinition::getFieldName,
                        entry -> typeMapper.getClassName(entry.getType())));
        List<FieldSpec> fields = ImmutableList.of(
                FieldSpec.builder(baseClass, VALUE_FIELD_NAME, Modifier.PRIVATE, Modifier.FINAL).build());

        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(typeDef.getTypeName().getName())
                .addAnnotation(ConjureAnnotations.getConjureGeneratedAnnotation(UnionGenerator.class))
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addFields(fields)
                .addMethod(generateConstructor(baseClass))
                .addMethod(generateGetValue(baseClass))
                .addMethods(generateStaticFactories(typeMapper, unionClass, typeDef.getUnion()))
                .addMethod(generateAcceptVisitMethod(visitorClass, memberTypes.keySet()))
                .addType(generateVisitor(visitorClass, memberTypes))
                .addType(generateBase(baseClass, memberTypes))
                .addTypes(generateWrapperClasses(typeMapper, baseClass, typeDef.getUnion()))
                .addType(generateUnknownWrapper(baseClass))
                .addMethod(generateEquals(unionClass))
                .addMethod(MethodSpecs.createEqualTo(unionClass, fields))
                .addMethod(MethodSpecs.createHashCode(fields))
                .addMethod(MethodSpecs.createToString(unionClass.simpleName(),
                        fields.stream().map(
                                fieldSpec -> FieldName.of(fieldSpec.name))
                                .collect(Collectors.toList())));

        typeDef.getDocs().ifPresent(docs ->
                typeBuilder.addJavadoc("$L", StringUtils.appendIfMissing(docs.get(), "\n")));

        return JavaFile.builder(typePackage, typeBuilder.build())
                .skipJavaLangImports(true)
                .indent("    ")
                .build();
    }

    private static MethodSpec generateConstructor(ClassName baseClass) {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .addAnnotation(AnnotationSpec.builder(JsonCreator.class).build())
                .addParameter(baseClass, VALUE_FIELD_NAME)
                // no null check because this constructor is private and is only called by nice factory methods
                .addStatement("this.$1L = $1L", VALUE_FIELD_NAME)
                .build();
    }

    private static MethodSpec generateGetValue(ClassName baseClass) {
        return MethodSpec.methodBuilder("getValue")
                .addModifiers(Modifier.PRIVATE)
                .addAnnotation(AnnotationSpec.builder(JsonValue.class).build())
                .addStatement("return $L", VALUE_FIELD_NAME)
                .returns(baseClass)
                .build();
    }

    private static List<MethodSpec> generateStaticFactories(
            TypeMapper typeMapper, ClassName unionClass, List<FieldDefinition> memberTypeDefs) {
        return memberTypeDefs.stream().map(memberTypeDef -> {
            FieldName memberName = memberTypeDef.getFieldName();
            TypeName memberType = typeMapper.getClassName(memberTypeDef.getType());
            String variableName = variableName();
            // memberName is guarded to be a valid Java identifier and not to end in an underscore, so this is safe
            MethodSpec.Builder builder = MethodSpec.methodBuilder(JavaNameSanitizer.sanitize(memberName))
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .addParameter(memberType, variableName)
                    .addStatement("return new $T(new $T($L))",
                            unionClass, wrapperClass(unionClass, memberName), variableName)
                    .returns(unionClass);
            memberTypeDef.getDocs()
                    .ifPresent(docs -> builder.addJavadoc("$L", StringUtils.appendIfMissing(docs.get(), "\n")));
            return builder.build();
        }).collect(Collectors.toList());
    }

    private static MethodSpec generateAcceptVisitMethod(ClassName visitorClass, Set<FieldName> memberNames) {
        ParameterizedTypeName parameterizedVisitorClass = ParameterizedTypeName.get(visitorClass, TYPE_VARIABLE);
        ParameterSpec visitor = ParameterSpec.builder(parameterizedVisitorClass, "visitor").build();
        MethodSpec.Builder visitBuilder = MethodSpec.methodBuilder("accept")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(visitor)
                .addTypeVariable(TYPE_VARIABLE)
                .returns(TYPE_VARIABLE);

        CodeBlock.Builder codeBuilder = CodeBlock.builder();
        boolean beginControlFlow = true;
        for (FieldName memberName : memberNames) {
            ClassName wrapperClass = peerWrapperClass(visitorClass, memberName);
            CodeBlock ifStatement = CodeBlock.of("if ($L instanceof $T)",
                    VALUE_FIELD_NAME, wrapperClass);
            if (beginControlFlow) {
                codeBuilder.beginControlFlow("$L", ifStatement);
                beginControlFlow = false;
            } else {
                codeBuilder.nextControlFlow("else $L", ifStatement);
            }
            codeBuilder.addStatement(
                    "return $1N.$2L((($3T) $4L).$4L)",
                    visitor,
                    VISIT_METHOD_NAME + StringUtils.capitalize(memberName.get()),
                    wrapperClass,
                    VALUE_FIELD_NAME);
        }
        ClassName unknownWrapperClass = visitorClass.peerClass(UNKNOWN_WRAPPER_CLASS_NAME);
        codeBuilder.nextControlFlow("else if ($L instanceof $T)", VALUE_FIELD_NAME, unknownWrapperClass);
        codeBuilder.addStatement("return $N.$L((($T) $L).getType())",
                visitor, VISIT_UNKNOWN_METHOD_NAME, unknownWrapperClass, VALUE_FIELD_NAME);
        codeBuilder.endControlFlow();
        codeBuilder.addStatement("throw new $T(String.format(\"Could not identify type %s\", $L.getClass()))",
                IllegalStateException.class, VALUE_FIELD_NAME);
        return visitBuilder.addCode(codeBuilder.build()).build();
    }

    private static MethodSpec generateEquals(ClassName unionClass) {
        ParameterSpec other = ParameterSpec.builder(TypeName.OBJECT, "other").build();
        CodeBlock.Builder codeBuilder = CodeBlock.builder()
                .add("return this == $1N || ($1N instanceof $2T && equalTo(($2T) $1N))", other, unionClass);

        return MethodSpec.methodBuilder("equals")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(other)
                .returns(TypeName.BOOLEAN)
                .addStatement("$L", codeBuilder.build())
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
        return memberTypes.entrySet().stream().map(entry -> {
            String variableName = variableName();
            return MethodSpec.methodBuilder(VISIT_METHOD_NAME + StringUtils.capitalize(entry.getKey().get()))
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .addParameter(entry.getValue(), variableName)
                    .returns(TYPE_VARIABLE)
                    .build();
        }).collect(Collectors.toList());
    }

    private static TypeSpec generateBase(ClassName baseClass, Map<FieldName, TypeName> memberTypes) {
        ClassName unknownWrapperClass = baseClass.peerClass(UNKNOWN_WRAPPER_CLASS_NAME);
        TypeSpec.Builder baseBuilder = TypeSpec.interfaceBuilder(baseClass)
                .addModifiers(Modifier.PRIVATE)
                .addAnnotation(AnnotationSpec.builder(JsonTypeInfo.class)
                        .addMember("use", "JsonTypeInfo.Id.NAME")
                        .addMember("property", "\"type\"")
                        .addMember("visible", "$L", true)
                        .addMember("defaultImpl", "$T.class", unknownWrapperClass)
                        .build());
        List<AnnotationSpec> subAnnotations = memberTypes.entrySet().stream()
                .map(entry -> AnnotationSpec.builder(JsonSubTypes.Type.class)
                        .addMember("value", "$T.class", peerWrapperClass(baseClass, entry.getKey()))
                        .build())
                .collect(Collectors.toList());
        AnnotationSpec.Builder annotationBuilder = AnnotationSpec.builder(JsonSubTypes.class);
        subAnnotations.forEach(subAnnotation -> annotationBuilder.addMember("value", "$L", subAnnotation));
        baseBuilder.addAnnotation(annotationBuilder.build());
        baseBuilder.addAnnotation(AnnotationSpec.builder(JsonIgnoreProperties.class)
                .addMember("ignoreUnknown", "$L", true).build());

        return baseBuilder.build();
    }

    private static List<TypeSpec> generateWrapperClasses(
            TypeMapper typeMapper,
            ClassName baseClass,
            List<FieldDefinition> memberTypeDefs) {
        return memberTypeDefs.stream().map(memberTypeDef -> {
            FieldName memberName = memberTypeDef.getFieldName();
            TypeName memberType = typeMapper.getClassName(memberTypeDef.getType());
            ClassName wrapperClass = peerWrapperClass(baseClass, memberName);

            AnnotationSpec jsonPropertyAnnotation = AnnotationSpec.builder(JsonProperty.class)
                    .addMember("value", "$S", memberName.get()).build();
            List<FieldSpec> fields = ImmutableList.of(
                    FieldSpec.builder(memberType, VALUE_FIELD_NAME, Modifier.PRIVATE, Modifier.FINAL).build());

            TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(wrapperClass)
                    .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                    .addSuperinterface(baseClass)
                    .addAnnotation(AnnotationSpec.builder(JsonTypeName.class)
                            .addMember("value", "$S", memberName.get())
                            .build())
                    .addFields(fields)
                    .addMethod(MethodSpec.constructorBuilder()
                            .addModifiers(Modifier.PRIVATE)
                            .addAnnotation(AnnotationSpec.builder(JsonCreator.class).build())
                            .addParameter(ParameterSpec.builder(memberType, VALUE_FIELD_NAME)
                                    .addAnnotation(jsonPropertyAnnotation)
                                    .build())
                            .addStatement("$L", Expressions.requireNonNull(VALUE_FIELD_NAME,
                                    String.format("%s cannot be null", memberName.get())))
                            .addStatement("this.$1L = $1L", VALUE_FIELD_NAME)
                            .build())
                    .addMethod(MethodSpec.methodBuilder("getValue")
                            .addModifiers(Modifier.PRIVATE)
                            .addAnnotation(jsonPropertyAnnotation)
                            .addStatement("return $L", VALUE_FIELD_NAME)
                            .returns(memberType)
                            .build())
                    .addMethod(MethodSpecs.createEquals(wrapperClass))
                    .addMethod(MethodSpecs.createEqualTo(wrapperClass, fields))
                    .addMethod(MethodSpecs.createHashCode(fields))
                    .addMethod(MethodSpecs.createToString(wrapperClass.simpleName(),
                            fields.stream().map(
                                    fieldSpec -> FieldName.of(fieldSpec.name))
                                    .collect(Collectors.toList())));

            return typeBuilder.build();
        }).collect(Collectors.toList());
    }

    private static TypeSpec generateUnknownWrapper(ClassName baseClass) {
        ParameterizedTypeName genericMapType = ParameterizedTypeName.get(Map.class, String.class, Object.class);
        ParameterizedTypeName genericHashMapType = ParameterizedTypeName.get(HashMap.class, String.class, Object.class);
        ParameterSpec typeParameter = ParameterSpec.builder(String.class, "type").build();
        ParameterSpec annotatedTypeParameter = ParameterSpec.builder(String.class, "type")
                .addAnnotation(AnnotationSpec.builder(JsonProperty.class).addMember("value", "\"type\"").build())
                .build();

        ClassName wrapperClass = baseClass.peerClass(UNKNOWN_WRAPPER_CLASS_NAME);
        List<FieldSpec> fields = ImmutableList.of(
                FieldSpec.builder(String.class, "type", Modifier.PRIVATE, Modifier.FINAL).build(),
                FieldSpec.builder(genericMapType, VALUE_FIELD_NAME, Modifier.PRIVATE, Modifier.FINAL).build());
        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(wrapperClass)
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .addSuperinterface(baseClass)
                .addAnnotation(AnnotationSpec.builder(JsonTypeInfo.class)
                        .addMember("use", "JsonTypeInfo.Id.NAME")
                        .addMember("include", "JsonTypeInfo.As.EXISTING_PROPERTY")
                        .addMember("property", "\"type\"")
                        .addMember("visible", "$L", true)
                        .build())
                .addFields(fields)
                .addMethod(MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PRIVATE)
                        .addAnnotation(AnnotationSpec.builder(JsonCreator.class).build())
                        .addParameter(annotatedTypeParameter)
                        .addStatement("this($N, new $T())", typeParameter, genericHashMapType)
                        .build())
                .addMethod(MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PRIVATE)
                        .addParameter(typeParameter)
                        .addParameter(ParameterSpec.builder(genericMapType, VALUE_FIELD_NAME).build())
                        .addStatement("$L", Expressions.requireNonNull(typeParameter.name, "type cannot be null"))
                        .addStatement("$L", Expressions.requireNonNull(VALUE_FIELD_NAME,
                                String.format("%s cannot be null", VALUE_FIELD_NAME)))
                        .addStatement("this.$1N = $1N", typeParameter)
                        .addStatement("this.$1L = $1L", VALUE_FIELD_NAME)
                        .build())
                .addMethod(MethodSpec.methodBuilder("getType")
                        .addModifiers(Modifier.PRIVATE)
                        .addAnnotation(AnnotationSpec.builder(JsonProperty.class).build())
                        .addStatement("return type")
                        .returns(String.class)
                        .build())
                .addMethod(MethodSpec.methodBuilder("getValue")
                        .addModifiers(Modifier.PRIVATE)
                        .addAnnotation(AnnotationSpec.builder(JsonAnyGetter.class).build())
                        .addStatement("return $L", VALUE_FIELD_NAME)
                        .returns(genericMapType)
                        .build())
                .addMethod(MethodSpec.methodBuilder("put")
                        .addModifiers(Modifier.PRIVATE)
                        .addParameter(String.class, "key")
                        .addParameter(Object.class, "val")
                        .addAnnotation(AnnotationSpec.builder(JsonAnySetter.class).build())
                        .addStatement("$L.put(key, val)", VALUE_FIELD_NAME)
                        .build())
                .addMethod(MethodSpecs.createEquals(wrapperClass))
                .addMethod(MethodSpecs.createEqualTo(wrapperClass, fields))
                .addMethod(MethodSpecs.createHashCode(fields))
                .addMethod(MethodSpecs.createToString(wrapperClass.simpleName(),
                        fields.stream().map(
                                fieldSpec -> FieldName.of(fieldSpec.name))
                                .collect(Collectors.toList())));
        return typeBuilder.build();
    }

    private static ClassName wrapperClass(ClassName unionClass, FieldName memberTypeName) {
        return ClassName.get(
                unionClass.packageName(),
                unionClass.simpleName(),
                StringUtils.capitalize(memberTypeName.get()) + "Wrapper");
    }

    private static ClassName peerWrapperClass(ClassName peerClass, FieldName memberTypeName) {
        return peerClass.peerClass(StringUtils.capitalize(memberTypeName.get()) + "Wrapper");
    }

    private static String variableName() {
        return "value";
    }

    private UnionGenerator() {}
}
