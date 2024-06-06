package com.bin.im.server.common.msg;

public class CapimgMsg extends ImMsg {

    private String phash; // 图片指纹
    private String url;
    private String md5;


    public String getPhash() {
        return phash;
    }

    public void setPhash(String phash) {
        this.phash = phash;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
}
