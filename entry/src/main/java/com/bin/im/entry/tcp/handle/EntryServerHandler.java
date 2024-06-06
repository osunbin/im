package com.bin.im.entry.tcp.handle;

import com.bin.im.entry.common.Converter;
import com.bin.im.entry.tcp.session.OnePeer;
import com.bin.im.entry.tcp.session.OnePeerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static com.bin.im.entry.tcp.session.OnePeerManager.E_TERMINAL_TYPE_PHONE;
import static com.bin.im.entry.tcp.session.OnePeerManager.sourceToTerminal;
import static com.bin.im.entry.tcp.session.SessionManager.DELETE_SESSION_THIS;
// todo  dubbo
public class EntryServerHandler {
    private Logger logger = LoggerFactory.getLogger(EntryServerHandler.class);


    private static final int KICKOUT_REASON_RELOGIN = 1,
            KICKOUT_REASON_STATUS_ERROR = 2, // 关闭连接
            KICKOUT_REASON_PC_TO_MOBILE = 3;

    private OnePeerManager onePeerManager;

    public void kicout(Map<String,Object> content) {
        Object sourceTypeObj = content.get("sourceType");
        Object uidObj = content.get("uid");
        Object reasonObj = content.get("reason");
        int sourceType = Converter.coverInt(sourceTypeObj);
        long uid = Converter.coverLong(uidObj);

        int reason = Converter.coverInt(reasonObj);

        OnePeer onePeer = onePeerManager.getOnePeerFromUid(uid, sourceType);
        if (onePeer == null) {
            logger.error("kickout not find onePeer uid:{} sourceType:{}",uid,sourceType);
        }


        int terminal = sourceToTerminal(sourceType);
        if (E_TERMINAL_TYPE_PHONE == terminal) {
            onePeerManager.getSessionManager()
                  .deleteSession(uid,sourceType,DELETE_SESSION_THIS);
        }

        if (onePeer != null && KICKOUT_REASON_STATUS_ERROR != reason) {
            onePeer.setKickout(true);
            onePeer.setBlock(true);
            onePeer.setAuth(false);

            EntryContext context = new EntryContext();
            context.setParameterMap(content);

            onePeer.pushApp(context); //TODO

            onePeerManager.removeIDOnePeerMap(onePeer);

        } else if (onePeer != null) { // KICKOUT_REASON_STATUS_ERROR
            onePeer.setBlock(true);
            onePeer.onTimeout();
        }
    }

    private EntryHandler entryHandler;

    public void handle(Map<String,Object> content) {
        Object sourceTypeObj = content.get("sourceType");
        Object uidObj = content.get("uid");

        int sourceType = Converter.coverInt(sourceTypeObj);
        long toUid = Converter.coverLong(uidObj);

        EntryContext context = new EntryContext();
        context.setParameterMap(content);
        OnePeer toOnePeer = onePeerManager.getOnePeerFromUid(toUid, sourceType);
        boolean send = false;
        if (toOnePeer != null) {

            try {
                toOnePeer.pushApp(context); //TODO
                send = true;
            }catch (Exception e) {
                logger.error("notice msg error uid:{}",toUid,e);
            }
        }

        if (!send) { // TODO  发送失败  不在线
            context.setModule("msg");
            context.setCmd("unreachable");
            entryHandler.perform(context);
        }
    }
}
