package ling.yuze.mymoviememoir.formatting;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class JsonParser {
    public static String objectToJson(Object object) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(object);
        return jsonString;
    }

    public static Object getJsonValue(String key, String jsonString, int degree) {
        Object value = null;
        if (degree == 1) {
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                value = jsonObject.opt(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return value;

    }
}
