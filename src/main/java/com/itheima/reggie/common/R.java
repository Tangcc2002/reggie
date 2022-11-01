package com.itheima.reggie.common;

import lombok.Data;
import java.util.HashMap;
import java.util.Map;

/**
 * 通用返回结果，服务端相应的数据都会封装成此对象
 * @param <T>
 */
@Data
public class R<T> {

    private Integer code; //编码：1成功，0和其它数字为失败

    private String msg; //错误信息

    private T data; //数据

    private Map<String, Object> map = new HashMap<>(); //动态数据

    /**
     * 操作成功时调用此方法响应结果至前端
     * @param object    需要响应的数据
     * @return  返回给前端的数据
     * @param <T>   响应数据的数据类型
     */
    public static <T> R<T> success(T object) {
        R<T> r = new R<>();
        r.data = object;
        r.code = 1;
        return r;
    }

    /**
     * 操作失败时调用此方法响应结果至前端
     * @param msg   需要响应至前端的提示信息
     * @return  返回给前端的提示信息
     */
    public static <T> R<T> error(String msg) {
        R<T> r = new R<>();
        r.msg = msg;
        r.code = 0;
        return r;
    }

    public R<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

}
