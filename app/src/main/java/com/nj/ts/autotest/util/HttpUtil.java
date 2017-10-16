package com.nj.ts.autotest.util;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ts on 17-10-10.
 */

public class HttpUtil {
    public static void getBack(String url,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient.Builder()
                .build();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(callback);
    }

    public static String get(String url) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient.Builder()
                .build();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        String responseText;
        if(response.isSuccessful())
            responseText= response.body().string();
        else
            responseText="error";
        return responseText;
    }
}
