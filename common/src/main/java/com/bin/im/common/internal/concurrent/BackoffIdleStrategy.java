package com.bin.im.common.internal.concurrent;

import java.util.concurrent.locks.LockSupport;

import static java.lang.Math.min;
import static java.util.concurrent.TimeUnit.MICROSECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class BackoffIdleStrategy implements IdleStrategy {



    final long yieldThreshold;
    final long parkThreshold;
    final long minParkPeriodNs;
    final long maxParkPeriodNs;
    private final int maxShift;

    private static final long IDLE_MAX_SPINS = 20;
    private static final long IDLE_MAX_YIELDS = 50;
    private static final long IDLE_MIN_PARK_NS = NANOSECONDS.toNanos(1);
    private static final long IDLE_MAX_PARK_NS = MICROSECONDS.toNanos(100);



    public BackoffIdleStrategy() {
        this.yieldThreshold = IDLE_MAX_SPINS;
        this.parkThreshold = IDLE_MAX_SPINS + IDLE_MAX_YIELDS;
        this.minParkPeriodNs = IDLE_MIN_PARK_NS;
        this.maxParkPeriodNs = IDLE_MAX_PARK_NS;
        this.maxShift = Long.numberOfLeadingZeros(minParkPeriodNs) - Long.numberOfLeadingZeros(maxParkPeriodNs);
    }
    /**
     * Create a set of state tracking idle behavior
     *
     * @param maxSpins        to perform before moving to {@link Thread#yield()}
     * @param maxYields       to perform before moving to {@link LockSupport#parkNanos(long)}
     * @param minParkPeriodNs to use when initiating parking
     * @param maxParkPeriodNs to use when parking
     *  20 50  1ns  100s
     */
    public BackoffIdleStrategy(long maxSpins, long maxYields, long minParkPeriodNs, long maxParkPeriodNs) {
        checkNotNegative(maxSpins, "maxSpins must be positive or zero");
        checkNotNegative(maxYields, "maxYields must be positive or zero");
        checkNotNegative(minParkPeriodNs, "minParkPeriodNs must be positive or zero");
        checkNotNegative(maxParkPeriodNs - minParkPeriodNs,
                "maxParkPeriodNs must be greater than or equal to minParkPeriodNs");
        this.yieldThreshold = maxSpins;
        this.parkThreshold = maxSpins + maxYields;
        this.minParkPeriodNs = minParkPeriodNs;
        this.maxParkPeriodNs = maxParkPeriodNs;
        this.maxShift = Long.numberOfLeadingZeros(minParkPeriodNs) - Long.numberOfLeadingZeros(maxParkPeriodNs);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean idle(long n) {
        if (n < yieldThreshold) {
            return false;
        }
        if (n < parkThreshold) {
            Thread.yield();
            return false;
        }
        final long parkTime = parkTime(n);
        LockSupport.parkNanos(parkTime);
        return parkTime == maxParkPeriodNs;
    }

    long parkTime(long n) {
        final long proposedShift = n - parkThreshold;
        final long allowedShift = min(maxShift, proposedShift);
        return proposedShift > maxShift ? maxParkPeriodNs
                : proposedShift < maxShift ? minParkPeriodNs << allowedShift
                : min(minParkPeriodNs << allowedShift, maxParkPeriodNs);
    }




    public static long checkNotNegative(long value, String errorMessage) {
        if (value < 0) {
            throw new IllegalArgumentException(errorMessage);
        }
        return value;
    }
}
