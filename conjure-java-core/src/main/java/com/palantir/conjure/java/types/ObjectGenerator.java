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

import com.palantir.conjure.java.Generator;
import com.palantir.conjure.java.Options;
import com.palantir.conjure.java.util.TypeFunctions;
import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.conjure.spec.TypeDefinition;
import com.palantir.conjure.spec.TypeName;
import com.palantir.conjure.visitor.TypeDefinitionVisitor;
import com.squareup.javapoet.JavaFile;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public final class ObjectGenerator implements Generator {
    private final Options options;

    public ObjectGenerator(Options options) {
        this.options = options;
    }

    @Override
    public Stream<JavaFile> generate(ConjureDefinition definition) {
        List<TypeDefinition> types = definition.getTypes();
        Map<TypeName, TypeDefinition> typesMap = TypeFunctions.toTypesMap(types);
        TypeMapper typeMapper = new TypeMapper(typesMap, options);
        SafetyEvaluator safetyEvaluator = new SafetyEvaluator(typesMap);
        return types.stream().map(typeDef -> generateInner(typeMapper, safetyEvaluator, typesMap, typeDef));
    }

    private JavaFile generateInner(
            TypeMapper typeMapper,
            SafetyEvaluator safetyEvaluator,
            Map<TypeName, TypeDefinition> typesMap,
            TypeDefinition typeDef) {
        if (typeDef.accept(TypeDefinitionVisitor.IS_OBJECT)) {
            return BeanGenerator.generateBeanType(
                    typeMapper, safetyEvaluator, typeDef.accept(TypeDefinitionVisitor.OBJECT), typesMap, options);
        } else if (typeDef.accept(TypeDefinitionVisitor.IS_UNION)) {
            return UnionGenerator.generateUnionType(
                    typeMapper, safetyEvaluator, typesMap, typeDef.accept(TypeDefinitionVisitor.UNION), options);
        } else if (typeDef.accept(TypeDefinitionVisitor.IS_ENUM)) {
            return EnumGenerator.generateEnumType(typeDef.accept(TypeDefinitionVisitor.ENUM), options);
        } else if (typeDef.accept(TypeDefinitionVisitor.IS_ALIAS)) {
            return AliasGenerator.generateAliasType(
                    typeMapper, safetyEvaluator, typeDef.accept(TypeDefinitionVisitor.ALIAS), options);
        } else {
            throw new IllegalArgumentException("Unknown object definition type " + typeDef.getClass());
        }
    }
}
