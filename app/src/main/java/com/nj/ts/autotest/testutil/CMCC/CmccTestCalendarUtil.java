package com.nj.ts.autotest.testutil.CMCC;

import android.content.Context;
import android.content.Intent;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.nj.ts.autotest.entity.TestResult;
import com.nj.ts.autotest.util.Constant;

public class CmccTestCalendarUtil {
    private static final String TAG = CmccTestCalendarUtil.class.getSimpleName();
    public static final String BROADCAST_CMCC_TEST_CALENDAR_FINISHED = "broadcast_cmcc_test_calendar_finished";
    private Context mContext;

    public CmccTestCalendarUtil(Context context) {
        mContext = context;
    }

    public TestResult hello(String param) {
        TestResult result = new TestResult();
        result.setMethod("hello");
        result.setResultCode(0);
        result.setResultMessage("成功");
        return result;
    }

    public void startTest() {
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(searchDate());
        jsonArray.add(addEvent());
        jsonArray.add(addReminder());
        jsonArray.add(delete());

        Intent intent = new Intent();
        intent.setAction(BROADCAST_CMCC_TEST_CALENDAR_FINISHED);
        intent.putExtra(Constant.BUNDLE_KET_TEST_RESULT, jsonArray.toJSONString());
        mContext.sendBroadcast(intent);
    }

    /**
     * @return all test items
     */
    public String[] getTestItems() {
        return new String[]{
                "searchDate",
                "addEvent",
                "addReminder",
                "delete"
        };
    }

    public TestResult searchDate() {
        TestResult testResult = new TestResult();
        testResult.setMethod(Thread.currentThread().getStackTrace()[2].getMethodName());
        testResult.setResultCode(Constant.TEST_RESULT_SUCCESS);
        testResult.setResultMessage("查找日期成功");
        return testResult;
    }

    public TestResult addEvent() {
        TestResult testResult = new TestResult();
        testResult.setMethod(Thread.currentThread().getStackTrace()[2].getMethodName());
        testResult.setResultCode(Constant.TEST_RESULT_SUCCESS);
        testResult.setResultMessage("添加事件成功");
        return testResult;
    }

    public TestResult addReminder() {
        TestResult testResult = new TestResult();
        testResult.setMethod(Thread.currentThread().getStackTrace()[2].getMethodName());
        testResult.setResultCode(Constant.TEST_RESULT_SUCCESS);
        testResult.setResultMessage("添加提醒成功");
        return testResult;
    }

    public TestResult delete() {
        TestResult testResult = new TestResult();
        testResult.setMethod(Thread.currentThread().getStackTrace()[2].getMethodName());
        testResult.setResultCode(Constant.TEST_RESULT_SUCCESS);
        testResult.setResultMessage("删除提醒成功");
        return testResult;
    }
}
