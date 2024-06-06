package com.bin.im.common.encrypt;
 

 
import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 *  服务端随机生成公私钥，pk1,pk2，将公钥发给客户端
 *  客户端随机生成公私钥，pk11,pk22,将公钥pk11，通过pk1加密,传给客户端
 *  服务端随机生成对称密钥key=x,用pk11加密传给客户端
 */
public class RSAUtil {
 
    /**
     * 获取RSA公私钥匙对
     */
    private static KeyPair getKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048); //512、1024、2048
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair;
    }
 
 
    /**
     * 获取公钥(base64编码)
     */
    private static String getPublicKey(KeyPair keyPair) {
        PublicKey publicKey = keyPair.getPublic();
        byte[] bytes = publicKey.getEncoded();
        return Base64.getEncoder().encodeToString(bytes);
    }
 
 
    /**
     * 获取私钥(Base64编码)
     */
    private static String getPrivateKey(KeyPair keyPair) {
        PrivateKey privateKey = keyPair.getPrivate();
        byte[] bytes = privateKey.getEncoded();
        return Base64.getEncoder().encodeToString(bytes);
    }
 
 
    public static String[] genKeyPair() throws NoSuchAlgorithmException {
        KeyPair keyPair = getKeyPair();
        String[] keyPairArr = new String[2];
        keyPairArr[0] = getPublicKey(keyPair);
        keyPairArr[1] = getPrivateKey(keyPair);
        return keyPairArr;
    }
 
 
    /**
     * 将Base64编码后的公钥转换成PublicKey对象
     */
    public static PublicKey string2PublicKey(String pubStr) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(pubStr);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }
 
    /**
     * 将Base64编码后的私钥转换成PrivateKey对象
     */
    public static PrivateKey string2PrivateKey(String priStr) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(priStr);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }
 
    /**
     * 公钥加密
     */
    public static String publicEncrypt(String content, String publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, RSAUtil.string2PublicKey(publicKey));
        byte[] byteEncrypt = cipher.doFinal(content.getBytes("utf-8"));

        String msg = Base64.getEncoder().encodeToString(byteEncrypt);
        return msg;
    }
 
    /**
     * 私钥解密
     */
    public static String privateDecrypt(String contentBase64, String privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, RSAUtil.string2PrivateKey(privateKey));
        byte[] bytesDecrypt = cipher.doFinal(Base64.getDecoder().decode(contentBase64));
        String msg = new String(bytesDecrypt, "utf-8");
        return msg;
    }


    public static void main(String args[]) throws Exception {
        String[] keyPairArr = RSAUtil.genKeyPair();
        System.out.println("公钥: " + keyPairArr[0]);
        System.out.println();

        //
        System.out.println("私钥: " + keyPairArr[1]);
        System.out.println();

        //
        String string = "勇敢行sfsdf3择发生的方式5345353@#￥%……&*（）：“《》？@#$%^&()<>?:";

        String msg = RSAUtil.publicEncrypt(string, keyPairArr[0]);
        System.out.println("加密后内容: " + msg);
        System.out.println();

        //
        String mms = RSAUtil.privateDecrypt(msg, keyPairArr[1]);
        System.out.println("解密后内容: " + mms);
        System.out.println();
    }


 
 
}