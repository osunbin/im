package com.bin.im.common.encrypt;


import com.bin.im.common.internal.utils.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * AES加解密操作类
 */
public class AESOperator {
 
    /**
     * 加密用的Key 可以用26个字母和数字组成 此处使用AES-128-CBC加密模式，key需要为16位。
     */
    public static final String skey = "smkldospd121daaa";
    /**
     * 偏移量,可自行修改
     */
    private static String ivParameter = "1016449182184177";
 
    private static AESOperator instance = null;
 
    private AESOperator() {
 
    }
 
    public static AESOperator getInstance() {
        if (instance == null) {
            instance = new AESOperator();
        }
        return instance;
    }
 
    public String Encrypt(String encData, String secretKey, String vector) throws Exception {
 
        if (secretKey == null) {
            return null;
        }
        if (secretKey.length() != 16) {
            return null;
        }
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] raw = secretKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        // 使用CBC模式，需要一个向量iv，可增加加密算法的强度
        IvParameterSpec iv = new IvParameterSpec(vector.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(encData.getBytes("utf-8"));
        // 此处使用BASE64做转码。

        return Base64.getEncoder().encodeToString(encrypted);
    }
 
    /**
     * 加密
     *
     * @param sSrc
     * @param sKey
     * @return
     * @throws Exception
     */
    public String encrypt(String sSrc, String sKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] raw = sKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        // 使用CBC模式，需要一个向量iv，可增加加密算法的强度
        IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
        // 此处使用BASE64做转码。
        return replace(Base64.getEncoder().encodeToString(encrypted));
    }
 
    /**
     * 解密
     *
     * @param sSrc
     * @param sKey
     * @return
     */
    public String decrypt(String sSrc, String sKey) {
        try {
            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            // 先用base64解密
            byte[] encrypted1 = Base64.getDecoder().decode(sSrc);
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, "utf-8");
            return originalString;
        } catch (Exception ex) {
            return null;
        }
    }
 
    public String Decrypt(String sSrc, String key, String ivs) {
        try {
            byte[] raw = key.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(ivs.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            // 先用base64解密
            byte[] encrypted1 = Base64.getDecoder().decode(sSrc);
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, "utf-8");
            return originalString;
        } catch (Exception ex) {
            return null;
        }
    }
 
    public static String encodeBytes(byte[] bytes) {
        StringBuffer strBuf = new StringBuffer();
 
        for (int i = 0; i < bytes.length; i++) {
            strBuf.append((char) (((bytes[i] >> 4) & 0xF) + ((int) 'a')));
            strBuf.append((char) (((bytes[i]) & 0xF) + ((int) 'a')));
        }
 
        return strBuf.toString();
    }
 
    /**
     * 去除 换行符、制表符
     *
     * @param str
     * @return
     */
    public static String replace(String str) {
        if (!StringUtils.isEmpty(str)) {
            return str.replaceAll("\r|\n", "");
        }
        return str;
    }
 
    //测试
    public static void main(String[] args) throws Exception {

        // 生成AES密钥
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128, SecureRandom.getInstance("SHA1PRNG"));
        SecretKey secretKey = keyGenerator.generateKey();

        byte[] keyBytes = secretKey.getEncoded();
        // bNlMFkQJJXP/uyjiBBMbTg==
        // RgRybzWh1SIdQKrIqFsdEA==
        System.out.println("key:\n" +
                Base64.getEncoder().encodeToString(keyBytes));

        // 需要加密的字串
        String cSrc = "{\"loginName\":\"master\",\"secret\":\"123456\"}";
        System.out.println(cSrc);
        // 加密
        String enString = AESOperator.getInstance().Encrypt(cSrc, skey, ivParameter);
        System.out.println("加密后的字串是：" + replace(enString));
        String test = replace(enString);
 
        // 解密
        String DeString = AESOperator.getInstance().Decrypt(test, skey, ivParameter);
        System.out.println("解密后的字串是：" + DeString);
    }
}