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

package com.palantir.conjure.java.parameterized;

import com.google.common.collect.ImmutableList;
import com.palantir.conjure.java.Options;
import com.palantir.conjure.java.parameterized.objects.GeneratorType;
import com.palantir.conjure.java.parameterized.objects.ParameterizedTestCase;
import java.nio.file.Path;
import java.util.List;

public final class TestCases {
    private static final List<ParameterizedTestCase> CASES = ImmutableList.<ParameterizedTestCase>builder()
            .add(ParameterizedTestCase.builder()
                    .name("template")
                    .docs("Testing compilation of example objects with the base set of options from our templates.")
                    .files(Path.of("example-types.yml"))
                    .options(Options.builder()
                            .useImmutableBytes(true)
                            .strictObjects(true)
                            .nonNullCollections(true)
                            .useStagedBuilders(true)
                            .excludeEmptyOptionals(true)
                            .preferObjectBuilders(true)
                            .build())
                    .generatorTypes(GeneratorType.OBJECT)
                    .build())
            .add(ParameterizedTestCase.builder()
                    .name("no-static-factory")
                    .docs("Test generating objects without the static factory methods.")
                    .files(Path.of("example-types-no-static-factory.yml"))
                    .options(Options.builder().preferObjectBuilders(true).build())
                    .generatorTypes(GeneratorType.OBJECT)
                    .build())
            .add(ParameterizedTestCase.builder() // TODO(kkak): This feels slightly redundant to the template case
                    .name("all-examples")
                    .docs("Test generation of all example objects")
                    .files(Path.of("example-types.yml"))
                    .options(Options.builder().useImmutableBytes(true)
                            .strictObjects(true)
                            .nonNullCollections(true)
                            .excludeEmptyOptionals(true)
                            .unionsWithUnknownValues(true)
                            .jetbrainsContractAnnotations(true)
                            .build())
                    .generatorTypes(GeneratorType.OBJECT)
                    .build())
            .add(ParameterizedTestCase.builder()
                    .name("byte-buffer")
                    .docs("Test compatibility with legacy objects with binary fields.")
                    .files(Path.of("example-binary-types.yml"))
                    .options(Options.builder()
                            .excludeEmptyOptionals(true)
                            .jetbrainsContractAnnotations(true)
                            .build())
                    .generatorTypes(GeneratorType.OBJECT)
                    .build())
            .add(ParameterizedTestCase.builder()
                    .name("staged-builder")
                    .docs("Test generation of objects with staged builders.")
                    .files(Path.of("example-staged-types.yml"))
                    .options(Options.builder()
                            .useStagedBuilders(true)
                            .excludeEmptyOptionals(true)
                            .jetbrainsContractAnnotations(true)
                            .build())
                    .generatorTypes(GeneratorType.OBJECT)
                    .build())
            .add(ParameterizedTestCase.builder()
                    .name("strict-staged-builder")
                    .docs("Test generation of objects with strict staged builders.")
                    .files(Path.of("example-strict-staged-types.yml"))
                    .options(Options.builder()
                            .useStrictStagedBuilders(true)
                            .excludeEmptyOptionals(true)
                            .jetbrainsContractAnnotations(true)
                            .build())
                    .generatorTypes(GeneratorType.OBJECT)
                    .build())
            .add(ParameterizedTestCase.builder()
                    .name("both-strict-staged-builder")
                    .docs("Validate that enabling both staged and strict staged builders is equivalent to only enabling strict staged builders.")
                    .files(Path.of("example-strict-staged-types.yml"))
                    .options(Options.builder()
                            .useStagedBuilders(true)
                            .useStrictStagedBuilders(true)
                            .excludeEmptyOptionals(true)
                            .jetbrainsContractAnnotations(true)
                            .build())
                    .generatorTypes(GeneratorType.OBJECT)
                    .build())
            .add(ParameterizedTestCase.builder()
                    .name("exclude-empty-collections")
                    .docs("Testing support for exclusion of empty collections.")
                    .files(Path.of("exclude-empty-collections.yml"))
                    .options(Options.builder()
                            .excludeEmptyCollections(true)
                            .jetbrainsContractAnnotations(true)
                            .build())
                    .generatorTypes(GeneratorType.OBJECT)
                    .build())
            .add(ParameterizedTestCase.builder()
                    .name("primitive-collections")
                    .docs("Testing generation of objects with primitive collections.")
                    .files(Path.of("primitive-collections.yml"))
                    .options(Options.builder()
                            .excludeEmptyCollections(true)
                            .nonNullCollections(true)
                            .useStagedBuilders(true)
                            .build())
                    .generatorTypes(GeneratorType.OBJECT)
                    .build())
            .add(ParameterizedTestCase.builder()
                    .name("strict-primitive-collections")
                    .docs("Testing generation of objects with primitive collections and strict staged builders.")
                    .files(Path.of("primitive-collections-strict.yml"))
                    .options(Options.builder()
                            .excludeEmptyCollections(true)
                            .nonNullCollections(true)
                            .useStrictStagedBuilders(true)
                            .build())
                    .generatorTypes(GeneratorType.OBJECT)
                    .build())
            .add(ParameterizedTestCase.builder()
                    .name("strict-false")
                    .docs("Validating extra properties on empty objects are allowed when the strict objects feature flag is disabled.")
                    .files(Path.of("example-types-strict-objects.yml"))
                    .options(Options.builder()
                            .useImmutableBytes(true)
                            .strictObjects(false)
                            .jetbrainsContractAnnotations(true)
                            .build())
                    .generatorTypes(GeneratorType.OBJECT)
                    .build())
            .add(ParameterizedTestCase.builder()
                    .name("errors")
                    .docs("Testing error generation.")
                    .files(Path.of("example-errors.yml"))
                    .files(Path.of("example-errors-other.yml"))
                    .options(Options.builder()
                            .useImmutableBytes(true)
                            .excludeEmptyOptionals(true)
                            .jetbrainsContractAnnotations(true)
                            .build())
                    .generatorTypes(GeneratorType.ERROR)
                    .build())
            .build();

    public static List<ParameterizedTestCase> get() {
        return CASES;
    }

    private TestCases() {
    }
}
