package com.bin.im.entry.http.session;

import com.bin.im.common.internal.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class HttpSessionManager {


    private Map<Long, HttpSession> uidSessionMap = new ConcurrentHashMap<>();

    private int timeoutInterval = 21 * 1000;

    private int interval = 10;

    public boolean saveHttpSession(long uid, long loginTime, String clientIp, int sourceType) {
        boolean exit = false;
        HttpSession httpSession = uidSessionMap.get(uid);
        if (httpSession == null) {
            httpSession = new HttpSession();
            httpSession.setUid(uid);
            httpSession.setClientIp(clientIp);
            httpSession.setLoginTime(loginTime);
            httpSession.setLastTime(loginTime);
            httpSession.setLogin(0);
            httpSession.setSourceType(sourceType);
            uidSessionMap.put(uid, httpSession);
        } else {
            exit = true;
            httpSession.setLoginTime(loginTime);
            httpSession.setLastTime(loginTime);
            httpSession.setLogin(0);
            httpSession.setSourceType(sourceType);
            httpSession.setClientIp(clientIp);
        }
        return exit;
    }


    public boolean setLoginDone(long uid) {
        HttpSession httpSession = uidSessionMap.get(uid);
        if (httpSession == null) {
            return false;
        }
        httpSession.setLogin(1);
        return true;
    }

    public boolean refreshSession(long uid) {
        HttpSession httpSession = uidSessionMap.get(uid);
        if (httpSession == null) {
            return false;
        }
        httpSession.refresh();
        return true;
    }

    public HttpSession getHttpSession(long uid) {
        HttpSession httpSession = uidSessionMap.get(uid);
        if (httpSession == null) {
            return null;
        }
        return httpSession;
    }

    public void checkAndRemoveSession(List<HttpSession> allTimeoutUid) {
        long nowTime = TimeUtils.currTimeMillis();

        Set<Long> uidKeys = uidSessionMap.keySet();

        for (long uid : uidKeys) {
            HttpSession httpSession = uidSessionMap.get(uid);
            if (nowTime > httpSession.getLastTime() + timeoutInterval) {
                allTimeoutUid.add(httpSession);
            }
        }
        for (HttpSession session : allTimeoutUid) {
            uidSessionMap.remove(session.getUid());
        }

    }


    public void removeSession(long uid) {
        uidSessionMap.remove(uid);
    }


    public void init() {
        Thread timeout = new Thread(() -> sessionTimer());
        timeout.setName("http session timeout");
        timeout.start();
    }


    private MsgQueue msgQueue;

    public void sessionTimer() {

        List<HttpSession> httpSessions = new ArrayList<>();
        while (true) {
            checkAndRemoveSession(httpSessions);
            long now = TimeUtils.currTimeMillis();

            for (HttpSession httpSession : httpSessions) {
                long uid = httpSession.getUid();

                // TODO logout


            }

            for (HttpSession httpSession : httpSessions) {
                long uid = httpSession.getUid();


                MsgInfo msgInfo = msgQueue.getMsgInfo(uid);
                while (msgInfo != null) {
                    long msgId = msgInfo.getMsgId();
                    msgQueue.ackMsg(uid,msgId);
                    // TODO unreachable 接口



                    msgInfo = msgQueue.getMsgInfo(uid);
                }


            }


            httpSessions.clear();
            TimeUtils.sleeps(interval);
        }
    }
}
