package com.nj.ts.autotest.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ts.autotest.R;
import com.nj.ts.autotest.adapter.CheckboxAdapter;
import com.nj.ts.autotest.adapter.SpinnerAdapter;
import com.nj.ts.autotest.entity.Config;
import com.nj.ts.autotest.entity.Module;
import com.nj.ts.autotest.util.Constant;
import com.nj.ts.autotest.util.DomParser;
import com.nj.ts.autotest.util.Util;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity{
    private static final String TAG = "MainActivity";
    private TextView mTextView;
    private Button startTest;
    private Config config;
    private SpinnerAdapter arrayAdapter;
    private Spinner spinner;
    private List<Config> configs;
    private Button choose;
    private ArrayList<Module> yourChoices = new ArrayList<>();

    private View getlistview;
    private String[] mlistText;
    private ArrayList<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
    private AlertDialog.Builder builder;
    private SimpleAdapter adapter;
    private boolean[] bl ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configs = initConfig();
        initProjectName(configs);
    }

    private void initView(Config config) {
        initChooseButton();
        initButton(config);
        initTextView();
    }

    private void initTextView() {
        mTextView = (TextView)findViewById(R.id.main_chosen_modules);
        String result = "";
        for(int i = 0;i < yourChoices.size();i++){
            if(i == 0)
                result += "已选模块如下：\n";
            result+=yourChoices.get(i).getName();
            if(i !=yourChoices.size()-1)
                result += " , ";
        }
        if(yourChoices.size() == 0)
            result = "还未选择任何模块";
        mTextView.setText(result);
    }

    private void initChooseButton() {
        choose = (Button) findViewById(R.id.main_choose_modules);
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData.clear();
                int lengh = mlistText.length;
                for (int i = 0; i < lengh; i++) {
                    Map<String, Object> item = new HashMap<String, Object>();
                    item.put("text", mlistText[i]);
                    mData.add(item);
                }
                CreateDialog();// 点击创建Dialog
            }
        });

    }

    class ItemOnClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
            CheckBox cBox = (CheckBox) view.findViewById(R.id.X_checkbox);
            if (cBox.isChecked()) {
                cBox.setChecked(false);
            } else {
                Log.i("TAG", "取消该选项");
                cBox.setChecked(true);
            }

            if (position == 0 && (cBox.isChecked())) {
                //如果是选中 全选  就把所有的都选上 然后更新
                for (int i = 0; i < bl.length; i++) {
                    bl[i] = true;
                }
                adapter.notifyDataSetChanged();
            } else if (position == 0 && (!cBox.isChecked())) {
                //如果是取消全选 就把所有的都取消 然后更新
                for (int i = 0; i < bl.length; i++) {
                    bl[i] = false;
                }
                adapter.notifyDataSetChanged();
            }
            if (position != 0 && (!cBox.isChecked())) {
                // 如果把其它的选项取消   把全选取消
                bl[0] = false;
                bl[position]=false;
                adapter.notifyDataSetChanged();
            } else if (position != 0 && (cBox.isChecked())) {
                //如果选择其它的选项，看是否全部选择
                //先把该选项选中 设置为true
                bl[position]=true;
                int a = 0;
                for (int i = 1; i < bl.length; i++) {
                    if (bl[i] == false) {
                        //如果有一个没选中  就不是全选 直接跳出循环
                        break;
                    } else {
                        //计算有多少个选中的
                        a++;
                        if (a == bl.length - 1) {
                            //如果选项都选中，就把全选 选中，然后更新
                            bl[0] = true;
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        }

    }

    public void CreateDialog() {
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        getlistview = inflater.inflate(R.layout.listview, null);
        ListView listview = (ListView) getlistview.findViewById(R.id.X_listview);
        adapter = new SetSimpleAdapter(MainActivity.this, mData, R.layout.listitem, new String[] { "text" }, new int[] { R.id.X_item_text });
        listview.setAdapter(adapter);
        listview.setItemsCanFocus(false);
        listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listview.setOnItemClickListener(new ItemOnClick());
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Please choose modules");
        builder.setView(getlistview);
        builder.setPositiveButton("ok",new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                yourChoices.clear();
                for(int i =1;i < bl.length;i++){
                    if(bl[i]){
                        yourChoices.add(config.getModule().get(i-1));
                    }
                }
                initTextView();
            }
        });
        //builder.setNegativeButton("cancel", new DialogOnClick());
        builder.create().show();
    }

    class SetSimpleAdapter extends SimpleAdapter {

        public SetSimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from,
                                int[] to) {
            super(context, data, resource, from, to);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LinearLayout.inflate(getBaseContext(), R.layout.listitem, null);
            }
            CheckBox ckBox = (CheckBox) convertView.findViewById(R.id.X_checkbox);
            if (bl[position] == true) {
                ckBox.setChecked(true);
            } else if (bl[position] == false) {
                ckBox.setChecked(false);
            }
            return super.getView(position, convertView, parent);
        }
    }


    //确定选择哪一个Project
    private void initProjectName(final List<Config> configs) {
        spinner = (Spinner)findViewById(R.id.project_spinner);
        List<String> pro_string = new ArrayList<>();
        for(Config c : configs)
            pro_string.add(c.getProjectName());
        arrayAdapter = new SpinnerAdapter(this,pro_string);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                config = configs.get(i);
                Util.setConfig(config);
                initView(config);

                //初始化Dialog数据
                List<Module> modules = config.getModule();
                int len = modules.size();
                mlistText = new String[len+1];
                bl = new boolean[len+1];
                mlistText[0] = "全选";
                bl[0] = false;

                for(int j = 1;j < len + 1;j++){
                    mlistText[j] = modules.get(j-1).getName();
                    bl[j] = false;
                }
                //换项目时清除已经选择的模块信息（mTextView和yourChoices）
                mTextView.setText("还未选择任何模块");
                yourChoices.clear();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
    //初始化加载配置信息
    private List<Config> initConfig() {
        List<Config> configs=getConfigFromServer();
        getConfigFromServer();
        if (configs.size() == 0) {
            //解析本地config
            Log.d(TAG, "initConfig: 读取本地配置文件");
            try {
                InputStream is = getAssets().open("config.xml");
                configs = DomParser.getConfig(is);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }

        }
        return configs;
    }

    //从服务器端加载配置信息
    public List<Config> getConfigFromServer() {
         List<Config> configs =new ArrayList<>();
         String path = "http://10.0.2.2/TSAutoTestConfig.xml";
         return configs;

    }

    //检查配置信息的正确性,是否能找到这个项目名称和模块
    private boolean testConfig(Config config) {
        if(config == null){
            Toast.makeText(this, "读取配置文件失败", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "testConfig: 读取配置文件失败");
        }else{
            ArrayList<Module> modules = (ArrayList<Module>) config.getModule();
            try {
                for(int i=0;i<modules.size();i++){
                    String path=Constant.TEST_PATH+config.getProjectName()+".";
                    String name = modules.get(i).getName();
                    Log.d(TAG, "testConfig: "+path+"1");
                    Class c = Class.forName(path+name);
                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw, true));
                String strs = sw.toString();
                String [] s = strs.split("\n");
                String[] info = s[0].split("\\.");
                Toast.makeText(this,"未找到: "+info[info.length-1], Toast.LENGTH_SHORT).show();
                Log.d(TAG, "testConfig: 未找到:"+info[info.length-1]);
                return false;
            }

        }
        return true;
    }

    //开始执行测试代码
    private void initButton(final Config c) {
        startTest = (Button)findViewById(R.id.start_test);
        startTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bl == null){
                    Toast.makeText(MainActivity.this, "请至少选择一个测试模块", Toast.LENGTH_SHORT).show();
                    return;
                }

                List<Module> modules = yourChoices;

                if(modules.size() == 0){
                    Toast.makeText(MainActivity.this, "请至少选择一个测试模块", Toast.LENGTH_SHORT).show();
                    return;
                }
                Config config = new Config();
                config.setModule(modules);
                config.setProjectName(c.getProjectName());
                Util.setConfig(config);
                if(testConfig(config)){
                    Log.d(TAG, "onCreate: 开始测试");
                    Intent intent = new Intent(MainActivity.this,TestModuleActivity.class);
                    intent.putExtra("config",config);
                    startActivity(intent);

                }else{
                    Toast.makeText(MainActivity.this, "测试失败...", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onCreate: 测试失败");
                }


            }
        });
    }





}
