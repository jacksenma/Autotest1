package com.nj.ts.autotest.testutil.CMCC;

import android.content.Context;
import android.content.Intent;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class CmccTestMmsUtil {
    private static final String TAG = CmccTestMmsUtil.class.getSimpleName();
    public static final String BROADCAST_CMCC_TEST_MMS_FINISHED = "broadcast_cmcc_test_mms_finished";
    private Context mContext;

    public CmccTestMmsUtil(Context context) {
        mContext = context;
    }

    public void startTest() {
        JSONArray jsonArray = new JSONArray();
        if (add()) {
            JSONObject json = new JSONObject();
            json.put("method", "add");
            json.put("resultCode", 0);
            json.put("resultMessage", "新增成功");
            jsonArray.add(json);
        } else {
            JSONObject json = new JSONObject();
            json.put("method", "add");
            json.put("resultCode", 1);
            json.put("resultMessage", "新增失败");
            jsonArray.add(json);
        }

        if (delete()) {
            JSONObject json = new JSONObject();
            json.put("method", "delete");
            json.put("resultCode", 0);
            json.put("resultMessage", "删除成功");
            jsonArray.add(json);
        } else {
            JSONObject json = new JSONObject();
            json.put("method", "delete");
            json.put("resultCode", 1);
            json.put("resultMessage", "删除失败");
            jsonArray.add(json);
        }

        if (modify()) {
            JSONObject json = new JSONObject();
            json.put("method", "modify");
            json.put("resultCode", 0);
            json.put("resultMessage", "修改成功");
            jsonArray.add(json);
        } else {
            JSONObject json = new JSONObject();
            json.put("method", "modify");
            json.put("resultCode", 1);
            json.put("resultMessage", "修改失败");
            jsonArray.add(json);
        }

        Intent intent = new Intent();
        intent.setAction(BROADCAST_CMCC_TEST_MMS_FINISHED);
        intent.putExtra("result",jsonArray.toJSONString());
        mContext.sendBroadcast(intent);
    }

    public boolean add() {
        return true;
    }

    public boolean delete() {
        return true;
    }

    public boolean modify() {
        return true;
    }
}
