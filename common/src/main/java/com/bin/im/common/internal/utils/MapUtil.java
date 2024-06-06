package com.bin.im.common.internal.utils;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class MapUtil {

    /**
     * Default hash map load factor.
     */
    public static final float HASHMAP_DEFAULT_LOAD_FACTOR = 0.75f;

    private MapUtil() { }

    /**
     * Utility method that creates an {@link java.util.HashMap} with its initialCapacity calculated
     * to minimize rehash operations
     */
    public static <K, V> Map<K, V> createHashMap(int expectedMapSize) {
        final int initialCapacity = calculateInitialCapacity(expectedMapSize);
        return new HashMap<>(initialCapacity, HASHMAP_DEFAULT_LOAD_FACTOR);
    }

    /**
     * Utility method that creates an {@link java.util.LinkedHashMap} with its initialCapacity calculated
     * to minimize rehash operations
     */
    public static <K, V> Map<K, V> createLinkedHashMap(int expectedMapSize) {
        final int initialCapacity = calculateInitialCapacity(expectedMapSize);
        return new LinkedHashMap<>(initialCapacity, HASHMAP_DEFAULT_LOAD_FACTOR);
    }

    /**
     * Utility method that creates an {@link java.util.LinkedHashMap} with its initialCapacity calculated
     * to minimize rehash operations
     */
    public static <K, V> ConcurrentMap<K, V> createConcurrentHashMap(int expectedMapSize) {
        //concurrent hash map will size itself to accomodate this many elements
        return new ConcurrentHashMap<>(expectedMapSize);
    }



    /**
     * Returns the initial hash map capacity needed for the expected map size.
     * To avoid resizing the map, the initial capacity should be different than
     * the expected size, depending on the load factor.
     *
     * @param expectedMapSize the expected map size
     * @return the necessary initial capacity
     * @see HashMap
     */
    public static int calculateInitialCapacity(int expectedMapSize) {
        return (int) (expectedMapSize / HASHMAP_DEFAULT_LOAD_FACTOR) + 1;
    }

    /**
     * Test the given map and return {@code true} if the map is null or empty.
     * @param map the map to test
     * @return    {@code true} if {@code map} is null or empty, otherwise {@code false}.
     */
    public static boolean isNullOrEmpty(Map map) {
        return map == null || map.isEmpty();
    }

    /**
     * Returns a {@code Map.Entry} with the given key and value.
     */
    public static <K, V> Map.Entry<K, V> entry(K k, V v) {
        return new AbstractMap.SimpleImmutableEntry<>(k, v);
    }

    /**
     * Converts <code>long</code> map size to <code>int</code>.
     * If <code>size</code> is greater than <code>Integer.MAX_VALUE</code>
     * then <code>Integer.MAX_VALUE</code> is returned.
     *
     * @param size map size
     * @return map size in <code>int</code> type
     */
    public static int toIntSize(long size) {
        assert size >= 0 : "Invalid size value: " + size;
        return size > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) size;
    }

}