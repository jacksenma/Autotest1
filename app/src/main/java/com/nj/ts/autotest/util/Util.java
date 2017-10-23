package com.nj.ts.autotest.util;

import java.util.ArrayList;

public class Util {
    private static final String TAG = "Util";

    /**
     * 将ArrayList<String> arrayList 用spliter 分割符拼接
     *
     * @param arrayList
     * @param spliter
     * @return
     */
    public static String arrayListToString(ArrayList<String> arrayList, String spliter) {
        String result = "";
        if (null != arrayList && !arrayList.isEmpty()) {
            for (int i = 0; i < arrayList.size(); i++) {
                if (i == (arrayList.size() - 1)) {
                    result += arrayList.get(i);
                } else {
                    result += (arrayList.get(i) + spliter);
                }
            }
        }
        return result;
    }

}
