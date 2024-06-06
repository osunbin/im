package com.bin.im.common.internal.lang;

import java.nio.CharBuffer;
import java.util.Arrays;

/**
 * @author James Chen
 */
public class CharArrayBuffer implements CharSequence {

    private char[] buffer;
    private int length;

    public CharArrayBuffer(int capacity) {
        buffer = new char[capacity];
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(buffer);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof CharArrayBuffer that) {
            return Arrays.equals(buffer, that.buffer);
        }
        return false;
    }

    @Override
    public char charAt(int i) {
        return buffer[i];
    }

    @Override
    public int length() {
        return length;
    }

    @Override
    public CharSequence subSequence(int beginIndex, int endIndex) {
        return CharBuffer.wrap(buffer, beginIndex, endIndex);
    }

    @Override
    public String toString() {
        return new String(buffer, 0, length);
    }

    public char[] buffer() {
        return buffer;
    }

    public int capacity() {
        return buffer.length;
    }

    public boolean isEmpty() {
        return length == 0;
    }

    public void append(char[] chars) {
        append(chars, 0, chars.length);
    }

    public void append(char[] chars, int offset, int charsLength) {
        if (chars == null || charsLength == 0) {
            return;
        }
        int newLength = length + charsLength;
        if (newLength > buffer.length) {
            expand(newLength);
        }
        System.arraycopy(chars, offset, buffer, length, charsLength);
        length = newLength;
    }

    public void append( String str) {
        String s = str == null
                ? "null"
                : str;
        int strLength = s.length();
        int newLength = length + strLength;
        if (newLength > buffer.length) {
            expand(newLength);
        }
        s.getChars(0, strLength, buffer, length);
        length = newLength;
    }

    public void append( CharArrayBuffer b) {
        if (b == null) {
            return;
        }
        append(b.buffer, 0, b.length);
    }

    public void append(char c) {
        int newLength = length + 1;
        if (newLength > buffer.length) {
            expand(newLength);
        }
        buffer[length] = c;
        length = newLength;
    }

    public void clear() {
        length = 0;
    }

    private void expand(int newLength) {
        char[] newBuffer = new char[Math.max(buffer.length << 1, newLength)];
        System.arraycopy(buffer, 0, newBuffer, 0, length);
        buffer = newBuffer;
    }

    public char[] toCharArray() {
        char[] chars = new char[length];
        if (length > 0) {
            System.arraycopy(buffer, 0, chars, 0, length);
        }
        return chars;
    }

    public void ensureCapacity(int capacity) {
        if (capacity <= 0) {
            return;
        }
        int available = buffer.length - length;
        if (capacity > available) {
            expand(length + capacity);
        }
    }

    public String substring(int beginIndex, int endIndex) {
        return new String(buffer, beginIndex, endIndex - beginIndex);
    }



}