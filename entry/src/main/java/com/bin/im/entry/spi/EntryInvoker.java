package com.bin.im.entry.spi;

import com.bin.im.entry.tcp.handle.EntryContext;


public interface EntryInvoker {


    void invoker(EntryContext context);
}
