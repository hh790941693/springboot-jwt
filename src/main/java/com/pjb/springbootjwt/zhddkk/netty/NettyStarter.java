package com.pjb.springbootjwt.zhddkk.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.net.InetSocketAddress;

@Component
@Order(value = 1)
public class NettyStarter implements CommandLineRunner {

    private static Logger logger = LoggerFactory.getLogger(NettyStarter.class);

    @Autowired
    private NettyServer nettyServer;

    public void run(String...strings) throws Exception{
        InetSocketAddress address10000 = new InetSocketAddress(NettyConstants.SERVER_IP, NettyConstants.SERVER_PORT);
        logger.info("开始启动netty服务器端口：" + NettyConstants.SERVER_PORT);
        new Thread(new Runnable() {
            @Override
            public void run() {
                nettyServer.start(address10000);
                logger.info("端口:{}已启动", NettyConstants.SERVER_PORT);
            }
        }).start();
    }
}