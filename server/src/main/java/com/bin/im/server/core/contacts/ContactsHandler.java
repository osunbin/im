package com.bin.im.server.core.contacts;


import com.bin.im.server.core.BaseHandler;

import com.bin.im.server.spi.annotation.Service;

/**
 *  根据时间戳 查看 好友列表是否有新增或删除,然后拉取
 */
@Service
public class ContactsHandler extends BaseHandler {


    public static final String SERVICE_NAME = "ContactsHandler";


    @Override
    public String getServiceName() {
        return SERVICE_NAME;
    }

    public void fetchContacts() {


    }


}
