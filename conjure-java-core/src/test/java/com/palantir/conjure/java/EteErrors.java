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

package com.palantir.conjure.java;

import static org.assertj.core.api.Assertions.assertThat;

import com.palantir.another.ConjureJavaOtherErrors;
import com.palantir.conjure.java.api.errors.RemoteException;
import com.palantir.conjure.java.api.errors.SerializableError;
import com.palantir.conjure.java.api.errors.ServiceException;
import com.palantir.product.ConjureErrors;
import com.palantir.product.ConjureJavaErrors;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import org.junit.jupiter.api.Test;

public class EteErrors {

    @Test
    void testIsError() {
        RemoteException remoteException =
                serializeServiceException(ConjureErrors.invalidServiceDefinition("my-service", "service-def"));

        assertThat(ConjureErrors.isInvalidServiceDefinition(remoteException)).isTrue();
        assertThat(ConjureErrors.isInvalidTypeDefinition(remoteException)).isFalse();
    }

    @Test
    void causeParameter_isNullable() {
        Method[] methods = ConjureErrors.class.getMethods();
        List<Parameter> causeParameters = Arrays.stream(methods)
                .filter(method -> method.getReturnType().equals(ServiceException.class))
                .map(Executable::getParameters)
                .map(parameters -> Arrays.stream(parameters)
                        .filter(parameter -> parameter.getName().equals("cause"))
                        .findAny())
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toUnmodifiableList());
        assertThat(causeParameters).isNotEmpty();
        for (Parameter causeParameter : causeParameters) {
            Nullable annotation = causeParameter.getAnnotation(Nullable.class);
            assertThat(annotation).isNotNull();
        }
    }

    @Test
    void testIsError_differentNamespaces() {
        RemoteException remoteException = serializeServiceException(ConjureJavaErrors.javaCompilationFailed());

        assertThat(ConjureJavaErrors.isJavaCompilationFailed(remoteException)).isTrue();
        assertThat(ConjureJavaOtherErrors.isJavaCompilationFailed(remoteException))
                .isFalse();
    }

    private static RemoteException serializeServiceException(ServiceException serviceException) {
        SerializableError serializable = SerializableError.forException(serviceException);
        return new RemoteException(serializable, 200);
    }
}
