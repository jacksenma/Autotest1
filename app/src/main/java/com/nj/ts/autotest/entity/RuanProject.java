package com.nj.ts.autotest.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class RuanProject implements Serializable {
    private String project;
    private boolean select;
    private ArrayList<RuanModule> module;

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public ArrayList<RuanModule> getModule() {
        return module;
    }

    public void setModule(ArrayList<RuanModule> module) {
        this.module = module;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }
}
