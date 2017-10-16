package com.nj.ts.autotest.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ts on 17-9-28.
 */
public class Module implements Serializable{
    private String name;
    private boolean isChecked = true;
    private int state = 2;//表示被测模块的当前状态：0为测试失败，1为测试通过，2为待测试，3为正在测试
    private List<Function> functions;
    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Function> getFunctions() {
        return functions;
    }

    public void setFunctions(List functions) {
        this.functions = functions;
    }
}
