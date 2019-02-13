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

import static com.palantir.logsafe.testing.Assertions.assertThatLoggableExceptionThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

import com.palantir.insensitive.InsensitiveEnum;
import com.palantir.product.EnumExample;
import org.junit.Test;

public class EnumTests {

    @Test
    public void testVisitOne() {
        EnumExample enumExample = EnumExample.ONE;
        assertThat(enumExample.accept(Visitor.INSTANCE)).isEqualTo("one");
    }

    @Test
    public void testValueOfProducesSameInstance() {
        assertThat(EnumExample.valueOf("ONE")).isSameAs(EnumExample.ONE);
    }

    @Test
    public void testUnknown() {
        EnumExample enumExample = EnumExample.valueOf("SOME_VALUE");
        assertThat(enumExample.get()).isEqualTo(EnumExample.Value.UNKNOWN);
        assertThat(enumExample.toString()).isEqualTo("SOME_VALUE");
    }

    @Test
    public void testVisitUnknown() {
        EnumExample enumExample = EnumExample.valueOf("SOME_VALUE");
        assertThat(enumExample.accept(Visitor.INSTANCE)).isEqualTo("SOME_VALUE");
    }

    @Test
    public void testInsensitiveEnum_lowerCase() {
        assertThat(InsensitiveEnum.valueOf("one")).isEqualTo(InsensitiveEnum.ONE);
    }

    @Test
    public void testInsensitiveEnum_lowerCaseUnknown() {
        InsensitiveEnum value = InsensitiveEnum.valueOf("notknown");
        assertThat(value.get()).isEqualTo(InsensitiveEnum.Value.UNKNOWN);
    }

    @Test
    public void testNullValidationUsesSafeLoggable() {
        assertThatLoggableExceptionThrownBy(() -> EnumExample.valueOf(null)).hasLogMessage("value cannot be null");
    }

    private enum Visitor implements EnumExample.Visitor<String> {
        INSTANCE;

        @Override
        public String visitOne() {
            return "one";
        }

        @Override
        public String visitTwo() {
            return "two";
        }

        @Override
        public String visitOneHundred() {
            return "one hundred";
        }

        @Override
        public String visitUnknown(String unknownValue) {
            return unknownValue;
        }
    }
}
