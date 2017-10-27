package com.rpc.common.domain;

/**
 * Created on 2017/5/29.
 *
 * @author zhoushengfan
 */
public class ServiceHost {
    private String ip;
    private String port;

    public ServiceHost() {
    }

    public ServiceHost(String ip, String port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
