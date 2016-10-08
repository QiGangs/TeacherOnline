package com.example.qi.teacheronline.util;

/**
 * Created by Administrator on 2016/3/14.
 */
public class Settitle {
    public String set(String str){
        switch (str){
            case "math":
                return "数学";
            case "bcyy":
                return "编程语言";
            case "sjjg":
                return "数据结构";
            case "wljs":
                return "网络技术";
            case "ydkf":
                return "移动开发";
            case "javaee":
                return "JAVA EE";
            case "czxt":
                return "操作系统";
            case "fzyx":
                return "付之一笑";
        }
        return null;
    }
}
