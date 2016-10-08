package com.example.qi.teacheronline.controller;

import android.media.Image;
import android.os.NetworkOnMainThreadException;
import android.util.Log;

import com.example.qi.teacheronline.model.Userinfo;
import com.example.qi.teacheronline.util.CustomHttpClient;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by qi on 2016/2/3.
 */
public class Setpic {

    public String setpic(String qwe){
        ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();

        postParameters.add(new BasicNameValuePair("pic", qwe));

        String response = null;
        String res=null;
        try {
            response = CustomHttpClient.executeHttpPost("http://115.159.92.239:8080/setinfo.jsp", postParameters);  //Enetr Your remote PHP,ASP, Servlet file link
            res=response.toString();
            res= res.trim().toString();

        } catch (NetworkOnMainThreadException e) {
            Log.e("qwe", e.toString());
        }catch (Exception e){
            Log.e("qwe",e.toString());
        }
        return res;
    }
}
