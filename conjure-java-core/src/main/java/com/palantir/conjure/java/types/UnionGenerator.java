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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;
import com.palantir.conjure.java.ConjureAnnotations;
import com.palantir.conjure.java.Options;
import com.palantir.conjure.java.lib.internal.ConjureCollections;
import com.palantir.conjure.java.util.JavaNameSanitizer;
import com.palantir.conjure.java.util.Javadoc;
import com.palantir.conjure.java.util.Packages;
import com.palantir.conjure.java.util.Primitives;
import com.palantir.conjure.java.util.StableCollectors;
import com.palantir.conjure.java.util.TypeFunctions;
import com.palantir.conjure.java.visitor.DefaultableTypeVisitor;
import com.palantir.conjure.spec.FieldDefinition;
import com.palantir.conjure.spec.FieldName;
import com.palantir.conjure.spec.Type;
import com.palantir.conjure.spec.TypeDefinition;
import com.palantir.conjure.spec.UnionDefinition;
import com.palantir.conjure.visitor.TypeVisitor;
import com.palantir.javapoet.AnnotationSpec;
import com.palantir.javapoet.ArrayTypeName;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.FieldSpec;
import com.palantir.javapoet.JavaFile;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.ParameterSpec;
import com.palantir.javapoet.ParameterizedTypeName;
import com.palantir.javapoet.TypeName;
import com.palantir.javapoet.TypeSpec;
import com.palantir.javapoet.TypeVariableName;
import com.palantir.javapoet.WildcardTypeName;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.DoubleFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.lang.model.element.Modifier;
import org.apache.commons.lang3.StringUtils;

public final class UnionGenerator {

    private static final String VALUE_FIELD_NAME = "value";
    public static final String UNKNOWN_TYPE_PARAM_NAME = "unknownType";
    public static final String UNKNOWN_VALUE_PARAM_NAME = "unknownValue";
    private static final String UNKNOWN_WRAPPER_CLASS_NAME = "UnknownWrapper";
    private static final String VISIT_UNKNOWN_METHOD_NAME = "visitUnknown";
    private static final String COMPLETED = "completed_";
    private static final TypeVariableName TYPE_VARIABLE = TypeVariableName.get("T");
    private static final int MAX_VALUES_FOR_BUILDER = 100;

    // If the member type is not known, a String containing the name of the unknown type is used.
    private static final TypeName UNKNOWN_MEMBER_TYPE = ClassName.get(String.class);
    private static final TypeName UNKNOWN_VALUE_TYPE = ClassName.get(Object.class);

    private static final String SEALED_KNOWN_INTERFACE = "Known";
    private static final String SEALED_UNKNOWN_VARIANT_NAME = "Unknown";
    private static final String DESERIALIZER_CLASS_NAME = "Deserializer";
    private static final String SERIALIZER_CLASS_NAME = "Serializer";

    public static JavaFile generateUnionType(
            TypeMapper typeMapper,
            SafetyEvaluator safetyEvaluator,
            Map<com.palantir.conjure.spec.TypeName, TypeDefinition> typesMap,
            UnionDefinition typeDef,
            Options options) {
        com.palantir.conjure.spec.TypeName prefixedTypeName =
                Packages.getPrefixedName(typeDef.getTypeName(), options.packagePrefix());
        ClassName unionClass = ClassName.get(prefixedTypeName.getPackage(), prefixedTypeName.getName());
        ClassName visitorClass = unionClass.nestedClass("Visitor");
        Optional<ClassName> maybeVisitorBuilderClass = typeDef.getUnion().size() <= MAX_VALUES_FOR_BUILDER
                ? Optional.of(unionClass.nestedClass("VisitorBuilder"))
                : Optional.empty();

        Map<FieldDefinition, TypeName> memberTypes = typeDef.getUnion().stream()
                .collect(StableCollectors.toLinkedMap(
                        Function.identity(),
                        entry -> ConjureAnnotations.withSafety(
                                typeMapper.getClassName(entry.getType()), safetyEvaluator.getUsageTimeSafety(entry))));

        if (options.sealedUnions()) {
            ClassName unknownVariant = unionClass.nestedClass(SEALED_UNKNOWN_VARIANT_NAME);
            List<AnnotationSpec> safety =
                    ConjureAnnotations.safety(safetyEvaluator.evaluate(TypeDefinition.union(typeDef)));

            TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(
                            typeDef.getTypeName().getName())
                    .addAnnotations(safety)
                    .addAnnotation(ConjureAnnotations.getConjureGeneratedAnnotation(UnionGenerator.class))
                    .addAnnotation(generateJsonDeserialize(unionClass))
                    .addAnnotation(generateJsonSerialize(unionClass))
                    .addAnnotation(ignoreUnknownAnnotation())
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT, Modifier.SEALED)
                    .addPermittedSubclasses(typeDef.getUnion().stream()
                            .map(memberTypeDef -> sealedVariantClass(unionClass, memberTypeDef.getFieldName()))
                            .toList())
                    .addPermittedSubclass(unknownVariant)
                    .addTypes(generateSealedKnownInterface(unionClass, typeDef.getUnion()))
                    .addMethods(generateStaticFactories(
                            typeMapper, unionClass, typeDef.getUnion(), safetyEvaluator, options))
                    .addMethods(generateSealedThrowOnUnknown(unionClass, unknownVariant, typeDef.getUnion()))
                    .addTypes(generateWrapperClasses(
                            typeMapper, typesMap, unionClass, visitorClass, typeDef.getUnion(), options))
                    .addType(generateUnknownWrapper(unionClass, visitorClass, options))
                    .addType(generateSerializer(unionClass))
                    .addType(generateDeserializer(unionClass, typeDef.getUnion(), options));

            typeDef.getDocs().ifPresent(docs -> typeBuilder.addJavadoc("$L", Javadoc.render(docs)));

            if (options.sealedUnionVisitors()) {
                typeBuilder
                        .addMethod(generateAcceptVisitorMethodSignature(visitorClass))
                        .addType(generateVisitor(
                                unionClass, visitorClass, memberTypes, maybeVisitorBuilderClass, options));

                maybeVisitorBuilderClass.ifPresent(visitorBuilderClass -> typeBuilder
                        .addType(generateVisitorBuilder(
                                unionClass, visitorClass, visitorBuilderClass, memberTypes, options))
                        .addTypes(
                                generateVisitorBuilderStageInterfaces(unionClass, visitorClass, memberTypes, options)));
            }

            return JavaFile.builder(prefixedTypeName.getPackage(), typeBuilder.build())
                    .skipJavaLangImports(true)
                    .indent("    ")
                    .build();
        }

        ClassName baseClass = unionClass.nestedClass("Base");

        List<FieldSpec> fields =
                ImmutableList.of(FieldSpec.builder(baseClass, VALUE_FIELD_NAME, Modifier.PRIVATE, Modifier.FINAL)
                        .build());
        List<AnnotationSpec> safety =
                ConjureAnnotations.safety(safetyEvaluator.evaluate(TypeDefinition.union(typeDef)));

        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(
                        typeDef.getTypeName().getName())
                .addAnnotations(safety)
                .addAnnotation(ConjureAnnotations.getConjureGeneratedAnnotation(UnionGenerator.class))
                .addAnnotation(generateJsonDeserialize(unionClass))
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addFields(fields)
                .addMethod(generateConstructor(baseClass))
                .addMethod(generateGetValue(baseClass))
                .addMethods(
                        generateStaticFactories(typeMapper, unionClass, typeDef.getUnion(), safetyEvaluator, options))
                .addMethod(generateAcceptVisitMethod(visitorClass))
                .addType(generateVisitor(unionClass, visitorClass, memberTypes, maybeVisitorBuilderClass, options));

        maybeVisitorBuilderClass.ifPresent(visitorBuilderClass -> typeBuilder
                .addType(generateVisitorBuilder(unionClass, visitorClass, visitorBuilderClass, memberTypes, options))
                .addTypes(generateVisitorBuilderStageInterfaces(unionClass, visitorClass, memberTypes, options)));

        typeBuilder
                .addType(generateBase(baseClass, visitorClass, memberTypes))
                .addTypes(generateWrapperClasses(
                        typeMapper, typesMap, baseClass, visitorClass, typeDef.getUnion(), options))
                .addType(generateUnknownWrapper(baseClass, visitorClass, options))
                .addType(generateDeserializer(unionClass, typeDef.getUnion(), options))
                .addMethod(MethodSpecs.createEquals(unionClass))
                .addMethod(MethodSpecs.createEqualTo(unionClass, fields))
                .addMethod(MethodSpecs.createHashCode(fields))
                .addMethod(MethodSpecs.createToString(
                                unionClass.simpleName(),
                                fields.stream()
                                        .map(fieldSpec -> FieldName.of(fieldSpec.name()))
                                        .collect(Collectors.toList()))
                        .toBuilder()
                        .addAnnotations(safety)
                        .build());

        typeDef.getDocs().ifPresent(docs -> typeBuilder.addJavadoc("$L", Javadoc.render(docs)));

        return JavaFile.builder(prefixedTypeName.getPackage(), typeBuilder.build())
                .skipJavaLangImports(true)
                .indent("    ")
                .build();
    }

    /** Creates a backward compatible toString method. While in general, Conjure-Java does not provide any guarantees on
     * the toString representation of generated objects, the toString method is currently used when serializing error
     * parameters when JSON is not explicitly used. This is clunky because the sealedUnions implementation does not
     * generate any classes suffixed with `Wrapper`, yet such classes show up in the `toString` implementation. In a
     * future Conjure-Java change, we can update the toString method here to better represent the variant in the
     * sealedUnions implementation.
     */
    private static MethodSpec createLegacyToStringForSealedUnions(ClassName baseClassName, ClassName wrapperClassName) {
        return MethodSpec.methodBuilder("toString")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(ClassName.get(String.class))
                .addCode(CodeBlock.builder()
                        .add(
                                "return $S\n",
                                baseClassName.simpleName() + '{' + VALUE_FIELD_NAME + ": "
                                        + wrapperClassName.simpleName() + '{' + VALUE_FIELD_NAME + ": ")
                        .add(" + $N", VALUE_FIELD_NAME)
                        .add(" + \"}}\";")
                        .build())
                .build();
    }

    private static AnnotationSpec generateJsonDeserialize(ClassName unionClass) {
        return AnnotationSpec.builder(JsonDeserialize.class)
                .addMember("using", "$T.class", unionClass.nestedClass(DESERIALIZER_CLASS_NAME))
                .build();
    }

    private static AnnotationSpec generateJsonSerialize(ClassName unionClass) {
        return AnnotationSpec.builder(JsonSerialize.class)
                .addMember("using", "$T.class", unionClass.nestedClass(SERIALIZER_CLASS_NAME))
                .build();
    }

    private static AnnotationSpec ignoreUnknownAnnotation() {
        return AnnotationSpec.builder(JsonIgnoreProperties.class)
                .addMember("ignoreUnknown", "$L", true)
                .build();
    }

    private static List<TypeSpec> generateSealedKnownInterface(
            ClassName unionClass, List<FieldDefinition> memberTypeDefs) {
        if (memberTypeDefs.isEmpty()) {
            return List.of();
        }

        return List.of(TypeSpec.interfaceBuilder(SEALED_KNOWN_INTERFACE)
                .addModifiers(Modifier.PUBLIC, Modifier.SEALED)
                .addPermittedSubclasses(memberTypeDefs.stream()
                        .map(FieldDefinition::getFieldName)
                        .map(fieldName -> sealedVariantClass(unionClass, fieldName))
                        .toList())
                .build());
    }

    private static List<MethodSpec> generateSealedThrowOnUnknown(
            ClassName unionClass, ClassName unknownVariantClass, List<FieldDefinition> memberTypeDefs) {
        if (memberTypeDefs.isEmpty()) {
            return List.of();
        }

        ClassName knownInterface = unionClass.nestedClass(SEALED_KNOWN_INTERFACE);
        return List.of(MethodSpec.methodBuilder("throwOnUnknown")
                .addModifiers(Modifier.PUBLIC)
                .returns(knownInterface)
                .beginControlFlow("if (this instanceof $T)", unknownVariantClass)
                .addStatement(
                        "throw new $T($S, $T.of($S, (($T) this).type()))",
                        SafeIllegalArgumentException.class,
                        "Unknown variant of the '" + unionClass.simpleName() + "' union",
                        SafeArg.class,
                        "unknownType",
                        unknownVariantClass)
                .nextControlFlow("else")
                .addStatement("return ($T) this", knownInterface)
                .endControlFlow()
                .build());
    }

    private static MethodSpec generateConstructor(ClassName baseClass) {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .addAnnotation(ConjureAnnotations.delegatingJsonCreator())
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
            TypeMapper typeMapper,
            ClassName unionClass,
            List<FieldDefinition> memberTypeDefs,
            SafetyEvaluator safetyEvaluator,
            Options options) {
        List<MethodSpec> staticFactories = memberTypeDefs.stream()
                .map(memberTypeDef -> {
                    FieldName memberName = sanitizeUnknown(memberTypeDef.getFieldName());
                    TypeName memberType = ConjureAnnotations.withSafety(
                            typeMapper.getClassName(memberTypeDef.getType()),
                            safetyEvaluator.getUsageTimeSafety(memberTypeDef));
                    String variableName = variableName();
                    // memberName is guarded to be a valid Java identifier and not to end in an underscore, so this is
                    // safe
                    MethodSpec.Builder builder = MethodSpec.methodBuilder(JavaNameSanitizer.sanitize(memberName))
                            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                            .addParameter(ParameterSpec.builder(memberType, variableName)
                                    .build())
                            .addStatement(
                                    options.sealedUnions()
                                            ? CodeBlock.of(
                                                    "return new $T($L)",
                                                    sealedVariantClass(unionClass, memberName),
                                                    variableName)
                                            : CodeBlock.of(
                                                    "return new $T(new $T($L))",
                                                    unionClass,
                                                    wrapperClass(unionClass, memberName),
                                                    variableName))
                            .returns(unionClass);
                    Javadoc.render(memberTypeDef.getDocs(), memberTypeDef.getDeprecated())
                            .ifPresent(javadoc -> builder.addJavadoc("$L", javadoc));
                    memberTypeDef.getDeprecated().ifPresent(_deprecated -> builder.addAnnotation(Deprecated.class));
                    return builder.build();
                })
                .collect(Collectors.toList());
        staticFactories.add(generateUnknownStaticFactory(unionClass, memberTypeDefs, options));
        return staticFactories;
    }

    private static MethodSpec generateUnknownStaticFactory(
            ClassName unionClass, List<FieldDefinition> memberTypeDefs, Options options) {
        String typeParam = "type";
        String valueParam = "value";
        MethodSpec.Builder builder = MethodSpec.methodBuilder("unknown")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(ParameterSpec.builder(String.class, typeParam)
                        .addAnnotation(Safe.class)
                        .build())
                .addParameter(ParameterSpec.builder(Object.class, valueParam)
                        .addAnnotations(ConjureAnnotations.safety(SafetyEvaluator.UNKNOWN_UNION_VARINT_SAFETY))
                        .build())
                .returns(unionClass);
        // begin switch statement
        builder.beginControlFlow("switch($L)", Expressions.requireNonNull(typeParam, "Type is required"));
        // add all cases
        memberTypeDefs.forEach(memberTypeDef -> {
            String memberName = memberTypeDef.getFieldName().get();
            builder.addCode("case $S:", memberName);
            builder.addStatement(
                    "throw new $T($S)",
                    SafeIllegalArgumentException.class,
                    "Unknown type cannot be created as the provided type is known: " + memberName);
        });
        // add default case, which actually builds the unknown
        builder.addCode("default:");
        CodeBlock singletonMap = CodeBlock.of("$T.singletonMap($N, $N)", Collections.class, typeParam, valueParam);
        builder.addStatement(
                options.sealedUnions()
                        ? CodeBlock.of(
                                "return new $T($N, $L)",
                                unionClass.nestedClass(SEALED_UNKNOWN_VARIANT_NAME),
                                typeParam,
                                singletonMap)
                        : CodeBlock.of(
                                "return new $T(new $T($N, $L))",
                                unionClass,
                                wrapperClass(unionClass, FieldName.of("unknown")),
                                typeParam,
                                singletonMap));
        builder.endControlFlow();
        return builder.build();
    }

    private static MethodSpec generateAcceptVisitMethod(ClassName visitorClass) {
        ParameterizedTypeName parameterizedVisitorClass = ParameterizedTypeName.get(visitorClass, TYPE_VARIABLE);
        ParameterSpec visitor =
                ParameterSpec.builder(parameterizedVisitorClass, "visitor").build();
        return MethodSpec.methodBuilder("accept")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(visitor)
                .addTypeVariable(TYPE_VARIABLE)
                .addStatement("return $L.accept($N)", VALUE_FIELD_NAME, visitor)
                .returns(TYPE_VARIABLE)
                .build();
    }

    private static MethodSpec generateAcceptVisitorMethodSignature(ClassName visitorClass) {
        ParameterizedTypeName parameterizedVisitorClass = ParameterizedTypeName.get(visitorClass, TYPE_VARIABLE);
        ParameterSpec visitor =
                ParameterSpec.builder(parameterizedVisitorClass, "visitor").build();
        return MethodSpec.methodBuilder("accept")
                .addParameter(visitor)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addTypeVariable(TYPE_VARIABLE)
                .returns(TYPE_VARIABLE)
                .build();
    }

    private static TypeSpec generateVisitor(
            ClassName unionClass,
            ClassName visitorClass,
            Map<FieldDefinition, TypeName> memberTypes,
            Optional<ClassName> maybeVisitorBuilderClass,
            Options options) {
        TypeSpec.Builder visitorBuilder = TypeSpec.interfaceBuilder(visitorClass)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addTypeVariable(TYPE_VARIABLE)
                .addMethods(generateMemberVisitMethods(memberTypes));
        MethodSpec.Builder visitUnknownBuilder = MethodSpec.methodBuilder(VISIT_UNKNOWN_METHOD_NAME)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(ParameterSpec.builder(UNKNOWN_MEMBER_TYPE, UNKNOWN_TYPE_PARAM_NAME)
                        .addAnnotation(Safe.class)
                        .build())
                .returns(TYPE_VARIABLE);
        if (options.unionsWithUnknownValues()) {
            visitUnknownBuilder.addParameter(ParameterSpec.builder(UNKNOWN_VALUE_TYPE, UNKNOWN_VALUE_PARAM_NAME)
                    .addAnnotations(ConjureAnnotations.safety(SafetyEvaluator.UNKNOWN_UNION_VARINT_SAFETY))
                    .build());
        }
        visitorBuilder.addMethod(visitUnknownBuilder.build());
        maybeVisitorBuilderClass.ifPresent(visitorBuilderClass -> visitorBuilder
                .addMethod(MethodSpec.methodBuilder("builder")
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .addTypeVariable(TYPE_VARIABLE)
                        .addStatement("return new $T<$T>()", visitorBuilderClass, TYPE_VARIABLE)
                        .returns(ParameterizedTypeName.get(
                                visitorStageInterfaceName(
                                        unionClass,
                                        sortedStageNameTypePairs(memberTypes)
                                                .findFirst()
                                                .get()
                                                .memberName),
                                TYPE_VARIABLE))
                        .build())
                .build());
        return visitorBuilder.build();
    }

    private static List<MethodSpec> generateMemberVisitMethods(Map<FieldDefinition, TypeName> memberTypes) {
        return memberTypes.entrySet().stream()
                .map(entry -> {
                    String variableName = variableName();
                    return MethodSpec.methodBuilder(visitMethodName(sanitizeUnknown(
                                    entry.getKey().getFieldName().get())))
                            .addJavadoc(Javadoc.render(
                                            entry.getKey().getDocs(),
                                            entry.getKey().getDeprecated())
                                    .map(rendered -> CodeBlock.of("$L", rendered))
                                    .orElseGet(() -> CodeBlock.builder().build()))
                            .addAnnotations(ConjureAnnotations.deprecation(
                                    entry.getKey().getDeprecated()))
                            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                            .addParameter(ParameterSpec.builder(entry.getValue(), variableName)
                                    .build())
                            .returns(TYPE_VARIABLE)
                            .build();
                })
                .collect(Collectors.toList());
    }

    /** Generates a builder class for the given {@code visitor} class. */
    private static TypeSpec generateVisitorBuilder(
            ClassName enclosingClass,
            ClassName visitor,
            ClassName visitorBuilder,
            Map<FieldDefinition, TypeName> memberTypeMap,
            Options options) {
        TypeVariableName visitResultType = TypeVariableName.get("T");
        return TypeSpec.classBuilder(visitorBuilder)
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .addTypeVariable(visitResultType)
                .addSuperinterfaces(allVisitorBuilderStages(enclosingClass, memberTypeMap, visitResultType))
                .addFields(allVisitorBuilderFields(memberTypeMap, visitResultType, options))
                .addMethods(allVisitorBuilderSetters(enclosingClass, visitResultType, memberTypeMap, options))
                .addMethod(builderBuildMethod(visitor, visitResultType, memberTypeMap, options))
                .build();
    }

    /**
     * Generates setter methods for the visitor builder on the following format.
     *
     * <pre>
     * NextMemberStage&lt;T&gt; memberName(Function&lt;MemberType, T&gt; memberVisitor) {
     *     this.memberVisitor = memberVisitor;
     *     return this;
     * }
     * </pre>
     */
    private static List<MethodSpec> allVisitorBuilderSetters(
            ClassName enclosingClass,
            TypeName visitResultType,
            Map<FieldDefinition, TypeName> memberTypeMap,
            Options options) {
        ImmutableList.Builder<MethodSpec> setterMethods = ImmutableList.builder();
        Stream<NameTypeMetadata> memberTypes = sortedStageNameTypePairs(memberTypeMap);
        PeekingIterator<NameTypeMetadata> memberIter = Iterators.peekingIterator(memberTypes.iterator());
        while (memberIter.hasNext()) {
            NameTypeMetadata pair = memberIter.next();
            String nextBuilderStage = memberIter.hasNext() ? memberIter.peek().memberName : COMPLETED;
            ClassName nextVisitorStageClassName = visitorStageInterfaceName(enclosingClass, nextBuilderStage);
            MethodSpec.Builder setterPrototype =
                    visitorBuilderSetterPrototype(pair, visitResultType, nextVisitorStageClassName, options);
            setterMethods.add(setterPrototype
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .addStatement(
                            "$L",
                            Expressions.requireNonNull(
                                    visitorFieldName(pair.memberName),
                                    String.format("%s cannot be null", visitorFieldName(pair.memberName))))
                    .addStatement("this.$1L = $1L", visitorFieldName(pair.memberName))
                    .addStatement("return this")
                    .build());
            if (NameTypeMetadata.UNKNOWN.equals(pair)) {
                setterMethods.addAll(unknownSpecificVisitorSetters(
                        enclosingClass, visitResultType, nextVisitorStageClassName, options));
            }
        }
        return setterMethods.build();
    }

    private static List<MethodSpec> unknownSpecificVisitorSetters(
            ClassName enclosingClass, TypeName visitResultType, ClassName nextVisitorStageClassName, Options options) {
        ImmutableList.Builder<MethodSpec> methods = ImmutableList.builder();
        String lambdaParams = options.unionsWithUnknownValues()
                ? String.format("(%s, _%s)", UNKNOWN_TYPE_PARAM_NAME, UNKNOWN_VALUE_PARAM_NAME)
                : UNKNOWN_TYPE_PARAM_NAME;

        if (options.unionsWithUnknownValues()) {
            // Allow providing the old unknown visitor
            String visitorName = visitorFieldName("unknown");
            methods.add(MethodSpec.methodBuilder("unknown")
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .addParameter(ParameterSpec.builder(
                                    ParameterizedTypeName.get(
                                            ClassName.get(Function.class),
                                            UNKNOWN_MEMBER_TYPE.annotated(AnnotationSpec.builder(Safe.class)
                                                    .build()),
                                            visitResultType),
                                    visitorName)
                            .addAnnotation(Nonnull.class)
                            .build())
                    .returns(ParameterizedTypeName.get(nextVisitorStageClassName, visitResultType))
                    .addStatement(
                            "$L",
                            Expressions.requireNonNull(visitorName, String.format("%s cannot be null", visitorName)))
                    .addStatement(
                            "this.$1N = $2L -> $1N.apply($3N)", visitorName, lambdaParams, UNKNOWN_TYPE_PARAM_NAME)
                    .addStatement("return this")
                    .build());
        }

        // Throw on unknown
        methods.add(visitorBuilderUnknownThrowPrototype(visitResultType, nextVisitorStageClassName)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addStatement(
                        "this.$N = $L -> { throw new $T($S, $T.of($S, $N)); }",
                        visitorFieldName("unknown"),
                        lambdaParams,
                        SafeIllegalArgumentException.class,
                        "Unknown variant of the '" + enclosingClass.simpleName() + "' union",
                        SafeArg.class,
                        UNKNOWN_TYPE_PARAM_NAME,
                        UNKNOWN_TYPE_PARAM_NAME)
                .addStatement("return this")
                .build());

        return methods.build();
    }

    /**
     * Generates the build method for the visitor builder. The result looks as follows:
     *
     * <pre>
     * Visitor&lt;T&gt; build() {
     *     return new Visitor&lt;T&gt;() {
     *         [methods delegating to the various visitor function objects]
     *     }
     * }
     * </pre>
     */
    private static MethodSpec builderBuildMethod(
            ClassName visitorClass,
            TypeName visitResultType,
            Map<FieldDefinition, TypeName> memberTypeMap,
            Options options) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("build")
                .returns(ParameterizedTypeName.get(visitorClass, visitResultType))
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class);

        // Add statements to copy over visitor handlers to local immutable variables.
        sortedStageNameTypePairs(memberTypeMap)
                .forEach(nameType -> builder.addStatement(
                        "final $1T $2L = this.$2L",
                        visitorObjectTypeName(nameType, visitResultType, options),
                        visitorFieldName(nameType.memberName)));

        return builder.addStatement(
                        "return $L",
                        TypeSpec.anonymousClassBuilder("")
                                .addSuperinterface(ParameterizedTypeName.get(visitorClass, visitResultType))
                                .addMethods(allDelegatingVisitorMethods(memberTypeMap, visitResultType, options))
                                .build())
                .build();
    }

    /**
     * Generate the implementations of the visitor built by the visitor builder. Each method has the shape
     *
     * <pre>
     * T visitMemberName(MemberType value) {
     *     return memberVisitor.apply(value);
     * }
     * </pre>
     */
    private static List<MethodSpec> allDelegatingVisitorMethods(
            Map<FieldDefinition, TypeName> memberTypeMap, TypeName visitorResultType, Options options) {
        return sortedStageNameTypePairs(memberTypeMap)
                .map(pair -> NameTypeMetadata.UNKNOWN.equals(pair) && options.unionsWithUnknownValues()
                        ? MethodSpec.methodBuilder(visitMethodName(pair.memberName))
                                .addModifiers(Modifier.PUBLIC)
                                .addAnnotation(Override.class)
                                .addParameter(UNKNOWN_MEMBER_TYPE, UNKNOWN_TYPE_PARAM_NAME)
                                .addParameter(UNKNOWN_VALUE_TYPE, UNKNOWN_VALUE_PARAM_NAME)
                                .addStatement(
                                        "return $N.apply($N, $N)",
                                        visitorFieldName(pair.memberName),
                                        UNKNOWN_TYPE_PARAM_NAME,
                                        UNKNOWN_VALUE_PARAM_NAME)
                                .returns(visitorResultType)
                                .build()
                        : MethodSpec.methodBuilder(visitMethodName(pair.memberName))
                                .addModifiers(Modifier.PUBLIC)
                                .addAnnotation(Override.class)
                                .addParameter(pair.type, variableName())
                                .addStatement("return $N.apply($N)", visitorFieldName(pair.memberName), variableName())
                                .returns(visitorResultType)
                                .build())
                .collect(Collectors.toList());
    }

    /** Generates all the interface type names for the different visitor builder stages. */
    private static List<TypeName> allVisitorBuilderStages(
            ClassName enclosingClass, Map<FieldDefinition, TypeName> memberTypeMap, TypeVariableName visitResultType) {
        return Stream.concat(sortedStageNameTypePairs(memberTypeMap).map(p -> p.memberName), Stream.of(COMPLETED))
                .map(stageName -> visitorStageInterfaceName(enclosingClass, stageName))
                .map(stageType -> ParameterizedTypeName.get(stageType, visitResultType))
                .collect(Collectors.toList());
    }

    /**
     * Generate all fields of the Visitor builder, on the following format.
     *
     * <pre>
     * Function&lt;MemberType1, T&gt; member1Visitor;
     * Function&lt;MemberType2, T&gt; member2Visitor;
     * Function&lt;MemberType3, T&gt; member3Visitor;
     * Function&lt;UNKNOWN_MEMBER_TYPE, T&gt; unknownVisitor;
     * </pre>
     */
    private static List<FieldSpec> allVisitorBuilderFields(
            Map<FieldDefinition, TypeName> memberTypeMap, TypeVariableName visitResultType, Options options) {
        return sortedStageNameTypePairs(memberTypeMap)
                .map(field -> FieldSpec.builder(
                                visitorObjectTypeName(field, visitResultType, options),
                                visitorFieldName(field.memberName),
                                Modifier.PRIVATE)
                        .build())
                .collect(Collectors.toList());
    }

    /** Generates the name of the interface of a visitor builder stage. */
    private static ClassName visitorStageInterfaceName(ClassName enclosingClass, String stageName) {
        return enclosingClass.nestedClass(StringUtils.capitalize(stageName) + "StageVisitorBuilder");
    }

    /**
     * Convenience method for generating a {@code Function<MemberType, T>} used for fields and setters of the
     * visitor builder. Special-cases generation of a {@code BiFunction<String, Map<String, Object>, T>} in the case
     * of the new unknownVisitor.
     */
    private static TypeName visitorObjectTypeName(NameTypeMetadata member, TypeName visitResultType, Options options) {
        if (member.type.withoutAnnotations().equals(TypeName.INT)) {
            return ParameterizedTypeName.get(ClassName.get(IntFunction.class), visitResultType);
        } else if (member.type.withoutAnnotations().equals(TypeName.DOUBLE)) {
            return ParameterizedTypeName.get(ClassName.get(DoubleFunction.class), visitResultType);
        } else if (NameTypeMetadata.UNKNOWN.equals(member) && options.unionsWithUnknownValues()) {
            return ParameterizedTypeName.get(
                    ClassName.get(BiFunction.class),
                    UNKNOWN_MEMBER_TYPE.annotated(
                            AnnotationSpec.builder(Safe.class).build()),
                    UNKNOWN_VALUE_TYPE.annotated(
                            ConjureAnnotations.safety(SafetyEvaluator.UNKNOWN_UNION_VARINT_SAFETY)),
                    visitResultType);
        } else {
            return ParameterizedTypeName.get(
                    ClassName.get(Function.class), Primitives.box(member.type), visitResultType);
        }
    }

    /**
     * Generate all interfaces for the various visitor builder stages on the following pattern.
     *
     * <pre>
     * interface Member1Stage&lt;T&gt; { Member2Stage&lt;T&gt; member1(...) }
     * interface Member2Stage&lt;T&gt; { Member3Stage&lt;T&gt; member2(...) }
     * interface Member3Stage&lt;T&gt; { UnknownStage&lt;T&gt; member3(...) }
     * interface UnknownStage&lt;T&gt; { CompletedStage&lt;T&gt; unknown(...) }
     * interface CompletedStage&lt;T&gt; { Visitor&lt;T&gt; build(...) }
     * </pre>
     */
    private static List<TypeSpec> generateVisitorBuilderStageInterfaces(
            ClassName enclosingClass,
            ClassName visitorClass,
            Map<FieldDefinition, TypeName> memberTypes,
            Options options) {
        TypeVariableName visitResultType = TypeVariableName.get("T");
        List<TypeSpec> interfaces = new ArrayList<>();
        PeekingIterator<NameTypeMetadata> memberIter =
                Iterators.peekingIterator(sortedStageNameTypePairs(memberTypes).iterator());
        while (memberIter.hasNext()) {
            NameTypeMetadata member = memberIter.next();
            String nextBuilderStageName = memberIter.hasNext() ? memberIter.peek().memberName : COMPLETED;
            ClassName nextStageClassName = visitorStageInterfaceName(enclosingClass, nextBuilderStageName);
            interfaces.add(TypeSpec.interfaceBuilder(visitorStageInterfaceName(enclosingClass, member.memberName))
                    .addTypeVariable(visitResultType)
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .addMethod(visitorBuilderSetterPrototype(member, visitResultType, nextStageClassName, options)
                            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                            .build())
                    .addMethods(
                            NameTypeMetadata.UNKNOWN.equals(member)
                                    ? unknownSpecificVisitorPrototypes(visitResultType, nextStageClassName, options)
                                    : ImmutableList.of())
                    .build());
        }
        interfaces.add(TypeSpec.interfaceBuilder(visitorStageInterfaceName(enclosingClass, COMPLETED))
                .addTypeVariable(visitResultType)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addMethod(MethodSpec.methodBuilder("build")
                        .returns(ParameterizedTypeName.get(visitorClass, visitResultType))
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .build())
                .build());
        return interfaces;
    }

    private static ImmutableList<MethodSpec> unknownSpecificVisitorPrototypes(
            TypeVariableName visitResultType, ClassName nextStageClassName, Options options) {
        ImmutableList.Builder<MethodSpec> methods = ImmutableList.builder();

        if (options.unionsWithUnknownValues()) {
            // Allow providing the old unknown visitor
            methods.add(MethodSpec.methodBuilder("unknown")
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .addParameter(ParameterSpec.builder(
                                    ParameterizedTypeName.get(
                                            ClassName.get(Function.class),
                                            UNKNOWN_MEMBER_TYPE.annotated(AnnotationSpec.builder(Safe.class)
                                                    .build()),
                                            visitResultType),
                                    visitorFieldName("unknown"))
                            .addAnnotation(Nonnull.class)
                            .build())
                    .returns(ParameterizedTypeName.get(nextStageClassName, visitResultType))
                    .build());
        }

        // Throw on unknown
        methods.add(visitorBuilderUnknownThrowPrototype(visitResultType, nextStageClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .build());

        return methods.build();
    }

    private static Stream<NameTypeMetadata> sortedStageNameTypePairs(Map<FieldDefinition, TypeName> memberTypes) {
        return Stream.concat(
                memberTypes.entrySet().stream()
                        .map(entry -> new NameTypeMetadata(
                                sanitizeUnknown(entry.getKey().getFieldName().get()), entry.getValue()))
                        .sorted(Comparator.comparing(p -> p.memberName)),
                Stream.of(NameTypeMetadata.UNKNOWN));
    }

    private static TypeSpec generateDeserializer(
            ClassName unionClass, List<FieldDefinition> memberTypeDefs, Options options) {
        ClassName deserializerClass = unionClass.nestedClass(DESERIALIZER_CLASS_NAME);
        TypeSpec.Builder builder = TypeSpec.classBuilder(deserializerClass)
                .addModifiers(Modifier.STATIC, Modifier.FINAL)
                .superclass(ParameterizedTypeName.get(ClassName.get(JsonDeserializer.class), unionClass))
                .addSuperinterface(ResolvableDeserializer.class)
                .addField(generateVariantTypesField(unionClass, memberTypeDefs, options))
                .addField(generateDeserializersField())
                .addMethod(generateResolveMethod())
                .addMethod(generateIsCachableMethod())
                .addMethod(generateDeserializeMethod(unionClass))
                .addMethod(generateDeserializeBufferedMethod(unionClass))
                .addMethod(generateIsTypeFieldMethod())
                .addMethod(generateDeserializeSelectedMethod(unionClass, memberTypeDefs, options))
                .addMethod(generateResolveDeserializerMethod())
                .addMethod(generateDeserializeUnknownMethod(unionClass, options));
        return builder.build();
    }

    private static FieldSpec generateVariantTypesField(
            ClassName unionClass, List<FieldDefinition> memberTypeDefs, Options options) {
        TypeName classType =
                ParameterizedTypeName.get(ClassName.get(Class.class), WildcardTypeName.subtypeOf(Object.class));
        CodeBlock.Builder initializer = CodeBlock.builder().add("new $T<?>[] {$>", Class.class);
        for (int index = 0; index < memberTypeDefs.size(); index++) {
            FieldDefinition memberTypeDef = memberTypeDefs.get(index);
            ClassName wrapperClass = options.sealedUnions()
                    ? sealedVariantClass(unionClass, memberTypeDef.getFieldName())
                    : wrapperClass(unionClass, sanitizeUnknown(memberTypeDef.getFieldName()));
            if (index > 0) {
                initializer.add(",");
            }
            initializer.add("\n$T.class", wrapperClass);
        }
        initializer.add("$<\n}");
        return FieldSpec.builder(
                        ArrayTypeName.of(classType), "VARIANT_TYPES", Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer("$L", initializer.build())
                .build();
    }

    private static FieldSpec generateDeserializersField() {
        TypeName deserializerType = ParameterizedTypeName.get(
                ClassName.get(JsonDeserializer.class), WildcardTypeName.subtypeOf(Object.class));
        return FieldSpec.builder(
                        ArrayTypeName.of(deserializerType), "deserializers", Modifier.PRIVATE, Modifier.VOLATILE)
                .build();
    }

    private static MethodSpec generateResolveMethod() {
        return MethodSpec.methodBuilder("resolve")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(DeserializationContext.class, "context")
                .addException(JsonMappingException.class)
                .addStatement("deserializers = new $T<?>[VARIANT_TYPES.length]", JsonDeserializer.class)
                .build();
    }

    private static MethodSpec generateIsCachableMethod() {
        return MethodSpec.methodBuilder("isCachable")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.BOOLEAN)
                .addStatement("return true")
                .build();
    }

    private static TypeSpec generateSerializer(ClassName unionClass) {
        return TypeSpec.classBuilder(unionClass.nestedClass(SERIALIZER_CLASS_NAME))
                .addModifiers(Modifier.STATIC, Modifier.FINAL)
                .superclass(ParameterizedTypeName.get(ClassName.get(JsonSerializer.class), unionClass))
                .addMethod(MethodSpec.methodBuilder("serialize")
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(unionClass, "value")
                        .addParameter(JsonGenerator.class, "generator")
                        .addParameter(SerializerProvider.class, "serializers")
                        .addException(IOException.class)
                        .addStatement("serializers.findValueSerializer(value.getClass()).serialize(value, generator,"
                                + " serializers)")
                        .build())
                .build();
    }

    private static MethodSpec generateDeserializeMethod(ClassName unionClass) {
        return MethodSpec.methodBuilder("deserialize")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(unionClass)
                .addParameter(JsonParser.class, "parser")
                .addParameter(DeserializationContext.class, "context")
                .addException(IOException.class)
                .beginControlFlow("if (!parser.isExpectedStartObjectToken())")
                .addStatement(
                        "return context.reportInputMismatch($T.class, $S)",
                        unionClass,
                        "Expected a JSON object for union deserialization")
                .endControlFlow()
                .addStatement("$T firstToken = parser.nextToken()", JsonToken.class)
                .beginControlFlow(
                        "if (firstToken == $T.FIELD_NAME && isTypeField(parser.currentName(), context))",
                        JsonToken.class)
                .beginControlFlow("if (parser.nextToken() != $T.VALUE_STRING)", JsonToken.class)
                .addStatement(
                        "return context.reportInputMismatch($T.class, $S)",
                        unionClass,
                        "Union discriminator 'type' must be a string")
                .endControlFlow()
                .addStatement("$T type = parser.getText()", String.class)
                .addStatement("parser.nextToken()")
                .addStatement("return deserializeSelected(parser, context, type)")
                .endControlFlow()
                .addStatement("return deserializeBuffered(parser, context)")
                .build();
    }

    private static MethodSpec generateDeserializeBufferedMethod(ClassName unionClass) {
        return MethodSpec.methodBuilder("deserializeBuffered")
                .addModifiers(Modifier.PRIVATE)
                .returns(unionClass)
                .addParameter(JsonParser.class, "parser")
                .addParameter(DeserializationContext.class, "context")
                .addException(IOException.class)
                .beginControlFlow("try ($T buffer = context.bufferForInputBuffering(parser))", TokenBuffer.class)
                .addStatement("buffer.writeStartObject()")
                .addStatement("$T token = parser.currentToken()", JsonToken.class)
                .beginControlFlow("while (token == $T.FIELD_NAME)", JsonToken.class)
                .addStatement("$T fieldName = parser.currentName()", String.class)
                .addStatement("$T valueToken = parser.nextToken()", JsonToken.class)
                .beginControlFlow("if (isTypeField(fieldName, context))")
                .beginControlFlow("if (valueToken != $T.VALUE_STRING)", JsonToken.class)
                .addStatement(
                        "return context.reportInputMismatch($T.class, $S)",
                        unionClass,
                        "Union discriminator 'type' must be a string")
                .endControlFlow()
                .addStatement("$T type = parser.getText()", String.class)
                .addStatement("parser.nextToken()")
                .beginControlFlow("try ($T bufferedParser = buffer.asParser(parser))", JsonParser.class)
                .addStatement(
                        "$T combinedParser = $T.createFlattened(true, bufferedParser, parser)",
                        JsonParser.class,
                        JsonParserSequence.class)
                .addStatement("combinedParser.nextToken()")
                .addStatement("return deserializeSelected(combinedParser, context, type)")
                .endControlFlow()
                .endControlFlow()
                .addStatement("buffer.writeFieldName(fieldName)")
                .addStatement("buffer.copyCurrentStructure(parser)")
                .addStatement("token = parser.nextToken()")
                .endControlFlow()
                .beginControlFlow("if (token != $T.END_OBJECT)", JsonToken.class)
                .addStatement(
                        "return context.reportInputMismatch($T.class, $S)",
                        unionClass,
                        "Expected the end of a JSON object while deserializing a union")
                .endControlFlow()
                .endControlFlow()
                .addStatement(
                        "return context.reportInputMismatch($T.class, $S)",
                        unionClass,
                        "Union discriminator 'type' is required")
                .build();
    }

    private static MethodSpec generateIsTypeFieldMethod() {
        return MethodSpec.methodBuilder("isTypeField")
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .returns(TypeName.BOOLEAN)
                .addParameter(String.class, "fieldName")
                .addParameter(DeserializationContext.class, "context")
                .addStatement(
                        "return $S.equals(fieldName) || (context.isEnabled($T.ACCEPT_CASE_INSENSITIVE_PROPERTIES)"
                                + " && $S.equalsIgnoreCase(fieldName))",
                        "type",
                        MapperFeature.class,
                        "type")
                .build();
    }

    private static MethodSpec generateDeserializeSelectedMethod(
            ClassName unionClass, List<FieldDefinition> memberTypeDefs, Options options) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("deserializeSelected")
                .addModifiers(Modifier.PRIVATE)
                .returns(unionClass)
                .addParameter(JsonParser.class, "parser")
                .addParameter(DeserializationContext.class, "context")
                .addParameter(String.class, "type")
                .addException(IOException.class)
                .addCode("int variantIndex = switch (type) {\n");
        for (int index = 0; index < memberTypeDefs.size(); index++) {
            builder.addStatement("case $S -> $L", memberTypeDefs.get(index).getFieldName(), index);
        }
        builder.addStatement("default -> -1").addCode("};\n");
        builder.beginControlFlow("if (variantIndex < 0)");
        builder.addStatement("return deserializeUnknown(parser, context, type)");
        builder.endControlFlow();
        builder.addStatement("$T<?> deserializer = deserializers[variantIndex]", JsonDeserializer.class)
                .beginControlFlow("if (deserializer == null)")
                .addStatement("deserializer = resolveDeserializer(context, variantIndex)")
                .endControlFlow();
        if (options.sealedUnions()) {
            builder.addStatement("return ($T) deserializer.deserialize(parser, context)", unionClass);
        } else {
            builder.addStatement(
                    "return new $T(($T) deserializer.deserialize(parser, context))",
                    unionClass,
                    unionClass.nestedClass("Base"));
        }
        return builder.build();
    }

    private static MethodSpec generateResolveDeserializerMethod() {
        ParameterizedTypeName deserializerType = ParameterizedTypeName.get(
                ClassName.get(JsonDeserializer.class), WildcardTypeName.subtypeOf(Object.class));
        return MethodSpec.methodBuilder("resolveDeserializer")
                .addModifiers(Modifier.PRIVATE, Modifier.SYNCHRONIZED)
                .returns(deserializerType)
                .addParameter(DeserializationContext.class, "context")
                .addParameter(TypeName.INT, "variantIndex")
                .addException(JsonMappingException.class)
                .addStatement("$T deserializer = deserializers[variantIndex]", deserializerType)
                .beginControlFlow("if (deserializer == null)")
                .addStatement("deserializer = context.findRootValueDeserializer("
                        + "context.constructType(VARIANT_TYPES[variantIndex]))")
                .addStatement("$T[] updated = deserializers.clone()", deserializerType)
                .addStatement("updated[variantIndex] = deserializer")
                .addStatement("deserializers = updated")
                .endControlFlow()
                .addStatement("return deserializer")
                .build();
    }

    private static MethodSpec generateDeserializeUnknownMethod(ClassName unionClass, Options options) {
        ParameterizedTypeName valueMapType = ParameterizedTypeName.get(Map.class, String.class, Object.class);
        MethodSpec.Builder builder = MethodSpec.methodBuilder("deserializeUnknown")
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .returns(unionClass)
                .addParameter(JsonParser.class, "parser")
                .addParameter(DeserializationContext.class, "context")
                .addParameter(String.class, "type")
                .addException(IOException.class)
                .addStatement("$T values = new $T<>()", valueMapType, HashMap.class)
                .beginControlFlow("if (parser.currentToken() == $T.START_OBJECT)", JsonToken.class)
                .addStatement("parser.nextToken()")
                .endControlFlow()
                .beginControlFlow("while (parser.currentToken() == $T.FIELD_NAME)", JsonToken.class)
                .addStatement("$T fieldName = parser.currentName()", String.class)
                .addStatement("parser.nextToken()")
                .addStatement("values.put(fieldName, context.readValue(parser, $T.class))", Object.class)
                .addStatement("parser.nextToken()")
                .endControlFlow()
                .beginControlFlow("if (parser.currentToken() != $T.END_OBJECT)", JsonToken.class)
                .addStatement(
                        "return context.reportInputMismatch($T.class, $S)",
                        unionClass,
                        "Expected the end of a JSON object while deserializing a union")
                .endControlFlow();
        if (options.sealedUnions()) {
            builder.addStatement("return new $T(type, values)", unionClass.nestedClass(SEALED_UNKNOWN_VARIANT_NAME));
        } else {
            builder.addStatement(
                    "return new $T(new $T(type, values))",
                    unionClass,
                    unionClass.nestedClass(UNKNOWN_WRAPPER_CLASS_NAME));
        }
        return builder.build();
    }

    /**
     * Generates a prototype for a visitor builder setter that can be turned into an interface method declaration or an
     * implementation of such interface method. The signature of the returned builder is
     *
     * <pre>
     * NextStage&lt;T&gt; memberName(Function&lt;MemberType, T&gt; memberVisitor)
     * </pre>
     */
    private static MethodSpec.Builder visitorBuilderSetterPrototype(
            NameTypeMetadata member, TypeName visitResultType, ClassName nextBuilderStage, Options options) {
        TypeName visitorObject = visitorObjectTypeName(member, visitResultType, options);
        return MethodSpec.methodBuilder(JavaNameSanitizer.sanitize(member.memberName))
                .addParameter(ParameterSpec.builder(visitorObject, visitorFieldName(member.memberName))
                        .addAnnotation(Nonnull.class)
                        .build())
                .returns(ParameterizedTypeName.get(nextBuilderStage, visitResultType));
    }

    private static MethodSpec.Builder visitorBuilderUnknownThrowPrototype(
            TypeName visitResultType, ClassName nextBuilderStage) {
        return MethodSpec.methodBuilder("throwOnUnknown")
                .returns(ParameterizedTypeName.get(nextBuilderStage, visitResultType));
    }

    private static TypeSpec generateBase(
            ClassName baseClass, ClassName visitorClass, Map<FieldDefinition, TypeName> memberTypes) {
        TypeSpec.Builder baseBuilder = TypeSpec.interfaceBuilder(baseClass).addModifiers(Modifier.PRIVATE);
        ParameterizedTypeName parameterizedVisitorClass = ParameterizedTypeName.get(visitorClass, TYPE_VARIABLE);
        ParameterSpec visitor =
                ParameterSpec.builder(parameterizedVisitorClass, "visitor").build();
        baseBuilder.addMethod(MethodSpec.methodBuilder("accept")
                .addTypeVariable(TYPE_VARIABLE)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(visitor)
                .returns(TYPE_VARIABLE)
                .build());
        return baseBuilder.build();
    }

    @SuppressWarnings("checkstyle:cyclomaticcomplexity")
    private static List<TypeSpec> generateWrapperClasses(
            TypeMapper typeMapper,
            Map<com.palantir.conjure.spec.TypeName, TypeDefinition> typesMap,
            ClassName baseClass,
            ClassName visitorClass,
            List<FieldDefinition> memberTypeDefs,
            Options options) {
        return memberTypeDefs.stream()
                .map(memberTypeDef -> {
                    boolean isDeprecated = memberTypeDef.getDeprecated().isPresent();
                    FieldName memberName = sanitizeUnknown(memberTypeDef.getFieldName());
                    TypeName memberType = typeMapper.getClassName(memberTypeDef.getType());
                    ClassName wrapperClass = options.sealedUnions()
                            ? sealedVariantClass(baseClass, memberTypeDef.getFieldName())
                            : peerWrapperClass(baseClass, memberName);

                    List<FieldSpec> fields = ImmutableList.of(
                            FieldSpec.builder(memberType, VALUE_FIELD_NAME, Modifier.PRIVATE, Modifier.FINAL)
                                    .build());

                    TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(wrapperClass)
                            .addModifiers(
                                    options.sealedUnions() ? Modifier.PUBLIC : Modifier.PRIVATE,
                                    Modifier.STATIC,
                                    Modifier.FINAL)
                            .addAnnotation(AnnotationSpec.builder(JsonTypeName.class)
                                    .addMember("value", "$S", memberTypeDef.getFieldName())
                                    .build())
                            .addAnnotation(ignoreUnknownAnnotation())
                            .addFields(fields)
                            .addMethod(MethodSpec.constructorBuilder()
                                    .addModifiers(Modifier.PRIVATE)
                                    .addAnnotation(ConjureAnnotations.propertiesJsonCreator())
                                    .addParameter(ParameterSpec.builder(memberType, VALUE_FIELD_NAME)
                                            .addAnnotation(wrapperConstructorParameterAnnotation(
                                                    memberTypeDef, typeMapper, typesMap, options))
                                            .addAnnotations(deserializationAnnotationForSets(memberTypeDef))
                                            .addAnnotation(Nonnull.class)
                                            .build())
                                    .addStatement(
                                            "$L",
                                            Expressions.requireNonNull(
                                                    VALUE_FIELD_NAME,
                                                    String.format("%s cannot be null", memberName.get())))
                                    .addStatement(createConstructor(memberTypeDef.getType(), options))
                                    .build())
                            .addMethod(MethodSpec.methodBuilder(options.sealedUnions() ? "type" : "getType")
                                    .addModifiers(Modifier.PRIVATE)
                                    .addAnnotation(getTypeJsonPropertyAnnotation(options))
                                    .addStatement("return $S", memberTypeDef.getFieldName())
                                    .returns(String.class)
                                    .build())
                            .addMethod(MethodSpec.methodBuilder(options.sealedUnions() ? "value" : "getValue")
                                    .addModifiers(options.sealedUnions() ? Modifier.PUBLIC : Modifier.PRIVATE)
                                    .addAnnotation(AnnotationSpec.builder(JsonProperty.class)
                                            .addMember(
                                                    "value",
                                                    "$S",
                                                    memberTypeDef.getFieldName().get())
                                            .build())
                                    .addStatement("return $L", VALUE_FIELD_NAME)
                                    .returns(memberType)
                                    .build());

                    if (options.sealedUnions()) {
                        // Prevent the custom union serializer and deserializer on the sealed base class from being
                        // inherited by concrete variants when delegating directly to a known wrapper.
                        typeBuilder.addAnnotation(JsonDeserialize.class);
                        typeBuilder.addAnnotation(JsonSerialize.class);
                    }

                    if (!options.sealedUnions() || (options.sealedUnions() && options.sealedUnionVisitors())) {
                        typeBuilder.addMethod(createWrapperAcceptMethod(
                                visitorClass,
                                visitMethodName(memberName.get()),
                                VALUE_FIELD_NAME,
                                isDeprecated,
                                options));
                    }

                    if (options.sealedUnions()) {
                        typeBuilder
                                .superclass(baseClass)
                                .addSuperinterface(baseClass.nestedClass(SEALED_KNOWN_INTERFACE));
                    } else {
                        typeBuilder.addSuperinterface(baseClass);
                    }

                    typeBuilder
                            .addMethod(MethodSpecs.createEquals(wrapperClass))
                            .addMethod(MethodSpecs.createEqualTo(wrapperClass, fields))
                            .addMethod(MethodSpecs.createHashCode(fields))
                            .addMethod(
                                    options.sealedUnions()
                                            ? createLegacyToStringForSealedUnions(
                                                    baseClass, peerWrapperClass(baseClass, memberName))
                                            : MethodSpecs.createToString(
                                                    wrapperClass.simpleName(),
                                                    fields.stream()
                                                            .map(fieldSpec -> FieldName.of(fieldSpec.name()))
                                                            .toList()));
                    return typeBuilder.build();
                })
                .collect(Collectors.toList());
    }

    private static AnnotationSpec getTypeJsonPropertyAnnotation(Options options) {
        AnnotationSpec.Builder builder = AnnotationSpec.builder(JsonProperty.class);
        if (!options.sealedUnions()) {
            builder.addMember("value", "$S", "type");
        }
        return builder.addMember("index", "$L", 0).build();
    }

    private static Iterable<AnnotationSpec> deserializationAnnotationForSets(FieldDefinition field) {
        if (field.getType().accept(TypeVisitor.IS_SET)) {
            return List.of(AnnotationSpec.builder(JsonDeserialize.class)
                    .addMember("as", "$T.class", LinkedHashSet.class)
                    .build());
        }
        return List.of();
    }

    private static AnnotationSpec wrapperConstructorParameterAnnotation(
            FieldDefinition field,
            TypeMapper typeMapper,
            Map<com.palantir.conjure.spec.TypeName, TypeDefinition> typesMap,
            Options options) {
        AnnotationSpec.Builder builder = AnnotationSpec.builder(JsonSetter.class)
                .addMember("value", "$S", field.getFieldName().get());
        Type dealiased = TypeFunctions.toConjureTypeWithoutAliases(field.getType(), typesMap);
        if (dealiased.accept(DefaultableTypeVisitor.INSTANCE)) {
            builder.addMember("nulls", "$T.AS_EMPTY", Nulls.class);
            // We only need to restrict nulls by annotations on Maps, the other collections have non-null constructors
            if (options.defensiveCollections()
                    && options.nonNullCollections()
                    && field.getType().accept(TypeVisitor.IS_MAP)) {
                if (TypeFunctions.isOptionalInnerType(dealiased, typeMapper)) {
                    builder.addMember("contentNulls", "$T.AS_EMPTY", Nulls.class);
                } else {
                    builder.addMember("contentNulls", "$T.FAIL", Nulls.class);
                }
            }
        }
        return builder.build();
    }

    @SuppressWarnings("checkstyle:cyclomaticcomplexity")
    private static TypeSpec generateUnknownWrapper(ClassName baseClass, ClassName visitorClass, Options options) {
        ParameterizedTypeName genericMapType = ParameterizedTypeName.get(Map.class, String.class, Object.class);
        ParameterizedTypeName genericHashMapType = ParameterizedTypeName.get(HashMap.class, String.class, Object.class);
        ParameterSpec typeParameter = ParameterSpec.builder(String.class, "type")
                .addAnnotation(Nonnull.class)
                .build();
        ParameterSpec annotatedTypeParameter = ParameterSpec.builder(UNKNOWN_MEMBER_TYPE, "type")
                .addAnnotation(AnnotationSpec.builder(JsonProperty.class)
                        .addMember("value", "\"type\"")
                        .build())
                .build();

        ClassName wrapperClass = options.sealedUnions()
                ? baseClass.nestedClass(SEALED_UNKNOWN_VARIANT_NAME)
                : baseClass.peerClass(UNKNOWN_WRAPPER_CLASS_NAME);
        List<FieldSpec> fields = ImmutableList.of(
                FieldSpec.builder(UNKNOWN_MEMBER_TYPE, "type", Modifier.PRIVATE, Modifier.FINAL)
                        .build(),
                FieldSpec.builder(genericMapType, VALUE_FIELD_NAME, Modifier.PRIVATE, Modifier.FINAL)
                        .build());
        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(wrapperClass)
                .addModifiers(
                        options.sealedUnions() ? Modifier.PUBLIC : Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .addFields(fields)
                .addMethod(MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PRIVATE)
                        .addAnnotation(ConjureAnnotations.propertiesJsonCreator())
                        .addParameter(annotatedTypeParameter)
                        .addStatement("this($N, new $T())", typeParameter, genericHashMapType)
                        .build())
                .addMethod(MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PRIVATE)
                        .addParameter(typeParameter)
                        .addParameter(ParameterSpec.builder(genericMapType, VALUE_FIELD_NAME)
                                .addAnnotation(Nonnull.class)
                                .build())
                        .addStatement("$L", Expressions.requireNonNull(typeParameter.name(), "type cannot be null"))
                        .addStatement(
                                "$L",
                                Expressions.requireNonNull(
                                        VALUE_FIELD_NAME, String.format("%s cannot be null", VALUE_FIELD_NAME)))
                        .addStatement("this.$1N = $1N", typeParameter)
                        .addStatement("this.$1L = $1L", VALUE_FIELD_NAME)
                        .build())
                .addMethod(MethodSpec.methodBuilder(options.sealedUnions() ? "type" : "getType")
                        .addModifiers(options.sealedUnions() ? Modifier.PUBLIC : Modifier.PRIVATE)
                        .addAnnotation(
                                AnnotationSpec.builder(JsonProperty.class).build())
                        .addStatement("return type")
                        .returns(UNKNOWN_MEMBER_TYPE)
                        .build())
                .addMethod(MethodSpec.methodBuilder(options.sealedUnions() ? "value" : "getValue")
                        .addModifiers(options.sealedUnions() ? Modifier.PUBLIC : Modifier.PRIVATE)
                        .addAnnotation(
                                AnnotationSpec.builder(JsonAnyGetter.class).build())
                        .addStatement("return $L", VALUE_FIELD_NAME)
                        .returns(genericMapType)
                        .build())
                .addMethod(MethodSpec.methodBuilder("put")
                        .addModifiers(Modifier.PRIVATE)
                        .addParameter(String.class, "key")
                        .addParameter(Object.class, "val")
                        .addAnnotation(
                                AnnotationSpec.builder(JsonAnySetter.class).build())
                        .addStatement("$L.put(key, val)", VALUE_FIELD_NAME)
                        .build());

        if (options.sealedUnions()) {
            typeBuilder.addAnnotation(JsonDeserialize.class);
            typeBuilder.addAnnotation(JsonSerialize.class);
        }

        if (!options.sealedUnions() || (options.sealedUnions() && options.sealedUnionVisitors())) {
            typeBuilder.addMethod(createWrapperAcceptMethod(
                    visitorClass, VISIT_UNKNOWN_METHOD_NAME, typeParameter.name(), false, options));
        }

        if (options.sealedUnions()) {
            typeBuilder.superclass(baseClass);
        } else {
            typeBuilder.addSuperinterface(baseClass);
        }

        typeBuilder
                .addMethod(MethodSpecs.createEquals(wrapperClass))
                .addMethod(MethodSpecs.createEqualTo(wrapperClass, fields))
                .addMethod(MethodSpecs.createHashCode(fields))
                .addMethod(
                        options.sealedUnions()
                                ? createLegacyToStringForSealedUnions(
                                        baseClass, baseClass.peerClass(UNKNOWN_WRAPPER_CLASS_NAME))
                                : MethodSpecs.createToString(
                                        wrapperClass.simpleName(),
                                        fields.stream()
                                                .map(fieldSpec -> FieldName.of(fieldSpec.name()))
                                                .toList()));
        return typeBuilder.build();
    }

    private static MethodSpec createWrapperAcceptMethod(
            ClassName visitorClass, String visitMethodName, String valueName, boolean isDeprecated, Options options) {
        ParameterizedTypeName parameterizedVisitorClass = ParameterizedTypeName.get(visitorClass, TYPE_VARIABLE);
        ParameterSpec visitor =
                ParameterSpec.builder(parameterizedVisitorClass, "visitor").build();
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("accept")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addTypeVariable(TYPE_VARIABLE)
                .addParameter(visitor)
                .returns(TYPE_VARIABLE);
        if (visitMethodName.equals(VISIT_UNKNOWN_METHOD_NAME) && options.unionsWithUnknownValues()) {
            methodBuilder.addStatement(
                    "return $N.$N($N, $L)",
                    visitor,
                    visitMethodName,
                    "type",
                    CodeBlock.of("$N.get($N)", VALUE_FIELD_NAME, "type"));
        } else {
            methodBuilder.addStatement("return $N.$N($N)", visitor, visitMethodName, valueName);
        }
        if (isDeprecated) {
            methodBuilder.addAnnotation(AnnotationSpec.builder(SuppressWarnings.class)
                    .addMember("value", "$S", "deprecation")
                    .build());
        }
        return methodBuilder.build();
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

    private static ClassName sealedVariantClass(ClassName unionClass, FieldName memberTypeName) {
        return sealedVariantClass(unionClass, memberTypeName.get());
    }

    private static ClassName sealedVariantClass(ClassName unionClass, String memberTypeName) {
        return ClassName.get(
                unionClass.packageName(),
                unionClass.simpleName(),
                // Sanitize "Known" and "Unknown". Not sanitizing Java reserved names, since these are valid for classes
                // Also need to guard against union class name
                StringUtils.capitalize(sanitizeReserved(unionClass.simpleName(), memberTypeName)));
    }

    private static String visitMethodName(String fieldName) {
        return "visit" + StringUtils.capitalize(fieldName);
    }

    private static String visitorFieldName(String memberName) {
        return memberName + "Visitor";
    }

    private static String variableName() {
        return "value";
    }

    private static String sanitizeUnknown(String input) {
        return "unknown".equalsIgnoreCase(input) ? input + '_' : input;
    }

    private static FieldName sanitizeUnknown(FieldName input) {
        return "unknown".equalsIgnoreCase(input.get()) ? FieldName.of(input.get() + '_') : input;
    }

    private static String sanitizeReserved(String unionClass, String input) {
        return unionClass.equalsIgnoreCase(input)
                        || "unknown".equalsIgnoreCase(input)
                        || "known".equalsIgnoreCase(input)
                ? input + '_'
                : input;
    }

    private UnionGenerator() {}

    private static final class NameTypeMetadata {
        private final String memberName;
        private final TypeName type;

        static final NameTypeMetadata UNKNOWN = new NameTypeMetadata("unknown", UNKNOWN_MEMBER_TYPE);

        private NameTypeMetadata(String memberName, TypeName type) {
            this.memberName = memberName;
            this.type = type;
        }
    }

    private static CodeBlock createConstructor(Type type, Options options) {
        if (options.defensiveCollections() && type.accept(TypeVisitor.IS_LIST)) {
            CollectionType collectionType = CollectionType.from(type, options);
            return CodeBlock.of(
                    "this.$1L = $2T.unmodifiableList($2T.$3L($1L))",
                    VALUE_FIELD_NAME,
                    ConjureCollections.class,
                    collectionType.getConjureCollectionStaticFactoryMethod());
        }

        if (options.defensiveCollections() && type.accept(TypeVisitor.IS_SET)) {
            CollectionType collectionType = CollectionType.from(type, options);
            return CodeBlock.of(
                    "this.$1L = $2T.unmodifiableSet($3T.$4L($1L))",
                    VALUE_FIELD_NAME,
                    Collections.class,
                    ConjureCollections.class,
                    collectionType.getConjureCollectionStaticFactoryMethod());
        }

        if (options.defensiveCollections() && type.accept(TypeVisitor.IS_MAP)) {
            return CodeBlock.of(
                    "this.$1L = $2T.unmodifiableMap(new $3T<>($1L))",
                    VALUE_FIELD_NAME,
                    Collections.class,
                    LinkedHashMap.class);
        }

        return CodeBlock.of("this.$1L = $1L", VALUE_FIELD_NAME);
    }
}
