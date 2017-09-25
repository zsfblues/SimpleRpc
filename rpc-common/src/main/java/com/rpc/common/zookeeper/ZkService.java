package com.rpc.common.zookeeper;

import com.rpc.common.config.GlobalRunCfg;
import com.rpc.common.domain.rpcService.RpcServiceBean;
import com.rpc.common.domain.ServiceHost;
import com.rpc.common.domain.rpcService.RpcServiceContainer;
import com.rpc.common.ip.IpUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created on 2017/5/26.
 *
 * @author zhoushengfan
 */
@Component
public class ZkService {

    private CuratorFramework curator = null;

    private static final String ZK_ROOT_NODE =  "/simple-rpc";
    private static final String PATH_SEPARATOR = "/";

    @Resource(name = "globalRunCfg")
    private GlobalRunCfg cfg;

    public CuratorFramework connect(String str){
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .sessionTimeoutMs(3000)
                .connectString(str)
                .retryPolicy(retryPolicy)
                .build();
        client.start();
        return client;
    }

    public void register(String connectStr, RpcServiceContainer rpcServiceContainer){
        if (curator == null){
            curator = connect(connectStr);
        }

        try {
            String ip = IpUtil.getIp();

            synchronized (ZK_ROOT_NODE + PATH_SEPARATOR + ip){

                //预置根节点
                if (curator.checkExists().forPath(ZK_ROOT_NODE) == null){
                    curator.create()
                            .withMode(CreateMode.PERSISTENT).forPath(ZK_ROOT_NODE);
                }

                rpcServiceContainer.getServiceNameSet().forEach(k -> {
                    String childPath = ZK_ROOT_NODE + PATH_SEPARATOR + k + PATH_SEPARATOR + ip;
                    try {
                        if (curator.checkExists().forPath(childPath) == null){
                            newNode(childPath);
                        }else {
                            curator.setData().forPath(childPath,
                                    cfg.getNettyAddress().split(":")[1].getBytes());
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

    public List<ServiceHost> discoverServices(String name){
        List<ServiceHost> services = new ArrayList<>();
        if (curator == null){
            curator = connect(cfg.getZkAddress());
        }
        try {
            String serviceNode = ZK_ROOT_NODE + PATH_SEPARATOR + name;
            List<String> registerHostsForService = curator.getChildren().forPath(serviceNode);
            registerHostsForService.forEach(host -> {
                try {
                    ServiceHost serviceHost = new ServiceHost();
                    serviceHost.setIp(host);
                    serviceHost.setPort(new String(curator.getData().forPath(serviceNode + PATH_SEPARATOR + host)));
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
                            cfg.getNettyAddress().split(":")[1].getBytes());
        }
    }
}
