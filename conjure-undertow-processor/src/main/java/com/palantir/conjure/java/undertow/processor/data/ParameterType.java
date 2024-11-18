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

import com.squareup.javapoet.CodeBlock;
import org.derive4j.Data;

@Data
public interface ParameterType {
    interface Cases<R> {

        R body(
                String variableName,
                CodeBlock deserializerFactory,
                String deserializerFieldName,
                SafeLoggingAnnotation safeLoggable);

        R header(
                String variableName,
                String headerName,
                String deserializerFieldName,
                CodeBlock deserializerFactory,
                SafeLoggingAnnotation safeLoggable);

        R path(
                String paramName,
                String deserializerFieldName,
                CodeBlock deserializerFactory,
                SafeLoggingAnnotation safeLoggable);

        R pathMulti(
                String paramName,
                String deserializerFieldName,
                CodeBlock deserializerFactory,
                SafeLoggingAnnotation safeLoggable);

        R query(
                String variableName,
                String paramName,
                String deserializerFieldName,
                CodeBlock deserializerFactory,
                SafeLoggingAnnotation safeLoggable);

        R form(
                String variableName,
                String paramName,
                String deserializerFieldName,
                CodeBlock deserializerFactory,
                SafeLoggingAnnotation safeLoggable);

        R cookie(
                String variableName,
                String cookieName,
                String deserializerFieldName,
                CodeBlock deserializerFactory,
                SafeLoggingAnnotation safeLoggable);

        R authCookie(String variableName, String cookieName, String deserializerFieldName);

        R authHeader(String variableName);

        R exchange();

        R context();
    }

    <R> R match(Cases<R> cases);

    enum SafeLoggingAnnotation {
        SAFE,
        UNSAFE,
        UNKNOWN;
    }
}
