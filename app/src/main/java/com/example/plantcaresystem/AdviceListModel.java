package com.example.plantcaresystem;

public class AdviceListModel {
    private String title;
    private String advice;

    public AdviceListModel(String title, String advice) {
        this.title = title;
        this.advice = advice;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }
}
