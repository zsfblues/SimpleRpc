# SimpleRpc

一个基于Spring，Netty和Zookeeper的简易版RPC，支持服务分组 对要实现远程调用的服务加上RpcService注解，在电脑上装上Zookeeper并启动，
将新服务注册后可在rpc-client模块rpc-client\src\main\java\com\rpc\client\service中使用接口来进行 远程调用
