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
 * Integer，主要解决字符串为空时转Integer的异常
 */
public class IntegerTypeAdapter implements JsonSerializer<Integer>, JsonDeserializer<Integer> {

    /**
     * 当字符串为空时，转换成的默认值
     */
    private int mDefaultValue = 0;

    public IntegerTypeAdapter() {
        this.mDefaultValue = 0;
    }

    public IntegerTypeAdapter(int defaultValue) {
        this.mDefaultValue = defaultValue;
    }

    @Override
    public Integer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            if (json.getAsString().equals("") || json.getAsString().equals("null")) {//定义为int类型,如果后台返回""或者null,则返回0
                return mDefaultValue;
            }
        } catch (Exception ignore) {
        }
        try {
            return json.getAsInt();
        } catch (NumberFormatException e) {
            throw new JsonSyntaxException(e);
        }

    }

    @Override
    public JsonElement serialize(Integer src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src);
    }
}
