package com.rpc.common.domain.rpcService;

/**
 * Created on 2017/6/2.
 *
 * @author zhoushengfan
 */
public class RpcServiceMapping {
    private String serviceName;
    private RpcServiceBean serviceBean;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public RpcServiceBean getServiceBean() {
        return serviceBean;
    }

    public void setServiceBean(RpcServiceBean serviceBean) {
        this.serviceBean = serviceBean;
    }
}
