package com.bin.im.entry.tcp.handle;

import com.bin.im.entry.tcp.session.OnePeer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BackgroundHangsHandler extends EntryHandler{

    private static Logger logger = LoggerFactory.getLogger(BackgroundHangsHandler.class);


    public String key() {
        return "user_backgroundHangs";
    }

    public void perform(EntryContext context) {
        OnePeer onePeer = context.getOnePeer();
        long uid = onePeer.getUid();
        if (onePeer.isBackgroundHangs()) {
            logger.info("app already background hangs  uid:{}",uid);
            return;
        }
        onePeer.setBackgroundHangs(true);

        onePeer.onBackgroundHangs();
        logger.info("app background hangs  uid:{}",uid);
    }
}
