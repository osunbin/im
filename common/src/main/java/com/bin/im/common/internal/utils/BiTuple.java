package com.bin.im.common.internal.utils;

import java.util.Objects;

public final class BiTuple<X, Y> {

    public final X element1;
    public final Y element2;

    private BiTuple(X element1, Y element2) {
        this.element1 = element1;
        this.element2 = element2;
    }

    public static <X, Y> BiTuple<X, Y> of(X element1, Y element2) {
        return new BiTuple<>(element1, element2);
    }

    public X element1() {
        return element1;
    }

    public Y element2() {
        return element2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BiTuple<?, ?> biTuple = (BiTuple<?, ?>) o;

        if (!Objects.equals(element1, biTuple.element1)) {
            return false;
        }
        return Objects.equals(element2, biTuple.element2);
    }

    @Override
    public int hashCode() {
        int result = element1 != null ? element1.hashCode() : 0;
        result = 31 * result + (element2 != null ? element2.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BiTuple{" + "element1=" + element1 + ", element2=" + element2 + '}';
    }
}