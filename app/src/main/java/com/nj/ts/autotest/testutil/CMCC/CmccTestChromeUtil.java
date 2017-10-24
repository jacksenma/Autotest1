package com.nj.ts.autotest.testutil.CMCC;

import android.content.Context;
import android.content.Intent;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.nj.ts.autotest.entity.TestResult;
import com.nj.ts.autotest.util.Constant;

public class CmccTestChromeUtil {
    private static final String TAG = CmccTestChromeUtil.class.getSimpleName();
    public static final String BROADCAST_CMCC_TEST_CHROME_FINISHED = "broadcast_cmcc_test_chrome_finished";
    private Context mContext;

    public CmccTestChromeUtil(Context context) {
        mContext = context;
    }

    public void startTest() {
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(open());
        jsonArray.add(close());
        jsonArray.add(openWebPage());

        Intent intent = new Intent();
        intent.setAction(BROADCAST_CMCC_TEST_CHROME_FINISHED);
        intent.putExtra(Constant.BUNDLE_KET_TEST_RESULT, jsonArray.toJSONString());
        mContext.sendBroadcast(intent);
    }

    private TestResult open() {
        TestResult testResult = new TestResult();
        testResult.setMethod(Thread.currentThread().getStackTrace()[2].getMethodName());
        testResult.setResultCode(Constant.TEST_RESULT_SUCCESS);
        testResult.setResultMessage("打开浏览器成功");
        return testResult;
    }

    private TestResult close() {
        TestResult testResult = new TestResult();
        testResult.setMethod(Thread.currentThread().getStackTrace()[2].getMethodName());
        testResult.setResultCode(Constant.TEST_RESULT_SUCCESS);
        testResult.setResultMessage("打开浏览器成功");
        return testResult;
    }

    private TestResult openWebPage() {
        for (int i = 0; i < 100000; i++) {
            for (int j = 0; j < 1000; j++) {

            }
        }
        TestResult testResult = new TestResult();
        testResult.setMethod(Thread.currentThread().getStackTrace()[2].getMethodName());
        testResult.setResultCode(Constant.TEST_RESULT_FAILED);
        testResult.setResultMessage("打开网页失败:网址错误");
        return testResult;
    }
}
