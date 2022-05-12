package com.knife.router4j.server.common.constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface WhiteList {
    List<String> KNIFE4J = Arrays.asList(
            "/doc.html",
            "/swagger-resources",
            "/swagger-resources/configuration",
            "/v3/api-docs",
            "/v2/api-docs",
            "/webjars/**");

    List<String> ALL = new ArrayList<>(KNIFE4J);
}
