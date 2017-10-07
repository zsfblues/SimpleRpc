package com.rpc.common.registry.support.zookeeper;

import com.rpc.common.config.GlobalCfgParam;
import com.rpc.common.domain.ServiceHost;
import com.rpc.common.domain.URL;
import com.rpc.common.domain.rpcService.RpcServiceContainer;
import com.rpc.common.registry.support.normal.AbstractRegistry;
import com.rpc.common.util.SimpleRpcConstants;
import com.rpc.common.util.ip.IpUtil;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017/9/30.
 *
 * @author zhoushengfan
 */

public class ZookeeperRegistry extends AbstractRegistry {

    private CuratorFramework curator;
    private static final String ZK_ROOT_NODE =  "/simple-rpc";

    public ZookeeperRegistry(CuratorFramework curator) {
        this.curator = curator;
    }

    @Override
    protected void doRegister(URL url, RpcServiceContainer rpcServiceContainer) {
        try {
            String ip = IpUtil.getIp();

            synchronized (ZK_ROOT_NODE + SimpleRpcConstants.PATH_SEPARATOR + ip){

                //预置根节点
                if (curator.checkExists().forPath(ZK_ROOT_NODE) == null){
                    curator.create()
                            .withMode(CreateMode.PERSISTENT).forPath(ZK_ROOT_NODE);
                }

                rpcServiceContainer.getServiceNameSet().forEach(k -> {
                    String childPath = ZK_ROOT_NODE + SimpleRpcConstants.PATH_SEPARATOR + k + SimpleRpcConstants.PATH_SEPARATOR + ip;
                    try {
                        if (curator.checkExists().forPath(childPath) == null){
                            newNode(childPath);
                        }else {
                            curator.setData().forPath(childPath,
                                    GlobalCfgParam.NettyPort.getStrVal().getBytes());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doUnregister(URL url, RpcServiceContainer container) {

    }

    public List<ServiceHost> discoverServices(String name){
        List<ServiceHost> services = new ArrayList<>();

        try {
            String serviceNode = ZK_ROOT_NODE + SimpleRpcConstants.PATH_SEPARATOR + name;
            List<String> registerHostsForService = curator.getChildren().forPath(serviceNode);
            registerHostsForService.forEach(host -> {
                try {
                    ServiceHost serviceHost = new ServiceHost();
                    serviceHost.setIp(host);
                    serviceHost.setPort(new String(curator.getData().forPath(serviceNode + SimpleRpcConstants.PATH_SEPARATOR + host)));
                    services.add(serviceHost);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return services;
    }

    private void newNode(String childPath) throws Exception {
        if (curator != null){
            curator.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(childPath,
                            GlobalCfgParam.NettyPort.getStrVal().getBytes());
        }
    }

    @Override
    public URL getUrl() {
        return null;
    }
}
