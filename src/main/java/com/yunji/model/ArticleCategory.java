package com.yunji.model;

public class ArticleCategory extends category{
    private String circle;
    private String scenario;
    private String action;
    private Integer visibility;

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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Integer getVisibility() {
        return visibility;
    }

    public void setVisibility(Integer visibility) {
        this.visibility = visibility;
    }

    @Override
    public String toString() {
        return "ArticleCategory{" +
                "circle='" + circle + '\'' +
                ", scenario='" + scenario + '\'' +
                ", action='" + action + '\'' +
                ", visibility=" + visibility +
                '}';
    }
}
