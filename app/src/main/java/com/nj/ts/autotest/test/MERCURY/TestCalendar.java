package com.nj.ts.autotest.test.MERCURY;

import com.nj.ts.autotest.util.Info;

/**
 * Created by ts on 17-9-29.
 */

public class TestCalendar {
    private void CB1() {
        Info.sendResult( true, "测试CB1");
    }

    private void CB2() {
        Info.sendResult( true, "测试CB2");
    }

    private void CB3() {
        Info.sendResult(true, "测试CB3");
    }

    public boolean CB4() {
        //测试代码
        Info.sendResult( true, "测试CB4");
        return false;
    }

    private String getCurrentMethodName() {
        int level = 1;
        StackTraceElement[] stacks = new Throwable().getStackTrace();
        String methodName = stacks[level].getMethodName();
        return methodName;
    }
}
