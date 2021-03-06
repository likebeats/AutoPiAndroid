package com.manpreet.autopi.model;

import java.util.ArrayList;

public class User {

    public String date_joined;
    public String email;
    public String first_name;
    public int id;
    public Boolean is_active;
    public Boolean is_staff;
    public Boolean is_superuser;
    public String last_login;
    public ArrayList<Light> lights;
    public ArrayList<Blind> blinds;
    public ArrayList<Entrance> entrance;
    public String password;
    public ArrayList<RaspberryPi> raspberry_pi;
    public String resource_uri;
    public String username;

}
