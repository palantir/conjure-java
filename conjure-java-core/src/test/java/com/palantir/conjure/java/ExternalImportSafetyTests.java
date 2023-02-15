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

package com.palantir.conjure.java;

import static org.assertj.core.api.Assertions.assertThat;

import com.palantir.logsafe.DoNotLog;
import com.palantir.logsafe.Safe;
import com.palantir.product.ExternalLongUnionExample;
import com.palantir.product.SafeExternalLongExample;
import com.palantir.product.UndertowExternalLongTestService;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import test.prefix.com.palantir.product.SafeExternalLongAlias;

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

        Class<?> builder = getMatchingSubclass(SafeExternalLongExample.class, "Builder");
        assertMethodParamHasAnnotation(builder, "safeExternalLongValue", "safeExternalLongValue", Safe.class);
    }

    @Test
    public void testServiceAnnotations() {
        assertMethodParamHasAnnotation(
                UndertowExternalLongTestService.class, "testDangerousLong", "dangerousLong", DoNotLog.class);
        assertMethodParamHasAnnotation(
                UndertowExternalLongTestService.class, "testSafeExternalLong", "safeExternalLong", Safe.class);

        assertMethodParamHasNoAnnotation(UndertowExternalLongTestService.class, "testLong", "long_");
        assertMethodParamHasNoAnnotation(
                UndertowExternalLongTestService.class, "testDangerousLongAlias", "dangerousLongAlias");
        assertMethodParamHasNoAnnotation(
                UndertowExternalLongTestService.class, "testSafeExternalLongAlias", "safeExternalLongAlias");
        assertMethodParamHasNoAnnotation(UndertowExternalLongTestService.class, "testLongAlias", "longAlias");
    }

    @Test
    public void testUnionAnnotations() {
        assertMethodParamHasAnnotation(ExternalLongUnionExample.class, "safeLong", "value", Safe.class);
        assertMethodParamHasAnnotation(ExternalLongUnionExample.class, "unknown", "type", Safe.class);

        Class<?> visitor = getExactlyMatchingSubclass(
                ExternalLongUnionExample.class, "com.palantir.product.ExternalLongUnionExample$Visitor");
        assertMethodParamHasAnnotation(visitor, "visitSafeLong", "value", Safe.class);
        assertMethodParamHasAnnotation(visitor, "visitUnknown", "unknownType", Safe.class);

        Class<?> visitorBuilder = getExactlyMatchingSubclass(
                ExternalLongUnionExample.class, "com.palantir.product.ExternalLongUnionExample$VisitorBuilder");
        assertFieldTypeParamHasAnnotation(visitorBuilder, "safeLongVisitor", "Long", Safe.class);
        assertFieldTypeParamHasAnnotation(visitorBuilder, "unknownVisitor", "String", Safe.class);
        assertMethodParamWithTypeParameterHasAnnotation(visitorBuilder, "safeLong", "safeLong", "Long", Safe.class);
        assertMethodParamWithTypeParameterHasAnnotation(visitorBuilder, "unknown", "unknown", "String", Safe.class);

        Class<?> stageVisitorBuilder =
                getMatchingSubclass(ExternalLongUnionExample.class, "SafeLongStageVisitorBuilder");
        assertMethodParamWithTypeParameterHasAnnotation(
                stageVisitorBuilder, "safeLong", "safeLongVisitor", "Long", Safe.class);

        Class<?> unknownStageVisitorBuilder =
                getMatchingSubclass(ExternalLongUnionExample.class, "UnknownStageVisitorBuilder");
        assertMethodParamWithTypeParameterHasAnnotation(
                unknownStageVisitorBuilder, "unknown", "unknownVisitor", "String", Safe.class);
    }

    @Test
    public void testOptionalAnnotations() {
        assertMethodHasAnnotation(SafeExternalLongExample.class, "getOptionalSafeExternalLong", Safe.class);

        Class<?> builderSubclass = getMatchingSubclass(SafeExternalLongExample.class, "$Builder");
        assertFieldTypeParamHasAnnotation(builderSubclass, "optionalSafeExternalLong", "Long", Safe.class);
        assertMethodParamWithWildcardTypeParameterHasAnnotation(
                builderSubclass,
                "optionalSafeExternalLong",
                "optionalSafeExternalLong",
                "Optional",
                "Long",
                Safe.class);
    }

    @Test
    public void testListAnnotations() {
        assertMethodHasAnnotation(SafeExternalLongExample.class, "getSafeExternalLongList", Safe.class);

        Class<?> builderSubclass = getMatchingSubclass(SafeExternalLongExample.class, "$Builder");
        assertFieldTypeParamHasAnnotation(builderSubclass, "safeExternalLongList", "Long", Safe.class);
        assertMethodParamWithWildcardTypeParameterHasAnnotation(
                builderSubclass, "safeExternalLongList", "safeExternalLongList", "Iterable", "Long", Safe.class);
        assertMethodParamWithWildcardTypeParameterHasAnnotation(
                builderSubclass, "addAllSafeExternalLongList", "safeExternalLongList", "Iterable", "Long", Safe.class);
    }

    @Test
    public void testSetAnnotations() {
        assertMethodHasAnnotation(SafeExternalLongExample.class, "getSafeExternalLongSet", Safe.class);

        Class<?> builderSubclass = getMatchingSubclass(SafeExternalLongExample.class, "$Builder");
        assertFieldTypeParamHasAnnotation(builderSubclass, "safeExternalLongSet", "Long", Safe.class);
        assertMethodParamWithWildcardTypeParameterHasAnnotation(
                builderSubclass, "safeExternalLongSet", "safeExternalLongSet", "Iterable", "Long", Safe.class);
        assertMethodParamWithWildcardTypeParameterHasAnnotation(
                builderSubclass, "addAllSafeExternalLongSet", "safeExternalLongSet", "Iterable", "Long", Safe.class);
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

    private void assertMethodParamHasNoAnnotation(Class<?> parentClass, String methodName, String parameterName) {
        Stream<Method> desiredMethods = getMatchingMethods(parentClass, methodName);
        assertThat(desiredMethods)
                .withFailMessage(String.format(
                        "Expected %s:%s parameter %s to have no annotation",
                        parentClass.getName(), methodName, parameterName))
                .map(method -> getMatchingParameter(method, parameterName))
                .allMatch(parameter -> parameter.getAnnotations().length == 0);
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

    private void assertMethodParamWithWildcardTypeParameterHasAnnotation(
            Class<?> parentClass,
            String methodName,
            String parameterName,
            String parameterType,
            String typeParameter,
            Class<? extends Annotation> annotation) {
        Stream<Method> desiredMethods = getMatchingMethods(parentClass, methodName)
                .filter(method -> hasMatchingParameter(method, parameterName, parameterType));
        Stream<AnnotatedType> annotatedTypes = desiredMethods
                .map(method -> getMatchingParameter(method, parameterName, parameterType)
                        .getAnnotatedType())
                .filter(annotatedType -> hasTypeParameter(annotatedType, typeParameter));
        assertThat(annotatedTypes)
                .withFailMessage(String.format(
                        "Expected %s:%s parameter %s of type %s to have annotation %s",
                        parentClass.getName(), methodName, parameterName, typeParameter, annotation.getName()))
                .allMatch(t -> t.toString().contains("@com.palantir.logsafe.Safe()"));
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
        List<Method> methods = Arrays.stream(parentClass.getMethods())
                .filter(method -> method.getName().equals(methodName))
                .collect(Collectors.toList());
        assertThat(methods)
                .withFailMessage(String.format("Expected method %s on class %s", methodName, parentClass.getName()))
                .isNotEmpty();
        return methods.stream();
    }

    private Class<?> getMatchingSubclass(Class<?> parentClass, String subclassName) {
        Optional<Class<?>> subclassIfExists = Arrays.stream(parentClass.getDeclaredClasses())
                .filter(subclass -> subclass.getName().contains(subclassName))
                .findAny();
        assertThat(subclassIfExists)
                .withFailMessage(String.format("Expected %s:%s to exist", parentClass, subclassName))
                .isPresent();
        return subclassIfExists.get();
    }

    private Class<?> getExactlyMatchingSubclass(Class<?> parentClass, String subclassName) {
        Optional<Class<?>> subclassIfExists = Arrays.stream(parentClass.getDeclaredClasses())
                .filter(subclass -> subclass.getName().equals(subclassName))
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

    private Parameter getMatchingParameter(Method method, String parameterName, String parameterType) {
        Optional<Parameter> parameterIfExists = Arrays.stream(method.getParameters())
                .filter(parameter -> parameter.getName().contains(parameterName))
                .filter(parameter -> parameter.getType().getName().contains(parameterType))
                .findAny();
        assertThat(parameterIfExists)
                .withFailMessage(String.format(
                        "Expected to find parameter %s of type %s on method %s", parameterName, parameterType, method))
                .isPresent();
        return parameterIfExists.get();
    }

    private boolean hasMatchingParameter(Method method, String parameterName, String parameterType) {
        Optional<Parameter> parameterIfExists = Arrays.stream(method.getParameters())
                .filter(parameter -> parameter.getName().contains(parameterName))
                .filter(parameter -> parameter.getType().getName().contains(parameterType))
                .findAny();
        return parameterIfExists.isPresent();
    }

    private boolean hasTypeParameter(AnnotatedType parameterizedType, String parameter) {
        Optional<AnnotatedType> typeParameterIfExists = Arrays.stream(
                        ((AnnotatedParameterizedType) parameterizedType).getAnnotatedActualTypeArguments())
                .filter(t -> t.getType().getTypeName().contains(parameter))
                .findAny();
        return typeParameterIfExists.isPresent();
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
