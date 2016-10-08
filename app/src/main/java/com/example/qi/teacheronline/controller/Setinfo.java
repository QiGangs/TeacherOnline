package com.example.qi.teacheronline.controller;

import android.os.NetworkOnMainThreadException;
import android.util.Log;

import com.example.qi.teacheronline.util.CustomHttpClient;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * Created by qi on 2016/2/1.
 */
public class Setinfo {
    public String setinfo(String account,String name,String family,String tittle,String content,int picnum){
        ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();

        postParameters.add(new BasicNameValuePair("account",account ));
        postParameters.add(new BasicNameValuePair("name",name ));
        postParameters.add(new BasicNameValuePair("family",family ));
        postParameters.add(new BasicNameValuePair("tittle",tittle ));
        postParameters.add(new BasicNameValuePair("content",content ));
        postParameters.add(new BasicNameValuePair("picnum",picnum+"" ));

        String response = null;
        String res=null;
        try {
            response = CustomHttpClient.executeHttpPost("http://115.159.92.239:8080/setinfonotpic.jsp", postParameters);
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
