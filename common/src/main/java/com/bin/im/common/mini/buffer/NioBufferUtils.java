package com.bin.im.common.mini.buffer;


import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

import static java.util.Objects.requireNonNull;

public interface NioBufferUtils {


    MethodHandle BB_SLICE_OFFSETS = getByteBufferSliceOffsetsMethodHandle();
    MethodHandle BB_PUT_OFFSETS = getByteBufferPutOffsetsMethodHandle();
    /**
     * The maximum buffer size we support is the maximum array length generally supported by JVMs,
     * because on-heap buffers will be backed by byte-arrays.
     */
    int MAX_BUFFER_SIZE = Integer.MAX_VALUE - 8;

    static MethodHandle getByteBufferSliceOffsetsMethodHandle() {
        try {
            Lookup lookup = MethodHandles.lookup();
            MethodType type = MethodType.methodType(ByteBuffer.class, int.class, int.class);
            return lookup.findVirtual(ByteBuffer.class, "slice", type);
        } catch (Exception ignore) {
            return null;
        }
    }

    @SuppressWarnings("JavaLangInvokeHandleSignature")
    static MethodHandle getByteBufferPutOffsetsMethodHandle() {
        try {
            Lookup lookup = MethodHandles.lookup();
            MethodType type = MethodType.methodType(
                    ByteBuffer.class, int.class, ByteBuffer.class, int.class, int.class);
            return lookup.findVirtual(ByteBuffer.class, "put", type);
        } catch (Exception ignore) {
            return null;
        }
    }


    static VarHandle findVarHandle(Lookup lookup, Class<?> recv, String name, Class<?> type) {
        try {
            return lookup.findVarHandle(recv, name, type);
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }



    /**
     * Check the given {@code size} argument is a valid buffer size, or throw an {@link IllegalArgumentException}.
     *
     * @param size The size to check.
     * @throws IllegalArgumentException if the size is not positive, or if the size is too big (over ~2 GB) for a
     *                                  buffer to accommodate.
     */
    static void assertValidBufferSize(long size) {
        if (size < 0) {
            throw new IllegalArgumentException("Buffer size must not be negative, but was " + size + '.');
        }
        if (size > MAX_BUFFER_SIZE) {
            throw new IllegalArgumentException(
                    "Buffer size cannot be greater than " + MAX_BUFFER_SIZE + ", but was " + size + '.');
        }
    }

    static void checkImplicitCapacity(int implicitCapacity, int currentCapacity) {
        if (implicitCapacity < currentCapacity) {
            throw new IndexOutOfBoundsException(
                    "Implicit capacity limit (" + implicitCapacity +
                            ") cannot be less than capacity (" + currentCapacity + ')');
        }
        if (implicitCapacity > MAX_BUFFER_SIZE) {
            throw new IndexOutOfBoundsException(
                    "Implicit capacity limit (" + implicitCapacity +
                            ") cannot be greater than max buffer size (" + MAX_BUFFER_SIZE + ')');
        }
    }

    static void checkLength(int length) {
        if (length < 0) {
            throw new IndexOutOfBoundsException("The length cannot be negative: " + length + '.');
        }
    }



    /**
     * The ByteBuffer slice-with-offset-and-length method is only available from Java 13 and onwards, but we need to
     * support Java 11.
     */
    static ByteBuffer bbslice(ByteBuffer buffer, int fromOffset, int length) {
        if (BB_SLICE_OFFSETS != null) {
            return bbsliceJdk13(buffer, fromOffset, length);
        }
        return bbsliceFallback(buffer, fromOffset, length);
    }

     static ByteBuffer bbsliceJdk13(ByteBuffer buffer, int fromOffset, int length) {
        try {
            return (ByteBuffer) BB_SLICE_OFFSETS.invokeExact(buffer, fromOffset, length);
        } catch (RuntimeException re) {
            throw re;
        } catch (Throwable throwable) {
            throw new LinkageError("Unexpected exception from ByteBuffer.slice(int,int).", throwable);
        }
    }

     static ByteBuffer bbsliceFallback(ByteBuffer buffer, int fromOffset, int length) {
        if (fromOffset < 0) {
            throw new IndexOutOfBoundsException("The fromOffset must be positive: " + fromOffset + '.');
        }
        int newLimit = fromOffset + length;
        if (newLimit > buffer.capacity()) {
            throw new IndexOutOfBoundsException(
                    "The limit of " + newLimit + " would be greater than capacity: " + buffer.capacity() + '.');
        }
        return buffer.duplicate().clear().position(fromOffset).limit(newLimit).slice();
    }

    /**
     * The ByteBuffer put-buffer-with-offset-and-length method is not available in Java 11.
     */
    static void bbput(ByteBuffer dest, int destPos, ByteBuffer src, int srcPos, int length) {
        if (BB_PUT_OFFSETS != null) {
            bbputJdk16(dest, destPos, src, srcPos, length);
        } else {
            bbputFallback(dest, destPos, src, srcPos, length);
        }
    }

     static void bbputJdk16(ByteBuffer dest, int destPos, ByteBuffer src, int srcPos, int length) {
        try {
            @SuppressWarnings("unused") // We need to cast the return type in order to invokeExact.
            ByteBuffer ignore = (ByteBuffer) BB_PUT_OFFSETS.invokeExact(dest, destPos, src, srcPos, length);
        } catch (RuntimeException re) {
            throw re;
        } catch (Throwable throwable) {
            throw new LinkageError("Unexpected exception from ByteBuffer.put(int,ByteBuffer,int,int).", throwable);
        }
    }

     static void bbputFallback(ByteBuffer dest, int destPos, ByteBuffer src, int srcPos, int length) {
        dest.position(destPos).put(bbslice(src, srcPos, length));
    }

    static void setMemory(ByteBuffer buffer, int length, byte value) {
        if (!buffer.hasArray()) {
            final int intFillValue = (value & 0xFF) * 0x01010101;
            final int intCount = length >>> 2;
            for (int i = 0; i < intCount; i++) {
                buffer.putInt(i << 2, intFillValue);
            }
            final int byteCount = length & 3;
            final int bytesOffset = intCount << 2;
            for (int i = 0; i < byteCount; i++) {
                buffer.put(bytesOffset + i, value);
            }

        } else {
            final int start = buffer.arrayOffset();

            final int end = start + length;
            Arrays.fill(buffer.array(), start, end, value);

        }
    }



    static CharSequence readCharSequence(NioBuffer source, int length, Charset charset) {
        final CharSequence charSequence = copyToCharSequence(source, source.readerOffset(), length, charset);
        source.skipReadableBytes(length);
        return charSequence;
    }

    static String toString(NioBuffer source, Charset charset) {
        return copyToCharSequence(source, source.readerOffset(), source.readableBytes(), charset).toString();
    }

    static CharSequence copyToCharSequence(NioBuffer source, int srcIdx, int length, Charset charset) {
        byte[] data = new byte[length];
        source.copyInto(srcIdx, data, 0, length);
        return new String(data, 0, length, charset);
    }

    static void writeCharSequence(CharSequence source, NioBuffer destination, Charset charset) {
        byte[] bytes = source.toString().getBytes(charset);
        destination.writeBytes(bytes);
    }

    static boolean equals(NioBuffer nioBufferA, NioBuffer nioBufferB) {
        if (nioBufferA == null && nioBufferB != null || nioBufferB == null && nioBufferA != null) {
            return false;
        }
        if (nioBufferA == nioBufferB) {
            return true;
        }
        final int aLen = nioBufferA.readableBytes();
        if (aLen != nioBufferB.readableBytes()) {
            return false;
        }
        return equals(nioBufferA, nioBufferA.readerOffset(), nioBufferB, nioBufferB.readerOffset(), aLen);
    }

    static boolean equals(NioBuffer a, int aStartIndex, NioBuffer b, int bStartIndex, int length) {
        requireNonNull(a, "a");
        requireNonNull(b, "b");
        // All indexes and lengths must be non-negative
        checkPositiveOrZero(aStartIndex, "aStartIndex");
        checkPositiveOrZero(bStartIndex, "bStartIndex");
        checkPositiveOrZero(length, "length");

        if (a.writerOffset() - length < aStartIndex || b.writerOffset() - length < bStartIndex) {
            return false;
        }

        return equalsInner(a, aStartIndex, b, bStartIndex, length);
    }

     static boolean equalsInner(NioBuffer a, int aStartIndex, NioBuffer b, int bStartIndex, int length) {
        final int longCount = length >>> 3;
        final int byteCount = length & 7;

        for (int i = longCount; i > 0; i--) {
            if (a.getLong(aStartIndex) != b.getLong(bStartIndex)) {
                return false;
            }
            aStartIndex += 8;
            bStartIndex += 8;
        }

        for (int i = byteCount; i > 0; i--) {
            if (a.getByte(aStartIndex) != b.getByte(bStartIndex)) {
                return false;
            }
            aStartIndex++;
            bStartIndex++;
        }

        return true;
    }

    static int hashCode(NioBuffer nioBuffer) {
        final int aLen = nioBuffer.readableBytes();
        final int intCount = aLen >>> 2;
        final int byteCount = aLen & 3;

        int hashCode = 0;
        int arrayIndex = nioBuffer.readerOffset();
        for (int i = intCount; i > 0; i--) {
            hashCode = 31 * hashCode + nioBuffer.getInt(arrayIndex);
            arrayIndex += 4;
        }

        for (int i = byteCount; i > 0; i--) {
            hashCode = 31 * hashCode + nioBuffer.getByte(arrayIndex++);
        }

        if (hashCode == 0) {
            hashCode = 1;
        }

        return hashCode;
    }


    interface UncheckedLoadByte {
        byte load(NioBuffer nioBuffer, int offset);
    }

    UncheckedLoadByte UNCHECKED_LOAD_BYTE_BUFFER = NioBuffer::getByte;

    static int bytesBefore(NioBuffer haystack, UncheckedLoadByte hl,
                           NioBuffer needle, UncheckedLoadByte nl) {
        if (needle.readableBytes() > haystack.readableBytes()) {
            return -1;
        }

        if (hl == null) {
            hl = UNCHECKED_LOAD_BYTE_BUFFER;
        }
        if (nl == null) {
            nl = UNCHECKED_LOAD_BYTE_BUFFER;
        }

        int haystackLen = haystack.readableBytes();
        int needleLen = needle.readableBytes();
        if (needleLen == 0) {
            return 0;
        }

        // When the needle has only one byte that can be read,
        // the Buffer.bytesBefore() method can be used
        if (needleLen == 1) {
            return haystack.bytesBefore(needle.getByte(needle.readerOffset()));
        }

        int needleStart = needle.readerOffset();
        int haystackStart = haystack.readerOffset();
        long suffixes = maxFixes(needle, nl, needleLen, needleStart, true);
        long prefixes = maxFixes(needle, nl, needleLen, needleStart, false);
        int maxSuffix = Math.max((int) (suffixes >> 32), (int) (prefixes >> 32));
        int period = Math.max((int) suffixes, (int) prefixes);
        int length = Math.min(needleLen - period, maxSuffix + 1);

        if (equalsInner(needle, needleStart, needle, needleStart + period, length)) {
            return bytesBeforeInnerPeriodic(
                    haystack, hl, needle, nl, haystackLen, needleLen, needleStart, haystackStart, maxSuffix, period);
        }
        return bytesBeforeInnerNonPeriodic(
                haystack, hl, needle, nl, haystackLen, needleLen, needleStart, haystackStart, maxSuffix);
    }

    static int bytesBeforeInnerPeriodic(NioBuffer haystack, UncheckedLoadByte hl,
                                                NioBuffer needle, UncheckedLoadByte nl,
                                                int haystackLen, int needleLen, int needleStart, int haystackStart,
                                                int maxSuffix, int period) {
        int j = 0;
        int memory = -1;
        while (j <= haystackLen - needleLen) {
            int i = Math.max(maxSuffix, memory) + 1;
            while (i < needleLen && nl.load(needle, i + needleStart) == hl.load(haystack, i + j + haystackStart)) {
                ++i;
            }
            if (i > haystackLen) {
                return -1;
            }
            if (i >= needleLen) {
                i = maxSuffix;
                while (i > memory && nl.load(needle, i + needleStart) == hl.load(haystack, i + j + haystackStart)) {
                    --i;
                }
                if (i <= memory) {
                    return j;
                }
                j += period;
                memory = needleLen - period - 1;
            } else {
                j += i - maxSuffix;
                memory = -1;
            }
        }
        return -1;
    }

    static int bytesBeforeInnerNonPeriodic(NioBuffer haystack, UncheckedLoadByte hl,
                                                   NioBuffer needle, UncheckedLoadByte nl,
                                                   int haystackLen, int needleLen, int needleStart, int haystackStart,
                                                   int maxSuffix) {
        int j = 0;
        int period = Math.max(maxSuffix + 1, needleLen - maxSuffix - 1) + 1;
        while (j <= haystackLen - needleLen) {
            int i = maxSuffix + 1;
            while (i < needleLen && nl.load(needle, i + needleStart) == hl.load(haystack, i + j + haystackStart)) {
                ++i;
            }
            if (i > haystackLen) {
                return -1;
            }
            if (i >= needleLen) {
                i = maxSuffix;
                while (i >= 0 && nl.load(needle, i + needleStart) == hl.load(haystack, i + j + haystackStart)) {
                    --i;
                }
                if (i < 0) {
                    return j;
                }
                j += period;
            } else {
                j += i - maxSuffix;
            }
        }
        return -1;
    }

    static long maxFixes(NioBuffer needle, UncheckedLoadByte nl, int needleLen, int start, boolean isSuffix) {
        int period = 1;
        int maxSuffix = -1;
        int lastRest = start;
        int k = 1;
        while (lastRest + k < needleLen) {
            byte a = nl.load(needle, lastRest + k);
            byte b = nl.load(needle, maxSuffix + k);
            boolean suffix = isSuffix ? a < b : a > b;
            if (suffix) {
                lastRest += k;
                k = 1;
                period = lastRest - maxSuffix;
            } else if (a == b) {
                if (k != period) {
                    ++k;
                } else {
                    lastRest += period;
                    k = 1;
                }
            } else {
                maxSuffix = lastRest;
                lastRest = maxSuffix + 1;
                k = period = 1;
            }
        }
        return ((long) maxSuffix << 32) + period;
    }

    public static int roundToPowerOfTwo(int value) {
        if (value > 1073741824) {
            throw new IllegalArgumentException("There is no larger power of 2 int for value:" + value + " since it exceeds 2^31.");
        } else if (value < 0) {
            throw new IllegalArgumentException("Given value:" + value + ". Expecting value >= 0.");
        } else {
            int nextPow2 = 1 << 32 - Integer.numberOfLeadingZeros(value - 1);
            return nextPow2;
        }
    }

    public static int checkPositiveOrZero(int i, String name) {
        if (i < 0) {
            throw new IllegalArgumentException(name + " : " + i + " (expected: >= 0)");
        } else {
            return i;
        }
    }

    public static long checkPositiveOrZero(long l, String name) {
        if (l < 0L) {
            throw new IllegalArgumentException(name + " : " + l + " (expected: >= 0)");
        } else {
            return l;
        }
    }
}