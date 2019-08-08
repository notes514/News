package com.example.news.entity;

import java.util.Date;

public class NewsContent {
    private Integer newsId;

    private Integer typeId;

    private String newsTheme;

    private String newsPic;

    private String newsPress;

    private Date newsTime;

    private String remark;

    public Integer getNewsId() {
        return newsId;
    }

    public void setNewsId(Integer newsId) {
        this.newsId = newsId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getNewsTheme() {
        return newsTheme;
    }

    public void setNewsTheme(String newsTheme) {
        this.newsTheme = newsTheme == null ? null : newsTheme.trim();
    }

    public String getNewsPic() {
        return newsPic;
    }

    public void setNewsPic(String newsPic) {
        this.newsPic = newsPic == null ? null : newsPic.trim();
    }

    public String getNewsPress() {
        return newsPress;
    }

    public void setNewsPress(String newsPress) {
        this.newsPress = newsPress == null ? null : newsPress.trim();
    }

    public Date getNewsTime() {
        return newsTime;
    }

    public void setNewsTime(Date newsTime) {
        this.newsTime = newsTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}