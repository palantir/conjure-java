/*
 * (c) Copyright 2019 Palantir Technologies Inc. All rights reserved.
 */

package com.palantir.conjure.java.visitor;

import com.palantir.conjure.spec.ExternalReference;
import com.palantir.conjure.spec.ListType;
import com.palantir.conjure.spec.MapType;
import com.palantir.conjure.spec.OptionalType;
import com.palantir.conjure.spec.PrimitiveType;
import com.palantir.conjure.spec.SetType;
import com.palantir.conjure.spec.Type;

public abstract class DefaultTypeVisitor<T> implements Type.Visitor<T> {
    @Override
    public T visitPrimitive(PrimitiveType value) {
        return visitDefault();
    }

    @Override
    public T visitOptional(OptionalType value) {
        return visitDefault();
    }

    @Override
    public T visitList(ListType value) {
        return visitDefault();
    }

    @Override
    public T visitSet(SetType value) {
        return visitDefault();
    }

    @Override
    public T visitMap(MapType value) {
        return visitDefault();
    }

    @Override
    public T visitReference(com.palantir.conjure.spec.TypeName value) {
        return visitDefault();
    }

    @Override
    public T visitExternal(ExternalReference value) {
        return visitDefault();
    }

    @Override
    public T visitUnknown(String unknownType) {
        return visitDefault();
    }

    public T visitDefault() {
        throw new IllegalStateException("Unexpected type");
    }
}
