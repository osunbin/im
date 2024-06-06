package com.bin.im.entry.tcp.encrypt;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class TestEngine {


    public static void main(String[] args) {
        ClientEncrypt clientEngine = new ClientEngine();


        ServerEncrypt serverEngine = new ServerEngine();

        byte[] serverHello = serverEngine.initial();


        byte[] clientHello = clientEngine.serverHello(serverHello);

        byte[] hello = serverEngine.clientHello(clientHello);

        clientEngine.finalHello(hello);


        // 需要加密的字串
        String cSrc = "{\"loginName\":\"master\",\"secret\":\"123456\"}";
        System.out.println("加密前-"+cSrc);

        byte[] encrypt = clientEngine.encrypt(cSrc.getBytes(StandardCharsets.UTF_8));

        System.out.println("加密-" + Base64.getEncoder().encodeToString(encrypt));

        byte[] decrypt = serverEngine.decrypt(encrypt);

        String content = new String(decrypt, StandardCharsets.UTF_8);

        System.out.println("加密后-"+content);


        byte[] quickConnect = clientEngine.quickConnect();

        boolean verify = serverEngine.verify(quickConnect);

        System.out.println("verify quick connect " + verify);

    }
}
