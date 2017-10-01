package com.rpc.common.exception;

/**
 * Created on 2017/9/30.
 *
 * @author zhoushengfan
 */
public class SimpleRpcException extends RuntimeException {


    private static final long serialVersionUID = -7296126646506574250L;

    private  SimpleRpcExMsg simpleRpcExMsg = SimpleRpcExMsgConstants.RPC_SERVICE_DEFAULT_ERROR;

    public SimpleRpcException(){
        super();
    }

    public SimpleRpcException(Throwable cause){
        super(cause);
    }

    public SimpleRpcException(String message, Throwable cause){
        super(message, cause);
    }

    public SimpleRpcException(String message, SimpleRpcExMsg simpleRpcExMsg){
        super(message);
        this.simpleRpcExMsg = simpleRpcExMsg;
    }

    //新增一个对象作为异常的扩展对象
    public SimpleRpcException(String message, Throwable cause, SimpleRpcExMsg simpleRpcExMsg){
        super(message, cause);
        this.simpleRpcExMsg = simpleRpcExMsg;
    }
}
