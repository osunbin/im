package com.bin.im.entry.http;

import com.bin.im.common.internal.utils.StringUtils;
import com.bin.im.entry.http.handler.HttpProtocolParser;
import com.bin.im.entry.http.handler.HttpTaskPool;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;

public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {



    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest httpRequest) throws Exception {


        FullHttpRequest copyRequest = httpRequest.copy();
        NettyHttpRequest nettyHttpRequest = new NettyHttpRequest(copyRequest);
        Channel channel = ctx.channel();

        String uriPath = nettyHttpRequest.getUriPath(); // 去除参数
        if (StringUtils.isEmpty(uriPath)) {
            return;
        }

        HttpTaskPool.submitTask(new HttpProtocolParser(nettyHttpRequest,channel));

    }
}
