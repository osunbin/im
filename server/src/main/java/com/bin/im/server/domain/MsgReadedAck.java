package com.bin.im.server.domain;

import java.util.List;

public class MsgReadedAck {
    private List<ContactsReadedInfo> contactsReadedInfos;

    public MsgReadedAck(List<ContactsReadedInfo> contactsReadedInfos) {
        this.contactsReadedInfos = contactsReadedInfos;
    }

    public List<ContactsReadedInfo> getContactsReadedInfos() {
        return contactsReadedInfos;
    }


}
