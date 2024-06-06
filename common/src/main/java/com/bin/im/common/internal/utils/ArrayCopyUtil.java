package com.bin.im.common.internal.utils;

public class ArrayCopyUtil {
    public static byte[] copyOf(byte[] src, Accumulator accumulator, int arrayLen)
            throws Exception {

        byte[] array = new byte[arrayLen];
        System.arraycopy(src, accumulator.getIndex(), array, 0, arrayLen);
        accumulator.setIndex(accumulator.getIndex()+arrayLen);

        return array;
    }

    public static byte[] copyOf(byte[] src,byte[] dest,int length, Accumulator accumulator){

        System.arraycopy(src, 0, dest, accumulator.getIndex(), length);
        accumulator.setIndex(accumulator.getIndex()+length);

        return dest;
    }
}