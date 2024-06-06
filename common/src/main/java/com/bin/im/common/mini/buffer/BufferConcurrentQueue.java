package com.bin.im.common.mini.buffer;



import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;

import static java.lang.Long.numberOfLeadingZeros;

public class BufferConcurrentQueue {
    private final AtomicLong pos = new AtomicLong(0);
    private final AtomicReference<AtomicReferenceArray<NioBuffer>> array = new AtomicReference<>(new AtomicReferenceArray<>(1));

    private final ConcurrentHashMap<Integer, NioBuffer> map = new ConcurrentHashMap<>();

    final AtomicInteger realMin = new AtomicInteger(0);


    public NioBuffer poll() {
        long pos1, pos2;
        int head, tail;
        do {
            pos1 = pos.get();
            head = (int) (pos1 >>> 32);
            tail = (int) pos1;
            if (head == tail) {
                return null;
            }
            tail++;
            // watchdog
            if (NioBufferPool.USE_WATCHDOG) {
                int size = head - tail;
                realMin.updateAndGet(prevMin -> Math.min(prevMin, size));
            }
            pos2 = ((long) head << 32) + (tail & 0xFFFFFFFFL);
        } while (!pos.compareAndSet(pos1, pos2));

        Integer boxedTail = null;


        while (true) {
            AtomicReferenceArray<NioBuffer> bufs = array.get();
            NioBuffer buf = bufs.getAndSet(tail & (bufs.length() - 1), null);
            if (buf == null) {
                if (boxedTail == null) {
                    boxedTail = tail;
                }
                buf = map.remove(boxedTail);
                if (buf == null) {
                    Thread.yield();
                    continue;
                }
            }
            if (buf.pos == tail) {
                return buf;
            }
            map.put(buf.pos, buf);
        }
    }

    public void offer(NioBuffer buf) {
        long pos1, pos2;
        do {
            pos1 = pos.get();
            pos2 = pos1 + 0x100000000L;
        } while (!pos.compareAndSet(pos1, pos2));

        int head = (int) (pos2 >>> 32);
        buf.pos = head;

        AtomicReferenceArray<NioBuffer> bufs = array.get();
        int idx = head & (bufs.length() - 1);
        NioBuffer buf2 = bufs.getAndSet(idx, buf);
        if (buf2 == null && bufs == array.get()) {
            return; // fast path, everything is fine
        }
        // otherwise, evict bufs into map to make it retrievable by corresponding pop()
        pushToMap(bufs, idx, buf2);
    }

    private void pushToMap(AtomicReferenceArray<NioBuffer> bufs, int idx,  NioBuffer buf2) {
        NioBuffer buf3 = bufs.getAndSet(idx, null); // bufs may be stale at this moment, evict the data from this cell
        if (buf2 == null && buf3 == null) return;
        if (buf2 != null) map.put(buf2.pos, buf2);
        if (buf3 != null) map.put(buf3.pos, buf3);
        ensureCapacity(); // resize if needed
    }

    private void ensureCapacity() {
        int capacityNew = 1 << 32 - numberOfLeadingZeros(size() * 4 - 1);
        if (array.get().length() >= capacityNew) return;
        resize(capacityNew);
    }

    private void resize(int capacityNew) {
        AtomicReferenceArray<NioBuffer> bufsNew = new AtomicReferenceArray<>(capacityNew);
        AtomicReferenceArray<NioBuffer> bufsOld = array.getAndSet(bufsNew);
        // evict everything from old bufs array
        for (int i = 0; i < bufsOld.length(); i++) {
            NioBuffer buf = bufsOld.getAndSet(i, null);
            if (buf != null) map.put(buf.pos, buf);
        }
    }

    public void clear() {
        while (!isEmpty()) {
            poll();
        }
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
        long pos1 = pos.get();
        int head = (int) (pos1 >>> 32);
        int tail = (int) pos1;
        return head - tail;
    }

    @Override
    public String toString() {
        return "ByteBufConcurrentQueue{" +
                "size=" + size() +
                ", array=" + array.get().length() +
                ", map=" + map.size() +
                '}';
    }
}
