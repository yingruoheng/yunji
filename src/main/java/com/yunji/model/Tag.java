package com.yunji.model;

public class Tag {
    private String tagKey;
    private String tagValue;

    public String getTagKey() {
        return tagKey;
    }

    public void setTagKey(String tagKey) {
        this.tagKey = tagKey;
    }

    public String getTagValue() {
        return tagValue;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }



    @Override
    public String toString() {
        return "Tag{" +
                "tagKey='" + tagKey + '\'' +
                ", tagValue='" + tagValue + '\'' +
                '}';
    }
}
