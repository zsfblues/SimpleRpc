package com.rpc.common.pool.reject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 *
 * @author zhoushengfan
 */
public class DelayedRejectedExecutionHandler implements RejectedExecutionHandler {

    private static Logger logger = LoggerFactory.getLogger(DelayedRejectedExecutionHandler.class);

    private int delay;

    public DelayedRejectedExecutionHandler(int delay) {
        this.delay = delay;
    }

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            logger.error(String.format("Task %s delay execute fail from %s",
                    r.toString(), e));
            throw new RejectedExecutionException();
        }
        if (!executor.isShutdown()) {
            executor.execute(r);
        }
    }
}
