/*
 * (c) Copyright 2020 Palantir Technologies Inc. All rights reserved.
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

import com.palantir.conjure.java.util.Primitives;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import javax.annotation.Nonnull;
import javax.lang.model.element.Modifier;

final class Parameters {

    private Parameters() {}

    static ParameterSpec nonnullParameter(TypeName typeName, String paramName, Modifier... modifiers) {
        ParameterSpec.Builder builder = ParameterSpec.builder(typeName, paramName, modifiers);
        if (Primitives.isPrimitive(typeName)) {
            return builder.build();
        }
        return builder.addAnnotation(AnnotationSpec.builder(Nonnull.class).build())
                .build();
    }
}
