/*
 * (c) Copyright 2020 Palantir Technologies Inc. All rights reserved.
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

import com.google.common.base.CaseFormat;
import com.google.common.collect.Streams;
import com.palantir.conjure.java.ConjureAnnotations;
import com.palantir.conjure.java.Generator;
import com.palantir.conjure.java.Options;
import com.palantir.conjure.java.api.errors.CheckedServiceException;
import com.palantir.conjure.java.api.errors.ErrorType;
import com.palantir.conjure.java.api.errors.ServiceException;
import com.palantir.conjure.java.util.Javadoc;
import com.palantir.conjure.java.util.Packages;
import com.palantir.conjure.java.util.TypeFunctions;
import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.conjure.spec.ErrorDefinition;
import com.palantir.conjure.spec.ErrorNamespace;
import com.palantir.conjure.spec.FieldDefinition;
import com.palantir.conjure.spec.TypeDefinition;
import com.palantir.logsafe.Arg;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.UnsafeArg;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import javax.lang.model.element.Modifier;
import org.apache.commons.lang3.StringUtils;

public final class ErrorGenerator implements Generator {

    private final Options options;

    public ErrorGenerator(Options options) {
        this.options = options;
    }

    @Override
    public Stream<JavaFile> generate(ConjureDefinition definition) {
        List<TypeDefinition> types = definition.getTypes();
        TypeMapper typeMapper = new TypeMapper(TypeFunctions.toTypesMap(types), options);
        Set<com.palantir.conjure.spec.TypeName> checkedExceptions = findCheckedExceptions(definition);
        return splitErrorDefsByNamespace(definition.getErrors()).entrySet().stream()
                .flatMap(entry -> entry.getValue().entrySet().stream()
                        .map(innerEntry -> generateErrorTypesForNamespace(
                                typeMapper,
                                Packages.getPrefixedPackage(entry.getKey(), options.packagePrefix()),
                                innerEntry.getKey(),
                                innerEntry.getValue(),
                                e -> checkedExceptions.contains(e.getErrorName()))));
    }

    private static Set<com.palantir.conjure.spec.TypeName> findCheckedExceptions(ConjureDefinition definition) {
        return definition.getServices().stream()
                .flatMap(stream -> stream.getEndpoints().stream())
                .flatMap(endpoint -> endpoint.getErrors().stream())
                .collect(Collectors.toSet());
    }

    private static Map<String, Map<ErrorNamespace, List<ErrorDefinition>>> splitErrorDefsByNamespace(
            List<ErrorDefinition> errorTypeNameToDef) {
        Map<String, Map<ErrorNamespace, List<ErrorDefinition>>> pkgToNamespacedErrorDefs = new HashMap<>();
        errorTypeNameToDef.forEach(errorDef -> {
            String errorPkg = errorDef.getErrorName().getPackage();
            pkgToNamespacedErrorDefs.computeIfAbsent(errorPkg, key -> new HashMap<>());

            Map<ErrorNamespace, List<ErrorDefinition>> namespacedErrorDefs = pkgToNamespacedErrorDefs.get(errorPkg);
            ErrorNamespace namespace = errorDef.getNamespace();
            // TODO(rfink): Use Multimap?
            namespacedErrorDefs.computeIfAbsent(namespace, key -> new ArrayList<>());
            namespacedErrorDefs.get(namespace).add(errorDef);
        });
        return pkgToNamespacedErrorDefs;
    }

    private static JavaFile generateErrorTypesForNamespace(
            TypeMapper typeMapper,
            String conjurePackage,
            ErrorNamespace namespace,
            List<ErrorDefinition> errorTypeDefinitions,
            Predicate<ErrorDefinition> shouldBeChecked) {
        ClassName className = errorTypesClassName(conjurePackage, namespace);

        // Generate ErrorType definitions
        List<FieldSpec> fieldSpecs = errorTypeDefinitions.stream()
                .map(errorDef -> {
                    CodeBlock initializer = CodeBlock.of(
                            "ErrorType.create(ErrorType.Code.$L, \"$L:$L\")",
                            errorDef.getCode().get(),
                            namespace.get(),
                            errorDef.getErrorName().getName());
                    FieldSpec.Builder fieldSpecBuilder = FieldSpec.builder(
                                    ClassName.get(ErrorType.class),
                                    CaseFormat.UPPER_CAMEL.to(
                                            CaseFormat.UPPER_UNDERSCORE,
                                            errorDef.getErrorName().getName()),
                                    Modifier.PUBLIC,
                                    Modifier.STATIC,
                                    Modifier.FINAL)
                            .initializer(initializer);
                    errorDef.getDocs().ifPresent(docs -> fieldSpecBuilder.addJavadoc(docs.get()));
                    return fieldSpecBuilder.build();
                })
                .collect(Collectors.toList());

        Map<ErrorDefinition, TypeSpec> checkedExceptions = errorTypeDefinitions.stream()
                .filter(shouldBeChecked)
                .collect(Collectors.toMap(Function.identity(), entry -> TypeSpec.classBuilder(String.format(
                                "%sException", entry.getErrorName().getName()))
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                        .superclass(CheckedServiceException.class)
                        .addMethod(MethodSpec.constructorBuilder()
                                .addParameter(ClassName.get(ErrorType.class), "errorType")
                                .addParameter(
                                        ArrayTypeName.of(ParameterizedTypeName.get(
                                                ClassName.get(Arg.class), WildcardTypeName.subtypeOf(Object.class))),
                                        "parameters")
                                .varargs()
                                .addStatement("super($N, $N)", "errorType", "parameters")
                                .build())
                        .addMethod(MethodSpec.constructorBuilder()
                                .addParameter(ClassName.get(ErrorType.class), "errorType")
                                .addParameter(ParameterSpec.builder(ClassName.get(Throwable.class), "cause")
                                        .addAnnotation(Nullable.class)
                                        .build())
                                .addParameter(
                                        ArrayTypeName.of(ParameterizedTypeName.get(
                                                ClassName.get(Arg.class), WildcardTypeName.subtypeOf(Object.class))),
                                        "parameters")
                                .varargs()
                                .addStatement("super($N, $N, $N)", "errorType", "cause", "parameters")
                                .build())
                        .build()));

        // Generate ServiceException factory methods
        List<MethodSpec> methodSpecs = errorTypeDefinitions.stream()
                .flatMap(entry -> {
                    boolean isChecked = shouldBeChecked.test(entry);
                    ClassName exceptionClazz = isChecked
                            ? className.nestedClass(checkedExceptions.get(entry).name)
                            : ClassName.get(ServiceException.class);
                    MethodSpec withoutCause = generateExceptionFactory(typeMapper, entry, false, exceptionClazz);
                    MethodSpec withCause = generateExceptionFactory(typeMapper, entry, true, exceptionClazz);
                    return Stream.of(withoutCause, withCause);
                })
                .collect(Collectors.toList());

        // Generate ServiceException factory check methods
        List<MethodSpec> checkMethodSpecs = errorTypeDefinitions.stream()
                .map(entry -> {
                    String exceptionMethodName = CaseFormat.UPPER_CAMEL.to(
                            CaseFormat.LOWER_CAMEL, entry.getErrorName().getName());
                    String methodName = "throwIf" + entry.getErrorName().getName();

                    String shouldThrowVar = "shouldThrow";

                    MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(methodName)
                            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                            .addParameter(TypeName.BOOLEAN, shouldThrowVar);

                    methodBuilder.addJavadoc(
                            "Throws a {@link ServiceException} of type $L when {@code $L} is true.\n",
                            entry.getErrorName().getName(),
                            shouldThrowVar);
                    methodBuilder.addJavadoc("@param $L $L\n", shouldThrowVar, "Cause the method to throw when true");

                    if (shouldBeChecked.test(entry)) {
                        ClassName exception = className.nestedClass(checkedExceptions.get(entry).name);
                        methodBuilder.addException(exception);
                    }

                    Streams.concat(entry.getSafeArgs().stream(), entry.getUnsafeArgs().stream())
                            .forEach(arg -> {
                                methodBuilder.addParameter(
                                        typeMapper.getClassName(arg.getType()),
                                        arg.getFieldName().get());
                                methodBuilder.addJavadoc(
                                        "@param $L $L",
                                        arg.getFieldName().get(),
                                        StringUtils.appendIfMissing(
                                                arg.getDocs()
                                                        .map(Javadoc::render)
                                                        .orElse(""),
                                                "\n"));
                            });

                    methodBuilder.addCode("if ($L) {", shouldThrowVar);
                    methodBuilder.addCode(
                            "throw $L;",
                            Expressions.localMethodCall(
                                    exceptionMethodName,
                                    Streams.concat(entry.getSafeArgs().stream(), entry.getUnsafeArgs().stream())
                                            .map(arg -> arg.getFieldName().get())
                                            .collect(Collectors.toList())));
                    methodBuilder.addCode("}");
                    return methodBuilder.build();
                })
                .collect(Collectors.toList());

        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(className)
                .addMethod(privateConstructor())
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addFields(fieldSpecs)
                .addTypes(checkedExceptions.values())
                .addMethods(methodSpecs)
                .addMethods(checkMethodSpecs)
                .addAnnotation(ConjureAnnotations.getConjureGeneratedAnnotation(ErrorGenerator.class));

        return JavaFile.builder(conjurePackage, typeBuilder.build())
                .skipJavaLangImports(true)
                .indent("    ")
                .build();
    }

    private static MethodSpec generateExceptionFactory(
            TypeMapper typeMapper, ErrorDefinition entry, boolean withCause, ClassName exceptionClazz) {
        String methodName = CaseFormat.UPPER_CAMEL.to(
                CaseFormat.LOWER_CAMEL, entry.getErrorName().getName());
        String typeName = CaseFormat.UPPER_CAMEL.to(
                CaseFormat.UPPER_UNDERSCORE, entry.getErrorName().getName());

        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(exceptionClazz);

        methodBuilder.addCode("return new $T($L", exceptionClazz, typeName);

        if (withCause) {
            methodBuilder.addParameter(Throwable.class, "cause");
            methodBuilder.addCode(", cause");
        }

        entry.getSafeArgs().forEach(arg -> processArg(typeMapper, methodBuilder, arg, true));

        entry.getUnsafeArgs().forEach(arg -> processArg(typeMapper, methodBuilder, arg, false));
        methodBuilder.addCode(");");

        return methodBuilder.build();
    }

    private static void processArg(
            TypeMapper typeMapper, MethodSpec.Builder methodBuilder, FieldDefinition argDefinition, boolean isSafe) {
        String argName = argDefinition.getFieldName().get();
        TypeName argType = typeMapper.getClassName(argDefinition.getType());
        methodBuilder.addParameter(argType, argName);
        Class<?> clazz = isSafe ? SafeArg.class : UnsafeArg.class;
        methodBuilder.addCode(",\n    $T.of($S, $L)", clazz, argName, argName);
        argDefinition
                .getDocs()
                .ifPresent(docs -> methodBuilder.addJavadoc("@param $L $L", argName, Javadoc.render(docs)));
    }

    private static ClassName errorTypesClassName(String conjurePackage, ErrorNamespace namespace) {
        return ClassName.get(conjurePackage, namespace.get() + "Errors");
    }

    private static MethodSpec privateConstructor() {
        return MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE).build();
    }
}
