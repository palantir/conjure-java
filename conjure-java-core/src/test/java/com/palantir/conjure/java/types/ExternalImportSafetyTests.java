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
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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

        Optional<Class<?>> builder = Arrays.stream(SafeExternalLongExample.class.getClasses())
                .filter(subclass -> subclass.getName().contains("Builder"))
                .findAny();
        assertThat(builder).isPresent();
        assertMethodParamHasAnnotation(builder.get(), "safeExternalLongValue", "safeExternalLongValue", Safe.class);
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

        Optional<Class<?>> visitor = Arrays.stream(ExternalLongUnionExample.class.getClasses())
                .filter(subclass -> subclass.getName().contains("$Visitor"))
                .findAny();
        assertThat(visitor).isPresent();
        assertMethodParamHasAnnotation(visitor.get(), "visitSafeLong", "value", Safe.class);
        assertMethodParamHasAnnotation(visitor.get(), "visitUnknown", "unknownType", Safe.class);

        Optional<Class<?>> visitorBuilder = Arrays.stream(ExternalLongUnionExample.class.getDeclaredClasses())
                .filter(subclass -> subclass.getName().contains("$VisitorBuilder"))
                .findAny();
        assertThat(visitorBuilder).isPresent();
        assertFieldTypeParamHasAnnotation(visitorBuilder.get(), "safeLongVisitor", "Long", Safe.class);
        assertFieldTypeParamHasAnnotation(visitorBuilder.get(), "unknownVisitor", "String", Safe.class);
        assertMethodParamWithTypeParameterHasAnnotation(
                visitorBuilder.get(), "safeLong", "safeLongVisitor", "Long", Safe.class);
        assertMethodParamWithTypeParameterHasAnnotation(
                visitorBuilder.get(), "unknown", "unknownVisitor", "Long", Safe.class);

        Optional<Class<?>> stageVisitorBuilder = Arrays.stream(ExternalLongUnionExample.class.getDeclaredClasses())
                .filter(subclass -> subclass.getName().contains("SafeLongStageVisitorBuilder"))
                .findAny();
        assertThat(stageVisitorBuilder).isPresent();
        assertMethodParamWithTypeParameterHasAnnotation(
                stageVisitorBuilder.get(), "safeLong", "safeLongVisitor", "Long", Safe.class);

        Optional<Class<?>> unknownStageVisitorBuilder = Arrays.stream(
                        ExternalLongUnionExample.class.getDeclaredClasses())
                .filter(subclass -> subclass.getName().contains("UnknownStageVisitorBuilder"))
                .findAny();
        assertThat(unknownStageVisitorBuilder).isPresent();
        assertMethodParamWithTypeParameterHasAnnotation(
                unknownStageVisitorBuilder.get(), "unknown", "unknownVisitor", "Long", Safe.class);
    }

    private void assertFieldTypeParamHasAnnotation(
            Class<?> parentClass, String fieldName, String typeName, Class<? extends Annotation> annotation) {
        AnnotatedType type = Arrays.stream(parentClass.getDeclaredFields())
                .filter(field -> field.getName().contains(fieldName))
                .findAny()
                .get()
                .getAnnotatedType();
        assertThat(((AnnotatedParameterizedType) type).getAnnotatedActualTypeArguments())
                .filteredOn(t -> t.getType().getTypeName().contains(typeName))
                .hasSize(1)
                .allMatch(t -> t.isAnnotationPresent(annotation));
    }

    private void assertMethodHasAnnotation(
            Class<?> parentClass, String methodName, Class<? extends Annotation> annotation) {
        Method[] methods = parentClass.getMethods();
        assertThat(methods)
                .filteredOn(method -> method.getName().equals(methodName))
                .hasSize(1)
                .allMatch(method -> method.isAnnotationPresent(annotation));
    }

    private void assertMethodParamHasAnnotation(
            Class<?> parentClass, String methodName, String parameterName, Class<? extends Annotation> annotation) {
        Method[] methods = parentClass.getMethods();
        Optional<Method> desiredMethod = Arrays.stream(methods)
                .filter(method -> method.getName().equals(methodName))
                .findAny();
        assertThat(desiredMethod).isPresent();
        assertThat(desiredMethod)
                .map(method -> Arrays.stream(desiredMethod.get().getParameters())
                        .filter(parameter -> parameter.getName().contains(parameterName))
                        .findAny())
                .isPresent()
                .get()
                .satisfies(parameter -> parameter.get().isAnnotationPresent(annotation));
    }

    private void assertMethodParamWithTypeParameterHasAnnotation(
            Class<?> parentClass,
            String methodName,
            String parameterName,
            String typeParameter,
            Class<? extends Annotation> annotation) {
        Method[] methods = parentClass.getMethods();
        List<Method> desiredMethods = Arrays.stream(methods)
                .filter(method -> method.getName().equals(methodName))
                .collect(Collectors.toList());
        assertThat(desiredMethods).isNotEmpty();
        Stream<AnnotatedType> annotatedTypes = desiredMethods.stream()
                .map(method -> Arrays.stream(method.getParameters())
                        .filter(parameter -> parameter.getName().contains(parameterName))
                        .findAny()
                        .get()
                        .getAnnotatedType());
        assertThat(annotatedTypes).allMatch(annotatedType -> Arrays.stream(
                        ((AnnotatedParameterizedType) annotatedType).getAnnotatedActualTypeArguments())
                .filter(t -> t.getType().getTypeName().contains(typeParameter))
                .allMatch(t -> t.isAnnotationPresent(annotation)));
    }
}
