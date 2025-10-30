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

import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.logsafe.Preconditions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.eclipse.collections.impl.list.mutable.primitive.DoubleArrayList;
import org.eclipse.collections.impl.list.mutable.primitive.IntArrayList;
import org.eclipse.collections.impl.list.mutable.primitive.LongArrayList;

/**
 * Utility functions for conjure. Consumers should prefer to use something like guava instead of using these functions
 * directly.
 */
public final class ConjureCollections {

    private ConjureCollections() {
        // cannot instantiate
    }

    /*
     * This is bizarre. Allow me to explain...
     *
     * We do _not_ want to expose the Conjure*List types externally
     * but we also want the optimizations they provide to make it thru
     * to jackson for serialization. So the runtime type needs to be
     * preserved while also not exposing the type :phew:.
     *
     * To achieve this we have to do some gymnastics surrounding the type
     * system. We need this to return the type of the list given, but also
     * return specific Conjure types when detected. This requires that we
     * erase the type info, but we know this is safe because we are directly
     * returning the same type which is by definition the identity function.
     * Therefore the input List<T> is the same types as the output List<T>.
     */
    public static <T> List<T> unmodifiableList(List<T> list) {
        // Return the unmodifiable version of the Eclipse types
        if (list instanceof ConjureIntegerList conjureIntegerList) {
            return (List<T>) conjureIntegerList.asUnmodifiable();
        } else if (list instanceof ConjureDoubleList conjureDoubleList) {
            return (List<T>) conjureDoubleList.asUnmodifiable();
        } else if (list instanceof ConjureSafeLongList conjureSafeLongList) {
            return (List<T>) conjureSafeLongList.asUnmodifiable();
        } else {
            // Otherwise use the JDK types
            return Collections.unmodifiableList(list);
        }
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

    @SuppressWarnings("unchecked")
    public static <T> void addAllAndCheckNonNull(Collection<T> addTo, Iterable<? extends T> elementsToAdd) {
        if (elementsToAdd instanceof ConjureIntegerList
                || elementsToAdd instanceof ConjureDoubleList
                || elementsToAdd instanceof ConjureSafeLongList) {
            // Primitive lists will never have null elements.
            addAll(addTo, elementsToAdd);
            return;
        }
        Preconditions.checkNotNull(elementsToAdd, "elementsToAdd cannot be null");
        // If we know the number of elements we are adding and the addTo Collection is an ArrayList, we can eagerly
        // resize it to only do one grow() of the array.
        if (elementsToAdd instanceof Collection) {
            Collection<T> collectionElementsToAdd = (Collection<T>) elementsToAdd;
            if (addTo instanceof ArrayList) {
                ((ArrayList<T>) addTo).ensureCapacity(collectionElementsToAdd.size() + addTo.size());
            }
        }
        for (T element : elementsToAdd) {
            Preconditions.checkNotNull(element, "elementsToAdd cannot contain null elements");
            addTo.add(element);
        }
    }

    // Prefer to use newList(iterable)
    // explicitly need to return mutable list for generated builders
    @SuppressWarnings({"IllegalType", "unchecked", "NonApiType"})
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

    // Prefer to use newSet(iterable)
    @SuppressWarnings({"IllegalType", "NonApiType"}) // explicitly need to return mutable list for generated builders
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

    public static <T> List<T> newList() {
        return new ArrayList<>();
    }

    public static <T> List<T> newList(Iterable<? extends T> iterable) {
        return newArrayList(iterable);
    }

    public static <T> List<T> newNonNullList() {
        return new ArrayList<>();
    }

    public static <T> List<T> newNonNullList(Iterable<? extends T> iterable) {
        List<T> arrayList = newList(iterable);
        for (T item : arrayList) {
            Preconditions.checkNotNull(item, "iterable cannot contain null elements");
        }

        return arrayList;
    }

    public static <T> Set<T> newSet() {
        return new LinkedHashSet<>();
    }

    public static <T> Set<T> newSet(Iterable<? extends T> iterable) {
        return newLinkedHashSet(iterable);
    }

    public static <T> Set<T> newNonNullSet() {
        return new LinkedHashSet<>();
    }

    public static <T> Set<T> newNonNullSet(Iterable<? extends T> iterable) {
        Set<T> set = newSet(iterable);
        for (T item : set) {
            Preconditions.checkNotNull(item, "iterable cannot contain null elements");
        }

        return set;
    }

    // This method returns a list that can't handle nulls. Do not use this unless the nonNullCollections flag is set
    public static List<Double> newNonNullDoubleList() {
        return new ConjureDoubleList(new DoubleArrayList());
    }

    // This method returns a list that can't handle nulls. Do not use this unless the nonNullCollections flag is set
    public static List<Double> newNonNullDoubleList(double[] doubles) {
        return new ConjureDoubleList(new DoubleArrayList(doubles));
    }

    // This method returns a list that can't handle nulls. Do not use this unless the nonNullCollections flag is set
    public static List<Double> newNonNullDoubleList(Iterable<Double> iterable) {
        List<Double> doubleList;
        if (iterable instanceof Collection) {
            doubleList = new ConjureDoubleList(new DoubleArrayList(((Collection<Double>) iterable).size()));
        } else {
            doubleList = new ConjureDoubleList(new DoubleArrayList());
        }
        addAll(doubleList, iterable);

        return doubleList;
    }

    // This method modifies a list that can't handle nulls. Do not use this unless the nonNullCollections flag is set
    public static void addToDoubleList(Collection<Double> addTo, double toAdd) {
        if (addTo instanceof ConjureDoubleList conjureDoubleList) {
            conjureDoubleList.add(toAdd);
        } else {
            addTo.add(toAdd);
        }
    }

    // This method modifies a list that can't handle nulls. Do not use this unless the nonNullCollections flag is set
    public static void addAllToDoubleList(Collection<Double> addTo, double[] elementsToAdd) {
        if (addTo instanceof ConjureDoubleList conjureDoubleList) {
            conjureDoubleList.addAll(elementsToAdd);
        } else {
            for (double el : elementsToAdd) {
                addTo.add(el);
            }
        }
    }

    // This method returns a list that can't handle nulls. Do not use this unless the nonNullCollections flag is set
    public static List<Integer> newNonNullIntegerList() {
        return new ConjureIntegerList(new IntArrayList());
    }

    // This method returns a list that can't handle nulls. Do not use this unless the nonNullCollections flag is set
    public static List<Integer> newNonNullIntegerList(int[] ints) {
        return new ConjureIntegerList(new IntArrayList(ints));
    }

    // This method returns a list that can't handle nulls. Do not use this unless the nonNullCollections flag is set
    public static List<Integer> newNonNullIntegerList(Iterable<Integer> iterable) {
        List<Integer> integerList;
        if (iterable instanceof Collection) {
            integerList = new ConjureIntegerList(new IntArrayList(((Collection<Integer>) iterable).size()));
        } else {
            integerList = new ConjureIntegerList(new IntArrayList());
        }
        addAll(integerList, iterable);

        return integerList;
    }

    // This method modifies a list that can't handle nulls. Do not use this unless the nonNullCollections flag is set
    public static void addToIntegerList(Collection<Integer> addTo, int toAdd) {
        if (addTo instanceof ConjureIntegerList conjureIntegerList) {
            conjureIntegerList.add(toAdd);
        } else {
            addTo.add(toAdd);
        }
    }

    // This method modifies a list that can't handle nulls. Do not use this unless the nonNullCollections flag is set
    public static void addAllToIntegerList(Collection<Integer> addTo, int[] elementsToAdd) {
        if (addTo instanceof ConjureIntegerList conjureIntegerList) {
            conjureIntegerList.addAll(elementsToAdd);
        } else {
            for (int el : elementsToAdd) {
                addTo.add(el);
            }
        }
    }

    /**
     * Deprecated, this should only ever be called by a previously generated conjure internal implementation.
     */
    // This method returns a list that can't handle nulls. Do not use this unless the nonNullCollections flag is set
    public static List<Boolean> newNonNullBooleanList() {
        return newNonNullList();
    }

    /**
     * Deprecated, this should only ever be called by a previously generated conjure internal implementation.
     */
    public static List<Boolean> newNonNullBooleanList(Iterable<Boolean> iterable) {
        return newNonNullList(iterable);
    }

    // This method returns a list that can't handle nulls. Do not use this unless the nonNullCollections flag is set
    public static List<SafeLong> newNonNullSafeLongList() {
        return new ConjureSafeLongList(new LongArrayList());
    }

    // This method returns a list that can't handle nulls. Do not use this unless the nonNullCollections flag is set
    public static List<SafeLong> newNonNullSafeLongList(Iterable<SafeLong> iterable) {
        List<SafeLong> safeLongList;
        if (iterable instanceof Collection) {
            safeLongList = new ConjureSafeLongList(new LongArrayList(((Collection<SafeLong>) iterable).size()));
        } else {
            safeLongList = new ConjureSafeLongList(new LongArrayList());
        }
        addAll(safeLongList, iterable);

        return safeLongList;
    }

    // This method modifies a list that can't handle nulls. Do not use this unless the nonNullCollections flag is set
    public static void addAllToSafeLongList(Collection<SafeLong> addTo, long[] elementsToAdd) {
        for (long el : elementsToAdd) {
            addTo.add(SafeLong.of(el));
        }
    }
}
