package com.bin.im.server.antispam.ahocorasick.interval;

public interface Intervalable extends Comparable {

    public int getStart();
    public int getEnd();
    public int size();

}
