package com.nj.ts.autotest.entity;

import java.io.Serializable;

public class TestItem implements Serializable{
    private String title;
    private String result;

    public TestItem(String title, String result) {
        this.title = title;
        this.result = result;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
