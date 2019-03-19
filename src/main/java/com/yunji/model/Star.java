package com.yunji.model;

public class Star {

    private int star;

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    @Override
    public String toString(){
        return "Article{" +
                "star=" + star +
                '}';
    }
}
