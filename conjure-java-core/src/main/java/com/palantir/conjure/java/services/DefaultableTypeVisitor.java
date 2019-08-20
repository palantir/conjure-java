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

enum DefaultableTypeVisitor implements Type.Visitor<Boolean> {
    INSTANCE;

    @Override
    public Boolean visitOptional(OptionalType value) {
        return true;
    }

    @Override
    public Boolean visitList(ListType value) {
        return true;
    }

    @Override
    public Boolean visitSet(SetType value) {
        return true;
    }

    @Override
    public Boolean visitMap(MapType value) {
        return true;
    }

    @Override
    public Boolean visitPrimitive(PrimitiveType value) {
        return false;
    }

    @Override
    public Boolean visitReference(TypeName value) {
        return false;
    }

    @Override
    public Boolean visitExternal(ExternalReference value) {
        return false;
    }

    @Override
    public Boolean visitUnknown(String unknownType) {
        return false;
    }
}
