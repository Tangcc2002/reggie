package com.itheima.reggie.common;

/**
 * 基于ThreadLocal的工具类，保存和获取当前用户存储的ID
 */
public class BaseContext {

    private static final ThreadLocal<Long> local = new ThreadLocal<>();

    public static Long get() {
        return local.get();
    }

    public static void set(Long id) {
        local.set(id);
    }
}
