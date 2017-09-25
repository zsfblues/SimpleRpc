package com.rpc.client.network.netty;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * Created on 2017/9/25.
 *
 * @author zhoushengfan
 */
public class NettyClientPoolFactory extends BasePooledObjectFactory<NettyClient> {
    @Override
    public NettyClient create() throws Exception {
        return new NettyClient();
    }

    @Override
    public PooledObject<NettyClient> wrap(NettyClient nettyClient) {
        return new DefaultPooledObject<>(nettyClient);
    }

    public boolean validateObject(PooledObject<NettyClient> pooledObject) {
        return pooledObject.getObject().validate();
    }

    public void destroyObject(PooledObject<NettyClient> pooledObject) throws Exception {
        NettyClient client = pooledObject.getObject();
        client.close();
    }
}
