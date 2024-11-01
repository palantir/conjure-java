/*
 * (c) Copyright 2024 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java.services;

import com.google.common.base.CaseFormat;
import com.palantir.conjure.java.ConjureAnnotations;
import com.palantir.conjure.java.Generator;
import com.palantir.conjure.java.Options;
import com.palantir.conjure.java.types.TypeMapper;
import com.palantir.conjure.java.undertow.lib.CheckedServiceException;
import com.palantir.conjure.java.util.Javadoc;
import com.palantir.conjure.java.util.Packages;
import com.palantir.conjure.java.util.TypeFunctions;
import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.conjure.spec.ErrorDefinition;
import com.palantir.conjure.spec.ErrorNamespace;
import com.palantir.conjure.spec.FieldDefinition;
import com.palantir.conjure.spec.LogSafety;
import com.palantir.conjure.spec.TypeDefinition;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.UnsafeArg;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import javax.lang.model.element.Modifier;
import org.apache.commons.lang3.StringUtils;

public final class CheckedErrorGenerator implements Generator {

    private final Options options;

    public CheckedErrorGenerator(Options options) {
        this.options = options;
    }

    @Override
    public Stream<JavaFile> generate(ConjureDefinition definition) {
        Map<com.palantir.conjure.spec.TypeName, TypeDefinition> types = TypeFunctions.toTypesMap(definition);
        TypeMapper typeMapper = new TypeMapper(types, options);
        return splitErrorDefsByNamespace(definition.getErrors()).entrySet().stream()
                .flatMap(entry -> entry.getValue().entrySet().stream()
                        .flatMap(innerEntry -> Stream.of(generateErrorExceptionsForNamespace(
                                typeMapper,
                                Packages.getPrefixedPackage(entry.getKey(), options.packagePrefix()),
                                innerEntry.getKey(),
                                innerEntry.getValue()))));
    }

    // TODO(pm): move this to a common utility to share between this file and the ErrorGenerator.
    private static Map<String, Map<ErrorNamespace, List<ErrorDefinition>>> splitErrorDefsByNamespace(
            List<ErrorDefinition> errorTypeNameToDef) {
        Map<String, Map<ErrorNamespace, List<ErrorDefinition>>> pkgToNamespacedErrorDefs = new HashMap<>();
        errorTypeNameToDef.forEach(errorDef -> {
            String errorPkg = errorDef.getErrorName().getPackage();
            pkgToNamespacedErrorDefs.computeIfAbsent(errorPkg, key -> new HashMap<>());

            Map<ErrorNamespace, List<ErrorDefinition>> namespacedErrorDefs = pkgToNamespacedErrorDefs.get(errorPkg);
            ErrorNamespace namespace = errorDef.getNamespace();
            namespacedErrorDefs.computeIfAbsent(namespace, key -> new ArrayList<>());
            namespacedErrorDefs.get(namespace).add(errorDef);
        });
        return pkgToNamespacedErrorDefs;
    }

    private JavaFile generateErrorExceptionsForNamespace(
            TypeMapper typeMapper,
            String conjurePackage,
            ErrorNamespace namespace,
            List<ErrorDefinition> errorDefinitions) {
        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(errorExceptionsClassName(conjurePackage, namespace))
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(privateConstructor())
                .addTypes(errorDefinitions.stream()
                        .map(def -> generateErrorException(typeMapper, conjurePackage, namespace, def))
                        .toList())
                .addAnnotation(ConjureAnnotations.getConjureGeneratedAnnotation(CheckedErrorGenerator.class));
        return JavaFile.builder(conjurePackage, typeBuilder.build())
                .skipJavaLangImports(true)
                .indent("    ")
                .build();
    }

    private TypeSpec generateErrorException(
            TypeMapper typeMapper, String conjurePackage, ErrorNamespace namespace, ErrorDefinition errorDefinition) {
        MethodSpec.Builder methodBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addCode(
                        "super($L.$L",
                        errorTypesClassName(conjurePackage, namespace),
                        CaseFormat.UPPER_CAMEL.to(
                                CaseFormat.UPPER_UNDERSCORE,
                                errorDefinition.getErrorName().getName()));
        errorDefinition.getSafeArgs().forEach(arg -> processArg(typeMapper, methodBuilder, arg, true));
        errorDefinition.getUnsafeArgs().forEach(arg -> processArg(typeMapper, methodBuilder, arg, false));
        methodBuilder.addCode(");");
        // TODO(pm): I don't think we need to append "Exception" here. E.g. throw RecipeNotGood(...) is fine. Doesn't
        //  need to be RecipeNotGoodException. The fact that it's an Exception is apparent.
        return TypeSpec.classBuilder(errorDefinition.getErrorName().getName())
                .superclass(CheckedServiceException.class)
                .addMethod(methodBuilder.build())
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .build();
    }

    // TODO(pm): move to utils.
    private static void processArg(
            TypeMapper typeMapper, MethodSpec.Builder methodBuilder, FieldDefinition argDefinition, boolean isSafe) {
        Optional<LogSafety> safety = Optional.of(isSafe ? LogSafety.SAFE : LogSafety.UNSAFE);
        String argName = argDefinition.getFieldName().get();
        TypeName argType = ConjureAnnotations.withSafety(typeMapper.getClassName(argDefinition.getType()), safety);
        ParameterSpec.Builder parameterBuilder = ParameterSpec.builder(argType, argName);
        argDefinition
                .getDocs()
                .ifPresent(docs ->
                        parameterBuilder.addJavadoc("$L", StringUtils.appendIfMissing(Javadoc.render(docs), "\n")));
        methodBuilder.addParameter(parameterBuilder.build());
        Class<?> clazz = isSafe ? SafeArg.class : UnsafeArg.class;
        methodBuilder.addCode(",\n    $T.of($S, $L)", clazz, argName, argName);
    }

    // TODO(pm): move this to a utility class.
    private static ClassName errorTypesClassName(String conjurePackage, ErrorNamespace namespace) {
        return ClassName.get(conjurePackage, namespace.get() + "Errors");
    }

    private static ClassName errorExceptionsClassName(String conjurePackage, ErrorNamespace namespace) {
        return ClassName.get(conjurePackage, "Server" + namespace.get() + "Errors");
    }

    // TODO(pm): move this to a utility class.
    private static MethodSpec privateConstructor() {
        return MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE).build();
    }
}
