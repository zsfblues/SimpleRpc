package com.rpc.common.registry;

import com.rpc.common.domain.ServiceHost;
import com.rpc.common.domain.URL;
import com.rpc.common.domain.rpcService.RpcServiceContainer;
import com.rpc.common.util.ConcurrentHashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

/**
 * Created on 2017/9/30.
 *
 * @author zhoushengfan
 */
public abstract class AbstractRegistry implements Registry {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractRegistry.class);

    private Set<URL> registeredServiceUrls = new ConcurrentHashSet<>();

    public void unregister(URL url, RpcServiceContainer container){
        if (url == null){
            LOG.error("unregistry url is not allowed to be null");
            return;
        }
        LOG.info("unregister url : {}", url.toString());
        registeredServiceUrls.remove(url);
        doUnregister(url, container);
    }

    public void register(URL url, RpcServiceContainer container){
        if (url == null){
            LOG.error("registry url is not allowed to be null");
            return;
        }
        LOG.info("register url : {}", url.toString());
        registeredServiceUrls.add(url);
        doRegister(url, container);
    }

    @Override
    public URL getUrl() {
        return null;
    }

    public List<ServiceHost> discoverServices(String name) {
        return null;
    }

    protected abstract void doRegister(URL url, RpcServiceContainer container);

    protected abstract void doUnregister(URL url, RpcServiceContainer container);
}
