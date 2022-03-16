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

import com.palantir.conjure.java.undertow.processor.data.ParameterType.Cases;
import com.palantir.conjure.java.undertow.processor.data.ParameterType.SafeLoggingAnnotation;
import com.squareup.javapoet.CodeBlock;

public final class ParameterTypeVisitors {

    public enum UsesRequestContextVisitor implements Cases<Boolean> {
        INSTANCE;

        @Override
        public Boolean body(
                String _variableName,
                CodeBlock _deserializerFactory,
                String _deserializerFieldName,
                SafeLoggingAnnotation safeLoggable) {
            return !safeLoggable.equals(SafeLoggingAnnotation.UNKNOWN);
        }

        @Override
        public Boolean header(
                String _variableName,
                String _headerName,
                String _deserializerFieldName,
                CodeBlock _deserializerFactory,
                SafeLoggingAnnotation safeLoggable) {
            return !safeLoggable.equals(SafeLoggingAnnotation.UNKNOWN);
        }

        @Override
        public Boolean path(
                String _paramName,
                String _deserializerFieldName,
                CodeBlock _deserializerFactory,
                SafeLoggingAnnotation safeLoggable) {
            return !safeLoggable.equals(SafeLoggingAnnotation.UNKNOWN);
        }

        @Override
        public Boolean pathMulti(
                String _paramName,
                String _deserializerFieldName,
                CodeBlock _deserializerFactory,
                SafeLoggingAnnotation safeLoggable) {
            return !safeLoggable.equals(SafeLoggingAnnotation.UNKNOWN);
        }

        @Override
        public Boolean query(
                String _variableName,
                String _paramName,
                String _deserializerFieldName,
                CodeBlock _deserializerFactory,
                SafeLoggingAnnotation safeLoggable) {
            return !safeLoggable.equals(SafeLoggingAnnotation.UNKNOWN);
        }

        @Override
        public Boolean cookie(
                String _variableName,
                String _cookieName,
                String _deserializerFieldName,
                CodeBlock _deserializerFactory,
                SafeLoggingAnnotation safeLoggable) {
            return !safeLoggable.equals(SafeLoggingAnnotation.UNKNOWN);
        }

        @Override
        public Boolean authCookie(String _variableName, String _cookieName, String _deserializerFieldName) {
            return false;
        }

        @Override
        public Boolean authHeader(String _variableName) {
            return false;
        }

        @Override
        public Boolean exchange() {
            return false;
        }

        @Override
        public Boolean context() {
            return true;
        }
    }

    public enum IsPathMultiParamsVisitor implements Cases<Boolean> {
        INSTANCE;

        @Override
        public Boolean body(
                String _variableName,
                CodeBlock _deserializerFactory,
                String _deserializerFieldName,
                SafeLoggingAnnotation _safeLoggable) {
            return false;
        }

        @Override
        public Boolean header(
                String _variableName,
                String _headerName,
                String _deserializerFieldName,
                CodeBlock _deserializerFactory,
                SafeLoggingAnnotation _safeLoggable) {
            return false;
        }

        @Override
        public Boolean path(
                String _paramName,
                String _deserializerFieldName,
                CodeBlock _deserializerFactory,
                SafeLoggingAnnotation _safeLoggable) {
            return false;
        }

        @Override
        public Boolean pathMulti(
                String _paramName,
                String _deserializerFieldName,
                CodeBlock _deserializerFactory,
                SafeLoggingAnnotation _safeLoggable) {
            return true;
        }

        @Override
        public Boolean query(
                String _variableName,
                String _paramName,
                String _deserializerFieldName,
                CodeBlock _deserializerFactory,
                SafeLoggingAnnotation _safeLoggable) {
            return false;
        }

        @Override
        public Boolean cookie(
                String _variableName,
                String _cookieName,
                String _deserializerFieldName,
                CodeBlock _deserializerFactory,
                SafeLoggingAnnotation _safeLoggable) {
            return false;
        }

        @Override
        public Boolean authCookie(String _variableName, String _cookieName, String _deserializerFieldName) {
            return false;
        }

        @Override
        public Boolean authHeader(String _variableName) {
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
    }

    private ParameterTypeVisitors() {}
}
