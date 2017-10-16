package com.nj.ts.autotest.entity;

/**
 * Created by ts on 17-10-9.
 */

public class TestResult {
    private boolean result;
    private String message;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
