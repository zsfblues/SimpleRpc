package netty.handlers;


import com.rpc.common.domain.RpcPoster;
import com.rpc.common.domain.RpcResponse;
import com.rpc.common.domain.rpcService.RpcServiceBean;
import com.rpc.common.domain.rpcService.RpcServiceMapping;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import start.RpcServer;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created on 2017/5/26.
 *
 * @author zhoushengfan
 */
public class RequestHandler extends SimpleChannelInboundHandler<RpcPoster> {

    private RpcResponse response = new RpcResponse();
    private static final Logger LOG = LoggerFactory.getLogger(RequestHandler.class);
    /**
     *
     * @param poster 用于解析客户端远程调用的请求数据，以便判断调用哪种服务
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcPoster poster) throws Exception {
        List<RpcServiceMapping> serviceMappings =
                RpcServer.rpcServiceContainer.getRpcServiceMap().get(poster.getClassName());

        //返回结果不能为空，否则response对象将不会进行编解码
        response.setSuccess(Boolean.TRUE);
        for (RpcServiceMapping rpcServiceMapping : serviceMappings){

            RpcServiceBean serviceBean = rpcServiceMapping.getServiceBean();
            String group = serviceBean.getGroup();
            //该远程服务未分组直接匹配，有分组调用时必须传递组名
            if (StringUtils.isEmpty(group) || group.equals(poster.getGroup())){
                try {
                    Class<?> clazz = Class.forName(poster.getClassName());
                    Method target = clazz.getMethod(poster.getMethodName(), poster.getParameterTypes());
                    Object result = target.invoke(serviceBean.getObject(), poster.getParameters());
                    response.setRequestId(poster.getRequestId());
                    response.setResult(result);
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        LOG.error(cause.getMessage(), cause);
        response.setSuccess(Boolean.FALSE);
        response.setThrowable(cause);
        response.setErrorMsg(cause.getMessage());
        cause.printStackTrace();
    }
}
