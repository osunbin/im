package com.bin.im.common.internal.concurrent;

/**
 * Idle strategy for use by threads when then they don't have work to do.
 */
public interface IdleStrategy {
    /**
     * Perform current idle strategy's step <i>n</i>.
     *
     * @param n number of times this method has been previously called with no intervening work done.
     * @return whether the strategy has reached the longest pause time.
     */
    boolean idle(long n);
}