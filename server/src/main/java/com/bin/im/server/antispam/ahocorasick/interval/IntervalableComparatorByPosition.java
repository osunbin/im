package com.bin.im.server.antispam.ahocorasick.interval;

import java.util.Comparator;

public class IntervalableComparatorByPosition implements Comparator<Intervalable> {

    @Override
    public int compare(Intervalable intervalable, Intervalable intervalable2) {
        return intervalable.getStart() - intervalable2.getStart();
    }

}
