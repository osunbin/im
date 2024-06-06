package com.bin.im.common.mini.buffer;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static com.bin.im.common.mini.buffer.NioBufferUtils.MAX_BUFFER_SIZE;
import static com.bin.im.common.mini.buffer.NioBufferUtils.bbput;
import static com.bin.im.common.mini.buffer.NioBufferUtils.bbslice;
import static com.bin.im.common.mini.buffer.NioBufferUtils.checkImplicitCapacity;
import static com.bin.im.common.mini.buffer.NioBufferUtils.checkLength;
import static com.bin.im.common.mini.buffer.NioBufferUtils.checkPositiveOrZero;
import static com.bin.im.common.mini.buffer.NioBufferUtils.roundToPowerOfTwo;
import static com.bin.im.common.mini.buffer.NioBufferUtils.setMemory;


@SuppressWarnings("all")
public class NioBuffer {


    public static void main(String[] args) {


        NioBuffer nioBuffer = new NioBuffer(ByteBuffer.allocate(128));

        String s = "这是测试";
        nioBuffer.writeCharSequence(s);

        CharSequence charSequence = nioBuffer.readCharSequence(s.getBytes(StandardCharsets.UTF_8).length);

        System.out.println(charSequence);
    }
    int refs;


    private ByteBuffer rmem; // For reading.
    private ByteBuffer wmem; // For writing.

    private int roff;
    private int woff;
    private int implicitCapacityLimit;

    volatile int pos;

    public NioBuffer(ByteBuffer memory) {
        rmem = memory;
        wmem = memory;
        implicitCapacityLimit = MAX_BUFFER_SIZE;
    }


    public int capacity() {
        return rmem.capacity();
    }

    public int readerOffset() {
        return roff;
    }


    public NioBuffer skipReadableBytes(int delta) {
        checkPositiveOrZero(delta, "delta");
        readerOffset(readerOffset() + delta);
        return this;
    }

    // ofReadByteBuffer
   public NioBuffer readerOffset(int offset) {
        checkRead(offset, 0);
        roff = offset;
        return this;
    }


    public int writerOffset() {
        return woff;
    }


    public NioBuffer skipWritableBytes(int delta) {
        checkPositiveOrZero(delta, "delta");
        writerOffset(writerOffset() + delta);
        return this;
    }

   public NioBuffer writerOffset(int offset) {
        checkWrite(offset, 0, false);
        woff = offset;
        return this;
    }

    public boolean readable() {
        return writerOffset() != readerOffset();
    }

    public boolean writable() {
        return writerOffset() != capacity();
    }

    public int readableBytes() {
        return writerOffset() - readerOffset();
    }

    public int writableBytes() {
        return capacity() - writerOffset();
    }

    public NioBuffer fill(byte value) {
        int capacity = capacity();
        checkSet(0, capacity);
        final ByteBuffer wmem = this.wmem;
        setMemory(wmem, capacity, value);
        return this;
    }


    public boolean isDirect() {
        return rmem.isDirect();
    }

    public NioBuffer implicitCapacityLimit(int limit) {
        checkImplicitCapacity(limit, capacity());
        implicitCapacityLimit = limit;
        return this;
    }

    public int implicitCapacityLimit() {
        return implicitCapacityLimit;
    }


    public void copyInto(int srcPos, byte[] dest, int destPos, int length) {
        copyInto(srcPos, ByteBuffer.wrap(dest), destPos, length);
    }

    public void copyInto(int srcPos, ByteBuffer dest, int destPos, int length) {
        if (srcPos < 0) {
            throw new IndexOutOfBoundsException("The srcPos cannot be negative: " + srcPos + '.');
        }
        if (destPos < 0) {
            throw new IndexOutOfBoundsException("The destination position cannot be negative: " + destPos);
        }
        checkLength(length);
        if (capacity() < srcPos + length) {
            throw new IndexOutOfBoundsException("The srcPos + length is beyond the end of the buffer: " +
                    "srcPos = " + srcPos + ", length = " + length + '.');
        }
        if (dest.capacity() < destPos + length) {
            throw new IndexOutOfBoundsException("The destPos + length is beyond the end of the buffer: " +
                    "destPos = " + destPos + ", length = " + length + '.');
        }
        if (dest.hasArray() && hasReadableArray()) {
            final byte[] srcArray = rmem.array();
            final int srcStart = rmem.arrayOffset() + srcPos;
            final byte[] dstArray = dest.array();
            final int dstStart = dest.arrayOffset() + destPos;
            System.arraycopy(srcArray, srcStart, dstArray, dstStart, length);
            return;
        }
        dest = dest.duplicate().clear();
        bbput(dest, destPos, rmem, srcPos, length);
    }

    public void copyInto(int srcPos, NioBuffer dest, int destPos, int length) {
        dest.checkSet(destPos, length);
        copyInto(srcPos, dest.wmem, destPos, length);
    }

    public int transferTo(WritableByteChannel channel, int length) throws IOException {
        length = Math.min(readableBytes(), length);
        if (length == 0) {
            return 0;
        }
        checkGet(readerOffset(), length);
        int bytesWritten = channel.write(readableBuffer().limit(length));
        skipReadableBytes(bytesWritten);
        return bytesWritten;
    }

    public int transferFrom(FileChannel channel, long position, int length) throws IOException {
        checkPositiveOrZero(position, "position");
        checkPositiveOrZero(length, "length");
        length = Math.min(writableBytes(), length);
        if (length == 0) {
            return 0;
        }
        checkSet(writerOffset(), length);
        int bytesRead = channel.read(writableBuffer().limit(length), position);
        if (bytesRead > 0) { // Don't skipWritable if bytesRead is 0 or -1
            skipWritableBytes(bytesRead);
        }
        return bytesRead;
    }

    public int transferFrom(ReadableByteChannel channel, int length) throws IOException {
        length = Math.min(writableBytes(), length);
        if (length == 0) {
            return 0;
        }
        checkSet(writerOffset(), length);
        int bytesRead = channel.read(writableBuffer().limit(length));
        if (bytesRead != -1) {
            skipWritableBytes(bytesRead);
        }
        return bytesRead;
    }

    public NioBuffer writeCharSequence(CharSequence source) {
        writeCharSequence(source, StandardCharsets.UTF_8);
        return this;
    }

    public NioBuffer writeCharSequence(CharSequence source, Charset charset) {
        NioBufferUtils.writeCharSequence(source, this, charset);
        return this;
    }

    public CharSequence readCharSequence(int length) {
        return readCharSequence(length, StandardCharsets.UTF_8);
    }

    public CharSequence readCharSequence(int length, Charset charset) {
        return NioBufferUtils.readCharSequence(this, length, charset);
    }

    public NioBuffer writeBytes(NioBuffer source) {
        int size = source.readableBytes();
        if (writableBytes() < size && writerOffset() + size <= implicitCapacityLimit()) {
            ensureWritable(size, 1, false);
        }
        int woff = writerOffset();
        source.copyInto(source.readerOffset(), this, woff, size);
        source.skipReadableBytes(size);
        skipWritableBytes(size);
        return this;
    }

    public NioBuffer writeBytes(byte[] source) {
        return writeBytes(source, 0, source.length);
    }

    public NioBuffer writeBytes(byte[] source, int srcPos, int length) {
        if (source.length < srcPos + length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        if (writableBytes() < length && writerOffset() + length <= implicitCapacityLimit()) {
            ensureWritable(length, 1, false);
        }
        int woff = writerOffset();
        for (int i = 0; i < length; i++) {
            setByte(woff + i, source[srcPos + i]);
        }
        skipWritableBytes(length);
        return this;
    }

    public NioBuffer writeBytes(ByteBuffer source) {
        if (source.hasArray()) {
            writeBytes(source.array(), source.arrayOffset() + source.position(), source.remaining());
            source.position(source.limit());
        } else {
            int woff = writerOffset();
            int length = source.remaining();
            if (writableBytes() < length && woff + length <= implicitCapacityLimit()) {
                ensureWritable(length, 1, false);
            }
            writerOffset(woff + length);
            // Try to reduce bounds-checking by using long and int when possible.
            boolean needReverse = source.order() != ByteOrder.BIG_ENDIAN;
            for (; length >= Long.BYTES; length -= Long.BYTES, woff += Long.BYTES) {
                setLong(woff, needReverse ? Long.reverseBytes(source.getLong()) : source.getLong());
            }
            for (; length >= Integer.BYTES; length -= Integer.BYTES, woff += Integer.BYTES) {
                setInt(woff, needReverse ? Integer.reverseBytes(source.getInt()) : source.getInt());
            }
            for (; length > 0; length--, woff++) {
                setByte(woff, source.get());
            }
        }
        return this;
    }

    public NioBuffer readBytes(ByteBuffer destination) {
        int byteCount = destination.remaining();
        copyInto(readerOffset(), destination, destination.position(), byteCount);
        skipReadableBytes(byteCount);
        destination.position(destination.limit());
        return this;
    }

    public NioBuffer readBytes(byte[] destination, int destPos, int length) {
        int roff = readerOffset();
        copyInto(roff, destination, destPos, length);
        readerOffset(roff + length);
        return this;
    }


    public NioBuffer resetOffsets() {
        readerOffset(0);
        writerOffset(0);
        return this;
    }

    public int bytesBefore(byte needle) {
        int offset = roff;
        final int length = woff - roff;
        final int end = woff;

        if (length > 7) {
            final long pattern = (needle & 0xFFL) * 0x101010101010101L;
            for (final int longEnd = offset + (length >>> 3) * Long.BYTES;
                 offset < longEnd;
                 offset += Long.BYTES) {
                final long word = rmem.getLong(offset);

                long input = word ^ pattern;
                long tmp = (input & 0x7F7F7F7F7F7F7F7FL) + 0x7F7F7F7F7F7F7F7FL;
                tmp = ~(tmp | input | 0x7F7F7F7F7F7F7F7FL);
                final int binaryPosition = Long.numberOfLeadingZeros(tmp);

                int index = binaryPosition >>> 3;
                if (index < Long.BYTES) {
                    return offset + index - roff;
                }
            }
        }
        for (; offset < end; offset++) {
            if (rmem.get(offset) == needle) {
                return offset - roff;
            }
        }

        return -1;
    }

    public int bytesBefore(NioBuffer needle) {
        NioBufferUtils.UncheckedLoadByte uncheckedLoadByte = NioBuffer::uncheckedLoadByte;
        return NioBufferUtils.bytesBefore(this, uncheckedLoadByte,
                needle, needle instanceof NioBuffer ? uncheckedLoadByte : null);
    }

    private static byte uncheckedLoadByte(NioBuffer nioBuffer, int offset) {
        return ((NioBuffer) nioBuffer).rmem.get(offset);
    }

    public NioBuffer ensureWritable(int size) {
        ensureWritable(size, capacity(), true);
        return this;
    }

    public NioBuffer ensureWritable(int size, int minimumGrowth, boolean allowCompaction) {
        if (size < 0) {
            throw new IllegalArgumentException("Cannot ensure writable for a negative size: " + size + '.');
        }
        if (minimumGrowth < 0) {
            throw new IllegalArgumentException("The minimum growth cannot be negative: " + minimumGrowth + '.');
        }

        if (writableBytes() >= size) {
            // We already have enough space.
            return this;
        }

        if (allowCompaction && writableBytes() + readerOffset() >= size) {
            // We can solve this with compaction.
            return compact();
        }

        // Allocate a bigger buffer.
        long newSize = capacity() + (long) Math.max(size - writableBytes(), minimumGrowth);
        NioBufferUtils.assertValidBufferSize(newSize);


        NioBuffer nioBuffer = NioBufferPool.allocate((int) newSize);
        // Copy contents.
        copyInto(0, nioBuffer, 0, capacity());
        int roff = this.roff;
        int woff = this.woff;

        this.recycle();

        this.roff = roff;
        this.woff = woff;
        rmem = nioBuffer.rmem;
        wmem = nioBuffer.wmem;
        return this;
    }


    public NioBuffer copy() {
        int offset = readerOffset();
        int length = readableBytes();
        return copy(offset, length);
    }

    public NioBuffer copy(int offset, int length) {
        checkLength(length);
        checkGet(offset, length);

        NioBuffer copy = NioBufferPool.allocate(length);
        try {
            copyInto(offset, copy, 0, length);
            copy.writerOffset(length);
            return copy;
        } catch (Throwable e) {
            throw e;
        }
    }


    public NioBuffer readSplit(int length) {
        return split(readerOffset() + length);
    }

    public NioBuffer writeSplit(int length) {
        return split(writerOffset() + length);
    }


    public NioBuffer split() {
        return split(writerOffset());
    }

    public NioBuffer split(int splitOffset) {
        if (splitOffset < 0) {
            throw new IllegalArgumentException("The split offset cannot be negative: " + splitOffset + '.');
        }
        if (capacity() < splitOffset) {
            throw new IllegalArgumentException("The split offset cannot be greater than the buffer capacity, " +
                    "but the split offset was " + splitOffset + ", and capacity is " + capacity() + '.');
        }

        ByteBuffer splitByteBuffer = bbslice(rmem, 0, splitOffset);
        NioBuffer splitNioBuffer = new NioBuffer(splitByteBuffer);

        splitNioBuffer.woff = Math.min(woff, splitOffset);
        splitNioBuffer.roff = Math.min(roff, splitOffset);
        refs++;
        // Split preserves const-state.
        rmem = bbslice(rmem, splitOffset, rmem.capacity() - splitOffset);
        woff = Math.max(woff, splitOffset) - splitOffset;
        roff = Math.max(roff, splitOffset) - splitOffset;
        return splitNioBuffer;
    }

    public NioBuffer compact() {
        if (roff == 0) {
            return this;
        }
        rmem.limit(woff).position(roff).compact().clear();
        woff -= roff;
        roff = 0;
        return this;
    }


    public boolean hasReadableArray() {
        return rmem.hasArray();
    }


    public byte[] readableArray() {
        return rmem.array();
    }


    public int readableArrayOffset() {
        return rmem.arrayOffset() + roff;
    }


    public int readableArrayLength() {
        return woff - roff;
    }


    public ByteBuffer readableBuffer() {
        return bbslice(rmem.asReadOnlyBuffer(), readerOffset(), readableBytes());
    }


    public ByteBuffer mutableReadableBuffer() {
        return bbslice(rmem, readerOffset(), readableBytes());
    }


    public boolean hasWritableArray() {
        return wmem.hasArray();
    }


    public byte[] writableArray() {
        return wmem.array();
    }


    public int writableArrayOffset() {
        return wmem.arrayOffset() + woff;
    }


    public int writableArrayLength() {
        return writableBytes();
    }


    public ByteBuffer writableBuffer() {
        return bbslice(wmem, writerOffset(), writableBytes());
    }



    public byte readByte() {
        checkRead(roff, Byte.BYTES);
        byte value = rmem.get(roff);
        roff += Byte.BYTES;
        return value;
    }


    public byte getByte(int roff) {
        checkGet(roff, Byte.BYTES);
        return rmem.get(roff);
    }


    public int readUnsignedByte() {
        return readByte() & 0xFF;
    }


    public int getUnsignedByte(int roff) {
        return getByte(roff) & 0xFF;
    }


    public NioBuffer writeByte(byte value) {
        checkWrite(woff, Byte.BYTES, true);
        try {
            wmem.put(woff, value);
            woff += Byte.BYTES;
            return this;
        } catch (IndexOutOfBoundsException e) {
            throw checkWriteState(e, woff, Byte.BYTES);
        }
    }


    public NioBuffer setByte(int woff, byte value) {
        try {
            wmem.put(woff, value);
            return this;
        } catch (IndexOutOfBoundsException e) {
            throw checkWriteState(e, woff, Byte.BYTES);
        }
    }


    public NioBuffer writeUnsignedByte(int value) {
        checkWrite(woff, Byte.BYTES, true);
        try {
            wmem.put(woff, (byte) (value & 0xFF));
            woff += Byte.BYTES;
            return this;
        } catch (IndexOutOfBoundsException e) {
            throw checkWriteState(e, woff, Byte.BYTES);
        }
    }


    public NioBuffer setUnsignedByte(int woff, int value) {
        try {
            wmem.put(woff, (byte) (value & 0xFF));
            return this;
        } catch (IndexOutOfBoundsException e) {
            throw checkWriteState(e, woff, Byte.BYTES);
        }
    }


    public char readChar() {
        checkRead(roff, Character.BYTES);
        char value = rmem.getChar(roff);
        roff += Character.BYTES;
        return value;
    }


    public char getChar(int roff) {
        checkGet(roff, Character.BYTES);
        return rmem.getChar(roff);
    }


    public NioBuffer writeChar(char value) {
        checkWrite(woff, Character.BYTES, true);
        try {
            wmem.putChar(woff, value);
            woff += Character.BYTES;
            return this;
        } catch (IndexOutOfBoundsException e) {
            throw checkWriteState(e, woff, Character.BYTES);
        }
    }


    public NioBuffer setChar(int woff, char value) {
        try {
            wmem.putChar(woff, value);
            return this;
        } catch (IndexOutOfBoundsException e) {
            throw checkWriteState(e, woff, Character.BYTES);
        }
    }


    public short readShort() {
        checkRead(roff, Short.BYTES);
        short value = rmem.getShort(roff);
        roff += Short.BYTES;
        return value;
    }


    public short getShort(int roff) {
        checkGet(roff, Short.BYTES);
        return rmem.getShort(roff);
    }


    public int readUnsignedShort() {
        checkRead(roff, Short.BYTES);
        int value = rmem.getShort(roff) & 0xFFFF;
        roff += Short.BYTES;
        return value;
    }


    public int getUnsignedShort(int roff) {
        checkGet(roff, Short.BYTES);
        return rmem.getShort(roff) & 0xFFFF;
    }


    public NioBuffer writeShort(short value) {
        checkWrite(woff, Short.BYTES, true);
        try {
            wmem.putShort(woff, value);
            woff += Short.BYTES;
            return this;
        } catch (IndexOutOfBoundsException e) {
            throw checkWriteState(e, woff, Short.BYTES);
        }
    }


    public NioBuffer setShort(int woff, short value) {
        try {
            wmem.putShort(woff, value);
            return this;
        } catch (IndexOutOfBoundsException e) {
            throw checkWriteState(e, woff, Short.BYTES);
        }
    }


    public NioBuffer writeUnsignedShort(int value) {
        checkWrite(woff, Short.BYTES, true);
        try {
            wmem.putShort(woff, (short) (value & 0xFFFF));
            woff += Short.BYTES;
            return this;
        } catch (IndexOutOfBoundsException e) {
            throw checkWriteState(e, woff, Short.BYTES);
        }
    }


    public NioBuffer setUnsignedShort(int woff, int value) {
        try {
            wmem.putShort(woff, (short) (value & 0xFFFF));
            return this;
        } catch (IndexOutOfBoundsException e) {
            throw checkWriteState(e, woff, Short.BYTES);
        }
    }


    public int readMedium() {
        checkRead(roff, 3);
        int value = rmem.get(roff) << 16 | (rmem.get(roff + 1) & 0xFF) << 8 | rmem.get(roff + 2) & 0xFF;
        roff += 3;
        return value;
    }


    public int getMedium(int roff) {
        checkGet(roff, 3);
        return rmem.get(roff) << 16 | (rmem.get(roff + 1) & 0xFF) << 8 | rmem.get(roff + 2) & 0xFF;
    }


    public int readUnsignedMedium() {
        checkRead(roff, 3);
        int value = (rmem.get(roff) << 16 | (rmem.get(roff + 1) & 0xFF) << 8 | rmem.get(roff + 2) & 0xFF) & 0xFFFFFF;
        roff += 3;
        return value;
    }


    public int getUnsignedMedium(int roff) {
        checkGet(roff, 3);
        return (rmem.get(roff) << 16 | (rmem.get(roff + 1) & 0xFF) << 8 | rmem.get(roff + 2) & 0xFF) & 0xFFFFFF;
    }


    public NioBuffer writeMedium(int value) {
        checkWrite(woff, 3, true);
        wmem.put(woff, (byte) (value >> 16));
        wmem.put(woff + 1, (byte) (value >> 8 & 0xFF));
        wmem.put(woff + 2, (byte) (value & 0xFF));
        woff += 3;
        return this;
    }


    public NioBuffer setMedium(int woff, int value) {
        checkSet(woff, 3);
        wmem.put(woff, (byte) (value >> 16));
        wmem.put(woff + 1, (byte) (value >> 8 & 0xFF));
        wmem.put(woff + 2, (byte) (value & 0xFF));
        return this;
    }


    public NioBuffer writeUnsignedMedium(int value) {
        checkWrite(woff, 3, true);
        wmem.put(woff, (byte) (value >> 16));
        wmem.put(woff + 1, (byte) (value >> 8 & 0xFF));
        wmem.put(woff + 2, (byte) (value & 0xFF));
        woff += 3;
        return this;
    }


    public NioBuffer setUnsignedMedium(int woff, int value) {
        checkSet(woff, 3);
        wmem.put(woff, (byte) (value >> 16));
        wmem.put(woff + 1, (byte) (value >> 8 & 0xFF));
        wmem.put(woff + 2, (byte) (value & 0xFF));
        return this;
    }


    public int readInt() {
        checkRead(roff, Integer.BYTES);
        var value = rmem.getInt(roff);
        roff += Integer.BYTES;
        return value;
    }


    public int getInt(int roff) {
        checkGet(roff, Integer.BYTES);
        return rmem.getInt(roff);
    }


    public long readUnsignedInt() {
        checkRead(roff, Integer.BYTES);
        long value = rmem.getInt(roff) & 0xFFFFFFFFL;
        roff += Integer.BYTES;
        return value;
    }


    public long getUnsignedInt(int roff) {
        checkGet(roff, Integer.BYTES);
        return rmem.getInt(roff) & 0xFFFFFFFFL;
    }


    public NioBuffer writeInt(int value) {
        checkWrite(woff, Integer.BYTES, true);
        try {
            wmem.putInt(woff, value);
            woff += Integer.BYTES;
            return this;
        } catch (IndexOutOfBoundsException e) {
            throw checkWriteState(e, woff, Integer.BYTES);
        }
    }


    public NioBuffer setInt(int woff, int value) {
        try {
            wmem.putInt(woff, value);
            return this;
        } catch (IndexOutOfBoundsException e) {
            throw checkWriteState(e, this.woff, Integer.BYTES);
        }
    }


    public NioBuffer writeUnsignedInt(long value) {
        checkWrite(woff, Integer.BYTES, true);
        try {
            wmem.putInt(woff, (int) (value & 0xFFFFFFFFL));
            woff += Integer.BYTES;
            return this;
        } catch (IndexOutOfBoundsException e) {
            throw checkWriteState(e, woff, Integer.BYTES);
        }
    }


    public NioBuffer setUnsignedInt(int woff, long value) {
        try {
            wmem.putInt(woff, (int) (value & 0xFFFFFFFFL));
            return this;
        } catch (IndexOutOfBoundsException e) {
            throw checkWriteState(e, this.woff, Integer.BYTES);
        }
    }


    public float readFloat() {
        checkRead(roff, Float.BYTES);
        float value = rmem.getFloat(roff);
        roff += Float.BYTES;
        return value;
    }


    public float getFloat(int roff) {
        checkGet(roff, Float.BYTES);
        return rmem.getFloat(roff);
    }


    public NioBuffer writeFloat(float value) {
        checkWrite(woff, Float.BYTES, true);
        try {
            wmem.putFloat(woff, value);
            woff += Float.BYTES;
            return this;
        } catch (IndexOutOfBoundsException e) {
            throw checkWriteState(e, woff, Float.BYTES);
        }
    }


    public NioBuffer setFloat(int woff, float value) {
        try {
            wmem.putFloat(woff, value);
            return this;
        } catch (IndexOutOfBoundsException e) {
            throw checkWriteState(e, woff, Float.BYTES);
        }
    }


    public long readLong() {
        checkRead(roff, Long.BYTES);
        var value = rmem.getLong(roff);
        roff += Long.BYTES;
        return value;
    }


    public long getLong(int roff) {
        checkGet(roff, Long.BYTES);
        return rmem.getLong(roff);
    }


    public NioBuffer writeLong(long value) {
        checkWrite(woff, Long.BYTES, true);
        try {
            wmem.putLong(woff, value);
            woff += Long.BYTES;
            return this;
        } catch (IndexOutOfBoundsException e) {
            throw checkWriteState(e, woff, Long.BYTES);
        }
    }


    public NioBuffer setLong(int woff, long value) {
        try {
            wmem.putLong(woff, value);
            return this;
        } catch (IndexOutOfBoundsException e) {
            throw checkWriteState(e, woff, Long.BYTES);
        }
    }


    public double readDouble() {
        checkRead(roff, Double.BYTES);
        var value = rmem.getDouble(roff);
        roff += Double.BYTES;
        return value;
    }


    public double getDouble(int roff) {
        checkGet(roff, Double.BYTES);
        return rmem.getDouble(roff);
    }


    public NioBuffer writeDouble(double value) {
        checkWrite(woff, Double.BYTES, true);
        try {
            wmem.putDouble(woff, value);
            woff += Double.BYTES;
            return this;
        } catch (IndexOutOfBoundsException e) {
            throw checkWriteState(e, woff, Double.BYTES);
        }
    }


    public NioBuffer setDouble(int woff, double value) {
        try {
            wmem.putDouble(woff, value);
            return this;
        } catch (IndexOutOfBoundsException e) {
            throw checkWriteState(e, woff, Double.BYTES);
        }
    }


    public boolean readBoolean() {
        return readByte() != 0;
    }


    public boolean getBoolean(int roff) {
        return getByte(roff) != 0;
    }


    public NioBuffer writeBoolean(boolean value) {
        return writeByte((byte) (value ? 1 : 0));
    }


    public NioBuffer setBoolean(int woff, boolean value) {
        return setByte(woff, (byte) (value ? 1 : 0));
    }




    private void checkRead(int index, int size) {
        if (index < 0 | woff < index + size) {
            throw readAccessCheckException(index, size);
        }
    }

    private void checkGet(int index, int size) {
        if (index < 0 | capacity() < index + size) {
            throw readAccessCheckException(index, size);
        }
    }

    private void checkWrite(int index, int size, boolean mayExpand) {
        if (index < roff | wmem.capacity() < index + size) {
            handleWriteAccessBoundsFailure(index, size, mayExpand);
        }
    }

    private void checkSet(int index, int size) {
        if (index < 0 | wmem.capacity() < index + size) {
            handleWriteAccessBoundsFailure(index, size, false);
        }
    }

    private RuntimeException checkWriteState(IndexOutOfBoundsException ioobe, int offset, int size) {

        IndexOutOfBoundsException exception = outOfBounds(offset, size);
        exception.addSuppressed(ioobe);
        return exception;
    }

    private RuntimeException readAccessCheckException(int index, int size) {
        return outOfBounds(index, size);
    }

    private void handleWriteAccessBoundsFailure(int index, int size, boolean mayExpand) {

        int capacity = capacity();
        if (mayExpand && index >= 0 && index <= capacity && woff + size <= implicitCapacityLimit) {
            // Grow into next power-of-two, but not beyond the implicit limit.
            int minimumGrowth = Math.min(
                    Math.max(roundToPowerOfTwo(capacity * 2), size),
                    implicitCapacityLimit) - capacity;
            ensureWritable(size, minimumGrowth, false);
            checkSet(index, size); // Verify writing is now possible, without recursing.
            return;
        }
        throw outOfBounds(index, size);
    }


    private IndexOutOfBoundsException outOfBounds(int index, int size) {
        return new IndexOutOfBoundsException(
                "Access at index " + index + " of size " + size + " is out of bounds: " +
                        "[read 0 to " + woff + ", write 0 to " + rmem.capacity() + "].");
    }




    @Override
    public boolean equals(Object obj) {
        return NioBufferUtils.equals(this, (NioBuffer) obj);
    }

    @Override
    public int hashCode() {
        return NioBufferUtils.hashCode(this);
    }


    public void recycle() {
        // TODO
        if (refs > 0 && --refs == 0) {
            NioBufferPool.recycle(this);
        }
    }
}
