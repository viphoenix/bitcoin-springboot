package com.phoenix.blockchain.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by chengfeng on 2018/7/12.
 */
public class GsonUtils {

    /** gson */
    private static final Gson GSON = new GsonBuilder().create();

    /**
     * Object转为json
     *
     * @param object 对象
     * @return json
     */
    public static String objectToJson(Object object) {
        return GSON.toJson(object);
    }

    /**
     * json转换为对象
     *
     * @param json json
     * @param clazz
     * @param <T> 对象类型
     * @return 对象实例
     */
    public static <T> T jsonToObject(String json, Class<T> clazz) {
        return GSON.fromJson(json, clazz);
    }

}
