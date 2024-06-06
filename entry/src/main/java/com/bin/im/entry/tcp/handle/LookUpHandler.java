package com.bin.im.entry.tcp.handle;

import com.bin.im.entry.common.NacosHelper;
import com.bin.im.entry.spi.DubboAsyncCallback;
import com.bin.im.entry.spi.EntryInvoker;
import com.bin.im.entry.spi.Invoker;
import org.apache.dubbo.rpc.RpcContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.CompletableFuture;


public class LookUpHandler extends EntryHandler {

    private static Logger logger = LoggerFactory.getLogger(LookUpHandler.class);

    private EntryInvoker entryInvoker;

    private DubboAsyncCallback asyncCallback = new DubboAsyncCallback();

    public void perform(EntryContext context) {
        String module = context.getModule();
        String cmd = context.getCmd();

        String confStr = NacosHelper.getCmdConfig(module, cmd);
        // 判断请求是否注册
        if (null == confStr || confStr.isEmpty()) {
            logger.error("model:{} cmd:{}  register  not found", module, cmd);
            return; // TODO 接口找不到服务
        }
        String[] configList = confStr.split(":");
        String apiInterface = configList[0];
        String method = configList[1];
        context.setApiInterface(apiInterface);
        context.setMethod(method);

        Invoker invoker =
                (ct) -> {
                    try {

                        entryInvoker.invoker(ct);
                        registerReq(context);

                        CompletableFuture<Map<String, Object>> completableFuture =
                                RpcContext.getServiceContext().getCompletableFuture();
                        completableFuture.whenComplete(asyncCallback);

                    } catch (Exception e) {
                        logger.error("invoker error", e);
                    }
                };
        context.setInvoker(invoker);

    }
}
