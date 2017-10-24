package com.nj.ts.autotest.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.ts.autotest.R;
import com.nj.ts.autotest.adapter.TestResultAdapter;
import com.nj.ts.autotest.entity.Module;
import com.nj.ts.autotest.entity.Project;
import com.nj.ts.autotest.entity.TestResult;

import java.util.ArrayList;
import java.util.Iterator;

public class RuanTestResultActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = RuanTestResultActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private TextView mProjectView;
    private Button mRetestButton;
    private Button mTestAloneButton;

    private Project mSelectProject;
    private ArrayList<Module> mModuleArrayList;

    public static final String BUNDLE_KEY_MODULE = "module";
    public static final String BUNDLE_KEY_PROJECT = "project";
    public static final String BUNDLE_KEY_RESULT = "result";
    private ArrayList<Object> mResultArrayList;
    private TestResultAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_result);
        initData();
        initView();
    }

    private void initData() {
        mResultArrayList = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();

        mSelectProject = JSON.parseObject(bundle.getString(BUNDLE_KEY_PROJECT), Project.class);
        mModuleArrayList = (ArrayList<Module>) JSON.parseArray(bundle.getString(BUNDLE_KEY_MODULE), Module.class);
        JSONObject jsonObject = JSON.parseObject(bundle.getString(BUNDLE_KEY_RESULT));
        Iterator iterator = jsonObject.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            mResultArrayList.add(key);
            JSONArray jsonArray = jsonObject.getJSONArray(key);
            for (int i = 0; i < jsonArray.size(); i++) {
                TestResult testResult = JSON.parseObject(jsonArray.getJSONObject(i).toJSONString(), TestResult.class);
                mResultArrayList.add(testResult);
            }
        }
        Log.d(TAG, "ruan mResultArrayList size is " + mResultArrayList.size());

    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mProjectView = (TextView) findViewById(R.id.project);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);

        mProjectView.setText(mSelectProject.getProject());
        mAdapter = new TestResultAdapter(mModuleArrayList, mResultArrayList, this);
        mRecyclerView.setAdapter(mAdapter);

        mRetestButton = (Button) findViewById(R.id.retest);
        mTestAloneButton = (Button) findViewById(R.id.test_alone);
        mRetestButton.setOnClickListener(this);
        mTestAloneButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.retest:
                setResult(TestingActivity.RESULT_CODE_RETEST);
                finish();
                break;
            case R.id.test_alone:
                setResult(TestingActivity.RESULT_CODE_TEST_ALONE);
                finish();
                break;
            default:
                break;
        }
    }
}
