package com.bin.im.common.internal.utils;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class UnsafeUtil {


    public static final Unsafe UNSAFE;

    static {
        Field field;
        try {
            field = Unsafe.class.getDeclaredField("theUnsafe");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Missing field: sun.misc.Unsafe#theUnsafe", e);
        }
        field.setAccessible(true);
        try {
            UNSAFE = (Unsafe) field.get(null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(
                    "The field (sun.misc.Unsafe#theUnsafe) should be accessible",
                    e);
        }
    }

    private UnsafeUtil() {
    }

    public static int getNextFieldOffset(Class<?> clazz) {
        Field lastField = null;
        int lastFieldOffset = 0;
        long offset;
        for (Field field : clazz.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers())) {
                offset = UNSAFE.objectFieldOffset(field);
                if (lastFieldOffset < offset) {
                    lastField = field;
                    lastFieldOffset = (int) offset;
                }
            }
        }
        if (lastField == null) {
            return 0;
        }
        int size = getSize(lastField.getDeclaringClass());
        return lastFieldOffset + size;
    }

    public static int getSize(Class<?> clazz) {
        if (Object.class.isAssignableFrom(clazz)) {
            return UNSAFE.addressSize();
        } else if (byte.class == clazz) {
            return Byte.BYTES;
        } else if (short.class == clazz) {
            return Short.BYTES;
        } else if (int.class == clazz) {
            return Integer.BYTES;
        } else if (long.class == clazz) {
            return Long.BYTES;
        } else if (float.class == clazz) {
            return Float.BYTES;
        } else if (double.class == clazz) {
            return Double.BYTES;
        } else if (char.class == clazz) {
            return Character.BYTES;
        } else if (boolean.class == clazz) {
            return Byte.BYTES;
        } else {
            throw new RuntimeException(
                    "The class must be the Object class, or a primitive class, but got: "
                            + clazz.getName());
        }
    }

}