package com.nj.ts.autotest.testutil.CMCC;

import android.content.Context;
import android.content.Intent;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.nj.ts.autotest.entity.TestResult;
import com.nj.ts.autotest.util.Constant;

public class CmccTestMmsUtil {
    private static final String TAG = CmccTestMmsUtil.class.getSimpleName();
    public static final String BROADCAST_CMCC_TEST_MMS_FINISHED = "broadcast_cmcc_test_mms_finished";
    private Context mContext;

    public CmccTestMmsUtil(Context context) {
        mContext = context;
    }

    public void startTest() {
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(add());
        jsonArray.add(delete());
        jsonArray.add(modify());

        Intent intent = new Intent();
        intent.setAction(BROADCAST_CMCC_TEST_MMS_FINISHED);
        intent.putExtra(Constant.BUNDLE_KET_TEST_RESULT, jsonArray.toJSONString());
        mContext.sendBroadcast(intent);
    }

    /**
     * @return all test items
     */
    public String[] getTestItems() {
        return new String[]{
                "add",
                "delete",
                "modify",
        };
    }

    public TestResult add() {
        TestResult testResult = new TestResult();
        testResult.setMethod(Thread.currentThread().getStackTrace()[2].getMethodName());
        testResult.setResultCode(Constant.TEST_RESULT_SUCCESS);
        testResult.setResultMessage("新增成功");
        return testResult;
    }

    public TestResult delete() {
        TestResult testResult = new TestResult();
        testResult.setMethod(Thread.currentThread().getStackTrace()[2].getMethodName());
        testResult.setResultCode(Constant.TEST_RESULT_SUCCESS);
        testResult.setResultMessage("删除成功");
        return testResult;
    }

    public TestResult modify() {
        TestResult testResult = new TestResult();
        testResult.setMethod(Thread.currentThread().getStackTrace()[2].getMethodName());
        testResult.setResultCode(Constant.TEST_RESULT_SUCCESS);
        testResult.setResultMessage("修改成功");
        return testResult;
    }
}
