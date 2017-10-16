package com.nj.ts.autotest.test.CMCC;

import com.nj.ts.autotest.util.Info;
import com.nj.ts.autotest.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ts on 17-9-29.
 */

public class TestChrome {
    private static final String TAG = "TestChromeA";

    public void open() {
        Info.sendResult(true, "打开浏览器");
        android.util.Log.d(TAG, "CA1: ");
        Log.saveLog("aaa");
    }

    public void close() {
        Info.sendResult(true, "关闭浏览器");
        android.util.Log.d(TAG, "CA222 ");
        //Log.saveLog("aaa");
    }

    public List<Object> openWebPage() {
        for (int i = 0; i < 100000; i++) {
            for (int j = 0; j < 1000; j++) {

            }
        }
        List<Object> o = new ArrayList<>();
        o.add(true);
        o.add("测试CA3");
        Info.sendResult(false, "打开网页失败:网址错误");
        //测试代码
        return o;
    }

    private String getCurrentMethodName() {
        int level = 1;
        StackTraceElement[] stacks = new Throwable().getStackTrace();
        String methodName = stacks[level].getMethodName();
        return methodName;
    }

}
