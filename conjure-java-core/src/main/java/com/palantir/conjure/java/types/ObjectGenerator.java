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

package com.palantir.conjure.java.types;

import com.google.common.collect.ImmutableSet;
import com.palantir.conjure.java.Option;
import com.palantir.conjure.spec.ErrorDefinition;
import com.palantir.conjure.spec.TypeDefinition;
import com.palantir.conjure.visitor.TypeDefinitionVisitor;
import com.squareup.javapoet.JavaFile;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class ObjectGenerator implements TypeGenerator {

    private final Set<Option> options;

    public ObjectGenerator(Set<Option> options) {
        this.options = options;
    }

    @Override
    public Set<JavaFile> generateTypes(List<TypeDefinition> types) {
        TypeMapper typeMapper = new TypeMapper(types, options);
        return types.stream()
                .map(typeDef -> {
                    if (typeDef.accept(TypeDefinitionVisitor.IS_OBJECT)) {
                        return BeanGenerator.generateBeanType(
                                typeMapper, typeDef.accept(TypeDefinitionVisitor.OBJECT), options);
                    } else if (typeDef.accept(TypeDefinitionVisitor.IS_UNION)) {
                        return UnionGenerator.generateUnionType(
                                typeMapper, typeDef.accept(TypeDefinitionVisitor.UNION));
                    } else if (typeDef.accept(TypeDefinitionVisitor.IS_ENUM)) {
                        return EnumGenerator.generateEnumType(typeDef.accept(TypeDefinitionVisitor.ENUM));
                    } else if (typeDef.accept(TypeDefinitionVisitor.IS_ALIAS)) {
                        return AliasGenerator.generateAliasType(
                                typeMapper, typeDef.accept(TypeDefinitionVisitor.ALIAS));
                    } else {
                        throw new IllegalArgumentException("Unknown object definition type " + typeDef.getClass());
                    }
                })
                .collect(Collectors.toSet());
    }

    @Override
    public Set<JavaFile> generateErrors(List<TypeDefinition> types, List<ErrorDefinition> errors) {
        if (errors.isEmpty()) {
            return ImmutableSet.of();
        }

        TypeMapper typeMapper = new TypeMapper(types, options);
        return ErrorGenerator.generateErrorTypes(typeMapper, errors);
    }
}
