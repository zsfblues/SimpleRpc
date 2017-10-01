package com.rpc.common.domain;

import com.google.common.base.Splitter;
import com.rpc.common.util.SimpleRpcConstants;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 2017/9/30.
 *
 * @author zhoushengfan
 */
public class URL {

    private String protocol;

    private String ip;

    private int port;

    //请求接口的路由
    private String path;

    private Map<String, String> parameters;

    public URL(String protocol, String ip, int port) {
        this.protocol = protocol;
        this.ip = ip;
        this.port = port;
    }

    public URL(String protocol, String ip, int port, String path, Map<String, String> parameters) {
        this.protocol = protocol;
        this.ip = ip;
        this.port = port;
        this.path = path;
        this.parameters = parameters;
    }

    public static URL toURL(String url){
        if (StringUtils.isBlank(url)){
            return null;
        }
        String protocol = null;
        String ip = null;
        String path = null;
        int port = 0;
        // xx://ip:port/path?params..
        Map<String, String> parameters = new HashMap<>();
        if (url.contains("?")){
            getParamValues(url);
        }
        int inx = url.indexOf("?");
        if (inx > 0){
            url = url.substring(0, inx);
        }
        inx = url.indexOf(SimpleRpcConstants.PROTOCOL_SEPARATOR);
        // xx://ip:port/path
        if (inx > 0){
            protocol = url.substring(0, inx);
            // ip:port/path?params..
            url = url.substring(inx + SimpleRpcConstants.PROTOCOL_SEPARATOR.length());
        }

        inx = url.indexOf(SimpleRpcConstants.PATH_SEPARATOR);
        if (inx > 0) {
            path = url.substring(inx + SimpleRpcConstants.PATH_SEPARATOR.length());
            // ip:port
            url = url.substring(0, inx);
        }

        inx = url.indexOf(SimpleRpcConstants.SERVICE_SEPARATOR);
        if (inx > 0) {
            ip = url.substring(0, inx);
            port = Integer.valueOf(url.substring(inx + SimpleRpcConstants.SERVICE_SEPARATOR.length()));
        }

        return new URL(protocol, ip, port, path, parameters);
    }

    private static Map<String, String> getParamValues(String url){
        Map<String, String> paramMap = new HashMap<>();
        if (StringUtils.isBlank(url)){
            return paramMap;
        }
        String params = url.substring(url.indexOf("?") + 1, url.length());
        return Splitter.on("&").withKeyValueSeparator("=").split(params);
    }

    public String getUrl(){
        return this.toString();
    }

    public String getProtocol() {
        return protocol;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        return protocol + SimpleRpcConstants.PROTOCOL_SEPARATOR + ip + SimpleRpcConstants.SERVICE_SEPARATOR + port;
    }

}
