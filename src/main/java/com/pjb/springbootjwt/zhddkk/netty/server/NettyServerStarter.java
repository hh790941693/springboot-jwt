package com.pjb.springbootjwt.zhddkk.netty.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

@Component
@Order(value = 1)
public class NettyServerStarter implements CommandLineRunner {

    private static Logger logger = LoggerFactory.getLogger(NettyServerStarter.class);

    @Value("${ip.public}")
    private String serverIp;

    @Value("${port.nettyServer}")
    private Integer serverPort;

    @Autowired
    private NettyServer nettyServer;

    public void run(String...strings) throws Exception{
        InetSocketAddress addressInfo = new InetSocketAddress(serverIp, serverPort);
        logger.info("开始启动netty服务器端口：" + serverPort);
        new Thread(new Runnable() {
            @Override
            public void run() {
                nettyServer.start(addressInfo);
                logger.info("端口:{}已启动", serverPort);
            }
        }).start();
    }
}