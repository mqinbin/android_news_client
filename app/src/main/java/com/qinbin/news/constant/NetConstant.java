package com.qinbin.news.constant;

/**
 * Created by Teacher on 2016/8/9.
 */
public class NetConstant {
//    public static final String HOST = NetConstant.HOST+"";
    public static final String HOST = "http://123.57.42.161:8080/czxw";

    public static String formatUrl(String url){
        if(url.startsWith("http")){
            return url;
        }else{
            return HOST + url;
        }
    }
}
