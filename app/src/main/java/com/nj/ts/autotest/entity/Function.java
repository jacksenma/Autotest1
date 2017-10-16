package com.nj.ts.autotest.entity;

import java.io.Serializable;

/**
 * Created by ts on 17-9-30.
 */

public class Function implements Serializable {
    private String name;//方法名
    private int state = 2;//表示被测方法的当前状态：0为测试失败，1为测试通过，2为待测试，3为正在测试
    private String tag;//测试人员设置的信息，用来展示这个方法具体测试的是什么

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
