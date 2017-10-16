package com.nj.ts.autotest.test.MERCURY;

import com.nj.ts.autotest.util.Info;

/**
 * Created by ts on 17-9-29.
 */

public class TestNote {
    public boolean NB1() {
        Info.sendResult(true, "测试NB1");
        //测试代码
        return true;
    }

    public boolean NB2() {
        Info.sendResult( false, "错误信息");
        //测试代码
        return false;
    }

    public boolean NB3() {
        Info.sendResult( true, "测试NB3");
        //测试代码
        return true;
    }

    private String function(String a) {
        return a + "c";
    }

}
