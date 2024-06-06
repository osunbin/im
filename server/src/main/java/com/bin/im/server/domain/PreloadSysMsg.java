package com.bin.im.server.domain;

import java.util.List;

public class PreloadSysMsg {
   private long reqUid;
   private List<ContactPreloadSysMsg> contactPreloadSysMsgs;

    public long getReqUid() {
        return reqUid;
    }

    public void setReqUid(long reqUid) {
        this.reqUid = reqUid;
    }

    public List<ContactPreloadSysMsg> getContactPreloadSysMsgs() {
        return contactPreloadSysMsgs;
    }

    public void setContactPreloadSysMsgs(List<ContactPreloadSysMsg> contactPreloadSysMsgs) {
        this.contactPreloadSysMsgs = contactPreloadSysMsgs;
    }
}
