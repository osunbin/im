package com.bin.im.entry.tcp.handle;

import com.bin.im.common.internal.utils.TimeUtils;
import com.bin.im.entry.tcp.session.OnePeer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeepAliveHandler extends EntryHandler{

    private static Logger logger = LoggerFactory.getLogger(KeepAliveHandler.class);

    private static final int UserKeepAliveThreshold = 300;

    public String key() {
        return "user_keepAlive";
    }

    public void perform(EntryContext context) {
        long upTime = TimeUtils.currTime();
        OnePeer onePeer = context.getOnePeer();
        long uid = onePeer.getUid();
        if (!onePeer.isAuth() && !onePeer.isLoginReady()) { // 没登陆
            logger.warn("KeepAlive WithoutLogin ignored uid:{} currTime:{}",uid,upTime);
            return;
        }


        if (upTime < onePeer.getLastUserKeepAliveTime() + UserKeepAliveThreshold) {
            logger.info("ignore UserKeepAlive uid:{} currTime:{} lastKeepAliveTime:{}",uid,upTime,onePeer.getLastUserKeepAliveTime());
            return;
        }



        context.invoke();


        onePeer.setLastUserKeepAliveTime(upTime);
        logger.info("UserKeepAlive uid:{} lastKeepAliveTime:{}",uid,upTime);
    }
}
