package com.suchtool.router4j.server.common.util;
 
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
 
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
 
public class BeanHelper {
    /**
     * 浅拷贝
     */
    public static <T> T shallowClone(Object source, Class<T> target) {
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

    /**
     * 浅拷贝
     */
    public static <T> List<T> shallowClone(List<?> sources, Class<T> target) {
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

    /**
     * 深拷贝
     */
    public static <T> T deepClone(Object source, Class<T> target) {
        if (source == null) {
            return null;
        }

        String json = JsonUtil.toJson(source);
        return JsonUtil.toObject(json, target);
    }

    /**
     * 深拷贝
     */
    public static <T> List<T> deepClone(List<?> sources, Class<T> target) {
        if (CollectionUtils.isEmpty(sources)) {
            return new ArrayList<>();
        }

        String json = JsonUtil.toJson(sources);
        return JsonUtil.toObjectList(json, new TypeReference<List<T>>() {});
    }
}