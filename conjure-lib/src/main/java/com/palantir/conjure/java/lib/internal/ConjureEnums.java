/*
 * (c) Copyright 2019 Palantir Technologies Inc. All rights reserved.
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

import com.palantir.logsafe.UnsafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;

/**
 * Internal utility functions for conjure enum types.
 */
public final class ConjureEnums {

    private ConjureEnums() {
        // cannot instantiate
    }

    public static void validate(String value) {
        if (value.isEmpty()) {
            throw new SafeIllegalArgumentException("Enum values must not be empty");
        }

        int length = value.length();
        for (int index = 0; index < length; index++) {
            if (!isAllowedCharacter(value.charAt(index))) {
                throw new SafeIllegalArgumentException("Enum values must use UPPER_SNAKE_CASE",
                        UnsafeArg.of("value", value));
            }
        }
    }

    private static boolean isAllowedCharacter(char character) {
        return (character >= 'A' && character <= 'Z') || character == '_';
    }
}
