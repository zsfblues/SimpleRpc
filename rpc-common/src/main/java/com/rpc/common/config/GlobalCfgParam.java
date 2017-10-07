package com.rpc.common.config;

import com.rpc.common.util.SimpleRpcConstants;

/**
 * Created on 2017/9/30.
 * 提供一些默认配置
 * @author zhoushengfan
 */
public enum GlobalCfgParam {
    //毫秒
    ConnectTimeout("connectTimeout", 1000),
    SessionTimeout("sessionTimeout", 3000),
    RequestTimeout("requestTimeout", 5000),

    //全局配置
    ApplicationName("applicationName", SimpleRpcConstants.APPLICATION_NAME),
    RegistryAddress("registryAddress", "zookeeper://116.196.101.52:2181"),
    NettyPort("nettyPort", 8000);

    private String name;
    private int intVal;
    private String strVal;

    GlobalCfgParam(String name, String strVal) {
        this.name = name;
        this.strVal = strVal;
    }


    GlobalCfgParam(String name, int intVal) {
        this.name = name;
        this.intVal = intVal;
        this.strVal = String.valueOf(intVal);
    }

    public String getName() {
        return name;
    }

    public int getIntVal() {
        return intVal;
    }

    public String getStrVal() {
        return strVal;
    }
}
