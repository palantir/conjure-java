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

import java.util.List;
import java.util.stream.Collectors;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.AnnotationValueVisitor;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

final class AnnotationValueVisitors {

    static final class StringArrayAnnotationValueVisitor implements AnnotationValueVisitor<List<String>, Void> {

        @Override
        public List<String> visit(AnnotationValue value, Void unused) {
            return value.accept(this, unused);
        }

        @Override
        public List<String> visitBoolean(boolean _value, Void _unused) {
            return null;
        }

        @Override
        public List<String> visitByte(byte _value, Void _unused) {
            return null;
        }

        @Override
        public List<String> visitChar(char _value, Void _unused) {
            return null;
        }

        @Override
        public List<String> visitDouble(double _value, Void _unused) {
            return null;
        }

        @Override
        public List<String> visitFloat(float _value, Void _unused) {
            return null;
        }

        @Override
        public List<String> visitInt(int _value, Void _unused) {
            return null;
        }

        @Override
        public List<String> visitLong(long _value, Void _unused) {
            return null;
        }

        @Override
        public List<String> visitShort(short _value, Void _unused) {
            return null;
        }

        @Override
        public List<String> visitString(String _value, Void _unused) {
            return null;
        }

        @Override
        public List<String> visitType(TypeMirror _value, Void _unused) {
            return null;
        }

        @Override
        public List<String> visitEnumConstant(VariableElement _value, Void _unused) {
            return null;
        }

        @Override
        public List<String> visitAnnotation(AnnotationMirror _value, Void _unused) {
            return null;
        }

        @Override
        public List<String> visitArray(List<? extends AnnotationValue> vals, Void _unused) {
            return vals.stream()
                    .map(val -> val.accept(new StringAnnotationValueVisitor(), null))
                    .collect(Collectors.toList());
        }

        @Override
        public List<String> visitUnknown(AnnotationValue _value, Void _unused) {
            return null;
        }
    }

    static final class StringAnnotationValueVisitor implements AnnotationValueVisitor<String, Void> {

        @Override
        public String visit(AnnotationValue av, Void unused) {
            return av.accept(this, unused);
        }

        @Override
        public String visitBoolean(boolean _value, Void _unused) {
            return null;
        }

        @Override
        public String visitByte(byte _value, Void _unused) {
            return null;
        }

        @Override
        public String visitChar(char _value, Void _unused) {
            return null;
        }

        @Override
        public String visitDouble(double _value, Void _unused) {
            return null;
        }

        @Override
        public String visitFloat(float _value, Void _unused) {
            return null;
        }

        @Override
        public String visitInt(int _value, Void _unused) {
            return null;
        }

        @Override
        public String visitLong(long _value, Void _unused) {
            return null;
        }

        @Override
        public String visitShort(short _value, Void _unused) {
            return null;
        }

        @Override
        public String visitString(String value, Void _unused) {
            return value;
        }

        @Override
        public String visitType(TypeMirror _value, Void _unused) {
            return null;
        }

        @Override
        public String visitEnumConstant(VariableElement _value, Void _unused) {
            return null;
        }

        @Override
        public String visitAnnotation(AnnotationMirror _value, Void _unused) {
            return null;
        }

        @Override
        public String visitArray(List<? extends AnnotationValue> _value, Void _unused) {
            return null;
        }

        @Override
        public String visitUnknown(AnnotationValue _value, Void _unused) {
            return null;
        }
    }

    private AnnotationValueVisitors() {}
}
