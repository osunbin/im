package com.bin.im.entry.tcp.encrypt;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.ByteArrayOutputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;


public class Engine {





    protected static Logger logger = LoggerFactory.getLogger(Engine.class);

    public static final int  HANDSHAKE_ERROR = 1000;
    public static final int  RAS_PRIVATE_DECRYPT_ERROR = 1001;
    public static final int  RAS_PUBLIC_ENCRYPT_ERROR = 1002;

    public static final int  RAS_BYTE_TO_PUBLIC_ERROR = 1003;

    public static final int  QUICK_CONNECT_KEY_LEN_ERROR = 1011;

    public static final int  QUICK_CONNECT_VERIFY_INVALID = 1011;

    public static final int  QUICK_CONNECT_SESSION_NOT_FOUND = 1012;

    private static final int  AES_DECRYPT_ERROR = 1021;

    private static final String RSA = "RSA";
    private static final int RAS_KEY_SIZE = 1024;


    protected static final String CBC_PKCS5_PADDING = "AES/ECB/PKCS5Padding";
    protected static final String AES = "AES";
    protected static final int AES_KEY_SIZE = 128;
    protected static final String SHA1_PRNG = "SHA1PRNG";


    private int state = 1;
    protected PublicKey localPublicKey;
    protected PrivateKey localPrivateKey;


    protected byte[] localKeyBytes;
    protected Cipher cipherDecrypt;
    protected Cipher cipherEncrypt;


    public void generateRsaKeys() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA);
            keyPairGenerator.initialize(RAS_KEY_SIZE);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            localPrivateKey = keyPair.getPrivate();
            localPublicKey = keyPair.getPublic();
        } catch (NoSuchAlgorithmException e) {
            throw new HandshakeException("missing key pair generator algorithm RSA",e,HANDSHAKE_ERROR);
        }
    }


    // 加密
    public byte[] encrypt(byte[] content) {
        byte[] encrype = null;
        try {
            encrype = cipherEncrypt.doFinal(content);
        } catch (Exception e) {
            throw new RuntimeException(" content encrypt error", e);
        }
        return encrype;
    }

    // 解密
    public byte[] decrypt(byte[] content) {
        byte[] decrypt = null;
        try {
            decrypt = cipherDecrypt.doFinal(content);
        } catch (Exception e) {
            throw new HandshakeException(" content aes decrypt error", e,AES_DECRYPT_ERROR);
        }
        return decrypt;
    }


    // 私钥解密
    protected byte[] privateDecrypt(byte[] pk, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher(pk,cipher,RAS_KEY_SIZE / 8);
        } catch (Exception e) {
            throw new HandshakeException(" private decrypt fail", e,RAS_PRIVATE_DECRYPT_ERROR);
        }
    }

    // 公钥加密
    protected byte[] publicEncrypt(byte[] content, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher(content,cipher,RAS_KEY_SIZE / 8 - 11);
        } catch (Exception e) {
            throw new HandshakeException(" public  encrypt fail", e,RAS_PUBLIC_ENCRYPT_ERROR);
        }
    }



    public  byte[] cipher(byte[] srcBytes, Cipher cipher,int segmentSize) {
        try {

            byte[] resultBytes = null;
            if (segmentSize > 0)
                resultBytes = cipherDoFinal(cipher, srcBytes, segmentSize); //分段加密
            else
                resultBytes = cipher.doFinal(srcBytes);

            return resultBytes;
        } catch (Exception e) {
            throw new RuntimeException("segment cipher fail", e);
        }
    }



    public static byte[] cipherDoFinal(Cipher cipher, byte[] srcBytes, int segmentSize)
            throws IllegalBlockSizeException, BadPaddingException {
        if (segmentSize <= 0)
            throw new RuntimeException("分段大小必须大于0");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int inputLen = srcBytes.length;
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > segmentSize) {
                cache = cipher.doFinal(srcBytes, offSet, segmentSize);
            } else {
                cache = cipher.doFinal(srcBytes, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * segmentSize;
        }
        return out.toByteArray();
    }


    protected PublicKey coverPublicKey(byte[] pk) {
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(pk);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            return keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            throw new HandshakeException("cover server publicKey fail", e,RAS_BYTE_TO_PUBLIC_ERROR);
        }
    }


    public byte[] getPublicKeyBytes() {
        return localPublicKey.getEncoded();
    }


    protected byte[] getLocalKeyBytes() {
        return localKeyBytes;
    }


}
