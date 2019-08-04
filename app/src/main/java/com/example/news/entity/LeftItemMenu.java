package com.example.news.entity;

/**
 * 左侧菜单功能实体类
 */
public class LeftItemMenu {
    private int leftIcon;
    private String title;

    public LeftItemMenu(int leftIcon, String title) {
        this.leftIcon = leftIcon;
        this.title = title;
    }

    public int getLeftIcon() {
        return leftIcon;
    }

    public void setLeftIcon(int leftIcon) {
        this.leftIcon = leftIcon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
