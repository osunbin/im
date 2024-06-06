package com.bin.im.server.common.type;


public class ClientDevType {
    // 登录源
    public static final int
            CLIENT_TYPE_ALL = 0,
            CLIENT_TYPE_UNLOGIN = 1,
            CLIENT_TYPE_PC = 11,        //  PC端
            CLIENT_TYPE_ANDROID = 12,       // 安卓App
            CLIENT_TYPE_IOS = 13,           //苹果App
            CLIENT_TYPE_SELLER_ANDROID = 21, //卖家版安卓App
            CLIENT_TYPE_SELLER_IOS = 22, //卖家版苹果App
            CLIENT_TYPE_PC_M = 31, //PC M端(商户侧使用)
            CLIENT_TYPE_KF_M = 32, //转转客服使用的M端
            CLIENT_TYPE_MERCHAT_M = 33; //商户员工使用的M端

}
