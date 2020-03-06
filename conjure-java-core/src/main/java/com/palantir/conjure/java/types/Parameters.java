/*
 * (c) Copyright 2020 Palantir Technologies Inc. All rights reserved.
 */

package com.palantir.conjure.java.types;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import javax.annotation.Nonnull;
import javax.lang.model.element.Modifier;

final class Parameters {

    private Parameters() {}

    static ParameterSpec nonnullParameter(TypeName typeName, String paramName, Modifier... modifiers) {
        ParameterSpec.Builder builder = ParameterSpec.builder(typeName, paramName, modifiers);
        if (typeName.isPrimitive()) {
            return builder.build();
        }
        return builder.addAnnotation(AnnotationSpec.builder(Nonnull.class).build())
                .build();
    }
}
