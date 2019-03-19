package com.yunji.model;

import javax.persistence.*;

public class UserTag {
    @Id
    private Integer id;

    @Column(name = "userId")
    private Integer userid;

    private String tagname;

    private String tagvalue;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

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
     * @return tagname
     */
    public String getTagname() {
        return tagname;
    }

    /**
     * @param tagname
     */
    public void setTagname(String tagname) {
        this.tagname = tagname;
    }

    /**
     * @return tagvalue
     */
    public String getTagvalue() {
        return tagvalue;
    }

    /**
     * @param tagvalue
     */
    public void setTagvalue(String tagvalue) {
        this.tagvalue = tagvalue;
    }
}