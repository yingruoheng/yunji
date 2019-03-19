package com.yunji.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Organization {
    private Integer userId;
    private String organization;
    private String circle;
    private Date createtime;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getCircle() {
        return circle;
    }

    public void setCircle(String circle) {
        this.circle = circle;
    }

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createTime) {
        this.createtime = createTime;
    }

    @Override
    public String toString() {
        return "Organization{" +
                "userId=" + userId +
                ", organization='" + organization + '\'' +
                ", circle='" + circle + '\'' +
                ", createTime=" + createtime +
                '}';
    }
}
