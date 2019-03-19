package com.yunji.model;

import javax.persistence.*;

public class ThirdPartWay {
    @Id
    @Column(name = "userId")
    private Integer userid;

    @Id
    @Column(name = "wayId")
    private String wayid;

    private String way;

    @Column(name = "openId")
    private String openid;

    @Column(name = "unionId")
    private String unionid;

    private String nickname;

    /**
     * @return userId
     */
    public Integer getUserid() {
        return userid;
    }

    /**
     * @param userid
     */
    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    /**
     * @return wayId
     */
    public String getWayid() {
        return wayid;
    }

    /**
     * @param wayid
     */
    public void setWayid(String wayid) {
        this.wayid = wayid;
    }

    /**
     * @return way
     */
    public String getWay() {
        return way;
    }

    /**
     * @param way
     */
    public void setWay(String way) {
        this.way = way;
    }

    /**
     * @return openId
     */
    public String getOpenid() {
        return openid;
    }

    /**
     * @param openid
     */
    public void setOpenid(String openid) {
        this.openid = openid;
    }

    /**
     * @return unionId
     */
    public String getUnionid() {
        return unionid;
    }

    /**
     * @param unionid
     */
    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    /**
     * @return nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @param nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}