package com.bin.im.server.spi.invoker;

import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.service.GenericService;


import java.util.Map;

public class DubboInvoker {

    private static final String DUBBO = "dubbo://";
    private static final String apiInterface = "com.bin.im.entry.tcp.handle.EntryServerHandler";
    private static final String DUBBO_METHOD = "handle";

    private GenericService genericService;


    public DubboInvoker(String address) {
        genericService = createGenericService(address);
    }


    public void invoker(Map<String, Object> attachments,Map<String, Object> parameterMap) {



        RpcContext.getClientAttachment().setObjectAttachments(attachments);


        genericService.$invoke(DUBBO_METHOD,new String[] {Map.class.getName()}, new Object[] {parameterMap});

    }


    private GenericService createGenericService(String address) {

        ReferenceConfig<GenericService> reference = new ReferenceConfig<>();
        // 弱类型接口名
        reference.setInterface(apiInterface);

        // 声明为泛化接口
        reference.setGeneric("true");
        reference.setAsync(true);
        //设置超时时间
        // reference.setTimeout(7000);
        reference.setUrl(DUBBO + address);


        reference.setProtocol("dubbo");

        GenericService genericService = reference.get();
        return genericService;
    }
}
