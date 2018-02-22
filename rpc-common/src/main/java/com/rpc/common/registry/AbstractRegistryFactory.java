package com.rpc.common.registry;

import com.rpc.common.config.GlobalCfgParam;
import com.rpc.common.domain.URL;
import com.rpc.common.exception.SimpleRpcExMsgConstants;
import com.rpc.common.exception.SimpleRpcException;
import com.rpc.common.registry.Registry;
import com.rpc.common.registry.RegistryFactory;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created on 2017/9/30.
 *
 * @author zhoushengfan
 */
public abstract class AbstractRegistryFactory implements RegistryFactory {

    private static final ConcurrentHashMap<String, Registry> registryCenter = new ConcurrentHashMap<>();

    public Registry getRegistry(URL url) {
        String registryUrl = url.getUrl();
        Registry registry = registryCenter.get(registryUrl);
        if (registry != null) {
            return registry;
        }
        registry = newRegistry(url);
        if (registry == null) {
            throw new SimpleRpcException(String.format("create new registry for [ %s ] fails", url.toString()), SimpleRpcExMsgConstants.FRAMEWORK_INIT_ERROR);
        }
        Registry existRegistry = registryCenter.putIfAbsent(registryUrl, registry);
        if (existRegistry == null) {
            return registryCenter.get(registryUrl);
        } else {
            return existRegistry;
        }
    }

    protected CuratorFramework connect(String str) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(GlobalCfgParam.ConnectTimeout.getIntVal(), 3);
        CuratorFramework client = CuratorFrameworkFactory.builder().sessionTimeoutMs(GlobalCfgParam.SessionTimeout.getIntVal()).connectString(str).retryPolicy(retryPolicy).build();
        client.start();
        return client;
    }

    protected abstract Registry newRegistry(URL url);
}
