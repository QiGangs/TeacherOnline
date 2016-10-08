package com.example.qi.teacheronline.controller;

import android.os.NetworkOnMainThreadException;
import android.util.Log;

import com.example.qi.teacheronline.util.CustomHttpClient;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * Created by qi on 2016/2/14.
 */
public class testzw {
    public String setinfo(){
        ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();

        postParameters.add(new BasicNameValuePair("account","祁刚" ));
        postParameters.add(new BasicNameValuePair("name","祁刚" ));
        postParameters.add(new BasicNameValuePair("family","祁刚" ));
        postParameters.add(new BasicNameValuePair("tittle","祁刚" ));
        postParameters.add(new BasicNameValuePair("content","祁刚" ));

        String response = null;
        String res=null;
        try {
            response = CustomHttpClient.executeHttpPost("http://115.159.92.239:8080/Test/test.jsp", postParameters);  //Enetr Your remote PHP,ASP, Servlet file link
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
