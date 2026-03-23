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
package com.palantir.conjure.java.services.dialogue;

import com.palantir.conjure.java.ConjureAnnotations;
import com.palantir.conjure.java.Options;
import com.palantir.conjure.java.util.ErrorGenerationUtils;
import com.palantir.conjure.java.util.ErrorGenerationUtils.NamespacedErrors;
import com.palantir.conjure.java.util.Packages;
import com.palantir.conjure.spec.ErrorDefinition;
import com.palantir.dialogue.TypeMarker;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.FieldSpec;
import com.palantir.javapoet.JavaFile;
import com.palantir.javapoet.ParameterizedTypeName;
import com.palantir.javapoet.TypeSpec;
import java.util.List;
import java.util.stream.Stream;
import javax.lang.model.element.Modifier;

/**
 * Generates a {@code TypeMarker} holder class per error namespace in the dialogue output,
 * so that dialogue service interfaces can reference static {@code TypeMarker} fields
 * instead of creating anonymous classes at each usage site.
 */
final class ErrorTypeMarkersGenerator {

    private final Options options;

    ErrorTypeMarkersGenerator(Options options) {
        this.options = options;
    }

    Stream<JavaFile> generate(List<ErrorDefinition> errorDefinitions) {
        return ErrorGenerationUtils.getNamespacedErrorsFromDefinitions(errorDefinitions).stream()
                .map(this::generateTypeMarkersClass);
    }

    private JavaFile generateTypeMarkersClass(NamespacedErrors namespacedErrors) {
        String conjurePackage = Packages.getPrefixedPackage(namespacedErrors.javaPackage(), options.packagePrefix());
        String errorsClassName = ErrorGenerationUtils.errorTypesClassName(namespacedErrors.namespace());
        String typeMarkersClassName = errorsClassName + "TypeMarkers";
        ClassName errorsClass = ClassName.get(conjurePackage, errorsClassName);

        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(typeMarkersClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addAnnotation(ConjureAnnotations.getConjureGeneratedAnnotation(ErrorTypeMarkersGenerator.class));

        for (ErrorDefinition errorDef : namespacedErrors.errors()) {
            String errorName = errorDef.getErrorName().getName();

            String serializableErrorName = ErrorGenerationUtils.serializableErrorClassName(errorName);
            ClassName serializableErrorClass = errorsClass.nestedClass(serializableErrorName);
            classBuilder.addField(typeMarkerField(serializableErrorName, serializableErrorClass));

            String exceptionName = ErrorGenerationUtils.errorExceptionClassName(errorName);
            ClassName exceptionClass = errorsClass.nestedClass(exceptionName);
            classBuilder.addField(typeMarkerField(exceptionName, exceptionClass));
        }

        return JavaFile.builder(conjurePackage, classBuilder.build())
                .skipJavaLangImports(true)
                .indent("    ")
                .build();
    }

    private static FieldSpec typeMarkerField(String name, ClassName type) {
        return FieldSpec.builder(
                        ParameterizedTypeName.get(ClassName.get(TypeMarker.class), type),
                        name,
                        Modifier.PUBLIC,
                        Modifier.STATIC,
                        Modifier.FINAL)
                .initializer("new $T<$T>() {}", TypeMarker.class, type)
                .build();
    }
}
