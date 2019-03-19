package com.yunji.model;

import javax.persistence.*;

public class ArticleTag {
    @Id
    private Integer id;

    private Integer articleId;

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


    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
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