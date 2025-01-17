package com.bin.im.common.internal.wheel;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface Timer {
    Timeout newTimeout(TimerTask var1, long var2, TimeUnit var4);

    Set<Timeout> stop();
}