package com.bin.im.entry.spi;

import com.bin.im.entry.tcp.handle.EntryContext;

public interface InvokerAfter {

   void after(EntryContext context);
}
