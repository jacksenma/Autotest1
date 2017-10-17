package com.nj.ts.autotest.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ts on 17-9-28.
 */

public class Config implements Serializable {
    private String projectName;
    private List<Module> module;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public List<Module> getModule() {
        return module;
    }

    public void setModule(List module) {
        this.module = module;
    }
}
