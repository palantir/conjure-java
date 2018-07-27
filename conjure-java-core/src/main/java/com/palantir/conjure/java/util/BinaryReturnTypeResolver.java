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

package com.palantir.conjure.java.util;

import com.palantir.conjure.spec.AliasDefinition;
import com.palantir.conjure.spec.Type;
import com.palantir.conjure.spec.TypeDefinition;
import com.palantir.conjure.visitor.TypeDefinitionVisitor;
import com.palantir.conjure.visitor.TypeVisitor;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import java.util.Map;

public final class BinaryReturnTypeResolver {

    private BinaryReturnTypeResolver() {}

    /**
     * Recursively examines nested local alias references to see if the leaf node is of a binary type. If so,
     * this method resolves it to a binary type supplied by the {@code binaryTypeName}, otherwise it returns the
     * immediate local reference {@code {@link TypeName}}.
     *
     * @param types - a map of name to conjure definition
     * @param type - the given type to be resolved
     * @param binaryTypeName - the return binary type name if the leaf node is of a binary type
     * @return resolved return reference type
     */
    public static TypeName resolveReturnReferenceType(
            Map<com.palantir.conjure.spec.TypeName, TypeDefinition> types,
            com.palantir.conjure.spec.TypeName type,
            TypeName binaryTypeName) {

        if (!types.containsKey(type)) {
            throw new IllegalStateException("Unknown LocalReferenceType type: " + type);
        }

        TypeDefinition def = types.get(type);
        while (def.accept(TypeDefinitionVisitor.IS_ALIAS)) {
            AliasDefinition aliasDefinition = def.accept(TypeDefinitionVisitor.ALIAS);
            Type conjureType = aliasDefinition.getAlias();
            if (conjureType.accept(TypeVisitor.IS_REFERENCE)) {
                def = types.get(conjureType.accept(TypeVisitor.REFERENCE));
            } else if (conjureType.accept(TypeVisitor.IS_BINARY)) {
                return binaryTypeName;
            } else {
                break;
            }
        }

        return ClassName.get(type.getPackage(), type.getName());
    }
}
