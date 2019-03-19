package com.yunji.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Interest extends category{
    private Integer id;
    private  Integer userId;
    private  Integer toUserId;
    private String circle;
    private String scenario;
    private Date createTime;
    private String username;
    private String userImage;
    private String toUserImage;
    private String toUsername;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getToUserId() {
        return toUserId;
    }

    public void setToUserId(Integer toUserId) {
        this.toUserId = toUserId;
    }

    public String getToUserImage() {
        return toUserImage;
    }

    public void setToUserImage(String toUserImage) {
        this.toUserImage = toUserImage;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getToUsername() {
        return toUsername;
    }

    public void setToUsername(String toUsername) {
        this.toUsername = toUsername;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    @Override
    public String toString() {
        return "Interest{" +
                "id=" + id +
                ", userId=" + userId +
                ", toUserId=" + toUserId +
                ", circle='" + circle + '\'' +
                ", scenario='" + scenario + '\'' +
                ", createTime=" + createTime +
                ", username='" + username + '\'' +
                ", userImage='" + userImage + '\'' +
                ", toUserImage='" + toUserImage + '\'' +
                ", toUsername='" + toUsername + '\'' +
                '}';
    }
}
