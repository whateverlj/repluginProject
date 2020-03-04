package  com.example.commonlib.util;




import com.example.commonlib.retrofit.gson.DoubleTypeAdapter;
import com.example.commonlib.retrofit.gson.IntegerTypeAdapter;
import com.example.commonlib.retrofit.gson.LongTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtils {

    public static Gson gson = new GsonBuilder()
            .registerTypeAdapter(Integer.class, new IntegerTypeAdapter())
            .registerTypeAdapter(int.class, new IntegerTypeAdapter())
            .registerTypeAdapter(Double.class, new DoubleTypeAdapter())
            .registerTypeAdapter(double.class, new DoubleTypeAdapter())
            .registerTypeAdapter(Long.class, new LongTypeAdapter())
            .registerTypeAdapter(long.class, new LongTypeAdapter())
            .create();

}
