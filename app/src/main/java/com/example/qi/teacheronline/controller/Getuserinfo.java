package com.example.qi.teacheronline.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.NetworkOnMainThreadException;
import android.util.Log;

import com.example.qi.teacheronline.MainActivity;
import com.example.qi.teacheronline.model.Userinfo;
import com.example.qi.teacheronline.util.CustomHttpClient;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * Created by qi on 2016/1/30.
 */
public class Getuserinfo {
    private String account = null;

    public Getuserinfo(String account){
        this.account = account;
    }

    public String getinfo(){
        ArrayList < NameValuePair > postParameters = new ArrayList<NameValuePair>();
        postParameters.add(new BasicNameValuePair("phone", account));
        String res = null;

        String response = null;
        try {
            response = CustomHttpClient.executeHttpPost("http://115.159.92.239:8080/getuserinfo.jsp", postParameters);  //Enetr Your remote PHP,ASP, Servlet file link
            res=response.toString();
            res= res.trim().toString();
            //Userinfo.setName(res);

        } catch (NetworkOnMainThreadException e) {
            Log.e("qwe", e.toString()+"qweqweqweqweqweqwe");
        }catch (Exception e){
            Log.e("qwe",e.toString());
        }
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }).start();
        return res;
    }
}
