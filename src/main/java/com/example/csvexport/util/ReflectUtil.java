package com.example.csvexport.util;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>Project: CSVexport
 * <p>Description:反射工具类
 *
 * @author lizhao 2021/11/28
 */
public class ReflectUtil {

    /**
     * 获取作为参数的Class的对应名称的方法引用
     *
     * @param fieldNameList field名称List
     * @param clz           要获取方法引用的Class
     * @return 方法引用List
     */
    public static List<Method> getMethodList(List<String> fieldNameList, Class<?> clz) {
        //获取方法引用数组
        return fieldNameList.stream()
                //拼接get方法名
                .map(fieldName -> "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1))
                //获取对象的Method方法引用
                .map(getMethodName -> {
                    try {
                        return clz.getMethod(getMethodName);
                    } catch (NoSuchMethodException ex) {
                        ex.printStackTrace();
                    }
                    return null;
                })
                .collect(Collectors.toList());
    }
}
