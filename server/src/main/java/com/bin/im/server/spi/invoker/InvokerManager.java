package com.bin.im.server.spi.invoker;

import com.bin.im.server.core.MsgNotifyInfo;
import org.apache.dubbo.rpc.RpcContext;

import java.util.HashMap;
import java.util.Map;

public class InvokerManager {

    private Map<String,DubboInvoker> entryInvoker = new HashMap<>();





    public void notifyEntry(MsgNotifyInfo msgNotifyInfo, Map<String, Object> parameterMap) {

        String ip = msgNotifyInfo.getEsIp();
        int port = msgNotifyInfo.getEsPort();

        Map<String, Object> attachments = new HashMap<>();

        String address = ip + ":"  +port;
        DubboInvoker dubboInvoker = entryInvoker.get(address);
        if (dubboInvoker == null) {
            synchronized (this) {
                if (dubboInvoker == null) {
                    dubboInvoker = new DubboInvoker(address);
                    entryInvoker.put(address,dubboInvoker);
                }
            }
        }
        dubboInvoker.invoker(attachments,parameterMap);
    }



}
