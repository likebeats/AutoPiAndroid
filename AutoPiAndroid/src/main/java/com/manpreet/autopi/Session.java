package com.manpreet.autopi;

public class Session {
    private static Session ourInstance = new Session();

    protected String username;
    protected String password;

    public static Session getInstance() {
        return ourInstance;
    }

    private Session() {}

    protected void destroySession() {
        username = null;
        password = null;
    }

}
