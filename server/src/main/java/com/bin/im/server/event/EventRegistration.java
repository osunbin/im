package com.bin.im.server.event;


import java.util.Objects;

public class EventRegistration<E> {

    private final String topic;


    private final MessageListener<E> service;


    public EventRegistration(String topic, MessageListener<E> service) {
        this.topic = topic;
        this.service = service;
    }



    public void onMessage(Message<E> message){
        service.onMessage(message);
    }


    public String getTopic() {
        return topic;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventRegistration<?> that = (EventRegistration<?>) o;
        return Objects.equals(topic, that.topic) && Objects.equals(service, that.service);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topic, service);
    }
}
