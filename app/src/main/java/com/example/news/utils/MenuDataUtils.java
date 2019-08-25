package com.example.news.utils;

import com.example.news.R;
import com.example.news.entity.LeftItemMenu;

import java.util.ArrayList;
import java.util.List;

public class MenuDataUtils {

    public static List<LeftItemMenu> getItemMenus(){
        List<LeftItemMenu> menuList = new ArrayList<>();
        menuList.add(new LeftItemMenu(R.drawable.icon_zhanghaoxinxi, "消息中心"));
        menuList.add(new LeftItemMenu(R.drawable.icon_wodeguanzhu, "我的关注"));
        menuList.add(new LeftItemMenu(R.drawable.icon_shoucang, "我的评论"));
        menuList.add(new LeftItemMenu(R.drawable.icon_yijianfankui, "我的收藏"));
        menuList.add(new LeftItemMenu(R.drawable.icon_shezhi, "浏览记录"));
        menuList.add(new LeftItemMenu(R.drawable.icon_shezhi, "意见反馈"));
        menuList.add(new LeftItemMenu(R.drawable.icon_shezhi, "设置"));
        return menuList;
    }

}
