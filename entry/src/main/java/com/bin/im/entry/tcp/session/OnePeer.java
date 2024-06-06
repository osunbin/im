package com.bin.im.entry.tcp.session;

import com.bin.im.common.id.self.IdGenerate;
import com.bin.im.common.internal.utils.TimeUtils;
import com.bin.im.common.json.JsonHelper;
import com.bin.im.entry.config.EntryConfig;
import com.bin.im.entry.tcp.TcpHeader;
import com.bin.im.entry.tcp.encrypt.HandshakeException;
import com.bin.im.entry.tcp.encrypt.QuickConnectRequest;
import com.bin.im.entry.tcp.encrypt.ServerEncrypt;
import com.bin.im.entry.tcp.encrypt.ServerEngine;
import com.bin.im.entry.tcp.handle.EntryContext;
import com.bin.im.entry.tcp.handle.EntryHandler;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.bin.im.common.ImAttachment.CLIENT_IP;
import static com.bin.im.common.internal.utils.MDCUtil.LOG_ID;
import static com.bin.im.common.internal.utils.MDCUtil.LOG_PRE;
import static com.bin.im.entry.tcp.encrypt.Engine.HANDSHAKE_ERROR;
import static com.bin.im.entry.tcp.encrypt.Engine.QUICK_CONNECT_SESSION_NOT_FOUND;
import static com.bin.im.entry.tcp.session.OnePeerManager.clientTimeout;
import static com.bin.im.entry.tcp.session.OnePeerManager.isPhoneBySource;
import static com.bin.im.entry.tcp.session.SessionManager.DELETE_SESSION_OTHER;

public class OnePeer {

    private static final Logger logger = LoggerFactory.getLogger(OnePeer.class);

    private OnePeerManager manager;
    private long uid;
    private Channel channel;
    private int sourceType = -1;
    private String platformType;
    private String device;
    private int sessionTimeout;
    private ServerEncrypt serverEncrypt;
    private int tlsStatus;
    private boolean isAuth;
    private boolean isBlock;
    private boolean isKickout;
    private boolean isNoticeLogout;
    private String clientIp;
    private long loginTime;
    private long touchTime = TimeUtils.currTime();
    private long lastUserKeepAliveTime;
    private long lastUpReqTime;
    private long createTime = TimeUtils.currTime();
    private boolean loginReady;
    private boolean logoutAlready;
    private boolean close;
    private boolean backgroundHangs; // app 后台挂起
    private long handShakeTime;
    private long quickConnectTime;

    private EntryHandler entryHandler;
    private EntryConfig entryConfig;

    public void processApp(TcpHeader tcpHeader) {

        updateTouchTime();
        updateLastUpReqTime();


        Map<String, Object> parameterMap = tcpHeader.jsonToMap();



        long logId = IdGenerate.genLogId(uid);

        String logPre = tcpHeader.buildLogPre(logId, clientIp);


        Map<String, Object> attachments =
                entryConfig.buildEntryAttachments();
        attachments.put(LOG_ID, logId);
        attachments.put(CLIENT_IP, clientIp);
        attachments.put(LOG_PRE, logPre);

        EntryContext entryContext = new EntryContext(tcpHeader);
        entryContext.setParameterMap(parameterMap);
        entryContext.setAttachments(attachments);
        entryContext.setOnePeer(this);
        entryContext.setLogId(logId);
        entryContext.setLogPre(logPre);
        entryContext.setModule(tcpHeader.getModule());
        entryContext.setCmd(tcpHeader.getCmd());

        entryHandler.perform(entryContext);

    }



    public void checkTimeout(long timeNow) {
        long nTouchTime = getTouchTime();

        if (timeNow > nTouchTime + clientTimeout) {
            setBlock(true);
            logger.warn("uid:{} source:{} alive timeout idle:{}", uid, sourceType, (timeNow - nTouchTime));
            onTimeout();
        } else if (isLogoutAlready()) {
            logger.info("uid:{} source:{} alive logout idle:{}", uid, sourceType, (timeNow - nTouchTime));
        } else if (!isLoginReady()) {
            logger.info("uid:{} source:{} alive login idle:{}", uid, sourceType, (timeNow - nTouchTime));

        } else {
            logger.info("uid:{} source:{} alive online idle:{}", uid, sourceType, (timeNow - nTouchTime));
        }
    }


    public void onTimeout() {
        onHangs();
        getManager().unRegistryPnePeer(channel);
    }

    public void onBackgroundHangs() {
        onTimeout();
    }

    public void onHangs() {
        if (isLoginReady() && isAuth()) {
            if (isPhone() && sessionTimeout > 0) {
                getManager().saveSession(this);
                logger.info("save session uid:{} sourceType:{}", uid, sourceType);
            } else {
                 onLogout();
            }
        }
        setClose(true);
    }

    public void onLogout() {
        if (isKickout() || isLogoutAlready()) {
            return;
        }

        setLogoutAlready(true);


        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("sourceType",sourceType);
        parameterMap.put("reqUid",uid);
        parameterMap.put("logoutTime",lastUpReqTime);

        long logId = IdGenerate.genLogId(uid);

        String module = "user";
        String cmd =  "logout";
        String logPre = " logId: " + logId + "  clientIp: " + clientIp +
                " module: " + module + " cmd " + cmd;



        Map<String, Object> attachments =
                entryConfig.buildEntryAttachments();
        attachments.put(LOG_ID, logId);
        attachments.put(CLIENT_IP, clientIp);
        attachments.put(LOG_PRE, logPre);

        EntryContext entryContext = new EntryContext();
        entryContext.setParameterMap(parameterMap);
        entryContext.setAttachments(attachments);
        entryContext.setOnePeer(this);
        entryContext.setLogId(logId);
        entryContext.setLogPre(logPre);
        entryContext.setModule(module);
        entryContext.setCmd(cmd);

        entryHandler.perform(entryContext);

    }


    // TODO
    public void kickOut(int reason, int oldSourceType) {
        setKickout(true);
        setBlock(true);
        setAuth(false);
        Map<String,Object> param = new HashMap<>();
        param.put("module","user");
        param.put("cmd","kickout");
        param.put("code",reason);
        param.put("fromSourceType",oldSourceType);

        TcpHeader tcpHeader = new TcpHeader("user", "kickout");
        tcpHeader.setCode(reason);
        tcpHeader.setVersion("json");


    }

    // TODO
    public void pushApp(EntryContext context) {

        byte[] appData = null;
        byte[] encrypt = serverEncrypt.encrypt(appData);// 加密
        TcpHeader tcpHeader = context.getTcpHeader();

        tcpHeader.handshakeResp(channel, encrypt);

    }


    public boolean isPhone() {
        return isPhoneBySource(sourceType);
    }


    public void removeIDOnePeerMap() {
        getManager().removeIDOnePeerMap(this);
    }

    public void deleteSession(int flag) {
        getManager().getSessionManager().deleteSession(uid, sourceType, flag);
    }

    public byte[] createServerEngine() {
        serverEncrypt = new ServerEngine();
        byte[] keys = serverEncrypt.initial();
        setServerEncrypt(serverEncrypt);
        setHandShakeTime(TimeUtils.currTime());
        return keys;
    }

    public byte[] clientHello(byte[] hello) {
        return serverEncrypt.clientHello(hello);
    }
    // TODO
    public boolean quickConnect(byte[] keys) {
        long time = TimeUtils.currTime();
        setQuickConnectTime(time);
        // 没有登录 但 验证通过
        if (!isLoginReady() && isAuth()) {
            logger.error("quickconnect wrong uid:{} autu:{} login:{}", uid, isAuth(), isLoginReady());

            return false;
        }
        QuickConnectRequest request = null;
        try {
            request =
                    JsonHelper.jsonByteToObject(keys, QuickConnectRequest.class);
        } catch (IOException e) {
            logger.error("parse quick connect fail", e);
            throw new HandshakeException("parse quick connect fail ", HANDSHAKE_ERROR);
        }
        long reqUid = request.getUid();
        int reqSourceType = request.getSourceType();
        byte[] encryptKey = request.getEncryptKey();

        if (!isPhoneBySource(reqSourceType)) {
            logger.error("QuickConnect invalid reqUid:{} reqSourceType:{} ", reqUid, reqSourceType);

            return false;
        }

        TcpSession session = getManager().
                getSessionManager().getSession(reqUid);


        if (session == null) {
            throw new HandshakeException("quick connect verify fail ", QUICK_CONNECT_SESSION_NOT_FOUND);
        }
        int sessionSource = session.getSourceType();
        if (sessionSource != reqSourceType) {
            logger.error("session sourceType not match reqUid:{} reqSourceType:{}", reqUid, reqSourceType);
            throw new HandshakeException("quick connect session sourceType not match  fail ", QUICK_CONNECT_SESSION_NOT_FOUND);
        }
        ServerEncrypt sessionEncrypt = session.getServerEncrypt();
        if (sessionEncrypt == null) {
            logger.error("quickConnect Server encrypt is null  reqUid:{} reqSourceType:{}", reqUid, reqSourceType);
            throw new HandshakeException("quick connect verify fail ", HANDSHAKE_ERROR);

        }
        sessionEncrypt.verify(encryptKey);

        getManager().
                getSessionManager().
                pauseSessionTimeout(uid, sourceType);
        logger.info("quickconnect reqUid:{} reqSourceType:{}", reqUid, reqSourceType);




        getManager().
                getSessionManager().deleteSession(reqUid,reqSourceType,DELETE_SESSION_OTHER);

        OnePeer oldOnePeer = getManager().getOnePeerFromUid(reqUid, reqSourceType);
        if (oldOnePeer != null) {
            logger.info("Login OnSame Kickout Olduser Logout old sourceType:{}",oldOnePeer.getSourceType());
            oldOnePeer.kickOut(1,sourceType);


        }


        getManager().addIDOnePeerMap(reqUid,this);
        // TODO 白名单 黑名单
        getManager().getSessionManager().
                pauseSessionTimeout(reqUid,reqSourceType);

        setBackgroundHangs(false);
        setAuth(true);
        setLoginReady(true);

        return true;
    }



    public byte[] decrypt(byte[] content) {
        return serverEncrypt.decrypt(content);
    }

    private void updateTouchTime() {
        touchTime = TimeUtils.currTime();
    }

    private void updateLastUpReqTime() {
        lastUpReqTime = TimeUtils.currTimeMillis();
    }

    public OnePeerManager getManager() {
        return manager;
    }

    public void bindManager(OnePeerManager manager) {
        this.manager = manager;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getPlatformType() {
        return platformType;
    }

    public void setPlatformType(String platformType) {
        this.platformType = platformType;
    }

    public int getTlsStatus() {
        return tlsStatus;
    }

    public void setTlsStatus(int tlsStatus) {
        this.tlsStatus = tlsStatus;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public ServerEncrypt getServerEncrypt() {
        return serverEncrypt;
    }

    public void setServerEncrypt(ServerEncrypt serverEncrypt) {
        this.serverEncrypt = serverEncrypt;
    }

    public boolean isAuth() {
        return isAuth;
    }

    public void setAuth(boolean auth) {
        isAuth = auth;
    }

    public boolean isBlock() {
        return isBlock;
    }

    public void setBlock(boolean block) {
        isBlock = block;
    }

    public boolean isKickout() {
        return isKickout;
    }

    public void setKickout(boolean kickout) {
        isKickout = kickout;
    }


    public boolean isNoticeLogout() {
        return isNoticeLogout;
    }

    public void setNoticeLogout(boolean noticeLogout) {
        isNoticeLogout = noticeLogout;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(long loginTime) {
        this.loginTime = loginTime;
    }

    public long getTouchTime() {
        return touchTime;
    }

    public void setTouchTime(long touchTime) {
        this.touchTime = touchTime;
    }

    public long getLastUserKeepAliveTime() {
        return lastUserKeepAliveTime;
    }

    public void setLastUserKeepAliveTime(long lastUserKeepAliveTime) {
        this.lastUserKeepAliveTime = lastUserKeepAliveTime;
    }

    public long getLastUpReqTime() {
        return lastUpReqTime;
    }

    public void setLastUpReqTime(long lastUpReqTime) {
        this.lastUpReqTime = lastUpReqTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public boolean isLoginReady() {
        return loginReady;
    }

    public void setLoginReady(boolean loginReady) {
        this.loginReady = loginReady;
    }

    public boolean isLogoutAlready() {
        return logoutAlready;
    }

    public void setLogoutAlready(boolean logoutAlready) {
        this.logoutAlready = logoutAlready;
    }


    public boolean isClose() {
        return close;
    }

    public void setClose(boolean close) {
        this.close = close;
    }

    public boolean isBackgroundHangs() {
        return backgroundHangs;
    }

    public void setBackgroundHangs(boolean backgroundHangs) {
        this.backgroundHangs = backgroundHangs;
    }

    public long getHandShakeTime() {
        return handShakeTime;
    }

    public void setHandShakeTime(long handShakeTime) {
        this.handShakeTime = handShakeTime;
    }

    public long getQuickConnectTime() {
        return quickConnectTime;
    }

    public void setQuickConnectTime(long quickConnectTime) {
        this.quickConnectTime = quickConnectTime;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OnePeer onePeer = (OnePeer) o;
        return uid == onePeer.uid && sourceType == onePeer.sourceType && Objects.equals(channel, onePeer.channel) && Objects.equals(device, onePeer.device) && Objects.equals(serverEncrypt, onePeer.serverEncrypt) && Objects.equals(clientIp, onePeer.clientIp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, channel, sourceType, device, serverEncrypt, clientIp);
    }
}
