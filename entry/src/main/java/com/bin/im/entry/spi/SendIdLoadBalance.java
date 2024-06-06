package com.bin.im.entry.spi;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.cluster.loadbalance.AbstractLoadBalance;

import java.util.List;
import java.util.Map;

import static com.bin.im.common.ImAttachment.ID;

/**
 *  保证 同一个 发消息的落在同一个机器/同一个群的人发的消息都落在同一个机器
 */
public class SendIdLoadBalance extends AbstractLoadBalance {
    public static final String NAME = "sendId";


    @Override
    protected <T> Invoker<T> doSelect(List<Invoker<T>> invokers, URL url, Invocation invocation) {
        Map<String, Object> attachments = invocation.getObjectAttachments();
        Object o = attachments.get(ID);

        long id = Long.parseLong(o.toString());
        int index = (int) (id % invokers.size());
        return invokers.get(index);

    }
}
