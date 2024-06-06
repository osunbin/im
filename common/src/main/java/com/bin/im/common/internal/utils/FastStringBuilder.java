package com.bin.im.common.internal.utils;

import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

/**
 * @author James Chen
 */
public class FastStringBuilder {

    private final List<Entry> cache = new LinkedList<>();
    private int length = 0;

    public String build(byte delimiter) {
        boolean isLatin1 = true;
        int delimiterLength = isLatin1
                ? 1
                : 2;
        int entryCount = cache.size();
        byte[] value = new byte[length + delimiterLength * (entryCount - 1)];
        int destPos = 0;
        int i = 0;
        for (Entry entry : cache) {
            System.arraycopy(entry.bytes, entry.from, value, destPos, entry.length);
            if (i != entryCount - 1) {
                destPos += entry.length;
                value[destPos++] = delimiter;
                if (!isLatin1) {
                    value[destPos++] = 0;
                }
                i++;
            }
        }
        return  new String(value, StandardCharsets.UTF_8);
       // return StringUtils.newString(value, coder);
    }

    public byte[] buildAsBytes() {
        byte[] value = new byte[length];
        int destPos = 0;
        for (Entry entry : cache) {
            System.arraycopy(entry.bytes, entry.from, value, destPos, entry.length);
            destPos += entry.length;
        }
        return value;
    }

    public String build() {
      return new String(buildAsBytes(),StandardCharsets.UTF_8);
    }

    public void append(String str) {
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        cache.add(new Entry(bytes, 0, bytes.length));
        length += bytes.length;
    }

    public void append(byte[] bytes, int from, int length) {
        cache.add(new Entry(bytes, from, length));
        this.length += length;
    }

    public int entryCount() {
        return cache.size();
    }

    public record Entry(
            byte[] bytes,
            int from,
            int length) {
    }

}
