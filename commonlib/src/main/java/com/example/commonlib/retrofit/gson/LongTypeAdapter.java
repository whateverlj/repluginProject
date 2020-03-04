package  com.example.commonlib.retrofit.gson;

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
 * Long，主要解决字符串为空时转Long的异常
 */
public class LongTypeAdapter implements JsonSerializer<Long>, JsonDeserializer<Long> {

    /**
     * 当字符串为空时，转换的默认值
     */
    private long mDefaultValue = 0l;

    public LongTypeAdapter() {
        this.mDefaultValue = 0l;
    }

    public LongTypeAdapter(long defaultValue) {
        this.mDefaultValue = defaultValue;
    }

    @Override
    public Long deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            if (json.getAsString().equals("") || json.getAsString().equals("null")) {//定义为long类型,如果后台返回""或者null,则返回0
                return 0l;
            }
        } catch (Exception ignore) {
        }
        try {
            return json.getAsLong();
        } catch (NumberFormatException e) {
            throw new JsonSyntaxException(e);
        }

    }

    @Override
    public JsonElement serialize(Long src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src);
    }
}
