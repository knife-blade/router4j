package com.suchtool.router4j.server.common.util;

import com.suchtool.router4j.common.common.entity.Router4jPageBO;
import com.suchtool.router4j.common.common.entity.Router4jPageVO;

import java.util.List;

public class PageUtil {
    public static <T> Router4jPageVO<T> toPage(List<T> list, Router4jPageBO router4jPageBO){
        int startIndex = (int) (router4jPageBO.getCurrent() * router4jPageBO.getSize());
        int allSize = list.size();
        int dataSize = allSize - startIndex;
        List<T> listResult = list.subList(startIndex, dataSize - startIndex);

        Router4jPageVO<T> router4jPageVO = new Router4jPageVO<>();
        router4jPageVO.setSize(router4jPageBO.getSize());
        router4jPageVO.setCurrent(router4jPageBO.getCurrent());
        router4jPageVO.setTotal(allSize);
        router4jPageVO.setDataList(listResult);

        return router4jPageVO;
    }
}
