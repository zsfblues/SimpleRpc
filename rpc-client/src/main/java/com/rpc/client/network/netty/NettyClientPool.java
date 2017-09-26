package com.rpc.client.network.netty;

import org.apache.commons.pool2.impl.GenericObjectPool;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created on 2017/9/25.
 *
 * @author zhoushengfan
 */
public class NettyClientPool {

    private GenericObjectPool<NettyClient> pool = new GenericObjectPool<>(new NettyClientPoolFactory());

    //根据主机地址划分对象池(不可用服务名来划分)
    public static ConcurrentHashMap<String, NettyClientPool> clientPoolMap = new ConcurrentHashMap<>();

    public static NettyClient getClient(String serverAddr) throws Exception {
        NettyClientPool client = clientPoolMap.get(serverAddr);
        if (client != null){
            return client.pool.borrowObject();
        }
        client = new NettyClientPool();
        clientPoolMap.put(serverAddr, client);

        return client.pool.borrowObject();
    }

    public GenericObjectPool<NettyClient> getPool() {
        return pool;
    }
}
