package com.bin.im.entry.spi;

import com.bin.im.entry.common.Converter;
import com.bin.im.entry.tcp.handle.EntryContext;
import com.bin.im.entry.tcp.handle.EntryHandler;
import com.bin.im.entry.tcp.session.OnePeer;
import io.netty.channel.Channel;
import org.apache.dubbo.remoting.TimeoutException;
import org.apache.dubbo.rpc.RpcContext;

import java.util.Map;
import java.util.function.BiConsumer;

import static com.bin.im.entry.cache.RequestSessionCacheManager.sessionCache;

public class DubboAsyncCallback implements BiConsumer<Object,Throwable> {

    EntryHandler entryHandler;



    @Override
    public void accept(Object genericResult, Throwable throwable) {
        if (throwable != null) {
            if (throwable instanceof TimeoutException) {

            }
        }
        if (genericResult instanceof Map result) {
            Map<String, Object> attachments =
                    RpcContext.getServerAttachment().getObjectAttachments();

            Object seqObj = attachments.get("seq");
            long seq = Converter.coverLong(seqObj);
            EntryContext context = entryHandler.getContext(seq);
            context.setResultMap(result);
            context.onAfter();

            OnePeer onePeer = context.getOnePeer();

            onePeer.pushApp(context);


            entryHandler.unRegisterReq(seq);

        }

    }
}
