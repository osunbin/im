package com.bin.im.entry.tcp.encrypt;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.PublicKey;
import java.security.SecureRandom;


public class ServerEngine extends Engine implements ServerEncrypt{



    @Override
    public byte[] initial() {
        generateRsaKeys();
        return getPublicKeyBytes();
    }


    //  解密 client 的 publicKey
    // 生成 AES
    //  用 client 加密 AES
    @Override
    public byte[] clientHello(byte[] clientPublicKeys) {

        byte[] pk = privateDecrypt(clientPublicKeys, localPrivateKey);

        PublicKey clientPublicKey = coverPublicKey(pk);

        generateAesKeys();

        return publicEncrypt(localKeyBytes, clientPublicKey);
    }

    @Override
    public void verify(byte[] clientAes) {

        byte[] remoteKeyBytes = decrypt(clientAes);

        if (localKeyBytes.length != remoteKeyBytes.length) {
            throw new HandshakeException("aes key length invaild localLen-" + localKeyBytes.length + "remoteLen-" + remoteKeyBytes.length,QUICK_CONNECT_KEY_LEN_ERROR);

        }
        byte ret = 0;
        for (int i = 0; i < remoteKeyBytes.length; i++)
            ret |= localKeyBytes[i] ^ remoteKeyBytes[i];
        if (ret != 0) {
            throw new HandshakeException("quick connect verify fail ",QUICK_CONNECT_VERIFY_INVALID);
        }

    }


    private void generateAesKeys() {
        Cipher decrypt = null;
        Cipher encrypt = null;
        SecretKey secretKey = null;
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
            keyGenerator.init(AES_KEY_SIZE, SecureRandom.getInstance(SHA1_PRNG));

            secretKey = keyGenerator.generateKey();

            decrypt = Cipher.getInstance(CBC_PKCS5_PADDING);

            decrypt.init(Cipher.DECRYPT_MODE, secretKey);

            encrypt = Cipher.getInstance(CBC_PKCS5_PADDING);
            encrypt.init(Cipher.ENCRYPT_MODE, secretKey);
        } catch (Exception e) {
            throw new HandshakeException("romdom generate aes fail",e,HANDSHAKE_ERROR);
        }
        cipherDecrypt = decrypt;
        cipherEncrypt = encrypt;
        localKeyBytes = secretKey.getEncoded();
    }

}
