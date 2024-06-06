package com.bin.im.entry.tcp.session;

import com.bin.im.common.internal.utils.TimeUtils;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class OnePeerManager {

    private Logger logger = LoggerFactory.getLogger(OnePeerManager.class);


    public static final int clientTimeout = 600;

    private SessionManager sessionManager;

    private Map<Long, OnePeer> uidOnePeerMap = new ConcurrentHashMap<>();

    private Map<Long, OnePeer> uidOnePeerPhoneMap = new ConcurrentHashMap<>();

    private Map<Channel, OnePeer> channelOnePeerMap = new ConcurrentHashMap<>();


    // 5000
    private int checkInterval = 1000;


    private LinkedBlockingQueue<Channel>[] clientWheel;

    public OnePeerManager() {
        clientWheel = createWheel(checkInterval);
        Thread thread = new Thread(() -> onTimeWheel());
        thread.setName("client channel timeout check");
        // TODO

        sessionManager = new SessionManager();
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }


    public OnePeer getOnePeer(Channel channel) {
        return channelOnePeerMap.get(channel);
    }

    public void registryPnePeer(OnePeer peer) {

        channelOnePeerMap.put(peer.getChannel(), peer);

        addItem(peer.getCreateTime(), peer.getChannel());
        peer.bindManager(this);
    }


    public void unRegistryPnePeer(Channel channel) {
        OnePeer onePeer = getOnePeer(channel);
        if (onePeer != null) {
            removeIDOnePeerMap(onePeer);
            channelOnePeerMap.remove(channel);
            removeItem(onePeer.getCreateTime(), channel);
            logger.info("remove OnePeer channel uid:{}", onePeer.getUid());
        }
    }


    public void addIDOnePeerMap(long uid, OnePeer onePeer) {
        int terminal = sourceToTerminal(onePeer.getSourceType());
        if (E_TERMINAL_TYPE_UNKNOWN == terminal || E_TERMINAL_TYPE_ALL == terminal) {
            logger.error("uid:{} add invalid terminal type:{}", uid, onePeer.getSourceType());
            return;
        }

        switch (terminal) {
            case E_TERMINAL_TYPE_PC:
                uidOnePeerMap.put(uid, onePeer);
                break;
            case E_TERMINAL_TYPE_PHONE:
                uidOnePeerPhoneMap.put(uid, onePeer);
                break;
        }
    }

    public void removeIDOnePeerMap(OnePeer onePeer) {
        long uid = onePeer.getUid();
        int terminal = sourceToTerminal(onePeer.getSourceType());
        if (E_TERMINAL_TYPE_UNKNOWN == terminal || E_TERMINAL_TYPE_ALL == terminal) {
            logger.error("uid:{} remove invalid terminal type:{}", uid, onePeer.getSourceType());
            return;
        }
        if (E_TERMINAL_TYPE_PC == terminal &&
                uidOnePeerMap.get(uid) == onePeer) {
            uidOnePeerMap.remove(uid);
        } else if (E_TERMINAL_TYPE_PHONE == terminal &&
                uidOnePeerPhoneMap.get(uid) == onePeer) {
            uidOnePeerPhoneMap.remove(uid);
        }
    }


    public OnePeer getOnePeerFromUid(long uid, int sourceType) {
        OnePeer onePeer = null;
        int terminal = sourceToTerminal(sourceType);
        if (E_TERMINAL_TYPE_UNKNOWN == terminal || E_TERMINAL_TYPE_ALL == terminal) {
            logger.error("uid:{} get invalid terminal type:{}", uid, sourceType);
            return onePeer;
        }
        switch (terminal) {
            case E_TERMINAL_TYPE_PC:
                onePeer = uidOnePeerMap.get(uid);
                break;
            case E_TERMINAL_TYPE_PHONE:
                onePeer = uidOnePeerPhoneMap.get(uid);
                break;
        }
        return onePeer;

    }




    public void onTimeWheel() {
        int interval = 1;
        TimeUtils.sleeps(interval);
        long lastTime = TimeUtils.currTime();
        while (true) {
            TimeUtils.sleeps(interval);
            long nowTime = TimeUtils.currTime();
            for (;lastTime < nowTime;lastTime++) {
                onTimer(nowTime);
            }
        }
    }

    public void onTimer(long time) {
        Channel[] items = getCheckItems(time);
        for (int i = 0; i < items.length; i++) {
            Channel channel = items[i];
            OnePeer onePeer = getOnePeer(channel);
            if (onePeer == null) {
                continue;
            }
            onePeer.checkTimeout(time);
        }
    }



    private LinkedBlockingQueue<Channel>[] createWheel(int ticksPerWheel) {
        LinkedBlockingQueue<Channel>[] wheel = new LinkedBlockingQueue[ticksPerWheel];
        for (int i = 0; i < wheel.length; i++) {
            wheel[i] = new LinkedBlockingQueue<>();
        }
        return wheel;
    }


    public void addItem(long time, Channel channel) {
        int index = (int) (time % checkInterval);
        clientWheel[index].offer(channel);
    }

    public void removeItem(long time, Channel channel) {
        int index = (int) (time % checkInterval);
        clientWheel[index].remove(channel);
    }

    public Channel[] getCheckItems(long time) {
        int index = (int) (time % checkInterval);
        return clientWheel[index].toArray(new Channel[0]);
    }



    public void saveSession(OnePeer onePeer) {
        sessionManager.saveSession(onePeer.getUid(),onePeer.getSourceType(),onePeer.getServerEncrypt(),
                onePeer.getLoginTime(),onePeer.getSessionTimeout(),onePeer.getChannel());
    }



    public static final int E_TERMINAL_TYPE_ALL = 0,
            E_TERMINAL_TYPE_UNKNOWN = 1,
            E_TERMINAL_TYPE_PHONE = 2,
            E_TERMINAL_TYPE_PC = 3;

    public static boolean isPhoneBySource(int source) {
        int terminal = sourceToTerminal(source);
        if (E_TERMINAL_TYPE_PHONE == terminal) {
            return true;
        }
        return false;
    }

    public static int sourceToTerminal(int source) {
        int terminal = E_TERMINAL_TYPE_UNKNOWN;

        switch (source) {
            case 6:     //CLIENT_TYPE_MOBILE_WEB:
            case 7:     //CLIENT_TYPE_MOBILE:
            case 8:     //CLIENT_TYPE_MOBILE_IOS:
                //case 9:     //CLIENT_TYPE_MOBILE_M
            case 11:    //CLIENT_TYPE_MOBILE_BANGBANG_IOS
            case 13:    //CLIENT_TYPE_ZP_ANDROID
            case 14:    //CLIENT_TYPE_ZP_IOS
            case 15:    //CLIENT_TYPE_am_ANDROID 转转app
            case 16:    //CLIENT_TYPE_am_IOS     转转app
            case 17:    //CLIENT_TYPE_SELLER_ANDROID 卖家版app
            case 18:    //CLIENT_TYPE_SELLER_IOS     卖家版app
            case 19:    //CLIENT_TYPE_CHEAPCAR_ANDROID
            case 20:    //CLIENT_TYPE_CHEAPCAR_IOS
            case 21:    //CLIENT_TYPE_am_MOBILE_M
            case 22:    //CLIENT_TYPE_am_MOBILE_GANJI_M
            case 24:    //CLIENT_TYPE_am_WEIXIN_M
            case 25:    //CLINET_TYPE_am_WEB_M
            case 26:    //CLIENT_TYPE_am_WXGZH_M
            case 27:    //CLIENT_TYPE_HY_ANDROID
            case 28:    //CLIENT_TYPE_HY_IOS
            case 29:    //CLIENT_TYPE_WX
            case 30:    //CLIENT_TYPE_LIFE_ANDROID
            case 31:    //CLIENT_TYPE_LIFE_IOS
                terminal = E_TERMINAL_TYPE_PHONE;
                break;
            case 2:     //CLIENT_TYPE_AIR:
            case 3:     //CLIENT_TYPE_WEB:
                terminal = E_TERMINAL_TYPE_PC;
                break;
            case 1:     //CLIENT_TYPE_UNLOGIN:
            case 4:     //CLIENT_TYPE_SCF:
            case 5:     //CLIENT_TYPE_WEB_TINY:
                terminal = E_TERMINAL_TYPE_UNKNOWN;
                break;
            case 0:     //CLIENT_TYPE_ALL:
                terminal = E_TERMINAL_TYPE_ALL; //TERMINAL_TYPE_ALL
                break;
            default:
                terminal = E_TERMINAL_TYPE_UNKNOWN;
                break;
        }
        return terminal;
    }
}
