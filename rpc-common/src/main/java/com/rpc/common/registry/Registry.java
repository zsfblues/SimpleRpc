package com.rpc.common.registry;

import com.rpc.common.domain.ServiceHost;
import com.rpc.common.domain.URL;
import com.rpc.common.domain.rpcService.RpcServiceContainer;

import java.util.List;

/**
 * Created on 2017/9/30.
 *
 * @author zhoushengfan
 */
public interface Registry {
    URL getUrl();

    void unregister(URL url, RpcServiceContainer container);

    void register(URL url, RpcServiceContainer container);

    /**
     *
     * @param name 服务名
     * @return
     */
    List<ServiceHost> discoverServices(String name);
}
