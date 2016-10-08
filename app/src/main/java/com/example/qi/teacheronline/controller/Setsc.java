package com.example.qi.teacheronline.controller;

import android.os.NetworkOnMainThreadException;
import android.util.Log;

import com.example.qi.teacheronline.util.CustomHttpClient;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/12.
 */
public class Setsc {
    public void setsc(String account,String id,String title){
        ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();

        postParameters.add(new BasicNameValuePair("sczaccount",account ));
        postParameters.add(new BasicNameValuePair("scid",id ));
        postParameters.add(new BasicNameValuePair("tittle",title ));

        String response = null;
        String res=null;
        try {
            response = CustomHttpClient.executeHttpPost("http://115.159.92.239:8080/setsc.jsp", postParameters);  //Enetr Your remote PHP,ASP, Servlet file link
            res=response.toString();
            res= res.trim().toString();

        } catch (NetworkOnMainThreadException e) {
            Log.e("qwe", e.toString());
        }catch (Exception e){
            Log.e("qwe",e.toString());
        }
        return ;
    }

    public String issc(String account,String id){
        ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();

        postParameters.add(new BasicNameValuePair("sczaccount",account ));
        postParameters.add(new BasicNameValuePair("scid",id ));

        String response = null;
        String res=null;
        try {
            response = CustomHttpClient.executeHttpPost("http://182.254.135.113:8080/issc.jsp", postParameters);  //Enetr Your remote PHP,ASP, Servlet file link
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
