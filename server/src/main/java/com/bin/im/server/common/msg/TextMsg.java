package com.bin.im.server.common.msg;

public class TextMsg extends ImMsg {
    private String text;

    public TextMsg(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
