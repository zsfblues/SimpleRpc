package com.rpc.common.service;

import com.rpc.common.domain.RpcPoster;
import com.rpc.common.domain.RpcResponse;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

/**
 * Created on 2017/9/25.
 *
 * @author zhoushengfan
 */
public class RpcCallback {

    public static ConcurrentMap<Long, RpcCallback> callbackMap;
    public static ExecutorService taskPool;

    static {
        callbackMap = new ConcurrentHashMap<>();
        taskPool = Executors.newFixedThreadPool(20);
    }

    private RpcPoster request;
    private RpcResponse response;
    // future lock
    private volatile boolean isDone = false;

    public RpcCallback(RpcPoster request) {
        this.request = request;
    }

    public RpcResponse getResponse() {
        return response;
    }

    //获取结果后立即返回结果
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
            throw new TimeoutException(MessageFormat.format("simple-rpc, request timeout at:{0}, request:{1}", System.currentTimeMillis(), request.toString()));
        }
        return response;
    }
}
