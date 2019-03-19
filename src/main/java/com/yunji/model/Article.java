package com.yunji.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import util.Utils;

import java.util.Date;

public class Article extends category{
   private Integer articleId;
   private Integer userId;
   private String username;
   private String title;
   private Date createtime;
   private Date starttime;
   private String dateStr;
   private String circle;
   private String scenario;
   private String visibility;
   private String redisKey;
   private String htmlUrl;
   private String summary;
   private String picUrl;
   private String size;
   private Integer starNum;
   private Integer readNum;
   private Integer commentNum;
   private String organization;
   private String headImageUrl;
   private String shareVisibility;

    public String getShareVisibility() {
        return shareVisibility;
    }

    public void setShareVisibility(String shareVisibility) {
        this.shareVisibility = shareVisibility;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    private String mdUrl;

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    public Date getCreatetime() {

        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    public Date getStarttime() {

        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getMdUrl() {
        return mdUrl;
    }

    public void setMdUrl(String mdUrl) {
        this.mdUrl = mdUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCircle() {
        return circle;
    }

    public void setCircle(String circle) {
        this.circle = circle;
    }

    public String getScenario() {
        return scenario;
    }

    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getRedisKey() {
        return redisKey;
    }

    public void setRedisKey(String redisKey) {
        this.redisKey = redisKey;
    }

    public Integer getStarNum() {
        return starNum;
    }

    public void setStarNum(Integer starNum) {
        this.starNum = starNum;
    }

    public Integer getReadNum() {
        return readNum;
    }

    public void setReadNum(Integer readNum) {
        this.readNum = readNum;
    }

    public Integer getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getHeadImageUrl() {
        return headImageUrl;
    }

    public void setHeadImageUrl(String headImageUrl) {
        this.headImageUrl = headImageUrl;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    @Override
    public String toString() {
        return "Article{" +
                "articleId=" + articleId +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                ", title='" + title + '\'' +
                ", createtime=" + createtime +
                ", dateStr='" + dateStr + '\'' +
                ", circle='" + circle + '\'' +
                ", scenario='" + scenario + '\'' +
                ", visibility='" + visibility + '\'' +
                ", redisKey='" + redisKey + '\'' +
                ", htmlUrl='" + htmlUrl + '\'' +
                ", summary='" + summary + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", size='" + size + '\'' +
                ", starNum=" + starNum +
                ", readNum=" + readNum +
                ", commentNum=" + commentNum +
                ", organization='" + organization + '\'' +
                ", headImageUrl='" + headImageUrl + '\'' +
                ", shareVisibility='" + shareVisibility + '\'' +
                ", mdUrl='" + mdUrl + '\'' +
                '}';
    }
}
