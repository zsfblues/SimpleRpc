package com.rpc.common.util;

import com.rpc.common.config.GlobalCfgParam;
import com.rpc.common.domain.URL;
import com.rpc.common.exception.SimpleRpcExMsgConstants;
import com.rpc.common.exception.SimpleRpcException;
import com.rpc.common.registry.Registry;
import com.rpc.common.registry.RegistryFactory;
import com.rpc.common.registry.support.local.LocalRegistryFactory;
import com.rpc.common.registry.support.zookeeper.ZookeeperRegistryFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 2017/10/1.
 *
 * @author zhoushengfan
 */
public class RegistryUtil {

    private static final Holder<Map<String, Class<? extends RegistryFactory>>> registryClasses = new Holder<>();

    static {
        registryClasses.set(new HashMap<>());
        registryClasses.get().put("zookeeper", ZookeeperRegistryFactory.class);
        registryClasses.get().put("local", LocalRegistryFactory.class);
    }

    public static Registry resolveRegistryType(URL url) throws IllegalAccessException, InstantiationException {
        if (url == null){
            String registryUrl = GlobalCfgParam.RegistryAddress.getStrVal();
            url = URL.toURL(registryUrl);
        }
        String protocol = url.getProtocol();
        Class<? extends RegistryFactory> registryType = registryClasses.get().get(protocol);
        if (registryType == null) {
            throw new SimpleRpcException("Unsupported registry type", SimpleRpcExMsgConstants.RPC_UNKNOWN_REGISTRY_ERROR);
        }

        RegistryFactory factory = registryType.newInstance();

        return factory.getRegistry(url);
    }
}
