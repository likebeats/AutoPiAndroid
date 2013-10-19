package com.manpreet.autopi.store;

public class UserStore extends BaseStore {

    public static String getAllUsers(String username, String password) {

        return getJSONApi("/user?format=json", username, password);

    }

}
