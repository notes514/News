package com.example.news.entity;

public class NewsImage {
    private Integer newsImageId;

    private Integer newsId;

    private Integer detailsId;

    private String imagePic;

    private String remark;

    public Integer getNewsImageId() {
        return newsImageId;
    }

    public void setNewsImageId(Integer newsImageId) {
        this.newsImageId = newsImageId;
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

    public String getImagePic() {
        return imagePic;
    }

    public void setImagePic(String imagePic) {
        this.imagePic = imagePic == null ? null : imagePic.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}