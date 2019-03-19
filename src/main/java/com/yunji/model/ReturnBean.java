package com.yunji.model;

public class ReturnBean<T> {

    private Integer retVal;

    private String retMsg;

    private T retBean;

    public Integer getRetVal() {
        return retVal;
    }

    public void setRetVal(Integer retVal) {
        this.retVal = retVal;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }

    public T getRetBean() {
        return retBean;
    }

    public void setRetBean(T retBean) {
        this.retBean = retBean;
    }

    @Override
    public String toString() {
        return "ReturnBean{" +
                "retVal=" + retVal +
                ", retMsg='" + retMsg + '\'' +
                ", retBean=" + retBean +
                '}';
    }
}
