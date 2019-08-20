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

package com.palantir.conjure.java.services;

import com.palantir.conjure.spec.ExternalReference;
import com.palantir.conjure.spec.ListType;
import com.palantir.conjure.spec.MapType;
import com.palantir.conjure.spec.OptionalType;
import com.palantir.conjure.spec.PrimitiveType;
import com.palantir.conjure.spec.SetType;
import com.palantir.conjure.spec.Type;
import com.palantir.conjure.spec.TypeName;
import com.palantir.conjure.visitor.TypeVisitor;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import com.squareup.javapoet.CodeBlock;
import java.util.Collections;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;

enum DefaultTypeValueVisitor implements Type.Visitor<CodeBlock> {
    INSTANCE;

    @Override
    public CodeBlock visitOptional(OptionalType value) {
        if (value.getItemType().accept(TypeVisitor.IS_PRIMITIVE)) {
            PrimitiveType primitiveType = value.getItemType().accept(TypeVisitor.PRIMITIVE);
            // special handling for primitive optionals with Java 8
            if (primitiveType.equals(PrimitiveType.DOUBLE)) {
                return CodeBlock.of("$T.empty()", OptionalDouble.class);
            } else if (primitiveType.equals(PrimitiveType.INTEGER)) {
                return CodeBlock.of("$T.empty()", OptionalInt.class);
            }
        }
        return CodeBlock.of("$T.empty()", Optional.class);
    }

    @Override
    public CodeBlock visitList(ListType value) {
        return CodeBlock.of("$T.emptyList()", Collections.class);
    }

    @Override
    public CodeBlock visitSet(SetType value) {
        return CodeBlock.of("$T.emptySet()", Collections.class);
    }

    @Override
    public CodeBlock visitMap(MapType value) {
        return CodeBlock.of("$T.emptyMap()", Collections.class);
    }

    @Override
    public CodeBlock visitPrimitive(PrimitiveType value) {
        throw new SafeIllegalArgumentException("Cannot backfill non-defaultable parameter type.");
    }


    @Override
    public CodeBlock visitReference(TypeName value) {
        throw new SafeIllegalArgumentException("Cannot backfill non-defaultable parameter type.");
    }

    @Override
    public CodeBlock visitExternal(ExternalReference value) {
        throw new SafeIllegalArgumentException("Cannot backfill non-defaultable parameter type.");
    }

    @Override
    public CodeBlock visitUnknown(String unknownType) {
        throw new SafeIllegalArgumentException("Cannot backfill non-defaultable parameter type.");
    }
}
