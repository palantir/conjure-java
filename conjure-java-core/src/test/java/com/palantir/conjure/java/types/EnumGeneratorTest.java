/*
 * (c) Copyright 2023 Palantir Technologies Inc. All rights reserved.
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

import static org.assertj.core.api.Assertions.assertThat;

import com.palantir.product.EnumWithFiftyOneValues;
import com.palantir.product.EnumWithFiftyValues;
import org.junit.jupiter.api.Test;

class EnumGeneratorTest {

    @Test
    void enumWithFiftyValuesHasAVisitorBuilder() {
        assertThat(EnumWithFiftyValues.Visitor.class.getMethods())
                .anyMatch(method -> method.getName().equals("builder"));
        EnumWithFiftyValues.Visitor.builder(); // exists
    }

    @Test
    void enumWithFiftyOneValuesHasNoVisitorBuilder() {
        assertThat(EnumWithFiftyOneValues.Visitor.class.getMethods())
                .noneMatch(method -> method.getName().equals("builder"));
    }
}
