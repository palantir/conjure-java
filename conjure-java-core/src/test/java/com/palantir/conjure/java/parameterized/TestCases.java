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
import com.palantir.conjure.java.parameterized.objects.FilePath;
import com.palantir.conjure.java.parameterized.objects.GeneratorType;
import com.palantir.conjure.java.parameterized.objects.ParameterizedTestCase;
import java.util.List;

public final class TestCases {
    private static final List<ParameterizedTestCase> CASES = ImmutableList.<ParameterizedTestCase>builder()
            .add(ParameterizedTestCase.builder()
                    .name("no-static-factory")
                    .docs("Test generating objects without the static factory methods.")
                    .files(FilePath.of("src/test/resources/example-types-no-static-factory.yml"))
                    .options(Options.builder().preferObjectBuilders(true).build())
                    .generators(GeneratorType.OBJECT)
                    .build())
            .build();

    public static List<ParameterizedTestCase> get() {
        return CASES;
    }
}
