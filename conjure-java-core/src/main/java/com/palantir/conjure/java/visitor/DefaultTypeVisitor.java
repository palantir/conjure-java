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
import com.palantir.logsafe.exceptions.SafeIllegalStateException;

@SuppressWarnings("DesignForExtension")
public abstract class DefaultTypeVisitor<T> implements Type.Visitor<T> {
    @Override
    public T visitPrimitive(PrimitiveType _value) {
        return visitDefault();
    }

    @Override
    public T visitOptional(OptionalType _value) {
        return visitDefault();
    }

    @Override
    public T visitList(ListType _value) {
        return visitDefault();
    }

    @Override
    public T visitSet(SetType _value) {
        return visitDefault();
    }

    @Override
    public T visitMap(MapType _value) {
        return visitDefault();
    }

    @Override
    public T visitReference(com.palantir.conjure.spec.TypeName _value) {
        return visitDefault();
    }

    @Override
    public T visitExternal(ExternalReference _value) {
        return visitDefault();
    }

    @Override
    public T visitUnknown(String _unknownType) {
        return visitDefault();
    }

    public T visitDefault() {
        throw new SafeIllegalStateException("Unexpected type");
    }
}
