package com.bin.im.entry.tcp.handle;

import com.bin.im.entry.tcp.session.OnePeer;
import com.bin.im.entry.tcp.session.OnePeerManager;
import io.netty.channel.Channel;

public class ChannelCloseHandler {

    private OnePeerManager onePeerManager;



    public void channelClose(Channel channel) {
        channelClose(channel,null);
    }

    public void channelClose(Channel channel,Throwable cause) {
        OnePeer onePeer = onePeerManager.getOnePeer(channel);
        if (onePeer != null) {
            onePeer.onTimeout();
        }
        channel.close();
    }
}
