package com.knife.router4j.common.util;

import com.knife.router4j.common.common.entity.InstanceInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
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
                pathRuleUtil.findMatchedInstance(path);

        String host = matchedInstanceInfo.getHost();
        int port = matchedInstanceInfo.getPort();

        String resultUrl = null;
        try {
            URI uri = new URI(protocol, null, host, port, path, query, ref);
            resultUrl = uri.toString();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return resultUrl;
    }
}
