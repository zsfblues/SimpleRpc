package com.rpc.common.pool;

import com.rpc.common.pool.reject.DelayedRejectedExecutionHandler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created on 2017/10/11.
 *
 * @author zhoushengfan
 */
public class RpcTaskPool {

    private int corePoolSize = 10;
    private int maximumPoolSize = 35;
    private long keepAliveSec = 1;
    private BlockingQueue<Runnable> sPoolWorkQueue = defaultPoolWorkQueue;
    private RejectedExecutionHandler rejectedExecutionHandler = defaultRejectedExecutionHandler;
    private final AtomicReference<ThreadPoolExecutor> executorReference = new AtomicReference<>();
    private ThreadFactory threadFactory;

    private static final RejectedExecutionHandler defaultRejectedExecutionHandler = new DelayedRejectedExecutionHandler(10 * 1000);
    private static final BlockingQueue<Runnable> defaultPoolWorkQueue = new LinkedBlockingQueue<>(10);

    public RpcTaskPool(String threadName) {
        threadFactory = new ThreadFactory() {
            private final AtomicLong mCount = new AtomicLong();

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, threadName + "-" + mCount.incrementAndGet());
            }
        };
    }

    public RpcTaskPool setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
        return this;
    }

    public RpcTaskPool setMaximumPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
        return this;
    }

    public RpcTaskPool setKeepAliveSec(long keepAliveSec) {
        this.keepAliveSec = keepAliveSec;
        return this;
    }

    public RpcTaskPool setRejectedExecutionHandler(RejectedExecutionHandler rejectedExecutionHandler) {
        this.rejectedExecutionHandler = rejectedExecutionHandler;
        return this;
    }

    public ThreadPoolExecutor newExecutor() {
        ThreadPoolExecutor executor;
        if (executorReference.compareAndSet(null, (executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveSec,
                TimeUnit.SECONDS, sPoolWorkQueue, threadFactory, rejectedExecutionHandler)))) {
            return executor;
        } else {
            return executorReference.get();
        }
    }
}
