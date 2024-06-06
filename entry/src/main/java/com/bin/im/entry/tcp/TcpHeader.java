package com.bin.im.entry.tcp;

import com.bin.im.common.json.JsonHelper;
import com.bin.im.entry.tcp.encrypt.HandshakeException;
import com.bin.im.entry.tcp.session.OnePeer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import static com.bin.im.entry.tcp.encrypt.Engine.HANDSHAKE_ERROR;

public class TcpHeader {
    private static final int IM_MAGIC = 0x20240627;
    private static final int HANDSHAKE_OK = 1;

    private static final int initial = 1;
    private static final int ServerHello = 2;
    private static final int ClientHello = 3;
    private static final int Finished = 4;
    private static final int quickConnect = 5;
    private static final int app = 9;


    private int magic;
    private int tlsStatus; // handshake  app
    private String module;
    private String cmd;
    private String version; // content 协议版本
    private int code;
    private int dataLen;
    private byte[] data;
    private boolean tlsSuccess = true;
    private int  quickDataLen;
    private byte[] quickData;


    private byte[] decryptData;

    public TcpHeader() {

    }

    public TcpHeader(String module,String cmd) {
        this.magic = IM_MAGIC;
        this.tlsStatus = app;
        this.module = module;
        this.cmd = cmd;
    }

    public int headLen() {
        return 4 + 4 + 4;
    }



    public boolean parser(ByteBuf byteBuf) {
        int magic = byteBuf.readIntLE();
        if (magic != IM_MAGIC) {
            tlsFail();
            return false;
        }
        this.magic = magic;
        tlsStatus = byteBuf.readIntLE();
        code = byteBuf.readIntLE();
        dataLen = byteBuf.readIntLE();
        if (dataLen > 0) {

            if (byteBuf.readableBytes() < dataLen)
                return false;

            data = new byte[dataLen];
            byteBuf.readBytes(data);


            if (tlsStatus == quickConnect) {
                quickDataLen = byteBuf.readIntLE();
               if (quickDataLen > 0) {
                   if (byteBuf.readableBytes() < quickDataLen)
                       return false;

                   quickData = new byte[quickDataLen];
                   byteBuf.readBytes(quickData);
               }
            }
        }
        return true;
    }


    public void handshake(OnePeer onePeer) {

        byte[] keys = new byte[0];
        boolean needResp = true;
        try {
            switch (tlsStatus) {
                case initial:
                    onePeer.createServerEngine();
                    break;
                case ClientHello:
                    keys = onePeer.clientHello(data);
                    break;
                case quickConnect:
                    boolean quick = onePeer.quickConnect(data);
                    if (!quick)
                        return; // 直接丢弃

                    dataLen = quickDataLen;
                    data = quickData;
                case app:
                    if (dataLen == 0)
                        return;
                    decryptData = onePeer.decrypt(data);
                    needResp = false;
                    onePeer.processApp(this);
                    break;
            }
            code = HANDSHAKE_OK;
        } catch (Exception e) {
            if (e instanceof HandshakeException hs) {
                code = hs.getCode();
            } else {
                code = HANDSHAKE_ERROR;
            }
            tlsFail();
        }
        if (needResp) {
            handshakeResp(onePeer.getChannel(), keys);
        }
    }



    public void handshakeResp(Channel channel, byte[] data) {

        ByteBuf byteBuf = channel.alloc().buffer(headLen() + data.length);
        byteBuf.writeIntLE(IM_MAGIC);
        int status = tlsStatus;
        if (tlsSuccess && tlsStatus < 5) {
            ++status;
        }
        byteBuf.writeIntLE(status);
        byteBuf.writeIntLE(code);
        byteBuf.writeIntLE(data.length);
        if (data.length > 0)
              byteBuf.writeBytes(data);
    }


    public Map<String, Object> jsonToMap() {

        try {
           return JsonHelper.jsonByteToMap(decryptData);
        } catch (IOException e) {
            e.printStackTrace();
            // TODO
            return Collections.emptyMap();
        }
    }



    public String buildLogPre(long logId,String clientIp) {
        return " logId: " + logId + "  clientIp: " + clientIp + " module: " + module + " cmd " + cmd;
    }

    public void tlsFail() {
        tlsSuccess = false;
    }



    public int getMagic() {
        return magic;
    }


    public String getModule() {
        return module;
    }

    public String getCmd() {
        return cmd;
    }

    public String getVersion() {
        return version;
    }

    public int getCode() {
        return code;
    }

    public int getDataLen() {
        return dataLen;
    }

    public byte[] getData() {
        return data;
    }



    public void setModule(String module) {
        this.module = module;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setDataLen(int dataLen) {
        this.dataLen = dataLen;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
