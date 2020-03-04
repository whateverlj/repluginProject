package com.example.commonlib.retrofit.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

/**
 * Double的Json转换适配器，主要解决字符串为空时转Double的异常
 */
public class DoubleTypeAdapter implements JsonSerializer<Double>, JsonDeserializer<Double> {

    /**
     * 如果字符串为空，返回的默认值
     */
    private double mDefaultValue = 0.00;

    public DoubleTypeAdapter() {
        this.mDefaultValue = 0.00;
    }

    public DoubleTypeAdapter(double defaultValue) {
        this.mDefaultValue = defaultValue;
    }

    @Override
    public Double deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            if (json.getAsString().equals("") || json.getAsString().equals("null")) {//定义为double类型,如果后台返回""或者null,则返回0.00
                return mDefaultValue;
            }
        } catch (Exception ignore) {
        }
        try {
            return json.getAsDouble();
        } catch (NumberFormatException e) {
            throw new JsonSyntaxException(e);
        }

    }

    @Override
    public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src);
    }
}
