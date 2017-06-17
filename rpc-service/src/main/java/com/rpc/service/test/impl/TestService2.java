package com.rpc.service.test.impl;

import com.rpc.client.service.ITestService;
import com.rpc.common.annotations.RpcService;

/**
 * Created on 2017/6/2.
 *
 * @author zhoushengfan
 */
@RpcService(value = ITestService.class, group = "service2")
public class TestService2 implements ITestService{
    @Override
    public Integer sub(int x, int y) {
        return 2 * x - y;
    }
}
