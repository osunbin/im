package com.bin.im.common.internal.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class MD5Util {

    private MD5Util() {
    }

    /**
     * Converts given string to MD5 hash
     *
     * @param str str to be hashed with MD5
     */
    @SuppressWarnings("checkstyle:magicnumber")
    public static String toMD5String(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (md == null || str == null) {
                return null;
            }
            byte[] byteData = md.digest(str.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (byte aByteData : byteData) {
                sb.append(Integer.toString((aByteData & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ignored) {
            return null;
        }
    }
}