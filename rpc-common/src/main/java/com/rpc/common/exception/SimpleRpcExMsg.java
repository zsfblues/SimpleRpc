package com.rpc.common.exception;

import java.io.Serializable;

/**
 * Created on 2017/9/30.
 *
 * @author zhoushengfan
 */
public class SimpleRpcExMsg implements Serializable{

    private static final long serialVersionUID = -5446347706740935964L;

    private int code;

    private String errorMsg;

    private int status;

    public SimpleRpcExMsg(int code, String errorMsg, int status) {
        this.code = code;
        this.errorMsg = errorMsg;
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
