package com.knife.router4j.common.util;

import com.knife.router4j.common.entity.InstanceAddress;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * url工具
 */
public class UrlUtil {
    @Autowired
    private PathRule pathRule;

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
     * @return 修改好的url
     */
    private String replaceUrlWithRule(URL url) {
        String protocol = url.getProtocol();
        String query = url.getQuery();
        String ref = url.getRef();

        String path = url.getPath();
        InstanceAddress matchedInstanceAddress =
                pathRule.findMatchedInstanceAddress(path);

        String host = matchedInstanceAddress.getHost();
        int port = matchedInstanceAddress.getPort();

        return protocol + "://" + host + ":"
                + port + path + "?" + query + "#" + ref;
    }
}
