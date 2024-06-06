package com.bin.im.entry.tcp.encrypt;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionSecrets {


    private static Map<Long, ServerEncrypt> serverSecrets = new ConcurrentHashMap<>();


    public static byte[] create(long session) {
        ServerEncrypt serverEncrypt = new ServerEngine();

        byte[] publicKey = serverEncrypt.initial();
        serverSecrets.put(session, serverEncrypt);
        return publicKey;
    }


    public static byte[] clientHello(long session, byte[] clientPublicKeys) {
        ServerEncrypt serverEncrypt = serverSecrets.get(session);
        return serverEncrypt.clientHello(clientPublicKeys);
    }

    public static void verify(long session, byte[] aes) {
        ServerEncrypt serverEncrypt = serverSecrets.get(session);
        serverEncrypt.verify(aes);
    }

    public static byte[] encrypt(long session, byte[] content) {
        ServerEncrypt serverEncrypt = serverSecrets.get(session);
        return serverEncrypt.encrypt(content);
    }

    public static byte[] decrypt(long session, byte[] content) {
        ServerEncrypt serverEncrypt = serverSecrets.get(session);
        return serverEncrypt.decrypt(content);
    }

    public static void restore(long session) {
        serverSecrets.remove(session);
    }

}
