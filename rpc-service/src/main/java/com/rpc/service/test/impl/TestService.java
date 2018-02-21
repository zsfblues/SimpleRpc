package com.rpc.service.test.impl;

import com.rpc.client.service.ITestService;
import com.rpc.common.annotations.RpcService;

/**
 * Created on 2017/6/1.
 *
 * @author zhoushengfan
 */
@RpcService(value = ITestService.class, group = "service1")
public class TestService implements ITestService {
    @Override
    public Integer sub(int x, int y) {
        return x - y;
    }
}
