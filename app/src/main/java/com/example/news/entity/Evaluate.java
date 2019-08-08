package com.example.news.entity;

import java.util.Date;

public class Evaluate {
    private Integer evaluateId;

    private Integer userId;

    private Integer detailsId;

    private String evaluateTheme;

    private String evaluateContent;

    private Date evalueteTime;

    public Integer getEvaluateId() {
        return evaluateId;
    }

    public void setEvaluateId(Integer evaluateId) {
        this.evaluateId = evaluateId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getDetailsId() {
        return detailsId;
    }

    public void setDetailsId(Integer detailsId) {
        this.detailsId = detailsId;
    }

    public String getEvaluateTheme() {
        return evaluateTheme;
    }

    public void setEvaluateTheme(String evaluateTheme) {
        this.evaluateTheme = evaluateTheme == null ? null : evaluateTheme.trim();
    }

    public String getEvaluateContent() {
        return evaluateContent;
    }

    public void setEvaluateContent(String evaluateContent) {
        this.evaluateContent = evaluateContent == null ? null : evaluateContent.trim();
    }

    public Date getEvalueteTime() {
        return evalueteTime;
    }

    public void setEvalueteTime(Date evalueteTime) {
        this.evalueteTime = evalueteTime;
    }
}