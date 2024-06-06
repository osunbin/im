package com.bin.im.entry.http.handler;

import com.bin.im.entry.http.session.HttpConnManager;
import com.bin.im.entry.http.session.HttpSession;
import com.bin.im.entry.http.session.HttpSessionManager;
import com.bin.im.entry.http.session.MsgInfo;
import com.bin.im.entry.http.session.MsgQueue;
import com.bin.im.entry.http.session.PickManager;
import com.bin.im.entry.tcp.handle.EntryContext;
import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class HttpEntryHandler {

    public static final Map<Long,EntryContext> seqMap = new ConcurrentHashMap<>();
    // 正常请求
    protected HttpConnManager httpConnManager;
    // session
    protected HttpSessionManager httpSessionManager;

    protected MsgQueue msgQueue;
    // http 长轮询
    protected PickManager pickManager;

    private static final AtomicLong seqs = new AtomicLong();

    public String key() {
        return "";
    }
    // server notify
    public void perform() {
        String strJsonResp = "131";
        long uid = 1;
        long msgId = 10;

        boolean sendPick = pickManager.trySendPick(uid, strJsonResp);
        if (!sendPick) {

            msgQueue.saveMsg(uid, msgId, null, strJsonResp);
        }
    }
    // server 通知
    public void kickout() {
        long uid = 1;
        HttpSession httpSession = httpSessionManager.getHttpSession(uid);
        if (httpSession == null) {
            return;
        }
        String kickout = "kickout";
        boolean sendPick = pickManager.trySendPick(uid, kickout);


        httpSessionManager.removeSession(uid);

    }


    public void login(long seq, Channel channel) {

        httpConnManager.saveConn(seq,channel);




        httpSessionManager.saveHttpSession(
                1,2,"127.0.0.1",0);

        httpConnManager.removeConn(seq,channel);

        httpSessionManager.setLoginDone(1);
    }


    public void logout() {
        long uid = 1L;
        HttpSession httpSession = httpSessionManager.getHttpSession(uid);
        if (httpSession == null) {
            // TODO fail
            return;
        }

        // TODO send server

        httpSessionManager.removeSession(uid);
    }



    // TODO  http 长轮询
    public void userPick(Channel channel) {
        long uid = 1L;
        // 每次都  refresh
        HttpSession httpSession = httpSessionManager.getHttpSession(uid);
        if (httpSession == null) {
            // TODO fail
            return;
        }
        httpSession.refresh();


        MsgInfo msgInfo = msgQueue.getMsgInfo(uid);
        if (msgInfo != null) {
            String jsonMsg = msgInfo.getJsonMsg();
            channel.writeAndFlush(jsonMsg);

            msgQueue.ackMsg(uid,msgInfo.getMsgId());
            return; // 直接结束
        }
        // http 长轮询 hole住
        pickManager.savePick(uid,channel);

    }
}
