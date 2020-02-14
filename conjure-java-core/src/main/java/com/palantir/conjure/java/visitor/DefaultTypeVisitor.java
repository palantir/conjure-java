/*
 * (c) Copyright 2019 Palantir Technologies Inc. All rights reserved.
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
