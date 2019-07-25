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

package com.palantir.conjure.java.lib;

import java.lang.reflect.InvocationTargetException;

/**
 * This is the common interface for all Conjure-generated Enum types.
 */
public interface ConjureEnum<E extends Enum<E>> {

    E get();

    @SuppressWarnings("unchecked")
    static <E extends Enum<E>, T extends ConjureEnum<E>> T valueOf(E enumValue,
                                                                   Class<T> conjureEnumClass) {
        // Reflection and casts are safe here, since all Conjure-generated enums will have a valueOf method.
        // Tests on the generators will verify that this works.
        try {
            return (T) conjureEnumClass.getMethod("valueOf", String.class).invoke(null, enumValue.name());
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Conjure error: unable to find valueOf method.", e);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Conjure error: unable to call valueOf method.", e);
        }
    }

}
