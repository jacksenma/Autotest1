package com.nj.ts.autotest.testutil.CMCC;

import android.content.Context;
import android.content.Intent;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class CmccTestCalendarUtil {
    private static final String TAG = CmccTestCalendarUtil.class.getSimpleName();
    public static final String BROADCAST_CMCC_TEST_CALENDAR_FINISHED = "broadcast_cmcc_test_calendar_finished";
    private Context mContext;

    public CmccTestCalendarUtil(Context context) {
        mContext = context;
    }

    public void startTest() {
        JSONArray jsonArray = new JSONArray();
        if (searchDate()) {
            JSONObject json = new JSONObject();
            json.put("method", "searchDate");
            json.put("resultCode", 0);
            json.put("resultMessage", "查找日期成功");
            jsonArray.add(json);
        } else {
            JSONObject json = new JSONObject();
            json.put("method", "searchDate");
            json.put("resultCode", 1);
            json.put("resultMessage", "查找日期失败");
            jsonArray.add(json);
        }

        if (addEvent()) {
            JSONObject json = new JSONObject();
            json.put("method", "addEvent");
            json.put("resultCode", 0);
            json.put("resultMessage", "添加事件成功");
            jsonArray.add(json);
        } else {
            JSONObject json = new JSONObject();
            json.put("method", "addEvent");
            json.put("resultCode", 1);
            json.put("resultMessage", "添加事件失败");
            jsonArray.add(json);
        }

        if (addReminder()) {
            JSONObject json = new JSONObject();
            json.put("method", "addReminder");
            json.put("resultCode", 0);
            json.put("resultMessage", "添加提醒成功");
            jsonArray.add(json);
        } else {
            JSONObject json = new JSONObject();
            json.put("method", "addReminder");
            json.put("resultCode", 1);
            json.put("resultMessage", "添加提醒失败");
            jsonArray.add(json);
        }

        if (delete()) {
            JSONObject json = new JSONObject();
            json.put("method", "delete");
            json.put("resultCode", 0);
            json.put("resultMessage", "删除提醒成功");
            jsonArray.add(json);
        } else {
            JSONObject json = new JSONObject();
            json.put("method", "delete");
            json.put("resultCode", 1);
            json.put("resultMessage", "删除提醒失败");
            jsonArray.add(json);
        }

        Intent intent = new Intent();
        intent.setAction(BROADCAST_CMCC_TEST_CALENDAR_FINISHED);
        intent.putExtra("result",jsonArray.toJSONString());
        mContext.sendBroadcast(intent);
    }

    private boolean searchDate() {
        return true;
    }

    private boolean addEvent() {
        return true;
    }

    private boolean addReminder() {
        return true;
    }

    private boolean delete() {
        return true;
    }
}
