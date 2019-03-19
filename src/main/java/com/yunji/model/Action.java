package com.yunji.model;

import java.util.Date;

public class Action {
    private int userId;
    private int articleId;
    private String actionType;
    private Date createtime;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    @Override
    public String toString() {
        return "Action{" +
                "userId=" + userId +
                ", articleId=" + articleId +
                ", actionType='" + actionType + '\'' +
                ", createtime=" + createtime +
                '}';
    }
}
