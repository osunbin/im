package com.bin.im.entry.spi;

import com.bin.im.entry.tcp.handle.EntryContext;
import com.bin.im.entry.tcp.session.OnePeer;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.service.GenericService;

import java.util.Map;


public class DubboInvoker implements EntryInvoker{



    public void invoker(EntryContext context) {
        OnePeer onePeer = context.getOnePeer();
        String module = context.getModule();
        Map<String, Object> attachments = context.getAttachments();
        Map<String, Object> parameterMap = context.getParameterMap();
        String apiInterface = context.getApiInterface();
        String method = context.getMethod();

        parameterMap.put("reqUid",onePeer.getUid());
        ReferenceConfig<GenericService> reference = new ReferenceConfig<>();
        // 弱类型接口名
        reference.setInterface(apiInterface);

        // 声明为泛化接口
        reference.setGeneric("true");
        reference.setAsync(true);
        //设置超时时间
        // reference.setTimeout(7000);
        // reference.setUrl("dubbo://localhost:20880");

        reference.setLoadbalance(SendIdLoadBalance.NAME);

        reference.setGroup(module);
        reference.setProtocol("dubbo");

        GenericService genericService = reference.get();

        RpcContext.getClientAttachment().setObjectAttachments(attachments);


        genericService.$invoke(method,new String[] {Map.class.getName()}, new Object[] {parameterMap});

    }

}
