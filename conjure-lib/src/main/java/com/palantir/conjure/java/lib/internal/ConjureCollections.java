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

package com.palantir.conjure.java.lib.internal;

import com.palantir.logsafe.Preconditions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * Utility functions for conjure. Consumers should prefer to use something like guava instead of using these functions
 * directly.
 */
public final class ConjureCollections {

    private ConjureCollections() {
        // cannot instantiate
    }

    @SuppressWarnings("unchecked")
    public static <T> void addAll(Collection<T> addTo, Iterable<? extends T> elementsToAdd) {
        Preconditions.checkNotNull(elementsToAdd, "elementsToAdd cannot be null");
        if (elementsToAdd instanceof Collection) {
            // This special-casing allows us to take advantage of the more performant
            // ArrayList#addAll method which does a single System.arraycopy.
            addTo.addAll((Collection<T>) elementsToAdd);
        } else {
            for (T element : elementsToAdd) {
                addTo.add(element);
            }
        }
    }

    @SuppressWarnings({"IllegalType", "unchecked"}) // explicitly need to return mutable list for generated builders
    public static <T> ArrayList<T> newArrayList(Iterable<? extends T> iterable) {
        Preconditions.checkNotNull(iterable, "iterable cannot be null");
        if (iterable instanceof Collection) {
            return new ArrayList<>((Collection<T>) iterable);
        }
        ArrayList<T> list = new ArrayList<>();
        for (T item : iterable) {
            list.add(item);
        }
        return list;
    }

    @SuppressWarnings("IllegalType") // explicitly need to return mutable list for generated builders
    public static <T> LinkedHashSet<T> newLinkedHashSet(Iterable<? extends T> iterable) {
        Preconditions.checkNotNull(iterable, "iterable cannot be null");
        if (iterable instanceof Collection) {
            return new LinkedHashSet<>((Collection<T>) iterable);
        }
        LinkedHashSet<T> set = new LinkedHashSet<>();
        for (T item : iterable) {
            set.add(item);
        }
        return set;
    }
}
