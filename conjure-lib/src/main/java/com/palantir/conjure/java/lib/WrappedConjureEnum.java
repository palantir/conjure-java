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

/**
 * Represents a native Java enum that is the underlying value of a {@link ConjureEnum}.
 */
public interface WrappedConjureEnum<E extends Enum<E> & WrappedConjureEnum<E, T>, T extends ConjureEnum<T, E>> {
    /**
     * Returns the wrapped Conjure-generated enum corresponding to this enum entry.
     *
     * This method satisfies the contract that if {@code e} is a {@code WrappedConjureEnum}, then {@code e.toWrapped()
     * .get() == e}.
     */
    T toWrapper();
}
