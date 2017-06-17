package com.rpc.service.echo.impl;

import com.rpc.common.annotations.RpcService;
import com.rpc.client.service.IEchoService;


/**
 * Created on 2017/5/26.
 *
 * @author zhoushengfan
 */
@RpcService(IEchoService.class)
public class EchoService implements IEchoService {
    @Override
    public String hello(String str) {
        return "Hello " + str + "...";
    }

    @Override
    public void test(String content) {
        System.out.println(content);
    }

    @Override
    public Integer sum(int x, int y) {
        return x + y;
    }
}
