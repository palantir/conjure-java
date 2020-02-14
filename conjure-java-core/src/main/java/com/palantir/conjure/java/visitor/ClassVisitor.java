/*
 * (c) Copyright 2018 Palantir Technologies Inc. All rights reserved.
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

import com.palantir.conjure.java.Options;
import com.palantir.conjure.java.types.ClassNameVisitor;
import com.palantir.conjure.java.types.DefaultClassNameVisitor;
import com.palantir.conjure.spec.ExternalReference;
import com.palantir.conjure.spec.ListType;
import com.palantir.conjure.spec.MapType;
import com.palantir.conjure.spec.OptionalType;
import com.palantir.conjure.spec.PrimitiveType;
import com.palantir.conjure.spec.SetType;
import com.palantir.conjure.spec.TypeDefinition;
import com.palantir.dialogue.BinaryRequestBody;
import com.squareup.javapoet.TypeName;
import java.io.InputStream;
import java.util.List;

public final class ClassVisitor implements ClassNameVisitor {

    public enum Mode {
        RETURN_VALUE,
        PARAMETER
    }

    private final DefaultClassNameVisitor delegate;
    private final Mode mode;

    public ClassVisitor(List<TypeDefinition> types, Options options, Mode mode) {
        this.delegate = new DefaultClassNameVisitor(types, options);
        this.mode = mode;
    }

    @Override
    public TypeName visitPrimitive(PrimitiveType value) {
        if (!value.equals(PrimitiveType.BINARY)) {
            return delegate.visitPrimitive(value);
        }

        // TODO(forozco): handle optional binary response
        if (mode == Mode.RETURN_VALUE) {
            return TypeName.get(InputStream.class);
        } else {
            return TypeName.get(BinaryRequestBody.class);
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
