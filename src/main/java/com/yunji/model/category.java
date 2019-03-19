package com.yunji.model;

public class category {
    private String circle;
    private String scenario;

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

    @Override
    public String toString() {
        return "category{" +
                "circle='" + circle + '\'' +
                ", scenario='" + scenario + '\'' +
                '}';
    }
}
