package com.rpc.client.netty;

import com.rpc.common.domain.RpcPoster;
import com.rpc.common.domain.RpcResponse;
import com.rpc.common.protocol.RpcProtocol;
import com.rpc.common.service.RpcCallback;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.stereotype.Component;


/**
 * Created on 2017/5/28.
 *
 * @author zhoushengfan
 */
@Component
public class NettyClient {

    private Channel channel;

    public void startUp(String host, int port, RpcPoster poster) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, false)
                .handler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel ch) throws Exception {

                        ChannelPipeline cp = ch.pipeline();
                        //加入编解码类
                        RpcProtocol protocol = new RpcProtocol();
                        cp.addLast(protocol.new RpcPosterEncoder(RpcPoster.class));
                        cp.addLast(protocol.new RpcPosterDecoder(RpcResponse.class));
                        cp.addLast(new ClientHandler());
                    }
                });
        this.channel = b.connect(host, port).sync().channel();

    }

    public void send(RpcPoster poster) throws InterruptedException {
        this.channel.writeAndFlush(poster).sync();
    }

    public void close() {
        if (this.channel != null && this.channel.isOpen()) {
            this.channel.close();
        }
    }

    public class ClientHandler extends SimpleChannelInboundHandler<RpcResponse> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, RpcResponse response) throws Exception {
            RpcCallback future = RpcCallback.callbackMap.get(response.getRequestId());
            future.setResponse(response);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
                throws Exception {
            cause.printStackTrace();
//            ctx.close();
        }
    }
}
