package com.manpreet.autopi;

import android.content.Context;
import android.content.SharedPreferences;

import com.manpreet.autopi.model.User;

public class Session {
    private static Session sharedSession = new Session();

    protected String username;
    protected String password;
    protected User currentUser;
    public String authString;

    public static Session getInstance() {
        return sharedSession;
    }

    private Session() {}

    protected void saveSession() {
        SharedPreferences preferences = LoginActivity.la.getApplicationContext().getSharedPreferences("AutoPiSession", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", username);
        editor.putString("password", username);
        editor.commit();
    }

    protected boolean checkSession() {
        if (authString != null && username != null && password != null) return true;

        SharedPreferences preferences = LoginActivity.la.getApplicationContext().getSharedPreferences("AutoPiSession", Context.MODE_PRIVATE);
        String u = preferences.getString("username", null);
        String p = preferences.getString("password", null);

        if (u != null && p != null) {
            username = u;
            password = p;
            return true;
        }
        return false;
    }

    protected void destroySession() {
        username = null;
        password = null;
        currentUser = null;
        authString = null;

        SharedPreferences preferences = LoginActivity.la.getApplicationContext().getSharedPreferences("AutoPiSession", Context.MODE_PRIVATE);
        preferences.edit().clear().commit();
    }

}
