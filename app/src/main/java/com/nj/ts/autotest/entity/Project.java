package com.nj.ts.autotest.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class Project implements Serializable {
    private String project;
    private boolean select;
    private ArrayList<Module> module;

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public ArrayList<Module> getModule() {
        return module;
    }

    public void setModule(ArrayList<Module> module) {
        this.module = module;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }
}
