package com.example.news.entity;

public class NewsDetails {
    private Integer detailsId;

    private Integer newsId;

    private String detailsContent;

    private String remark;

    public Integer getDetailsId() {
        return detailsId;
    }

    public void setDetailsId(Integer detailsId) {
        this.detailsId = detailsId;
    }

    public Integer getNewsId() {
        return newsId;
    }

    public void setNewsId(Integer newsId) {
        this.newsId = newsId;
    }

    public String getDetailsContent() {
        return detailsContent;
    }

    public void setDetailsContent(String detailsContent) {
        this.detailsContent = detailsContent == null ? null : detailsContent.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}