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

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;
import java.util.Optional;
import org.immutables.value.Value;

@Value.Immutable
@StagedBuilder
public interface ReturnType {
    TypeName returnType();

    CodeBlock serializerFactory();

    String serializerFieldName();

    Optional<TypeName> asyncInnerType();

    @Value.Derived
    default boolean isVoid() {
        TypeName type = asyncInnerType().orElseGet(this::returnType);
        return type.box().equals(TypeName.VOID.box());
    }
}
