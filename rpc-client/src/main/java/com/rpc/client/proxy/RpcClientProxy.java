package com.rpc.client.proxy;

import com.rpc.client.network.netty.NettyClient;
import com.rpc.client.network.netty.NettyClientPool;
import com.rpc.common.domain.RpcPoster;
import com.rpc.common.domain.ServiceHost;
import com.rpc.common.uuid.IncrementId;
import com.rpc.common.zookeeper.ZkService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Random;

/**
 * Created on 2017/5/27.
 *
 * @author zhoushengfan
 */
@Component
public class RpcClientProxy implements IProxy {

    @Resource
    private ZkService zkService;

    /**
     * @param interfaceClass 远程调用的接口类型
     * @param group          分组名，用于区分同一个接口的不同实现类
     */
    @SuppressWarnings("unchecked")
    public <T> T newProxy(Class<T> interfaceClass, final String group) {
        checkInterface(interfaceClass);
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass},
            (proxy, method, args) -> {
                RpcPoster poster = new RpcPoster();
                poster.setRequestId(IncrementId.next());
                poster.setClassName(method.getDeclaringClass().getName());
                poster.setMethodName(method.getName());
                poster.setParameters(args);
                poster.setParameterTypes(method.getParameterTypes());
                poster.setGroup(group);

                List<ServiceHost> services = zkService.discoverServices(poster.getClassName());
                if (services.isEmpty()) {
                    throw new IllegalStateException("not exist service: " + poster.getClassName());
                }
                //在zookeeper中随机选择一台可以提供服务的远程主机
                ServiceHost remoteHost = services.get(new Random().nextInt(services.size()));
                NettyClient nettyClient = NettyClientPool.getClient(remoteHost.getIp());

                return nettyClient.request(poster, remoteHost);

            });
    }

    private void checkInterface(Class<?> clazz) {
        if (!clazz.isInterface()) throw new RuntimeException(clazz.getName() + " is not an interface");
    }
}
