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
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
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
     * We do _not_ want to expose the Conjure*List types externally,
     * but we also want the optimizations they provide to make it through
     * to jackson for serialization. So the runtime type needs to be
     * preserved while also not exposing the type :phew:.
     *
     * To achieve this we have to do some gymnastics surrounding the type
     * system. We need this to return the type of the list given, but also
     * return specific Conjure types when detected. This requires that we
     * erase the type info, but we know this is safe because we are directly
     * returning the same type which is by definition the identity function.
     * Therefore, the input List<T> is the same types as the output List<T>.
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

    public static <T> void addAll(Collection<T> addTo, Iterable<? extends T> elementsToAdd) {
        Preconditions.checkNotNull(elementsToAdd, "elementsToAdd cannot be null");
        if (elementsToAdd instanceof Collection<? extends T> collection) {
            // This special-casing allows us to take advantage of the more performant
            // ArrayList#addAll method which does a single System.arraycopy.
            addTo.addAll(collection);
        } else {
            elementsToAdd.forEach(addTo::add);
        }
    }

    public static <T> void addAllAndCheckNonNull(Collection<T> addTo, Iterable<? extends T> elementsToAdd) {
        Preconditions.checkNotNull(elementsToAdd, "elementsToAdd cannot be null");
        if (elementsToAdd instanceof Collection<? extends T> collection) {
            // Some collections such as ArrayList support bulk addAll optimizations to avoid repeated resizing.
            addTo.addAll(new AbstractCollection<>() {
                @Override
                public Iterator<T> iterator() {
                    return new NonNullIterator<>(collection.iterator());
                }

                @Override
                public Object[] toArray() {
                    Object[] array = collection.toArray();
                    for (Object element : array) {
                        checkNotNullElement(element);
                    }
                    return array;
                }

                @Override
                public int size() {
                    return collection.size();
                }
            });
        } else {
            elementsToAdd.forEach(element -> addTo.add(checkNotNullElement(element)));
        }
    }

    private static <T> T checkNotNullElement(T element) {
        return Preconditions.checkNotNull(element, "element cannot be null");
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
        iterable.forEach(list::add);
        return list;
    }

    // Prefer to use newSet(iterable)
    @SuppressWarnings({"IllegalType", "NonApiType"}) // explicitly need to return mutable list for generated builders
    public static <T> LinkedHashSet<T> newLinkedHashSet(Iterable<? extends T> iterable) {
        Preconditions.checkNotNull(iterable, "iterable cannot be null");
        if (iterable instanceof Collection<? extends T> collection) {
            return new LinkedHashSet<>(collection);
        }
        LinkedHashSet<T> set = new LinkedHashSet<>();
        iterable.forEach(set::add);
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
        arrayList.forEach(ConjureCollections::checkNotNullElement);
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
        set.forEach(ConjureCollections::checkNotNullElement);
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
        if (addTo instanceof ConjureDoubleList) {
            ((ConjureDoubleList) addTo).add(toAdd);
        } else {
            addTo.add(toAdd);
        }
    }

    // This method modifies a list that can't handle nulls. Do not use this unless the nonNullCollections flag is set
    public static void addAllToDoubleList(Collection<Double> addTo, double[] elementsToAdd) {
        if (addTo instanceof ConjureDoubleList) {
            ((ConjureDoubleList) addTo).addAll(elementsToAdd);
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
        if (addTo instanceof ConjureIntegerList) {
            ((ConjureIntegerList) addTo).add(toAdd);
        } else {
            addTo.add(toAdd);
        }
    }

    // This method modifies a list that can't handle nulls. Do not use this unless the nonNullCollections flag is set
    public static void addAllToIntegerList(Collection<Integer> addTo, int[] elementsToAdd) {
        if (addTo instanceof ConjureIntegerList) {
            ((ConjureIntegerList) addTo).addAll(elementsToAdd);
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

    private record NonNullIterator<T>(Iterator<? extends T> iterator) implements Iterator<T> {
        private NonNullIterator(Iterator<? extends T> iterator) {
            this.iterator = Preconditions.checkNotNull(iterator, "iterator cannot be null");
        }

        @Override
        public boolean hasNext() {
            return iterator().hasNext();
        }

        @Override
        public T next() {
            return checkNotNullElement(iterator().next());
        }
    }
}
