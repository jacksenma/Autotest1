package com.nj.ts.autotest.testutil.MercurySprint;

import android.content.Context;
import android.content.Intent;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.nj.ts.autotest.entity.TestResult;
import com.nj.ts.autotest.util.Constant;

public class MercuryNoteTestUtil {
    private static final String TAG = MercuryNoteTestUtil.class.getSimpleName();
    public static final String BROADCAST_MERCURY_TEST_NODE_FINISHED = "broadcast_mercury_test_node_finished";
    private Context mContext;

    public MercuryNoteTestUtil(Context context) {
        mContext = context;
    }

    public void startTest() {
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(newFile());
        jsonArray.add(deleteFile());
        jsonArray.add(saveFile());

        Intent intent = new Intent();
        intent.setAction(BROADCAST_MERCURY_TEST_NODE_FINISHED);
        intent.putExtra(Constant.BUNDLE_KET_TEST_RESULT, jsonArray.toJSONString());
        mContext.sendBroadcast(intent);
    }

    /**
     * @return all test items
     */
    public String[] getTestItems() {
        return new String[]{
                "newFile",
                "deleteFile",
                "saveFile",
        };
    }

    public TestResult newFile() {
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                for (int k = 0; k < 100; k++) {

                }
            }
        }
        TestResult testResult = new TestResult();
        testResult.setMethod(Thread.currentThread().getStackTrace()[2].getMethodName());
        testResult.setResultCode(Constant.TEST_RESULT_FAILED);
        testResult.setResultMessage("新建文件失败:没有权限");
        return testResult;
    }

    public TestResult deleteFile() {
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                for (int k = 0; k < 100; k++) {

                }
            }
        }
        TestResult testResult = new TestResult();
        testResult.setMethod(Thread.currentThread().getStackTrace()[2].getMethodName());
        testResult.setResultCode(Constant.TEST_RESULT_SUCCESS);
        testResult.setResultMessage("删除文件");
        return testResult;
    }

    public TestResult saveFile() {
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                for (int k = 0; k < 100; k++) {

                }
            }
        }
        TestResult testResult = new TestResult();
        testResult.setMethod(Thread.currentThread().getStackTrace()[2].getMethodName());
        testResult.setResultCode(Constant.TEST_RESULT_SUCCESS);
        testResult.setResultMessage("保存文件");
        return testResult;
    }
}
