package com.bin.im.entry.tcp.handle;



import com.bin.im.entry.tcp.TcpHeader;
import com.bin.im.entry.tcp.session.OnePeer;
import com.bin.im.entry.tcp.session.OnePeerManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;


public class TcpProtocolParser {
    private static Logger logger = LoggerFactory.getLogger(TcpProtocolParser.class);




    private Channel channel;

    private ByteBuf byteBuf;



    private OnePeerManager onePeerManager;



    /**
     * es/cts/ts  接收前端  ces_client 通知 logic
     * bg/cbg/bs  接收logic
     */
    public void run() {

        TcpHeader tcpHeader = new TcpHeader();
        if (!tcpHeader.parser(byteBuf)) {
            // TODO 丢弃
            return;
        }

        OnePeer onePeer = onePeerManager.getOnePeer(channel);
        if (onePeer == null) {
            synchronized (channel) {
                if (onePeer == null) {
                    onePeer = new OnePeer();
                    onePeer.setChannel(channel);
                    InetSocketAddress socketAddress = (InetSocketAddress) channel.remoteAddress();
                    String clientIp = socketAddress.getAddress().getHostAddress();
                    onePeer.setClientIp(clientIp);

                    onePeerManager.registryPnePeer(onePeer);
                }

            }
        }
        if (onePeer.isBlock())
            return;
        tcpHeader.handshake(onePeer);

    }


    /**
     * HandleData_TS_KICKOUT_USER_REQ ->DeleteSession
     * HandleLogoutData -> DeleteSession
     * <p>
     * HandleQuickConnectData ->PauseSessionTimeout
     * <p>
     * OnSendLSCommonPacket-> DeleteSession
     * <p>
     * OnSendLSCommonPacket-> RestorePeer
     * <p>
     * OnSendLSLoginPacket->DeleteSession
     * <p>
     * OnSendLSLoginPacket-> PauseSessionTimeout
     * <p>
     * OnSendLSLoginReadyPacket->DeleteSession
     */

}
