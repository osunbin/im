package com.bin.im.server.event;

import org.slf4j.MDC;

import java.util.Map;

public class LocalEvent implements StripedRunnable{

    private final EventRegistration registrations;
    private final Message<?> message;
    private final Map<String, String> context;

    public LocalEvent(EventRegistration registrations, Message<?> message,Map<String, String> context) {

        this.registrations = registrations;

        this.message = message;
        this.context = context;
    }

    @Override
    public String getKey() {
        return message.getKey();
    }

    @Override
    public void run() {
        if (context == null) {
            MDC.clear();
        } else {
            MDC.setContextMap(context);
        }
        registrations.onMessage(message);
    }
}
