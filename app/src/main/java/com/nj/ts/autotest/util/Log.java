package com.nj.ts.autotest.util;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ts on 17-10-10.
 */

public class Log {
    //保存log日志
    private static final String TAG = "Log";
    private static FileOutputStream fos;
    public static void saveLog(String message){
        android.util.Log.d(TAG, "saveLog1: "+getSdCardPath());
        try {
            ArrayList<String> getLog = new ArrayList<String>();
            getLog.add("logcat");
            getLog.add("-d");
            getLog.add("-v");
            getLog.add("time");
            ArrayList<String> clearLog = new ArrayList<String>();
            clearLog.add("logcat");
            clearLog.add("-c");

            Process process = Runtime.getRuntime().exec(getLog.toArray(new String[getLog.size()]));
            BufferedReader buffRead = new BufferedReader(new InputStreamReader(process.getInputStream()));
            Runtime.getRuntime().exec(clearLog.toArray(new String[clearLog.size()]));
            String str = null;
            File logFile = new File(Environment.getExternalStorageDirectory(),message+"_"+getTime()+".txt");//打开文件
            if(!logFile.exists()){
                logFile.createNewFile();
            }
            fos = new FileOutputStream(logFile,true);//true表示在写的时候在文件末尾追加
            String newline = System.getProperty("line.separator");//换行的字符串
            while((str=buffRead.readLine())!=null){//循环读取每一行

                fos.write(str.getBytes());
                fos.write(newline.getBytes());//换行
            }
            fos.close();
            fos = null;
            Runtime.getRuntime().exec(clearLog.toArray(new String[clearLog.size()]));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * 判断SDCard是否存在 [当没有外挂SD卡时，内置ROM也被识别为存在sd卡]
     *
     * @return
     */
    public static boolean isSdCardExist() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取SD卡根目录路径
     *
     * @return
     */
    public static String getSdCardPath() {
        boolean exist = isSdCardExist();
        String sdpath = "";
        if (exist) {
            sdpath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath();
        } else {
            sdpath = "不适用";
        }
        android.util.Log.d(TAG, "getSdCardPath: "+sdpath);
        return sdpath;

    }

    public static String getTime(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd#HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String time = format.format(date);
        return time;
    }



}
