package com.bin.im.entry.tcp.handle;

import com.bin.im.entry.common.Converter;
import com.bin.im.entry.tcp.session.OnePeer;
import com.bin.im.entry.tcp.session.OnePeerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static com.bin.im.entry.tcp.session.SessionManager.DELETE_SESSION_OTHER;

public class LoginHandler extends EntryHandler{

    private static Logger logger = LoggerFactory.getLogger(KeepAliveHandler.class);


    public String key() {
        return "user_login";
    }

    public void perform(EntryContext context) {
        OnePeer onePeer = context.getOnePeer();
        long uid = onePeer.getUid();
        if (onePeer.isLoginReady()) {
            logger.warn("user loginReady uid:{}",uid);
            return;
        }


        Map<String, Object> parameterMap = context.getParameterMap();

        Object sessionTimeout = parameterMap.get("sessionTimeout");
        int timeout = Converter.coverInt(sessionTimeout);
        if (onePeer.isPhone() && timeout > 0) {
            onePeer.setSessionTimeout(timeout);
        }



        context.setAfter((c) -> invokeAfter(c));

        context.invoke();
        logger.info("User Login uid:{}",uid);



    }

    public void invokeAfter(EntryContext context) {
        OnePeer onePeer = context.getOnePeer();
        long uid = onePeer.getUid();
        int sourceType = onePeer.getSourceType();

        OnePeerManager manager = onePeer.getManager();

        // TODO 登录成功 清除其他的 source 的session
        if (onePeer.isPhone()) {
            manager.getSessionManager().
                    deleteSession(uid,sourceType,DELETE_SESSION_OTHER);
        }

        OnePeer pAnother = manager.getOnePeerFromUid(uid, onePeer.getSourceType());
        if (pAnother != null) {
            logger.info("Login OnSame Kickout Olduser Logout old sourceType:{}",pAnother.getSourceType());
            pAnother.kickOut(1,sourceType);
            // TODO  pAnother 清除

        }
        manager.addIDOnePeerMap(uid,onePeer);
        // TODO 白名单 黑名单
        manager.getSessionManager().pauseSessionTimeout(uid,sourceType);
    }

}
