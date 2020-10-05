/*
 * (c) Copyright 2020 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java.util;

import com.google.common.collect.ImmutableMap;
import com.palantir.conjure.java.visitor.DefaultTypeVisitor;
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
import com.palantir.logsafe.SafeArg;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public final class TypeFunctions {

    public static boolean isAliasType(Type type) {
        return type.accept(new IsTypeVisitor() {
            @Override
            public Boolean visitReference(com.palantir.conjure.spec.TypeName _value) {
                return true;
            }
        });
    }

    public static boolean isOptionalBinary(Type type) {
        return type.accept(new IsTypeVisitor() {
            @Override
            public Boolean visitOptional(OptionalType value) {
                return value.getItemType().accept(TypeVisitor.IS_BINARY);
            }
        });
    }

    public static boolean isBinaryOrOptionalBinary(Type type) {
        return type.accept(TypeVisitor.IS_BINARY) || isOptionalBinary(type);
    }

    public static boolean isCollectionType(Type type) {
        return type.accept(TypeVisitor.IS_LIST) || type.accept(TypeVisitor.IS_SET);
    }

    // Returns the type that the given alias type refers to. For example, if the input type is defined as
    // "alias: integer", the returned type will be the type for "integer". The provided type must be an alias
    // (reference) type.
    public static Type getAliasedType(Type type, List<TypeDefinition> typeDefinitions) {
        com.palantir.logsafe.Preconditions.checkArgument(
                isAliasType(type), "Expected an alias", SafeArg.of("type", type));
        return getAliasedType(
                        type.accept(new AbstractTypeVisitor<com.palantir.conjure.spec.TypeName>() {
                            @Override
                            public com.palantir.conjure.spec.TypeName visitReference(
                                    com.palantir.conjure.spec.TypeName value) {
                                return value;
                            }
                        }),
                        typeDefinitions)
                .get();
    }

    public static Optional<Type> getAliasedType(
            com.palantir.conjure.spec.TypeName typeName, Collection<TypeDefinition> typeDefinitions) {
        // return type definition for the provided alias type
        TypeDefinition typeDefinition = typeDefinitions.stream()
                .filter(typeDef -> {
                    com.palantir.conjure.spec.TypeName currName = typeDef.accept(TypeDefinitionVisitor.TYPE_NAME);
                    String currClassName = currName.getPackage() + "." + currName.getName();
                    return currClassName.equals(typeName.getPackage() + "." + typeName.getName());
                })
                .findFirst()
                .get();

        if (typeDefinition.accept(TypeDefinitionVisitor.IS_ALIAS)) {
            AliasDefinition aliasDefinition = typeDefinition.accept(TypeDefinitionVisitor.ALIAS);
            return Optional.of(aliasDefinition.getAlias());
        }
        return Optional.empty();
    }

    private static final ImmutableMap<PrimitiveType.Value, String> PRIMITIVE_TO_TYPE_NAME = new ImmutableMap.Builder<
                    PrimitiveType.Value, String>()
            .put(PrimitiveType.Value.BEARERTOKEN, "BearerToken")
            .put(PrimitiveType.Value.BOOLEAN, "Boolean")
            .put(PrimitiveType.Value.DATETIME, "DateTime")
            .put(PrimitiveType.Value.DOUBLE, "Double")
            .put(PrimitiveType.Value.INTEGER, "Integer")
            .put(PrimitiveType.Value.RID, "Rid")
            .put(PrimitiveType.Value.SAFELONG, "SafeLong")
            .put(PrimitiveType.Value.STRING, "String")
            .put(PrimitiveType.Value.UUID, "Uuid")
            .build();

    public static String primitiveTypeName(PrimitiveType in) {
        String typeName = PRIMITIVE_TO_TYPE_NAME.get(in.get());
        if (typeName == null) {
            throw new IllegalStateException("unrecognized primitive type: " + in);
        }
        return typeName;
    }

    public static Type toConjureTypeWithoutAliases(final Type in, final Collection<TypeDefinition> typeDefinitions) {
        return in.accept(new Type.Visitor<Type>() {
            @Override
            public Type visitPrimitive(PrimitiveType _value) {
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
                return Type.map(MapType.of(
                        toConjureTypeWithoutAliases(value.getKeyType(), typeDefinitions),
                        toConjureTypeWithoutAliases(value.getValueType(), typeDefinitions)));
            }

            @Override
            public Type visitReference(com.palantir.conjure.spec.TypeName value) {
                return getAliasedType(value, typeDefinitions)
                        .map(aliasedType -> toConjureTypeWithoutAliases(aliasedType, typeDefinitions))
                        .orElse(in);
            }

            @Override
            public Type visitExternal(ExternalReference _value) {
                return in;
            }

            @Override
            public Type visitUnknown(String _unknownType) {
                return in;
            }
        });
    }

    public static final GetTypeVisitor<PrimitiveType> PRIMITIVE_VISITOR = new GetTypeVisitor<PrimitiveType>() {
        @Override
        public PrimitiveType visitPrimitive(PrimitiveType value) {
            return value;
        }
    };

    public static final GetTypeVisitor<OptionalType> OPTIONAL_VISITOR = new GetTypeVisitor<OptionalType>() {
        @Override
        public OptionalType visitOptional(OptionalType value) {
            return value;
        }
    };

    private abstract static class GetTypeVisitor<T> extends DefaultTypeVisitor<T> {
        @Override
        public T visitUnknown(String _unknownType) {
            throw new UnsupportedOperationException();
        }
    }

    private abstract static class AbstractTypeVisitor<T> extends DefaultTypeVisitor<T> {
        @Override
        public T visitDefault() {
            throw new UnsupportedOperationException();
        }
    }

    private abstract static class IsTypeVisitor extends DefaultTypeVisitor<Boolean> {
        @Override
        public final Boolean visitDefault() {
            return false;
        }
    }

    private TypeFunctions() {}
}
