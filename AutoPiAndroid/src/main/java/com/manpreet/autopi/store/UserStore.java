package com.manpreet.autopi.store;

import android.util.Base64;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.manpreet.autopi.Session;
import com.manpreet.autopi.model.User;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserStore extends BaseStore {

    public static User getCurrentUser(String username, String password) {

        String authString = username + ":" + password;
        byte[] authEncBytes = Base64.encode(authString.getBytes(), Base64.DEFAULT);
        String authStringEnc = new String(authEncBytes);

        String jsonStr = getApi("/user?format=json", authStringEnc);
        if (jsonStr == null) return null;

        Session.getInstance().authString = authStringEnc;

        Gson gson = new Gson();

        System.out.println("*** getCurrentUser JSON: "+jsonStr+" ***");

        Map<Object,Object> map = new HashMap<Object,Object>();
        map=(Map<Object,Object>) gson.fromJson(jsonStr, map.getClass());

        ArrayList<Map<Object,Object>> userObjects = (ArrayList<Map<Object,Object>>)map.get("objects");

        Type collectionType = new TypeToken<ArrayList<User>>(){}.getType();
        ArrayList<User> users = gson.fromJson(gson.toJson(userObjects), collectionType);

        for (User user: users) {
            if (user.username.equals(username)) {
                return user;
            }
        }

        return null;

    }

    public static String setLightStatus(int lightId, String status) {

        try {
            JSONObject params = new JSONObject();
            params.put("id", "1");
            params.put("gpio", "1");
            params.put("status", "true");
            params.put("user", "/api/v1/user/1/");
            params.put("resource_uri", "/api/v1/light/1/");
            params.put("raspberry_pi", "/api/v1/raspberry_pi/1/");
            params.put("label", "Foyer");
            params.put("last_updated", "2013-10-20T00:38:52.447854");
            params.put("component", "light");
            return api("/light/"+lightId+"/?format=json", "PUT", params, Session.getInstance().authString);
        } catch (JSONException e) {
            System.out.println(e);
        }
        return null;
    }

}
