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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Collections2;
import com.palantir.conjure.java.ConjureAnnotations;
import com.palantir.conjure.java.util.CaseConverter;
import com.palantir.conjure.java.util.JavaNameSanitizer;
import com.palantir.conjure.spec.FieldDefinition;
import com.palantir.conjure.spec.FieldName;
import com.palantir.conjure.spec.ObjectDefinition;
import com.palantir.conjure.visitor.TypeVisitor;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.lang.model.element.Modifier;
import org.apache.commons.lang3.StringUtils;
import org.immutables.value.Value;

public final class BeanGenerator {

    private BeanGenerator() {}

    /** The maximum number of parameters for which a static factory method is generated in addition to the builder. */
    private static final int MAX_NUM_PARAMS_FOR_FACTORY = 3;

    /** The name of the singleton instance field generated for empty types. */
    private static final String SINGLETON_INSTANCE_NAME = "INSTANCE";

    public static JavaFile generateBeanType(TypeMapper typeMapper, ObjectDefinition typeDef) {

        String typePackage = typeDef.getTypeName().getPackage();
        ClassName objectClass = ClassName.get(typePackage, typeDef.getTypeName().getName());
        ClassName builderClass = ClassName.get(objectClass.packageName(), objectClass.simpleName(), "Builder");

        Collection<EnrichedField> fields = createFields(typeMapper, typeDef.getFields());
        Collection<FieldSpec> poetFields = EnrichedField.toPoetSpecs(fields);
        Collection<EnrichedField> nonPrimitiveEnrichedFields = fields.stream()
                .filter(field -> !field.isPrimitive())
                .collect(Collectors.toList());

        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(typeDef.getTypeName().getName())
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addFields(poetFields)
                .addMethod(createConstructor(fields, poetFields))
                .addMethods(createGetters(fields));

        if (!poetFields.isEmpty()) {
            typeBuilder
                    .addMethod(MethodSpecs.createEquals(objectClass))
                    .addMethod(MethodSpecs.createEqualTo(objectClass, poetFields));
            MethodSpecs.addCachedHashCode(typeBuilder, poetFields);
        }

        typeBuilder.addMethod(MethodSpecs.createToString(typeDef.getTypeName().getName(),
                fields.stream().map(EnrichedField::fieldName).collect(Collectors.toList())));

        if (poetFields.size() <= MAX_NUM_PARAMS_FOR_FACTORY) {
            typeBuilder.addMethod(createStaticFactoryMethod(poetFields, objectClass));
        }

        if (!nonPrimitiveEnrichedFields.isEmpty()) {
            typeBuilder
                    .addMethod(createValidateFields(nonPrimitiveEnrichedFields))
                    .addMethod(createAddFieldIfMissing(nonPrimitiveEnrichedFields.size()));
        }

        if (poetFields.isEmpty()) {
            // Need to add JsonSerialize annotation which indicates that the empty bean serializer should be used to
            // serialize this class. Without this annotation no serializer will be set for this class, thus preventing
            // serialization.
            typeBuilder
                    .addAnnotation(JsonSerialize.class)
                    .addField(createSingletonField(objectClass));
        } else {
            typeBuilder
                    .addAnnotation(AnnotationSpec.builder(JsonDeserialize.class)
                            .addMember("builder", "$T.class", builderClass).build())
                    .addMethod(createBuilder(builderClass))
                    .addType(BeanBuilderGenerator.generate(
                            typeMapper, objectClass, builderClass, typeDef));
        }

        typeBuilder.addAnnotation(ConjureAnnotations.getConjureGeneratedAnnotation(BeanGenerator.class));

        typeDef.getDocs().ifPresent(docs ->
                typeBuilder.addJavadoc("$L", StringUtils.appendIfMissing(docs.get(), "\n")));

        return JavaFile.builder(typePackage, typeBuilder.build())
                .skipJavaLangImports(true)
                .indent("    ")
                .build();
    }

    private static FieldSpec createSingletonField(ClassName objectClass) {
        return FieldSpec
                .builder(objectClass, SINGLETON_INSTANCE_NAME, Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer("new $T()", objectClass)
                .build();
    }

    private static Collection<EnrichedField> createFields(
            TypeMapper typeMapper, List<FieldDefinition> fields) {
        return fields.stream()
                .map(e -> EnrichedField.of(e.getFieldName(), e, FieldSpec.builder(
                        // fields are guarded against using reserved keywords
                        typeMapper.getClassName(e.getType()),
                        JavaNameSanitizer.sanitize(e.getFieldName()),
                        Modifier.PRIVATE, Modifier.FINAL)
                        .build()))
                .collect(Collectors.toList());
    }

    private static MethodSpec createConstructor(Collection<EnrichedField> fields, Collection<FieldSpec> poetFields) {
        MethodSpec.Builder builder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PRIVATE);

        Collection<FieldSpec> nonPrimitivePoetFields = Collections2.filter(poetFields, f -> !f.type.isPrimitive());
        if (!nonPrimitivePoetFields.isEmpty()) {
            builder.addStatement("$L", Expressions.localMethodCall("validateFields", nonPrimitivePoetFields));
        }

        CodeBlock.Builder body = CodeBlock.builder();
        for (EnrichedField field : fields) {
            FieldSpec spec = field.poetSpec();

            builder.addParameter(spec.type, spec.name);

            // Collection and Map types not copied in constructor for performance. This assumes that the constructor
            // is private and necessarily called from the builder, which does its own defensive copying.
            if (field.conjureDef().getType().accept(TypeVisitor.IS_LIST)) {
                // TODO(melliot): contribute a fix to JavaPoet that parses $T correctly for a JavaPoet FieldSpec
                body.addStatement("this.$1N = $2T.unmodifiableList($1N)", spec, Collections.class);
            } else if (field.conjureDef().getType().accept(TypeVisitor.IS_SET)) {
                body.addStatement("this.$1N = $2T.unmodifiableSet($1N)", spec, Collections.class);
            } else if (field.conjureDef().getType().accept(TypeVisitor.IS_MAP)) {
                body.addStatement("this.$1N = $2T.unmodifiableMap($1N)", spec, Collections.class);
            } else {
                body.addStatement("this.$1N = $1N", spec);
            }
        }

        builder.addCode(body.build());

        return builder.build();
    }

    private static Collection<MethodSpec> createGetters(Collection<EnrichedField> fields) {
        return fields.stream()
                .map(BeanGenerator::createGetter)
                .collect(Collectors.toList());
    }

    private static MethodSpec createGetter(EnrichedField field) {
        MethodSpec.Builder getterBuilder = MethodSpec.methodBuilder(field.getterName())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(AnnotationSpec.builder(JsonProperty.class)
                        .addMember("value", "$S", field.fieldName().get())
                        .build())
                .returns(field.poetSpec().type);

        if (field.conjureDef().getType().accept(TypeVisitor.IS_BINARY)) {
            getterBuilder.addStatement("return this.$N.asReadOnlyBuffer()", field.poetSpec().name);
        } else {
            getterBuilder.addStatement("return this.$N", field.poetSpec().name);
        }

        field.conjureDef().getDocs().ifPresent(docs ->
                getterBuilder.addJavadoc("$L", StringUtils.appendIfMissing(docs.get(), "\n")));
        return getterBuilder.build();
    }

    private static MethodSpec createValidateFields(Collection<EnrichedField> fields) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("validateFields")
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC);

        builder.addStatement("$T missingFields = null", ParameterizedTypeName.get(List.class, String.class));
        for (EnrichedField field : fields) {
            FieldSpec spec = field.poetSpec();
            builder.addParameter(ParameterSpec.builder(spec.type, spec.name).build());
            builder.addStatement(
                    "missingFields = addFieldIfMissing(missingFields, $N, $S)", spec, field.fieldName().get());
        }

        builder
                .beginControlFlow("if (missingFields != null)")
                .addStatement("throw new $T(\"Some required fields have not been set\","
                                + " $T.of(\"missingFields\", missingFields))",
                        SafeIllegalArgumentException.class, SafeArg.class)
                .endControlFlow();
        return builder.build();
    }

    private static MethodSpec createStaticFactoryMethod(Collection<FieldSpec> fields, ClassName objectClass) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("of")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(objectClass);

        if (fields.isEmpty()) {
            builder
                    .addAnnotation(JsonCreator.class)
                    .addCode("return $L;", SINGLETON_INSTANCE_NAME);
        } else {
            builder.addCode("return builder()");
            for (FieldSpec spec : fields) {
                if (isOptional(spec)) {
                    builder.addCode("\n    .$L(Optional.of($L))", spec.name, spec.name);
                } else {
                    builder.addCode("\n    .$L($L)", spec.name, spec.name);
                }
                builder.addParameter(ParameterSpec.builder(getTypeNameWithoutOptional(spec), spec.name).build());
            }
            builder.addCode("\n    .build();\n");
        }

        return builder.build();
    }

    private static MethodSpec createAddFieldIfMissing(int fieldCount) {
        ParameterizedTypeName listOfStringType = ParameterizedTypeName.get(List.class, String.class);
        ParameterSpec listParam = ParameterSpec.builder(listOfStringType, "prev").build();
        ParameterSpec fieldValueParam =
                ParameterSpec.builder(com.squareup.javapoet.TypeName.OBJECT, "fieldValue").build();
        ParameterSpec fieldNameParam = ParameterSpec.builder(ClassName.get(String.class), "fieldName").build();

        return MethodSpec.methodBuilder("addFieldIfMissing")
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .returns(listOfStringType)
                .addParameter(listParam)
                .addParameter(fieldValueParam)
                .addParameter(fieldNameParam)
                .addStatement("$T missingFields = $N", listOfStringType, listParam)
                .beginControlFlow("if ($N == null)", fieldValueParam)
                .beginControlFlow("if (missingFields == null)")
                .addStatement("missingFields = new $T<>($L)", ArrayList.class, fieldCount)
                .endControlFlow()
                .addStatement("missingFields.add($N)", fieldNameParam)
                .endControlFlow()
                .addStatement("return missingFields")
                .build();
    }

    private static MethodSpec createBuilder(ClassName builderClass) {
        return MethodSpec.methodBuilder("builder")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(builderClass)
                .addStatement("return new $T()", builderClass)
                .build();
    }

    private static com.squareup.javapoet.TypeName getTypeNameWithoutOptional(FieldSpec spec) {
        if (!isOptional(spec)) {
            return spec.type;
        }
        return ((ParameterizedTypeName) spec.type).typeArguments.get(0);
    }

    private static boolean isOptional(FieldSpec spec) {
        if (!(spec.type instanceof ParameterizedTypeName)) {
            // spec isn't a wrapper class
            return false;
        }
        return ((ParameterizedTypeName) spec.type).rawType.simpleName().equals("Optional");
    }

    /**
     * Note, this is an implementation detail shared between {@link BeanBuilderGenerator} and {@link BeanGenerator}.
     */
    @Value.Immutable
    interface EnrichedField {
        @Value.Parameter
        FieldName fieldName();

        @Value.Derived
        default String getterName() {
            return asGetterName(fieldName().get());
        }

        @Value.Parameter
        FieldDefinition conjureDef();

        @Value.Parameter
        FieldSpec poetSpec();

        @Value.Derived
        default boolean isPrimitive() {
            return poetSpec().type.isPrimitive();
        }

        static EnrichedField of(FieldName fieldName, FieldDefinition conjureDef, FieldSpec poetSpec) {
            return ImmutableEnrichedField.of(fieldName, conjureDef, poetSpec);
        }

        static Collection<FieldSpec> toPoetSpecs(Collection<EnrichedField> fields) {
            return fields.stream().map(EnrichedField::poetSpec).collect(Collectors.toList());
        }
    }

    public static String asGetterName(String fieldName) {
        String lowerCamelCaseName = CaseConverter.toCase(fieldName, CaseConverter.Case.LOWER_CAMEL_CASE);
        return "get" + StringUtils.capitalize(lowerCamelCaseName);
    }
}
