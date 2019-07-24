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
public interface ConjureEnum<E extends ConjureEnum<E>> {

    /**
     * This method is intended to function roughly equivalently to {@link Enum#valueOf(Class, String)} for
     * Conjure-generated enum types.
     */
    @SuppressWarnings("unchecked")
    static <T extends ConjureEnum<T>> T valueOf(String value,
                                                Class<T> conjureEnumClass) {
        // Reflection and casts are safe here, since all Conjure-generated enums will have a valueOf method.
        // Tests on the generators will verify that this works.
        try {
            return (T) conjureEnumClass.getMethod("valueOf", String.class).invoke(null, value);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Conjure error: unable to find valueOf method.", e);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Conjure error: unable to call valueOf method.", e);
        }
    }

    /**
     * This method is intended to function roughly equivalently to {@link Class#getEnumConstants()} for
     * Conjure-generated enum types.
     */
    @SuppressWarnings("unchecked")
    static <T extends ConjureEnum<T>> T[] values(Class<T> conjureEnumClass) {
        try {
            return (T[]) conjureEnumClass.getMethod("values").invoke(null);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Conjure error: unable to find values method.", e);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Conjure error: unable to call values method.", e);
        }
    }
}
