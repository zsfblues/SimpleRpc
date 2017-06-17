package start;

import com.rpc.common.annotations.RpcService;
import com.rpc.common.config.GlobalRunCfg;
import com.rpc.common.domain.rpcService.RpcServiceBean;
import com.rpc.common.domain.rpcService.RpcServiceContainer;
import com.rpc.common.domain.rpcService.RpcServiceMapping;
import netty.NettyServer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import com.rpc.common.zookeeper.ZkService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 2017/5/26.
 *
 * @author zhoushengfan
 */
@Component
public class RpcServer implements ApplicationContextAware, InitializingBean{

    public static final RpcServiceContainer rpcServiceContainer = new RpcServiceContainer();

    @Resource
    private ZkService zkService;

    @Resource(name = "globalRunCfg")
    private GlobalRunCfg cfg;

    private static final String ZK_ADDRESS = "zkAddress";
    private static final String NETTY_PORT = "nettyPort";

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> serviceBeanMap = applicationContext
                .getBeansWithAnnotation(RpcService.class);

        //初始化所有服务接口
        initService(serviceBeanMap);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, String> properties = resolveProperties();

        //向zookeeper注册所有服务
        zkService.register(properties.get(ZK_ADDRESS), rpcServiceContainer);

        //启动netty开始监听来自客户端的请求
        new NettyServer().bind(Integer.parseInt(properties.get(NETTY_PORT)));
    }

    private void initService(Map<String, Object> serviceBeanMap){
        serviceBeanMap.values().forEach((v) -> {
            RpcService serviceAnnotation = v.getClass().getAnnotation(RpcService.class);

            RpcServiceBean serviceBean = new RpcServiceBean();
            serviceBean.setGroup(serviceAnnotation.group());
            serviceBean.setObject(v);

            String valueName = serviceAnnotation.value().getName();
            RpcServiceMapping serviceMapping = new RpcServiceMapping();
            serviceMapping.setServiceBean(serviceBean);
            serviceMapping.setServiceName(valueName);

            rpcServiceContainer.getServiceNameSet().add(valueName);
            List<RpcServiceMapping> mappings = rpcServiceContainer.getRpcServiceMap().get(valueName);
            if (mappings == null){
                mappings = new ArrayList<>();
            }
            mappings.add(serviceMapping);
            rpcServiceContainer.getRpcServiceMap().put(valueName, mappings);
        });
    }

    private Map<String, String> resolveProperties(){
        Map<String, String> properties = new HashMap<>();
        String[] nettyAddress = cfg.getNettyAddress().split(":");
        properties.put(NETTY_PORT, nettyAddress[1]);
        properties.put(ZK_ADDRESS, cfg.getZkAddress());

        return properties;
    }
}
