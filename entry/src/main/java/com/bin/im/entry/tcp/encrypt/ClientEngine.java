package com.bin.im.entry.tcp.encrypt;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.PublicKey;

public class ClientEngine extends Engine implements ClientEncrypt {


    // 客户端 生成 公私密钥 用 s公钥加密c公钥 传给服务端
    @Override
    public byte[] serverHello(byte[] serverPublic) {
        PublicKey publicKey = coverPublicKey(serverPublic);
        generateRsaKeys();
        return publicEncrypt(getPublicKeyBytes(), publicKey);
    }

    @Override
    public void finalHello(byte[] serverAesKey) {
        byte[] keyBytes = privateDecrypt(serverAesKey, localPrivateKey);
        initAes(keyBytes);
    }

    @Override
    public byte[] quickConnect() {
        byte[] localKeyBytes = getLocalKeyBytes();

        return encrypt(localKeyBytes);
    }

    private void initAes(byte[] keyBytes) {
        Cipher decrypt = null;
        Cipher encrypt = null;
        try {
            SecretKey secretKey = new SecretKeySpec(keyBytes, AES);

            decrypt = Cipher.getInstance(CBC_PKCS5_PADDING);

            decrypt.init(Cipher.DECRYPT_MODE, secretKey);

            encrypt = Cipher.getInstance(CBC_PKCS5_PADDING);
            encrypt.init(Cipher.ENCRYPT_MODE, secretKey);
        } catch (Exception e) {
            throw new RuntimeException("init aes fail");
        }
        cipherDecrypt = decrypt;
        cipherEncrypt = encrypt;
        localKeyBytes = keyBytes;
    }
}
