package com.rpc.common.registry.support.normal;

import com.rpc.common.domain.URL;
import com.rpc.common.exception.SimpleRpcExMsgConstants;
import com.rpc.common.exception.SimpleRpcException;
import com.rpc.common.registry.Registry;
import com.rpc.common.registry.RegistryFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created on 2017/9/30.
 *
 * @author zhoushengfan
 */
public abstract class AbstractRegistryFactory implements RegistryFactory{

    private static final ConcurrentHashMap<String, Registry> registryCenter = new ConcurrentHashMap<>();

    public Registry getRegistry(URL url){
        String registryUrl = url.getUrl();
        synchronized (this){
            Registry registry = registryCenter.get(registryUrl);
            if (registry != null) {
                return registry;
            }
            registry = newRegistry(url);
            if (registry == null){
                throw new SimpleRpcException(String.format("create new registry for [ %s ] fails", url.toString()), SimpleRpcExMsgConstants.FRAMEWORK_INIT_ERROR);
            }
            registryCenter.put(registryUrl, registry);

            return registry;
        }
    }

    protected abstract Registry newRegistry(URL url);
}
