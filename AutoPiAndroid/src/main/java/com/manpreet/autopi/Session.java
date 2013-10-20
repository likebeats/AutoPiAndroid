package com.manpreet.autopi;

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

    protected void destroySession() {
        username = null;
        password = null;
        currentUser = null;
        authString = null;
    }

}
