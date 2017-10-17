package com.nj.ts.autotest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ts.autotest.R;
import com.nj.ts.autotest.adapter.ModuleEntityAdapter;
import com.nj.ts.autotest.util.SectionedSpanSizeLookup;
import com.nj.ts.autotest.util.Util;


public class TestResultActivity extends AppCompatActivity {

    private static final String TAG = "TestResultActivity";
    private RecyclerView mRecyclerView;
    private ModuleEntityAdapter mAdapter;
    private TextView mProjectView;
    private Button mRetest;
    private Button mTestAlone;
//    private ResultAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_result);
        initView();

    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mProjectView = (TextView) findViewById(R.id.project);
        mProjectView.setText("Project : " + Util.getConfig().getProjectName());
        mRetest = (Button) findViewById(R.id.retest);
        mRetest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TestResultActivity.this, MainActivity.class);
                startActivity(intent);
                Util.createResultXML(Util.getConfig());
            }
        });
        mTestAlone = (Button) findViewById(R.id.test_alone);
        mTestAlone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mAdapter = new ModuleEntityAdapter(this);
//        GridLayoutManager manager = new GridLayoutManager(this, 4);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        //设置header
//        manager.setSpanSizeLookup(new SectionedSpanSizeLookup(mAdapter, manager));
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setData(Util.getConfig().getModule());
    }

}
