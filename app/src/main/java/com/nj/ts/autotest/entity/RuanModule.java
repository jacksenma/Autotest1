package com.nj.ts.autotest.entity;

public class RuanModule {
    private String name;
    private boolean select;
    private boolean isAllSuccess;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public boolean isAllSuccess() {
        return isAllSuccess;
    }

    public void setAllSuccess(boolean allSuccess) {
        isAllSuccess = allSuccess;
    }
}
