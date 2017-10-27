package com.rpc.common.registry.support.local;

import com.rpc.common.domain.ServiceHost;
import com.rpc.common.domain.URL;
import com.rpc.common.domain.rpcService.RpcServiceContainer;
import com.rpc.common.registry.support.normal.AbstractRegistry;
import org.apache.curator.framework.CuratorFramework;

import java.util.List;

/**
 * Created on 2017/10/12.
 *
 * @author zhoushengfan
 */
public class LocalRegistry extends AbstractRegistry {

    private RpcServiceContainer container;

    @Override
    protected void doRegister(URL url, RpcServiceContainer container) {
        this.container = container;
    }

    @Override
    protected void doUnregister(URL url, RpcServiceContainer container) {

    }

    @Override
    public URL getUrl() {
        return null;
    }

    @Override
    public List<ServiceHost> discoverServices(String name) {
        return null;
    }
}
