package com.rpc.common.config;

/**
 * Created on 2017/6/1.
 *
 * @author zhoushengfan
 */
public class GlobalRunCfg {
    private String nettyAddress;
    private String zkAddress;
    private Integer timeout;

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public String getNettyAddress() {
        return nettyAddress;
    }

    public void setNettyAddress(String nettyAddress) {
        this.nettyAddress = nettyAddress;
    }

    public String getZkAddress() {
        return zkAddress;
    }

    public void setZkAddress(String zkAddress) {
        this.zkAddress = zkAddress;
    }
}
