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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.PeekingIterator;
import com.palantir.conjure.java.ConjureAnnotations;
import com.palantir.conjure.java.Options;
import com.palantir.conjure.java.lib.internal.ConjureCollections;
import com.palantir.conjure.java.types.BeanGenerator.EnrichedField;
import com.palantir.conjure.java.util.JavaNameSanitizer;
import com.palantir.conjure.java.util.Javadoc;
import com.palantir.conjure.java.util.Primitives;
import com.palantir.conjure.java.util.TypeFunctions;
import com.palantir.conjure.java.visitor.DefaultTypeVisitor;
import com.palantir.conjure.java.visitor.DefaultableTypeVisitor;
import com.palantir.conjure.java.visitor.MoreVisitors;
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
import com.palantir.conjure.visitor.TypeDefinitionVisitor;
import com.palantir.conjure.visitor.TypeVisitor;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import com.palantir.logsafe.exceptions.SafeIllegalStateException;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.lang.model.element.Modifier;
import org.apache.commons.lang3.StringUtils;

public final class BeanBuilderGenerator {

    private static final Type.Visitor<Class<?>> COLLECTION_CONCRETE_TYPE = new DefaultTypeVisitor<>() {
        @Override
        public Class<?> visitList(ListType _value) {
            return ArrayList.class;
        }

        @Override
        public Class<?> visitSet(SetType _value) {
            return LinkedHashSet.class;
        }

        @Override
        public Class<?> visitMap(MapType _value) {
            return LinkedHashMap.class;
        }
    };

    private static final String BUILT_FIELD = "_buildInvoked";
    private static final String CHECK_NOT_BUILT_METHOD = "checkNotBuilt";
    private static final String BUILDER_IMPLEMENTATION_NAME = "Builder";
    private static final String STAGED_BUILDER_INTERFACE_NAME = "Builder";
    private static final String STAGED_BUILDER_IMPLEMENTATION_NAME = "DefaultBuilder";

    private final TypeMapper typeMapper;
    private final SafetyEvaluator safetyEvaluator;
    private final ClassName builderClass;
    private final ClassName objectClass;
    private final Options options;

    private BeanBuilderGenerator(
            TypeMapper typeMapper,
            SafetyEvaluator safetyEvaluator,
            ClassName builderClass,
            ClassName objectClass,
            Options options) {
        this.typeMapper = typeMapper;
        this.safetyEvaluator = safetyEvaluator;
        this.builderClass = builderClass;
        this.objectClass = objectClass;
        this.options = options;
    }

    public static void addBuilder(
            TypeSpec.Builder specBuilder,
            TypeMapper typeMapper,
            SafetyEvaluator safetyEvaluator,
            ClassName objectClass,
            ClassName builderClass,
            ObjectDefinition typeDef,
            Map<com.palantir.conjure.spec.TypeName, TypeDefinition> typesMap,
            Options options) {
        new BeanBuilderGenerator(typeMapper, safetyEvaluator, builderClass, objectClass, options)
                .addBuilderImpl(specBuilder, typeDef, typesMap);
    }

    public static void addStagedBuilder(
            TypeSpec.Builder specBuilder,
            TypeMapper typeMapper,
            SafetyEvaluator safetyEvaluator,
            ClassName objectClass,
            ClassName builderClass,
            ObjectDefinition typeDef,
            Map<com.palantir.conjure.spec.TypeName, TypeDefinition> typesMap,
            Options options) {
        new BeanBuilderGenerator(typeMapper, safetyEvaluator, builderClass, objectClass, options)
                .addStagedBuilderImpl(specBuilder, typeDef, typesMap);
    }

    public static boolean fieldShouldBeInFinalStage(EnrichedField field) {
        Type type = field.conjureDef().getType();
        return type.accept(TypeVisitor.IS_LIST)
                || type.accept(TypeVisitor.IS_SET)
                || type.accept(TypeVisitor.IS_MAP)
                || type.accept(TypeVisitor.IS_OPTIONAL);
    }

    private void addBuilderImpl(
            TypeSpec.Builder specBuilder,
            ObjectDefinition typeDef,
            Map<com.palantir.conjure.spec.TypeName, TypeDefinition> typesMap) {
        addJsonDeserializeUsingBuilderAnnotation(specBuilder, BUILDER_IMPLEMENTATION_NAME);
        specBuilder
                .addMethod(MethodSpec.methodBuilder("builder")
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .returns(builderClass)
                        .addStatement("return new $T()", builderClass)
                        .build())
                .addType(generateBuilderImplementation(
                        typeDef, typesMap, BUILDER_IMPLEMENTATION_NAME, Optional.empty()));
    }

    private void addStagedBuilderImpl(
            TypeSpec.Builder builder,
            ObjectDefinition typeDef,
            Map<com.palantir.conjure.spec.TypeName, TypeDefinition> typesMap) {
        List<EnrichedField> allFields = enrichFields(typeDef.getFields());
        List<EnrichedField> fieldsNeedingBuilderStage =
                allFields.stream().filter(f -> !fieldShouldBeInFinalStage(f)).collect(Collectors.toList());

        if (fieldsNeedingBuilderStage.isEmpty()) {
            addBuilderImpl(builder, typeDef, typesMap);
            return;
        }

        addJsonDeserializeUsingBuilderAnnotation(builder, STAGED_BUILDER_IMPLEMENTATION_NAME);
        addStageInterfacesAndBuilderMethod(builder, allFields, fieldsNeedingBuilderStage);
        builder.addType(generateBuilderImplementation(
                typeDef, typesMap, STAGED_BUILDER_IMPLEMENTATION_NAME, Optional.of(STAGED_BUILDER_INTERFACE_NAME)));
    }

    private void addJsonDeserializeUsingBuilderAnnotation(
            TypeSpec.Builder specBuilder, String implementationClassName) {
        specBuilder.addAnnotation(AnnotationSpec.builder(JsonDeserialize.class)
                .addMember(
                        "builder",
                        "$T.class",
                        ClassName.get(objectClass.packageName(), objectClass.simpleName(), implementationClassName))
                .build());
    }

    private void addStageInterfacesAndBuilderMethod(
            TypeSpec.Builder specBuilder,
            List<EnrichedField> allFields,
            List<EnrichedField> fieldsNeedingBuilderStage) {
        Preconditions.checkState(!fieldsNeedingBuilderStage.isEmpty(), "TODO(pritham)");
        List<TypeSpec> interfaces = generateStageInterfaces(
                objectClass,
                builderClass,
                typeMapper,
                fieldsNeedingBuilderStage,
                allFields.stream()
                        .filter(f -> !fieldsNeedingBuilderStage.contains(f))
                        .collect(ImmutableList.toImmutableList()),
                safetyEvaluator);

        List<TypeName> interfaceClassNames = interfaces.stream()
                .map(stageInterface ->
                        ClassName.get(objectClass.packageName(), objectClass.simpleName(), stageInterface.name))
                .map(Primitives::box)
                .collect(Collectors.toList());

        TypeSpec builderInterface = TypeSpec.interfaceBuilder("Builder")
                .addModifiers(Modifier.PUBLIC)
                .addMethods(interfaces.stream()
                        .map(stageInterface -> stageInterface.methodSpecs)
                        .flatMap(List::stream)
                        .map(method -> method.toBuilder()
                                .addAnnotation(Override.class)
                                .returns(
                                        method.name.equals("build")
                                                ? method.returnType
                                                : ClassName.get(
                                                        objectClass.packageName(), objectClass.simpleName(), "Builder"))
                                .build())
                        .collect(Collectors.toList()))
                .addSuperinterfaces(interfaceClassNames)
                .build();

        specBuilder
                .addTypes(interfaces)
                .addType(builderInterface)
                .addMethod(MethodSpec.methodBuilder("builder")
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .returns(interfaceClassNames.get(0))
                        .addStatement("return new DefaultBuilder()")
                        .build());
    }

    private static List<TypeSpec> generateStageInterfaces(
            ClassName objectClass,
            ClassName builderClass,
            TypeMapper typeMapper,
            List<EnrichedField> fieldsNeedingBuilderStage,
            List<EnrichedField> otherFields,
            SafetyEvaluator safetyEvaluator) {
        List<TypeSpec.Builder> interfaces = new ArrayList<>();

        PeekingIterator<EnrichedField> fieldPeekingIterator =
                Iterators.peekingIterator(fieldsNeedingBuilderStage.iterator());

        while (fieldPeekingIterator.hasNext()) {
            EnrichedField field = fieldPeekingIterator.next();
            String nextBuilderStageName = fieldPeekingIterator.hasNext()
                    ? JavaNameSanitizer.sanitize(fieldPeekingIterator.peek().fieldName())
                    : "completed_";

            ClassName nextStageClassName = stageBuilderInterfaceName(objectClass, nextBuilderStageName);

            interfaces.add(TypeSpec.interfaceBuilder(
                            stageBuilderInterfaceName(objectClass, JavaNameSanitizer.sanitize(field.fieldName())))
                    .addModifiers(Modifier.PUBLIC)
                    .addMethod(MethodSpec.methodBuilder(JavaNameSanitizer.sanitize(field.fieldName()))
                            .addParameter(ParameterSpec.builder(
                                            field.poetSpec().type, JavaNameSanitizer.sanitize(field.fieldName()))
                                    .addAnnotation(Nonnull.class)
                                    .build())
                            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                            .returns(Primitives.box(nextStageClassName))
                            .build()));
        }

        ClassName completedStageClass = stageBuilderInterfaceName(objectClass, "completed_");

        TypeSpec.Builder completedStage = TypeSpec.interfaceBuilder(completedStageClass)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(MethodSpec.methodBuilder("build")
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .returns(Primitives.box(objectClass))
                        .build());

        completedStage.addMethods(otherFields.stream()
                .map(field ->
                        generateMethodsForFinalStageField(field, typeMapper, completedStageClass, safetyEvaluator))
                .flatMap(List::stream)
                .collect(Collectors.toList()));

        interfaces.add(completedStage);

        interfaces
                .get(0)
                .addMethod(MethodSpec.methodBuilder("from")
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .returns(builderClass)
                        .addParameter(objectClass, "other")
                        .build());

        return interfaces.stream().map(TypeSpec.Builder::build).collect(Collectors.toList());
    }

    private static ClassName stageBuilderInterfaceName(ClassName enclosingClass, String stageName) {
        return enclosingClass.nestedClass(StringUtils.capitalize(stageName) + "StageBuilder");
    }

    private static List<MethodSpec> generateMethodsForFinalStageField(
            EnrichedField enriched, TypeMapper typeMapper, ClassName returnClass, SafetyEvaluator safetyEvaluator) {
        List<MethodSpec> methodSpecs = new ArrayList<>();
        Type type = enriched.conjureDef().getType();
        FieldDefinition definition = enriched.conjureDef();
        Optional<LogSafety> safety = safetyEvaluator.getUsageTimeSafety(definition);

        methodSpecs.add(MethodSpec.methodBuilder(JavaNameSanitizer.sanitize(enriched.fieldName()))
                .addParameter(ParameterSpec.builder(
                                BeanBuilderAuxiliarySettersUtils.widenParameterIfPossible(
                                        enriched.poetSpec().type, type, typeMapper, safety),
                                JavaNameSanitizer.sanitize(enriched.fieldName()))
                        .addAnnotation(Nonnull.class)
                        .build())
                .addJavadoc(Javadoc.render(definition.getDocs(), definition.getDeprecated())
                        .map(rendered -> CodeBlock.of("$L", rendered))
                        .orElseGet(() -> CodeBlock.builder().build()))
                .addAnnotations(ConjureAnnotations.deprecation(definition.getDeprecated()))
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(Primitives.box(returnClass))
                .build());

        if (type.accept(TypeVisitor.IS_LIST)) {
            methodSpecs.add(BeanBuilderAuxiliarySettersUtils.createCollectionSetterBuilder(
                            "addAll", enriched, typeMapper, returnClass, safetyEvaluator)
                    .addModifiers(Modifier.ABSTRACT)
                    .build());
            methodSpecs.add(BeanBuilderAuxiliarySettersUtils.createItemSetterBuilder(
                            enriched, type.accept(TypeVisitor.LIST).getItemType(), typeMapper, returnClass, safety)
                    .addModifiers(Modifier.ABSTRACT)
                    .build());
        }

        if (type.accept(TypeVisitor.IS_SET)) {
            methodSpecs.add(BeanBuilderAuxiliarySettersUtils.createCollectionSetterBuilder(
                            "addAll", enriched, typeMapper, returnClass, safetyEvaluator)
                    .addModifiers(Modifier.ABSTRACT)
                    .build());
            methodSpecs.add(BeanBuilderAuxiliarySettersUtils.createItemSetterBuilder(
                            enriched, type.accept(TypeVisitor.SET).getItemType(), typeMapper, returnClass, safety)
                    .addModifiers(Modifier.ABSTRACT)
                    .build());
        }

        if (type.accept(TypeVisitor.IS_MAP)) {
            methodSpecs.add(BeanBuilderAuxiliarySettersUtils.createCollectionSetterBuilder(
                            "putAll", enriched, typeMapper, returnClass, safetyEvaluator)
                    .addModifiers(Modifier.ABSTRACT)
                    .build());
            methodSpecs.add(BeanBuilderAuxiliarySettersUtils.createMapSetterBuilder(enriched, typeMapper, returnClass)
                    .addModifiers(Modifier.ABSTRACT)
                    .build());
        }

        if (type.accept(TypeVisitor.IS_OPTIONAL)) {
            methodSpecs.add(BeanBuilderAuxiliarySettersUtils.createOptionalSetterBuilder(
                            enriched, typeMapper, returnClass, safetyEvaluator)
                    .addModifiers(Modifier.ABSTRACT)
                    .build());
        }

        return methodSpecs;
    }

    private TypeSpec generateBuilderImplementation(
            ObjectDefinition typeDef,
            Map<com.palantir.conjure.spec.TypeName, TypeDefinition> typesMap,
            String className,
            Optional<String> builderInterfaceClass) {
        Collection<EnrichedField> enrichedFields = enrichFields(typeDef.getFields());
        Collection<FieldSpec> poetFields = EnrichedField.toPoetSpecs(enrichedFields);
        TypeSpec.Builder specBuilder = TypeSpec.classBuilder(className);

        boolean implementsInterface = builderInterfaceClass.isPresent();

        if (implementsInterface) {
            specBuilder.addSuperinterface(
                    ClassName.get(objectClass.packageName(), objectClass.simpleName(), builderInterfaceClass.get()));
        } else {
            specBuilder.addModifiers(Modifier.PUBLIC);
        }

        specBuilder
                .addAnnotation(ConjureAnnotations.getConjureGeneratedAnnotation(BeanBuilderGenerator.class))
                .addModifiers(Modifier.STATIC, Modifier.FINAL)
                .addField(FieldSpec.builder(boolean.class, BUILT_FIELD).build())
                .addFields(poetFields)
                .addFields(primitivesInitializedFields(enrichedFields))
                .addMethod(createConstructor())
                .addMethod(createFromObject(enrichedFields, implementsInterface))
                .addMethods(createSetters(enrichedFields, typesMap, implementsInterface))
                .addMethods(maybeCreateValidateFieldsMethods(enrichedFields))
                .addMethod(createBuild(enrichedFields, poetFields, implementsInterface))
                .addMethod(createCheckNotBuilt());

        if (!options.strictObjects()) {
            specBuilder.addAnnotation(AnnotationSpec.builder(JsonIgnoreProperties.class)
                    .addMember("ignoreUnknown", "$L", true)
                    .build());
        }
        return specBuilder.build();
    }

    private Collection<MethodSpec> maybeCreateValidateFieldsMethods(Collection<EnrichedField> enrichedFields) {
        List<EnrichedField> primitives =
                enrichedFields.stream().filter(EnrichedField::isPrimitive).collect(Collectors.toList());

        if (primitives.isEmpty()) {
            return Collections.emptyList();
        }

        return ImmutableList.of(createValidateFieldsMethod(primitives), createAddFieldIfMissing(primitives.size()));
    }

    private static MethodSpec createValidateFieldsMethod(List<EnrichedField> primitives) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("validatePrimitiveFieldsHaveBeenInitialized")
                .addModifiers(Modifier.PRIVATE);

        builder.addStatement("$T missingFields = null", ParameterizedTypeName.get(List.class, String.class));
        for (EnrichedField field : primitives) {
            String name = deriveFieldInitializedName(field);
            builder.addStatement(
                    "missingFields = addFieldIfMissing(missingFields, $N, $S)",
                    name,
                    field.fieldName().get());
        }

        builder.beginControlFlow("if (missingFields != null)")
                .addStatement(
                        "throw new $T(\"Some required fields have not been set\","
                                + " $T.of(\"missingFields\", missingFields))",
                        SafeIllegalArgumentException.class,
                        SafeArg.class)
                .endControlFlow();

        return builder.build();
    }

    private static MethodSpec createAddFieldIfMissing(int fieldCount) {
        ParameterizedTypeName listOfStringType = ParameterizedTypeName.get(List.class, String.class);
        ParameterSpec listParam =
                ParameterSpec.builder(listOfStringType, "prev").build();
        ParameterSpec fieldValueParam =
                ParameterSpec.builder(TypeName.BOOLEAN, "initialized").build();
        ParameterSpec fieldNameParam =
                ParameterSpec.builder(ClassName.get(String.class), "fieldName").build();

        return MethodSpec.methodBuilder("addFieldIfMissing")
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .returns(listOfStringType)
                .addParameter(listParam)
                .addParameter(fieldValueParam)
                .addParameter(fieldNameParam)
                .addStatement("$T missingFields = $N", listOfStringType, listParam)
                .beginControlFlow("if (!$N)", fieldValueParam)
                .beginControlFlow("if (missingFields == null)")
                .addStatement("missingFields = new $T<>($L)", ArrayList.class, fieldCount)
                .endControlFlow()
                .addStatement("missingFields.add($N)", fieldNameParam)
                .endControlFlow()
                .addStatement("return missingFields")
                .build();
    }

    private Collection<FieldSpec> primitivesInitializedFields(Collection<EnrichedField> enrichedFields) {
        return enrichedFields.stream()
                .filter(EnrichedField::isPrimitive)
                .map(field -> FieldSpec.builder(TypeName.BOOLEAN, deriveFieldInitializedName(field), Modifier.PRIVATE)
                        .initializer("false")
                        .build())
                .collect(Collectors.toList());
    }

    private static String deriveFieldInitializedName(EnrichedField field) {
        return "_" + JavaNameSanitizer.sanitize(field.conjureDef().getFieldName()) + "Initialized";
    }

    private ImmutableList<EnrichedField> enrichFields(List<FieldDefinition> fields) {
        return fields.stream().map(e -> createField(e.getFieldName(), e)).collect(ImmutableList.toImmutableList());
    }

    private static MethodSpec createConstructor() {
        return MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE).build();
    }

    private MethodSpec createFromObject(Collection<EnrichedField> enrichedFields, boolean override) {
        CodeBlock assignmentBlock = CodeBlocks.of(Collections2.transform(
                enrichedFields,
                enrichedField -> CodeBlocks.statement(
                        "$1N(other.$2N())", enrichedField.poetSpec().name, enrichedField.getterName())));

        return MethodSpec.methodBuilder("from")
                .addAnnotations(ConjureAnnotations.override(override))
                .addModifiers(Modifier.PUBLIC)
                .returns(builderClass)
                .addParameter(objectClass, "other")
                .addCode(verifyNotBuilt())
                .addCode(assignmentBlock)
                .addStatement("return this")
                .build();
    }

    private EnrichedField createField(FieldName fieldName, FieldDefinition field) {
        Type type = field.getType();
        TypeName typeName =
                ConjureAnnotations.withSafety(typeMapper.getClassName(type), safetyEvaluator.getUsageTimeSafety(field));
        FieldSpec.Builder spec = FieldSpec.builder(typeName, JavaNameSanitizer.sanitize(fieldName), Modifier.PRIVATE);
        if (type.accept(TypeVisitor.IS_LIST) || type.accept(TypeVisitor.IS_SET) || type.accept(TypeVisitor.IS_MAP)) {
            spec.initializer("new $T<>()", type.accept(COLLECTION_CONCRETE_TYPE));
        } else if (type.accept(TypeVisitor.IS_OPTIONAL)) {
            spec.initializer("$T.empty()", asRawType(typeMapper.getClassName(type)));
        } else if (type.accept(MoreVisitors.IS_INTERNAL_REFERENCE)) {
            com.palantir.conjure.spec.TypeName name = type.accept(TypeVisitor.REFERENCE);
            typeMapper
                    .getType(name)
                    .filter(definition -> definition.accept(TypeDefinitionVisitor.IS_ALIAS))
                    .map(definition -> definition.accept(TypeDefinitionVisitor.ALIAS))
                    .ifPresent(aliasDefinition -> {
                        Type aliasType = aliasDefinition.getAlias();
                        if (aliasType.accept(MoreVisitors.IS_COLLECTION) || aliasType.accept(TypeVisitor.IS_OPTIONAL)) {
                            spec.initializer("$T.empty()", typeMapper.getClassName(type));
                        }
                    });
        }
        // else no initializer

        return EnrichedField.of(fieldName, field, spec.build());
    }

    private Iterable<MethodSpec> createSetters(
            Collection<EnrichedField> fields,
            Map<com.palantir.conjure.spec.TypeName, TypeDefinition> typesMap,
            boolean override) {
        Collection<MethodSpec> setters = Lists.newArrayListWithExpectedSize(fields.size());
        for (EnrichedField field : fields) {
            setters.add(createSetter(field, typesMap, override));
            setters.addAll(createAuxiliarySetters(field, override));
        }
        return setters;
    }

    private MethodSpec createSetter(
            EnrichedField enriched,
            Map<com.palantir.conjure.spec.TypeName, TypeDefinition> typesMap,
            boolean override) {
        FieldSpec field = enriched.poetSpec();
        Type type = enriched.conjureDef().getType();
        AnnotationSpec.Builder annotationBuilder = AnnotationSpec.builder(JsonSetter.class)
                .addMember("value", "$S", enriched.fieldName().get());
        if (type.accept(TypeVisitor.IS_OPTIONAL)) {
            annotationBuilder.addMember("nulls", "$T.SKIP", Nulls.class);
        } else if (isCollectionType(type)) {
            annotationBuilder.addMember("nulls", "$T.SKIP", Nulls.class);
            if (isOptionalInnerType(type)) {
                annotationBuilder.addMember("contentNulls", "$T.AS_EMPTY", Nulls.class);
            } else if (options.nonNullCollections()) {
                annotationBuilder.addMember("contentNulls", "$T.FAIL", Nulls.class);
            }
        } else if (type.accept(TypeVisitor.IS_REFERENCE)) {
            Type dealiased = TypeFunctions.toConjureTypeWithoutAliases(type, typesMap);
            if (dealiased.accept(DefaultableTypeVisitor.INSTANCE)) {
                annotationBuilder.addMember("nulls", "$T.AS_EMPTY", Nulls.class);
            }
        }

        boolean shouldClearFirst = true;
        MethodSpec.Builder setterBuilder = BeanBuilderAuxiliarySettersUtils.publicSetter(enriched, builderClass)
                .addParameter(Parameters.nonnullParameter(
                        BeanBuilderAuxiliarySettersUtils.widenParameterIfPossible(
                                field.type,
                                type,
                                typeMapper,
                                safetyEvaluator.getUsageTimeSafety(enriched.conjureDef())),
                        field.name))
                .addCode(verifyNotBuilt())
                .addCode(typeAwareAssignment(enriched, type, shouldClearFirst));

        if (enriched.isPrimitive()) {
            setterBuilder.addCode("this.$L = true;", deriveFieldInitializedName(enriched));
        }

        return setterBuilder
                .addStatement("return this")
                .addAnnotations(ConjureAnnotations.override(override))
                .addAnnotation(annotationBuilder.build())
                .build();
    }

    private MethodSpec createCollectionSetter(String prefix, EnrichedField enriched, boolean override) {
        FieldDefinition definition = enriched.conjureDef();
        Type type = definition.getType();
        boolean shouldClearFirst = false;
        return BeanBuilderAuxiliarySettersUtils.createCollectionSetterBuilder(
                        prefix, enriched, typeMapper, builderClass, safetyEvaluator)
                .addAnnotations(ConjureAnnotations.override(override))
                .addCode(verifyNotBuilt())
                .addCode(typeAwareAssignment(enriched, type, shouldClearFirst))
                .addStatement("return this")
                .build();
    }

    private CodeBlock typeAwareAssignment(EnrichedField enriched, Type type, boolean shouldClearFirst) {
        FieldSpec spec = enriched.poetSpec();
        if (type.accept(TypeVisitor.IS_LIST) || type.accept(TypeVisitor.IS_SET)) {
            if (shouldClearFirst) {
                return CodeBlocks.statement(
                        "this.$1N = $2T.new$3T($4L)",
                        spec.name,
                        ConjureCollections.class,
                        type.accept(COLLECTION_CONCRETE_TYPE),
                        Expressions.requireNonNull(
                                spec.name, enriched.fieldName().get() + " cannot be null"));
            }
            return CodeBlocks.statement(
                    "$1T.addAll(this.$2N, $3L)",
                    ConjureCollections.class,
                    spec.name,
                    Expressions.requireNonNull(spec.name, enriched.fieldName().get() + " cannot be null"));
        } else if (type.accept(TypeVisitor.IS_MAP)) {
            if (shouldClearFirst) {
                return CodeBlocks.statement(
                        "this.$1N = new $2T<>($3L)",
                        spec.name,
                        type.accept(COLLECTION_CONCRETE_TYPE),
                        Expressions.requireNonNull(
                                spec.name, enriched.fieldName().get() + " cannot be null"));
            }
            return CodeBlocks.statement(
                    "this.$1N.putAll($2L)",
                    spec.name,
                    Expressions.requireNonNull(spec.name, enriched.fieldName().get() + " cannot be null"));
        } else if (isByteBuffer(type)) {
            return CodeBlock.builder()
                    .addStatement(
                            "$L",
                            Expressions.requireNonNull(
                                    spec.name, enriched.fieldName().get() + " cannot be null"))
                    .addStatement(
                            "this.$1N = $2T.allocate($1N.remaining()).put($1N.duplicate())",
                            spec.name,
                            ByteBuffer.class)
                    .addStatement("(($1T)this.$2N).rewind()", Buffer.class, spec.name)
                    .build();
        } else if (type.accept(TypeVisitor.IS_OPTIONAL)) {
            OptionalType optionalType = type.accept(TypeVisitor.OPTIONAL);
            CodeBlock nullCheckedValue =
                    Expressions.requireNonNull(spec.name, enriched.fieldName().get() + " cannot be null");

            if (BeanBuilderAuxiliarySettersUtils.isWidenableContainedType(optionalType.getItemType())) {
                // we capture covariant type via generic Function#identity mapping before assignment to bind
                // the resultant optional to the invariant inner variable type
                return CodeBlock.builder()
                        .addStatement("this.$1N = $2L.map($3T.identity())", spec.name, nullCheckedValue, Function.class)
                        .build();
            } else {
                return CodeBlocks.statement("this.$1L = $2L", spec.name, nullCheckedValue);
            }
        } else {
            CodeBlock nullCheckedValue = Primitives.isPrimitive(spec.type)
                    ? CodeBlock.of("$N", spec.name) // primitive types can't be null, so no need for requireNonNull!
                    : Expressions.requireNonNull(spec.name, enriched.fieldName().get() + " cannot be null");
            return CodeBlocks.statement("this.$1L = $2L", spec.name, nullCheckedValue);
        }
    }

    private boolean isByteBuffer(Type type) {
        return type.accept(TypeVisitor.IS_BINARY) && !options.useImmutableBytes();
    }

    private List<MethodSpec> createAuxiliarySetters(EnrichedField enriched, boolean override) {
        Type type = enriched.conjureDef().getType();
        Optional<LogSafety> safety = safetyEvaluator.getUsageTimeSafety(enriched.conjureDef());

        if (type.accept(TypeVisitor.IS_LIST)) {
            return ImmutableList.of(
                    createCollectionSetter("addAll", enriched, override),
                    createItemSetter(enriched, type.accept(TypeVisitor.LIST).getItemType(), override, safety));
        }

        if (type.accept(TypeVisitor.IS_SET)) {
            return ImmutableList.of(
                    createCollectionSetter("addAll", enriched, override),
                    createItemSetter(enriched, type.accept(TypeVisitor.SET).getItemType(), override, safety));
        }

        if (type.accept(TypeVisitor.IS_MAP)) {
            return ImmutableList.of(
                    createCollectionSetter("putAll", enriched, override), createMapSetter(enriched, override));
        }

        if (type.accept(TypeVisitor.IS_OPTIONAL)) {
            return ImmutableList.of(createOptionalSetter(enriched, override));
        }

        return ImmutableList.of();
    }

    private MethodSpec createOptionalSetter(EnrichedField enriched, boolean override) {
        OptionalType type = enriched.conjureDef().getType().accept(TypeVisitor.OPTIONAL);
        return BeanBuilderAuxiliarySettersUtils.createOptionalSetterBuilder(
                        enriched, typeMapper, builderClass, safetyEvaluator)
                .addAnnotations(ConjureAnnotations.override(override))
                .addCode(verifyNotBuilt())
                .addCode(optionalAssignmentStatement(enriched, type))
                .addStatement("return this")
                .build();
    }

    @SuppressWarnings("checkstyle:cyclomaticcomplexity")
    private CodeBlock optionalAssignmentStatement(EnrichedField enriched, OptionalType type) {
        FieldSpec spec = enriched.poetSpec();
        if (isPrimitiveOptional(type)) {
            return CodeBlocks.statement(
                    "this.$1N = $2T.of($1N)",
                    enriched.poetSpec().name,
                    asRawType(typeMapper.getClassName(Type.optional(type))));
        } else {
            return CodeBlocks.statement(
                    "this.$1N = $2T.of($3L)",
                    spec.name,
                    Optional.class,
                    Expressions.requireNonNull(spec.name, enriched.fieldName().get() + " cannot be null"));
        }
    }

    private static final EnumSet<PrimitiveType.Value> OPTIONAL_PRIMITIVES =
            EnumSet.of(PrimitiveType.Value.INTEGER, PrimitiveType.Value.DOUBLE, PrimitiveType.Value.BOOLEAN);

    /** Check if the optionalType contains a primitive boolean, double or integer. */
    private boolean isPrimitiveOptional(OptionalType optionalType) {
        return optionalType.getItemType().accept(TypeVisitor.IS_PRIMITIVE)
                && OPTIONAL_PRIMITIVES.contains(
                        optionalType.getItemType().accept(TypeVisitor.PRIMITIVE).get());
    }

    private MethodSpec createItemSetter(
            EnrichedField enriched, Type itemType, boolean override, Optional<LogSafety> safety) {
        FieldSpec field = enriched.poetSpec();
        return BeanBuilderAuxiliarySettersUtils.createItemSetterBuilder(
                        enriched, itemType, typeMapper, builderClass, safety)
                .addAnnotations(ConjureAnnotations.override(override))
                .addCode(verifyNotBuilt())
                .addStatement("this.$1N.add($1N)", field.name)
                .addStatement("return this")
                .build();
    }

    private MethodSpec createMapSetter(EnrichedField enriched, boolean override) {
        return BeanBuilderAuxiliarySettersUtils.createMapSetterBuilder(enriched, typeMapper, builderClass)
                .addAnnotations(ConjureAnnotations.override(override))
                .addCode(verifyNotBuilt())
                .addStatement("this.$1N.put(key, value)", enriched.poetSpec().name)
                .addStatement("return this")
                .build();
    }

    private MethodSpec createBuild(
            Collection<EnrichedField> enrichedFields, Collection<FieldSpec> fields, boolean override) {
        MethodSpec.Builder method = MethodSpec.methodBuilder("build")
                .addAnnotations(ConjureAnnotations.override(override))
                .addModifiers(Modifier.PUBLIC)
                .returns(objectClass)
                .addCode(verifyNotBuilt())
                .addStatement("this.$N = true", BUILT_FIELD);

        if (enrichedFields.stream().anyMatch(EnrichedField::isPrimitive)) {
            method.addStatement("validatePrimitiveFieldsHaveBeenInitialized()");
        }

        return method.addStatement("return new $L", Expressions.constructorCall(objectClass, fields))
                .build();
    }

    private static MethodSpec createCheckNotBuilt() {
        return MethodSpec.methodBuilder(CHECK_NOT_BUILT_METHOD)
                .addModifiers(Modifier.PRIVATE)
                .addStatement(
                        "$T.checkState(!$N, $S)", Preconditions.class, BUILT_FIELD, "Build has already been called")
                .build();
    }

    private static CodeBlock verifyNotBuilt() {
        return CodeBlocks.statement("$N()", CHECK_NOT_BUILT_METHOD);
    }

    private static TypeName asRawType(TypeName type) {
        if (type instanceof ParameterizedTypeName) {
            return ((ParameterizedTypeName) type).rawType;
        }
        return type;
    }

    private static boolean isCollectionType(Type type) {
        return type.accept(TypeVisitor.IS_LIST) || type.accept(TypeVisitor.IS_SET) || type.accept(TypeVisitor.IS_MAP);
    }

    private boolean isOptionalInnerType(Type type) {
        return type.accept(new Type.Visitor<>() {
            @Override
            public Boolean visitPrimitive(PrimitiveType value) {
                return false;
            }

            @Override
            public Boolean visitOptional(OptionalType value) {
                return true;
            }

            @Override
            public Boolean visitList(ListType value) {
                return isOptionalInnerType(value.getItemType());
            }

            @Override
            public Boolean visitSet(SetType value) {
                return value.getItemType().accept(TypeVisitor.IS_OPTIONAL);
            }

            @Override
            public Boolean visitMap(MapType value) {
                return isOptionalInnerType(value.getValueType());
            }

            @Override
            public Boolean visitReference(com.palantir.conjure.spec.TypeName value) {
                return typeMapper
                        .getType(value)
                        .map(typeDef -> typeDef.accept(TypeDefinitionVisitor.IS_ALIAS)
                                && typeDef.accept(TypeDefinitionVisitor.ALIAS)
                                        .getAlias()
                                        .accept(TypeVisitor.IS_OPTIONAL))
                        .orElse(false);
            }

            @Override
            public Boolean visitExternal(ExternalReference value) {
                return false;
            }

            @Override
            public Boolean visitUnknown(String unknownType) {
                throw new SafeIllegalStateException("Encountered unknown type", SafeArg.of("type", unknownType));
            }
        });
    }
}
