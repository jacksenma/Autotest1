package com.nj.ts.autotest.test.MercurySprint;

import com.nj.ts.autotest.util.Info;
import com.nj.ts.autotest.util.Log;

/**
 * Created by ts on 17-9-29.
 */

public class CalendarTest {
    private static final String TAG = "TestCalendarA";

    public void searchDate() {
        Info.sendResult(true, "查找日期");
        android.util.Log.d(TAG, "CA1: ");
//        Log.saveLog("CA1");
    }

    public void addEvent() {
        Info.sendResult(true, "添加事件");
        android.util.Log.d(TAG, "CA222 ");
        //Log.saveLog("aaa");
    }

    public void addReminder() {
        android.util.Log.d(TAG, "添加提醒");
//        Log.saveLog("CA2");
        Info.sendResult(true, "添加提醒");
    }

    public void delete() {
        android.util.Log.d(TAG, "添加提醒");
//        Log.saveLog("CA2");
        Info.sendResult(true, "删除");
    }



}

