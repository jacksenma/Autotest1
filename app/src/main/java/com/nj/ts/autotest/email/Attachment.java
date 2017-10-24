package com.nj.ts.autotest.email;

import java.io.Serializable;

/**
 * Created by root on 10/17/17.
 */

public class Attachment implements Serializable{
    private String path;
    private String name;

    public Attachment(String name, String path) {
        this.path = path;
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
