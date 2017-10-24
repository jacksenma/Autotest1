package com.nj.ts.autotest.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.ts.autotest.R;
import com.nj.ts.autotest.adapter.ModuleAdapter;
import com.nj.ts.autotest.adapter.SpinnerAdapter;
import com.nj.ts.autotest.email.Attachment;
import com.nj.ts.autotest.email.MailServer;
import com.nj.ts.autotest.entity.Module;
import com.nj.ts.autotest.entity.Project;
import com.nj.ts.autotest.smb.SmbConfig;
import com.nj.ts.autotest.smb.SmbServer;
import com.nj.ts.autotest.util.ToastUtil;
import com.nj.ts.autotest.util.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private Spinner mSelectProjectSpinner;
    private TextView mSelectedProjectTextView;
    private Button mSelectModuleButton;
    private Button mStartTestButton;

    private SpinnerAdapter mProjectAdapter;
    private ModuleAdapter mModuleAdapter;


    private AlertDialog.Builder builder;

    private List<Project> mProjects;
    private Project mSelectProject;


    private static final int MSG_DOWNLOAD_SUCCESS = 0x01;
    private static final int MSG_DOWNLOAD_FAILED = 0x02;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_DOWNLOAD_SUCCESS:
                    readTestNode();
                    break;
                case MSG_DOWNLOAD_FAILED:
                    ToastUtil.showShortToast(MainActivity.this, "配置文件下载失败");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
        registerBroadcast();
//        downloadConfigFile();

        readTestNode();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    private void initData() {
        mProjects = new ArrayList<Project>();
    }

    private void initView() {
        mSelectedProjectTextView = (TextView) findViewById(R.id.main_chosen_modules);
        mSelectModuleButton = (Button) findViewById(R.id.main_choose_modules);
        mSelectProjectSpinner = (Spinner) findViewById(R.id.project_spinner);
        mStartTestButton = (Button) findViewById(R.id.start_test);

        mSelectModuleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showModuleListDialog();
            }
        });

        mStartTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickStartTestButton();
            }
        });


        mProjectAdapter = new SpinnerAdapter(this, mProjects);
        mSelectProjectSpinner.setAdapter(mProjectAdapter);
        mSelectProjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectProject(mProjects.get(i));
                mSelectProject = mProjects.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    /**
     * 下载配置文件
     */
    private void downloadConfigFile() {
        String path = SmbConfig.DEFAULT_DOWNLOAD_PATH + "node.json";
//        File file = new File(path);
//        if (file.exists()) {
//            file.delete();
//        }

        SmbConfig sb = SmbConfig.getInstance();
        sb.setIp("192.168.191.1").setRootPath("/share/AutoTest");
        sb.setConfigName("node.json");
        SmbServer.downloadConfigFile(this, "node.json");
    }

    /**
     * 读取节点配置数据
     */
    private void readTestNode() {
        try {
//            String path = SmbConfig.DEFAULT_DOWNLOAD_PATH + "node.json";
//            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(path), "UTF-8");

            InputStreamReader inputStreamReader = new InputStreamReader(getAssets().open("node.json"), "UTF-8");

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            bufferedReader.close();
            inputStreamReader.close();

            List<Project> projects = JSON.parseArray(stringBuilder.toString(), Project.class);
            mProjects.addAll(projects);
            mSelectProject = mProjects.get(0);
            mProjectAdapter.notifyDataSetChanged();
            refreshSelectModule();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 选择项目
     *
     * @param project
     */
    private void selectProject(Project project) {
        for (int i = 0; i < mProjects.size(); i++) {
            mProjects.get(i).setSelect(false);
            for (int j = 0; j < mProjects.get(i).getModule().size(); j++) {
                mProjects.get(i).getModule().get(j).setSelect(false);
            }
        }
        project.setSelect(true);
        refreshSelectModule();
    }

    /**
     * 点击开始按钮
     */
    private void clickStartTestButton() {
        boolean flag = false;
        for (int i = 0; i < mSelectProject.getModule().size(); i++) {
            if (mSelectProject.getModule().get(i).isSelect()) {
                flag = true;
                break;
            }
        }


        if (flag) {
            ArrayList<String> selectedModules = new ArrayList<>();
            for (int i = 0; i < mSelectProject.getModule().size(); i++) {
                if (i != 0 && mSelectProject.getModule().get(i).isSelect()) {
                    selectedModules.add(mSelectProject.getModule().get(i).getName());
                }
            }

            Intent intent = new Intent(MainActivity.this, TestingActivity.class);
            Bundle bundle = new Bundle();
            bundle.putStringArrayList(TestingActivity.BUNDLE_KEY_MODULE, selectedModules);
            bundle.putString(TestingActivity.BUNDLE_KEY_PROJECT, JSON.toJSONString(mSelectProject));
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            Toast.makeText(MainActivity.this, "请至少选择一个测试模块", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    /**
     * 现实模块列表对话框
     */
    public void showModuleListDialog() {
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        View getlistview = inflater.inflate(R.layout.listview, null);
        ListView listview = (ListView) getlistview.findViewById(R.id.X_listview);

        mModuleAdapter = new ModuleAdapter(this, mSelectProject.getModule());
        listview.setAdapter(mModuleAdapter);
        listview.setItemsCanFocus(false);
        listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    if (mSelectProject.getModule().get(0).isSelect()) {
                        for (int i = 0; i < mSelectProject.getModule().size(); i++) {
                            mSelectProject.getModule().get(i).setSelect(false);
                        }
                    } else {
                        for (int i = 0; i < mSelectProject.getModule().size(); i++) {
                            mSelectProject.getModule().get(i).setSelect(true);
                        }
                    }
                } else {
                    Module module = mSelectProject.getModule().get(position);
                    module.setSelect(!module.isSelect());

                    boolean selectAllFlag = true;
                    for (int i = 0; i < mSelectProject.getModule().size(); i++) {
                        if (i != 0) {
                            if (!mSelectProject.getModule().get(i).isSelect()) {
                                selectAllFlag = false;
                                break;
                            }
                        }
                    }
                    if (selectAllFlag) {
                        mSelectProject.getModule().get(0).setSelect(true);
                    } else {
                        mSelectProject.getModule().get(0).setSelect(false);
                    }
                }
                mModuleAdapter.notifyDataSetChanged();
            }
        });
        builder = new AlertDialog.Builder(this);

        TextView title = new TextView(this);
        title.setText("请选择想测试的模块");
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextSize(20);
//        title.setBackgroundColor(this.getResources().getColor(R.color.blue_200));
        builder.setCustomTitle(title);
        builder.setView(getlistview);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                refreshSelectModule();
            }
        });
        //builder.setNegativeButton("cancel", new DialogOnClick());

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    /**
     * 选择模块
     */
    private void refreshSelectModule() {
        String result = "已选项目：" + mSelectProject.getProject() + "\n\n";

        ArrayList<String> selectModules = new ArrayList<>();
        for (int j = 0; j < mSelectProject.getModule().size(); j++) {
            if (j != 0 && mSelectProject.getModule().get(j).isSelect()) {
                selectModules.add(mSelectProject.getModule().get(j).getName());
            }
        }
        if (selectModules.isEmpty()) {
            result += "还未选择任何模块";
        } else {
            result += "已选模块如下：\n";
            result += Util.arrayListToString(selectModules, " , ");
        }
        mSelectedProjectTextView.setText(result);
    }


    /**
     * 测试服务器
     */
    private void testSmbServer() {
        SmbConfig sb = SmbConfig.getInstance();
        sb.setIp("192.168.191.1").setRootPath("/share/AutoTest");
        sb.setConfigName("node.json");
        SmbServer.downloadConfigFile(this, "node.json");


//        SmbServer.uploadFile(this,
//                SmbConfig.getInstance().getDownloadPath()+"config1.xml",
//                SmbConfig.getInstance().getRootUrl()+"lala/lala/" + System.currentTimeMillis()+"hello1.xml");
    }

    /**
     * 测试邮件
     */
    private void testEmail() {
        Log.d(TAG, "testEmail: ");
        String path = SmbConfig.DEFAULT_DOWNLOAD_PATH + "node.json";
        ArrayList<Attachment> attaches = new ArrayList<>();
        Attachment a1 = new Attachment("a1.xml", path);
        attaches.add(a1);
        MailServer.sendMail(this, "AutoTest First Email", "Hello Auto test", attaches);
    }

    private void registerBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SmbServer.ACTION_DOWNLOAD_CONFIG_SUCCESS);
        intentFilter.addAction(SmbServer.ACTION_DOWNLOAD_CONFIG_FAILED);
        registerReceiver(mReceiver, intentFilter);
    }

    private DownloadBroadcastReceiver mReceiver = new DownloadBroadcastReceiver();

    private class DownloadBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (!TextUtils.isEmpty(action)) {
                if (action.equals(SmbServer.ACTION_DOWNLOAD_CONFIG_SUCCESS)) {
                    mHandler.sendEmptyMessage(MSG_DOWNLOAD_SUCCESS);
                } else if (action.equals(SmbServer.ACTION_DOWNLOAD_CONFIG_FAILED)) {
                    mHandler.sendEmptyMessage(MSG_DOWNLOAD_FAILED);
                }
            }
        }
    }
}
