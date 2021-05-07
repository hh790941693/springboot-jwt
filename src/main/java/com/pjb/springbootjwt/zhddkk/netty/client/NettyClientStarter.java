package com.pjb.springbootjwt.zhddkk.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClientStarter {

    public static void main(String[] args){
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).handler(new NettyClientInitializer());
            System.out.println("客户端准备连接!");
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 10000).sync();
            channelFuture.channel().closeFuture().sync();
            channelFuture.channel().writeAndFlush("hello i am client");
        }catch (Exception e) {
            System.out.println("客户端断开连接!");
            eventLoopGroup.shutdownGracefully();
        }
    }
}
