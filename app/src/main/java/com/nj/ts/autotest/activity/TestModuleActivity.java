package com.nj.ts.autotest.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ts.autotest.R;
import com.nj.ts.autotest.entity.Config;
import com.nj.ts.autotest.entity.Module;
import com.nj.ts.autotest.entity.TabViewChild;
import com.nj.ts.autotest.fragment.CommonFragment;
import com.nj.ts.autotest.util.Info;
import com.nj.ts.autotest.view.TabView;

import java.util.ArrayList;
import java.util.List;


public class TestModuleActivity extends AppCompatActivity {

    private static final String TAG = "TestModuleActivity";

    private Config config;
    private TabView mTabView;
    private Button mShowResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_module);
        config = (Config) getIntent().getSerializableExtra("config");
        initView();
    }

    private void initView() {
        mTabView = (TabView) findViewById(R.id.tabView);
        mTabView.setIsTesting(true);
        mShowResult = (Button) findViewById(R.id.show_result);
        mShowResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mTabView.isTesting()) {
                    Intent intent = new Intent(TestModuleActivity.this, TestResultActivity.class);
                    startActivity(intent);
                }
            }
        });
        initModulesList();
    }

    private void initModulesList() {
        final List<Module> modules = config.getModule();
        List<TabViewChild> tabViewChildList = new ArrayList<>();
        String projectName = config.getProjectName();
        for (Module m : modules) {
            Log.d(TAG, "initModulesList: " + m.getName());
            String moduleName = m.getName();
            tabViewChildList.add(new TabViewChild(0, 0, moduleName,
                    CommonFragment.newInstance(projectName, moduleName)));
        }

        mTabView.setTabViewChild(tabViewChildList, getSupportFragmentManager());
        mTabView.setOnTabChildClickListener(new TabView.OnTabChildClickListener() {
            @Override
            public void onTabChildClick(int position, ImageView currentImageIcon, TextView currentTextView) {
                // Toast.makeText(getApplicationContext(),"position:"+position,Toast.LENGTH_SHORT).show();
                Info.currentModuleIndex = position;
                Log.d("lxx", "on tab click"+position);
            }
        });

    }

    public TabView getmTabView() {
        return mTabView;
    }

}
