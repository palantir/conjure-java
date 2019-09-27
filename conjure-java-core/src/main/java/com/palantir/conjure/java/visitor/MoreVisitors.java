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
import com.palantir.conjure.spec.TypeName;

public final class MoreVisitors {
    private MoreVisitors() {}

    public static final IsExternalType IS_EXTERNAL = new IsExternalType();
    public static final ExternalType EXTERNAL = new ExternalType();
    public static final IsInternalReference IS_INTERNAL_REFERENCE = new IsInternalReference();

    private static class IsExternalType extends IsTypeVisitor {
        @Override
        public Boolean visitExternal(ExternalReference _value) {
            return true;
        }
    }

    private static class ExternalType extends DefaultTypeVisitor<ExternalReference> {
        @Override
        public ExternalReference visitExternal(ExternalReference value) {
            return value;
        }
    }

    private static class IsInternalReference extends IsTypeVisitor {
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
}
