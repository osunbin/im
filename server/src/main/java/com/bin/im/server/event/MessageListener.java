package com.bin.im.server.event;

import java.util.EventListener;

@FunctionalInterface
public interface MessageListener<E> extends EventListener {



    /**
     * Invoked when a message is received for the topic. Note that the topic guarantees message ordering.
     * Therefore there is only one thread invoking onMessage. The user should not keep the thread busy, but preferably
     * should dispatch it via an Executor. This will increase the performance of the topic.
     *
     * @param message the message that is received for the topic
     */
    void onMessage(Message<E> message);
}