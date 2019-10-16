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

package com.palantir.conjure.java;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ImmutableList;
import com.palantir.product.CovariantListExample;
import com.palantir.product.CovariantOptionalExample;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@Execution(ExecutionMode.CONCURRENT)
public class CovariantsTest {

    @Test
    public void covariantOptionalBuilderTest() {
        String value = "foo";
        Optional<String> maybeValue = Optional.of(value);

        CovariantOptionalExample covExample = CovariantOptionalExample.builder().item(maybeValue).build();
        // without covariants item would be Optional<Optional<String>>
        assertThat(covExample.getItem()).isEqualTo(maybeValue);
    }

    @Test
    public void covarianListBuilderTest() {
        String value = "foo";
        List<String> values = ImmutableList.of(value);

        Integer otherValue = 1;
        List<Integer> otherValues = ImmutableList.of(otherValue);

        CovariantListExample covExample =
                CovariantListExample.builder().addAllItems(values).addAllItems(otherValues).build();
        // without covariants item would be Optional<Optional<String>>
        assertThat(covExample.getItems()).isEqualTo(ImmutableList.of(value, otherValue));
    }
}
