package com.nj.ts.autotest.util;

import android.util.Log;

import com.nj.ts.autotest.entity.Function;
import com.nj.ts.autotest.entity.Module;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ts on 17-9-28.
 */

public class Info {
    private static final String TAG = "Info";
    public static List<Function> funcs = new ArrayList<>();
    public static int currentResult;
    public static String resultMessage;
    public static int currentModuleIndex;
    public static int currentMethodIndex;

    public static void sendResult( boolean result, String message) {
        Function func = new Function();
//        func.setName(functionName);
        func.setTag(message);
        resultMessage = message;
        if (result) {
            func.setState(1);
            currentResult = 1;
        } else {
            func.setState(0);
            currentResult = 0;
        }
        funcs.add(func);
        Log.d(TAG, "currentModuleIndex = " + currentModuleIndex
                + " currentMethodIndex = " + currentMethodIndex);
        updateConfigOfMethod(currentResult, resultMessage);
    }

    public static void updateConfigOfMethod(int state, String message) {
        Log.d(TAG, "Method state before is : "+Util.getConfig().getModule()
                .get(currentModuleIndex).getFunctions().get(currentMethodIndex).getState());
        Function function = Util.getConfig().getModule()
                .get(currentModuleIndex).getFunctions().get(currentMethodIndex);
        function.setTag(message);
        function.setState(state);
        Log.d(TAG, "Method state after is : "+Util.getConfig().getModule()
                .get(currentModuleIndex).getFunctions().get(currentMethodIndex).getState());
    }

    public static void updateConfigOfModule(int state) {
        Log.d(TAG, "Module state before is : "+Util.getConfig().getModule().get(currentModuleIndex).getState());
        Module module = Util.getConfig().getModule().get(currentModuleIndex);
        module.setState(state);
        Log.d(TAG, "Module state after is :"+Util.getConfig().getModule().get(currentModuleIndex).getState());
    }

    public static List getFuncs() {
        return funcs;
    }

    public static void clearFuncs() {
        funcs.clear();
    }

}
