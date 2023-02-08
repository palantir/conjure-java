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
import com.palantir.product.SafeExternalLongAlias;
import com.palantir.product.SafeExternalLongExample;
import com.palantir.product.UndertowExternalLongTestService;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Optional;
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
    public void testService() {
        assertMethodParamHasAnnotation(
                UndertowExternalLongTestService.class, "testExternalLongArg", "externalLong", DoNotLog.class);
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
        Optional<Method> valueOfMethod = Arrays.stream(methods)
                .filter(method -> method.getName().equals(methodName))
                .findAny();
        Parameter[] test = valueOfMethod.get().getParameters();
        assertThat(valueOfMethod)
                .map(method -> Arrays.stream(test)
                        .filter(parameter -> parameter.getName().contains(parameterName))
                        .findAny())
                .isPresent()
                .get()
                .satisfies(parameter -> parameter.get().isAnnotationPresent(annotation));
    }
}
