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
import com.palantir.conjure.spec.ServiceDefinition;
import com.palantir.conjure.spec.TypeDefinition;
import com.palantir.conjure.spec.TypeName;
import com.palantir.conjure.visitor.TypeDefinitionVisitor;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ReachabilityMetadataGenerator implements Generator {

    public enum GenerationMode {
        OBJECTS,
        DIALOGUE_INTERFACES
    }

    private final Optional<String> packagePrefix;
    private final Set<GenerationMode> generationModes;

    public ReachabilityMetadataGenerator(Optional<String> packagePrefix, Set<GenerationMode> generationModes) {
        this.packagePrefix = packagePrefix;
        this.generationModes = generationModes;
    }

    @Override
    public Stream<GeneratedFile> generate(ConjureDefinition definition) {
        Stream<GeneratedFile> generatedFileStream = Stream.empty();
        if (generationModes.contains(GenerationMode.OBJECTS)) {
            generatedFileStream = Stream.concat(
                    generatedFileStream,
                    ReflectionMetadataFqcnByPackage.createForObjects(packagePrefix, definition.getTypes()).stream()
                            .map(packageAndFqcns -> new GeneratedReachabilityMetadataFile(
                                    Packages.getPrefixedPackage(packageAndFqcns.packageName(), packagePrefix),
                                    new ReachabilityMetadata(
                                            new ReflectionMetadata(packageAndFqcns.fullyQualifiedClassNames().stream()
                                                    .map(ReflectionMetadataForType::allEnabled)
                                                    .toList())))));
        }
        if (generationModes.contains(GenerationMode.DIALOGUE_INTERFACES)) {
            generatedFileStream = Stream.concat(
                    generatedFileStream,
                    ReflectionMetadataFqcnByPackage.createForDialogueInterfaces(packagePrefix, definition.getServices())
                            .stream()
                            .map(packageAndFqcns -> new GeneratedReachabilityMetadataFile(
                                    Packages.getPrefixedPackage(packageAndFqcns.packageName(), packagePrefix),
                                    // Needed for Dialogue interfaces (the types created by DialogueInterfaceGenerator)
                                    // - isAnnotationPresent, isInterface
                                    // - `of` static factory
                                    new ReachabilityMetadata(
                                            new ReflectionMetadata(packageAndFqcns.fullyQualifiedClassNames().stream()
                                                    .map(ReflectionMetadataForType::allEnabled)
                                                    .toList())))));
        }
        return generatedFileStream;
    }

    private record ReflectionMetadataFqcnByPackage(String packageName, List<String> fullyQualifiedClassNames) {
        static List<ReflectionMetadataFqcnByPackage> createForObjects(
                Optional<String> packagePrefix, List<TypeDefinition> typeDefinitions) {
            return createFqcnsFromPackageToTypeNameMap(
                    typeDefinitions.stream()
                            .collect(Collectors.groupingBy(
                                    type -> type.accept(TypeDefinitionVisitor.TYPE_NAME)
                                            .getPackage(),
                                    Collectors.mapping(
                                            type -> type.accept(TypeDefinitionVisitor.TYPE_NAME),
                                            Collectors.toList()))),
                    packagePrefix);
        }

        static List<ReflectionMetadataFqcnByPackage> createForDialogueInterfaces(
                Optional<String> packagePrefix, List<ServiceDefinition> serviceDefinitions) {
            return createFqcnsFromPackageToTypeNameMap(
                    serviceDefinitions.stream()
                            .collect(Collectors.groupingBy(
                                    serviceDefinition ->
                                            serviceDefinition.getServiceName().getPackage(),
                                    Collectors.mapping(ServiceDefinition::getServiceName, Collectors.toList()))),
                    packagePrefix);
        }

        private static List<ReflectionMetadataFqcnByPackage> createFqcnsFromPackageToTypeNameMap(
                Map<String, List<TypeName>> list, Optional<String> packagePrefix) {
            return list.entrySet().stream()
                    .map(entry -> {
                        String packageName = entry.getKey();
                        List<String> fullyQualifiedTypeNames = entry.getValue().stream()
                                .map(typeName -> {
                                    TypeName prefixedTypeName = Packages.getPrefixedName(typeName, packagePrefix);
                                    return prefixedTypeName.getPackage() + "." + prefixedTypeName.getName();
                                })
                                .toList();
                        return new ReflectionMetadataFqcnByPackage(packageName, fullyQualifiedTypeNames);
                    })
                    .toList();
        }
    }
}
