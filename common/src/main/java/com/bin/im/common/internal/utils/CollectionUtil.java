package com.bin.im.common.internal.utils;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class CollectionUtil {

    private CollectionUtil() {
    }

    /**
     * Returns {@code true} if the given collection is {@code null} or empty, otherwise returns {@code false}.
     *
     * @param collection the given collection
     * @return {@code true} if collection is empty
     */
    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * Returns {@code true} if the given collection is not {@code null} and not empty, otherwise returns {@code false}.
     *
     * @param collection the given collection
     * @return {@code true} if collection is not empty
     */
    public static boolean isNotEmpty(Collection collection) {
        return !isEmpty(collection);
    }

    /**
     * Adds a value to a list of values in the map.
     * <p>
     * Creates a new list if no list is found for the key.
     *
     * @param map   the given map of lists
     * @param key   the key of the target list
     * @param value the value to add to the target list
     * @return the updated list of values
     */
    public static <K, V> List<V> addToValueList(Map<K, List<V>> map, K key, V value) {
        List<V> valueList = map.get(key);
        if (valueList == null) {
            valueList = new ArrayList<V>();
            map.put(key, valueList);
        }
        valueList.add(value);

        return valueList;
    }

    /**
     * Returns the n-th item or {@code null} if collection is smaller.
     *
     * @param collection the given collection
     * @param position   position of the wanted item
     * @return the item on position or {@code null} if the given collection is too small
     * @throws NullPointerException if collection is {@code null}
     */
    public static <T> T getItemAtPositionOrNull(Collection<T> collection, int position) {
        if (position >= collection.size()) {
            return null;
        }
        if (collection instanceof List) {
            return ((List<T>) collection).get(position);
        }
        Iterator<T> iterator = collection.iterator();
        T item = null;
        for (int i = 0; i < position + 1; i++) {
            item = iterator.next();
        }
        return item;
    }




    /**
     * Converts a {@link Collection} of {@link Integer} to a primitive {@code int[]} array.
     *
     * @param collection the given collection
     * @return a primitive int[] array
     * @throws NullPointerException if collection is {@code null}
     */
    public static int[] toIntArray(Collection<Integer> collection) {
        int[] collectionArray = new int[collection.size()];
        int index = 0;
        for (Integer item : collection) {
            collectionArray[index++] = item;
        }
        return collectionArray;
    }

    /**
     * Converts a {@link Collection} of {@link Long} to a primitive {@code long[]} array.
     *
     * @param collection the given collection
     * @return a primitive long[] array
     * @throws NullPointerException if collection is {@code null}
     */
    public static long[] toLongArray(Collection<Long> collection) {
        long[] collectionArray = new long[collection.size()];
        int index = 0;
        for (Long item : collection) {
            collectionArray[index++] = item;
        }
        return collectionArray;
    }

    /**
     * Adapts an int array to an Integer {@link List}.
     *
     * @param array the array
     * @return the list
     * @throws NullPointerException if array is null.
     */
    public static List<Integer> asIntegerList(int[] array) {
        return new AbstractList<Integer>() {
            @Override
            public Integer get(int index) {
                return array[index];
            }

            @Override
            public int size() {
                return array.length;
            }
        };
    }

    /** Returns an empty Collection if argument is null. **/
    public static <T> Collection<T> nullToEmpty(Collection<T> collection) {
        return collection == null ? Collections.<T>emptyList() : collection;
    }
}
