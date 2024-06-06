package com.bin.im.server.core.cache;

import com.bin.im.common.internal.utils.TimeUtils;
import com.bin.im.server.domain.SendMsgNotify;
import com.bin.im.server.cache.CacheInternal;
import com.bin.im.server.core.BaseHandler;
import com.bin.im.server.core.MsgNotifyInfo;
import com.bin.im.server.domain.KickoutUserNotify;
import com.bin.im.server.domain.LoginInfo;
import com.bin.im.server.domain.MsgAckInfo;
import com.bin.im.server.event.Message;
import com.bin.im.server.event.model.OfflineMsgData;
import com.bin.im.server.repositories.sql.contacts.UserContacts;
import com.bin.im.server.spi.annotation.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;


import static com.bin.im.server.domain.KickoutUserNotify.KICKOUT_REASON_STATUS_ERROR;
@Service
public class MsgTimeoutManager extends BaseHandler {

    public static final String SERVICE_NAME = "MsgTimeoutManager";


    private int timeoutCount = 1;

    protected int ticktackVal = 1000;


    private volatile boolean holdLock = true;
    private volatile boolean stop = false;


    @Override
    public String getServiceName() {
        return SERVICE_NAME;
    }

    public void init() {

        Thread lockThread = new Thread(() -> keepLock(), "KeepLockThread");
        lockThread.setDaemon(true);
        lockThread.start();

        Thread timeoutThread = new Thread(() -> timeout(), "msgTimeoutThread");
        timeoutThread.setDaemon(true);
        timeoutThread.start();
    }


    private void timeout() {
        int time = ticktackVal;
        while (true) {
            if (holdLock) {


                TimeUtils.sleepMillis(time);

                long t1 = System.currentTimeMillis();
                if (!stop) {
                    invoke();
                    long t2 = System.currentTimeMillis();
                    int tc = (int) (t2 - t1);
                    if (tc < ticktackVal) {
                        time = ticktackVal - tc;
                    }
                }
            }
        }
    }

    private void invoke() {
        List<MsgAckInfo> timeOutMsgAcks = getTimeWheel().getTimeOutMsgAckInfo();
        for (MsgAckInfo ackInfo : timeOutMsgAcks) {
            int timeout = ackInfo.getTimeout();
            if (timeout == timeoutCount) {
                discardMsg(ackInfo);
            } else {
                resendMsg(ackInfo);
            }
        }
    }

    private void keepLock() {
        // 分布式抢锁
        String lockKey = "imMsgTimeout";
        String currName = Thread.currentThread().getName();
        String lock = "";
        try {
            try (CacheInternal cache = getCache()) {
                lock = cache.setex(lockKey, currName, 60);
            }
            if ("OK".equals(lock)) {
                holdLock = true;
            } else {
                holdLock = false;
            }
            try {
                TimeUnit.SECONDS.sleep(60);
            } catch (InterruptedException e) {
            }
        } finally {
            try (CacheInternal cache = getCache()) {
                String value = cache.get(lockKey);
                if (cache.exists(lockKey) && currName.equals(value)) {
                    cache.del(lockKey);
                }
            }
        }
    }


    private CacheInternal getCache() {
        return getImEngine().getCacheManager().getCache();
    }

    private TimeWheelManager getTimeWheel() {
        return getImEngine().getService(TimeWheelManager.SERVICE_NAME);
    }

    // timeout = 0
    public void resendMsg(MsgAckInfo ackInfo) {
        long toUid = ackInfo.getToUid();

        LoginInfo loginInfo = getUserInfoRouter().fetchUserLoginInfo(toUid);
        if (loginInfo == null) {
            return;
        }
        MsgNotifyInfo msgNotifyInfo =
                MsgNotifyInfo.create(toUid, loginInfo.getLoginESIp(), loginInfo.getLoginESPort());


        SendMsgNotify sendMsgNotify = new SendMsgNotify();
        sendMsgNotify.setFromUid(ackInfo.getFromUid());
        sendMsgNotify.setToUid(ackInfo.getToUid());
        sendMsgNotify.setTime(ackInfo.getTimestamp());
        sendMsgNotify.setMsgId(ackInfo.getMsgId());
        sendMsgNotify.setClientMsgId(ackInfo.getClientMsgId());
        sendMsgNotify.setMsgContent(ackInfo.getMsgContent());
        sendMsgNotify.setMsgType(ackInfo.getMsgType());
        sendMsgNotify.setMsgId(ackInfo.getMsgId());

        getImEngine().notifyMsg(msgNotifyInfo, sendMsgNotify);


        ackInfo.setTimeout(ackInfo.getTimeout() + 1);
        TimeWheelManager timeWheelManager =
                  getService(TimeWheelManager.SERVICE_NAME);
        timeWheelManager.addMsg(ackInfo);
    }


    // timeout = 1
    public void discardMsg(MsgAckInfo msgAckInfo) {
        long currTime = TimeUtils.currTimeMillis();
        OfflineMsgData offlineMsg = new OfflineMsgData();
        offlineMsg.setFromUid(msgAckInfo.getFromUid());
        offlineMsg.setToUid(msgAckInfo.getToUid());
        offlineMsg.setFromSource(msgAckInfo.getSourceType());
        offlineMsg.setMsgType(msgAckInfo.getMsgType());
        offlineMsg.setMsgContent(msgAckInfo.getMsgContent());


        //   IM的离线逻辑
        publishOfflineEvent(Message.of(offlineMsg));


        long toUid = msgAckInfo.getToUid();


        UserContacts userContacts = new UserContacts();
        userContacts.setUidA(toUid);
        userContacts.setUidB(msgAckInfo.getFromUid());
        userContacts.setUnreadCount(1);
        userContacts.setLastTime(currTime);
        imDas().updateOfflineSingleContactReceive(userContacts);

        LoginInfo loginInfo = getUserInfoRouter().fetchUserLoginInfo(toUid);
        if (loginInfo == null) {
            return;
        }

        KickoutUserNotify kickoutUserNotify = new KickoutUserNotify();
        kickoutUserNotify.setReason(KICKOUT_REASON_STATUS_ERROR);
        kickoutUserNotify.setSourceType(loginInfo.getCliType());
        kickoutUserNotify.setLoginTime(loginInfo.getLoginTime());
        kickoutUserNotify.setUid(toUid);

        MsgNotifyInfo msgNotifyInfo =
                MsgNotifyInfo.create(toUid, loginInfo.getLoginESIp(), loginInfo.getLoginESPort());


        getImEngine().kickout(msgNotifyInfo, kickoutUserNotify);
        getUserInfoRouter().delUserLoginInfo(toUid);

    }


}
