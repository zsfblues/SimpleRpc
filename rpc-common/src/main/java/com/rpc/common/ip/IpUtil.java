package com.rpc.common.ip;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 2017/9/24.
 *
 * @author zhoushengfan
 */
public class IpUtil {

    public static String getIp() throws IOException {
        InputStream inputStream = null;
        try {
            URL url = new URL("http://www.taobao.com/help/getip.php");
            URLConnection urlConn = url.openConnection();
            inputStream = urlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder webContent = new StringBuilder();
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                webContent.append(str);
            }
            Matcher m = Pattern.compile("(?<=\\()(.+?)(?=\\))").matcher(webContent.toString());
            String re = null;
            if (m.find()){
                re = m.group();
            }
            JSONObject object = JSON.parseObject(re);
            String ip = object.getString("ip");
            if (StringUtils.isBlank(ip)){
                ip = Inet4Address.getLocalHost().getHostAddress();
            }
            return ip;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }
}
