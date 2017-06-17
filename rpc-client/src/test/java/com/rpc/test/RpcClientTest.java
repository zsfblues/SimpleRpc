package com.rpc.test;

import com.rpc.client.proxy.RpcClientProxy;
import com.rpc.client.service.IEchoService;
import com.rpc.client.service.ITestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created on 2017/5/29.
 *
 * @author zhoushengfan
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:client-test-conf.xml"})
public class RpcClientTest {

    @Autowired
    private RpcClientProxy proxy;

    @Test
    public void test(){

        IEchoService echoService = proxy.newProxy(IEchoService.class, null);
        ITestService testService = proxy.newProxy(ITestService.class, "service1");
        ITestService testService2 = proxy.newProxy(ITestService.class, "service2");
//        testService.sub(1, 1);
//        testService2.sub(1, 1);
//        System.out.println(echoService.sum(1,1));
//        System.out.println(testService.sub(1, 1));
//        System.out.println(echoService.hello("zsf"));
    }
}
