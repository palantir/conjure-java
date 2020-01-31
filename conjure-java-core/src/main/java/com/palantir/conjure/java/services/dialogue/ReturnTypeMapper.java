/*
 * (c) Copyright 2019 Palantir Technologies Inc. All rights reserved.
 */

package com.palantir.conjure.java.services.dialogue;

import com.google.common.util.concurrent.ListenableFuture;
import com.palantir.conjure.java.types.TypeMapper;
import com.palantir.conjure.spec.Type;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import java.util.Optional;

public final class ReturnTypeMapper {
    private static final ClassName LISTENABLE_FUTURE = ClassName.get(ListenableFuture.class);
    private final TypeMapper returnTypes;

    public ReturnTypeMapper(TypeMapper returnTypes) {
        this.returnTypes = returnTypes;
    }

    public TypeName baseType(Type type) {
        return returnTypes.getClassName(type);
    }

    public TypeName baseType(Optional<Type> type) {
        return type.map(this::baseType).orElse(TypeName.VOID);
    }

    public TypeName async(Optional<Type> type) {
        return ParameterizedTypeName.get(LISTENABLE_FUTURE, baseType(type).box());
    }
}
