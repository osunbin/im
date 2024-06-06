package com.bin.im.entry.spi;

import com.bin.im.entry.tcp.handle.EntryContext;


public interface Invoker {

    void invoke(EntryContext context);
}
