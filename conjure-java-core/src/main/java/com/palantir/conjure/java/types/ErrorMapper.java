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

import com.palantir.conjure.spec.ErrorDefinition;
import com.palantir.conjure.spec.TypeName;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ErrorMapper {

    private final Map<TypeName, ErrorDefinition> errors;

    public ErrorMapper(List<ErrorDefinition> errors) {
        this.errors = errors.stream().collect(Collectors.toMap(ErrorDefinition::getErrorName, Function.identity()));
    }

    public final Optional<ErrorDefinition> getError(TypeName typeName) {
        return Optional.ofNullable(errors.get(typeName));
    }
}
