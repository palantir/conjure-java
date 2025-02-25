/*
 * (c) Copyright 2025 Palantir Technologies Inc. All rights reserved.
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

import com.palantir.conjure.java.GeneratedFile;
import com.palantir.conjure.java.GeneratedFile.GeneratedReachabilityMetadataFile;
import com.palantir.conjure.java.Generator;
import com.palantir.conjure.java.types.ReachabilityMetadata.ReflectionMetadata;
import com.palantir.conjure.java.types.ReachabilityMetadata.ReflectionMetadataForType;
import com.palantir.conjure.java.util.Packages;
import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.conjure.spec.TypeDefinition;
import com.palantir.conjure.spec.TypeName;
import com.palantir.conjure.visitor.TypeDefinitionVisitor;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public final class ReachabilityMetadataGenerator implements Generator {

    private final Optional<String> packagePrefix;

    public ReachabilityMetadataGenerator(Optional<String> packagePrefix) {
        this.packagePrefix = packagePrefix;
    }

    @Override
    public Stream<GeneratedFile> generate(ConjureDefinition definition) {
        List<TypeDefinition> types = definition.getTypes();
        return types.stream()
                .map(type -> {
                    TypeName typeName = type.accept(TypeDefinitionVisitor.TYPE_NAME);
                    TypeName prefixedTypeName = Packages.getPrefixedName(typeName, packagePrefix);
                    return new GeneratedReachabilityMetadataFile(
                            prefixedTypeName.getPackage(),
                            prefixedTypeName.getName(),
                            new ReachabilityMetadata(new ReflectionMetadata(
                                    List.of(ReflectionMetadataForType.allEnabled(prefixedTypeName.getName())))));
                });
    }
}
