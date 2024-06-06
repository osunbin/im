package com.bin.im.common.internal.concurrent;

/**
 *以尽可能低的延迟为目标的繁忙旋转策略。此策略将独占线程以实现尽可能低的延迟。
 * 对于在没有其他循环的紧密繁忙旋转循环的执行管道中创建气泡非常有用,对进度进行逻辑检查而非状态检查
 */
public class BusySpinIdleStrategy implements IdleStrategy {
    private int dummyCounter;

    public boolean idle(final long n) {
        final int dummyValue = 64;
        // Trick speculative execution into not progressing
        if (dummyCounter > 0) {
            if (Math.random() > 0) {
                --dummyCounter;
            }
        } else {
            dummyCounter = dummyValue;
        }
        return true;
    }
}