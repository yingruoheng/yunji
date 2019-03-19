package com.yunji.model;


public class IdStatus {
    private Integer sid;

    private Integer status;

    public IdStatus(Integer sid, Integer status) {
        this.sid = sid;
        this.status = status;
    }

    public IdStatus( Integer status) {
        this.status = status;
    }


    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}