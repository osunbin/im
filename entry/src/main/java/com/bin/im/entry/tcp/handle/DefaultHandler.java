package com.bin.im.entry.tcp.handle;

import com.bin.im.entry.tcp.session.OnePeer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DefaultHandler extends EntryHandler{
    private static Logger logger = LoggerFactory.getLogger(DefaultHandler.class);



    public void perform(EntryContext context) {
        OnePeer onePeer = context.getOnePeer();
        if (!onePeer.isAuth() && !onePeer.isLoginReady()) {
            logger.warn("recve data without login");
        }

        context.invoke();

        logger.info("user im data uid:{}",onePeer.getUid());
    }

}
