package com.rpc.common.domain;

/**
 * Created on 2017/5/31.
 *
 * @author zhoushengfan
 */
public class RpcResponse {
    private Object result;
    private String errorMsg;
    private Throwable throwable;
    private Boolean isSuccess;

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }
}
