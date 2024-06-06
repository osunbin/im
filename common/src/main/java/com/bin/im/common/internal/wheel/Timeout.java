package com.bin.im.common.internal.wheel;

public interface Timeout {
    Timer timer();

    TimerTask task();

    boolean isExpired();

    boolean isCancelled();

    boolean cancel();
}