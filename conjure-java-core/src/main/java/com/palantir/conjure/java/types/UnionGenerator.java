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
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.Nulls;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;
import com.palantir.conjure.java.ConjureAnnotations;
import com.palantir.conjure.java.Options;
import com.palantir.conjure.java.util.JavaNameSanitizer;
import com.palantir.conjure.java.util.Javadoc;
import com.palantir.conjure.java.util.Packages;
import com.palantir.conjure.java.util.StableCollectors;
import com.palantir.conjure.java.util.TypeFunctions;
import com.palantir.conjure.java.visitor.DefaultableTypeVisitor;
import com.palantir.conjure.spec.FieldDefinition;
import com.palantir.conjure.spec.FieldName;
import com.palantir.conjure.spec.LogSafety;
import com.palantir.conjure.spec.Type;
import com.palantir.conjure.spec.TypeDefinition;
import com.palantir.conjure.spec.UnionDefinition;
import com.palantir.logsafe.Safe;
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
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
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

    // If the member type is not known, a String containing the name of the unknown type is used.
    private static final TypeName UNKNOWN_MEMBER_TYPE = ClassName.get(String.class);
    private static final TypeName UNKNOWN_VALUE_TYPE = ClassName.get(Object.class);

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
        ClassName visitorBuilderClass = unionClass.nestedClass("VisitorBuilder");
        Map<FieldDefinition, TypeName> memberTypes = typeDef.getUnion().stream()
                .collect(StableCollectors.toLinkedMap(
                        Function.identity(),
                        entry -> ConjureAnnotations.withSafety(
                                typeMapper.getClassName(entry.getType()), entry.getSafety())));

        if (options.sealedUnions()) {
            ClassName unknownWrapperClass = peerWrapperClass(unionClass, FieldName.of("unknown"), options);
            TypeSpec.Builder typeBuilder = TypeSpec.interfaceBuilder(
                            typeDef.getTypeName().getName())
                    .addAnnotations(ConjureAnnotations.safety(safetyEvaluator.evaluate(TypeDefinition.union(typeDef))))
                    .addAnnotation(ConjureAnnotations.getConjureGeneratedAnnotation(UnionGenerator.class))
                    .addAnnotation(jacksonJsonTypeInfo(unknownWrapperClass))
                    .addAnnotation(generateJacksonSubtypeAnnotation(unionClass, memberTypes, options))
                    .addAnnotation(jacksonIgnoreUnknownAnnotation())
                    .addModifiers(Modifier.PUBLIC, Modifier.SEALED)
                    .addMethods(generateStaticFactories(typeMapper, unionClass, typeDef.getUnion(), options))
                    .addTypes(generateWrapperClasses(
                            typeMapper, typesMap, unionClass, visitorClass, typeDef.getUnion(), options))
                    .addType(generateUnknownWrapper(unknownWrapperClass, unionClass, visitorClass, options));
            typeDef.getDocs().ifPresent(docs -> typeBuilder.addJavadoc("$L", Javadoc.render(docs)));

            if (options.sealedUnionVisitors()) {
                typeBuilder
                        .addMethod(generateAcceptVisitorMethodSignature(visitorClass))
                        .addType(generateVisitor(unionClass, visitorClass, memberTypes, visitorBuilderClass, options))
                        .addType(generateVisitorBuilder(
                                unionClass, visitorClass, visitorBuilderClass, memberTypes, options))
                        .addTypes(
                                generateVisitorBuilderStageInterfaces(unionClass, visitorClass, memberTypes, options));
            }

            return JavaFile.builder(prefixedTypeName.getPackage(), typeBuilder.build())
                    .skipJavaLangImports(true)
                    .indent("    ")
                    .build();
        } else {
            ClassName baseClass = unionClass.nestedClass("Base");
            ClassName unknownWrapperClass = baseClass.peerClass(UNKNOWN_WRAPPER_CLASS_NAME);
            List<FieldSpec> fields =
                    ImmutableList.of(FieldSpec.builder(baseClass, VALUE_FIELD_NAME, Modifier.PRIVATE, Modifier.FINAL)
                            .build());

            TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(
                            typeDef.getTypeName().getName())
                    .addAnnotations(ConjureAnnotations.safety(safetyEvaluator.evaluate(TypeDefinition.union(typeDef))))
                    .addAnnotation(ConjureAnnotations.getConjureGeneratedAnnotation(UnionGenerator.class))
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addFields(fields)
                    .addMethod(generateConstructor(baseClass))
                    .addMethod(generateGetValue(baseClass))
                    .addMethods(generateStaticFactories(typeMapper, unionClass, typeDef.getUnion(), options))
                    .addMethod(generateAcceptVisitMethod(visitorClass))
                    .addType(generateVisitor(unionClass, visitorClass, memberTypes, visitorBuilderClass, options))
                    .addType(
                            generateVisitorBuilder(unionClass, visitorClass, visitorBuilderClass, memberTypes, options))
                    .addTypes(generateVisitorBuilderStageInterfaces(unionClass, visitorClass, memberTypes, options))
                    .addType(generateBase(baseClass, visitorClass, memberTypes, options))
                    .addTypes(generateWrapperClasses(
                            typeMapper, typesMap, baseClass, visitorClass, typeDef.getUnion(), options))
                    .addType(generateUnknownWrapper(unknownWrapperClass, baseClass, visitorClass, options))
                    .addMethod(generateEquals(unionClass))
                    .addMethod(MethodSpecs.createEqualTo(unionClass, fields))
                    .addMethod(MethodSpecs.createHashCode(fields))
                    .addMethod(MethodSpecs.createToString(
                            unionClass.simpleName(),
                            fields.stream()
                                    .map(fieldSpec -> FieldName.of(fieldSpec.name))
                                    .collect(Collectors.toList())));

            typeDef.getDocs().ifPresent(docs -> typeBuilder.addJavadoc("$L", Javadoc.render(docs)));

            return JavaFile.builder(prefixedTypeName.getPackage(), typeBuilder.build())
                    .skipJavaLangImports(true)
                    .indent("    ")
                    .build();
        }
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
            TypeMapper typeMapper, ClassName unionClass, List<FieldDefinition> memberTypeDefs, Options options) {
        List<MethodSpec> staticFactories = memberTypeDefs.stream()
                .map(memberTypeDef -> {
                    FieldName memberName = sanitizeUnknown(memberTypeDef.getFieldName());
                    TypeName memberType = typeMapper.getClassName(memberTypeDef.getType());
                    String variableName = variableName();
                    // memberName is guarded to be a valid Java identifier and not to end in an underscore, so this is
                    // safe
                    MethodSpec.Builder builder = MethodSpec.methodBuilder(JavaNameSanitizer.sanitize(memberName))
                            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                            .addParameter(ParameterSpec.builder(memberType, variableName)
                                    .addAnnotations(ConjureAnnotations.safety(memberTypeDef.getSafety()))
                                    .build())
                            .addCode(
                                    options.sealedUnions()
                                            ? CodeBlock.of(
                                                    "return new $T($L);",
                                                    wrapperClass(unionClass, memberName, options),
                                                    variableName)
                                            : CodeBlock.of(
                                                    "return new $T(new $T($L));",
                                                    unionClass,
                                                    wrapperClass(unionClass, memberName, options),
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
                                wrapperClass(unionClass, FieldName.of("unknown"), options),
                                typeParam,
                                singletonMap)
                        : CodeBlock.of(
                                "return new $T(new $T($N, $L))",
                                unionClass,
                                wrapperClass(unionClass, FieldName.of("unknown"), options),
                                typeParam,
                                singletonMap));
        builder.endControlFlow();
        return builder.build();
    }

    private static MethodSpec generateAcceptVisitorMethodSignature(ClassName visitorClass) {
        ParameterizedTypeName parameterizedVisitorClass = ParameterizedTypeName.get(visitorClass, TYPE_VARIABLE);
        ParameterSpec visitor =
                ParameterSpec.builder(parameterizedVisitorClass, "visitor").build();
        return MethodSpec.methodBuilder("accept")
                .addParameter(visitor)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addTypeVariable(TYPE_VARIABLE)
                .build();
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

    private static MethodSpec generateEquals(ClassName unionClass) {
        ParameterSpec other = ParameterSpec.builder(ClassName.OBJECT, "other").build();
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

    private static TypeSpec generateVisitor(
            ClassName unionClass,
            ClassName visitorClass,
            Map<FieldDefinition, TypeName> memberTypes,
            ClassName visitorBuilderClass,
            Options options) {

        MethodSpec.Builder visitUnknownMethod = MethodSpec.methodBuilder(VISIT_UNKNOWN_METHOD_NAME)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(ParameterSpec.builder(UNKNOWN_MEMBER_TYPE, UNKNOWN_TYPE_PARAM_NAME)
                        .addAnnotation(Safe.class)
                        .build())
                .returns(TYPE_VARIABLE);
        if (options.unionsWithUnknownValues()) {
            visitUnknownMethod.addParameter(ParameterSpec.builder(UNKNOWN_VALUE_TYPE, UNKNOWN_VALUE_PARAM_NAME)
                    .addAnnotations(ConjureAnnotations.safety(SafetyEvaluator.UNKNOWN_UNION_VARINT_SAFETY))
                    .build());
        }

        MethodSpec.Builder builderStaticFactoryMethod = MethodSpec.methodBuilder("builder")
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
                        TYPE_VARIABLE));
        if (options.sealedUnions()) {
            builderStaticFactoryMethod.addAnnotation(Deprecated.class);
            builderStaticFactoryMethod.addJavadoc("@Deprecated - prefer Java 17 pattern matching switch expressions.");
        }

        return TypeSpec.interfaceBuilder(visitorClass)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addTypeVariable(TYPE_VARIABLE)
                .addMethods(generateMemberVisitMethods(memberTypes))
                .addMethod(visitUnknownMethod.build())
                .addMethod(builderStaticFactoryMethod.build())
                .build();
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
                                    .addAnnotations(ConjureAnnotations.safety(
                                            entry.getKey().getSafety()))
                                    .build())
                            .returns(TYPE_VARIABLE)
                            .build();
                })
                .collect(Collectors.toList());
    }

    /**
     * Generates a builder class for the given {@code visitor} class.
     */
    private static TypeSpec generateVisitorBuilder(
            ClassName enclosingClass,
            ClassName visitor,
            ClassName visitorBuilder,
            Map<FieldDefinition, TypeName> memberTypeMap,
            Options options) {
        TypeVariableName visitResultType = TypeVariableName.get("T");
        return TypeSpec.classBuilder(visitorBuilder)
                .addModifiers(
                        options.sealedUnions() ? Modifier.PUBLIC : Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
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

    /**
     * Generates all the interface type names for the different visitor builder stages.
     */
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

    /**
     * Generates the name of the interface of a visitor builder stage.
     */
    private static ClassName visitorStageInterfaceName(ClassName enclosingClass, String stageName) {
        return enclosingClass.nestedClass(StringUtils.capitalize(stageName) + "StageVisitorBuilder");
    }

    /**
     * Convenience method for generating a {@code Function<MemberType, T>} used for fields and setters of the
     * visitor builder. Special-cases generation of a {@code BiFunction<String, Map<String, Object>, T>} in the case
     * of the new unknownVisitor.
     */
    private static TypeName visitorObjectTypeName(NameTypeMetadata member, TypeName visitResultType, Options options) {
        if (member.type.equals(TypeName.INT)) {
            return ParameterizedTypeName.get(ClassName.get(IntFunction.class), visitResultType);
        } else if (member.type.equals(TypeName.DOUBLE)) {
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
                    ClassName.get(Function.class),
                    member.type.box().annotated(ConjureAnnotations.safety(member.safety)),
                    visitResultType);
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
                                sanitizeUnknown(entry.getKey().getFieldName().get()),
                                entry.getValue(),
                                entry.getKey().getSafety()))
                        .sorted(Comparator.comparing(p -> p.memberName)),
                Stream.of(NameTypeMetadata.UNKNOWN));
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
            ClassName baseClass, ClassName visitorClass, Map<FieldDefinition, TypeName> memberTypes, Options options) {
        ClassName unknownWrapperClass = peerWrapperClass(baseClass, FieldName.of("unknown"), options);
        TypeSpec.Builder baseBuilder = TypeSpec.interfaceBuilder(baseClass)
                .addModifiers(Modifier.PRIVATE)
                .addAnnotation(jacksonJsonTypeInfo(unknownWrapperClass));
        if (!memberTypes.isEmpty()) {
            baseBuilder.addAnnotation(generateJacksonSubtypeAnnotation(baseClass, memberTypes, options));
        }
        baseBuilder.addAnnotation(jacksonIgnoreUnknownAnnotation());
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

    private static AnnotationSpec jacksonJsonTypeInfo(ClassName unknownWrapperClass) {
        return AnnotationSpec.builder(JsonTypeInfo.class)
                .addMember("use", "JsonTypeInfo.Id.NAME")
                .addMember("include", "JsonTypeInfo.As.EXISTING_PROPERTY")
                .addMember("property", "\"type\"")
                .addMember("visible", "$L", true)
                .addMember("defaultImpl", "$T.class", unknownWrapperClass)
                .build();
    }

    private static AnnotationSpec jacksonIgnoreUnknownAnnotation() {
        return AnnotationSpec.builder(JsonIgnoreProperties.class)
                .addMember("ignoreUnknown", "$L", true)
                .build();
    }

    private static AnnotationSpec generateJacksonSubtypeAnnotation(
            ClassName baseClass, Map<FieldDefinition, TypeName> memberTypes, Options options) {
        List<AnnotationSpec> subAnnotations = memberTypes.entrySet().stream()
                .map(entry -> AnnotationSpec.builder(JsonSubTypes.Type.class)
                        .addMember(
                                "value",
                                "$T.class",
                                peerWrapperClass(
                                        baseClass,
                                        sanitizeUnknown(entry.getKey().getFieldName()),
                                        options))
                        .build())
                .collect(Collectors.toList());
        AnnotationSpec.Builder annotationBuilder = AnnotationSpec.builder(JsonSubTypes.class);
        subAnnotations.forEach(subAnnotation -> annotationBuilder.addMember("value", "$L", subAnnotation));
        return annotationBuilder.build();
    }

    private static List<TypeSpec> generateWrapperClasses(
            TypeMapper typeMapper,
            Map<com.palantir.conjure.spec.TypeName, TypeDefinition> typesMap,
            ClassName superInterface,
            ClassName visitorClass,
            List<FieldDefinition> memberTypeDefs,
            Options options) {
        return memberTypeDefs.stream()
                .map(memberTypeDef -> {
                    boolean isDeprecated = memberTypeDef.getDeprecated().isPresent();
                    FieldName memberName = sanitizeUnknown(memberTypeDef.getFieldName());
                    TypeName memberType = typeMapper.getClassName(memberTypeDef.getType());
                    ClassName wrapperClass = peerWrapperClass(superInterface, memberName, options);

                    List<FieldSpec> fields = ImmutableList.of(
                            FieldSpec.builder(memberType, VALUE_FIELD_NAME, Modifier.PRIVATE, Modifier.FINAL)
                                    .build());

                    TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(wrapperClass)
                            .addModifiers(
                                    options.sealedUnions() ? Modifier.PUBLIC : Modifier.PRIVATE,
                                    Modifier.STATIC,
                                    Modifier.FINAL)
                            .addSuperinterface(superInterface)
                            .addAnnotation(AnnotationSpec.builder(JsonTypeName.class)
                                    .addMember("value", "$S", memberTypeDef.getFieldName())
                                    .build())
                            .addFields(fields)
                            .addMethod(MethodSpec.constructorBuilder()
                                    .addModifiers(Modifier.PRIVATE)
                                    .addAnnotation(ConjureAnnotations.propertiesJsonCreator())
                                    .addParameter(ParameterSpec.builder(memberType, VALUE_FIELD_NAME)
                                            .addAnnotation(
                                                    wrapperConstructorParameterAnnotation(memberTypeDef, typesMap))
                                            .addAnnotation(Nonnull.class)
                                            .build())
                                    .addStatement(
                                            "$L",
                                            Expressions.requireNonNull(
                                                    VALUE_FIELD_NAME,
                                                    String.format("%s cannot be null", memberName.get())))
                                    .addStatement("this.$1L = $1L", VALUE_FIELD_NAME)
                                    .build())
                            .addMethod(MethodSpec.methodBuilder("getType")
                                    .addModifiers(Modifier.PRIVATE)
                                    .addAnnotation(AnnotationSpec.builder(JsonProperty.class)
                                            .addMember("value", "$S", "type")
                                            .addMember("index", "$L", 0)
                                            .build())
                                    .addStatement("return $S", memberTypeDef.getFieldName())
                                    .returns(String.class)
                                    .build())
                            .addMethod(MethodSpec.methodBuilder("getValue")
                                    .addModifiers(Modifier.PRIVATE)
                                    .addAnnotation(AnnotationSpec.builder(JsonProperty.class)
                                            .addMember(
                                                    "value",
                                                    "$S",
                                                    memberTypeDef.getFieldName().get())
                                            .build())
                                    .addStatement("return $L", VALUE_FIELD_NAME)
                                    .returns(memberType)
                                    .build())
                            .addMethods(
                                    !options.sealedUnions() || options.sealedUnionVisitors()
                                            ? List.of(createWrapperAcceptMethod(
                                                    visitorClass,
                                                    visitMethodName(memberName.get()),
                                                    VALUE_FIELD_NAME,
                                                    isDeprecated,
                                                    options))
                                            : List.of())
                            .addMethod(MethodSpecs.createEquals(wrapperClass))
                            .addMethod(MethodSpecs.createEqualTo(wrapperClass, fields))
                            .addMethod(MethodSpecs.createHashCode(fields))
                            .addMethod(MethodSpecs.createToString(
                                    wrapperClass.simpleName(),
                                    fields.stream()
                                            .map(fieldSpec -> FieldName.of(fieldSpec.name))
                                            .collect(Collectors.toList())));

                    return typeBuilder.build();
                })
                .collect(Collectors.toList());
    }

    private static AnnotationSpec wrapperConstructorParameterAnnotation(
            FieldDefinition field, Map<com.palantir.conjure.spec.TypeName, TypeDefinition> typesMap) {
        AnnotationSpec.Builder builder = AnnotationSpec.builder(JsonSetter.class)
                .addMember("value", "$S", field.getFieldName().get());
        Type dealiased = TypeFunctions.toConjureTypeWithoutAliases(field.getType(), typesMap);
        if (dealiased.accept(DefaultableTypeVisitor.INSTANCE)) {
            builder.addMember("nulls", "$T.AS_EMPTY", Nulls.class);
        }
        return builder.build();
    }

    private static TypeSpec generateUnknownWrapper(
            ClassName unknownWrapperClass, ClassName superinterface, ClassName visitorClass, Options options) {
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

        List<FieldSpec> fields = ImmutableList.of(
                FieldSpec.builder(UNKNOWN_MEMBER_TYPE, "type", Modifier.PRIVATE, Modifier.FINAL)
                        .build(),
                FieldSpec.builder(genericMapType, VALUE_FIELD_NAME, Modifier.PRIVATE, Modifier.FINAL)
                        .build());
        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(unknownWrapperClass)
                .addModifiers(
                        options.sealedUnions() ? Modifier.PUBLIC : Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .addSuperinterface(superinterface)
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
                        .addStatement("$L", Expressions.requireNonNull(typeParameter.name, "type cannot be null"))
                        .addStatement(
                                "$L",
                                Expressions.requireNonNull(
                                        VALUE_FIELD_NAME, String.format("%s cannot be null", VALUE_FIELD_NAME)))
                        .addStatement("this.$1N = $1N", typeParameter)
                        .addStatement("this.$1L = $1L", VALUE_FIELD_NAME)
                        .build())
                .addMethod(MethodSpec.methodBuilder("getType")
                        .addModifiers(Modifier.PRIVATE)
                        .addAnnotation(
                                AnnotationSpec.builder(JsonProperty.class).build())
                        .addStatement("return type")
                        .returns(UNKNOWN_MEMBER_TYPE)
                        .build())
                .addMethod(MethodSpec.methodBuilder("getValue")
                        .addModifiers(Modifier.PRIVATE)
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
                        .build())
                .addMethods(
                        !options.sealedUnions() || options.sealedUnionVisitors()
                                ? List.of(createWrapperAcceptMethod(
                                        visitorClass, VISIT_UNKNOWN_METHOD_NAME, typeParameter.name, false, options))
                                : List.of())
                .addMethod(MethodSpecs.createEquals(unknownWrapperClass))
                .addMethod(MethodSpecs.createEqualTo(unknownWrapperClass, fields))
                .addMethod(MethodSpecs.createHashCode(fields))
                .addMethod(MethodSpecs.createToString(
                        unknownWrapperClass.simpleName(),
                        fields.stream()
                                .map(fieldSpec -> FieldName.of(fieldSpec.name))
                                .collect(Collectors.toList())));
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

    private static ClassName wrapperClass(ClassName unionClass, FieldName memberTypeName, Options options) {
        if (options.sealedUnions()) {
            return ClassName.get(
                    unionClass.packageName(),
                    unionClass.simpleName(),
                    StringUtils.capitalize(memberTypeName.get()));
        } else {
            return ClassName.get(
                    unionClass.packageName(),
                    unionClass.simpleName(),
                    StringUtils.capitalize(memberTypeName.get()) + "Wrapper");
        }
    }

    private static ClassName peerWrapperClass(ClassName peerClass, FieldName memberTypeName, Options options) {
        if (options.sealedUnions()) {
            return peerClass.peerClass(StringUtils.capitalize(memberTypeName.get()));
        } else {
            return peerClass.peerClass(StringUtils.capitalize(memberTypeName.get()) + "Wrapper");
        }
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

    private UnionGenerator() {}

    private static final class NameTypeMetadata {
        private final String memberName;
        private final TypeName type;
        private final Optional<LogSafety> safety;

        static final NameTypeMetadata UNKNOWN =
                new NameTypeMetadata("unknown", UNKNOWN_MEMBER_TYPE, SafetyEvaluator.UNKNOWN_UNION_VARINT_SAFETY);

        private NameTypeMetadata(String memberName, TypeName type, Optional<LogSafety> safety) {
            this.memberName = memberName;
            this.type = type;
            this.safety = safety;
        }
    }
}
