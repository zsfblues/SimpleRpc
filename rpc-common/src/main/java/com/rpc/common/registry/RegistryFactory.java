package com.rpc.common.registry;

import com.rpc.common.domain.URL;

/**
 * Created on 2017/9/30.
 *
 * @author zhoushengfan
 */
public interface RegistryFactory {

    Registry getRegistry(URL url);
}
