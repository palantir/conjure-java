/*
 * (c) Copyright 2021 Palantir Technologies Inc. All rights reserved.
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

import com.google.auto.common.MoreElements;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.conjure.java.undertow.annotations.DefaultParamDecoder;
import com.palantir.conjure.java.undertow.annotations.Handle;
import com.palantir.conjure.java.undertow.annotations.ParamDecoders;
import com.palantir.conjure.java.undertow.lib.RequestContext;
import com.palantir.conjure.java.undertow.processor.data.ParameterType.SafeLoggingAnnotation;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.Unsafe;
import com.palantir.logsafe.exceptions.SafeIllegalStateException;
import com.palantir.ri.ResourceIdentifier;
import com.palantir.tokens.auth.AuthHeader;
import com.palantir.tokens.auth.BearerToken;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;
import io.undertow.server.HttpServerExchange;
import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

public final class ParamTypesResolver {

    private static final ImmutableSet<Class<?>> PARAM_ANNOTATION_CLASSES = ImmutableSet.of(
            Handle.Body.class,
            Handle.PathParam.class,
            Handle.PathMultiParam.class,
            Handle.QueryParam.class,
            Handle.Header.class,
            Handle.Cookie.class);
    private static final ImmutableSet<String> SUPPORTED_ANNOTATIONS = Stream.concat(
                    Stream.of(Safe.class, Unsafe.class), PARAM_ANNOTATION_CLASSES.stream())
            .map(Class::getCanonicalName)
            .collect(ImmutableSet.toImmutableSet());

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
            UUID.class.getName(),
            Long.class.getName(),
            OptionalLong.class.getName());

    private static final ImmutableSet<ContainerType> INPUT_TYPES =
            ImmutableSet.of(ContainerType.NONE, ContainerType.LIST);

    private final ResolverContext context;

    public ParamTypesResolver(ResolverContext context) {
        this.context = context;
    }

    @SuppressWarnings("CyclomaticComplexity")
    public Optional<ParameterType> getParameterType(VariableElement variableElement, TypeMirror parameterType) {
        List<AnnotationMirror> paramAnnotationMirrors = new ArrayList<>();
        for (AnnotationMirror annotationMirror : variableElement.getAnnotationMirrors()) {
            TypeElement annotationTypeElement =
                    MoreElements.asType(annotationMirror.getAnnotationType().asElement());
            if (SUPPORTED_ANNOTATIONS.contains(
                    annotationTypeElement.getQualifiedName().toString())) {
                paramAnnotationMirrors.add(annotationMirror);
            }
        }

        List<AnnotationReflector> annotationReflectors = paramAnnotationMirrors.stream()
                .map(ImmutableAnnotationReflector::of)
                .collect(Collectors.toList());

        boolean isSafe = annotationReflectors.stream().anyMatch(annotation -> annotation.isAnnotation(Safe.class));
        boolean isUnsafe = annotationReflectors.stream().anyMatch(annotation -> annotation.isAnnotation(Unsafe.class));
        SafeLoggingAnnotation safeLoggable = isUnsafe
                ? SafeLoggingAnnotation.UNSAFE
                : isSafe ? SafeLoggingAnnotation.SAFE : SafeLoggingAnnotation.UNKNOWN;

        List<AnnotationReflector> otherAnnotationReflectors = annotationReflectors.stream()
                .filter(annotation -> !annotation.isAnnotation(Safe.class) && !annotation.isAnnotation(Unsafe.class))
                .collect(Collectors.toList());

        if (otherAnnotationReflectors.isEmpty()) {
            if (!safeLoggable.equals(SafeLoggingAnnotation.UNKNOWN)) {
                context.reportError(
                        "Parameter type cannot be annotated with safe logging annotations",
                        variableElement,
                        SafeArg.of("type", parameterType));
                return Optional.empty();
            }

            if (context.isSameTypes(parameterType, AuthHeader.class)) {
                return Optional.of(ParameterTypes.authHeader(
                        variableElement.getSimpleName().toString()));
            } else if (context.isSameTypes(parameterType, HttpServerExchange.class)) {
                return Optional.of(ParameterTypes.exchange());
            } else if (context.isSameTypes(parameterType, RequestContext.class)) {
                return Optional.of(ParameterTypes.context());
            } else {
                context.reportError(
                        "At least one annotation should be present or type should be InputStream",
                        variableElement,
                        SafeArg.of("requestBody", InputStream.class),
                        SafeArg.of("supportedAnnotations", PARAM_ANNOTATION_CLASSES));
                return Optional.empty();
            }
        }

        if (otherAnnotationReflectors.size() > 1) {
            context.reportError(
                    "Only single annotation can be used",
                    variableElement,
                    SafeArg.of("annotations", otherAnnotationReflectors));
            return Optional.empty();
        }

        // TODO(ckozak): More validation of values.

        AnnotationReflector annotationReflector = Iterables.getOnlyElement(otherAnnotationReflectors);
        if (annotationReflector.isAnnotation(Handle.Body.class)) {
            return Optional.of(bodyParameter(variableElement, annotationReflector, safeLoggable));
        } else if (annotationReflector.isAnnotation(Handle.Header.class)) {
            return Optional.of(headerParameter(variableElement, annotationReflector, safeLoggable));
        } else if (annotationReflector.isAnnotation(Handle.PathParam.class)) {
            return Optional.of(pathParameter(variableElement, annotationReflector, safeLoggable));
        } else if (annotationReflector.isAnnotation(Handle.PathMultiParam.class)) {
            return Optional.of(pathMultiParameter(variableElement, annotationReflector, safeLoggable));
        } else if (annotationReflector.isAnnotation(Handle.QueryParam.class)) {
            return Optional.of(queryParameter(variableElement, annotationReflector, safeLoggable));
        } else if (annotationReflector.isAnnotation(Handle.Cookie.class)) {
            return cookieParameter(variableElement, annotationReflector, safeLoggable);
        }

        throw new SafeIllegalStateException("Not possible");
    }

    private ParameterType bodyParameter(
            VariableElement variableElement,
            AnnotationReflector annotationReflector,
            SafeLoggingAnnotation safeLoggable) {
        String javaParameterName = variableElement.getSimpleName().toString();
        String deserializerName = InstanceVariables.joinCamelCase(javaParameterName, "Deserializer");
        TypeMirror deserializer = annotationReflector.getAnnotationValue(TypeMirror.class);
        return ParameterTypes.body(
                javaParameterName, Instantiables.instantiate(deserializer), deserializerName, safeLoggable);
    }

    private ParameterType headerParameter(
            VariableElement variableElement,
            AnnotationReflector annotationReflector,
            SafeLoggingAnnotation safeLoggable) {
        String javaParameterName = variableElement.getSimpleName().toString();
        String deserializerName = InstanceVariables.joinCamelCase(javaParameterName, "Deserializer");
        return ParameterTypes.header(
                javaParameterName,
                annotationReflector.getAnnotationValue(String.class),
                deserializerName,
                getCollectionParamDecoder(variableElement, annotationReflector),
                safeLoggable);
    }

    private ParameterType pathParameter(
            VariableElement variableElement,
            AnnotationReflector annotationReflector,
            SafeLoggingAnnotation safeLoggable) {
        String javaParameterName = variableElement.getSimpleName().toString();
        String deserializerName = InstanceVariables.joinCamelCase(javaParameterName, "Deserializer");
        return ParameterTypes.path(
                javaParameterName,
                deserializerName,
                getParamDecoder(variableElement, annotationReflector),
                safeLoggable);
    }

    private ParameterType pathMultiParameter(
            VariableElement variableElement,
            AnnotationReflector annotationReflector,
            SafeLoggingAnnotation safeLoggable) {
        String javaParameterName = variableElement.getSimpleName().toString();
        String deserializerName = InstanceVariables.joinCamelCase(javaParameterName, "Deserializer");
        return ParameterTypes.pathMulti(
                javaParameterName,
                deserializerName,
                getCollectionParamDecoder(variableElement, annotationReflector),
                safeLoggable);
    }

    private ParameterType queryParameter(
            VariableElement variableElement,
            AnnotationReflector annotationReflector,
            SafeLoggingAnnotation safeLoggable) {
        String javaParameterName = variableElement.getSimpleName().toString();
        String deserializerName =
                InstanceVariables.joinCamelCase(variableElement.getSimpleName().toString(), "Deserializer");
        return ParameterTypes.query(
                javaParameterName,
                annotationReflector.getAnnotationValue(String.class),
                deserializerName,
                getCollectionParamDecoder(variableElement, annotationReflector),
                safeLoggable);
    }

    private Optional<ParameterType> cookieParameter(
            VariableElement variableElement,
            AnnotationReflector annotationReflector,
            SafeLoggingAnnotation safeLoggable) {
        String javaParameterName = variableElement.getSimpleName().toString();
        String deserializerName = InstanceVariables.joinCamelCase(javaParameterName, "Deserializer");
        if (context.isSameTypes(variableElement.asType(), BearerToken.class)) {
            if (!safeLoggable.equals(SafeLoggingAnnotation.UNKNOWN)) {
                context.reportError(
                        "BearerToken parameter cannot be annotated with safe logging annotations",
                        variableElement,
                        SafeArg.of("type", variableElement.asType()));
                return Optional.empty();
            }
            // TODO(fwindheuser): Add some validation no more than one BearerToken cookie param is used.
            return Optional.of(ParameterTypes.authCookie(
                    javaParameterName, annotationReflector.getAnnotationValue(String.class), deserializerName));
        }

        return Optional.of(ParameterTypes.cookie(
                javaParameterName,
                annotationReflector.getAnnotationValue(String.class),
                deserializerName,
                getParamDecoder(variableElement, annotationReflector),
                safeLoggable));
    }

    private CodeBlock getParamDecoder(VariableElement variableElement, AnnotationReflector annotationReflector) {
        // If the default marker interface is not used (overwritten by user), we want to use the user-provided decoder.
        TypeMirror typeMirror = annotationReflector.getAnnotationValue("decoder", TypeMirror.class);
        if (!context.isSameTypes(typeMirror, DefaultParamDecoder.class)) {
            return Instantiables.instantiate(typeMirror);
        }

        TypeMirror variableType = variableElement.asType();
        // For param decoders, we don't support list and set container types.
        Optional<TypeMirror> innerOptionalType = context.getGenericInnerType(Optional.class, variableType);
        TypeMirror decoderType = innerOptionalType.orElse(variableType);
        ContainerType decoderOutputType = getOutputType(Optional.empty(), Optional.empty(), innerOptionalType);

        return getDefaultDecoderFactory(decoderType, ContainerType.NONE, decoderOutputType)
                .orElseGet(() -> {
                    context.reportError(
                            "No default decoder exists for parameter. "
                                    + "Types with a valueOf(String) method are supported, as are conjure types",
                            variableElement,
                            SafeArg.of("variableType", variableType),
                            SafeArg.of("supportedTypes", SUPPORTED_CLASSES));
                    return CodeBlock.of("// error");
                });
    }

    private CodeBlock getCollectionParamDecoder(
            VariableElement variableElement, AnnotationReflector annotationReflector) {
        // If the default marker interface is not used (overwritten by user), we want to use the user-provided decoder.
        TypeMirror typeMirror = annotationReflector.getAnnotationValue("decoder", TypeMirror.class);
        if (!context.isSameTypes(typeMirror, DefaultParamDecoder.class)) {
            return Instantiables.instantiate(typeMirror);
        }

        TypeMirror variableType = variableElement.asType();
        Optional<TypeMirror> innerListType = context.getGenericInnerType(List.class, variableType);
        Optional<TypeMirror> innerSetType = context.getGenericInnerType(Set.class, variableType);
        Optional<TypeMirror> innerOptionalType = context.getGenericInnerType(Optional.class, variableType);

        TypeMirror decoderType =
                innerListType.or(() -> innerSetType).or(() -> innerOptionalType).orElse(variableType);
        ContainerType decoderOutputType = getOutputType(innerListType, innerSetType, innerOptionalType);

        return getDefaultDecoderFactory(decoderType, ContainerType.LIST, decoderOutputType)
                .orElseGet(() -> {
                    context.reportError(
                            "No default decoder exists for parameter. "
                                    + "Types with a valueOf(String) method are supported, as are conjure types",
                            variableElement,
                            SafeArg.of("variableType", variableType),
                            SafeArg.of("supportedTypes", SUPPORTED_CLASSES));
                    return CodeBlock.of("// error");
                });
    }

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
    private Optional<CodeBlock> getDefaultDecoderFactory(
            TypeMirror type, ContainerType inputType, ContainerType outType) {
        return getClassNameForTypeMirror(type)
                .filter(SUPPORTED_CLASSES::contains)
                .filter(className -> {
                    // Conjure uses OptionalInt and OptionalDouble, we cannot use these methods
                    // to construct Optional<Integer> and Optional<Double>.
                    boolean isOptionalBoxedConjureType = outType == ContainerType.OPTIONAL
                            && (Integer.class.getName().equals(className)
                                    || Double.class.getName().equals(className)
                                    || Long.class.getName().equals(className));
                    return !isOptionalBoxedConjureType;
                })
                .map(className -> getDefaultDecoderMethodName(className, inputType, outType))
                .map(decoderMethodName ->
                        CodeBlock.of("$T.$L(runtime.plainSerDe())", ParamDecoders.class, decoderMethodName))
                .or(() -> getComplexDecoderFactory(type, inputType, outType));
    }

    private Optional<CodeBlock> getComplexDecoderFactory(
            TypeMirror typeMirror, ContainerType inputType, ContainerType outType) {
        return getUnknownDecoderFactoryFunction(typeMirror)
                .map(factoryFunction -> CodeBlock.of(
                        "$T.$L(runtime.plainSerDe(), $L)",
                        ParamDecoders.class,
                        getDefaultDecoderMethodName(Optional.empty(), inputType, outType),
                        factoryFunction));
    }

    private Optional<CodeBlock> getUnknownDecoderFactoryFunction(TypeMirror typeMirror) {
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
                return getFactoryDecoderFactoryFunction(declaredType, "valueOf")
                        .or(() -> getConstructorDecoderFactoryFunction(declaredType))
                        .or(() -> getFactoryDecoderFactoryFunction(declaredType, "of"))
                        .or(() -> getFactoryDecoderFactoryFunction(declaredType, "fromString"))
                        .or(() -> getFactoryDecoderFactoryFunction(declaredType, "create"));
            default:
                return Optional.empty();
        }
    }

    private Optional<CodeBlock> getFactoryDecoderFactoryFunction(DeclaredType declaredType, String methodName) {
        TypeElement typeElement = (TypeElement) declaredType.asElement();
        // T valueOf(String)
        return typeElement.getEnclosedElements().stream()
                .filter(element -> element.getKind() == ElementKind.METHOD)
                .map(ExecutableElement.class::cast)
                .filter(element -> element.getModifiers().contains(Modifier.PUBLIC)
                        && element.getModifiers().contains(Modifier.STATIC)
                        && element.getSimpleName().contentEquals(methodName)
                        && element.getThrownTypes().stream().allMatch(this::isRuntimeException)
                        && element.getParameters().size() == 1
                        && isString(element.getParameters().get(0).asType())
                        && Objects.equals(TypeName.get(declaredType), TypeName.get(element.getReturnType())))
                .map(_element -> CodeBlock.of("$T::" + methodName, TypeName.get(declaredType)))
                .findFirst();
    }

    private Optional<CodeBlock> getConstructorDecoderFactoryFunction(DeclaredType declaredType) {
        TypeElement typeElement = (TypeElement) declaredType.asElement();
        // new T(String)
        return typeElement.getEnclosedElements().stream()
                .filter(element -> element.getKind() == ElementKind.CONSTRUCTOR)
                .map(ExecutableElement.class::cast)
                .filter(element -> element.getModifiers().contains(Modifier.PUBLIC)
                        && element.getThrownTypes().stream().allMatch(this::isRuntimeException)
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
        String type = className.map(ParamTypesResolver::getTypeName).orElse("Complex");
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

    private boolean isString(TypeMirror typeMirror) {
        return context.isSameTypes(typeMirror, String.class);
    }

    private boolean isRuntimeException(TypeMirror typeMirror) {
        return context.isAssignable(typeMirror, RuntimeException.class);
    }

    private static ContainerType getOutputType(
            Optional<TypeMirror> listType, Optional<TypeMirror> setType, Optional<TypeMirror> optionalType) {
        if (listType.isPresent()) {
            return ContainerType.LIST;
        } else if (setType.isPresent()) {
            return ContainerType.SET;
        } else if (optionalType.isPresent()) {
            return ContainerType.OPTIONAL;
        }
        return ContainerType.NONE;
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
}
