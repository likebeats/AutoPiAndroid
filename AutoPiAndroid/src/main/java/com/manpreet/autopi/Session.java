package com.manpreet.autopi;

import com.manpreet.autopi.model.User;

public class Session {
    private static Session ourInstance = new Session();

    protected String username;
    protected String password;
    protected User currentUser;

    public static Session getInstance() {
        return ourInstance;
    }

    private Session() {}

    protected void destroySession() {
        username = null;
        password = null;
        currentUser = null;
    }

}
