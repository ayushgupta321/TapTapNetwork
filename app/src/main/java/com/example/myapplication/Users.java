package com.example.myapplication;

public class Users {


    private int level;
    private String email;
    private String name;

    public Users(String Name, String email, int level)
    {
        this.name = Name;
        this.email = email;
        this.level = level;
    }

    public Users(){

    }

    public String getName()
    {
        return name;
    }

    public String getEmail()
    {
        return email;
    }

    public int getLevel()
    {
        return level;
    }


}
