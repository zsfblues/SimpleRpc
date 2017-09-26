package com.rpc.common.ip;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 2017/9/24.
 *
 * @author zhoushengfan
 */
public class IpUtil {

    private static final String SELF_CONNECTIVITY_IP = "127.0.0.1";
    private static final String OUTSIDE_IP_WEBSITE = "http://www.taobao.com/help/getip.php";

    public static String getIp() throws IOException {
        String ip = getLocalIp();
        if (SELF_CONNECTIVITY_IP.equals(ip)){
            ip = getOutsideIp();
        }

        return ip;
    }

    private static String getLocalIp() throws UnknownHostException, SocketException {

        Enumeration netInterfaces = NetworkInterface.getNetworkInterfaces();
        InetAddress net0;
        String localIp = null;
        while (netInterfaces.hasMoreElements()) {
            NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
            net0 = ni.getInetAddresses().nextElement();
            if (!net0.isSiteLocalAddress() && !net0.isLoopbackAddress() && !net0.getHostAddress().contains(":")) {
                localIp = net0.getHostAddress();
                break;
            }
        }

        if (localIp == null){
            localIp = Inet4Address.getLocalHost().getHostAddress();
        }

        return localIp;
    }

    private static String getOutsideIp() throws IOException {
        InputStream inputStream = null;
        try {
            URL url = new URL(OUTSIDE_IP_WEBSITE);
            URLConnection urlConn = url.openConnection();
            inputStream = urlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder content = new StringBuilder();
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                content.append(str);
            }
            Matcher m = Pattern.compile("(?<=\\()(.+?)(?=\\))").matcher(content.toString());
            String re = null;
            if (m.find()){
                re = m.group();
            }
            JSONObject object = JSON.parseObject(re);

            return object.getString("ip");
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }
}
