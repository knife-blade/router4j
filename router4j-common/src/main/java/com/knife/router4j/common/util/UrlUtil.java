package com.knife.router4j.common.util;

import com.knife.router4j.common.entity.InstanceInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * url工具
 */
public class UrlUtil {
    @Autowired
    private PathRuleUtil pathRuleUtil;

    /**
     * 根据规则修改URL
     * @param url 原先的url
     * @return 修改后的URL
     */
    public URL modifyWithRule(String url) {
        URL originUrl = null;
        try {
            originUrl = new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        String newUrlString = replaceUrlWithRule(originUrl);

        URL newUrl = null;
        try {
            newUrl = new URL(newUrlString);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return newUrl;
    }

    /**
     * 将地址解析为对象
     * @param instanceAddress 实例地址。例：http://127.0.0.1:8080
     * @return 实例地址对象
     */
    public InstanceInfo parse(String instanceAddress) {
        URL url = null;
        try {
            url = new URL(instanceAddress);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        InstanceInfo instance = new InstanceInfo();
        instance.setProtocol(url.getProtocol());
        instance.setHost(url.getHost());
        instance.setPort(url.getPort());

        return instance;
    }

    /**
     * 使用规则来替换url
     * @param url 原先的url
     * @return 修改后的url
     */
    private String replaceUrlWithRule(URL url) {
        String protocol = url.getProtocol();
        String query = url.getQuery();
        String ref = url.getRef();

        String path = url.getPath();
        InstanceInfo matchedInstanceInfo =
                pathRuleUtil.findMatchedInstanceAddress(path);

        String host = matchedInstanceInfo.getHost();
        int port = matchedInstanceInfo.getPort();

        return protocol + "://" + host + ":"
                + port + path + "?" + query + "#" + ref;
    }
}
