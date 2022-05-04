package com.knife.router4j.common.util;

import java.net.MalformedURLException;
import java.net.URL;

public class Router4jUrlUtil {
    /**
     * 根据规则修改URL
     */
    public static URL modify(String url) {
        URL originUrl = null;
        try {
            originUrl = new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        String protocol = originUrl.getProtocol();
        String query = originUrl.getQuery();
        String ref = originUrl.getRef();

        String path = originUrl.getPath();
        // todo 通过path与Redis的已有规则进行匹配

        // todo 匹配到后，取出host和port
        String host = "";
        int port = 0;

        String newUrlString = protocol + host + port + path + query + ref;

        URL newUrl = null;
        try {
            newUrl = new URL(newUrlString);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return newUrl;
    }
}
