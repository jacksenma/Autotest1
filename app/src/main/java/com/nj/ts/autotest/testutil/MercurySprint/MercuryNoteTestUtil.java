package com.nj.ts.autotest.testutil.MercurySprint;

import android.content.Context;
import android.content.Intent;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nj.ts.autotest.testutil.CMCC.CmccTestMmsUtil;
import com.nj.ts.autotest.testutil.CMCC.CmccTestNodeUtil;

public class MercuryNoteTestUtil {
    private static final String TAG = MercuryNoteTestUtil.class.getSimpleName();
    public static final String BROADCAST_MERCURY_TEST_NODE_FINISHED = "broadcast_mercury_test_node_finished";
    private Context mContext;

    public MercuryNoteTestUtil(Context context) {
        mContext = context;
    }

    public void startTest() {
        JSONArray jsonArray = new JSONArray();
        if (newFile()) {
            JSONObject json = new JSONObject();
            json.put("method", "newFile");
            json.put("resultCode", 0);
            json.put("resultMessage", "测试成功");
            jsonArray.add(json);
        } else {
            JSONObject json = new JSONObject();
            json.put("method", "newFile");
            json.put("resultCode", 1);
            json.put("resultMessage", "新建文件失败:没有权限");
            jsonArray.add(json);
        }

        if (deleteFile()) {
            JSONObject json = new JSONObject();
            json.put("method", "deleteFile");
            json.put("resultCode", 0);
            json.put("resultMessage", "删除文件");
            jsonArray.add(json);
        } else {
            JSONObject json = new JSONObject();
            json.put("method", "deleteFile");
            json.put("resultCode", 1);
            json.put("resultMessage", "删除失败");
            jsonArray.add(json);
        }

        if (saveFile()) {
            JSONObject json = new JSONObject();
            json.put("method", "saveFile");
            json.put("resultCode", 0);
            json.put("resultMessage", "保存文件");
            jsonArray.add(json);
        } else {
            JSONObject json = new JSONObject();
            json.put("method", "saveFile");
            json.put("resultCode", 1);
            json.put("resultMessage", "保存失败");
            jsonArray.add(json);
        }

        Intent intent = new Intent();
        intent.setAction(BROADCAST_MERCURY_TEST_NODE_FINISHED);
        intent.putExtra("result",jsonArray.toJSONString());
        mContext.sendBroadcast(intent);
    }

    private boolean newFile() {
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                for (int k = 0; k < 100; k++) {

                }
            }
        }
        //测试代码
        return false;
    }

    private boolean deleteFile() {
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                for (int k = 0; k < 100; k++) {

                }
            }
        }
        //测试代码
        return true;
    }

    private boolean saveFile() {
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                for (int k = 0; k < 100; k++) {

                }
            }
        }
        //测试代码
        return true;
    }
}
