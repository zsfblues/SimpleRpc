package com.rpc.common.domain.rpcService;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created on 2017/6/2.
 *
 * @author zhoushengfan
 */
public class RpcServiceContainer {
    private Set<String> serviceNameSet = new HashSet<>();
    private Map<String, List<RpcServiceMapping>> rpcServiceMap = new ConcurrentHashMap<>();

    public Set<String> getServiceNameSet() {
        return serviceNameSet;
    }

    public Map<String, List<RpcServiceMapping>> getRpcServiceMap() {
        return rpcServiceMap;
    }
}
