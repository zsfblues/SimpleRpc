package com.rpc.common.domain.rpcService;

/**
 * Created on 2017/6/2.
 *
 * @author zhoushengfan
 */
public class RpcServiceBean {
    private Object object;
    private String group;

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
