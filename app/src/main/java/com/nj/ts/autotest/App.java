package com.nj.ts.autotest;

import android.app.Application;
import android.content.Context;

/**
 * Created by root on 10/17/17.
 */

public class App extends Application {

    public static App sApp;

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
    }

    public static Context getContext() {
        return sApp.getApplicationContext();
    }
}
