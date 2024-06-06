package com.bin.im.common.internal.utils;

import java.util.Objects;

public final class TriTuple<X, Y, Z> {

    public final X element1;
    public final Y element2;
    public final Z element3;

    private TriTuple(X element1, Y element2, Z element3) {
        this.element1 = element1;
        this.element2 = element2;
        this.element3 = element3;
    }

    public static <X, Y, Z> TriTuple<X, Y, Z> of(X element1, Y element2, Z element3) {
        return new TriTuple<>(element1, element2, element3);
    }

    @Override
    @SuppressWarnings("checkstyle:npathcomplexity")
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TriTuple<?, ?, ?> triTuple = (TriTuple<?, ?, ?>) o;

        if (!Objects.equals(element1, triTuple.element1)) {
            return false;
        }
        if (!Objects.equals(element2, triTuple.element2)) {
            return false;
        }
        return Objects.equals(element3, triTuple.element3);
    }

    @Override
    public int hashCode() {
        int result = element1 != null ? element1.hashCode() : 0;
        result = 31 * result + (element2 != null ? element2.hashCode() : 0);
        result = 31 * result + (element3 != null ? element3.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TriTuple{" + "element1=" + element1 + ", element2=" + element2 + ", element3=" + element3 + '}';
    }
}
