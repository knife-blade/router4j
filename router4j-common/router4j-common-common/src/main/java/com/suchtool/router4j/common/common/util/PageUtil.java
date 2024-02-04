package com.suchtool.router4j.common.common.util;

import com.suchtool.router4j.common.common.entity.Router4jPageBO;
import com.suchtool.router4j.common.common.entity.Router4jPageVO;

import java.util.List;

public class PageUtil {
    public static <T> Router4jPageVO<T> toPage(List<T> list, Router4jPageBO pageBO){
        int startIndex = (int) (pageBO.getCurrent() * pageBO.getSize());
        int allSize = list.size();
        int dataSize = allSize - startIndex;
        List<T> listResult = list.subList(startIndex, dataSize - startIndex);

        Router4jPageVO<T> pageVO = new Router4jPageVO<>();
        pageVO.setSize(pageBO.getSize());
        pageVO.setCurrent(pageBO.getCurrent());
        pageVO.setTotal(allSize);
        pageVO.setDataList(listResult);

        return pageVO;
    }
}
