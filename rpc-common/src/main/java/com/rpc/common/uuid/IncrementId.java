package com.rpc.common.uuid;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created on 2017/5/29.
 *
 * @author zhoushengfan
 */
public class IncrementId {
    private static final AtomicLong incrementLong = new AtomicLong(1L);

    public static Long next(){
        return incrementLong.getAndIncrement();
    }
}
