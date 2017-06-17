package com.rpc.client.service;


/**
 * Created on 2017/5/26.
 *
 * @author zhoushengfan
 */
public interface IEchoService {
    String hello(String str);
    void test(String content);

    Integer sum(int x, int y);
}
