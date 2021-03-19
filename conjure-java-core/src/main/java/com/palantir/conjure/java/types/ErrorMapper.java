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

package com.palantir.conjure.java.types;

import com.google.common.base.Preconditions;
import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.conjure.spec.ErrorDefinition;
import com.palantir.conjure.spec.TypeName;
import com.squareup.javapoet.ClassName;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ErrorMapper {

    private final Map<TypeName, ErrorDefinition> errors;
    private final Set<TypeName> checkedExceptions;

    public ErrorMapper(ConjureDefinition conjureDefinition) {
        this.errors = conjureDefinition.getErrors().stream()
                .collect(Collectors.toMap(ErrorDefinition::getErrorName, Function.identity()));
        this.checkedExceptions = conjureDefinition.getServices().stream()
                .flatMap(stream -> stream.getEndpoints().stream())
                .flatMap(endpoint -> endpoint.getErrors().stream())
                .collect(Collectors.toSet());
    }

    public final boolean isChecked(ErrorDefinition errorDefinition) {
        return checkedExceptions.contains(errorDefinition.getErrorName());
    }

    public final ErrorDefinition getError(TypeName typeName) {
        return Preconditions.checkNotNull(errors.get(typeName), "No error found with name %s", typeName.getName());
    }

    public final ClassName getServiceClassNameForError(TypeName typeName) {
        ErrorDefinition error = getError(typeName);
        return ClassName.get(
                typeName.getPackage(), error.getNamespace().get() + "Errors", typeName.getName() + "ServiceException");
    }

    public final ClassName getRemoteClassNameForError(TypeName typeName) {
        ErrorDefinition error = getError(typeName);
        return ClassName.get(
                typeName.getPackage(), error.getNamespace().get() + "Errors", typeName.getName() + "RemoteException");
    }

    public final String getNameForServiceException(ErrorDefinition error) {
        return String.format("%sServiceException", error.getErrorName().getName());
    }

    public final String getNameForRemoteException(ErrorDefinition error) {
        return String.format("%sRemoteException", error.getErrorName().getName());
    }
}
