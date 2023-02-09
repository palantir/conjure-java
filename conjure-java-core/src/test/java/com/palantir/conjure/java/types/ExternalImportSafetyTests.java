/*
 * (c) Copyright 2023 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java.types;

import static org.assertj.core.api.Assertions.assertThat;

import com.palantir.logsafe.DoNotLog;
import com.palantir.logsafe.Safe;
import com.palantir.product.ExternalLongUnionExample;
import com.palantir.product.SafeExternalLongAlias;
import com.palantir.product.SafeExternalLongExample;
import com.palantir.product.UndertowExternalLongTestService;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

public class ExternalImportSafetyTests {

    @Test
    public void testAliasAnnotations() {
        assertThat(SafeExternalLongAlias.class).hasAnnotation(Safe.class);

        assertMethodHasAnnotation(SafeExternalLongAlias.class, "toString", Safe.class);
        assertMethodHasAnnotation(SafeExternalLongAlias.class, "get", Safe.class);
        assertMethodParamHasAnnotation(SafeExternalLongAlias.class, "valueOf", "value", Safe.class);
        assertMethodParamHasAnnotation(SafeExternalLongAlias.class, "of", "value", Safe.class);
    }

    @Test
    public void testObjectAnnotations() {
        assertThat(SafeExternalLongExample.class).hasAnnotation(Safe.class);

        assertMethodHasAnnotation(SafeExternalLongExample.class, "toString", Safe.class);
        assertMethodHasAnnotation(SafeExternalLongExample.class, "getSafeExternalLongValue", Safe.class);

        assertMethodParamHasAnnotation(SafeExternalLongExample.class, "of", "safeExternalLongValue", Safe.class);

        Class<?> builder = getMatchingSubclass(SafeExternalLongExample.class, "Builder");
        assertMethodParamHasAnnotation(builder, "safeExternalLongValue", "safeExternalLongValue", Safe.class);
    }

    @Test
    public void testServiceAnnotations() {
        assertMethodParamHasAnnotation(
                UndertowExternalLongTestService.class, "testExternalLongArg", "externalLong", DoNotLog.class);
    }

    @Test
    public void testUnionAnnotations() {
        assertMethodParamHasAnnotation(ExternalLongUnionExample.class, "safeLong", "value", Safe.class);
        assertMethodParamHasAnnotation(ExternalLongUnionExample.class, "unknown", "type", Safe.class);

        Class<?> visitor = getMatchingSubclass(ExternalLongUnionExample.class, "$Visitor");
        assertMethodParamHasAnnotation(visitor, "visitSafeLong", "value", Safe.class);
        assertMethodParamHasAnnotation(visitor, "visitUnknown", "unknownType", Safe.class);

        Class<?> visitorBuilder = getMatchingSubclass(ExternalLongUnionExample.class, "$VisitorBuilder");
        assertFieldTypeParamHasAnnotation(visitorBuilder, "safeLongVisitor", "Long", Safe.class);
        assertFieldTypeParamHasAnnotation(visitorBuilder, "unknownVisitor", "String", Safe.class);
        assertMethodParamWithTypeParameterHasAnnotation(
                visitorBuilder, "safeLong", "safeLongVisitor", "Long", Safe.class);
        assertMethodParamWithTypeParameterHasAnnotation(
                visitorBuilder, "unknown", "unknownVisitor", "String", Safe.class);

        Class<?> stageVisitorBuilder =
                getMatchingSubclass(ExternalLongUnionExample.class, "SafeLongStageVisitorBuilder");
        assertMethodParamWithTypeParameterHasAnnotation(
                stageVisitorBuilder, "safeLong", "safeLongVisitor", "Long", Safe.class);

        Class<?> unknownStageVisitorBuilder =
                getMatchingSubclass(ExternalLongUnionExample.class, "UnknownStageVisitorBuilder");
        assertMethodParamWithTypeParameterHasAnnotation(
                unknownStageVisitorBuilder, "unknown", "unknownVisitor", "String", Safe.class);
    }

    private void assertMethodHasAnnotation(
            Class<?> parentClass, String methodName, Class<? extends Annotation> annotation) {
        Stream<Method> desiredMethods = getMatchingMethods(parentClass, methodName);
        assertThat(desiredMethods)
                .withFailMessage(String.format(
                        "Expected %s:%s to have annotation %s",
                        parentClass.getName(), methodName, annotation.getName()))
                .allMatch(method -> method.isAnnotationPresent(annotation));
    }

    private void assertMethodParamHasAnnotation(
            Class<?> parentClass, String methodName, String parameterName, Class<? extends Annotation> annotation) {
        Stream<Method> desiredMethods = getMatchingMethods(parentClass, methodName);
        assertThat(desiredMethods)
                .withFailMessage(String.format(
                        "Expected %s:%s parameter %s to have annotation %s",
                        parentClass.getName(), methodName, parameterName, annotation.getName()))
                .map(method -> getMatchingParameter(method, parameterName))
                .allMatch(parameter -> parameter.isAnnotationPresent(annotation));
    }

    private void assertMethodParamWithTypeParameterHasAnnotation(
            Class<?> parentClass,
            String methodName,
            String parameterName,
            String typeParameter,
            Class<? extends Annotation> annotation) {
        Stream<Method> desiredMethods = getMatchingMethods(parentClass, methodName);
        Stream<AnnotatedType> annotatedTypes = desiredMethods.map(
                method -> getMatchingParameter(method, parameterName).getAnnotatedType());
        assertThat(annotatedTypes)
                .withFailMessage(String.format(
                        "Expected %s:%s parameter %s of type %s to have annotation %s",
                        parentClass.getName(), methodName, parameterName, typeParameter, annotation.getName()))
                .map(annotatedType -> getAnnotatedTypeParameter(annotatedType, typeParameter))
                .allMatch(t -> t.isAnnotationPresent(annotation));
    }

    private void assertFieldTypeParamHasAnnotation(
            Class<?> parentClass, String fieldName, String typeName, Class<? extends Annotation> annotation) {
        Stream<Field> desiredFields = Arrays.stream(parentClass.getDeclaredFields())
                .filter(field -> field.getName().contains(fieldName));
        Stream<AnnotatedType> annotatedTypeParameters =
                desiredFields.map(Field::getAnnotatedType).map(type -> getAnnotatedTypeParameter(type, typeName));
        assertThat(annotatedTypeParameters)
                .withFailMessage(String.format(
                        "Expected %s:%s of type %s to have annotation %s",
                        parentClass.getName(), fieldName, typeName, annotation.getName()))
                .allMatch(t -> t.isAnnotationPresent(annotation));
    }

    private Stream<Method> getMatchingMethods(Class<?> parentClass, String methodName) {
        return Arrays.stream(parentClass.getMethods())
                .filter(method -> method.getName().equals(methodName));
    }

    private Class<?> getMatchingSubclass(Class<?> parentClass, String subclassName) {
        Optional<Class<?>> subclassIfExists = Arrays.stream(SafeExternalLongExample.class.getClasses())
                .filter(subclass -> subclass.getName().contains("Builder"))
                .findAny();
        assertThat(subclassIfExists)
                .withFailMessage(String.format("Expected %s:%s to exist", parentClass, subclassName))
                .isPresent();
        return subclassIfExists.get();
    }

    private Parameter getMatchingParameter(Method method, String parameterName) {
        Optional<Parameter> parameterIfExists = Arrays.stream(method.getParameters())
                .filter(parameter -> parameter.getName().contains(parameterName))
                .findAny();
        assertThat(parameterIfExists)
                .withFailMessage(String.format("Expected to find parameter %s on method %s", parameterName, method))
                .isPresent();
        return parameterIfExists.get();
    }

    private AnnotatedType getAnnotatedTypeParameter(AnnotatedType parameterizedType, String parameter) {
        Optional<AnnotatedType> typeParameterIfExists = Arrays.stream(
                        ((AnnotatedParameterizedType) parameterizedType).getAnnotatedActualTypeArguments())
                .filter(t -> t.getType().getTypeName().contains(parameter))
                .findAny();
        assertThat(typeParameterIfExists)
                .withFailMessage("Expected type parameter %s on type %s", parameter, parameterizedType)
                .isPresent();
        return typeParameterIfExists.get();
    }
}
