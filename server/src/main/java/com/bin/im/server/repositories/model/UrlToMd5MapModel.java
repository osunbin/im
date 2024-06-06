package com.bin.im.server.repositories.model;

public class UrlToMd5MapModel {
    private long desUid;
    private long sendUid;
    private String md5;
    private int desType;
    private long timestamp;
    private String fileUrl;
    private int deleteFlag;

    public long getDesUid() {
        return desUid;
    }

    public void setDesUid(long desUid) {
        this.desUid = desUid;
    }

    public long getSendUid() {
        return sendUid;
    }

    public void setSendUid(long sendUid) {
        this.sendUid = sendUid;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public int getDesType() {
        return desType;
    }

    public void setDesType(int desType) {
        this.desType = desType;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public int getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(int deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
}
