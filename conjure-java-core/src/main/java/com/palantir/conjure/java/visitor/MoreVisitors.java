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
import com.palantir.conjure.spec.TypeName;

public final class MoreVisitors {
    private MoreVisitors() {}

    public static final IsExternalType IS_EXTERNAL = new IsExternalType();
    public static final ExternalType EXTERNAL = new ExternalType();
    public static final IsInternalReference IS_INTERNAL_REFERENCE = new IsInternalReference();
    public static final IsCollection IS_COLLECTION = new IsCollection();

    private static final class IsExternalType extends IsTypeVisitor {
        @Override
        public Boolean visitExternal(ExternalReference _value) {
            return true;
        }
    }

    private static final class ExternalType extends DefaultTypeVisitor<ExternalReference> {
        @Override
        public ExternalReference visitExternal(ExternalReference value) {
            return value;
        }
    }

    private static final class IsInternalReference extends IsTypeVisitor {
        @Override
        public Boolean visitReference(TypeName _value) {
            return true;
        }
    }

    /** Copied from {@link com.palantir.conjure.visitor.TypeVisitor.IsTypeVisitor}. */
    private static class IsTypeVisitor implements Type.Visitor<Boolean> {
        @Override
        public Boolean visitPrimitive(PrimitiveType _value) {
            return false;
        }

        @Override
        public Boolean visitOptional(OptionalType _value) {
            return false;
        }

        @Override
        public Boolean visitList(ListType _value) {
            return false;
        }

        @Override
        public Boolean visitSet(SetType _value) {
            return false;
        }

        @Override
        public Boolean visitMap(MapType _value) {
            return false;
        }

        @Override
        public Boolean visitReference(TypeName _value) {
            return false;
        }

        @Override
        public Boolean visitExternal(ExternalReference _value) {
            return false;
        }

        @Override
        public Boolean visitUnknown(String _unknownType) {
            return false;
        }
    }

    private static final class IsCollection extends IsTypeVisitor {
        @Override
        public Boolean visitList(ListType _value) {
            return true;
        }

        @Override
        public Boolean visitSet(SetType _value) {
            return true;
        }

        @Override
        public Boolean visitMap(MapType _value) {
            return true;
        }
    }
}
