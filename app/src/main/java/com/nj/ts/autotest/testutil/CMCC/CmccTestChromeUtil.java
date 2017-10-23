package com.nj.ts.autotest.testutil.CMCC;

import android.content.Context;
import android.content.Intent;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nj.ts.autotest.util.Log;

public class CmccTestChromeUtil {
    private static final String TAG = CmccTestChromeUtil.class.getSimpleName();
    public static final String BROADCAST_CMCC_TEST_CHROME_FINISHED = "broadcast_cmcc_test_chrome_finished";
    private Context mContext;

    public CmccTestChromeUtil(Context context) {
        mContext = context;
    }

    public void startTest() {
        JSONArray jsonArray = new JSONArray();
        if (open()) {
            JSONObject json = new JSONObject();
            json.put("method", "open");
            json.put("resultCode", 0);
            json.put("resultMessage", "打开浏览器成功");
            jsonArray.add(json);
        } else {
            JSONObject json = new JSONObject();
            json.put("method", "open");
            json.put("resultCode", 1);
            json.put("resultMessage", "打开浏览器失败");
            jsonArray.add(json);
        }

        if (close()) {
            JSONObject json = new JSONObject();
            json.put("method", "close");
            json.put("resultCode", 0);
            json.put("resultMessage", "关闭浏览器成功");
            jsonArray.add(json);
        } else {
            JSONObject json = new JSONObject();
            json.put("method", "close");
            json.put("resultCode", 1);
            json.put("resultMessage", "关闭浏览器失败");
            jsonArray.add(json);
        }

        if (openWebPage()) {
            JSONObject json = new JSONObject();
            json.put("method", "openWebPage");
            json.put("resultCode", 0);
            json.put("resultMessage", "打开网页成功");
            jsonArray.add(json);
        } else {
            JSONObject json = new JSONObject();
            json.put("method", "openWebPage");
            json.put("resultCode", 1);
            json.put("resultMessage", "打开网页失败:网址错误");
            jsonArray.add(json);
        }

        Intent intent = new Intent();
        intent.setAction(BROADCAST_CMCC_TEST_CHROME_FINISHED);
        intent.putExtra("result",jsonArray.toJSONString());
        mContext.sendBroadcast(intent);
    }

    private boolean open() {
        Log.saveLog("aaa");
        return true;
    }

    private boolean close() {
        //Log.saveLog("aaa");
        return true;
    }

    private boolean openWebPage() {
        for (int i = 0; i < 100000; i++) {
            for (int j = 0; j < 1000; j++) {

            }
        }
        //测试代码
        return false;
    }
}
