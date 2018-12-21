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

package com.palantir.conjure.java.services;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.palantir.conjure.spec.AliasDefinition;
import com.palantir.conjure.spec.ExternalReference;
import com.palantir.conjure.spec.ListType;
import com.palantir.conjure.spec.MapType;
import com.palantir.conjure.spec.OptionalType;
import com.palantir.conjure.spec.PrimitiveType;
import com.palantir.conjure.spec.SetType;
import com.palantir.conjure.spec.Type;
import com.palantir.conjure.spec.TypeDefinition;
import com.palantir.conjure.visitor.TypeDefinitionVisitor;
import com.palantir.conjure.visitor.TypeVisitor;
import com.squareup.javapoet.TypeName;
import java.util.List;
import java.util.Optional;

final class UndertowTypeFunctions {

    static boolean isAliasType(Type type) {
        return type.accept(new IsTypeVisitor() {
            @Override
            public Boolean visitReference(com.palantir.conjure.spec.TypeName value) {
                return true;
            }
        });
    }

    static boolean isOptionalBinary(Type type) {
        return type.accept(new IsTypeVisitor() {
            @Override
            public Boolean visitOptional(OptionalType value) {
                return value.getItemType().accept(TypeVisitor.IS_BINARY);
            }
        });
    }

    // Returns the type that the given alias type refers to. For example, if the input type is defined as
    // "alias: integer", the returned type will be the type for "integer". The provided type must be an alias
    // (reference) type.
    static Type getAliasedType(Type type, List<TypeDefinition> typeDefinitions) {
        Preconditions.checkArgument(isAliasType(type));
        return getAliasedType(type.accept(
                new AbstractTypeVisitor<com.palantir.conjure.spec.TypeName>() {
                    @Override
                    public com.palantir.conjure.spec.TypeName visitReference(com.palantir.conjure.spec.TypeName value) {
                        return value;
                    }
                }), typeDefinitions).get();
    }

    static Optional<Type> getAliasedType(com.palantir.conjure.spec.TypeName typeName,
            List<TypeDefinition> typeDefinitions) {
        // return type definition for the provided alias type
        TypeDefinition typeDefinition = typeDefinitions.stream().filter(typeDef -> {
            com.palantir.conjure.spec.TypeName currName = typeDef.accept(TypeDefinitionVisitor.TYPE_NAME);
            String currClassName = currName.getPackage() + "." + currName.getName();
            return currClassName.equals(typeName.getPackage() + "." + typeName.getName());
        }).findFirst().get();

        if (typeDefinition.accept(TypeDefinitionVisitor.IS_ALIAS)) {
            AliasDefinition aliasDefinition = typeDefinition.accept(TypeDefinitionVisitor.ALIAS);
            return Optional.of(aliasDefinition.getAlias());
        }
        return Optional.empty();
    }

    private static final ImmutableMap<PrimitiveType.Value, String> PRIMITIVE_TO_TYPE_NAME =
            new ImmutableMap.Builder<PrimitiveType.Value, String>()
                    .put(PrimitiveType.Value.BEARERTOKEN, "BearerToken")
                    .put(PrimitiveType.Value.BINARY, "Binary")
                    .put(PrimitiveType.Value.BOOLEAN, "Boolean")
                    .put(PrimitiveType.Value.DATETIME, "DateTime")
                    .put(PrimitiveType.Value.DOUBLE, "Double")
                    .put(PrimitiveType.Value.INTEGER, "Integer")
                    .put(PrimitiveType.Value.RID, "Rid")
                    .put(PrimitiveType.Value.SAFELONG, "SafeLong")
                    .put(PrimitiveType.Value.STRING, "String")
                    .put(PrimitiveType.Value.UUID, "Uuid")
                    .build();

    static String primitiveTypeName(PrimitiveType in) {
        String typeName = PRIMITIVE_TO_TYPE_NAME.get(in.get());
        if (typeName == null) {
            throw new IllegalStateException("unrecognized primitive type: " + in);
        }
        return typeName;
    }

    static Type toConjureTypeWithoutAliases(final Type in, final List<TypeDefinition> typeDefinitions) {
        return in.accept(new Type.Visitor<Type>() {

            @Override
            public Type visitPrimitive(PrimitiveType value) {
                return in;
            }

            @Override
            public Type visitOptional(OptionalType value) {
                return Type.optional(
                        OptionalType.of(toConjureTypeWithoutAliases(value.getItemType(), typeDefinitions)));
            }

            @Override
            public Type visitList(ListType value) {
                return Type.list(ListType.of(toConjureTypeWithoutAliases(value.getItemType(), typeDefinitions)));
            }

            @Override
            public Type visitSet(SetType value) {
                return Type.set(SetType.of(toConjureTypeWithoutAliases(value.getItemType(), typeDefinitions)));
            }

            @Override
            public Type visitMap(MapType value) {
                return Type.map(MapType.of(toConjureTypeWithoutAliases(value.getKeyType(), typeDefinitions),
                        toConjureTypeWithoutAliases(value.getValueType(), typeDefinitions)));
            }

            @Override
            public Type visitReference(com.palantir.conjure.spec.TypeName value) {
                return getAliasedType(value, typeDefinitions)
                        .map(aliasedType -> toConjureTypeWithoutAliases(aliasedType, typeDefinitions))
                        .orElse(in);
            }

            @Override
            public Type visitExternal(ExternalReference value) {
                return in;
            }

            @Override
            public Type visitUnknown(String unknownType) {
                return in;
            }
        });
    }


    static final GetTypeVisitor<PrimitiveType> PRIMITIVE_VISITOR = new GetTypeVisitor<PrimitiveType>() {
        @Override
        public PrimitiveType visitPrimitive(PrimitiveType value) {
            return value;
        }
    };

    static final GetTypeVisitor<OptionalType> OPTIONAL_VISITOR = new GetTypeVisitor<OptionalType>() {
        @Override
        public OptionalType visitOptional(OptionalType value) {
            return value;
        }
    };

    private abstract static class GetTypeVisitor<T> implements TypeVisitor.Default<T> {
        @Override
        public final T visitDefault() {
            throw new UnsupportedOperationException();
        }
    }

    private abstract static class AbstractTypeVisitor<T> implements Type.Visitor<T> {
        @Override
        public T visitPrimitive(PrimitiveType value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public T visitOptional(OptionalType value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public T visitList(ListType value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public T visitSet(SetType value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public T visitMap(MapType value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public T visitReference(com.palantir.conjure.spec.TypeName value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public T visitExternal(ExternalReference value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public T visitUnknown(String unknownType) {
            throw new UnsupportedOperationException();
        }
    }

    private abstract static class IsTypeVisitor implements Type.Visitor<Boolean> {
        @Override
        public Boolean visitPrimitive(PrimitiveType value) {
            return false;
        }

        @Override
        public Boolean visitOptional(OptionalType value) {
            return false;
        }

        @Override
        public Boolean visitList(ListType value) {
            return false;
        }

        @Override
        public Boolean visitSet(SetType value) {
            return false;
        }

        @Override
        public Boolean visitMap(MapType value) {
            return false;
        }

        @Override
        public Boolean visitReference(com.palantir.conjure.spec.TypeName value) {
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

    static TypeName unbox(TypeName input) {
        if (input.isBoxedPrimitive()) {
            return input.unbox();
        }
        return input;
    }

    private UndertowTypeFunctions() {}

}
