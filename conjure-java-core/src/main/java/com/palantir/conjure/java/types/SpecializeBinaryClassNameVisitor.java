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

package com.palantir.conjure.java.types;

import com.palantir.conjure.spec.ExternalReference;
import com.palantir.conjure.spec.ListType;
import com.palantir.conjure.spec.MapType;
import com.palantir.conjure.spec.OptionalType;
import com.palantir.conjure.spec.PrimitiveType;
import com.palantir.conjure.spec.SetType;
import com.squareup.javapoet.TypeName;

public final class SpecializeBinaryClassNameVisitor implements ClassNameVisitor {

    private final ClassNameVisitor delegate;
    private final TypeName binaryClassName;

    public SpecializeBinaryClassNameVisitor(ClassNameVisitor delegate, TypeName binaryClassName) {
        this.delegate = delegate;
        this.binaryClassName = binaryClassName;
    }

    @Override
    public TypeName visitPrimitive(PrimitiveType value) {
        if (value.get() == PrimitiveType.Value.BINARY) {
            return binaryClassName;
        } else {
            return delegate.visitPrimitive(value);
        }
    }

    @Override
    public TypeName visitOptional(OptionalType value) {
        return delegate.visitOptional(value);
    }

    @Override
    public TypeName visitList(ListType value) {
        return delegate.visitList(value);
    }

    @Override
    public TypeName visitSet(SetType value) {
        return delegate.visitSet(value);
    }

    @Override
    public TypeName visitMap(MapType value) {
        return delegate.visitMap(value);
    }

    @Override
    public TypeName visitReference(com.palantir.conjure.spec.TypeName value) {
        return delegate.visitReference(value);
    }

    @Override
    public TypeName visitExternal(ExternalReference value) {
        return delegate.visitExternal(value);
    }
}
