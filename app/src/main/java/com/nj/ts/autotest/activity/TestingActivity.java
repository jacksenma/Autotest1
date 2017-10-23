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
import com.nj.ts.autotest.entity.RuanModule;
import com.nj.ts.autotest.entity.RuanProject;
import com.nj.ts.autotest.entity.RuanTestResult;
import com.nj.ts.autotest.testutil.CMCC.CmccTestChromeUtil;
import com.nj.ts.autotest.testutil.CMCC.CmccTestMmsUtil;
import com.nj.ts.autotest.testutil.CMCC.CmccTestNodeUtil;
import com.nj.ts.autotest.testutil.CMCC.CmccTestCalendarUtil;
import com.nj.ts.autotest.testutil.MercurySprint.MercuryCalendarTest;
import com.nj.ts.autotest.testutil.MercurySprint.MercuryNoteTestUtil;
import com.nj.ts.autotest.util.Constant;
import com.nj.ts.autotest.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;

public class TestingActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = TestingActivity.class.getSimpleName();
    public static final String BUNDLE_KEY_PROJECT = "project";
    public static final String BUNDLE_KEY_MODULE = "module";

    public static final int REQUEST_CODE = 0X01;
    public static final int RESULT_CODE_RETEST = 0X02;
    public static final int RESULT_CODE_TEST_ALONE = 0X03;

    //OMA
    private static final String ACTION_START_OMA_TEST = "ts.intent.action.ActionOmaTester";
    private static final String BROADCAST_OMA_TEST_FINISHED = "baroadcast_oma_test_finished";

    private Button mShowResultButton;
    private ProgressBar mProgressBar;
    private RuanProject mSelectProject;
    private ArrayList<RuanModule> mModuleArrayList;
    private ArrayList<RuanTestResult> mCurrentModuleFunctionTestResult;

    private HashMap<String, ArrayList<RuanTestResult>> mTestResultMap;
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
        mCurrentModuleFunctionTestResult = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        ArrayList<String> arrayList = bundle.getStringArrayList(BUNDLE_KEY_MODULE);
        for (int i = 0; i < arrayList.size(); i++) {
            RuanModule module = new RuanModule();
            module.setName(arrayList.get(i));
            if (i == 0) {
                module.setSelect(true);
            } else {
                module.setSelect(false);
            }
            mModuleArrayList.add(module);

            ArrayList<RuanTestResult> functionTestResults = new ArrayList<>();
            mTestResultMap.put(arrayList.get(i), functionTestResults);
        }
        mSelectProject = JSON.parseObject(bundle.getString(BUNDLE_KEY_PROJECT), RuanProject.class);
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
                    RuanModule module = mModuleArrayList.get(i);
                    if (module.isSelect()) {
                        ArrayList<RuanTestResult> testResults = mTestResultMap.get(module.getName());
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
        mFunctionRecycleView.setAdapter(mTestFunctionAdapter);
    }

    /**
     * 发送测试广播
     */
    private void sendTestNotification() {
        if (mSelectProject.getProject().equals("Cmcc")) {
            for (int i = 0; i < mModuleArrayList.size(); i++) {
                RuanModule module = mModuleArrayList.get(i);
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
                RuanModule module = mModuleArrayList.get(i);
                if (module.getName().equals(Constant.MODULE_MERCURY_TEST_NODE)) {
                    MercuryNoteTestUtil util = new MercuryNoteTestUtil(this);
                    util.startTest();
                } else if (module.getName().equals(Constant.MODULE_MERCURY_TEST_CALENDAR)) {
                    MercuryCalendarTest util = new MercuryCalendarTest(this);
                    util.startTest();
                } else if (module.getName().equals("OMADMTest")) {


                    Log.d(TAG, "ruan send oma test notification");
                    Intent intent = new Intent();
                    intent.putExtras(getIntent().getExtras());
                    intent.addFlags(0x01000000);
                    intent.setAction(ACTION_START_OMA_TEST);
                    sendBroadcast(intent);
                }
            }
        }
    }

    private void refreshUi() {
        //刷新当前选中module的结果
        for (int i = 0; i < mModuleArrayList.size(); i++) {
            RuanModule module = mModuleArrayList.get(i);
            if (module.isSelect()) {
                ArrayList<RuanTestResult> testResults = mTestResultMap.get(module.getName());
                mCurrentModuleFunctionTestResult.clear();
                mCurrentModuleFunctionTestResult.addAll(testResults);
                mTestFunctionAdapter.notifyDataSetChanged();
                break;
            }
        }

        //刷新左侧module的字体颜色
        for (int i = 0; i < mModuleArrayList.size(); i++) {
            RuanModule module = mModuleArrayList.get(i);
            ArrayList<RuanTestResult> testResults = mTestResultMap.get(module.getName());
            if (!testResults.isEmpty()) {
                boolean isAllSuccess = true;
                for (int j = 0; j < testResults.size(); j++) {
                    RuanTestResult result = testResults.get(j);
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
        intentFilter.addAction(MercuryCalendarTest.BROADCAST_MERCURY_TEST_CALENDAR_FINISHED);
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
                    ArrayList<RuanTestResult> testResults = (ArrayList<RuanTestResult>) JSONArray.parseArray(data, RuanTestResult.class);
                    mTestResultMap.put(Constant.MODULE_CMSS_TEST_NODE, testResults);
                    Message msg = new Message();
                    msg.what = MSG_REFRESH_UI;
                    msg.obj = Constant.MODULE_CMSS_TEST_NODE;
                    mHandler.sendMessage(msg);
                } else if (action.equals(CmccTestCalendarUtil.BROADCAST_CMCC_TEST_CALENDAR_FINISHED)) {
                    ArrayList<RuanTestResult> testResults = (ArrayList<RuanTestResult>) JSONArray.parseArray(data, RuanTestResult.class);
                    mTestResultMap.put(Constant.MODULE_CMSS_TEST_CALENDAR, testResults);
                    Message msg = new Message();
                    msg.what = MSG_REFRESH_UI;
                    msg.obj = Constant.MODULE_CMSS_TEST_CALENDAR;
                    mHandler.sendMessage(msg);
                } else if (action.equals(CmccTestChromeUtil.BROADCAST_CMCC_TEST_CHROME_FINISHED)) {
                    ArrayList<RuanTestResult> testResults = (ArrayList<RuanTestResult>) JSONArray.parseArray(data, RuanTestResult.class);
                    mTestResultMap.put(Constant.MODULE_CMSS_TEST_CHROME, testResults);
                    Message msg = new Message();
                    msg.what = MSG_REFRESH_UI;
                    msg.obj = Constant.MODULE_CMSS_TEST_CHROME;
                    mHandler.sendMessage(msg);
                } else if (action.equals(CmccTestMmsUtil.BROADCAST_CMCC_TEST_MMS_FINISHED)) {
                    ArrayList<RuanTestResult> testResults = (ArrayList<RuanTestResult>) JSONArray.parseArray(data, RuanTestResult.class);
                    mTestResultMap.put(Constant.MODULE_CMSS_TEST_CMMS, testResults);
                    Message msg = new Message();
                    msg.what = MSG_REFRESH_UI;
                    msg.obj = Constant.MODULE_CMSS_TEST_CMMS;
                    mHandler.sendMessage(msg);
                } else if (action.equals(MercuryNoteTestUtil.BROADCAST_MERCURY_TEST_NODE_FINISHED)) {
                    ArrayList<RuanTestResult> testResults = (ArrayList<RuanTestResult>) JSONArray.parseArray(data, RuanTestResult.class);
                    mTestResultMap.put(Constant.MODULE_MERCURY_TEST_NODE, testResults);
                    Message msg = new Message();
                    msg.what = MSG_REFRESH_UI;
                    msg.obj = Constant.MODULE_MERCURY_TEST_NODE;
                    mHandler.sendMessage(msg);
                } else if (action.equals(MercuryCalendarTest.BROADCAST_MERCURY_TEST_CALENDAR_FINISHED)) {
                    ArrayList<RuanTestResult> testResults = (ArrayList<RuanTestResult>) JSONArray.parseArray(data, RuanTestResult.class);
                    mTestResultMap.put(Constant.MODULE_MERCURY_TEST_CALENDAR, testResults);
                    Message msg = new Message();
                    msg.what = MSG_REFRESH_UI;
                    msg.obj = Constant.MODULE_MERCURY_TEST_CALENDAR;
                    mHandler.sendMessage(msg);
                } else if (intent.getAction().equals(BROADCAST_OMA_TEST_FINISHED)) {
                    ArrayList<RuanTestResult> testResults = (ArrayList<RuanTestResult>) JSONArray.parseArray(data, RuanTestResult.class);
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
}
