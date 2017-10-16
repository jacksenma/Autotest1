package com.nj.ts.autotest.util;

import android.content.Context;
import android.util.*;
import android.util.Log;
import android.widget.Toast;

import com.nj.ts.autotest.entity.Config;
import com.nj.ts.autotest.entity.Function;
import com.nj.ts.autotest.entity.Module;

import org.xmlpull.v1.XmlSerializer;

import java.io.StringWriter;
import java.util.List;

/**
 * Created by ts on 17-10-10.
 */

public class Util {
    private static Config config;
    private static final String TAG = "Util";
    public static void setConfig(Config c) {
        config = c;
    }

    public static Config getConfig() {
        return config;
    }

    public static String getProjectName(){
        return config.getProjectName();
    }

    public static List<Module> getModules(){
        return config.getModule();
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static <D> boolean isEmpty(List<D> list) {
        return list == null || list.isEmpty();
    }


    //将测试结果整理成XML形式
    public static void createResultXML(Config config){
        List<Module> m = config.getModule();

        for(int a=0;a<m.size();a++){
            Log.d(TAG, "createResultXML: "+m.get(a).getName());
        }
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        String projectName = config.getProjectName();
        List<Module> modules = config.getModule();
        try
        {
            serializer.setOutput(writer);
            serializer.startDocument("UTF-8",true);
            serializer.startTag("","project");
            serializer.text(projectName);
            serializer.startTag("","modules");
            for(int i = 0;i < modules.size();i++){
                serializer.startTag("","module");
                serializer.text(modules.get(i).getName());

                //state
                serializer.startTag("","state");
                serializer.text((modules.get(i).getState()==1? "pass":"failure"));
                serializer.endTag("","state");
                //functions
                serializer.startTag("","functions");
                //function
                List<Function> funcs = modules.get(i).getFunctions();
                for(int j = 0;j < funcs.size();j++){
                    serializer.startTag("","function");
                    serializer.text(funcs.get(j).getName());
                    //function state
                    serializer.startTag("","state");
                    serializer.text((funcs.get(j).getState()==1? "pass":"failure"));
                    serializer.endTag("","state");
                    //function message
                    serializer.startTag("","message");
                    serializer.text(funcs.get(j).getTag());
                    serializer.endTag("","message");
                    serializer.endTag("","function");
                }
                serializer.endTag("","functions");
                serializer.endTag("","module");
            }
            serializer.endTag("","modules");
            serializer.endTag("","project");
            serializer.endDocument();
            android.util.Log.d(TAG, "createResultXML: "+writer.toString());
//            return writer.toString();
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }

}
