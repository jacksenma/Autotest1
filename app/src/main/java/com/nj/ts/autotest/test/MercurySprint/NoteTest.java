package com.nj.ts.autotest.test.MercurySprint;

import android.util.Log;

import com.nj.ts.autotest.util.Info;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ts on 17-9-29.
 */

public class NoteTest {

    private static final String TAG = "TestNoteA";

    public List<Object> newFile() {
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                for (int k = 0; k < 100; k++) {

                }
            }
        }
        List<Object> o = new ArrayList<>();
        o.add(true);
        o.add("测试NA1");
        Info.sendResult(false, "新建文件失败:没有权限");
        //测试代码
        return o;
    }

    public void deleteFile() {
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                for (int k = 0; k < 100; k++) {

                }
            }
        }
        List<Object> o = new ArrayList<>();
        o.add(true);
        o.add("测试NA2");
        Info.sendResult(true, "删除文件");
        //测试代码
    }

    public void saveFile() {
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                for (int k = 0; k < 100; k++) {

                }
            }
        }
        List<Object> o = new ArrayList<>();
        o.add(false);
        o.add("测试NA3哈哈哈");
        Info.sendResult(true, "保存文件");
        Log.d(TAG, "NA3: lalala");
        //测试代码
    }

    private String function(String a) {
        return a + "c";
    }

}
