package com.bin.im.entry.tcp.handle;

import com.bin.im.entry.tcp.session.OnePeer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.bin.im.entry.tcp.session.SessionManager.DELETE_SESSION_THIS;

public class LogoutHandler  extends EntryHandler{

    private static Logger logger = LoggerFactory.getLogger(LogoutHandler.class);

    public String key() {
        return "user_logout";
    }


    public void perform(EntryContext context) {
        OnePeer onePeer = context.getOnePeer();
        if (onePeer != null) {
            if (onePeer.isLoginReady()) {
                logger.warn("logout before need login");
            }
            if (onePeer.isNoticeLogout() || onePeer.isLogoutAlready()) {
                logger.warn("already logout , repeat");
            }
            onePeer.setLoginReady(true);

            context.invoke();

            onePeer.setNoticeLogout(true);

            // 通知客户端
            onePeer.pushApp(context);


            onePeer.setAuth(false);

            onePeer.setBlock(true);

            if (onePeer.isPhone()) {
                onePeer.deleteSession(DELETE_SESSION_THIS);
            }
            onePeer.removeIDOnePeerMap();
        } else {
            context.invoke();
        }

    }



}
