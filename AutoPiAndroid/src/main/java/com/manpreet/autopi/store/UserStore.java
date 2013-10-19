package com.manpreet.autopi.store;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.manpreet.autopi.model.User;

public class UserStore extends BaseStore {

    public static User getCurrentUser(String username, String password) {

        String jsonStr = getJSONApi("/user?format=json", username, password);
        Gson gson = new Gson();

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

}
