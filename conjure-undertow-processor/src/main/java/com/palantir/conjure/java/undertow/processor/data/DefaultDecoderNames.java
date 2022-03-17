/*
 * (c) Copyright 2022 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java.undertow.processor.data;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.conjure.java.undertow.annotations.ParamDecoders;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.ri.ResourceIdentifier;
import com.palantir.tokens.auth.BearerToken;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.UUID;
import java.util.stream.Stream;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

final class DefaultDecoderNames {
    /** Mirrored from {@link ParamDecoders}. */
    @VisibleForTesting
    static final ImmutableList<String> SUPPORTED_CLASSES = ImmutableList.of(
            String.class.getName(),
            Boolean.class.getName(),
            BearerToken.class.getName(),
            OffsetDateTime.class.getName(),
            Double.class.getName(),
            OptionalDouble.class.getName(),
            Integer.class.getName(),
            OptionalInt.class.getName(),
            ResourceIdentifier.class.getName(),
            SafeLong.class.getName(),
            UUID.class.getName());

    private static final ImmutableSet<ContainerType> INPUT_TYPES =
            ImmutableSet.of(ContainerType.NONE, ContainerType.LIST);

    /**
     * Generates the corresponding factory invocation for a default decoder as declared in
     * {@link com.palantir.conjure.java.undertow.annotations.ParamDecoders}.
     *
     * E.g. calling {@code getDefaultDecoderMethodName(String, LIST, OPTIONAL} will return
     * 'optionalStringCollectionParamDecoder' as the respective factory method name for
     * {@code CollectionParamDecoder<Optional<String>>}.
     *
     * @param type the type of the encoder, e.g. 'string' or 'rid'.
     * @param inputType the container type used as input for the encoder, 'LIST' or 'NONE'.
     * @param outType the container type used as output for the encoder, 'LIST', 'SET', 'OPTIONAL', or 'NONE'.
     */
    static Optional<CodeBlock> getDefaultDecoderFactory(
            TypeMirror type, ContainerType inputType, ContainerType outType) {
        return getClassNameForTypeMirror(type)
                .filter(SUPPORTED_CLASSES::contains)
                .filter(className -> {
                    // Conjure uses OptionalInt and OptionalDouble, we cannot use these methods
                    // to construct Optional<Integer> and Optional<Double>.
                    boolean isOptionalBoxedConjureType = outType == ContainerType.OPTIONAL
                            && (Integer.class.getName().equals(className)
                                    || Double.class.getName().equals(className));
                    return !isOptionalBoxedConjureType;
                })
                .map(className -> getDefaultDecoderMethodName(className, inputType, outType))
                .map(decoderMethodName ->
                        CodeBlock.of("$T.$L(runtime.plainSerDe())", ParamDecoders.class, decoderMethodName))
                .or(() -> getComplexDecoderFactory(type, inputType, outType));
    }

    private static Optional<CodeBlock> getComplexDecoderFactory(
            TypeMirror typeMirror, ContainerType inputType, ContainerType outType) {
        return getUnknownDecoderFactoryFunction(typeMirror)
                .map(factoryFunction -> CodeBlock.of(
                        "$T.$L(runtime.plainSerDe(), $L)",
                        ParamDecoders.class,
                        getDefaultDecoderMethodName(Optional.empty(), inputType, outType),
                        factoryFunction));
    }

    static Optional<CodeBlock> getUnknownDecoderFactoryFunction(TypeMirror typeMirror) {
        // No need to handle int/double/boolean because they're covered by default handlers
        switch (typeMirror.getKind()) {
            case FLOAT:
                return Optional.of(CodeBlock.of("$T::parseFloat", Float.class));
            case LONG:
                return Optional.of(CodeBlock.of("$T::parseLong", Long.class));
            case BYTE:
                return Optional.of(CodeBlock.of("$T::parseByte", Byte.class));
            case SHORT:
                return Optional.of(CodeBlock.of("$T::parseShort", Short.class));
            case DECLARED:
                DeclaredType declaredType = (DeclaredType) typeMirror;
                return getValueOfDecoderFactoryFunction(declaredType)
                        .or(() -> getStringConstructorDecoderFactoryFunction(declaredType));
            default:
                return Optional.empty();
        }
    }

    private static Optional<CodeBlock> getValueOfDecoderFactoryFunction(DeclaredType declaredType) {
        TypeElement typeElement = (TypeElement) declaredType.asElement();
        // T valueOf(String)
        return typeElement.getEnclosedElements().stream()
                .filter(element -> element.getKind() == ElementKind.METHOD)
                .map(ExecutableElement.class::cast)
                .filter(element -> element.getModifiers().contains(Modifier.PUBLIC)
                        && element.getModifiers().contains(Modifier.STATIC)
                        && element.getSimpleName().contentEquals("valueOf")
                        && element.getParameters().size() == 1
                        && isStringMirror(element.getParameters().get(0).asType())
                        && Objects.equals(TypeName.get(declaredType), TypeName.get(element.getReturnType())))
                .map(_element -> CodeBlock.of("$T::valueOf", TypeName.get(declaredType)))
                .findFirst();
    }

    private static Optional<CodeBlock> getStringConstructorDecoderFactoryFunction(DeclaredType declaredType) {
        TypeElement typeElement = (TypeElement) declaredType.asElement();
        // new T(String)
        return typeElement.getEnclosedElements().stream()
                .filter(element -> element.getKind() == ElementKind.CONSTRUCTOR)
                .map(ExecutableElement.class::cast)
                .filter(element -> element.getModifiers().contains(Modifier.PUBLIC)
                        && element.getParameters().size() == 1
                        && TypeName.get(element.getParameters().get(0).asType()).equals(ClassName.get(String.class)))
                .map(_element -> CodeBlock.of("$T::new", TypeName.get(declaredType)))
                .findFirst();
    }

    @VisibleForTesting
    static String getDefaultDecoderMethodName(String className, ContainerType inputType, ContainerType outType) {
        return getDefaultDecoderMethodName(Optional.of(className), inputType, outType);
    }

    @VisibleForTesting
    static String getDefaultDecoderMethodName(
            Optional<String> className, ContainerType inputType, ContainerType outType) {
        Preconditions.checkState(
                INPUT_TYPES.contains(inputType),
                "Only list is allowed as container for encoders",
                SafeArg.of("type", inputType));

        String optionalPrefix = outType.equals(ContainerType.OPTIONAL) ? "optional" : "";
        String type = className.map(DefaultDecoderNames::getTypeName).orElse("Complex");
        String decoderSuffix = inputType.equals(ContainerType.LIST) ? "CollectionParamDecoder" : "ParamDecoder";
        String typeSuffix =
                outType.equals(ContainerType.LIST) || outType.equals(ContainerType.SET) ? outType.toString() : "";

        String[] segments = Stream.of(optionalPrefix, type, typeSuffix, decoderSuffix)
                .filter(segment -> !segment.isEmpty())
                .toArray(String[]::new);
        return InstanceVariables.joinCamelCase(segments);
    }

    private static String getTypeName(String className) {
        // We have a few special cases, where we don't want to use the full class name.
        if (className.equals(ResourceIdentifier.class.getName())) {
            return "rid";
        } else if (className.equals(OffsetDateTime.class.getName())) {
            return "dateTime";
        } else if (className.equals(OptionalInt.class.getName())) {
            return "optionalInteger";
        } else {
            int lastDelimiterIndex = className.lastIndexOf('.');
            return lastDelimiterIndex < 0 ? className : className.substring(lastDelimiterIndex + 1);
        }
    }

    private static Optional<String> getClassNameForTypeMirror(TypeMirror typeMirror) {
        // Only need to support primitives that are also supported by {@link PlainSerDe}.
        switch (typeMirror.getKind()) {
            case INT:
                return Optional.of(Integer.class.getName());
            case DOUBLE:
                return Optional.of(Double.class.getName());
            case BOOLEAN:
                return Optional.of(Boolean.class.getName());
            case DECLARED:
                DeclaredType declaredType = (DeclaredType) typeMirror;
                TypeElement typeElement = (TypeElement) declaredType.asElement();
                return Optional.of(typeElement.getQualifiedName().toString());
            default:
                return Optional.empty();
        }
    }

    private static boolean isStringMirror(TypeMirror typeMirror) {
        return ClassName.get(String.class).equals(TypeName.get(typeMirror));
    }

    enum ContainerType {
        NONE(""),
        OPTIONAL("optional"),
        LIST("list"),
        SET("set");

        private final String value;

        ContainerType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    private DefaultDecoderNames() {}
}
