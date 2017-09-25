package com.rpc.client.network.netty.handler;

import com.rpc.common.domain.RpcResponse;
import com.rpc.common.service.RpcCallback;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created on 2017/9/25.
 *
 * @author zhoushengfan
 */
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