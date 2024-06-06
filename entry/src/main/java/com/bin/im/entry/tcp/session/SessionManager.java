package com.bin.im.entry.tcp.session;

import com.bin.im.common.id.self.IdGenerate;
import com.bin.im.entry.config.EntryConfig;
import com.bin.im.entry.tcp.encrypt.ServerEncrypt;
import com.bin.im.entry.tcp.handle.EntryContext;
import com.bin.im.entry.tcp.handle.EntryHandler;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.bin.im.common.internal.utils.MDCUtil.LOG_ID;
import static com.bin.im.common.internal.utils.MDCUtil.LOG_PRE;

/**
 * 连接检测  10s
 */
public class SessionManager {
    private static final Logger logger = LoggerFactory.getLogger(SessionManager.class);

    private static final int BUCKET_NUM_MAX = 300;

    private int checkInterval = 2 * 1000;

    private long lastCheckTime = System.currentTimeMillis();

    private List<List<Long>> hashTimers = new ArrayList<>(BUCKET_NUM_MAX);

    private Map<Long, TcpSession> sessionMap = new ConcurrentHashMap<>();


    public TcpSession getSession(long uid) {
        return sessionMap.get(uid);
    }


    /**
     * sessionTimeout  43200 * 1000 - 12小时   1500 * 1000 -25分钟
     * 离线 后 保存session
     */
    public void saveSession(long uid, int sourceType, ServerEncrypt serverEncrypt,
                            long loginTime, long sessionTimeout,
                            Channel channel) {
        TcpSession tcpSession = sessionMap.get(uid);
        if (tcpSession == null) {
            tcpSession = new TcpSession();
            tcpSession.setUid(uid);
            tcpSession.setServerEncrypt(serverEncrypt);
            tcpSession.setSourceType(sourceType);
            tcpSession.setLoginTime(loginTime);
            tcpSession.setSessionTimeout(sessionTimeout);
            tcpSession.setChannel(channel);
        } else {
            int oldSourceType = tcpSession.getSourceType();
            if (oldSourceType != sourceType) {
                return;
            }
            long oldExpireTime = tcpSession.getExpireTime();
            tcpSession.updateExpireTime();
            if (oldExpireTime > 0)
                removeTimer(oldExpireTime, uid);

        }
        addTimer(tcpSession.getExpireTime(), uid);

    }

    /**
     * 由于会话超时和登录/快速连接过程是在不同的线程上处理的，因此登录/快速链接
     * 过程中会话超时可能会导致状态不一致。
     * 例如，逻辑回复loginReady->会话超时并将注销发送到逻辑->loginReady resp发送到客户端
     */
    public void pauseSessionTimeout(long uid, int sourceType) {
        long timeNow = System.currentTimeMillis();

        long expireTime = 0;
        long expireTimeNew = 0;
        TcpSession tcpSession = sessionMap.get(uid);
        if (tcpSession != null) {
            int oldSourceType = tcpSession.getSourceType();
            if (oldSourceType != sourceType) {
                return;
            }
            expireTimeNew = expireTime = tcpSession.getExpireTime();
            if ((0 != expireTime) && (expireTime <= timeNow + checkInterval)) {
                expireTimeNew = timeNow + checkInterval;
                tcpSession.setExpireTime(expireTimeNew);
                int slotOld = (int) (expireTime % BUCKET_NUM_MAX);
                int slotNew = (int) (expireTimeNew % BUCKET_NUM_MAX);
                if (slotOld != slotNew) {
                    hashTimers.get(slotOld).remove(uid);
                    hashTimers.get(slotNew).add(uid);
                }
            }
        }
    }

    /**
     * 快速 重连 失败
     * delete the timer but keep the session
     */
    public void restorePeer(long uid, int source) {

        TcpSession tcpSession = sessionMap.get(uid);
        if (tcpSession != null) {
            int sourceType = tcpSession.getSourceType();
            if (sourceType != source) {
                return;
            }
            tcpSession.setServerEncrypt(null); // TODO
            long expireTime = tcpSession.getExpireTime();
            tcpSession.resetExpireTime();
            if (expireTime > 0) {
                removeTimer(expireTime, uid);
            }
        }
    }

    public static final int DELETE_SESSION_ALL = 0,
            DELETE_SESSION_THIS = 1,
            DELETE_SESSION_OTHER = 2;


    public void deleteSession(long uid, int sourceType, int flag) {
        long expireTime = 0;
        TcpSession tcpSession = sessionMap.get(uid);
        if (tcpSession != null) {
            expireTime = tcpSession.getExpireTime();
            int sourceTypeSaved = tcpSession.getSourceType();
            if ((DELETE_SESSION_THIS == flag) && (sourceTypeSaved != sourceType)) {
                return;
            } else if ((DELETE_SESSION_OTHER == flag) && (sourceTypeSaved == sourceType)) {
                return;
            }
            sessionMap.remove(uid);
            if (expireTime > 0) {
                removeTimer(expireTime, uid);
            }
        }

    }

    public void removeTimer(long time, long uid) {
        int slot = (int) (time % BUCKET_NUM_MAX);
        hashTimers.get(slot).remove(uid);

    }

    public void addTimer(long time, long uid) {
        int slot = (int) (time % BUCKET_NUM_MAX);
        hashTimers.get(slot).add(uid);
    }

    public SessionManager() {

    }

    private void init() {
        Runnable runnable = () -> processTimer();
        Thread check = new Thread(runnable);
        check.setName("user session check");
        check.start();
    }


    public void processTimer() {
        long timeNow;
        long timeToCheck;
        int slotToCheck;

        while (true) {
            sleepInterval();
            timeNow = System.currentTimeMillis();
            timeToCheck = lastCheckTime + 1;
            for (; timeToCheck < timeNow; ++timeToCheck) {
                slotToCheck = (int) (timeToCheck % BUCKET_NUM_MAX);
                List<Long> keyIt = hashTimers.get(slotToCheck);

                for (long uid : keyIt) {
                    TcpSession tcpSession = sessionMap.get(uid);
                    if (tcpSession != null) {
                        long expireTime = tcpSession.getExpireTime();
                        int sourceType = tcpSession.getSourceType();
                        if (timeNow >= expireTime) {

                            // TODO
                            sendLogout(uid,sourceType,tcpSession.getLoginTime());
                            keyIt.remove(uid);
                            sessionMap.remove(uid);
                        }
                    }
                }


                lastCheckTime = timeToCheck;
            }
        }
    }


    private EntryConfig entryConfig;
    private EntryHandler entryHandler;

    public void sendLogout(long uid, int sourceType, long time) {
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("sourceType", sourceType);
        parameterMap.put("reqUid", uid);
        parameterMap.put("logoutTime", time);

        long logId = IdGenerate.genLogId(uid);

        String module = "user";
        String cmd = "logout";
        String logPre = " logId: " + logId +
                " module: " + module + " cmd " + cmd;
        Map<String, Object> attachments =
                entryConfig.buildEntryAttachments();
        attachments.put(LOG_ID, logId);
        attachments.put(LOG_PRE, logPre);

        EntryContext entryContext = new EntryContext();
        entryContext.setParameterMap(parameterMap);
        entryContext.setAttachments(attachments);


        entryContext.setLogId(logId);
        entryContext.setLogPre(logPre);
        entryContext.setModule(module);
        entryContext.setCmd(cmd);

        entryHandler.perform(entryContext);

    }


    private void sleepInterval() {
        try {
            Thread.sleep(checkInterval);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
