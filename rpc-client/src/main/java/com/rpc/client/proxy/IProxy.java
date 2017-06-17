package com.rpc.client.proxy;

/**
 * Created on 2017/6/6.
 *
 * @author zhoushengfan
 */
public interface IProxy {
    <T> T newProxy(Class<T> interfaceClass, final String group);
}
