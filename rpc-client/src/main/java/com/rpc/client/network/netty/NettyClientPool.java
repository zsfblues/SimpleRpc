package com.rpc.client.network.netty;

import org.apache.commons.pool2.impl.GenericObjectPool;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created on 2017/9/25.
 *
 * @author zhoushengfan
 */
public class NettyClientPool {

    private GenericObjectPool<NettyClient> pool = new GenericObjectPool<>(new NettyClientPooledObjectFactory());

    // 根据主机地址划分对象池(不可用服务名来划分)
    public static ConcurrentHashMap<String, NettyClientPool> clientPoolMap = new ConcurrentHashMap<>();

    public static NettyClient getClient(String serverAddr) throws Exception {
        NettyClientPool client = clientPoolMap.get(serverAddr);
        if (client != null) {
            return client.pool.borrowObject();
        }
        client = new NettyClientPool();
        NettyClientPool existClientPool = clientPoolMap.putIfAbsent(serverAddr, client);

        if (existClientPool == null) {
            return clientPoolMap.get(serverAddr).pool.borrowObject();
        } else {
            return existClientPool.pool.borrowObject();
        }
    }

    public GenericObjectPool<NettyClient> getPool() {
        return pool;
    }
}
