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
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.eclipse.collections.impl.list.mutable.primitive.BooleanArrayList;
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

    /**
     * The following Conjure boxed list wrappers for the eclipse-collections [type]ArrayList are temporary (except
     * ConjureSafeLongList). In eclipse-collections 12, a BoxedMutable[type]List will be released. Once available,
     * Conjure[type]List should be replaced with that.
     */

    // This method returns a list that can't handle nulls. Do not use this unless the nonNullCollections flag is set
    public static List<Double> newNonNullDoubleList() {
        return new ConjureDoubleList(new DoubleArrayList());
    }

    // This method returns a list that can't handle nulls. Do not use this unless the nonNullCollections flag is set
    public static List<Double> newNonNullDoubleList(double[] doubles) {
        return new ConjureDoubleList(DoubleArrayList.newListWith(doubles));
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

    // This method returns a list that can't handle nulls. Do not use this unless the nonNullCollections flag is set
    public static List<Integer> newNonNullIntegerList() {
        return new ConjureIntegerList(new IntArrayList());
    }

    // This method returns a list that can't handle nulls. Do not use this unless the nonNullCollections flag is set
    public static List<Integer> newNonNullIntegerList(int[] integers) {
        return new ConjureIntegerList(IntArrayList.newListWith(integers));
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

    // This method returns a list that can't handle nulls. Do not use this unless the nonNullCollections flag is set
    public static List<Boolean> newNonNullBooleanList() {
        return new ConjureBooleanList(new BooleanArrayList());
    }

    // This method returns a list that can't handle nulls. Do not use this unless the nonNullCollections flag is set
    public static List<Boolean> newNonNullBooleanList(boolean[] booleans) {
        return new ConjureBooleanList(BooleanArrayList.newListWith(booleans));
    }

    // This method returns a list that can't handle nulls. Do not use this unless the nonNullCollections flag is set
    public static List<Boolean> newNonNullBooleanList(Iterable<Boolean> iterable) {
        List<Boolean> booleanList;
        if (iterable instanceof Collection) {
            booleanList = new ConjureBooleanList(new BooleanArrayList(((Collection<Boolean>) iterable).size()));
        } else {
            booleanList = new ConjureBooleanList(new BooleanArrayList());
        }
        addAll(booleanList, iterable);

        return booleanList;
    }

    // This method returns a list that can't handle nulls. Do not use this unless the nonNullCollections flag is set
    public static List<SafeLong> newNonNullSafeLongList() {
        return new ConjureSafeLongList(new LongArrayList());
    }

    // This method returns a list that can't handle nulls. Do not use this unless the nonNullCollections flag is set
    public static List<SafeLong> newNonNullSafeLongList(long[] longs) {
        for (long value : longs) {
            if (!safeLongCheck(value)) {
                throw new SafeIllegalArgumentException(
                        "number must be safely representable in javascript i.e. lie between -9007199254740991 and "
                                + "9007199254740991");
            }
        }
        return new ConjureSafeLongList(LongArrayList.newListWith(longs));
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

    private static boolean safeLongCheck(long value) {
        return SafeLong.MIN_VALUE.longValue() <= value && value <= SafeLong.MAX_VALUE.longValue();
    }
}
