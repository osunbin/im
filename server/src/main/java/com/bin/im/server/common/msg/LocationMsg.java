package com.bin.im.server.common.msg;

public class LocationMsg extends ImMsg{

    private String longitude; // 精度
    private String latitude;  // 维度


    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
