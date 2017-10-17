package com.nj.ts.autotest.test.CMCC;

import com.nj.ts.autotest.util.Info;
import com.nj.ts.autotest.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ts on 17-9-29.
 */

public class TestMms {
    private static final String TAG = "TestMmsA";

    public void add() {
        Info.sendResult( true, "新增");
        android.util.Log.d(TAG, "MA1: ");
        Log.saveLog("aaa");
    }

    public void delete() {
        Info.sendResult(true, "删除");
        android.util.Log.d(TAG, "MA2222 ");
        //Log.saveLog("aaa");
    }

    public void modify() {
        android.util.Log.d(TAG, "修改");
        Log.saveLog("aaa");
        Info.sendResult( true, "修改");
    }

    private String getCurrentMethodName() {
        int level = 1;
        StackTraceElement[] stacks = new Throwable().getStackTrace();
        String methodName = stacks[level].getMethodName();
        return methodName;
    }

}
