package com.suchtool.router4j.common.common.util;

import com.suchtool.router4j.common.common.entity.Router4jPageBO;
import com.suchtool.router4j.common.common.entity.Router4jPageVO;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class Router4jPageUtil {
    private static long startPageNo = 1;

    public static <T> Router4jPageVO<T> toPage(List<T> list, Router4jPageBO router4jPageBO) {
        Router4jPageVO<T> router4jPageVO = new Router4jPageVO<>();
        router4jPageVO.setSize(router4jPageBO.getPageSize());
        router4jPageVO.setCurrent(router4jPageBO.getPageNo());

        int allSize = 0;
        List<T> listResult = null;

        if (!CollectionUtils.isEmpty(list)) {
            int startIndex = (int) ((router4jPageBO.getPageNo() - startPageNo)
                    * router4jPageBO.getPageSize());
            allSize = list.size();
            int dataSize = allSize - startIndex;
            listResult = list.subList(startIndex, dataSize - startIndex);
        }

        router4jPageVO.setTotal(allSize);
        router4jPageVO.setDataList(listResult);

        return router4jPageVO;
    }
}
