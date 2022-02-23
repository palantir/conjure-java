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
import com.google.common.base.Predicates;
import com.palantir.conjure.java.undertow.annotations.Handle;
import com.palantir.conjure.java.undertow.annotations.HttpMethod;
import com.palantir.conjure.java.undertow.processor.ErrorContext;
import com.palantir.conjure.java.undertow.processor.data.ParameterType.Cases;
import com.squareup.javapoet.CodeBlock;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public final class EndpointDefinitions {

    private final ParamTypesResolver paramTypesResolver;
    private final ArgumentTypesResolver argumentTypesResolver;
    private final ReturnTypesResolver returnTypesResolver;
    private final ErrorContext errorContext;

    public EndpointDefinitions(ErrorContext errorContext, Elements elements, Types types) {
        this.errorContext = errorContext;
        ResolverContext context = new ResolverContext(errorContext, elements, types);
        this.paramTypesResolver = new ParamTypesResolver(context);
        this.argumentTypesResolver = new ArgumentTypesResolver(context);
        this.returnTypesResolver = new ReturnTypesResolver(context);
    }

    public Optional<EndpointDefinition> tryParseEndpointDefinition(ExecutableElement element) {
        AnnotationReflector requestAnnotationReflector = MoreElements.getAnnotationMirror(element, Handle.class)
                .toJavaUtil()
                .map(ImmutableAnnotationReflector::of)
                .orElseThrow();

        EndpointName endpointName =
                ImmutableEndpointName.of(element.getSimpleName().toString());
        ServiceName serviceName = ImmutableServiceName.of(
                element.getEnclosingElement().getSimpleName().toString());

        HttpMethod method = HttpMethod.valueOf(requestAnnotationReflector
                .getAnnotationValue("method", VariableElement.class)
                .getSimpleName()
                .toString());
        HttpPath path = HttpPath.of(requestAnnotationReflector.getAnnotationValue("path", String.class));
        Optional<ReturnType> maybeReturnType =
                returnTypesResolver.getReturnType(endpointName, element, requestAnnotationReflector);
        List<Optional<ArgumentDefinition>> args = element.getParameters().stream()
                .map(this::getArgumentDefinition)
                .collect(Collectors.toList());

        if (!args.stream()
                        .filter(Predicates.not(Optional::isPresent))
                        .collect(Collectors.toList())
                        .isEmpty()
                || maybeReturnType.isEmpty()) {
            return Optional.empty();
        }

        List<ArgumentDefinition> argumentDefinitions =
                args.stream().map(Optional::get).collect(Collectors.toList());

        List<ArgumentDefinition> multiPathParams = getPathMultiParams(argumentDefinitions);
        if (multiPathParams.size() > 1) {
            errorContext.reportError("Only one PathMultiParam is supported", element);
            return Optional.empty();
        }

        if (multiPathParams.size() == 1) {
            String argName = multiPathParams.get(0).argName().get();
            String expectedEnd = "/{" + argName + '}';
            String pathString = path.path();
            if (!pathString.endsWith(expectedEnd)) {
                errorContext.reportError("PathMultiParam is only supported at the end of the path template", element);
                return Optional.empty();
            }
            path = HttpPath.of(pathString.substring(0, pathString.length() - expectedEnd.length()) + "/*");
        }

        return Optional.of(ImmutableEndpointDefinition.builder()
                .endpointName(endpointName)
                .serviceName(serviceName)
                .httpMethod(method)
                .httpPath(path)
                .returns(maybeReturnType.get())
                .addAllArguments(argumentDefinitions)
                .build());
    }

    private Optional<ArgumentDefinition> getArgumentDefinition(VariableElement param) {
        Optional<ArgumentType> argumentType = argumentTypesResolver.getArgumentType(param);
        Optional<ParameterType> parameterType = paramTypesResolver.getParameterType(param);

        if (argumentType.isEmpty() || parameterType.isEmpty()) {
            return Optional.empty();
        }

        // TODO(ckozak): More validation around ArgumentType and ParameterType actually agreeing, e.g. if
        //  ArgumentType#requestBody then ParameterType#rawBody.

        return Optional.of(ImmutableArgumentDefinition.builder()
                .argName(ImmutableArgumentName.of(param.getSimpleName().toString()))
                .argType(argumentType.get())
                .paramType(parameterType.get())
                .build());
    }

    private static List<ArgumentDefinition> getPathMultiParams(List<ArgumentDefinition> argumentDefinitions) {
        return argumentDefinitions.stream()
                .filter(definition -> definition.paramType().match(new Cases<>() {
                    @Override
                    public Boolean body(CodeBlock _deserializerFactory, String _deserializerFieldName) {
                        return false;
                    }

                    @Override
                    public Boolean header(
                            String _headerName, String _deserializerFieldName, CodeBlock _deserializerFactory) {
                        return false;
                    }

                    @Override
                    public Boolean path(
                            String _paramName, String _deserializerFieldName, CodeBlock _deserializerFactory) {
                        return false;
                    }

                    @Override
                    public Boolean pathMulti(
                            String _paramName, String _deserializerFieldName, CodeBlock _deserializerFactory) {
                        return true;
                    }

                    @Override
                    public Boolean query(
                            String _paramName, String _deserializerFieldName, CodeBlock _deserializerFactory) {
                        return false;
                    }

                    @Override
                    public Boolean cookie(
                            String _cookieName, String _deserializerFieldName, CodeBlock _deserializerFactory) {
                        return false;
                    }

                    @Override
                    public Boolean authCookie(String _cookieName, String _deserializerFieldName) {
                        return false;
                    }

                    @Override
                    public Boolean authHeader() {
                        return false;
                    }

                    @Override
                    public Boolean exchange() {
                        return false;
                    }

                    @Override
                    public Boolean context() {
                        return false;
                    }
                }))
                .collect(Collectors.toList());
    }
}
