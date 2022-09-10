package com.knife.router4j.server.common.util;
 
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
 
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
 
public class BeanHelper {
    public static <T> List<T> convert(List<?> sources, Class<T> target) {
        if (CollectionUtils.isEmpty(sources)) {
            return new ArrayList<>();
        }
 
        List<T> targets = new LinkedList<>();
        for (Object source : sources) {
            T t = null;
            try {
                t = target.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
 
            BeanUtils.copyProperties(source, t);
            targets.add(t);
        }
        
        return targets;
    }
 
    public static <T> T convert(Object source, Class<T> target) {
        if (source == null) {
            return null;
        }
 
        T t;
        try {
            t = target.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        BeanUtils.copyProperties(source, t);
        return t;
    }
}