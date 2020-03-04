package  com.example.commonlib.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtil {
    public static String getJsonString(String keyWord, String result) {
        String res = "";
        try {
            JSONObject object = new JSONObject(result);
            if (!object.isNull(keyWord)){
                res = object.getString(keyWord);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static boolean getJsonBoolean(String keyWord, String result) {
        boolean res = false;
        try {
            JSONObject object = new JSONObject(result);
            res = object.getBoolean(keyWord);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static Double getJsonDouble(String keyWord, String result) {
        Double res = 0.0;
        try {
            JSONObject object = new JSONObject(result);
            res = object.getDouble(keyWord);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return res;
    }


    public static int getJsonInt(String keyWord, String result) {
        int res = 0;
        try {
            JSONObject object = new JSONObject(result);
            if (!object.isNull(keyWord)) {
                res = object.getInt(keyWord);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static Integer getJsonInteger(String keyWord, String result) {
        Integer res = null;
        try {
            JSONObject object = new JSONObject(result);
            if (!object.isNull(keyWord)) {
                res = (Integer) object.get(keyWord);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static Long getJsonLong(String keyWord, String result) {
        Long res = 0L;
        try {
            JSONObject object = new JSONObject(result);
            res = object.getLong(keyWord);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static JSONObject getJsonObject(String keyWord, String result) {
        JSONObject res = null;

        try {
            JSONObject object = new JSONObject(result);
            res = object.getJSONObject(keyWord);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static JSONArray getJsonArray(String keyWord, String result) {
        JSONArray res = null;
        try {
            JSONObject object = new JSONObject(result);
            res = object.getJSONArray(keyWord);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return res;
    }
}
