package com.knife.router4j.server.common.util;

import com.knife.router4j.server.common.entity.PageRequest;
import com.knife.router4j.server.common.entity.PageResponse;

import java.util.List;

public class PageUtil {
    public static <T> PageResponse<T> toPage(List<T> list, PageRequest pageRequest){
        int startIndex = (int) (pageRequest.getCurrent() * pageRequest.getSize());
        int allSize = list.size();
        int dataSize = allSize - startIndex;
        List<T> listResult = list.subList(startIndex, dataSize - startIndex);

        PageResponse<T> pageResponse = new PageResponse<>();
        pageResponse.setSize(pageRequest.getSize());
        pageResponse.setCurrent(pageRequest.getCurrent());
        pageResponse.setTotal(allSize);
        pageResponse.setDataList(listResult);

        return pageResponse;
    }
}
