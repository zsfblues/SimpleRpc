package com.rpc.client.network.netty;

import com.rpc.client.network.netty.handler.ClientHandler;
import com.rpc.common.config.GlobalCfgParam;
import com.rpc.common.domain.RpcPoster;
import com.rpc.common.domain.RpcResponse;
import com.rpc.common.domain.ServiceHost;
import com.rpc.common.protocol.RpcProtocol;
import com.rpc.common.service.RpcCallback;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created on 2017/5/28.
 *
 * @author zhoushengfan
 */
public class NettyClient {

    private Channel channel;

    private static Logger logger = LoggerFactory.getLogger(NettyClient.class);

    public Object request(RpcPoster poster, ServiceHost remoteHost) throws Exception {

        RpcCallback callback = new RpcCallback(poster);
        RpcCallback.callbackMap.put(poster.getRequestId(), callback);

        RpcCallback.taskPool.execute(() -> {
            try {
                startUp(remoteHost.getIp(), Integer.parseInt(remoteHost.getPort()));
                send(poster);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        try {

            RpcResponse response = callback.get(GlobalCfgParam.RequestTimeout.getIntVal());

            return response.getResult();
        } catch (Exception ex) {
            logger.error(ex.toString());
            throw ex;
        } finally {
            RpcCallback.callbackMap.remove(poster.getRequestId());
            NettyClientPool attachedClientPool =  NettyClientPool.clientPoolMap.get(remoteHost.getIp());
            attachedClientPool.getPool().returnObject(this);
        }
    }

    public boolean validate(){
        return this.channel.isActive();
    }

    public void close() {
        if (this.channel != null && this.channel.isOpen()) {
            this.channel.close();
        }
    }

    private void startUp(String host, int port) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, false)
                .handler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel ch) throws Exception {

                        ChannelPipeline cp = ch.pipeline();
                        // 加入编解码类
                        RpcProtocol protocol = new RpcProtocol();
                        cp.addLast(protocol.new RpcPosterEncoder(RpcPoster.class));
                        cp.addLast(protocol.new RpcPosterDecoder(RpcResponse.class));
                        cp.addLast(new ClientHandler());
                    }
                });
        this.channel = b.connect(host, port).sync().channel();

    }

    private void send(RpcPoster poster) throws InterruptedException {
        this.channel.writeAndFlush(poster).sync();
    }

}
