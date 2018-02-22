package com.rpc.common.service;

import com.rpc.common.domain.RpcPoster;
import com.rpc.common.domain.RpcResponse;
import com.rpc.common.pool.RpcTaskPool;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeoutException;

/**
 * Created on 2017/9/25.
 *
 * @author zhoushengfan
 */
public class RpcCallback {

    public static ConcurrentMap<Long, RpcCallback> callbackMap;
    public static ThreadPoolExecutor taskPool;

    static {
        callbackMap = new ConcurrentHashMap<>();
        taskPool = new RpcTaskPool("taskPoolThread").newExecutor();
    }

    private RpcPoster request;
    private RpcResponse response;

    private volatile boolean isDone = false;

    public RpcCallback(RpcPoster request) {
        this.request = request;
    }

    public RpcResponse getResponse() {
        return response;
    }

    // 获取结果后立即返回结果
    public void setResponse(RpcResponse response) {
        this.response = response;
        // notify future lock
        synchronized (this) {
            isDone = true;
            this.notifyAll();
        }
    }

    public RpcResponse get(long timeoutMillis) throws InterruptedException, TimeoutException{
        if (!isDone) {
            synchronized (this) {
                try {
                    this.wait(timeoutMillis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        }

        if (!isDone) {
            throw new TimeoutException(String.format("simple-rpc --------> request timeout at: %s, time limit:%s, request: %s",
                    System.currentTimeMillis(), timeoutMillis, request.toString()));
        }
        return response;
    }
}
