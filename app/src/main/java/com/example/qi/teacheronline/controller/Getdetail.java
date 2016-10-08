package com.example.qi.teacheronline.controller;

import com.example.qi.teacheronline.util.CustomHttpClient;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qi on 2016/2/10.
 */
public class Getdetail {
    public List<Map<String,Object>> getgzinfo(String id,String content,String ftname){
        List<Map<String,Object>> data=new ArrayList<Map<String,Object>>();

        ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
        postParameters.add(new BasicNameValuePair("tzid",id));
        String response = null;
        String res = null;
        try {
            response = CustomHttpClient.executeHttpPost("http://115.159.92.239:8080/getgzinfo.jsp", postParameters);
            res=response.toString();
            res= res.trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] result = res.split("<conts>");

        for(int i=0;i<result.length;i++){
            result[i]=result[i].trim();
        }
        for(int i=0,j=1;i<result.length;i+=2,j++){
            Map<String,Object> mapx=new HashMap<String, Object>();
            mapx.put("text", j+"楼\n"+result[i]);
            mapx.put("name",result[i+1]);
            data.add(mapx);
        }
        return data;
    }



    public String[] getftzinfo (String id){

        ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
        postParameters.add(new BasicNameValuePair("tzid",id));
        String response = null;
        String res = null;
        try {
            response = CustomHttpClient.executeHttpPost("http://115.159.92.239:8080/gettzcont.jsp", postParameters);
            res=response.toString();
            res= res.trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] result = res.split("<conts>");
        for(int i=0;i<result.length;i++){
           result[i]=result[i].trim().toString();
        }
        return result;
    }

//    public List<Map<String,Object>> getinfobytzid(String family){
//        List<Map<String,Object>> data=new ArrayList<Map<String,Object>>();
//
//        ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
//        postParameters.add(new BasicNameValuePair("family",family));
//        String response = null;
//        String res = null;
//        try {
//            response = CustomHttpClient.executeHttpPost("http://192.168.0.104:8080/Test/getinfo.jsp", postParameters);
//            res=response.toString();
//            res= res.trim();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        String[] result = res.split("&");
//
//        for(int i=0;i<result.length;i++){
//            result[i]=result[i].trim();
//        }
//        for(int i=0;i<result.length;i+=3){
//            Map<String,Object> map=new HashMap<String, Object>();
//            map.put("tittle", result[i+1]);
//            map.put("tzid",result[i]);
//            map.put("info",result[i+2]);
//            data.add(map);
//        }
//
//        return data;
//    }
}
