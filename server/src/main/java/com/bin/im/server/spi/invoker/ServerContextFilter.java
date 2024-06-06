package com.bin.im.server.spi.invoker;

import com.bin.im.common.ImAttachment;
import com.bin.im.server.spi.impl.ServerContext;
import com.bin.im.server.spi.impl.ServiceManager;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcException;

@Activate(group = CommonConstants.PROVIDER)
public class ServerContextFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

        String clientIp = invocation.getAttachment(ImAttachment.CLIENT_IP);
        String entryIp = invocation.getAttachment(ImAttachment.ENTRY_IP);

        String entryPort = invocation.getAttachment(ImAttachment.ENTRY_PORT);

        ServerContext serverContext = new ServerContext();
        serverContext.setClientIp(clientIp);
        serverContext.setEsIp(entryIp);
        serverContext.setEsPort(Integer.parseInt(entryPort));

        ServiceManager.createContext(serverContext);


        return invoker.invoke(invocation);
    }
}
