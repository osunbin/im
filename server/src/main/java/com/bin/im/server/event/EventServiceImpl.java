package com.bin.im.server.event;


import com.bin.im.server.spi.impl.ImEngineImpl;
import org.slf4j.MDC;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class EventServiceImpl implements EventService {

    private final ConcurrentMap<String, List<EventRegistration<?>>> segments
            = new ConcurrentHashMap<>();

    private StripedExecutor eventExecutor;
    private ImEngineImpl imEngine;


    public EventServiceImpl(ImEngineImpl imEngine) {
        this.imEngine = imEngine;
        eventExecutor = new StripedExecutor();

    }

    @Override
    public void registerListener(String topic, MessageListener<?> listener) {

        getSegment(topic, listener);
    }



    public synchronized List<EventRegistration<?>> getSegment(String topic, MessageListener<?> listener) {
        List<EventRegistration<?>> listeners = segments.get(topic);
        if (listeners == null) {
            listeners = new ArrayList<>();
            segments.putIfAbsent(topic, listeners);
        }
        EventRegistration<?> newSegment = new EventRegistration<>(topic, listener); // nodeEngine.getService(service)
        listeners.add(newSegment);
        return listeners;
    }





    private List<EventRegistration<?>> getRegistrations(String topic) {
        return segments.get(topic);
    }

    @Override
    public void publishEvent(Message<?> message) {
        String topic = message.getTopic();
        List<EventRegistration<?>> registrations = getRegistrations(topic);
        for (EventRegistration<?> registration : registrations) {
            if (registration != null) {
                eventExecutor.execute(new LocalEvent(registration, message, MDC.getCopyOfContextMap()));
            }
        }
    }

}
