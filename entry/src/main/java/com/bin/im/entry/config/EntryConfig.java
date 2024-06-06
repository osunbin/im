package com.bin.im.entry.config;

import com.bin.im.common.internal.utils.IPConverter;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bin.im.common.ImAttachment.ENTRY_IP;
import static com.bin.im.common.ImAttachment.ENTRY_PORT;


/**
 *   MAX_CT_PACKET_SIZE : 262144
 *   MAX_TC_PACKET_SIZE : 524288
 *   m_nMaxBuffSize = m_nMaxCTPacketSize * 4;
 * 	 m_nMaxTCBuffSize = m_nMaxTCPacketSize * 2;
 * 	 m_nMaxCTBuffSize = m_nMaxCTPacketSize *2;
 *
 */
public class EntryConfig {

    public static final EntryConfig ENTRY_CONFIG = new EntryConfig();

    private int tcpServerPort = 8200;

    private int socketServerPort = 8100;

    private int cpuCores = Runtime.getRuntime().availableProcessors();

    private String ip;

    private int port;



    private  List<String> httpHeaders = new ArrayList<>();


    public List<String> getHttpHeaders() {
        return httpHeaders;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }



    public Map<String,Object> buildEntryAttachments() {
        Map<String,Object> attachments = new HashMap<>();


        attachments.put(ENTRY_IP,IPConverter.ip2Int(getIp()));
        attachments.put(ENTRY_PORT,getPort());

        return attachments;
    }



    public int getCpuCores() {
        return cpuCores;
    }

    public void setCpuCores(int cpuCores) {
        this.cpuCores = cpuCores;
    }


    public int getSocketServerPort() {
        return socketServerPort;
    }

    public void setSocketServerPort(int socketServerPort) {
        this.socketServerPort = socketServerPort;
    }

    public int getTcpServerPort() {
        return tcpServerPort;
    }

    public void setTcpServerPort(int tcpServerPort) {
        this.tcpServerPort = tcpServerPort;
    }
}
