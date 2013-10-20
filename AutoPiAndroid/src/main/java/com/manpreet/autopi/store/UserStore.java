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

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("status", status));
        params.add(new BasicNameValuePair("user", "/api/v1/user/1/"));
        params.add(new BasicNameValuePair("resource_uri", "/api/v1/light/1/"));
        String response = api("/light/"+lightId+"?format=json", "POST", params, null);

        return response;
    }

}
