package com.suchtool.router4j.server.common.util;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

/**
 * 属性工具
 */
public class PropertyUtil {
    /**
     * 获得值为null的属性名
     */
    public static String[] getNullPropertyNames(Object source) {
        BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();
 
        Set<String> emptyNames = new HashSet<>();
 
        for (PropertyDescriptor pd : pds) {
            //check if value of this property is null then add it to the collection
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null){
                emptyNames.add(pd.getName());
            }
        }
 
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}