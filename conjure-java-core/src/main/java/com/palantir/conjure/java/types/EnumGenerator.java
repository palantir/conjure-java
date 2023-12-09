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

import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.base.CaseFormat;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;
import com.google.errorprone.annotations.Immutable;
import com.palantir.conjure.java.ConjureAnnotations;
import com.palantir.conjure.java.Options;
import com.palantir.conjure.java.util.Javadoc;
import com.palantir.conjure.java.util.Packages;
import com.palantir.conjure.spec.EnumDefinition;
import com.palantir.conjure.spec.EnumValueDefinition;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.lang.model.element.Modifier;

public final class EnumGenerator {

    private static final String VALUE_PARAMETER = "value";
    private static final String STRING_PARAMETER = "string";
    private static final String VISIT_METHOD_NAME = "visit";
    private static final String VISIT_UNKNOWN_METHOD_NAME = "visitUnknown";
    private static final TypeVariableName TYPE_VARIABLE = TypeVariableName.get("T");
    private static final String UNKNOWN_TYPE_PARAM_NAME = "unknownType";
    private static final String UNKNOWN_TYPE_NAME = "unknown";
    private static final String COMPLETED = "completed_";
    private static final String STAGE_VISITOR_BUILDER = "StageVisitorBuilder";
    private static final String VISITOR_FIELD_NAME_SUFFIX = "Visitor";
    private static final TypeName UNKNOWN_MEMBER_TYPE = ClassName.get(String.class);
    /** The largest number of values allowed in the enum before we stop generating visitor builders. */
    private static final int MAXIMUM_ENUM_VALUE_COUNT_FOR_VISITOR_BUILDERS = 50;

    private EnumGenerator() {}

    public static JavaFile generateEnumType(EnumDefinition typeDef, Options options) {
        com.palantir.conjure.spec.TypeName prefixedTypeName =
                Packages.getPrefixedName(typeDef.getTypeName(), options.packagePrefix());
        ClassName thisClass = ClassName.get(prefixedTypeName.getPackage(), prefixedTypeName.getName());
        ClassName enumClass = thisClass.nestedClass("Value");
        ClassName visitorClass = thisClass.nestedClass("Visitor");
        ClassName visitorBuilderClass = thisClass.nestedClass("VisitorBuilder");

        return JavaFile.builder(
                        prefixedTypeName.getPackage(),
                        createSafeEnum(typeDef, thisClass, enumClass, visitorClass, visitorBuilderClass))
                .skipJavaLangImports(true)
                .indent("    ")
                .build();
    }

    private static TypeSpec createSafeEnum(
            EnumDefinition typeDef,
            ClassName thisClass,
            ClassName enumClass,
            ClassName visitorClass,
            ClassName visitorBuilderClass) {
        TypeSpec.Builder wrapper = TypeSpec.classBuilder(typeDef.getTypeName().getName())
                .addAnnotation(ConjureAnnotations.getConjureGeneratedAnnotation(EnumGenerator.class))
                .addAnnotation(Safe.class)
                .addAnnotation(Immutable.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addType(createEnum(enumClass, typeDef.getValues(), true))
                .addType(createVisitor(thisClass, visitorClass, visitorBuilderClass, typeDef.getValues()));

        if (typeDef.getValues().size() <= MAXIMUM_ENUM_VALUE_COUNT_FOR_VISITOR_BUILDERS) {
            wrapper.addType(createVisitorBuilder(thisClass, visitorClass, visitorBuilderClass, typeDef.getValues()))
                    .addTypes(generateVisitorBuilderStageInterfaces(thisClass, visitorClass, typeDef.getValues()));
        }

        wrapper.addField(enumClass, VALUE_PARAMETER, Modifier.PRIVATE, Modifier.FINAL)
                .addField(ClassName.get(String.class), STRING_PARAMETER, Modifier.PRIVATE, Modifier.FINAL)
                .addFields(createConstants(typeDef.getValues(), thisClass, enumClass))
                .addField(createValuesList(thisClass, typeDef.getValues()))
                .addMethod(createConstructor(enumClass))
                .addMethod(MethodSpec.methodBuilder("get")
                        .addModifiers(Modifier.PUBLIC)
                        .returns(enumClass)
                        .addStatement("return this.value")
                        .build())
                .addMethod(MethodSpec.methodBuilder("toString")
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(Override.class)
                        .addAnnotation(JsonValue.class)
                        .returns(ClassName.get(String.class))
                        .addStatement("return this.string")
                        .build())
                .addMethod(createEquals(thisClass))
                .addMethod(createHashCode())
                .addMethod(createValueOf(thisClass, typeDef.getValues()))
                .addMethod(generateAcceptVisitMethod(visitorClass, typeDef.getValues()))
                .addMethod(createValues(thisClass));

        typeDef.getDocs().ifPresent(docs -> wrapper.addJavadoc("$L<p>\n", Javadoc.render(docs)));

        wrapper.addJavadoc(
                "This class is used instead of a native enum to support unknown values.\n"
                        + "Rather than throw an exception, the {@link $1T#valueOf} method defaults to a new "
                        + "instantiation of\n{@link $1T} where {@link $1T#get} will return {@link $2T#UNKNOWN}.\n"
                        + "<p>\n"
                        + "For example, {@code $1T.valueOf(\"corrupted value\").get()} will return "
                        + "{@link $2T#UNKNOWN},\nbut {@link $1T#toString} will return \"corrupted value\".\n"
                        + "<p>\n"
                        + "There is no method to access all instantiations of this class, since they cannot be known "
                        + "at compile time.\n",
                thisClass,
                enumClass);

        return wrapper.build();
    }

    private static Iterable<FieldSpec> createConstants(
            Iterable<EnumValueDefinition> values, ClassName thisClass, ClassName enumClass) {
        return Iterables.transform(values, v -> {
            FieldSpec.Builder fieldSpec = FieldSpec.builder(
                            thisClass, v.getValue(), Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                    .initializer(
                            CodeBlock.of("new $1T($2T.$3N, $4S)", thisClass, enumClass, v.getValue(), v.getValue()));
            Javadoc.render(v.getDocs(), v.getDeprecated()).ifPresent(docs -> fieldSpec.addJavadoc("$L", docs));
            v.getDeprecated().ifPresent(_deprecated -> fieldSpec.addAnnotation(Deprecated.class));
            return fieldSpec.build();
        });
    }

    private static TypeSpec createEnum(ClassName enumClass, Iterable<EnumValueDefinition> values, boolean withUnknown) {
        TypeSpec.Builder enumBuilder = TypeSpec.enumBuilder(enumClass.simpleName())
                .addAnnotation(ConjureAnnotations.getConjureGeneratedAnnotation(EnumGenerator.class))
                .addModifiers(Modifier.PUBLIC);
        for (EnumValueDefinition value : values) {
            TypeSpec.Builder anonymousClassBuilder = TypeSpec.anonymousClassBuilder("");
            Javadoc.render(value.getDocs(), value.getDeprecated())
                    .ifPresent(docs -> anonymousClassBuilder.addJavadoc("$L", docs));
            value.getDeprecated().ifPresent(_deprecation -> anonymousClassBuilder.addAnnotation(Deprecated.class));
            enumBuilder.addEnumConstant(value.getValue(), anonymousClassBuilder.build());
        }
        if (withUnknown) {
            enumBuilder.addEnumConstant("UNKNOWN");
        } else {
            enumBuilder.addMethod(MethodSpec.methodBuilder("fromString")
                    .addJavadoc("$L", "Preferred, case-insensitive constructor for string-to-enum conversion.\n")
                    .addAnnotation(ConjureAnnotations.delegatingJsonCreator())
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .addParameter(ClassName.get(String.class), "value")
                    .addStatement("return $T.valueOf(value.toUpperCase($T.ROOT))", enumClass, Locale.class)
                    .returns(enumClass)
                    .build());
        }
        return enumBuilder.build();
    }

    private static TypeSpec createVisitor(
            ClassName enumClass,
            ClassName visitorClass,
            ClassName visitorBuilderClass,
            List<EnumValueDefinition> values) {
        TypeSpec.Builder wrapper = TypeSpec.interfaceBuilder(visitorClass.simpleName())
                .addAnnotation(ConjureAnnotations.getConjureGeneratedAnnotation(EnumGenerator.class))
                .addModifiers(Modifier.PUBLIC)
                .addTypeVariable(TYPE_VARIABLE)
                .addMethods(generateMemberVisitMethods(values))
                .addMethod(MethodSpec.methodBuilder(VISIT_UNKNOWN_METHOD_NAME)
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .addParameter(String.class, "unknownValue")
                        .returns(TYPE_VARIABLE)
                        .build());
        if (values.size() <= MAXIMUM_ENUM_VALUE_COUNT_FOR_VISITOR_BUILDERS) {
            wrapper.addMethod(MethodSpec.methodBuilder("builder")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .addTypeVariable(TYPE_VARIABLE)
                    .addStatement("return new $T<$T>()", visitorBuilderClass, TYPE_VARIABLE)
                    .returns(ParameterizedTypeName.get(
                            visitorStageInterfaceName(
                                    enumClass,
                                    stageEnumNames(values).findFirst().get()),
                            TYPE_VARIABLE))
                    .build());
        }
        return wrapper.build();
    }

    private static Stream<String> stageEnumNames(Iterable<EnumValueDefinition> values) {
        return Stream.concat(
                ImmutableList.copyOf(values).stream().map(EnumValueDefinition::getValue), Stream.of(UNKNOWN_TYPE_NAME));
    }

    private static List<MethodSpec> generateMemberVisitMethods(Iterable<EnumValueDefinition> values) {
        ImmutableList.Builder<MethodSpec> methods = ImmutableList.builder();
        for (EnumValueDefinition value : values) {
            MethodSpec.Builder methodSpecBuilder = MethodSpec.methodBuilder(getVisitorMethodName(value))
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .returns(TYPE_VARIABLE);
            Javadoc.render(value.getDocs(), value.getDeprecated())
                    .ifPresent(docs -> methodSpecBuilder.addJavadoc("$L", docs));
            value.getDeprecated().ifPresent(_deprecated -> methodSpecBuilder.addAnnotation(Deprecated.class));
            methods.add(methodSpecBuilder.build());
        }
        return methods.build();
    }

    /** Generates a builder class for the given {@code visitor} class. */
    private static TypeSpec createVisitorBuilder(
            ClassName enclosingClass,
            ClassName visitor,
            ClassName visitorBuilder,
            Iterable<EnumValueDefinition> values) {
        TypeVariableName visitResultType = TypeVariableName.get("T");
        return TypeSpec.classBuilder(visitorBuilder)
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .addTypeVariable(visitResultType)
                .addSuperinterfaces(allVisitorBuilderStages(enclosingClass, values, visitResultType))
                .addFields(allVisitorBuilderFields(values, visitResultType))
                .addMethods(allVisitorBuilderSetters(enclosingClass, visitResultType, values))
                .addMethod(builderBuildMethod(visitor, visitResultType, values))
                .build();
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
            ClassName enclosingClass, ClassName visitorClass, Iterable<EnumValueDefinition> values) {
        TypeVariableName visitResultType = TypeVariableName.get("T");
        List<TypeSpec> interfaces = new ArrayList<>();
        PeekingIterator<String> valueIter =
                Iterators.peekingIterator(stageEnumNames(values).iterator());
        while (valueIter.hasNext()) {
            String value = valueIter.next();
            String nextBuilderStageName = valueIter.hasNext() ? valueIter.peek() : COMPLETED;
            ClassName nextStageClassName = visitorStageInterfaceName(enclosingClass, nextBuilderStageName);
            List<MethodSpec> methods = UNKNOWN_TYPE_NAME.equals(value)
                    ? unknownSpecificVisitorPrototypes(visitResultType, nextStageClassName)
                    : ImmutableList.of(visitorBuilderSetterPrototype(value, visitResultType, nextStageClassName)
                            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                            .build());
            interfaces.add(TypeSpec.interfaceBuilder(visitorStageInterfaceName(enclosingClass, value))
                    .addTypeVariable(visitResultType)
                    .addModifiers(Modifier.PUBLIC)
                    .addMethods(methods)
                    .build());
        }
        interfaces.add(TypeSpec.interfaceBuilder(visitorStageInterfaceName(enclosingClass, COMPLETED))
                .addTypeVariable(visitResultType)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(MethodSpec.methodBuilder("build")
                        .returns(ParameterizedTypeName.get(visitorClass, visitResultType))
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .build())
                .build());
        return interfaces;
    }

    private static ImmutableList<MethodSpec> unknownSpecificVisitorPrototypes(
            TypeVariableName visitResultType, ClassName nextStageClassName) {
        ImmutableList.Builder<MethodSpec> methods = ImmutableList.builder();

        methods.add(MethodSpec.methodBuilder("visitUnknown")
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

        // Throw on unknown
        methods.add(visitorBuilderUnknownThrowPrototype(visitResultType, nextStageClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .build());

        return methods.build();
    }

    /** Generates all the interface type names for the different visitor builder stages. */
    private static List<TypeName> allVisitorBuilderStages(
            ClassName enclosingClass, Iterable<EnumValueDefinition> values, TypeVariableName visitResultType) {
        return Stream.concat(stageEnumNames(values), Stream.of(COMPLETED))
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
            Iterable<EnumValueDefinition> values, TypeVariableName visitResultType) {
        return stageEnumNames(values)
                .map(value -> FieldSpec.builder(
                                UNKNOWN_TYPE_NAME.equals(value)
                                        ? ParameterizedTypeName.get(
                                                ClassName.get(Function.class),
                                                UNKNOWN_MEMBER_TYPE.annotated(AnnotationSpec.builder(Safe.class)
                                                        .build()),
                                                visitResultType)
                                        : visitorObjectTypeName(visitResultType),
                                visitorFieldName(value),
                                Modifier.PRIVATE)
                        .build())
                .collect(Collectors.toList());
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
            ClassName enclosingClass, TypeName visitResultType, Iterable<EnumValueDefinition> values) {
        ImmutableList.Builder<MethodSpec> setterMethods = ImmutableList.builder();
        PeekingIterator<String> valueIter =
                Iterators.peekingIterator(stageEnumNames(values).iterator());
        while (valueIter.hasNext()) {
            String value = valueIter.next();
            String nextBuilderStage = valueIter.hasNext() ? valueIter.peek() : COMPLETED;
            ClassName nextVisitorStageClassName = visitorStageInterfaceName(enclosingClass, nextBuilderStage);
            if (UNKNOWN_TYPE_NAME.equals(value)) {
                setterMethods.addAll(
                        unknownSpecificVisitorSetters(enclosingClass, visitResultType, nextVisitorStageClassName));
            } else {
                MethodSpec.Builder setterPrototype =
                        visitorBuilderSetterPrototype(value, visitResultType, nextVisitorStageClassName);
                setterMethods.add(setterPrototype
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(Override.class)
                        .addStatement(
                                "$L",
                                Expressions.requireNonNull(
                                        visitorFieldName(value),
                                        String.format("%s cannot be null", visitorFieldName(value))))
                        .addStatement("this.$1L = $1L", visitorFieldName(value))
                        .addStatement("return this")
                        .build());
            }
        }
        return setterMethods.build();
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
            String value, TypeName visitResultType, ClassName nextBuilderStage) {
        TypeName visitorObject = visitorObjectTypeName(visitResultType);
        return MethodSpec.methodBuilder(getVisitorMethodName(value))
                .addParameter(ParameterSpec.builder(visitorObject, visitorFieldName(value))
                        .addAnnotation(Nonnull.class)
                        .build())
                .returns(ParameterizedTypeName.get(nextBuilderStage, visitResultType));
    }

    /**
     * Convenience method for generating a {@code Function<MemberType, T>} used for fields and setters of the
     * visitor builder. Special-cases generation of a {@code BiFunction<String, Map<String, Object>, T>} in the case
     * of the new unknownVisitor.
     */
    private static TypeName visitorObjectTypeName(TypeName visitResultType) {
        return ParameterizedTypeName.get(ClassName.get(Supplier.class), visitResultType);
    }

    private static List<MethodSpec> unknownSpecificVisitorSetters(
            ClassName enclosingClass, TypeName visitResultType, ClassName nextVisitorStageClassName) {
        ImmutableList.Builder<MethodSpec> methods = ImmutableList.builder();

        String visitorName = visitorFieldName("unknown");
        methods.add(MethodSpec.methodBuilder("visitUnknown")
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
                        "$L", Expressions.requireNonNull(visitorName, String.format("%s cannot be null", visitorName)))
                .addStatement(
                        "this.$1N = $2L -> $1N.apply($3N)",
                        visitorName,
                        UNKNOWN_TYPE_PARAM_NAME,
                        UNKNOWN_TYPE_PARAM_NAME)
                .addStatement("return this")
                .build());

        // Throw on unknown
        methods.add(visitorBuilderUnknownThrowPrototype(visitResultType, nextVisitorStageClassName)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addStatement(
                        "this.$N = $L -> { throw new $T($S, $T.of($S, $N)); }",
                        visitorFieldName("unknown"),
                        UNKNOWN_TYPE_PARAM_NAME,
                        SafeIllegalArgumentException.class,
                        "Unknown variant of the '" + enclosingClass.simpleName() + "' union",
                        SafeArg.class,
                        UNKNOWN_TYPE_PARAM_NAME,
                        UNKNOWN_TYPE_PARAM_NAME)
                .addStatement("return this")
                .build());

        return methods.build();
    }

    private static MethodSpec.Builder visitorBuilderUnknownThrowPrototype(
            TypeName visitResultType, ClassName nextBuilderStage) {
        return MethodSpec.methodBuilder("throwOnUnknown")
                .returns(ParameterizedTypeName.get(nextBuilderStage, visitResultType));
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
            ClassName visitorClass, TypeName visitResultType, Iterable<EnumValueDefinition> values) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("build")
                .returns(ParameterizedTypeName.get(visitorClass, visitResultType))
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class);

        // Add statements to copy over visitor handlers to local immutable variables.
        values.forEach(value -> builder.addStatement(
                "final $1T $2L = this.$2L",
                visitorObjectTypeName(visitResultType),
                visitorFieldName(value.getValue())));
        builder.addStatement(
                "final $1T $2L = this.$2L",
                ParameterizedTypeName.get(
                        ClassName.get(Function.class),
                        UNKNOWN_MEMBER_TYPE.annotated(
                                AnnotationSpec.builder(Safe.class).build()),
                        visitResultType),
                visitorFieldName(UNKNOWN_TYPE_NAME));

        return builder.addStatement(
                        "return $L",
                        TypeSpec.anonymousClassBuilder("")
                                .addSuperinterface(ParameterizedTypeName.get(visitorClass, visitResultType))
                                .addMethods(allDelegatingVisitorMethods(values, visitResultType))
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
            Iterable<EnumValueDefinition> values, TypeName visitorResultType) {
        return stageEnumNames(values)
                .map(value -> UNKNOWN_TYPE_NAME.equals(value)
                        ? MethodSpec.methodBuilder(getVisitorMethodName(value))
                                .addModifiers(Modifier.PUBLIC)
                                .addAnnotation(Override.class)
                                .addParameter(UNKNOWN_MEMBER_TYPE, UNKNOWN_TYPE_PARAM_NAME)
                                .addStatement("return $N.apply($N)", visitorFieldName(value), UNKNOWN_TYPE_PARAM_NAME)
                                .returns(visitorResultType)
                                .build()
                        : MethodSpec.methodBuilder(getVisitorMethodName(value))
                                .addModifiers(Modifier.PUBLIC)
                                .addAnnotation(Override.class)
                                .addStatement("return $N.get()", visitorFieldName(value))
                                .returns(visitorResultType)
                                .build())
                .collect(Collectors.toList());
    }

    private static MethodSpec generateAcceptVisitMethod(ClassName visitorClass, List<EnumValueDefinition> values) {
        CodeBlock.Builder switchBlock = CodeBlock.builder();
        switchBlock.beginControlFlow("switch (value)");
        for (EnumValueDefinition value : values) {
            switchBlock
                    .add("case $N:\n", value.getValue())
                    .addStatement("return visitor.$L()", getVisitorMethodName(value));
        }
        switchBlock
                .add("default:\n")
                .addStatement("return visitor.$1L(string)", VISIT_UNKNOWN_METHOD_NAME)
                .endControlFlow();
        ParameterizedTypeName parameterizedVisitorClass = ParameterizedTypeName.get(visitorClass, TYPE_VARIABLE);
        boolean anyDeprecatedValues = values.stream()
                .anyMatch(definition -> definition.getDeprecated().isPresent());
        return MethodSpec.methodBuilder("accept")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(parameterizedVisitorClass, "visitor")
                        .build())
                .addTypeVariable(TYPE_VARIABLE)
                .returns(TYPE_VARIABLE)
                .addCode(switchBlock.build())
                .addAnnotations(
                        anyDeprecatedValues
                                ? ImmutableList.of(AnnotationSpec.builder(SuppressWarnings.class)
                                        .addMember("value", "$S", "deprecation")
                                        .build())
                                : ImmutableList.of())
                .build();
    }

    private static String getVisitorMethodName(EnumValueDefinition definition) {
        return getVisitorMethodName(definition.getValue());
    }

    private static String getVisitorMethodName(String value) {
        return VISIT_METHOD_NAME + formatName(value, CaseFormat.UPPER_CAMEL);
    }

    /** Generates the name of the interface of a visitor builder stage. */
    private static ClassName visitorStageInterfaceName(ClassName enclosingClass, String stageName) {
        return enclosingClass.nestedClass(formatName(stageName, CaseFormat.UPPER_CAMEL) + STAGE_VISITOR_BUILDER);
    }

    private static String visitorFieldName(String value) {
        return formatName(value, CaseFormat.LOWER_CAMEL) + VISITOR_FIELD_NAME_SUFFIX;
    }

    private static String formatName(String value, CaseFormat camelCase) {
        String formatted = CaseFormat.UPPER_UNDERSCORE.to(camelCase, value);
        if (value.endsWith("_")) {
            formatted += "_";
        }
        return formatted;
    }

    private static MethodSpec createConstructor(ClassName enumClass) {
        // Note: We generate a two arg constructor that handles both known
        // and unknown variants instead of using separate constructors to avoid
        // jackson's behavior of preferring single string constructors for key
        // deserializers over static factories.
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .addParameter(enumClass, "value")
                .addParameter(ClassName.get(String.class), "string")
                .addStatement("this.value = value")
                .addStatement("this.string = string")
                .build();
    }

    private static MethodSpec createValueOf(ClassName thisClass, List<EnumValueDefinition> values) {
        ParameterSpec param = ParameterSpec.builder(ClassName.get(String.class), "value")
                .addAnnotation(Nonnull.class)
                .addAnnotation(Safe.class)
                .build();

        CodeBlock.Builder parser = CodeBlock.builder().beginControlFlow("switch (upperCasedValue)");
        for (EnumValueDefinition value : values) {
            parser.add("case $S:\n", value.getValue())
                    .indent()
                    .addStatement("return $L", value.getValue())
                    .unindent();
        }
        parser.add("default:\n")
                .indent()
                .addStatement("return new $T(Value.UNKNOWN, upperCasedValue)", thisClass)
                .unindent()
                .endControlFlow();
        boolean anyDeprecatedValues = values.stream()
                .anyMatch(definition -> definition.getDeprecated().isPresent());
        return MethodSpec.methodBuilder("valueOf")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(thisClass)
                .addAnnotation(ConjureAnnotations.delegatingJsonCreator())
                .addAnnotations(
                        anyDeprecatedValues
                                ? ImmutableList.of(AnnotationSpec.builder(SuppressWarnings.class)
                                        .addMember("value", "$S", "deprecation")
                                        .build())
                                : ImmutableList.of())
                .addParameter(param)
                .addStatement("$L", Expressions.requireNonNull(param.name, param.name + " cannot be null"))
                // uppercase param for backwards compatibility
                .addStatement("String upperCasedValue = $N.toUpperCase($T.ROOT)", param, Locale.class)
                .addCode(parser.build())
                .build();
    }

    private static FieldSpec createValuesList(ClassName thisClass, List<EnumValueDefinition> values) {
        CodeBlock arrayValues = values.stream()
                .map(value -> CodeBlock.of("$L", value.getValue()))
                .collect(CodeBlock.joining(", "));
        boolean anyDeprecatedValues = values.stream()
                .anyMatch(definition -> definition.getDeprecated().isPresent());
        return FieldSpec.builder(
                        ParameterizedTypeName.get(ClassName.get(List.class), thisClass),
                        "values",
                        Modifier.PRIVATE,
                        Modifier.STATIC,
                        Modifier.FINAL)
                .initializer(CodeBlock.of(
                        "$T.unmodifiableList($T.asList($L))", Collections.class, Arrays.class, arrayValues))
                .addAnnotations(
                        anyDeprecatedValues
                                ? ImmutableList.of(AnnotationSpec.builder(SuppressWarnings.class)
                                        .addMember("value", "$S", "deprecation")
                                        .build())
                                : ImmutableList.of())
                .build();
    }

    private static MethodSpec createValues(ClassName thisClass) {
        return MethodSpec.methodBuilder("values")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(ParameterizedTypeName.get(ClassName.get(List.class), thisClass))
                .addStatement("return values")
                .build();
    }

    private static MethodSpec createEquals(TypeName thisClass) {
        ParameterSpec other = ParameterSpec.builder(ClassName.OBJECT, "other")
                .addAnnotation(Nullable.class)
                .build();
        return MethodSpec.methodBuilder("equals")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(other)
                .returns(TypeName.BOOLEAN)
                .addStatement(
                        "return (this == $1N) || (this.value == Value.UNKNOWN && $1N instanceof $2T "
                                + "&& this.string.equals((($2T) $1N).string))",
                        other,
                        thisClass)
                .build();
    }

    private static MethodSpec createHashCode() {
        return MethodSpec.methodBuilder("hashCode")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(TypeName.INT)
                .addStatement("return this.string.hashCode()")
                .build();
    }
}
