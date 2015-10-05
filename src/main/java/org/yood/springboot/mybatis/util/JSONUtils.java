package org.yood.springboot.mybatis.util;

import com.google.gson.Gson;

public class JSONUtils {

    private static final Gson GSON = new Gson();

    private JSONUtils() {
    }

    public static String toJSONString(Object object) {
        return GSON.toJson(object);
    }

    public static <T> T toObject(String jsonString, Class<T> objectType) {
        return GSON.fromJson(jsonString, objectType);
    }
}


