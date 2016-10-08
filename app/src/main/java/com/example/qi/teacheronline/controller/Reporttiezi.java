package com.example.qi.teacheronline.controller;

import android.os.NetworkOnMainThreadException;
import android.util.Log;

import com.example.qi.teacheronline.util.CustomHttpClient;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * Created by qigang on 16/5/9.
 */
public class Reporttiezi {
    public void report(String id,String reporter,String reportacc){
        ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();

        postParameters.add(new BasicNameValuePair("mainid",id ));
        postParameters.add(new BasicNameValuePair("reporter",reporter ));
        postParameters.add(new BasicNameValuePair("reportacc",reportacc ));

        String response = null;
        String res=null;
        try {
            response = CustomHttpClient.executeHttpPost("http://115.159.92.239:8080/reporttz.jsp", postParameters);  //Enetr Your remote PHP,ASP, Servlet file link
            res=response.toString();
            res= res.trim().toString();
        } catch (NetworkOnMainThreadException e) {
            Log.e("qwe", e.toString());
        }catch (Exception e){
            Log.e("qwe",e.toString());
        }
        return ;
    }

    public int isreport(String id){
        int flag = 0;
        ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();

        postParameters.add(new BasicNameValuePair("mainid",id ));

        String response = null;
        String res=null;
        try {
            response = CustomHttpClient.executeHttpPost("http://115.159.92.239:8080/isreport.jsp", postParameters);  //Enetr Your remote PHP,ASP, Servlet file link
            res=response.toString();
            res= res.trim().toString();
            if(res.equals("1")){
                flag = 1;
            }else {
                flag = 0;
            }
        } catch (NetworkOnMainThreadException e) {
            Log.e("qwe", e.toString());
        }catch (Exception e){
            Log.e("qwe",e.toString());
        }
        return flag;
    }
}
