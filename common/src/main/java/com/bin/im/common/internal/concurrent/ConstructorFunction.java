package com.bin.im.common.internal.concurrent;

@FunctionalInterface
public interface ConstructorFunction<K, V> {

    /**
     * Creates a new instance of an object given the construction argument
     *
     * @param arg construction argument
     * @return a new instance of an object
     */
    V createNew(K arg);
}