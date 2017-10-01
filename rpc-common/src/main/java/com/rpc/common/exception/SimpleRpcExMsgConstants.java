package com.rpc.common.exception;

/**
 * Created on 2017/9/30.
 *
 * @author zhoushengfan
 */
public class SimpleRpcExMsgConstants {

    private static final int RPC_SERVICE_DEFAULT_ERROR_CODE = 80000;

    //server
    public static final int RPC_SERVICE_REJECT_ERROR_CODE = 80502;
    public static final int RPC_SERVICE_TIMEOUT_ERROR_CODE = 80503;

    //client
    public static final int RPC_SERVICE_NOT_FOUND_ERROR_CODE = 80401;
    public static final int RPC_SERVICE_REQUEST_LENGTH_OUT_OF_LIMIT_ERROR_CODE = 80402;
    public static final int RPC_SERVICE_CONFIG_PARAM_ILLEGAL_CODE = 80403;
    public static final int RPC_UNKNOWN_REGISTRY_CODE = 80404;

    //framework errors
    public static final int FRAMEWORK_INIT_ERROR_CODE = 80104;


    public static final SimpleRpcExMsg RPC_SERVICE_DEFAULT_ERROR = new SimpleRpcExMsg(503, "rpc fails because of unknown exception", RPC_SERVICE_DEFAULT_ERROR_CODE);
    public static final SimpleRpcExMsg RPC_SERVICE_REJECT_ERROR = new SimpleRpcExMsg(503, "rpc request reject", RPC_SERVICE_REJECT_ERROR_CODE);
    public static final SimpleRpcExMsg RPC_SERVICE_TIMEOUT_ERROR = new SimpleRpcExMsg(503, "rpc request timeout", RPC_SERVICE_TIMEOUT_ERROR_CODE);

    public static final SimpleRpcExMsg SERVICE_NOT_FOUND_ERROR = new SimpleRpcExMsg(404, "unknown service", RPC_SERVICE_NOT_FOUND_ERROR_CODE);
    public static final SimpleRpcExMsg SERVICE_REQUEST_LENGTH_OUT_OF_LIMIT_ERROR = new SimpleRpcExMsg(404, "request data too long", RPC_SERVICE_REQUEST_LENGTH_OUT_OF_LIMIT_ERROR_CODE);
    public static final SimpleRpcExMsg RPC_SERVICE_CONFIG_PARAM_ILLEGAL_ERROR = new SimpleRpcExMsg(404, "illegal config params", RPC_SERVICE_CONFIG_PARAM_ILLEGAL_CODE);
    public static final SimpleRpcExMsg RPC_UNKNOWN_REGISTRY_ERROR = new SimpleRpcExMsg(404, "unknown or unsupported registry", RPC_UNKNOWN_REGISTRY_CODE);

    public static final SimpleRpcExMsg FRAMEWORK_INIT_ERROR = new SimpleRpcExMsg(500, "framework init error", FRAMEWORK_INIT_ERROR_CODE);
}
