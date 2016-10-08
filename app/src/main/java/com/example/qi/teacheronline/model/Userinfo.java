package com.example.qi.teacheronline.model;

/**
 * Created by qi on 2016/1/30.
 */
public class Userinfo {
    static String phone;
    static String name;

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        Userinfo.name = name;
    }

    public static String getPhone() {
        return phone;
    }

    public static void setPhone(String phone) {
        Userinfo.phone = phone;
    }
}
