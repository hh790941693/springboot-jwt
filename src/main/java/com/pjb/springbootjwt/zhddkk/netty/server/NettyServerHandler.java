package com.pjb.springbootjwt.zhddkk.netty.server;

import com.pjb.springbootjwt.common.utils.SpringContextHolder;
import com.pjb.springbootjwt.zhddkk.service.WsUsersService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: qingcheng
 * @author: XIONG CHUAN
 * @create: 2019-04-28 15:21
 * @description: netty服务端处理类
 **/

//@Slf4j
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);

    @Autowired
    private static WsUsersService wsUsersService = SpringContextHolder.getBean(WsUsersService.class);

    /**
     * 管理一个全局map，保存连接进服务端的通道数量
     */
    private static ConcurrentHashMap<String, ChannelHandlerContext> CONTEXT_MAP = new ConcurrentHashMap();

    /**
     * 有客户端连接服务器会触发此函数
     *
     * @param ctx
     * @author xiongchuan on 2019/4/28 16:10
     * @return: void
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        logger.info("服务端口{}有客户端连上netty服务器",NettyConstants.SERVER_PORT);
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        if (null == insocket){
            logger.info("无效的客户端连接");
            return;
        }
        String clientIp = insocket.getAddress().getHostAddress();
        int clientPort = insocket.getPort();
        //获取连接通道唯一标识
        ChannelId channelId = ctx.channel().id();
        logger.info("[{}][连接]客户端已连上服务器 IP:{} PORT:{} channelId:{}", NettyConstants.SERVER_PORT,clientIp, clientPort, channelId);

        if (!CONTEXT_MAP.containsKey(clientIp)){
            CONTEXT_MAP.put(clientIp, ctx);
        }

        logger.info("[{}] 客户端连接数量:{}", NettyConstants.SERVER_PORT, CONTEXT_MAP.size());
    }

    /**
     * 有客户端终止连接服务器会触发此函数
     *
     * @param ctx
     * @author xiongchuan on 2019/4/28 16:10
     * @return: void
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        logger.info("服务端口{}有客户端断开netty服务器",NettyConstants.SERVER_PORT);
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        if (null == insocket){
            logger.info("无效的客户端断开连接");
            return;
        }
        String clientIp = insocket.getAddress().getHostAddress();
        ChannelId channelId = ctx.channel().id();
        int clientPort = insocket.getPort();
        logger.info("[{}][断开]客户端已断开服务器 IP:{} PORT:{} channelId:{}", NettyConstants.SERVER_PORT, clientIp, clientPort, channelId);

        if (CONTEXT_MAP.containsKey(clientIp)){
            CONTEXT_MAP.remove(clientIp);
        }
        logger.info("[{}] 客户端连接数量:{}", NettyConstants.SERVER_PORT, CONTEXT_MAP.size());
    }

    /**
     * 有客户端发消息会触发此函数
     *
     * @param ctx
     * @author xiongchuan on 2019/4/28 16:10
     * @return: void
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("端口{}发现有客户端发来的消息",NettyConstants.SERVER_PORT);
        String msgStr = msg.toString().trim();
        if (StringUtils.isEmpty(msgStr)){
            return;
        }
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        if (null == insocket){
            logger.info("无效的客户端读连接");
            return;
        }
        String clientIp = insocket.getAddress().getHostAddress();
        ChannelId channelId = ctx.channel().id();
        int clientPort = insocket.getPort();

        logger.info("[{}][报文] IP:{} PORT:{} channelId:{} msg:{}", NettyConstants.SERVER_PORT, clientIp, clientPort, channelId, msg);
    }

    /**
     * 全部被读取完毕触发此事件
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        logger.info("端口{}发现有客户端发来的消息且全部读取完毕", NettyConstants.SERVER_PORT);
        // do something
        channelWrite(ctx, "welcome you to meet my server.");
    }

    /**
     * 服务端给客户端发送消息
     * @param msg       需要发送的消息内容
     * @param ctx       上下文
     * @author xiongchuan on 2019/4/28 16:10
     * @return: void
     */
    public static String channelWrite(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("[{}][发送指令]{}", NettyConstants.SERVER_PORT, msg);
        if (null == msg || msg.equals("")){
            logger.info("指令不能为空");
            return "failed";
        }
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        if (null == insocket){
            logger.info("无效的客户端断开连接");
            return "failed";
        }
        String clientIp = insocket.getAddress().getHostAddress();
        //logger.info("检查渠道是否存在");
        if (!CONTEXT_MAP.containsKey(clientIp)) {
            logger.info("渠道ip:{}不存在,操作忽略", clientIp);
            return "failed";
        }

        ChannelId channelId = ctx.channel().id();
        logger.info("开始给渠道ip:{} 渠道id:{} 发送指令{}",clientIp, channelId, msg);
        //将客户端的信息直接返回写入ctx
        ctx.write(msg);
        //刷新缓存区
        ctx.flush();
        //logger.info("发送指令结束");
        return "success";
    }

    /**
     * 连接超时事件
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        logger.info("[{}][超时]有客户端出现超时", NettyConstants.SERVER_PORT);
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        if (null == insocket){
            logger.info("无效的客户端读连接");
            return;
        }
        String clientIp = insocket.getAddress().getHostAddress();
        ChannelId channelId = ctx.channel().id();
        int clientPort = insocket.getPort();
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                logger.info("客户端读超时 IP:{} PORT:{} channelId:{}", clientIp, clientPort, channelId);
                //ctx.disconnect();
            } else if (event.state() == IdleState.WRITER_IDLE) {
                logger.info("客户端写超时 IP:{} PORT:{} channelId:{}", clientIp, clientPort, channelId);
                //ctx.disconnect();
            } else if (event.state() == IdleState.ALL_IDLE) {
                logger.info("客户端总超时 IP:{} PORT:{} channelId:{}", clientIp, clientPort, channelId);
                //ctx.disconnect();
            }
        }
    }

    /**
     * 发生异常会触发此函数
     *
     * @param ctx
     * @author xiongchuan on 2019/4/28 16:10
     * @return: void
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.info("[{}][异常]有客户端出现异常", NettyConstants.SERVER_PORT);
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        if (null == insocket){
            logger.info("无效的客户端读连接");
            return;
        }
        String clientIp = insocket.getAddress().getHostAddress();
        ChannelId channelId = ctx.channel().id();
        int clientPort = insocket.getPort();
        // TODO 异常时暂时不关闭上下文
        logger.info("[{}][断开]客户端发生了错误,上下文暂时不关闭 IP{} PORT:{} channelId:{}", NettyConstants.SERVER_PORT, clientIp, clientPort, channelId);
    }
}