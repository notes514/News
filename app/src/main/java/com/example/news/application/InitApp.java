package com.example.news.application;

import android.app.Application;
import android.content.Context;

/**
 * 全局Application类,作为全局数据的配置以及相关参数数据初始化工作
 */
public class InitApp extends Application {

    public static final String TAG = "Application";
    private static InitApp instance;
    private static Context context;
    public static String ip_port = "http://192.168.1.6:8080/NewsServer/news/";

    @Override
    public void onCreate() {
        super.onCreate();
        this.instance = this;
        this.context = this;
    }

    public static InitApp getInstance() {
        return instance;
    }

    public static Context getContext(){
        return context;
    }

}
