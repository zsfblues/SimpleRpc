package com.rpc.common.util;

import com.rpc.common.config.GlobalCfgParam;
import com.rpc.common.domain.URL;
import com.rpc.common.exception.SimpleRpcExMsgConstants;
import com.rpc.common.exception.SimpleRpcException;
import com.rpc.common.registry.Registry;
import com.rpc.common.registry.support.zookeeper.ZookeeperRegistryFactory;

/**
 * Created on 2017/10/1.
 *
 * @author zhoushengfan
 */
public class RegistryUtil {

    public static Registry resolveRegistryType(URL url){
        if (url == null){
            String registryUrl = GlobalCfgParam.RegistryAddress.getStrVal();
            url = URL.toURL(registryUrl);
        }
        String protocol = url.getProtocol();
        Registry registry = null;
        switch (protocol){
            case SimpleRpcConstants.ZK_DEFAULT_PREFIX:
                registry = new ZookeeperRegistryFactory().getRegistry(url);
                break;
            default:
                throw new SimpleRpcException("Unsupported registry type", SimpleRpcExMsgConstants.RPC_UNKNOWN_REGISTRY_ERROR);
        }

        return registry;
    }
}
