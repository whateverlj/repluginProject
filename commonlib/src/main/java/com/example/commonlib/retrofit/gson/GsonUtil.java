package  com.example.commonlib.retrofit.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtil {

    /**
     * 注册了数字转换，防止空字符串时的转换异常
     */
    public static Gson gson = new GsonBuilder()
            .registerTypeAdapter(Integer.class, new IntegerTypeAdapter())
            .registerTypeAdapter(int.class, new IntegerTypeAdapter(-1))
            .registerTypeAdapter(Double.class, new DoubleTypeAdapter(-1.00))
            .registerTypeAdapter(double.class, new DoubleTypeAdapter(-1.00))
            .registerTypeAdapter(Long.class, new LongTypeAdapter(-1l))
            .registerTypeAdapter(long.class, new LongTypeAdapter(-1l))
            .create();

    public static Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();

}
