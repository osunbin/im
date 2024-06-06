package com.bin.im.entry.http.session;

import io.netty.channel.Channel;

public class Pick {
    private long uid;
    private long pickTime;
    private Channel channel;


    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getPickTime() {
        return pickTime;
    }

    public void setPickTime(long pickTime) {
        this.pickTime = pickTime;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
