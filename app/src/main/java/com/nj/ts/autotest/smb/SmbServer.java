package com.nj.ts.autotest.smb;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;

public class SmbServer extends IntentService{

    private static final String TAG = "SmbServer";

    public static final String ACTION_UPLOAD_FILE = "smb_upload_file";
    public static final String ACTION_DOWNLOAD_FILE = "smb_download_file";
    public static final String ACTION_DOWNLOAD_CONFIG_SUCCESS = "action_download_config_success";
    public static final String ACTION_DOWNLOAD_CONFIG_FAILED = "action_download_config_failed";

    public static final String STATUS_TYPE_SERVICE = "status_type_service";
    public static final String STATUS_TYPE_THREAD = "status_type_thread";

    public static void downloadConfigFile(Context context, String configFileName) {
        if (TextUtils.isEmpty(configFileName)) return;

        Intent startIntent = new Intent(context, SmbServer.class);
        startIntent.setAction(SmbServer.ACTION_DOWNLOAD_FILE);
        startIntent.putExtra("urlLocal", SmbConfig.getInstance().getDownloadPath() + configFileName);
        startIntent.putExtra("urlSmb", SmbConfig.getInstance().getConfigUrl());
        context.startService(startIntent);
    }

    public static void downloadFile(Context context, String smbUrl, String fileDest) {
        if (TextUtils.isEmpty(fileDest) || TextUtils.isEmpty(smbUrl)) return;

        Intent startIntent = new Intent(context, SmbServer.class);
        startIntent.setAction(SmbServer.ACTION_DOWNLOAD_FILE);
        startIntent.putExtra("urlLocal", fileDest);
        startIntent.putExtra("urlSmb", smbUrl);
        context.startService(startIntent);
    }

    public static void uploadFile(Context context, String file, String smbUrl) {
        if (TextUtils.isEmpty(file)) return;

        Intent startIntent = new Intent(context, SmbServer.class);
        startIntent.setAction(SmbServer.ACTION_UPLOAD_FILE);
        startIntent.putExtra("urlLocal", file);
        startIntent.putExtra("urlSmb", smbUrl);
        context.startService(startIntent);
    }

    public enum StatusService {
        STARTED, STOPPED
    }

    public enum StatusThread {
        STARTED, RUNNING, STOPPED
    }

    private LocalBroadcastManager mLBM;

    public SmbServer() {
        super("SmbServer");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mLBM = LocalBroadcastManager.getInstance(this);
        sendServiceStatus(StatusService.STARTED);
    }

    private boolean downloadFile(String urlSmb, String urlLocal) {
        try {
            SmbFile smbFile = new SmbFile(urlSmb);
            if (!smbFile.exists()) {
                Log.d(TAG, "downloadFile: !smbFile.exists()");
                return false;
            }
            if (smbFile.isDirectory()) {
                Log.d(TAG, "downloadFile: smbFile.isDirectory()");
                return false;
            }
            InputStream smbIn = new BufferedInputStream(new SmbFileInputStream(smbFile));
            Log.d(TAG, "downloadFile: smbFile = " + urlSmb);
            File localFile = new File(urlLocal);
            if (!localFile.exists()) {
                Log.d(TAG, "downloadFile: !localFile.exists()");
                localFile.createNewFile();
            }
            Log.d(TAG, "downloadFile:  last modifidate = " + smbFile.getLastModified());
            FileOutputStream fout = new FileOutputStream(localFile);
            byte[] b = new byte[1024];
            int n;
            while((n=smbIn.read(b))!=-1){
                fout.write(b, 0, n);
            }
            smbIn.close();
            fout.close();
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.d(TAG, "downloadFile: \n" + e.getLocalizedMessage());
        } catch (SmbException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     *
     * @param urlLocal the absolute path of local file
     * @param urlSmb the url of file shared. e.g. smb://192.168.191.1/share/AutoTest/lala/lala/hello1.xml
     * @return success is true, otherwise is false
     */
    private boolean uploadFile(String urlLocal, String urlSmb) {
        try {
            File localFile = new File(urlLocal);
            if (!localFile.exists()) {
                return false;
            }
            if (localFile.isDirectory()) {
                return false;
            }
            FileInputStream localIn = new FileInputStream(localFile);
            SmbFile smbFile = new SmbFile(urlSmb);
            Log.d(TAG, "uploadFileTo: urlsmb = " + urlSmb);
            if (!smbFile.exists()) {
                int index = urlSmb.lastIndexOf('/');
                String urlSmbDir = urlSmb.substring(0, index);
                SmbFile dirs = new SmbFile(urlSmbDir);
                if (!dirs.exists()) {
                    dirs.mkdirs();
                }
                smbFile.createNewFile();
            }
            SmbFileOutputStream smbFout = new SmbFileOutputStream(smbFile);
            byte[] b = new byte[1024];
            int n;
            while((n=localIn.read(b))!=-1){
                smbFout.write(b, 0, n);
            }
            localIn.close();
            smbFout.close();
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (SmbException e) {
            Log.d(TAG, "uploadFile: e.getMessage = " + e.getLocalizedMessage() + " e  = " + e.getRootCause());
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null) {
            return;
        }
        Log.d(TAG, "onHandleIntent: " + SmbConfig.getInstance().getConfigUrl());
        String action = intent.getAction();
        String urlLocal = intent.getExtras().getString("urlLocal");
        String urlSmb = intent.getExtras().getString("urlSmb");
        switch (action) {
            case ACTION_UPLOAD_FILE:
                uploadFile(urlLocal, urlSmb);
                break;
            case ACTION_DOWNLOAD_FILE: {
                boolean result = downloadFile(urlSmb, urlLocal);
                if (result) {
                    sendBroadcast(new Intent(ACTION_DOWNLOAD_CONFIG_SUCCESS));
                } else {
                    sendBroadcast(new Intent(ACTION_DOWNLOAD_CONFIG_FAILED));
                }
            }

                break;
            default:
                break;
        }
    }

    public void sendServiceStatus(StatusService status) {
        Intent intent = new Intent(STATUS_TYPE_SERVICE);
        intent.putExtra("status", status);
        mLBM.sendBroadcast(intent);
    }
    public void sendThreadStatus(String status) {
        Intent intent = new Intent(STATUS_TYPE_THREAD);
        intent.putExtra("status", status);
        mLBM.sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sendServiceStatus(StatusService.STOPPED);
    }
}
