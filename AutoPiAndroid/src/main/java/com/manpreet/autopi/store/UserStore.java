package com.manpreet.autopi.store;

import android.util.Base64;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.manpreet.autopi.Session;
import com.manpreet.autopi.model.User;

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

    public static String setLightStatus(int lightId, boolean status) {

        try {
            JSONObject params = new JSONObject();
            params.put("status", status);
            return api("/light/"+lightId+"/?format=json", "PUT", params, Session.getInstance().authString);
        } catch (JSONException e) {
            System.out.println(e);
        }
        return null;
    }

}
