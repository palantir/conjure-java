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

package com.palantir.conjure.visitor;

import com.palantir.conjure.spec.ExternalReference;
import com.palantir.conjure.spec.ListType;
import com.palantir.conjure.spec.MapType;
import com.palantir.conjure.spec.OptionalType;
import com.palantir.conjure.spec.PrimitiveType;
import com.palantir.conjure.spec.SetType;
import com.palantir.conjure.spec.Type;
import com.palantir.conjure.spec.TypeName;

public final class TypeVisitor {

    private TypeVisitor() {}

    public static final PrimitiveTypeVisitor PRIMITIVE = new PrimitiveTypeVisitor();
    public static final MapTypeVisitor MAP = new MapTypeVisitor();
    public static final ListTypeVisitor LIST = new ListTypeVisitor();
    public static final SetTypeVisitor SET = new SetTypeVisitor();
    public static final OptionalTypeVisitor OPTIONAL = new OptionalTypeVisitor();
    public static final ReferenceTypeVisitor REFERENCE = new ReferenceTypeVisitor();

    public static final IsPrimitiveTypeVisitor IS_PRIMITIVE = new IsPrimitiveTypeVisitor();
    public static final IsOptionalTypeVisitor IS_OPTIONAL = new IsOptionalTypeVisitor();
    public static final IsListTypeVisitor IS_LIST = new IsListTypeVisitor();
    public static final IsSetTypeVisitor IS_SET = new IsSetTypeVisitor();
    public static final IsMapTypeVisitor IS_MAP = new IsMapTypeVisitor();
    public static final IsReferenceTypeVisitor IS_REFERENCE = new IsReferenceTypeVisitor();
    public static final IsInternalReferenceTypeVisitor IS_INTERNAL_REFERENCE = new IsInternalReferenceTypeVisitor();
    public static final IsPrimitiveOrReferenceType IS_PRIMITIVE_OR_REFERENCE = new IsPrimitiveOrReferenceType();
    public static final IsBinaryType IS_BINARY = new IsBinaryType();
    public static final IsAnyType IS_ANY = new IsAnyType();

    public interface Default<T> extends Type.Visitor<T> {
        @Override
        default T visitPrimitive(PrimitiveType value) {
            return visitDefault();
        }

        @Override
        default T visitOptional(OptionalType value) {
            return visitDefault();
        }

        @Override
        default T visitList(ListType value) {
            return visitDefault();
        }

        @Override
        default T visitSet(SetType value) {
            return visitDefault();
        }

        @Override
        default T visitMap(MapType value) {
            return visitDefault();
        }

        @Override
        default T visitReference(TypeName value) {
            return visitDefault();
        }

        @Override
        default T visitExternal(ExternalReference value) {
            return visitDefault();
        }

        @Override
        default T visitUnknown(String unknownType) {
            return visitDefault();
        }

        T visitDefault();
    }

    private static class IsPrimitiveTypeVisitor extends IsTypeVisitor {
        @Override
        public Boolean visitPrimitive(PrimitiveType value) {
            return true;
        }
    }

    private static class IsOptionalTypeVisitor extends IsTypeVisitor {
        @Override
        public Boolean visitOptional(OptionalType value) {
            return true;
        }
    }

    private static class IsListTypeVisitor extends IsTypeVisitor {
        @Override
        public Boolean visitList(ListType value) {
            return true;
        }
    }

    private static class IsSetTypeVisitor extends IsTypeVisitor {
        @Override
        public Boolean visitSet(SetType value) {
            return true;
        }
    }

    private static class IsMapTypeVisitor extends IsTypeVisitor {
        @Override
        public Boolean visitMap(MapType value) {
            return true;
        }
    }

    private static class IsReferenceTypeVisitor extends IsTypeVisitor {
        @Override
        public Boolean visitReference(TypeName value) {
            return true;
        }

        @Override
        public Boolean visitExternal(ExternalReference value) {
            return true;
        }
    }

    private static class IsInternalReferenceTypeVisitor extends IsTypeVisitor {
        @Override
        public Boolean visitReference(TypeName value) {
            return true;
        }
    }

    private static class IsPrimitiveOrReferenceType extends IsTypeVisitor {
        @Override
        public Boolean visitPrimitive(PrimitiveType value) {
            return true;
        }

        @Override
        public Boolean visitReference(TypeName value) {
            return true;
        }

        @Override
        public Boolean visitExternal(ExternalReference value) {
            return true;
        }
    }

    private static class IsBinaryType extends IsTypeVisitor {
        @Override
        public Boolean visitPrimitive(PrimitiveType value) {
            return value.get() == PrimitiveType.Value.BINARY;
        }
    }

    private static class IsAnyType extends IsTypeVisitor {
        @Override
        public Boolean visitPrimitive(PrimitiveType value) {
            return value.get() == PrimitiveType.Value.ANY;
        }
    }

    private static class IsTypeVisitor implements Default<Boolean> {
        @Override
        public Boolean visitDefault() {
            return false;
        }
    }

    private static class DefaultToThrowingTypeVisitor<T> implements Type.Visitor<T> {
        @Override
        public T visitPrimitive(PrimitiveType value) {
            return null;
        }

        @Override
        public T visitOptional(OptionalType value) {
            throw new IllegalStateException("Unsupported type: " + value);
        }

        @Override
        public T visitList(ListType value) {
            throw new IllegalStateException("Unsupported type: " + value);
        }

        @Override
        public T visitSet(SetType value) {
            throw new IllegalStateException("Unsupported type: " + value);
        }

        @Override
        public T visitMap(MapType value) {
            throw new IllegalStateException("Unsupported type: " + value);
        }

        @Override
        public T visitReference(TypeName value) {
            throw new IllegalStateException("Unsupported type: " + value);
        }

        @Override
        public T visitExternal(ExternalReference value) {
            throw new IllegalStateException("Unsupported type: " + value);
        }

        @Override
        public T visitUnknown(String unknownType) {
            throw new IllegalStateException("Unsupported type: " + unknownType);
        }
    }

    private static class PrimitiveTypeVisitor extends DefaultToThrowingTypeVisitor<PrimitiveType> {
        @Override
        public PrimitiveType visitPrimitive(PrimitiveType value) {
            return value;
        }
    }

    private static class ReferenceTypeVisitor extends DefaultToThrowingTypeVisitor<TypeName> {
        @Override
        public TypeName visitReference(TypeName value) {
            return value;
        }

        @Override
        public TypeName visitExternal(ExternalReference value) {
            return value.getExternalReference();
        }
    }

    private static class MapTypeVisitor extends DefaultToThrowingTypeVisitor<MapType> {
        @Override
        public MapType visitMap(MapType value) {
            return value;
        }
    }

    private static class ListTypeVisitor extends DefaultToThrowingTypeVisitor<ListType> {
        @Override
        public ListType visitList(ListType value) {
            return value;
        }
    }

    private static class SetTypeVisitor extends DefaultToThrowingTypeVisitor<SetType> {
        @Override
        public SetType visitSet(SetType value) {
            return value;
        }
    }

    private static class OptionalTypeVisitor extends DefaultToThrowingTypeVisitor<OptionalType> {
        @Override
        public OptionalType visitOptional(OptionalType value) {
            return value;
        }
    }
}
