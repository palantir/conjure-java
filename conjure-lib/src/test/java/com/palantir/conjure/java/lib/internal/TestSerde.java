/*
 * (c) Copyright 2024 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java.lib.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.palantir.conjure.java.lib.Doubles;
import org.assertj.core.api.Assertions;
import org.eclipse.collections.impl.list.mutable.primitive.DoubleArrayList;
import org.junit.jupiter.api.Test;

public class TestSerde {
    private static ObjectMapper mapper = new ObjectMapper();
    private static String json = "[1.1, 2.2, 3.3, 4.4, 5.5]";
    private static double[] expectedDoubles = new double[]{1.1, 2.2, 3.3, 4.4, 5.5};

    @Test
    public void testDeser() throws JsonProcessingException {
        // We will need to make sure that when we attempt to deser a primitive type that
        // we are piping thru adequate information so that jackson knows it should
        ConjureDoubleList expected = new ConjureDoubleList(new DoubleArrayList(expectedDoubles));
        ConjureDoubleList test = mapper.readValue(json, ConjureDoubleList.class);
        Assertions.assertThat(test).isEqualTo(expected);
    }

    // To do this right we would need a "Doubles" for the 4 types:
    //   double, int, long, boolean (this one seems suspect)
    // And then thread that through
    // Doing this would mean that we could remove eclipse collections from our API
    @Test
    public void testDeserDoubles() throws JsonProcessingException {
        // We will need to make sure that when we attempt to deser a primitive type that
        // we are piping thru adequate information so that jackson knows it should
        Doubles expected = Doubles.from(expectedDoubles);
        Doubles test = mapper.readValue(json, Doubles.class);
        Assertions.assertThat(test).isEqualTo(expected);
    }
}
