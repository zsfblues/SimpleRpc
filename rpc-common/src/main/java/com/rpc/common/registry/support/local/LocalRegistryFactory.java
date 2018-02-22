package com.rpc.common.registry.support.local;

import com.rpc.common.domain.URL;
import com.rpc.common.registry.Registry;
import com.rpc.common.registry.AbstractRegistryFactory;

/**
 * Created on 2017/10/10.
 *
 * @author zhoushengfan
 */
public class LocalRegistryFactory extends AbstractRegistryFactory {

    @Override
    protected Registry newRegistry(URL url) {

//        String registryUrl = url.toString();
//        int index = registryUrl.indexOf(SimpleRpcConstants.LOCAL_DEFAULT_PREFIX);
//        if (index == -1){
//            throw new SimpleRpcException("illegal zookeeper protocol", SimpleRpcExMsgConstants.RPC_SERVICE_CONFIG_PARAM_ILLEGAL_ERROR);
//        }
//        registryUrl = registryUrl.substring(index + SimpleRpcConstants.LOCAL_DEFAULT_PREFIX.length() + SimpleRpcConstants.PROTOCOL_SEPARATOR.length());
//        CuratorFramework curator;
//        try {
//            curator = connect(registryUrl);
//        }catch (Exception ex){
//            throw new SimpleRpcException("connect zookeeper fails", ex);
//        }

        return new LocalRegistry();
    }
}
