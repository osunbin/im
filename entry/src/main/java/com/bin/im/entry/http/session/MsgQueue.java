package com.bin.im.entry.http.session;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;

public class MsgQueue {

    // uid - (msgId - msg)
    private Map<Long, ConcurrentNavigableMap<Long, MsgInfo>> allUidMsg =
            new ConcurrentHashMap<>();

    public void saveMsg(long uid, long msgId, RelayP2PMsg relayMsg, String jsonMsg) {

        MsgInfo msgInfo = new MsgInfo();
        msgInfo.setUid(uid);
        msgInfo.setMsgId(msgId);
        msgInfo.setRelayMsg(relayMsg);
        msgInfo.setJsonMsg(jsonMsg);
        ConcurrentNavigableMap<Long, MsgInfo> uidMsg = allUidMsg.get(uid);
        if (uidMsg == null) {
            synchronized (this) {
                if (uidMsg == null) {
                    uidMsg = new ConcurrentSkipListMap<>();
                    allUidMsg.put(uid, uidMsg);
                }
            }
        }
        uidMsg.put(msgId, msgInfo);
    }


    public String getJsonMsg(long uid, long msgId) {
        Map<Long, MsgInfo> uidMsg = allUidMsg.get(uid);
        if (uidMsg == null) {
            return "";
        }
        MsgInfo msgInfo = uidMsg.get(msgId);
        if (msgInfo == null) {
            return "";
        }
        return msgInfo.getJsonMsg();
    }

    public MsgInfo getMsgInfo(long uid) {
        ConcurrentNavigableMap<Long, MsgInfo> msgInfoMap = allUidMsg.get(uid);
        if (msgInfoMap != null) {
           return msgInfoMap.pollFirstEntry().getValue();
        }
        return null;
    }


    public void ackMsg(long uid, long msgId) {
        Map<Long, MsgInfo> uidMsg = allUidMsg.get(uid);
        if (uidMsg == null) {
            return;
        }
        uidMsg.remove(msgId);
    }

}
