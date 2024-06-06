package com.bin.im.common.internal.wheel;


import java.util.Objects;
import java.util.concurrent.Executor;

public final class ImmediateExecutor implements Executor {
    public static final ImmediateExecutor INSTANCE = new ImmediateExecutor();

    private ImmediateExecutor() {
    }

    public void execute(Runnable command) {
        ((Runnable) Objects.requireNonNull(command, "command")).run();
    }
}