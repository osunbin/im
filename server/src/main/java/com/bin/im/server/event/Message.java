package com.bin.im.server.event;

import java.util.Map;

public class Message<E> {
    private String topic;
    private String tag;
    private String key;
    private String subscriber;
    private Map<String,Object> header;
    private E obj;

    public static <E> Message<E> of( E obj) {
        return new Message(obj);
    }

    public static <E> Message<E> of(String topic, E obj) {
        return new Message(topic,obj);
    }

    public Message(E obj) {
        this.obj = obj;
    }

    public Message(String topic, E obj) {
        this.topic = topic;
        this.obj = obj;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(String subscriber) {
        this.subscriber = subscriber;
    }

    public Map<String, Object> getHeader() {
        return header;
    }

    public void setHeader(Map<String, Object> header) {
        this.header = header;
    }

    public E getObj() {
        return obj;
    }

    public void setObj(E obj) {
        this.obj = obj;
    }
}
