package com.example.qi.teacheronline.util;

/**
 * Created by qigang on 16/4/7.
 */
public class Checkflag {
    public boolean check(String content){
        if(content.indexOf("<cont>")>=0 || content.indexOf("conts")>=0){
            return true;
        }
        return false;
    }

}
