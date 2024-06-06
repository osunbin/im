package com.bin.im.entry.tcp.session;

import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 连接检测  10s
 */
public class ConnectManager {
    private int clientTimeouts = 600;
    Map<Long, Channel> connectMap = new ConcurrentHashMap<>();

    private int checkInterval = 10;
    Map<Long, List<Long>> timerKeyMap = new ConcurrentHashMap<>();


    private List<Long> getCheckItems(long time) {
        int index = (int) (time % checkInterval);
        return timerKeyMap.get(index);
    }

    public void timeoutCheck() {
        long lastTime = currSecsonds();

        while (true) {
            sleeps();
            long nowTime = currSecsonds();
            if (lastTime < nowTime) {

                List<Long> checkItems = getCheckItems(lastTime);
                for (long uid : checkItems) {
                    Channel channel = connectMap.get(uid);
                    if (channel != null) {
                        long touchTime  = 0; // GetTouchTime
                        if (nowTime > touchTime + clientTimeouts) {
                            // TODO  m_StatusData.m_bBlock = 1;


                        }



                    }
                }


                lastTime++;
            }


        }
    }


    private long currSecsonds() {
        return System.currentTimeMillis() / 1000;
    }

    private void sleeps() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
