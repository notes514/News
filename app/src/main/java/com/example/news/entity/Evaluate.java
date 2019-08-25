package com.example.news.entity;

import java.util.Date;

public class Evaluate {
    private Integer evaluateId;

    private Integer userId;

    private Integer newsId;

    private Integer detailsId;

    private String evaluateTheme;

    private String evaluateContent;

    private Integer evalueteNumber;

    private Integer evalueteHelpNumber;

    private Date evalueteTime;

    private String ramerk;

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

    public Integer getNewsId() {
        return newsId;
    }

    public void setNewsId(Integer newsId) {
        this.newsId = newsId;
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

    public Integer getEvalueteNumber() {
        return evalueteNumber;
    }

    public void setEvalueteNumber(Integer evalueteNumber) {
        this.evalueteNumber = evalueteNumber;
    }

    public Integer getEvalueteHelpNumber() {
        return evalueteHelpNumber;
    }

    public void setEvalueteHelpNumber(Integer evalueteHelpNumber) {
        this.evalueteHelpNumber = evalueteHelpNumber;
    }

    public Date getEvalueteTime() {
        return evalueteTime;
    }

    public void setEvalueteTime(Date evalueteTime) {
        this.evalueteTime = evalueteTime;
    }

    public String getRamerk() {
        return ramerk;
    }

    public void setRamerk(String ramerk) {
        this.ramerk = ramerk == null ? null : ramerk.trim();
    }
}