package com.rpc.common.registry.support.zookeeper;

import com.rpc.common.config.GlobalCfgParam;
import com.rpc.common.domain.URL;
import com.rpc.common.exception.SimpleRpcExMsgConstants;
import com.rpc.common.exception.SimpleRpcException;
import com.rpc.common.registry.Registry;
import com.rpc.common.registry.support.normal.AbstractRegistryFactory;
import com.rpc.common.util.SimpleRpcConstants;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;


/**
 * Created on 2017/9/30.
 *
 * @author zhoushengfan
 */
public class ZookeeperRegistryFactory extends AbstractRegistryFactory {

    @Override
    protected Registry newRegistry(URL url) {
        String registryUrl = url.toString();
        int index = registryUrl.indexOf(SimpleRpcConstants.ZK_DEFAULT_PREFIX);
        if (index == -1){
            throw new SimpleRpcException("illegal zookeeper protocol", SimpleRpcExMsgConstants.RPC_SERVICE_CONFIG_PARAM_ILLEGAL_ERROR);
        }
        registryUrl = registryUrl.substring(index + SimpleRpcConstants.ZK_DEFAULT_PREFIX.length() + SimpleRpcConstants.PROTOCOL_SEPARATOR.length());
        CuratorFramework curator;
        try {
            curator = connect(registryUrl);
        }catch (Exception ex){
            throw new SimpleRpcException("connect zookeeper fails", ex);
        }

        return new ZookeeperRegistry(curator);
    }

    private CuratorFramework connect(String str){
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .sessionTimeoutMs(GlobalCfgParam.SessionTimeout.getIntVal())
                .connectString(str)
                .retryPolicy(retryPolicy)
                .build();
        client.start();
        return client;
    }
}
