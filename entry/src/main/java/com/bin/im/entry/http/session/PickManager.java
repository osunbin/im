package com.bin.im.entry.http.session;

import com.bin.im.common.internal.utils.TimeUtils;
import io.netty.channel.Channel;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class PickManager {



    private int timeoutInterval = 12 * 1000;

    private Map<Long,Pick> uidPickMap = new ConcurrentHashMap<>();


    public void savePick(long uid, Channel channel) {
        Pick pick = uidPickMap.get(uid);

        long now = TimeUtils.currTimeMillis();

        if (pick == null) {
            pick = new Pick();
            pick.setUid(uid);
            pick.setPickTime(now);
            pick.setChannel(channel);
            uidPickMap.put(uid,pick);
        }else {

            pick.setUid(uid);
            pick.setPickTime(now);
            pick.setChannel(channel);

            channel.writeAndFlush("send ok"); // 通知
        }
        // TODO
        channel.closeFuture().addListener(future -> {
            uidPickMap.remove(uid);
           // httpConnManager.removeConn();
        });
    }

    public boolean trySendPick(long uid,String msg) {
        Pick pick = uidPickMap.get(uid);
        if (pick == null) {
            return false;
        }
        Channel channel = pick.getChannel();
        channel.writeAndFlush("send ok");
        uidPickMap.remove(uid);
        return  true;
    }

    public void checkPick() {
        while (true) {
            TimeUtils.sleeps(10);
            String noPick = "{\"cmd\":\"user\", \"sub_cmd\":\"pick\", \"code\":200000}";
            long now = TimeUtils.currTimeMillis();
            List<Pick> collect = uidPickMap.values().stream().collect(Collectors.toList());
            for (Pick pick : collect) {
                if (now > timeoutInterval + pick.getPickTime()) {
                    Channel channel = pick.getChannel();
                    channel.writeAndFlush(noPick);
                    uidPickMap.remove(pick.getUid());
                }
            }
        }
    }

}
