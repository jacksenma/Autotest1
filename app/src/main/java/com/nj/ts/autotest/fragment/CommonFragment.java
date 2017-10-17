package com.nj.ts.autotest.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ts.autotest.R;
import com.nj.ts.autotest.activity.TestModuleActivity;
import com.nj.ts.autotest.activity.TestResultActivity;
import com.nj.ts.autotest.adapter.GridViewAdapter;
import com.nj.ts.autotest.entity.Config;
import com.nj.ts.autotest.entity.Function;
import com.nj.ts.autotest.entity.Module;
import com.nj.ts.autotest.util.Constant;
import com.nj.ts.autotest.util.Info;
import com.nj.ts.autotest.util.Util;
import com.nj.ts.autotest.view.TabView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CommonFragment extends Fragment {
    private String TAG = "CommonFragment";
    private GridView mGridView;
    private TabView mTabView;
    private GridViewAdapter mGridViewAdapter;
    private List<Method> mTestMethods = new ArrayList<>();
    private List<Object> mMethodInstances = new ArrayList<>();
    private List<Function> mModuleFunctions;
    private String mProjectName;
    private String mModuleName;

    public static CommonFragment newInstance(String projectName, String module) {
        CommonFragment fragmentCommon = new CommonFragment();
        Bundle bundle = new Bundle();
        bundle.putString("module", module);
        bundle.putString("projectName", projectName);
        fragmentCommon.setArguments(bundle);
        return fragmentCommon;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_common, container, false);
        mProjectName = getArguments().getString("projectName");
        mModuleName = getArguments().getString("module");
        initData();
        mGridView = view.findViewById(R.id.test_content);
        Log.d("lxx", "create------");
        mGridViewAdapter = new GridViewAdapter(getActivity(), R.layout.grid_item_layout, mModuleFunctions);
        mGridView.setAdapter(mGridViewAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        });
        mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                mTabView.setIsTesting(true);
                testMethod(i);
                mTabView.setIsTesting(false);
                return false;
            }
        });
        return view;
    }

    private void testMethod(int position) {
        try {
            Info.currentMethodIndex = position;
            Method method = mTestMethods.get(position);
            method.setAccessible(true);
            TextView resultView = (TextView) ((ViewGroup) mGridView.getChildAt(position)).getChildAt(1);
            resultView.setText("testing");
            Util.showToast(getActivity(), "开始测试" + method.getName() + "方法");
            method.invoke(mMethodInstances.get(position));
            Log.d("lxx", "testmethod------" + position + "methodname" + method.getName());
            resultView.setText(Info.resultMessage);
            if (Info.currentResult == 0) {
                mGridView.getChildAt(position).setBackgroundResource(R.drawable.test_item_fail_bg);
                Util.showToast(getActivity(), method.getName() + "方法" + "测试失败");
            } else {
                mGridView.getChildAt(position).setBackgroundResource(R.drawable.test_item_success_bg);
                Util.showToast(getActivity(), method.getName() + "方法" + "测试成功");
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mTabView = ((TestModuleActivity) getActivity()).getmTabView();
        Log.d("lxx", "tabview-----" + mTabView);
        TimerTask task = new TimerTask() {
            public void run() {
                //execute the task
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        testModule();
                        Util.showToast(getActivity(), mModuleName + "测试完成");
                    }
                });
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 500);
    }

    private void testModule() {
        Config config = Util.getConfig();
        List<Module> modules = (ArrayList<Module>) config.getModule();
        try {
            for (int i = 0; i < modules.size(); i++) {

                //设置当前模块正在测试
                Module module = modules.get(i);
                if (module.getName().equals(mModuleName)) {
                    setTestState(3, i);
                    String path = Constant.TEST_PATH + config.getProjectName() + ".";
                    String name = modules.get(i).getName();
                    Class c = Class.forName(path + name);
                    Object o = c.newInstance();
                    Log.d(TAG, "startTest: 正在测试" + name + "类");
                    Method[] methods = c.getDeclaredMethods();
                    Method[] methods_public = c.getMethods();
                    boolean isSuccess = true;//记录这个模块是否测试通过
                    List<Function> fs = new ArrayList<>();//存放每个模块的各个方法

                    int j = 0;
                    for (Method m : methods) {
                        if (isPublic(m, methods_public)) {
                            Info.currentMethodIndex = j;
                            m.setAccessible(true);
                            Log.d("lxx", "j=" + j);
                            TextView resultView = (TextView) ((ViewGroup) mGridView.getChildAt(j)).getChildAt(1);
                            resultView.setText("testing");
                            Info.updateConfigOfMethod(3, "testing");
                            m.invoke(o);//依次执行每个方法，只有有了sendResult的才会记录
                            Log.d("lxx", "end------" + j);
                            resultView.setText(Info.resultMessage);
                            if (Info.currentResult == 0) {
                                mGridView.getChildAt(j).setBackgroundResource(R.drawable.test_item_fail_bg);
                            } else {
                                mGridView.getChildAt(j).setBackgroundResource(R.drawable.test_item_success_bg);
                            }
                            j++;
                        }//end isPublic if

                    }//end method for
                    fs = Info.getFuncs();

                    Log.d(TAG, "startTest: ------------------");
                    for (int l = 0; l < fs.size(); l++) {
                        Log.d(TAG, "startTest: " + fs.get(l).getName());
                        if (fs.get(l).getState() == 0)
                            isSuccess = false;
                    }

                    //设置测试结果
                    if (isSuccess) {
                        setTestState(1, i);
                    } else {
                        setTestState(0, i);
                    }
                    if (i < modules.size() - 1) {
                        setTestState(3, i + 1);
                    } else {
                        mTabView.setIsTesting(false);
                        Intent intent = new Intent(getActivity(), TestResultActivity.class);
                        getActivity().startActivity(intent);
                    }

                    Info.clearFuncs();
                }
            }//end module for


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void setTestState(int state, int position) {
        TextView moduleText = mTabView.getModuleList().get(position);
        switch (state) {
            case 0:
                moduleText.setTextColor(Color.RED);
                break;
            case 1:
                moduleText.setTextColor(Color.GREEN);
                break;
            case 2:
                moduleText.setTextColor(Color.DKGRAY);
                break;
            case 3:
                Log.d("lxx", "choose " + position);
                mTabView.chooseModule(position);
                moduleText.setTextColor(Color.BLUE);
                break;
            default:
                break;
        }
        Info.updateConfigOfModule(state);

        Log.d(TAG, "setTestState: 颜色改变完成");

    }

    private void initData() {
        String path = Constant.TEST_PATH + mProjectName + ".";
        mModuleFunctions = new ArrayList<>();//存放每个模块的各个方法
        try {
            Class c = Class.forName(path + mModuleName);
            Method[] methods = c.getDeclaredMethods();
            Method[] methods_public = c.getMethods();
            for (Method m : methods) {
                if (isPublic(m, methods_public)) {
                    mTestMethods.add(m);
                    mMethodInstances.add(c.newInstance());
                    Function function = new Function();
                    function.setName(m.getName());
                    function.setState(2);
                    mModuleFunctions.add(function);
                }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        updateConfig(mModuleName, mModuleFunctions);
    }

    private void updateConfig(String moduleName, List<Function> fs) {
        Config config = Util.getConfig();
        List<Module> backModules = config.getModule();
        List<Module> modules = new ArrayList<>();
        for (int i = 0; i < backModules.size(); i++) {
            Module module = backModules.get(i);
            if (moduleName.equals(module.getName())) {
                module.setFunctions(fs);
            }
            modules.add(module);
        }
        config.setModule(modules);
        Util.setConfig(config);
    }

    private boolean isPublic(Method m, Method[] methods_public) {
        if (m.getName().equals("access$super"))
            return false;
        for (int i = 0; i < methods_public.length; i++) {
            if (methods_public[i].equals(m))
                return true;
        }
        return false;
    }
}
