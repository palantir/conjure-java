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
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.palantir.conjure.java.undertow.annotations.DefaultCollectionParamDecoder;
import com.palantir.conjure.java.undertow.annotations.DefaultParamDecoder;
import com.palantir.conjure.java.undertow.annotations.Handle;
import com.palantir.conjure.java.undertow.lib.RequestContext;
import com.palantir.conjure.java.undertow.processor.data.DefaultDecoderNames.ContainerType;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalStateException;
import com.palantir.tokens.auth.AuthHeader;
import com.palantir.tokens.auth.BearerToken;
import com.squareup.javapoet.CodeBlock;
import io.undertow.server.HttpServerExchange;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

public final class ParamTypesResolver {

    private static final ImmutableSet<Class<?>> PARAM_ANNOTATION_CLASSES = ImmutableSet.of(
            Handle.Body.class,
            Handle.PathParam.class,
            Handle.PathMultiParam.class,
            Handle.QueryParam.class,
            Handle.Header.class,
            Handle.Cookie.class);
    private static final ImmutableSet<String> PARAM_ANNOTATIONS =
            PARAM_ANNOTATION_CLASSES.stream().map(Class::getCanonicalName).collect(ImmutableSet.toImmutableSet());

    private final ResolverContext context;

    public ParamTypesResolver(ResolverContext context) {
        this.context = context;
    }

    @SuppressWarnings("CyclomaticComplexity")
    public Optional<ParameterType> getParameterType(EndpointName endpointName, VariableElement variableElement) {
        List<AnnotationMirror> paramAnnotationMirrors = new ArrayList<>();
        for (AnnotationMirror annotationMirror : variableElement.getAnnotationMirrors()) {
            TypeElement annotationTypeElement =
                    MoreElements.asType(annotationMirror.getAnnotationType().asElement());
            if (PARAM_ANNOTATIONS.contains(
                    annotationTypeElement.getQualifiedName().toString())) {
                paramAnnotationMirrors.add(annotationMirror);
            }
        }

        if (paramAnnotationMirrors.isEmpty()) {
            if (context.isSameTypes(variableElement.asType(), AuthHeader.class)) {
                return Optional.of(ParameterTypes.authHeader());
            } else if (context.isSameTypes(variableElement.asType(), HttpServerExchange.class)) {
                return Optional.of(ParameterTypes.exchange());
            } else if (context.isSameTypes(variableElement.asType(), RequestContext.class)) {
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

        if (paramAnnotationMirrors.size() > 1) {
            context.reportError(
                    "Only single annotation can be used",
                    variableElement,
                    SafeArg.of("annotations", paramAnnotationMirrors));
            return Optional.empty();
        }

        // TODO(ckozak): More validation of values.

        AnnotationReflector annotationReflector =
                ImmutableAnnotationReflector.of(Iterables.getOnlyElement(paramAnnotationMirrors));
        if (annotationReflector.isAnnotation(Handle.Body.class)) {
            return Optional.of(bodyParameter(endpointName, annotationReflector));
        } else if (annotationReflector.isAnnotation(Handle.Header.class)) {
            return Optional.of(headerParameter(variableElement, annotationReflector));
        } else if (annotationReflector.isAnnotation(Handle.PathParam.class)) {
            return Optional.of(pathParameter(variableElement, annotationReflector));
        } else if (annotationReflector.isAnnotation(Handle.PathMultiParam.class)) {
            return Optional.of(pathMultiParameter(variableElement, annotationReflector));
        } else if (annotationReflector.isAnnotation(Handle.QueryParam.class)) {
            return Optional.of(queryParameter(variableElement, annotationReflector));
        } else if (annotationReflector.isAnnotation(Handle.Cookie.class)) {
            return Optional.of(cookieParameter(variableElement, annotationReflector));
        }

        throw new SafeIllegalStateException("Not possible");
    }

    private ParameterType bodyParameter(EndpointName endpointName, AnnotationReflector annotationReflector) {
        String deserializerName = InstanceVariables.joinCamelCase(endpointName.get(), "Deserializer");
        TypeMirror deserializer = annotationReflector.getAnnotationValue(TypeMirror.class);
        return ParameterTypes.body(Instantiables.instantiate(deserializer), deserializerName);
    }

    private ParameterType headerParameter(VariableElement variableElement, AnnotationReflector annotationReflector) {
        String deserializerName =
                InstanceVariables.joinCamelCase(variableElement.getSimpleName().toString(), "Deserializer");
        return ParameterTypes.header(
                annotationReflector.getAnnotationValue(String.class),
                deserializerName,
                getCollectionParamDecoder(variableElement, annotationReflector));
    }

    private ParameterType pathParameter(VariableElement variableElement, AnnotationReflector annotationReflector) {
        String javaParameterName = variableElement.getSimpleName().toString();
        String deserializerName = InstanceVariables.joinCamelCase(javaParameterName, "Deserializer");
        return ParameterTypes.path(
                javaParameterName, deserializerName, getParamDecoder(variableElement, annotationReflector));
    }

    private ParameterType pathMultiParameter(VariableElement variableElement, AnnotationReflector annotationReflector) {
        String javaParameterName = variableElement.getSimpleName().toString();
        String deserializerName = InstanceVariables.joinCamelCase(javaParameterName, "Deserializer");
        return ParameterTypes.pathMulti(
                javaParameterName, deserializerName, getCollectionParamDecoder(variableElement, annotationReflector));
    }

    private ParameterType queryParameter(VariableElement variableElement, AnnotationReflector annotationReflector) {
        String deserializerName =
                InstanceVariables.joinCamelCase(variableElement.getSimpleName().toString(), "Deserializer");
        return ParameterTypes.query(
                annotationReflector.getAnnotationValue(String.class),
                deserializerName,
                getCollectionParamDecoder(variableElement, annotationReflector));
    }

    private ParameterType cookieParameter(VariableElement variableElement, AnnotationReflector annotationReflector) {
        String deserializerName =
                InstanceVariables.joinCamelCase(variableElement.getSimpleName().toString(), "Deserializer");
        if (context.isSameTypes(variableElement.asType(), BearerToken.class)) {
            // TODO(fwindheuser): Add some validation no more than one BearerToken cookie param is used.
            return ParameterTypes.authCookie(annotationReflector.getAnnotationValue(String.class), deserializerName);
        }

        return ParameterTypes.cookie(
                annotationReflector.getAnnotationValue(String.class),
                deserializerName,
                getParamDecoder(variableElement, annotationReflector));
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

        return DefaultDecoderNames.getDefaultDecoderFactory(decoderType, ContainerType.NONE, decoderOutputType)
                .orElseGet(() -> {
                    context.reportError(
                            "No default decoder exists for parameter. "
                                    + "Types with a valueOf(String) method are supported, as are conjure types",
                            variableElement,
                            SafeArg.of("variableType", variableType),
                            SafeArg.of("supportedTypes", DefaultDecoderNames.SUPPORTED_CLASSES));
                    return CodeBlock.of("// error");
                });
    }

    private CodeBlock getCollectionParamDecoder(
            VariableElement variableElement, AnnotationReflector annotationReflector) {
        // If the default marker interface is not used (overwritten by user), we want to use the user-provided decoder.
        TypeMirror typeMirror = annotationReflector.getAnnotationValue("decoder", TypeMirror.class);
        if (!context.isSameTypes(typeMirror, DefaultCollectionParamDecoder.class)) {
            return Instantiables.instantiate(typeMirror);
        }

        TypeMirror variableType = variableElement.asType();
        Optional<TypeMirror> innerListType = context.getGenericInnerType(List.class, variableType);
        Optional<TypeMirror> innerSetType = context.getGenericInnerType(Set.class, variableType);
        Optional<TypeMirror> innerOptionalType = context.getGenericInnerType(Optional.class, variableType);

        TypeMirror decoderType =
                innerListType.or(() -> innerSetType).or(() -> innerOptionalType).orElse(variableType);
        ContainerType decoderOutputType = getOutputType(innerListType, innerSetType, innerOptionalType);

        return DefaultDecoderNames.getDefaultDecoderFactory(decoderType, ContainerType.LIST, decoderOutputType)
                .orElseGet(() -> {
                    context.reportError(
                            "No default decoder exists for parameter. "
                                    + "Types with a valueOf(String) method are supported, as are conjure types",
                            variableElement,
                            SafeArg.of("variableType", variableType),
                            SafeArg.of("supportedTypes", DefaultDecoderNames.SUPPORTED_CLASSES));
                    return CodeBlock.of("// error");
                });
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
}
