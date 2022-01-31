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
import com.palantir.conjure.java.undertow.annotations.Handle;
import com.palantir.conjure.java.undertow.lib.RequestContext;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalStateException;
import com.palantir.tokens.auth.AuthHeader;
import io.undertow.server.HttpServerExchange;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

public final class ParamTypesResolver {

    private static final ImmutableSet<Class<?>> PARAM_ANNOTATION_CLASSES =
            ImmutableSet.of(Handle.Body.class, Handle.PathParam.class, Handle.QueryParam.class, Handle.Header.class);
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
            String deserializerName = InstanceVariables.joinCamelCase(endpointName.get(), "Deserializer");
            TypeMirror deserializer = annotationReflector.getAnnotationValue(TypeMirror.class);
            return Optional.of(ParameterTypes.body(Instantiables.instantiate(deserializer), deserializerName));
        } else if (annotationReflector.isAnnotation(Handle.Header.class)) {
            String deserializerName = InstanceVariables.joinCamelCase(
                    variableElement.getSimpleName().toString(), "Deserializer");
            return Optional.of(ParameterTypes.header(
                    annotationReflector.getAnnotationValue(String.class),
                    deserializerName,
                    Instantiables.instantiate(annotationReflector.getAnnotationValue("decoder", TypeMirror.class))));
        } else if (annotationReflector.isAnnotation(Handle.PathParam.class)) {
            String javaParameterName = variableElement.getSimpleName().toString();
            String deserializerName = InstanceVariables.joinCamelCase(javaParameterName, "Deserializer");
            return Optional.of(ParameterTypes.path(
                    javaParameterName,
                    deserializerName,
                    Instantiables.instantiate(annotationReflector.getAnnotationValue("decoder", TypeMirror.class))));
        } else if (annotationReflector.isAnnotation(Handle.QueryParam.class)) {
            String deserializerName = InstanceVariables.joinCamelCase(
                    variableElement.getSimpleName().toString(), "Deserializer");
            return Optional.of(ParameterTypes.query(
                    annotationReflector.getAnnotationValue(String.class),
                    deserializerName,
                    Instantiables.instantiate(annotationReflector.getAnnotationValue("decoder", TypeMirror.class))));
        }

        throw new SafeIllegalStateException("Not possible");
    }
}
