package com.bin.im.entry.common;

public class Converter {

    public static int coverInt(Object obj) {
        if (obj != null) {
            try {
                return Integer.parseInt(obj.toString());
            }catch (Exception e) {
            }
        }
        return 0;
    }

    public static long coverLong(Object obj) {
        if (obj != null) {
            try {
                return Long.parseLong(obj.toString());
            }catch (Exception e) {
            }
        }
        return 0;
    }

    public static String coverStr(Object obj) {
        if (obj != null) {
            try {
                return obj.toString();
            }catch (Exception e) {
            }
        }
        return "";
    }
}
