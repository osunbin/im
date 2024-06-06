package com.bin.im.server.common.msg;

public class ImMsg {

    //个人聊天消息类型
    public static final int CHAT_MSG_TYPE_UNKNOWN    = 0, //未知消息
            CHAT_MSG_TYPE_TEXT       = 1, //文本消息
            CHAT_MSG_TYPE_FACE       = 2, //表情消息
            CHAT_MSG_TYPE_URL        = 3, //http地址消息
            CHAT_MSG_TYPE_CAPIMG     = 4, //图片消息
            CHAT_MSG_TYPE_VOICE      = 5, //语音消息
            CHAT_MSG_TYPE_VIDEO      = 6, //视频消息
            CHAT_MSG_TYPE_GOODS      = 7, //宝贝分享消息
            CHAT_MSG_TYPE_LOCATION   = 8; //位置消息

    private int contentType;

    private int quickhint; // 是否是快捷

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }


    public int getQuickhint() {
        return quickhint;
    }

    public void setQuickhint(int quickhint) {
        this.quickhint = quickhint;
    }
}
