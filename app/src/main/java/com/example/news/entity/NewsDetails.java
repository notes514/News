package com.example.news.entity;

import java.util.Date;

public class NewsDetails {
    private Integer detailsId;

    private Integer newsId;

    private String detailsContent;

    private String detailsPic;

    private Integer detailsNumber;

    private Date releaseTime;

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

    public String getDetailsPic() {
        return detailsPic;
    }

    public void setDetailsPic(String detailsPic) {
        this.detailsPic = detailsPic == null ? null : detailsPic.trim();
    }

    public Integer getDetailsNumber() {
        return detailsNumber;
    }

    public void setDetailsNumber(Integer detailsNumber) {
        this.detailsNumber = detailsNumber;
    }

    public Date getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
    }
}