package start;

import com.rpc.common.annotations.RpcService;
import com.rpc.common.config.GlobalCfgParam;
import com.rpc.common.domain.URL;
import com.rpc.common.domain.rpcService.RpcServiceBean;
import com.rpc.common.domain.rpcService.RpcServiceContainer;
import com.rpc.common.domain.rpcService.RpcServiceMapping;
import com.rpc.common.registry.Registry;
import com.rpc.common.util.RegistryUtil;
import netty.NettyServer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> serviceBeanMap = applicationContext
                .getBeansWithAnnotation(RpcService.class);

        //初始化所有服务接口
        initService(serviceBeanMap);
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        URL url = URL.toURL(GlobalCfgParam.RegistryAddress.getStrVal());
        Registry registry = RegistryUtil.resolveRegistryType(null);
        //注册所有服务
        registry.register(url, rpcServiceContainer);

        //启动netty开始监听来自客户端的请求
        new NettyServer().bind(GlobalCfgParam.NettyPort.getIntVal());
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
}
