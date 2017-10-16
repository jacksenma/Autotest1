package com.nj.ts.autotest.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ts.autotest.R;
import com.nj.ts.autotest.adapter.TestFunctionAdapter;
import com.nj.ts.autotest.entity.Function;
import com.nj.ts.autotest.entity.Module;
import com.nj.ts.autotest.util.Constant;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestFunctionActivity extends AppCompatActivity {

    private Module module;
    private TextView moduleName;
    private List<Function> list=new ArrayList<>();
    private TestFunctionAdapter adapter = new TestFunctionAdapter(list);
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;

    private String projectName;
    private static final String TAG = "TestFunctionActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_function);
        module  = (Module)getIntent().getSerializableExtra("functions");
        projectName = getIntent().getStringExtra("projectName");
        intView();
    }

    private void intView() {
        initModuleName();
        initFunctionList();
    }

    private void initFunctionList() {
        list.clear();
        final List<Function> functions = module.getFunctions();
        for(Function f : functions){
            Log.d(TAG, "initModulesList: "+f.getName());
            list.add(f);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.functionlist);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickListener(new TestFunctionAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View view , int position){
                Log.d(TAG, "onItemClick: "+list.get(position).getName());
                String path= Constant.TEST_PATH+projectName+".";
                String name = module.getName();
                Class c = null;
                try {
                    c = Class.forName(path+name);
                    Object o = c.newInstance();
                    Method m = c.getMethod(list.get(position).getName());
                    List<Object> x =(List<Object>) m.invoke(o);

                    //判断方法返回的boolean值
                    if((x.get(0) ==(Object) false)){
                        list.get(position).setState(0);
                        Toast.makeText(TestFunctionActivity.this, "方法"+m.getName()+"测试完成,仍不通过", Toast.LENGTH_SHORT).show();
                    }else{
                        list.get(position).setState(1);
                        Toast.makeText(TestFunctionActivity.this, "方法"+m.getName()+"测试完成,已经通过", Toast.LENGTH_SHORT).show();
                    }
                    adapter.notifyDataSetChanged();

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private void initModuleName() {
        moduleName = (TextView)findViewById(R.id.module_name);
        moduleName.setText(module.getName());
    }
}
