package com.example.news.application;

import android.app.Application;
import android.content.Context;

/**
 * 全局Application类,作为全局数据的配置以及相关参数数据初始化工作
 */
public class InitApp extends Application {
    
    private static InitApp instance;
    private static Context context;
    public static final String TAG = "Application";

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
