package com.example.news.utils;

import com.example.news.R;
import com.example.news.entity.LeftItemMenu;

import java.util.ArrayList;
import java.util.List;

public class MenuDataUtils {

    public static List<LeftItemMenu> getItemMenus(){
        List<LeftItemMenu> menuList = new ArrayList<>();
        menuList.add(new LeftItemMenu(R.drawable.icon_zhanghaoxinxi, "账号信息"));
        menuList.add(new LeftItemMenu(R.drawable.icon_wodeguanzhu, "我的关注"));
        menuList.add(new LeftItemMenu(R.drawable.icon_shoucang, "我的收藏"));
        menuList.add(new LeftItemMenu(R.drawable.icon_yijianfankui, "意见反馈"));
        menuList.add(new LeftItemMenu(R.drawable.icon_shezhi, "设置"));
        return menuList;
    }

}
