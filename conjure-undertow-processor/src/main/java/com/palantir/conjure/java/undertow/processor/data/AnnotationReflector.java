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

import com.google.auto.common.AnnotationMirrors;
import com.google.auto.common.MoreElements;
import com.palantir.common.streams.KeyedStream;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import org.immutables.value.Value;

@Value.Immutable
public interface AnnotationReflector {

    @Value.Parameter
    AnnotationMirror annotationMirror();

    @Value.Derived
    default TypeElement annotationTypeElement() {
        return MoreElements.asType(annotationMirror().getAnnotationType().asElement());
    }

    @Value.Derived
    default Map<String, ExecutableElement> methods() {
        return KeyedStream.of(ElementFilter.methodsIn(annotationTypeElement().getEnclosedElements()))
                .mapKeys(element -> element.getSimpleName().toString())
                .collectToMap();
    }

    @Value.Derived
    default Map<String, Object> values() {
        return KeyedStream.stream(AnnotationMirrors.getAnnotationValuesWithDefaults(annotationMirror()))
                .mapKeys(key -> key.getSimpleName().toString())
                .map(AnnotationValue::getValue)
                .collectToMap();
    }

    default boolean isAnnotation(Class<? extends Annotation> annotationClazz) {
        return annotationTypeElement().getQualifiedName().contentEquals(annotationClazz.getCanonicalName());
    }

    default String getStringValueField() {
        return getAnnotationValue(String.class);
    }

    @SuppressWarnings("unchecked")
    default <T> T getAnnotationValue(String name, Class<T> valueClazz) {
        Object value =
                AnnotationMirrors.getAnnotationValue(annotationMirror(), name).getValue();
        if (valueClazz.isArray()) {
            Class<?> arrayType =
                    Preconditions.checkNotNull(valueClazz.getComponentType(), "Unable to find array component type");
            List<AnnotationValue> arrayValue = (List<AnnotationValue>) value;
            List<Object> results = new ArrayList<>(arrayValue.size());
            for (AnnotationValue annotationValue : arrayValue) {
                results.add(arrayType.cast(annotationValue.getValue()));
            }
            return (T) results.toArray((Object[]) Array.newInstance(arrayType, results.size()));
        }

        return valueClazz.cast(
                AnnotationMirrors.getAnnotationValue(annotationMirror(), name).getValue());
    }

    default <T> T getAnnotationValue(Class<T> valueClazz) {
        return getAnnotationValue("value", valueClazz);
    }

    default <T> Optional<T> getFieldMaybe(String fieldName, Class<T> valueClazz) {
        Preconditions.checkArgument(
                methods().containsKey(fieldName),
                "Unknown field",
                SafeArg.of("fieldName", fieldName),
                SafeArg.of("type", annotationTypeElement()),
                SafeArg.of("fields", methods()));
        Optional<Object> maybeValue = Optional.ofNullable(values().get(fieldName));
        return maybeValue.map(value -> {
            Preconditions.checkArgument(
                    valueClazz.isInstance(value),
                    "Value not of the right type",
                    SafeArg.of("expectedType", valueClazz),
                    SafeArg.of("value", value.getClass()));
            return valueClazz.cast(value);
        });
    }
}
