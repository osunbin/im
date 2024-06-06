package com.bin.im.server.common.msg;

public class VoiceMsg extends ImMsg{

    private String md5;
    private String url;


    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
