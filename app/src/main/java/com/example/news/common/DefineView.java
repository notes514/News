package com.example.news.common;

/**
 * 所有的Activity,Fragment可以实现这个接口，来进行一些公共的操作
 */
public interface DefineView {

   void initView();      //初始化界面元素(使用绑定数据，界面元素不需要进行初始化)

   void initValidata();  //初始化变量

   void initListener();  //初始化监听器

   void bindData();      //绑定数据

}
