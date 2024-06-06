package com.bin.im.entry.tcp.handle;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class EntryHandler {

    public static final Map<Long,EntryContext> seqMap = new ConcurrentHashMap<>();

    private static final AtomicLong seqs = new AtomicLong();

    public String key() {
        return "";
    }




    public void perform(EntryContext context) {


    }


    public long registerReq(EntryContext context) {

        long seq = seqs.incrementAndGet();

        seqMap.put(seq,context);

        context.getAttachments().put("seq",seq);
        return seq;
    }

    public EntryContext unRegisterReq(long seq) {

       return seqMap.remove(seq);
    }

    public EntryContext getContext(long seq) {
      return   seqMap.get(seq);
    }

}
