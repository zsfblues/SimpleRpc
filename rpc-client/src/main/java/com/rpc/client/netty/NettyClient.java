package com.rpc.client.netty;

import com.rpc.common.config.GlobalRunCfg;
import com.rpc.common.domain.RpcPoster;
import com.rpc.common.domain.RpcResponse;
import com.rpc.common.protocol.RpcProtocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2017/5/28.
 *
 * @author zhoushengfan
 */
@Component
public class NettyClient {

    private RpcResponse response;
    private CountDownLatch latch = new CountDownLatch(1);

    @Resource(name = "globalRunCfg")
    private GlobalRunCfg cfg;

    public RpcResponse startUp(String host, int port, RpcPoster poster) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
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
            ChannelFuture future = b.connect(host, port).sync();
            future.channel().writeAndFlush(poster).sync();
            latch.await(cfg.getTimeout(), TimeUnit.SECONDS);
//            if (this.poster != null){
//                future.channel().closeFuture().sync();
//            }
            return response;
        }finally {
            group.shutdownGracefully();
        }
    }

    public class ClientHandler extends SimpleChannelInboundHandler<RpcResponse> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) throws Exception {
            response = msg;
            latch.countDown();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
                throws Exception {
            cause.printStackTrace();
//            ctx.close();
        }
    }
}
