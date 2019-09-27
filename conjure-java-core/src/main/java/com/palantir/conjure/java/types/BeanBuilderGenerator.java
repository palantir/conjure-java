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
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.palantir.conjure.java.ConjureAnnotations;
import com.palantir.conjure.java.FeatureFlags;
import com.palantir.conjure.java.lib.internal.ConjureCollections;
import com.palantir.conjure.java.types.BeanGenerator.EnrichedField;
import com.palantir.conjure.java.util.JavaNameSanitizer;
import com.palantir.conjure.java.util.Javadoc;
import com.palantir.conjure.java.visitor.DefaultTypeVisitor;
import com.palantir.conjure.spec.FieldDefinition;
import com.palantir.conjure.spec.FieldName;
import com.palantir.conjure.spec.MapType;
import com.palantir.conjure.spec.ObjectDefinition;
import com.palantir.conjure.spec.OptionalType;
import com.palantir.conjure.spec.PrimitiveType;
import com.palantir.conjure.spec.Type;
import com.palantir.conjure.visitor.TypeVisitor;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.lang.model.element.Modifier;
import org.apache.commons.lang3.StringUtils;

public final class BeanBuilderGenerator {
    private final TypeMapper typeMapper;
    private final ClassName builderClass;
    private final ClassName objectClass;
    private final Set<FeatureFlags> featureFlags;

    private BeanBuilderGenerator(
            TypeMapper typeMapper,
            ClassName builderClass,
            ClassName objectClass,
            Set<FeatureFlags> featureFlags) {
        this.typeMapper = typeMapper;
        this.builderClass = builderClass;
        this.objectClass = objectClass;
        this.featureFlags = featureFlags;
    }

    public static TypeSpec generate(
            TypeMapper typeMapper,
            ClassName objectClass,
            ClassName builderClass,
            ObjectDefinition typeDef,
            Set<FeatureFlags> featureFlags) {
        return new BeanBuilderGenerator(typeMapper, builderClass, objectClass, featureFlags).generate(typeDef);
    }

    private TypeSpec generate(ObjectDefinition typeDef) {
        Collection<EnrichedField> enrichedFields = enrichFields(typeDef.getFields());
        Collection<FieldSpec> poetFields = EnrichedField.toPoetSpecs(enrichedFields);

        TypeSpec.Builder builder = TypeSpec.classBuilder("Builder")
                .addAnnotation(ConjureAnnotations.getConjureGeneratedAnnotation(BeanBuilderGenerator.class))
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .addFields(poetFields)
                .addFields(primitivesInitializedFields(enrichedFields))
                .addMethod(createConstructor())
                .addMethod(createFromObject(enrichedFields))
                .addMethods(createSetters(enrichedFields))
                .addMethods(maybeCreateValidateFieldsMethods(enrichedFields))
                .addMethod(createBuild(enrichedFields, poetFields))
                .addAnnotation(
                        AnnotationSpec.builder(JsonIgnoreProperties.class)
                                .addMember("ignoreUnknown", "$L", true)
                                .build());

        return builder.build();
    }

    private Collection<MethodSpec> maybeCreateValidateFieldsMethods(Collection<EnrichedField> enrichedFields) {
        List<EnrichedField> primitives = enrichedFields.stream()
                .filter(EnrichedField::isPrimitive)
                .collect(Collectors.toList());

        if (primitives.isEmpty()) {
            return Collections.emptyList();
        }

        return ImmutableList.of(
                createValidateFieldsMethod(primitives),
                createAddFieldIfMissing(primitives.size()));
    }

    private static MethodSpec createValidateFieldsMethod(List<EnrichedField> primitives) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("validatePrimitiveFieldsHaveBeenInitialized")
                .addModifiers(Modifier.PRIVATE);

        builder.addStatement("$T missingFields = null", ParameterizedTypeName.get(List.class, String.class));
        for (EnrichedField field : primitives) {
            String name = deriveFieldInitializedName(field);
            builder.addStatement("missingFields = addFieldIfMissing(missingFields, $N, $S)",
                    name, field.fieldName().get());
        }

        builder.beginControlFlow("if (missingFields != null)")
                .addStatement("throw new $T(\"Some required fields have not been set\","
                                + " $T.of(\"missingFields\", missingFields))",
                        SafeIllegalArgumentException.class, SafeArg.class)
                .endControlFlow();

        return builder.build();
    }

    private static MethodSpec createAddFieldIfMissing(int fieldCount) {
        ParameterizedTypeName listOfStringType = ParameterizedTypeName.get(List.class, String.class);
        ParameterSpec listParam = ParameterSpec.builder(listOfStringType, "prev").build();
        ParameterSpec fieldValueParam =
                ParameterSpec.builder(TypeName.BOOLEAN, "initialized").build();
        ParameterSpec fieldNameParam = ParameterSpec.builder(ClassName.get(String.class), "fieldName").build();

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
                .map(field -> FieldSpec.builder(
                        TypeName.BOOLEAN,
                        deriveFieldInitializedName(field),
                        Modifier.PRIVATE).initializer("false").build())
                .collect(Collectors.toList());
    }

    private static String deriveFieldInitializedName(EnrichedField field) {
        return "_" + JavaNameSanitizer.sanitize(field.conjureDef().getFieldName()) + "Initialized";
    }

    private Collection<EnrichedField> enrichFields(List<FieldDefinition> fields) {
        return fields.stream()
                .map(e -> createField(e.getFieldName(), e))
                .collect(Collectors.toList());
    }

    private static MethodSpec createConstructor() {
        return MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE).build();
    }

    private MethodSpec createFromObject(Collection<EnrichedField> enrichedFields) {
        CodeBlock assignmentBlock = CodeBlocks.of(Collections2.transform(enrichedFields,
                enrichedField -> CodeBlocks.statement(
                        "$1N(other.$2N())",
                        enrichedField.poetSpec().name,
                        enrichedField.getterName())));

        return MethodSpec.methodBuilder("from")
                .addModifiers(Modifier.PUBLIC)
                .returns(builderClass)
                .addParameter(objectClass, "other")
                .addCode(assignmentBlock)
                .addStatement("return this")
                .build();
    }

    private EnrichedField createField(FieldName fieldName, FieldDefinition field) {
        FieldSpec.Builder spec = FieldSpec.builder(
                typeMapper.getClassName(field.getType()),
                JavaNameSanitizer.sanitize(fieldName),
                Modifier.PRIVATE);

        if (field.getType().accept(TypeVisitor.IS_LIST)) {
            spec.initializer("new $T<>()", ArrayList.class);
        } else if (field.getType().accept(TypeVisitor.IS_SET)) {
            spec.initializer("new $T<>()", LinkedHashSet.class);
        } else if (field.getType().accept(TypeVisitor.IS_MAP)) {
            spec.initializer("new $T<>()", LinkedHashMap.class);
        } else if (field.getType().accept(TypeVisitor.IS_OPTIONAL)) {
            spec.initializer("$T.empty()", asRawType(typeMapper.getClassName(field.getType())));
        }
        // else no initializer

        return EnrichedField.of(fieldName, field, spec.build());
    }

    private Iterable<MethodSpec> createSetters(Collection<EnrichedField> fields) {
        Collection<MethodSpec> setters = Lists.newArrayListWithExpectedSize(fields.size());
        for (EnrichedField field : fields) {
            setters.add(createSetter(field));
            setters.addAll(createAuxiliarySetters(field));
        }
        return setters;
    }

    private MethodSpec createSetter(EnrichedField enriched) {
        FieldSpec field = enriched.poetSpec();
        Type type = enriched.conjureDef().getType();
        AnnotationSpec.Builder annotationBuilder = AnnotationSpec.builder(JsonSetter.class)
                .addMember("value", "$S", enriched.fieldName().get());
        if (isCollectionType(type)) {
            annotationBuilder.addMember("nulls", "$T.SKIP", Nulls.class);
        }

        boolean shouldClearFirst = true;
        MethodSpec.Builder setterBuilder = publicSetter(enriched)
                .addParameter(widenParameterIfPossible(field.type, type), field.name)
                .addCode(typeAwareAssignment(enriched, type, shouldClearFirst));

        if (enriched.isPrimitive()) {
            setterBuilder.addCode("this.$L = true;", deriveFieldInitializedName(enriched));
        }

        return setterBuilder
                .addStatement("return this")
                .addAnnotation(annotationBuilder.build())
                .build();
    }

    private MethodSpec createCollectionSetter(String prefix, EnrichedField enriched) {
        FieldSpec field = enriched.poetSpec();
        Type type = enriched.conjureDef().getType();
        boolean shouldClearFirst = false;
        return MethodSpec.methodBuilder(prefix + StringUtils.capitalize(field.name))
                .addJavadoc(enriched.conjureDef().getDocs().map(Javadoc::render).orElse(""))
                .addModifiers(Modifier.PUBLIC)
                .returns(builderClass)
                .addParameter(widenParameterIfPossible(field.type, type), field.name)
                .addCode(typeAwareAssignment(enriched, type, shouldClearFirst))
                .addStatement("return this")
                .build();
    }

    private TypeName widenParameterIfPossible(TypeName current, Type type) {
        if (type.accept(TypeVisitor.IS_LIST)) {
            Type innerType = type.accept(TypeVisitor.LIST).getItemType();
            TypeName innerTypeName = typeMapper.getClassName(innerType).box();
            if (isWidenableContainedType(innerType)) {
                innerTypeName = WildcardTypeName.subtypeOf(innerTypeName);
            }
            return ParameterizedTypeName.get(ClassName.get(Iterable.class), innerTypeName);
        }

        if (type.accept(TypeVisitor.IS_SET)) {
            Type innerType = type.accept(TypeVisitor.SET).getItemType();
            TypeName innerTypeName = typeMapper.getClassName(innerType).box();
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
            TypeName innerTypeName = typeMapper.getClassName(innerType).box();
            return ParameterizedTypeName.get(ClassName.get(Optional.class), WildcardTypeName.subtypeOf(innerTypeName));
        }

        return current;
    }

    private CodeBlock typeAwareAssignment(EnrichedField enriched, Type type, boolean shouldClearFirst) {
        FieldSpec spec = enriched.poetSpec();
        if (type.accept(TypeVisitor.IS_LIST) || type.accept(TypeVisitor.IS_SET)) {
            CodeBlock addStatement = CodeBlocks.statement(
                    "$1T.addAll(this.$2N, $3L)",
                    ConjureCollections.class,
                    spec.name,
                    Expressions.requireNonNull(spec.name, enriched.fieldName().get() + " cannot be null"));
            return shouldClearFirst ? CodeBlocks.of(CodeBlocks.statement("this.$1N.clear()", spec.name), addStatement)
                    : addStatement;
        } else if (type.accept(TypeVisitor.IS_MAP)) {
            CodeBlock addStatement = CodeBlocks.statement(
                    "this.$1N.putAll($2L)", spec.name,
                    Expressions.requireNonNull(spec.name, enriched.fieldName().get() + " cannot be null"));
            return shouldClearFirst ? CodeBlocks.of(CodeBlocks.statement("this.$1N.clear()", spec.name), addStatement)
                    : addStatement;
        } else if (isByteBuffer(type)) {
            return CodeBlock.builder()
                    .addStatement("$L", Expressions.requireNonNull(
                            spec.name, enriched.fieldName().get() + " cannot be null"))
                    .addStatement("this.$1N = $2T.allocate($1N.remaining()).put($1N.duplicate())",
                            spec.name,
                            ByteBuffer.class)
                    .addStatement("(($1T)this.$2N).rewind()", Buffer.class, spec.name)
                    .build();
        } else if (type.accept(TypeVisitor.IS_OPTIONAL)) {
            OptionalType optionalType = type.accept(TypeVisitor.OPTIONAL);
            CodeBlock nullCheckedValue = Expressions.requireNonNull(
                    spec.name, enriched.fieldName().get() + " cannot be null");

            if (isWidenableContainedType(optionalType.getItemType())) {
                // we capture covariant type via generic Function#identity mapping before assignment to bind
                // the resultant optional to the invariant inner variable type
                return CodeBlock.builder()
                        .addStatement("this.$1N = $2L.map($3T.identity())",
                                spec.name, nullCheckedValue, Function.class)
                        .build();
            } else {
                return CodeBlocks.statement("this.$1L = $2L", spec.name, nullCheckedValue);
            }
        } else {
            CodeBlock nullCheckedValue = spec.type.isPrimitive()
                    ? CodeBlock.of("$N", spec.name) // primitive types can't be null, so no need for requireNonNull!
                    : Expressions.requireNonNull(spec.name, enriched.fieldName().get() + " cannot be null");
            return CodeBlocks.statement("this.$1L = $2L", spec.name, nullCheckedValue);
        }
    }

    private boolean isByteBuffer(Type type) {
        return type.accept(TypeVisitor.IS_BINARY) && !featureFlags.contains(FeatureFlags.UseImmutableBytes);
    }

    private List<MethodSpec> createAuxiliarySetters(EnrichedField enriched) {
        Type type = enriched.conjureDef().getType();

        if (type.accept(TypeVisitor.IS_LIST)) {
            return ImmutableList.of(
                    createCollectionSetter("addAll", enriched),
                    createItemSetter(enriched, type.accept(TypeVisitor.LIST).getItemType()));
        }

        if (type.accept(TypeVisitor.IS_SET)) {
            return ImmutableList.of(
                    createCollectionSetter("addAll", enriched),
                    createItemSetter(enriched, type.accept(TypeVisitor.SET).getItemType()));
        }

        if (type.accept(TypeVisitor.IS_MAP)) {
            return ImmutableList.of(
                    createCollectionSetter("putAll", enriched),
                    createMapSetter(enriched));
        }

        if (type.accept(TypeVisitor.IS_OPTIONAL)) {
            return ImmutableList.of(
                    createOptionalSetter(enriched));
        }

        return ImmutableList.of();
    }

    private MethodSpec createOptionalSetter(EnrichedField enriched) {
        FieldSpec field = enriched.poetSpec();
        OptionalType type = enriched.conjureDef().getType().accept(TypeVisitor.OPTIONAL);
        return publicSetter(enriched)
                .addParameter(typeMapper.getClassName(type.getItemType()), field.name)
                .addCode(optionalAssignmentStatement(enriched, type))
                .addStatement("return this")
                .build();
    }

    @SuppressWarnings("checkstyle:cyclomaticcomplexity")
    private CodeBlock optionalAssignmentStatement(EnrichedField enriched, OptionalType type) {
        FieldSpec spec = enriched.poetSpec();
        if (isPrimitiveOptional(type)) {
            return CodeBlocks.statement("this.$1N = $2T.of($1N)",
                    enriched.poetSpec().name, asRawType(typeMapper.getClassName(Type.optional(type))));
        } else {
            return CodeBlocks.statement("this.$1N = $2T.of($3L)",
                    spec.name, Optional.class,
                    Expressions.requireNonNull(spec.name, enriched.fieldName().get() + " cannot be null"));
        }
    }

    private static final EnumSet<PrimitiveType.Value> OPTIONAL_PRIMITIVES = EnumSet.of(
            PrimitiveType.Value.INTEGER, PrimitiveType.Value.DOUBLE, PrimitiveType.Value.BOOLEAN);

    /**
     * Check if the optionalType contains a primitive boolean, double or integer.
     */
    private boolean isPrimitiveOptional(OptionalType optionalType) {
        return optionalType.getItemType().accept(TypeVisitor.IS_PRIMITIVE)
                && OPTIONAL_PRIMITIVES.contains(optionalType.getItemType().accept(TypeVisitor.PRIMITIVE).get());
    }

    // we want to widen containers of anything that's not a primitive, a conjure reference or an optional
    // since we know all of those are final.
    private boolean isWidenableContainedType(Type containedType) {
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


    private MethodSpec createItemSetter(EnrichedField enriched, Type itemType) {
        FieldSpec field = enriched.poetSpec();
        return publicSetter(enriched)
                .addParameter(typeMapper.getClassName(itemType), field.name)
                .addStatement("this.$1N.add($1N)", field.name)
                .addStatement("return this")
                .build();
    }

    private MethodSpec createMapSetter(EnrichedField enriched) {
        MapType type = enriched.conjureDef().getType().accept(TypeVisitor.MAP);
        return publicSetter(enriched)
                .addParameter(typeMapper.getClassName(type.getKeyType()), "key")
                .addParameter(typeMapper.getClassName(type.getValueType()), "value")
                .addStatement("this.$1N.put(key, value)", enriched.poetSpec().name)
                .addStatement("return this")
                .build();
    }

    private MethodSpec.Builder publicSetter(EnrichedField enriched) {
        return MethodSpec.methodBuilder(enriched.poetSpec().name)
                .addJavadoc(enriched.conjureDef().getDocs().map(Javadoc::render).orElse(""))
                .addModifiers(Modifier.PUBLIC)
                .returns(builderClass);
    }

    private MethodSpec createBuild(Collection<EnrichedField> enrichedFields, Collection<FieldSpec> fields) {
        MethodSpec.Builder method = MethodSpec.methodBuilder("build")
                .addModifiers(Modifier.PUBLIC)
                .returns(objectClass);

        if (enrichedFields.stream().filter(EnrichedField::isPrimitive).count() != 0) {
            method.addStatement("validatePrimitiveFieldsHaveBeenInitialized()");
        }

        return method
                .addStatement("return new $L", Expressions.constructorCall(objectClass, fields))
                .build();
    }

    private static TypeName asRawType(TypeName type) {
        if (type instanceof ParameterizedTypeName) {
            return ((ParameterizedTypeName) type).rawType;
        }
        return type;
    }

    private static boolean isCollectionType(Type type) {
        return type.accept(TypeVisitor.IS_LIST)
                || type.accept(TypeVisitor.IS_SET)
                || type.accept(TypeVisitor.IS_MAP)
                || type.accept(TypeVisitor.IS_OPTIONAL);
    }
}
