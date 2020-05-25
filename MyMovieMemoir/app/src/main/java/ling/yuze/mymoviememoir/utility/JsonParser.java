package ling.yuze.mymoviememoir.utility;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JsonParser {
    public static HashMap<String, Integer> toHashMap(String jsonList, String... keys) {
        HashMap<String, Integer> map = new HashMap<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonList);
            for (int index = 0; index < jsonArray.length(); index ++) {
                JSONObject jsonObject = jsonArray.getJSONObject(index);
                map.put(jsonObject.getString(keys[0]), jsonObject.getInt(keys[1]));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        finally {
            return map;
        }
    }

    public static List<Object[]> toList(String jsonList, String... keys) {
        List<Object[]> list = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonList);
            for (int index = 0; index < jsonArray.length(); index ++) {
                JSONObject jsonObject = jsonArray.getJSONObject(index);
                Object[] objects = {jsonObject.get(keys[0]), jsonObject.get(keys[1]), jsonObject.get(keys[2])};
                list.add(objects);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return list;
        }

    }

    public static String objectToJson(Object object) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(object);
        return jsonString;
    }

    public static List<Object> getValueList(String jsonList, String key) {
        List<Object> list = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonList);
            for (int index = 0; index < jsonArray.length(); index ++) {
                list.add(jsonArray.getJSONObject(index).get(key));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        finally {
            return list;
        }
    }

    public static Object getJsonValue(String jsonString, String... keys) {
        Object value = null;
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            for (int index = 0; index < keys.length; index ++) {
                if (index != keys.length - 1) {
                    jsonObject = jsonObject.getJSONObject(keys[index]);
                }
                else {
                    value = jsonObject.get(keys[index]);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return value;

    }
}
