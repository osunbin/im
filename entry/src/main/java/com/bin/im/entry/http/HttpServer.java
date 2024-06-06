package com.bin.im.entry.http;

import com.bin.im.entry.common.netty.NettyHelper;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpServer {

    private static Logger logger = LoggerFactory.getLogger(HttpServer.class);

    final public static int MAX_REQUEST_LENGTH = 16384;   // websocket client 最大的请求长度

    private int workThreads;

    private int port;

    private Channel parentChannel;

    public HttpServer(int port, int workThreads) {
        this.port = port;
        this.workThreads = workThreads;
    }

    public void start(){
        EventLoopGroup bossGroup = NettyHelper.newNioOrEpollEventLoopGroup(1,"http server boss");

        EventLoopGroup workGroup = NettyHelper.newNioOrEpollEventLoopGroup(workThreads,"http server worker");

        ServerBootstrap serverBootstrap = NettyHelper.serverBootstrap(bossGroup,workGroup);
        serverBootstrap
                .option(ChannelOption.ALLOCATOR, ByteBufAllocator.DEFAULT)
                .childOption(ChannelOption.SO_SNDBUF, 32 * 1024)
                .childOption(ChannelOption.SO_RCVBUF, 16 * 1024);

        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast( new HttpServerCodec());
                // post处理
                pipeline.addLast( new HttpObjectAggregator(32 * 1024));
                pipeline.addLast(new HttpServerHandler());
            }
        });

        ChannelFuture channelFuture = serverBootstrap.bind(port).syncUninterruptibly().addListener(future -> {

            logger.info("Netty websocket Server started on port {}", port);
        });
        channelFuture.channel().closeFuture().addListener(future -> {
            logger.info("Netty websocket  Server Start Shutdown ............");
            bossGroup.shutdownGracefully().syncUninterruptibly();
            workGroup.shutdownGracefully().syncUninterruptibly();
        });
        parentChannel = channelFuture.channel();
    }


    public void shutdown() {
        logger.info("shutdown called {}", port);
        if (parentChannel != null) {
            parentChannel.close();
        }
    }

}
