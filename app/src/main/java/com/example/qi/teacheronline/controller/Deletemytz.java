package com.example.qi.teacheronline.controller;

import android.os.NetworkOnMainThreadException;
import android.util.Log;

import com.example.qi.teacheronline.util.CustomHttpClient;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/13.
 */
public class Deletemytz {
    public void delete(String id,String flag){
        ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();

        postParameters.add(new BasicNameValuePair("id",id ));
        postParameters.add(new BasicNameValuePair("flag",flag ));

        String response = null;
        String res=null;
        try {
            response = CustomHttpClient.executeHttpPost("http://115.159.92.239:8080/deletetz.jsp", postParameters);  //Enetr Your remote PHP,ASP, Servlet file link
            res=response.toString();
            res= res.trim().toString();
        } catch (NetworkOnMainThreadException e) {
            Log.e("qwe", e.toString());
        }catch (Exception e){
            Log.e("qwe",e.toString());
        }
        return ;
    }
}
