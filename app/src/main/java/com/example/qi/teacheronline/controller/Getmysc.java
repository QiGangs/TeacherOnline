package com.example.qi.teacheronline.controller;

import com.example.qi.teacheronline.util.CustomHttpClient;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/12.
 */
public class Getmysc {
    public List<Map<String,Object>> getmysc(String account,int s,int f){
        List<Map<String,Object>> data=new ArrayList<Map<String,Object>>();

        ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
        postParameters.add(new BasicNameValuePair("account",account));
        postParameters.add(new BasicNameValuePair("s",s+""));
        postParameters.add(new BasicNameValuePair("f",f+""));
        String response = null;
        String res = null;
        try {
            response = CustomHttpClient.executeHttpPost("http://115.159.92.239:8080/getmysc.jsp", postParameters);
            res=response.toString();
            res= res.trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] result = res.split("<cont>");

        for(int i=0;i<result.length;i++){
            result[i]=result[i].trim();
        }
        for(int i=0;i<result.length;i+=2){
            Map<String,Object> map=new HashMap<String, Object>();
            map.put("tittle", result[i+1]);
            map.put("tzid",result[i]);
            data.add(map);
        }
        //Collections.reverse(data);
        return data;
    }
}
