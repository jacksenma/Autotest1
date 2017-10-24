package com.nj.ts.autotest.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.ts.autotest.R;
import com.nj.ts.autotest.adapter.TestingFunctionAdapter;
import com.nj.ts.autotest.adapter.TestingModuleAdapter;
import com.nj.ts.autotest.email.Attachment;
import com.nj.ts.autotest.email.MailServer;
import com.nj.ts.autotest.entity.Module;
import com.nj.ts.autotest.entity.Project;
import com.nj.ts.autotest.entity.TestResult;
import com.nj.ts.autotest.smb.SmbConfig;
import com.nj.ts.autotest.testutil.CMCC.CmccTestChromeUtil;
import com.nj.ts.autotest.testutil.CMCC.CmccTestMmsUtil;
import com.nj.ts.autotest.testutil.CMCC.CmccTestNodeUtil;
import com.nj.ts.autotest.testutil.CMCC.CmccTestCalendarUtil;
import com.nj.ts.autotest.testutil.MercurySprint.MercuryCalendarTestUtil;
import com.nj.ts.autotest.testutil.MercurySprint.MercuryNoteTestUtil;
import com.nj.ts.autotest.testutil.MercurySprint.MercuryOmaTestUtil;
import com.nj.ts.autotest.util.Constant;
import com.nj.ts.autotest.util.ToastUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TestingActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = TestingActivity.class.getSimpleName();
    public static final String BUNDLE_KEY_PROJECT = "project";
    public static final String BUNDLE_KEY_MODULE = "module";

    public static final int REQUEST_CODE = 0X01;
    public static final int RESULT_CODE_RETEST = 0X02;
    public static final int RESULT_CODE_TEST_ALONE = 0X03;

    //OMA
    private static final String BROADCAST_OMA_TEST_FINISHED = "baroadcast_oma_test_finished";

    private Button mShowResultButton;
    private ProgressBar mProgressBar;
    private Project mSelectProject;
    private ArrayList<Module> mModuleArrayList;
    private ArrayList<TestResult> mCurrentModuleFunctionTestResult;

    private HashMap<String, ArrayList<TestResult>> mTestResultMap;
    private RecyclerView mModuleRecyclerView, mFunctionRecycleView;
    private TestingModuleAdapter mTestModuleAdapter;
    private TestingFunctionAdapter mTestFunctionAdapter;
    private int mCompleteModuleCount = 0;

    private static final int MSG_REFRESH_UI = 0X01;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_REFRESH_UI:
                    String node = (String) msg.obj;
                    ToastUtil.showShortToast(TestingActivity.this, node + "测试完成");
                    refreshUi();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);
        initData();
        initView();
        registerBroadcast();
        sendTestNotification();
    }

    private void initData() {
        mModuleArrayList = new ArrayList<>();
        mTestResultMap = new HashMap<>();

        Bundle bundle = getIntent().getExtras();
        mSelectProject = JSON.parseObject(bundle.getString(BUNDLE_KEY_PROJECT), Project.class);
        ArrayList<String> arrayList = bundle.getStringArrayList(BUNDLE_KEY_MODULE);
        for (int i = 0; i < arrayList.size(); i++) {
            Module module = new Module();
            module.setName(arrayList.get(i));
            if (i == 0) {
                module.setSelect(true);
            } else {
                module.setSelect(false);
            }
            mModuleArrayList.add(module);

            if (mSelectProject.getProject().equals(Constant.PROJECT_CMCC)) {
                if (module.getName().equals(Constant.MODULE_CMSS_TEST_NODE)) {
                    ArrayList<TestResult> functionTestResults = new ArrayList<>();
                    CmccTestNodeUtil util = new CmccTestNodeUtil(this);
                    for (int j = 0; j < util.getTestItems().length; j++) {
                        TestResult testResult = new TestResult();
                        testResult.setMethod(util.getTestItems()[j]);
                        testResult.setResultCode(Constant.TEST_RESULT_TESTING);
                        functionTestResults.add(testResult);
                    }
                    mTestResultMap.put(arrayList.get(i), functionTestResults);
                } else if (module.getName().equals(Constant.MODULE_CMSS_TEST_CALENDAR)) {
                    ArrayList<TestResult> functionTestResults = new ArrayList<>();
                    CmccTestCalendarUtil util = new CmccTestCalendarUtil(this);
                    for (int j = 0; j < util.getTestItems().length; j++) {
                        TestResult testResult = new TestResult();
                        testResult.setMethod(util.getTestItems()[j]);
                        testResult.setResultCode(Constant.TEST_RESULT_TESTING);
                        functionTestResults.add(testResult);
                    }
                    mTestResultMap.put(arrayList.get(i), functionTestResults);
                } else if (module.getName().equals(Constant.MODULE_CMSS_TEST_CHROME)) {
                    ArrayList<TestResult> functionTestResults = new ArrayList<>();
                    CmccTestChromeUtil util = new CmccTestChromeUtil(this);
                    for (int j = 0; j < util.getTestItems().length; j++) {
                        TestResult testResult = new TestResult();
                        testResult.setMethod(util.getTestItems()[j]);
                        testResult.setResultCode(Constant.TEST_RESULT_TESTING);
                        functionTestResults.add(testResult);
                    }
                    mTestResultMap.put(arrayList.get(i), functionTestResults);
                } else if (module.getName().equals(Constant.MODULE_CMSS_TEST_CMMS)) {
                    ArrayList<TestResult> functionTestResults = new ArrayList<>();
                    CmccTestMmsUtil util = new CmccTestMmsUtil(this);
                    for (int j = 0; j < util.getTestItems().length; j++) {
                        TestResult testResult = new TestResult();
                        testResult.setMethod(util.getTestItems()[j]);
                        testResult.setResultCode(Constant.TEST_RESULT_TESTING);
                        functionTestResults.add(testResult);
                    }
                    mTestResultMap.put(arrayList.get(i), functionTestResults);
                }
            } else if (mSelectProject.getProject().equals("MercurySprint")) {
                if (module.getName().equals(Constant.MODULE_MERCURY_TEST_NODE)) {
                    ArrayList<TestResult> functionTestResults = new ArrayList<>();
                    MercuryNoteTestUtil util = new MercuryNoteTestUtil(this);
                    for (int j = 0; j < util.getTestItems().length; j++) {
                        TestResult testResult = new TestResult();
                        testResult.setMethod(util.getTestItems()[j]);
                        testResult.setResultCode(Constant.TEST_RESULT_TESTING);
                        functionTestResults.add(testResult);
                    }
                    mTestResultMap.put(arrayList.get(i), functionTestResults);
                } else if (module.getName().equals(Constant.MODULE_MERCURY_TEST_CALENDAR)) {
                    ArrayList<TestResult> functionTestResults = new ArrayList<>();
                    MercuryCalendarTestUtil util = new MercuryCalendarTestUtil(this);
                    for (int j = 0; j < util.getTestItems().length; j++) {
                        TestResult testResult = new TestResult();
                        testResult.setMethod(util.getTestItems()[j]);
                        testResult.setResultCode(Constant.TEST_RESULT_TESTING);
                        functionTestResults.add(testResult);
                    }
                    mTestResultMap.put(arrayList.get(i), functionTestResults);
                } else if (module.getName().equals("OMADMTest")) {
                    ArrayList<TestResult> functionTestResults = new ArrayList<>();
                    MercuryOmaTestUtil util = new MercuryOmaTestUtil(this);
                    for (int j = 0; j < util.getTestItems().length; j++) {
                        TestResult testResult = new TestResult();
                        testResult.setMethod(util.getTestItems()[j]);
                        testResult.setResultCode(Constant.TEST_RESULT_TESTING);
                        functionTestResults.add(testResult);
                    }
                    mTestResultMap.put(arrayList.get(i), functionTestResults);
                }
            }
            if (i == 0) {
                mCurrentModuleFunctionTestResult = new ArrayList<>();
                mCurrentModuleFunctionTestResult.addAll(mTestResultMap.get(arrayList.get(i)));
            }
        }
    }

    private void initView() {
        mShowResultButton = (Button) findViewById(R.id.btn_show_result);
        mShowResultButton.setOnClickListener(this);
        mModuleRecyclerView = (RecyclerView) findViewById(R.id.module_list_recyclerview);
        mFunctionRecycleView = (RecyclerView) findViewById(R.id.function_list_recyclerview);
        mProgressBar = (ProgressBar) findViewById(R.id.progress);

        LinearLayoutManager moduleLayoutManager = new LinearLayoutManager(this);
        mModuleRecyclerView.setLayoutManager(moduleLayoutManager);
        mModuleRecyclerView.setHasFixedSize(true);
        mTestModuleAdapter = new TestingModuleAdapter(mModuleArrayList, this);
        mTestModuleAdapter.setOnItemClickListener(new TestingModuleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                for (int i = 0; i < mModuleArrayList.size(); i++) {
                    if (i == position) {
                        mModuleArrayList.get(i).setSelect(true);
                    } else {
                        mModuleArrayList.get(i).setSelect(false);
                    }
                    mTestModuleAdapter.notifyDataSetChanged();
                }

                for (int i = 0; i < mModuleArrayList.size(); i++) {
                    Module module = mModuleArrayList.get(i);
                    if (module.isSelect()) {
                        ArrayList<TestResult> testResults = mTestResultMap.get(module.getName());
                        mCurrentModuleFunctionTestResult.clear();
                        mCurrentModuleFunctionTestResult.addAll(testResults);
                        mTestFunctionAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            }
        });
        mModuleRecyclerView.setAdapter(mTestModuleAdapter);

        GridLayoutManager layoutManage = new GridLayoutManager(this, 2);
        mFunctionRecycleView.setLayoutManager(layoutManage);
        mFunctionRecycleView.setHasFixedSize(true);
        mTestFunctionAdapter = new TestingFunctionAdapter(mCurrentModuleFunctionTestResult, this);
        mTestFunctionAdapter.setOnItemLongClickListener(new TestingFunctionAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                Log.d(TAG, "ruan onItemLongClick and position is " + position);
                testAlone(position);
            }
        });
        mFunctionRecycleView.setAdapter(mTestFunctionAdapter);
    }

    /**
     * 发送测试广播
     */
    private void sendTestNotification() {
        if (mSelectProject.getProject().equals("Cmcc")) {
            for (int i = 0; i < mModuleArrayList.size(); i++) {
                Module module = mModuleArrayList.get(i);
                if (module.getName().equals(Constant.MODULE_CMSS_TEST_NODE)) {
                    CmccTestNodeUtil util = new CmccTestNodeUtil(this);
                    util.startTest();
                } else if (module.getName().equals(Constant.MODULE_CMSS_TEST_CALENDAR)) {
                    CmccTestCalendarUtil util = new CmccTestCalendarUtil(this);
                    util.startTest();
                } else if (module.getName().equals(Constant.MODULE_CMSS_TEST_CHROME)) {
                    CmccTestChromeUtil util = new CmccTestChromeUtil(this);
                    util.startTest();
                } else if (module.getName().equals(Constant.MODULE_CMSS_TEST_CMMS)) {
                    CmccTestMmsUtil util = new CmccTestMmsUtil(this);
                    util.startTest();
                }
            }
        } else if (mSelectProject.getProject().equals("MercurySprint")) {
            for (int i = 0; i < mModuleArrayList.size(); i++) {
                Module module = mModuleArrayList.get(i);
                if (module.getName().equals(Constant.MODULE_MERCURY_TEST_NODE)) {
                    MercuryNoteTestUtil util = new MercuryNoteTestUtil(this);
                    util.startTest();
                } else if (module.getName().equals(Constant.MODULE_MERCURY_TEST_CALENDAR)) {
                    MercuryCalendarTestUtil util = new MercuryCalendarTestUtil(this);
                    util.startTest();
                } else if (module.getName().equals("OMADMTest")) {
                    MercuryOmaTestUtil util = new MercuryOmaTestUtil(this);
                    util.startTest();
                }
            }
        }
    }

    private void refreshUi() {
        //刷新当前选中module的结果
        for (int i = 0; i < mModuleArrayList.size(); i++) {
            Module module = mModuleArrayList.get(i);
            if (module.isSelect()) {
                ArrayList<TestResult> testResults = mTestResultMap.get(module.getName());
                mCurrentModuleFunctionTestResult.clear();
                mCurrentModuleFunctionTestResult.addAll(testResults);
                mTestFunctionAdapter.notifyDataSetChanged();
                break;
            }
        }

        //刷新左侧module的字体颜色
        for (int i = 0; i < mModuleArrayList.size(); i++) {
            Module module = mModuleArrayList.get(i);
            ArrayList<TestResult> testResults = mTestResultMap.get(module.getName());
            if (!testResults.isEmpty()) {
                boolean isAllSuccess = true;
                for (int j = 0; j < testResults.size(); j++) {
                    TestResult result = testResults.get(j);
                    if (result.getResultCode() != Constant.TEST_RESULT_SUCCESS) {
                        isAllSuccess = false;
                        break;
                    }
                }
                module.setAllSuccess(isAllSuccess);
            }
        }
        mTestModuleAdapter.notifyDataSetChanged();


        mCompleteModuleCount++;
        if (mCompleteModuleCount == mModuleArrayList.size()) {
            mProgressBar.setVisibility(View.GONE);

            sendTestResultByEmail();

            Intent intent = new Intent();
            intent.setClass(this, RuanTestResultActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(RuanTestResultActivity.BUNDLE_KEY_MODULE, JSON.toJSONString(mModuleArrayList));
            bundle.putString(RuanTestResultActivity.BUNDLE_KEY_RESULT, JSON.toJSONString(mTestResultMap));
            bundle.putString(RuanTestResultActivity.BUNDLE_KEY_PROJECT, JSON.toJSONString(mSelectProject));
            intent.putExtras(bundle);
            startActivityForResult(intent, REQUEST_CODE);
        }

    }

    private void registerBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_OMA_TEST_FINISHED);
        intentFilter.addAction(CmccTestNodeUtil.BROADCAST_CMCC_TEST_NODE_FINISHED);
        intentFilter.addAction(CmccTestCalendarUtil.BROADCAST_CMCC_TEST_CALENDAR_FINISHED);
        intentFilter.addAction(CmccTestChromeUtil.BROADCAST_CMCC_TEST_CHROME_FINISHED);
        intentFilter.addAction(CmccTestMmsUtil.BROADCAST_CMCC_TEST_MMS_FINISHED);
        intentFilter.addAction(MercuryNoteTestUtil.BROADCAST_MERCURY_TEST_NODE_FINISHED);
        intentFilter.addAction(MercuryCalendarTestUtil.BROADCAST_MERCURY_TEST_CALENDAR_FINISHED);
        registerReceiver(mReceiver, intentFilter);
    }

    private MyBroadcastReceiver mReceiver = new MyBroadcastReceiver();

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_show_result: {
                Intent intent = new Intent();
                intent.setClass(this, RuanTestResultActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(RuanTestResultActivity.BUNDLE_KEY_MODULE, JSON.toJSONString(mModuleArrayList));
                bundle.putString(RuanTestResultActivity.BUNDLE_KEY_RESULT, JSON.toJSONString(mTestResultMap));
                bundle.putString(RuanTestResultActivity.BUNDLE_KEY_PROJECT, JSON.toJSONString(mSelectProject));
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_CODE);
            }
            break;
            default:
                break;
        }
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "ruan action is " + intent.getAction() + " and data is " + intent.getStringExtra("result"));
            String action = intent.getAction();
            String data = intent.getStringExtra("result");
            if (!TextUtils.isEmpty(intent.getAction())) {
                if (intent.getAction().equals(CmccTestNodeUtil.BROADCAST_CMCC_TEST_NODE_FINISHED)) {
                    ArrayList<TestResult> testResults = (ArrayList<TestResult>) JSONArray.parseArray(data, TestResult.class);
                    mTestResultMap.put(Constant.MODULE_CMSS_TEST_NODE, testResults);
                    Message msg = new Message();
                    msg.what = MSG_REFRESH_UI;
                    msg.obj = Constant.MODULE_CMSS_TEST_NODE;
                    mHandler.sendMessage(msg);
                } else if (action.equals(CmccTestCalendarUtil.BROADCAST_CMCC_TEST_CALENDAR_FINISHED)) {
                    ArrayList<TestResult> testResults = (ArrayList<TestResult>) JSONArray.parseArray(data, TestResult.class);
                    mTestResultMap.put(Constant.MODULE_CMSS_TEST_CALENDAR, testResults);
                    Message msg = new Message();
                    msg.what = MSG_REFRESH_UI;
                    msg.obj = Constant.MODULE_CMSS_TEST_CALENDAR;
                    mHandler.sendMessage(msg);
                } else if (action.equals(CmccTestChromeUtil.BROADCAST_CMCC_TEST_CHROME_FINISHED)) {
                    ArrayList<TestResult> testResults = (ArrayList<TestResult>) JSONArray.parseArray(data, TestResult.class);
                    mTestResultMap.put(Constant.MODULE_CMSS_TEST_CHROME, testResults);
                    Message msg = new Message();
                    msg.what = MSG_REFRESH_UI;
                    msg.obj = Constant.MODULE_CMSS_TEST_CHROME;
                    mHandler.sendMessage(msg);
                } else if (action.equals(CmccTestMmsUtil.BROADCAST_CMCC_TEST_MMS_FINISHED)) {
                    ArrayList<TestResult> testResults = (ArrayList<TestResult>) JSONArray.parseArray(data, TestResult.class);
                    mTestResultMap.put(Constant.MODULE_CMSS_TEST_CMMS, testResults);
                    Message msg = new Message();
                    msg.what = MSG_REFRESH_UI;
                    msg.obj = Constant.MODULE_CMSS_TEST_CMMS;
                    mHandler.sendMessage(msg);
                } else if (action.equals(MercuryNoteTestUtil.BROADCAST_MERCURY_TEST_NODE_FINISHED)) {
                    ArrayList<TestResult> testResults = (ArrayList<TestResult>) JSONArray.parseArray(data, TestResult.class);
                    mTestResultMap.put(Constant.MODULE_MERCURY_TEST_NODE, testResults);
                    Message msg = new Message();
                    msg.what = MSG_REFRESH_UI;
                    msg.obj = Constant.MODULE_MERCURY_TEST_NODE;
                    mHandler.sendMessage(msg);
                } else if (action.equals(MercuryCalendarTestUtil.BROADCAST_MERCURY_TEST_CALENDAR_FINISHED)) {
                    ArrayList<TestResult> testResults = (ArrayList<TestResult>) JSONArray.parseArray(data, TestResult.class);
                    mTestResultMap.put(Constant.MODULE_MERCURY_TEST_CALENDAR, testResults);
                    Message msg = new Message();
                    msg.what = MSG_REFRESH_UI;
                    msg.obj = Constant.MODULE_MERCURY_TEST_CALENDAR;
                    mHandler.sendMessage(msg);
                } else if (intent.getAction().equals(BROADCAST_OMA_TEST_FINISHED)) {
                    ArrayList<TestResult> testResults = (ArrayList<TestResult>) JSONArray.parseArray(data, TestResult.class);
                    mTestResultMap.put(Constant.MODULE_OMA, testResults);
                    Message msg = new Message();
                    msg.what = MSG_REFRESH_UI;
                    msg.obj = Constant.MODULE_OMA;
                    mHandler.sendMessage(msg);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_CODE_RETEST) {
                finish();
            } else if (resultCode == RESULT_CODE_TEST_ALONE) {

            }
        }
    }

    private void testAlone(int position) {
        String testItem = mCurrentModuleFunctionTestResult.get(position).getMethod();
        Module module = null;
        TestResult testResult = null;
        for (int i = 0; i < mModuleArrayList.size(); i++) {
            if (mModuleArrayList.get(i).isSelect()) {
                module = mModuleArrayList.get(i);
                break;
            }
        }

        if (module.getName().equals("OMADMTest")) {
            ToastUtil.showShortToast(this, "OMA 模块暂不支持单测");
            return;
        }
        ToastUtil.showShortToast(this, "开始测试" + testItem + "方法");

        if (mSelectProject.getProject().equals("Cmcc")) {
            if (module.getName().equals(Constant.MODULE_CMSS_TEST_NODE)) {
                CmccTestNodeUtil util = new CmccTestNodeUtil(this);
                Method method = null;
                try {
                    Log.d(TAG, "ruan and item is " + testItem);
                    method = util.getClass().getDeclaredMethod(testItem);
                    testResult = (TestResult) method.invoke(util);
                } catch (Exception e) {
                    Log.d(TAG, "ruan exception is " + e.getLocalizedMessage());
                    e.printStackTrace();
                }
            } else if (module.getName().equals(Constant.MODULE_CMSS_TEST_CALENDAR)) {
                CmccTestCalendarUtil util = new CmccTestCalendarUtil(this);
                Method method = null;
                try {
                    Log.d(TAG, "ruan and item is " + testItem);
                    method = util.getClass().getDeclaredMethod(testItem);
                    testResult = (TestResult) method.invoke(util);
                } catch (Exception e) {
                    Log.d(TAG, "ruan exception is " + e.getLocalizedMessage());
                    e.printStackTrace();
                }
            } else if (module.getName().equals(Constant.MODULE_CMSS_TEST_CHROME)) {
                CmccTestChromeUtil util = new CmccTestChromeUtil(this);
                Method method = null;
                try {
                    Log.d(TAG, "ruan and item is " + testItem);
                    method = util.getClass().getDeclaredMethod(testItem);
                    testResult = (TestResult) method.invoke(util);
                } catch (Exception e) {
                    Log.d(TAG, "ruan exception is " + e.getLocalizedMessage());
                    e.printStackTrace();
                }
            } else if (module.getName().equals(Constant.MODULE_CMSS_TEST_CMMS)) {
                CmccTestMmsUtil util = new CmccTestMmsUtil(this);
                Method method = null;
                try {
                    Log.d(TAG, "ruan and item is " + testItem);
                    method = util.getClass().getDeclaredMethod(testItem);
                    testResult = (TestResult) method.invoke(util);
                } catch (Exception e) {
                    Log.d(TAG, "ruan exception is " + e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
        } else if (mSelectProject.getProject().equals("MercurySprint")) {
            if (module.getName().equals(Constant.MODULE_MERCURY_TEST_NODE)) {
                MercuryNoteTestUtil util = new MercuryNoteTestUtil(this);
                Method method = null;
                try {
                    Log.d(TAG, "ruan and item is " + testItem);
                    method = util.getClass().getDeclaredMethod(testItem);
                    testResult = (TestResult) method.invoke(util);
                } catch (Exception e) {
                    Log.d(TAG, "ruan exception is " + e.getLocalizedMessage());
                    e.printStackTrace();
                }
            } else if (module.getName().equals(Constant.MODULE_MERCURY_TEST_CALENDAR)) {
                MercuryCalendarTestUtil util = new MercuryCalendarTestUtil(this);
                Method method = null;
                try {
                    Log.d(TAG, "ruan and item is " + testItem);
                    method = util.getClass().getDeclaredMethod(testItem);
                    testResult = (TestResult) method.invoke(util);
                } catch (Exception e) {
                    Log.d(TAG, "ruan exception is " + e.getLocalizedMessage());
                    e.printStackTrace();
                }
            } else if (module.getName().equals("OMADMTest")) {
                MercuryOmaTestUtil util = new MercuryOmaTestUtil(this);
                util.startTest();
            }
        }

        if (null != testResult) {
            mCurrentModuleFunctionTestResult.get(position).setResultCode(testResult.getResultCode());
            mCurrentModuleFunctionTestResult.get(position).setResultMessage(testResult.getResultMessage());
            mTestResultMap.put(module.getName(), mCurrentModuleFunctionTestResult);
        }
        ToastUtil.showShortToast(this, testItem + "方法测试成功");
    }

    private void sendTestResultByEmail() {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<h1 style=\"font-family:verdana\">测试项目:" + mSelectProject.getProject() + "</h1><br>");

            Iterator iterator = mTestResultMap.keySet().iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                stringBuilder.append("<h3 style=\"font-family:verdana\">测试模块：" + key + "</h3>");
                ArrayList<TestResult> testResults = mTestResultMap.get(key);


                stringBuilder.append("<table border=\"0\">");
                stringBuilder.append("<tr><th>测试条目</th><th>测试结果</th></tr>");


                int successCount = 0;
                for (int i = 0; i < testResults.size(); i++) {
                    TestResult testResult = testResults.get(i);
                    if (testResult.getResultCode() == Constant.TEST_RESULT_SUCCESS) {
                        successCount++;
                        stringBuilder.append("<tr><td align=\"center\">" + testResult.getMethod() + "</td><td align=\"center\" style=\"color:green;\">" + testResult.getResultMessage() + "</td></tr>");
                    } else {
                        stringBuilder.append("<tr><td align=\"center\">" + testResult.getMethod() + "</td><td align=\"center\" style=\"color:red;\">" + testResult.getResultMessage() + "</td></tr>");
                    }
                }
                stringBuilder.append("</table>");
                stringBuilder.append("<h6>测试成功<font color=\"green\" size=4>"
                        + successCount + "</font>项，失败<font color=\"red\" size=4>"
                        + (testResults.size() - successCount) + "</font>项</h6>");
                stringBuilder.append("<br>");
            }

            MailServer.sendMail(this, "AutoTest Test Result", stringBuilder.toString(), null);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "send test result email failed");
        }
    }
}
